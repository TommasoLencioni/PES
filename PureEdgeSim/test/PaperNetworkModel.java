package test;

import org.cloudbus.cloudsim.core.events.SimEvent;

import com.mechalikh.pureedgesim.datacentersmanager.DataCenter;
import com.mechalikh.pureedgesim.datacentersmanager.DefaultEnergyModel;
import com.mechalikh.pureedgesim.network.FileTransferProgress;
import com.mechalikh.pureedgesim.network.NetworkModel;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters.TYPES;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import com.mechalikh.pureedgesim.tasksgenerator.Task;

public class PaperNetworkModel extends NetworkModel {
	public static final int OPTIMIZATION_REQUEST_SENT = base + 8;
	public static final int OPTIMIZATION_REQUEST_RESPONSE_SENT = base + 9;
	public static final int OPTIMIZATION_REQUEST_CONFIRMED_SENT = base + 10;
	public static final int OPTIMIZATION_REQUEST_NOT_CONFIRMED_SENT = base + 11;
	

	public PaperNetworkModel(SimulationManager simulationManager) {
		super(simulationManager);
	}

	@Override
	public void processEvent(SimEvent ev) {
		switch (ev.getTag()) {
		case SEND_REQUEST_FROM_DEVICE_TO_ORCH:
			// Send the offloading request to the orchestrator
			sendRequestFromDeviceToOrch((Task) ev.getData());
			break;
		case SEND_REQUEST_FROM_ORCH_TO_DESTINATION:
			// Forward the offloading request from orchestrator to offloading destination
			sendRequestFromOrchToDest((Task) ev.getData());
			break;
		case DOWNLOAD_CONTAINER:
			// Pull the container from the registry
			addContainer((Task) ev.getData());
			break;
		case SEND_RESULT_TO_ORCH:
			// Send the execution results to the orchestrator
			sendResultFromDevToOrch((Task) ev.getData());
			break;
		case SEND_RESULT_FROM_ORCH_TO_DEV:
			// Transfer the execution results from the orchestrators to the device
			sendResultFromOrchToDev((Task) ev.getData());
			break;
		case UPDATE_PROGRESS:
			// update the progress of the current transfers and their allocated bandwidth
			updateTasksProgress();
			schedule(this, SimulationParameters.NETWORK_UPDATE_INTERVAL, UPDATE_PROGRESS);
			break;
		default:
			break;
		}
	}
	
	private double CalculateLatency(DataCenter device1, DataCenter device2) {
		if(device1.equals(device2))
			return 0;
		return (PaperSettings.LATENCY_DELAY * device1.getMobilityManager().distanceTo(device2)) + PaperSettings.MIN_LATENCY_DELAY;
	}
	
	@Override
	protected void transferFinished(FileTransferProgress transfer) {
		// Update logger parameters
		simulationManager.getSimulationLogger().updateNetworkUsage(transfer);

		// Delete the transfer from the queue
		transferProgressList.remove(transfer);

		// If it is an offloading request that is sent to the orchestrator
		if (transfer.getTransferType() == FileTransferProgress.Type.REQUEST) {		
			double latency = CalculateLatency(transfer.getTask().getEdgeDevice(), transfer.getTask().getOrchestrator());	
			// Find the offloading destination and execute the task
			if (transfer.getTask().getOrchestrator().getType().equals(TYPES.CLOUD))
				schedule(simulationManager, SimulationParameters.WAN_PROPAGATION_DELAY + latency,
						SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, transfer.getTask());
			else
				schedule(simulationManager, latency, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION , transfer.getTask());
			updateEnergyConsumption(transfer, "Orchestrator");
		}
		// If it is an task (or offloading request) that is sent to the destination
		else if (transfer.getTransferType() == FileTransferProgress.Type.TASK) {		
			if (SimulationParameters.ENABLE_REGISTRY && "CLOUD".equals(SimulationParameters.registry_mode)
					&& !((DataCenter) transfer.getTask().getVm().getHost().getDatacenter()).getType().equals(TYPES.CLOUD)) {
				// if the registry is enabled and the task is offloaded to the edge data centers
				// or the mist nodes (edge devices),
				// then download the container
				
				double latency = CalculateLatency((DataCenter)transfer.getTask().getVm().getHost().getDatacenter(), transfer.getTask().getRegistry());
				transfer.getTask().setReceptionTime(simulationManager.getSimulation().clock() + latency);
				
				schedule(this, latency, NetworkModel.DOWNLOAD_CONTAINER, transfer.getTask());

			} else {// if the registry is disabled, execute directly the request, as it represents
					// the offloaded task in this case
			
				double latency = CalculateLatency((DataCenter)transfer.getTask().getVm().getHost().getDatacenter(), transfer.getTask().getOrchestrator());			
				if (((DataCenter) transfer.getTask().getVm().getHost().getDatacenter()).getType().equals(TYPES.CLOUD))
					schedule(simulationManager, SimulationParameters.WAN_PROPAGATION_DELAY + latency, SimulationManager.EXECUTE_TASK,
							transfer.getTask());
				else
					schedule(simulationManager, latency, SimulationManager.EXECUTE_TASK, transfer.getTask());
			}
			updateEnergyConsumption(transfer, "Destination");
		}
		
		// If the container has been downloaded, then execute the task now
		else if (transfer.getTransferType() == FileTransferProgress.Type.CONTAINER) {
			double latency = CalculateLatency((DataCenter)transfer.getTask().getVm().getHost().getDatacenter(), transfer.getTask().getOrchestrator());
			transfer.getTask().setReceptionTime(simulationManager.getSimulation().clock() + latency);
			schedule(simulationManager, latency, SimulationManager.EXECUTE_TASK, transfer.getTask());
			updateEnergyConsumption(transfer, "Container");
		}
		// If the transfer of execution results to the orchestrator has finished
		else if (transfer.getTransferType() == FileTransferProgress.Type.RESULTS_TO_ORCH) {
			// if the results are returned from the cloud, consider the wan propagation
			// delay
			double latency = CalculateLatency((DataCenter)transfer.getTask().getVm().getHost().getDatacenter(), transfer.getTask().getOrchestrator());
			if (transfer.getTask().getOrchestrator().getType().equals(TYPES.CLOUD)
					|| ((DataCenter) transfer.getTask().getVm().getHost().getDatacenter()).getType().equals(TYPES.CLOUD))
				schedule(this, SimulationParameters.WAN_PROPAGATION_DELAY + latency, NetworkModel.SEND_RESULT_FROM_ORCH_TO_DEV,
						transfer.getTask());
			else
				schedule(this,latency, NetworkModel.SEND_RESULT_FROM_ORCH_TO_DEV, transfer.getTask());
			updateEnergyConsumption(transfer, "Result_Orchestrator");
		}
		// Results transferred to the device
		else {			
			double latency = CalculateLatency((DataCenter)transfer.getTask().getEdgeDevice(), transfer.getTask().getOrchestrator());
			schedule(simulationManager, latency, SimulationManager.RESULT_RETURN_FINISHED, transfer.getTask());
			updateEnergyConsumption(transfer, "Result_Origin");
		}

	}
	
