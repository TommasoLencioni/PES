package test;

import com.mechalikh.pureedgesim.datacentersmanager.DataCenter;
import com.mechalikh.pureedgesim.network.FileTransferProgress;
import com.mechalikh.pureedgesim.network.NetworkModel;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.simulationmanager.SimLog;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import com.mechalikh.pureedgesim.tasksgenerator.Task;

public class LeaderNetworkModel extends NetworkModel{

    public LeaderNetworkModel(SimulationManager simulationManager) {
        super(simulationManager);
    }

    public void sendResultFromDevToOrch(Task task) {
        if (task.getOrchestrator() != task.getEdgeDevice()) {
            //System.out.println("Sto usando il mio");
            task.setOrchestrator(closerNode(task));
            transferProgressList.add(new FileTransferProgress(task, task.getOutputSize() * 8,
                    FileTransferProgress.Type.RESULTS_TO_ORCH));
        }
        else
            scheduleNow(this, NetworkModel.SEND_RESULT_FROM_ORCH_TO_DEV, task);
    }

    public LeaderEdgeDevice closerNode(Task task){
        try {
            int minDist = SimulationParameters.AREA_LENGTH;
            LeaderEdgeDevice closerNode = (LeaderEdgeDevice) task.getOrchestrator();
            int tmpDist;
            int actualDistante = (int) task.getOrchestrator().getMobilityManager().distanceTo(task.getEdgeDevice());
            if (((LeaderEdgeDevice) task.getOrchestrator()).getLeader() != null) {
                for (LeaderEdgeDevice dc : ((LeaderEdgeDevice) task.getOrchestrator()).getLeader().community) {
                    tmpDist = (int) task.getEdgeDevice().getMobilityManager().distanceTo(dc);
                    //System.out.println(dc.getName() + " " + tmpDist + " rispetto agli attuali " + task.getOrchestrator().getName() + " " + actualDistante);
                    if (tmpDist < minDist) {
                        minDist = tmpDist;
                        closerNode = dc;
                    }
                }
            }
            if (!closerNode.equals(task.getOrchestrator())) {
                //System.err.println(task.getId() + " Cambio orchestratore da "+ task.getOrchestrator().getName() +" a  " + closerNode.getName());
                //task.setOrchestrator(closerNode);
                return closerNode;
            }
            else return (LeaderEdgeDevice) task.getOrchestrator();
        }
        catch (Exception e) {
            return (LeaderEdgeDevice) task.getOrchestrator();
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
            if (transfer.getTask().getOrchestrator().getType().equals(SimulationParameters.TYPES.CLOUD))
                schedule(simulationManager, SimulationParameters.WAN_PROPAGATION_DELAY + latency,
                        SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, transfer.getTask());
            else
                schedule(simulationManager, latency, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION , transfer.getTask());
            updateEnergyConsumption(transfer, "Orchestrator");
        }
        // If it is an task (or offloading request) that is sent to the destination
        else if (transfer.getTransferType() == FileTransferProgress.Type.TASK) {
            if (SimulationParameters.ENABLE_REGISTRY && "CLOUD".equals(SimulationParameters.registry_mode)
                    && !((DataCenter) transfer.getTask().getVm().getHost().getDatacenter()).getType().equals(SimulationParameters.TYPES.CLOUD)) {
                // if the registry is enabled and the task is offloaded to the edge data centers
                // or the mist nodes (edge devices),
                // then download the container

                double latency = CalculateLatency((DataCenter)transfer.getTask().getVm().getHost().getDatacenter(), transfer.getTask().getRegistry());
                transfer.getTask().setReceptionTime(simulationManager.getSimulation().clock() + latency);

                schedule(this, latency, NetworkModel.DOWNLOAD_CONTAINER, transfer.getTask());

            } else {// if the registry is disabled, execute directly the request, as it represents
                // the offloaded task in this case

                double latency = CalculateLatency((DataCenter)transfer.getTask().getVm().getHost().getDatacenter(), transfer.getTask().getOrchestrator());
                if (((DataCenter) transfer.getTask().getVm().getHost().getDatacenter()).getType().equals(SimulationParameters.TYPES.CLOUD))
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
            if (transfer.getTask().getOrchestrator().getType().equals(SimulationParameters.TYPES.CLOUD)
                    || ((DataCenter) transfer.getTask().getVm().getHost().getDatacenter()).getType().equals(SimulationParameters.TYPES.CLOUD))
                schedule(this, SimulationParameters.WAN_PROPAGATION_DELAY + latency, NetworkModel.SEND_RESULT_FROM_ORCH_TO_DEV,
                        transfer.getTask());
            else
                schedule(this,latency, NetworkModel.SEND_RESULT_FROM_ORCH_TO_DEV, transfer.getTask());
            updateEnergyConsumption(transfer, "Result_Orchestrator");
        }
        // Results transferred to the device
        else {
            if(transfer.getTask().getOrchestrator().getType()!= SimulationParameters.TYPES.CLOUD){
                double latency = CalculateLatency((DataCenter)transfer.getTask().getEdgeDevice(), transfer.getTask().getOrchestrator());
                schedule(simulationManager, latency, SimulationManager.RESULT_RETURN_FINISHED, transfer.getTask());
            }
            schedule(simulationManager, SimulationParameters.WAN_PROPAGATION_DELAY, SimulationManager.RESULT_RETURN_FINISHED, transfer.getTask());
            updateEnergyConsumption(transfer, "Result_Origin");
        }

    }
}
