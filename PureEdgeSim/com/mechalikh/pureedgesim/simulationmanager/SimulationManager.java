/**
 *     PureEdgeSim:  A Simulation Framework for Performance Evaluation of Cloud, Edge and Mist Computing Environments 
 *
 *     This file is part of PureEdgeSim Project.
 *
 *     PureEdgeSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     PureEdgeSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with PureEdgeSim. If not, see <http://www.gnu.org/licenses/>.
 *     
 *     @author Mechalikh
 **/
package com.mechalikh.pureedgesim.simulationmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.mechalikh.pureedgesim.datacentersmanager.DataCenter;
import com.mechalikh.pureedgesim.locationmanager.Location;
import com.mechalikh.pureedgesim.network.NetworkModel;
import com.mechalikh.pureedgesim.network.NetworkModelAbstract;
import com.mechalikh.pureedgesim.scenariomanager.Scenario;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters.TYPES;
import com.mechalikh.pureedgesim.simulationvisualizer.SimulationVisualizer;
import com.mechalikh.pureedgesim.tasksgenerator.Task;
//import test.Task;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import test.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SimulationManager extends SimulationManagerAbstract {
	public static final int Base = 1000; // avoid conflict with CloudSim Plus tags
	private static final int PRINT_LOG = Base + 1;
	private static final int SHOW_PROGRESS = Base + 2;
	public static final int EXECUTE_TASK = Base + 3;
	public static final int TRANSFER_RESULTS_TO_ORCH = Base + 4;
	public static final int RESULT_RETURN_FINISHED = Base + 5;
	public static final int SEND_TO_ORCH = Base + 6;
	public static final int UPDATE_REAL_TIME_CHARTS = Base + 7;
	public static final int SEND_TASK_FROM_ORCH_TO_DESTINATION = Base + 8;
	private int lastWrittenNumber = 0;
	private int oldProgress = -1;
	private double failedTasksCount = 0;
	private int tasksCount = 0;
	//Custom
	public static final int DEBUG = Base + 10;
	public Integer offload_to_leader = 0;
	public static ArrayList<ConcurrentLinkedDeque<Timestep>> movements;

	public SimulationManager(SimLog simLog, CloudSim simulation, int simulationId, int iteration, Scenario scenario) {
		super(simLog, simulation, simulationId, iteration, scenario);

		// Show real time results during the simulation
		if (SimulationParameters.DISPLAY_REAL_TIME_CHARTS && !SimulationParameters.PARALLEL)
			simulationVisualizer = new SimulationVisualizer(this);
	}

	// Start simulation
	public void startSimulation() {
		simLog.print("SimulationManager-  "+ scenario.toString());
		simulation.start();
	}

	@Override
	public void startInternal() {
		// Initialize logger variables
		simLog.setGeneratedTasks(tasksList.size());
		simLog.setCurrentOrchPolicy(scenario.getStringOrchArchitecture());

		simLog.print("SimulationManager- Simulation: " + getSimulationId() + "  , iteration: " + getIterationId());

		//Here per ogni task in Tasklist si controlla se gli orchestratori siano disabilitati (se cos?? fosse il device diventa orchestratore)
		// Dopo di che si schedula il task con tag SEND_TO_ORCH
		//Tasks scheduling
		for (Task task : tasksList) {
			if (!SimulationParameters.ENABLE_ORCHESTRATORS)
				task.setOrchestrator(task.getEdgeDevice());
			// Schedule the tasks offloading
			schedule(this, task.getTime(), SEND_TO_ORCH, task);
		}

		// Scheduling the end of the simulation
		schedule(this, SimulationParameters.SIMULATION_TIME, PRINT_LOG);

		// Updating real time charts
		if (SimulationParameters.DISPLAY_REAL_TIME_CHARTS && !SimulationParameters.PARALLEL)
			schedule(this, SimulationParameters.INITIALIZATION_TIME, UPDATE_REAL_TIME_CHARTS);

		// Show simulation progress
		schedule(this, SimulationParameters.INITIALIZATION_TIME, SHOW_PROGRESS);


		//schedule(this, SimulationParameters.INITIALIZATION_TIME, DEBUG);
		simLog.printSameLine("Simulation progress : [", "red");
	}

	@Override
	public void processEvent(SimEvent ev) {
		Task task = (Task) ev.getData();

		switch (ev.getTag()) {
		case SEND_TO_ORCH:
			// Send the offloading request to the closest orchestrator
			sendTaskToOrchestrator(task);
			break;

		case SEND_TASK_FROM_ORCH_TO_DESTINATION:
			// Send the request from the orchestrator to the offloading destination
			sendFromOrchToDestination(task);
			break;

		case EXECUTE_TASK:
			// Execute the task
			if (taskFailed(task, 2))
				return;
			getBroker().submitCloudlet(task);
			//Aggiorna l'uso di risorse del datacenter
			((DataCenter) task.getVm().getHost().getDatacenter()).getResources().addCpuUtilization(task);
			//Aggiorna l'uso di energia del datacenter
			((DataCenter) task.getVm().getHost().getDatacenter()).getEnergyModel()
					.updateCpuEnergyConsumption(task.getLength()
							/ ((DataCenter) task.getVm().getHost().getDatacenter()).getResources().getTotalMips());
			break;

		case TRANSFER_RESULTS_TO_ORCH:
			// Transfer the results to the orchestrator
			((DataCenter) task.getVm().getHost().getDatacenter()).getResources().removeCpuUtilization(task);
			sendResultsToOchestrator(task);
			break;

		case RESULT_RETURN_FINISHED:
			// Result returned to edge device
			if (taskFailed(task, 0))
				return;

			this.edgeOrchestrator.resultsReturned(task);
			tasksCount++;
			break;

		case SHOW_PROGRESS:
			// Calculate the simulation progress
			if (simLog.getGeneratedTasks()>0) {
				int progress = 100 * broker.getCloudletFinishedList().size() / simLog.getGeneratedTasks();
				if (oldProgress != progress) {
					oldProgress = progress;
					if (progress % 10 == 0 || (progress % 10 < 5) && lastWrittenNumber + 10 < progress) {
						lastWrittenNumber = progress - progress % 10;
						if (lastWrittenNumber != 100)
							simLog.printSameLine(" " + lastWrittenNumber + " ", "red");
					} else
						simLog.printSameLine("#", "red");
				}
				//here schedulato ogni SIMULATION_TIME / 100, semplicemente aggiorna la barra rossa di simulazione
				schedule(this, SimulationParameters.SIMULATION_TIME / 100, SHOW_PROGRESS);
			}
			break;

		case UPDATE_REAL_TIME_CHARTS:
			// Update simulation Map
			simulationVisualizer.updateCharts();
			//here schedulato ogni CHARTS_UPDATE_INTERVAL, semplicemente aggiorna i grafici della simulazione
			// Schedule the next update
			schedule(this, SimulationParameters.CHARTS_UPDATE_INTERVAL, UPDATE_REAL_TIME_CHARTS);
			break;

		case PRINT_LOG:
			// Print results when simulation is over
			List<Task> finishedTasks = broker.getCloudletFinishedList();
			// If some tasks have not been executed
			if (simLog.getGeneratedTasks() >0 && SimulationParameters.WAIT_FOR_TASKS && (tasksCount / simLog.getGeneratedTasks()) < 1) {
				// 1 = 100% , 0,9= 90%
				// Some tasks may take hours to be executed that's why we don't wait until
				// all of them get executed, but we only wait for 99% of tasks to be executed at
				// least, to end the simulation. that's why we set it to " < 0.99"
				// especially when 1% doesn't affect the simulation results that much, change
				// this value to lower ( 95% or 90%) in order to make simulation faster. however
				// this may affect the results
				//here attendo la terminazione della simulazione aspettando altri 10 secondi
				schedule(this, 10, PRINT_LOG);
				break;
			}
			//System.err.println("Mancano ancora " + (simLog.getGeneratedTasks() - tasksCount) + " da eseguire");
			simLog.printSameLine(" 100% ]", "red");

			if (SimulationParameters.DISPLAY_REAL_TIME_CHARTS && !SimulationParameters.PARALLEL) {
				// Close real time charts after the end of the simulation
				if (SimulationParameters.AUTO_CLOSE_REAL_TIME_CHARTS)
					simulationVisualizer.close();
				try {
					// Save those charts in bitmap and vector formats
					if (SimulationParameters.SAVE_CHARTS)
						simulationVisualizer.saveCharts();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// Show results and stop the simulation
			if( simLog.getGeneratedTasks()>0) simLog.showIterationResults(finishedTasks);

			// Terminate the simulation
			simulation.terminate();
			break;

		case DEBUG:
			System.out.println("+++++");
				for(DataCenter dc: serversManager.getOrchestratorsList()){
					System.out.println(dc.getName());
					if(dc.getType().equals(TYPES.EDGE_DATACENTER)){
						for(Host h: dc.getHostList()){
							for(Vm vm: h.getVmList()){
								System.out.println(vm.getCloudletScheduler().getCloudletFinishedList().size());

							}
						}

					}
				}
			System.out.println("+++++");
			schedule(this, 10, DEBUG);
			break;


		default:
			simLog.print("Unknown event type");
			break;
		}

	}

	private void sendResultsToOchestrator(Task task) {
		if (taskFailed(task, 2))
			return;
		// If the task was offloaded
		if (task.getEdgeDevice().getId() != task.getVm().getHost().getDatacenter().getId()) {
			scheduleNow(getNetworkModel(), NetworkModelAbstract.SEND_RESULT_TO_ORCH, task);

		} else { // The task has been executed locally / no offloading
			scheduleNow(this, RESULT_RETURN_FINISHED, task);
		}
		// update tasks execution and waiting delays
		simLog.getTasksExecutionInfos(task);
	}

	//Original
	/*
	private void sendFromOrchToDestination(Task task) {
		if (taskFailed(task, 1))
			return;

		// Find the best VM for executing the task
		edgeOrchestrator.initialize(task);

		// Stop in case no resource was available for this task, the offloading is
		// failed
		if (task.getVm() == Vm.NULL) {
			simLog.incrementTasksFailedLackOfRessources(task);
			tasksCount++;
			return;
		} else {
			simLog.taskSentFromOrchToDest(task);
		}

		// If the task is offloaded
		// and the orchestrator is not the offloading destination
		if (task.getEdgeDevice().getId() != task.getVm().getHost().getDatacenter().getId()
				&& task.getOrchestrator() != ((DataCenter) task.getVm().getHost().getDatacenter())) {
			scheduleNow(getNetworkModel(), NetworkModelAbstract.SEND_REQUEST_FROM_ORCH_TO_DESTINATION, task);

		} else { // The task will be executed locally / no offloading or will be executed where
			// the orchestrator is deployed (no network usage)
			scheduleNow(this, EXECUTE_TASK, task);
		}
	}
	*/

	//LEADER
	private void sendFromOrchToDestination(Task task) {
		if (taskFailed(task, 1))
			return;

		// Find the best VM for executing the task
		((LeaderEdgeOrchestrator)edgeOrchestrator).my_initialize(task);
		//edgeOrchestrator.initialize(task);
		// Stop in case no resource was available for this task, the offloading is
		// failed
		if (task.getVm() == Vm.NULL) {
			if(((LeaderEdgeDevice) task.getOrchestrator()).getLeader()!=null) {
				//System.err.println("Offload su Orchestratore "+ task.getOrchestrator().getName() + " "+ task.getOrchestrator().getType());
				scheduleNow(((LeaderEdgeDevice) task.getOrchestrator()).getLeader(), LeaderEdgeDevice.TASK_OFFLOAD, task);
				//scheduleFirst();
				return;
			}
			else if (((LeaderEdgeDevice) task.getOrchestrator()).isLeader){
				//System.err.println("Offload su Orchestratore "+ task.getOrchestrator().getName());
				scheduleNow(task.getOrchestrator(), LeaderEdgeDevice.TASK_OFFLOAD, task);
				return;
			}
			simLog.incrementTasksFailedLackOfRessources(task);
			tasksCount++;
			return;
		} else {
			simLog.taskSentFromOrchToDest(task);
		}

		// If the task is offloaded
		// and the orchestrator is not the offloading destination
		if (task.getEdgeDevice().getId() != task.getVm().getHost().getDatacenter().getId()
				&& task.getOrchestrator() != ((DataCenter) task.getVm().getHost().getDatacenter())) {
			scheduleNow(getNetworkModel(), NetworkModelAbstract.SEND_REQUEST_FROM_ORCH_TO_DESTINATION, task);

		} else { // The task will be executed locally / no offloading or will be executed where
			// the orchestrator is deployed (no network usage)

			scheduleNow(this, EXECUTE_TASK, task);
		}
	}


	private void sendTaskToOrchestrator(Task task) {
		//Check whether the edge device is in the simulation area
		// If it is not, the task count still increases, so the simulation can terminate
		//if(task.getEdgeDevice().getMobilityManager().getCurrentLocation().getXPos()<0 || task.getEdgeDevice().getMobilityManager().getCurrentLocation().getYPos()<0) {
		if(task.getEdgeDevice().isDead()) {
			//task.setStatus(Cloudlet.Status.CANCELED);
			tasksCount++;
			return;
		}
		simLog.incrementTasksSent();

		if (SimulationParameters.ENABLE_ORCHESTRATORS) {
			// Send the offloading request to the closest orchestrator
			double min = -1;
			int selected = 0;
			double distance;
			//Select as orchestrator the one closer
			for (int i = 0; i < orchestratorsList.size(); i++) {
				if (orchestratorsList.get(i).getType() != SimulationParameters.TYPES.CLOUD) {
					distance = orchestratorsList.get(i).getMobilityManager().distanceTo(task.getEdgeDevice());
					if (min == -1 || min > distance) {
						min = distance;
						selected = i;
					}
				}
			}

			if (orchestratorsList.size() == 0) {
				simLog.printSameLine("SimulationManager- Error no orchestrator found", "red");
				tasksCount++;
				return;
			}
			task.setOrchestrator(orchestratorsList.get(selected));
		}
		scheduleNow(networkModel, NetworkModelAbstract.SEND_REQUEST_FROM_DEVICE_TO_ORCH, task);
	}


	public double getFailureRate() {
		double result = (failedTasksCount * 100) / tasksList.size();
		failedTasksCount = 0;
		return result;
	}

	public boolean taskFailed(Task task, int phase) {
		// The task is failed due to long delay
		if ((task.getSimulation().clock() - task.getTime()) > task.getMaxLatency()) {
			task.setFailureReason(Task.Status.FAILED_DUE_TO_LATENCY);
			simLog.incrementTasksFailedLatency(task);
			return setFailed(task);
		}
		// task not generated because device died
		if (phase == 0 && task.getEdgeDevice().isDead()) {
			simLog.incrementNotGeneratedBeacuseDeviceDead();
			task.setFailureReason(Task.Status.NOT_GENERATED_BECAUSE_DEVICE_DEAD);
			return setFailed(task);
		}
		// Set the task as failed if the device is dead
		if (phase != 0 && task.getEdgeDevice().isDead()) {
			simLog.incrementFailedBeacauseDeviceDead(task);
			task.setFailureReason(Task.Status.FAILED_BECAUSE_DEVICE_DEAD);
			return setFailed(task);
		}
		// or if the orchestrator died
		if (phase == 1 && task.getOrchestrator() != null && task.getOrchestrator().isDead()) {
			task.setFailureReason(Task.Status.FAILED_BECAUSE_DEVICE_DEAD);
			simLog.incrementFailedBeacauseDeviceDead(task);
			return setFailed(task);
		}
		// or the destination device is dead
		if (phase == 2 && ((DataCenter) task.getVm().getHost().getDatacenter()).isDead()) {
			task.setFailureReason(Task.Status.FAILED_BECAUSE_DEVICE_DEAD);
			simLog.incrementFailedBeacauseDeviceDead(task);
			return setFailed(task);
		}

		if (!"LEADER".equals(SimulationParameters.DEPLOY_ORCHESTRATOR)) {
			if (phase == 1 && task.getOrchestrator() != null
					&& task.getOrchestrator().getType() != SimulationParameters.TYPES.CLOUD
					&& !sameLocation(task.getEdgeDevice(), task.getOrchestrator())) {
				task.setFailureReason(Task.Status.FAILED_DUE_TO_DEVICE_MOBILITY);
				simLog.incrementTasksFailedMobility(task);
				return setFailed(task);
			}
			if (phase == 2 && (task.getVm().getHost().getDatacenter()) != null
					&& ((DataCenter) task.getVm().getHost().getDatacenter()).getType() != SimulationParameters.TYPES.CLOUD
					&& (!sameLocation(task.getEdgeDevice(), ((DataCenter) task.getVm().getHost().getDatacenter())) &&
					!sameLocation(task.getEdgeDevice(), task.getOrchestrator()))) {
				task.setFailureReason(Task.Status.FAILED_DUE_TO_DEVICE_MOBILITY);
				simLog.incrementTasksFailedMobility(task);
				return setFailed(task);
			}
		} else {
			if (phase == 1 && task.getOrchestrator() != null
					&& task.getOrchestrator().getType() != SimulationParameters.TYPES.CLOUD
				//		&& !sameLocation(task.getEdgeDevice(), task.getOrchestrator())
			) {
				//((LeaderNetworkModel) getNetworkModel()).closerNode(task);
				//System.out.println("fase 1");
				boolean sameloc = false;
				if (((LeaderEdgeDevice) (task.getOrchestrator())).getLeader() != null &&
						((LeaderEdgeDevice) (task.getOrchestrator())).getLeader().current_tasks.containsKey(task)) {
					//sameloc=sameLocation(task.getEdgeDevice(), ((LeaderEdgeDevice)(task.getOrchestrator())).getLeader().current_tasks.get(task));
					sameloc = sameLocation(task.getEdgeDevice(), ((LeaderNetworkModel) getNetworkModel()).closerNode(task));
				} else sameloc = sameLocation(task.getEdgeDevice(), task.getOrchestrator());
				if (!sameloc) {
					//System.out.println("Fallisco 1");
					task.setFailureReason(Task.Status.FAILED_DUE_TO_DEVICE_MOBILITY);
					simLog.incrementTasksFailedMobility(task);
					return setFailed(task);
				}
			}
			if (phase == 2 && (task.getVm().getHost().getDatacenter()) != null
					&& ((DataCenter) task.getVm().getHost().getDatacenter()).getType() != SimulationParameters.TYPES.CLOUD
				//		&& !sameLocation(task.getEdgeDevice(), ((DataCenter) task.getVm().getHost().getDatacenter()))) {
			) {
				//((LeaderNetworkModel) getNetworkModel()).closerNode(task);
				//System.out.println("fase 2");
				boolean sameloc = false;
				if (((LeaderEdgeDevice) (task.getOrchestrator())).getLeader() != null
					//&& ((LeaderEdgeDevice)(task.getOrchestrator())).getLeader().current_tasks.containsKey(task) ){
				) {
					//sameloc=sameLocation(task.getEdgeDevice(), ((LeaderEdgeDevice)(task.getOrchestrator())).getLeader().current_tasks.get(task));
					//System.out.println("Entro qua con "+ task.getOrchestrator().getResources().getTotalMips());
					sameloc = sameLocation(task.getEdgeDevice(), ((LeaderNetworkModel) getNetworkModel()).closerNode(task));
				} else {
					//System.out.println("Entro qui con "+ task.getOrchestrator().getResources().getTotalMips());
					sameloc = sameLocation(task.getEdgeDevice(), ((DataCenter) task.getVm().getHost().getDatacenter()));
				}

				if (!sameloc) {
					task.setFailureReason(Task.Status.FAILED_DUE_TO_DEVICE_MOBILITY);
					simLog.incrementTasksFailedMobility(task);
					return setFailed(task);
				}
			}
		}
		return false;
	}

	//Original version for mobility failure
	/*
	if (phase == 1 && task.getOrchestrator() != null
				&& task.getOrchestrator().getType() != SimulationParameters.TYPES.CLOUD
				&& !sameLocation(task.getEdgeDevice(), task.getOrchestrator())) {
			task.setFailureReason(Task.Status.FAILED_DUE_TO_DEVICE_MOBILITY);
			simLog.incrementTasksFailedMobility(task);
			return setFailed(task);
		}
		if (phase == 2 && (task.getVm().getHost().getDatacenter()) != null
				&& ((DataCenter) task.getVm().getHost().getDatacenter()).getType() != SimulationParameters.TYPES.CLOUD
				&& !sameLocation(task.getEdgeDevice(), ((DataCenter) task.getVm().getHost().getDatacenter()))) {
			task.setFailureReason(Task.Status.FAILED_DUE_TO_DEVICE_MOBILITY);
			simLog.incrementTasksFailedMobility(task);
			return setFailed(task);
		}
		return false;
	}
	 */

	//My version for mobility failure
	/*
		if (phase == 1 && task.getOrchestrator() != null
				&& task.getOrchestrator().getType() != SimulationParameters.TYPES.CLOUD
			//		&& !sameLocation(task.getEdgeDevice(), task.getOrchestrator())
		){
			((LeaderNetworkModel) getNetworkModel()).closerNode(task);
			System.out.println("fase 1");
			boolean sameloc=false;
			if(((LeaderEdgeDevice)(task.getOrchestrator())).getLeader()!=null &&
					((LeaderEdgeDevice)(task.getOrchestrator())).getLeader().current_tasks.containsKey(task) ){
				sameloc=sameLocation(task.getEdgeDevice(), ((LeaderEdgeDevice)(task.getOrchestrator())).getLeader().current_tasks.get(task));
			}
			else sameloc=sameLocation(task.getEdgeDevice(), task.getOrchestrator());
			if(!sameloc) {
				System.out.println("Fallisco 1");
				task.setFailureReason(Task.Status.FAILED_DUE_TO_DEVICE_MOBILITY);
				simLog.incrementTasksFailedMobility(task);
				return setFailed(task);
			}
		}
		if (phase == 2 && (task.getVm().getHost().getDatacenter()) != null
				&& ((DataCenter) task.getVm().getHost().getDatacenter()).getType() != SimulationParameters.TYPES.CLOUD
			//		&& !sameLocation(task.getEdgeDevice(), ((DataCenter) task.getVm().getHost().getDatacenter()))) {
		){
			((LeaderNetworkModel) getNetworkModel()).closerNode(task);
			System.out.println("fase ");
			boolean sameloc=false;
			if(((LeaderEdgeDevice)(task.getOrchestrator())).getLeader()!=null &&
					((LeaderEdgeDevice)(task.getOrchestrator())).getLeader().current_tasks.containsKey(task) ){
				sameloc=sameLocation(task.getEdgeDevice(), ((LeaderEdgeDevice)(task.getOrchestrator())).getLeader().current_tasks.get(task));
			}
			else sameloc=sameLocation(task.getEdgeDevice(), ((DataCenter) task.getVm().getHost().getDatacenter()));

			if(!sameloc) {
				System.out.println("Fallisco 2");
				task.setFailureReason(Task.Status.FAILED_DUE_TO_DEVICE_MOBILITY);
				simLog.incrementTasksFailedMobility(task);
				return setFailed(task);
			}
		}
		return false;
	}

	*/

	private boolean setFailed(Task task) {
		failedTasksCount++;
		tasksCount++;
		this.edgeOrchestrator.resultsReturned(task);
		return true;
	}

	private boolean sameLocation(DataCenter Dev1, DataCenter Dev2) {
		if (Dev1.getType() == TYPES.CLOUD || Dev2.getType() == TYPES.CLOUD)
			return true;
		double distance = Dev1.getMobilityManager().distanceTo(Dev2);
		int RANGE = SimulationParameters.EDGE_DEVICES_RANGE;

		if(SimulationParameters.MIN_EDGE_DC_COVERAGE!=null){
			if(Dev1.getType()==TYPES.EDGE_DATACENTER){
				RANGE=((LeaderEdgeDevice)Dev1).range;
			}
		}
		else {
			if (Dev1.getType() != Dev2.getType()) // One of them is an edge data center and the other is an edge device
				RANGE = SimulationParameters.EDGE_DATACENTERS_RANGE;
		}

		return (distance < RANGE);
	}

	static void routeXML(){
		try {
			File devicesFile = new File(MyMain.settingsPath+"/sim-output.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(devicesFile);
			doc.getDocumentElement().normalize();
			NodeList timesteps = doc.getElementsByTagName("timestep");
			//System.out.println("Sono");
			//System.out.println(timestamps.item(0).getTextContent());
			//System.out.println(timestamps.getLength());
			//System.out.println("dentro");

			for (int i = 0; i < timesteps.getLength(); i++) {
				Node step_node = timesteps.item(i);


				if (step_node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) step_node;
					//System.out.println("Sono");
					//System.out.println(element.getNam);
					//System.out.println("dentro");
					NodeList vehiclesNodeList = element.getElementsByTagName("vehicle");
					//System.out.println(vehiclesNodeList.item(0).getTextContent());

					for (int j = 0; j < vehiclesNodeList.getLength(); j++) {
						Node vehinode = vehiclesNodeList.item(j);
						int step_time = (int)Float.parseFloat(step_node.getAttributes().getNamedItem("time").getTextContent());
						int id = (int)Float.parseFloat(vehinode.getAttributes().getNamedItem("id").getTextContent());
						int x = (int)Float.parseFloat(vehinode.getAttributes().getNamedItem("x").getTextContent());
						int y = (int)Float.parseFloat(vehinode.getAttributes().getNamedItem("y").getTextContent());
						Timestep ts = new Timestep(step_time, new Location(x,y));
						//System.out.println("Tempo: "+ step_time + ", id: "+ id +" , x: " + x +" , y: "+ y);
						//movements.get(id).put(ts);
						movements.get(id).add(ts);
					}

				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
        /*
        int i=0;
        for(ConcurrentLinkedDeque<Timestep> el: movements){
            while (!el.isEmpty()){
                Timestep ts= (Timestep) el.poll();
                System.out.println("Tempo: "+ ts.getTime() + ", id: "+ i +" , x: " + ts.getLocation().getXPos() +" , y: "+ ts.getLocation().getYPos());
            }
            i++;
        }

         */
	}
}