	@Override
	protected void updateEnergyConsumption(FileTransferProgress transfer, String type) {
		// update energy consumption
		if ("Orchestrator".equals(type)) {
			calculateEnergyConsumption(transfer.getTask().getEdgeDevice(), transfer.getTask().getOrchestrator(),
					transfer);
		} 
		else if ("Destination".equals(type)) {
			calculateEnergyConsumption(transfer.getTask().getOrchestrator(),
					((DataCenter) transfer.getTask().getVm().getHost().getDatacenter()), transfer);
		}
		else if ("Container".equals(type)) {
			// update the energy consumption of the registry and the device
			calculateEnergyConsumption(transfer.getTask().getRegistry(), transfer.getTask().getEdgeDevice(), transfer);
		} else if ("Result_Orchestrator".equals(type)) {
			calculateEnergyConsumption(((DataCenter) transfer.getTask().getVm().getHost().getDatacenter()),
					transfer.getTask().getOrchestrator(), transfer);
		} else if ("Result_Origin".equals(type)) {
			calculateEnergyConsumption(transfer.getTask().getOrchestrator(), transfer.getTask().getEdgeDevice(),
					transfer);
		}

	}

	@Override
	protected void updateTasksProgress() {
		// Ignore finished transfers, so we will start looping from the first index of
		// the remaining transfers
		int remainingTransfersCount_Lan;
		int remainingTransfersCount_Wan;
		for (int i = 0; i < transferProgressList.size(); i++) {
			if (transferProgressList.get(i).getRemainingFileSize() > 0) {
				remainingTransfersCount_Lan = 0;
				remainingTransfersCount_Wan = 0;
				for (int j = 0; j < transferProgressList.size(); j++) {
					if (transferProgressList.get(j).getRemainingFileSize() > 0 && j != i
							&& wanIsUsed(transferProgressList.get(j))) {
						remainingTransfersCount_Wan++;
						//bwUsage += transferProgressList.get(j).getRemainingFileSize();
					}
					if (transferProgressList.get(j).getRemainingFileSize() > 0 && j != i && sameLanIsUsed(
							transferProgressList.get(i).getTask(), transferProgressList.get(j).getTask())) {
						// Both transfers use same Lan
						remainingTransfersCount_Lan++;
					}
				}

				// allocate bandwidths
				transferProgressList.get(i).setLanBandwidth(getLanBandwidth(remainingTransfersCount_Lan));
				transferProgressList.get(i).setWanBandwidth(getWanBandwidth(remainingTransfersCount_Wan));
				updateBandwidth(transferProgressList.get(i));
				updateTransfer(transferProgressList.get(i));
			}

		}
	}
	
	@Override
	protected boolean wanIsUsed(FileTransferProgress fileTransferProgress) {
		 if(fileTransferProgress.getTask()!=null)
				return 
						((fileTransferProgress.getTransferType() == FileTransferProgress.Type.TASK
						&& ((DataCenter) fileTransferProgress.getTask().getVm().getHost().getDatacenter()).getType()
							.equals(TYPES.CLOUD))
						// If the offloading destination is the cloud

						|| (fileTransferProgress.getTransferType() == FileTransferProgress.Type.CONTAINER
									&& (fileTransferProgress.getTask().getRegistry() == null
						|| fileTransferProgress.getTask().getRegistry().getType() == TYPES.CLOUD))
						// Or if containers will be downloaded from registry

						|| (fileTransferProgress.getTask().getOrchestrator().getType() == SimulationParameters.TYPES.CLOUD));
		 				// Or if the orchestrator is deployed in the cloud
		 return false;

	}
	
	private void calculateEnergyConsumption(DataCenter origin, DataCenter destination, FileTransferProgress transfer) {
		if (origin != null) {
			origin.getEnergyModel().updatewirelessEnergyConsumption(transfer, origin, destination,
					DefaultEnergyModel.TRANSMISSION);
		}
		destination.getEnergyModel().updatewirelessEnergyConsumption(transfer, origin, destination,
				DefaultEnergyModel.RECEPTION);
	}


	//@Override
	//protected void startEntity() {
	//	schedule(this, SimulationParameters.NETWORK_UPDATE_INTERVAL, UPDATE_PROGRESS);
	//}
}
