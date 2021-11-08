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
package test;

import com.mechalikh.pureedgesim.datacentersmanager.DataCenter;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.simulationmanager.SimLog;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import com.mechalikh.pureedgesim.tasksgenerator.Task;
import com.mechalikh.pureedgesim.tasksorchestration.Orchestrator;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.List;
import java.util.Random;

public class LeaderEdgeOrchestrator extends Orchestrator {

	public LeaderEdgeOrchestrator(SimulationManager simulationManager) {
		super(simulationManager);
	}

	protected int findVM(String[] architecture, Task task) {
		return 0;
	}

	protected Vm my_findVM(String[] architecture, Task task) {
		if ("LEADER".equals(algorithm)) {
			if (!arrayContains(architecture, "Cloud") || !arrayContains(architecture, "Edge")) {
				SimLog.println("");
				simLog.printSameLine("At least Cloud and Edge must me specified as architecture in order to use '" + algorithm
						+ "', please check the simulation parameters file...", "red");
				// Cancel the simulation
				SimulationParameters.STOP = true;
				simulationManager.getSimulation().terminate();
			}
			return leader(architecture, task);
		}
		else if ("INCREASE_LIFETIME".equals(algorithm)) {
			int vm= increseLifetime(architecture, task);
			return vmList.get(vm);
			//return null;
		} else {
			SimLog.println("");
			SimLog.println("Default Orchestrator- Unknown orchestration algorithm '" + algorithm
					+ "', please check the simulation parameters file...");
			// Cancel the simulation
			SimulationParameters.STOP = true;
			simulationManager.getSimulation().terminate();
		}
		return null;
	}

	/***
		Get best vm for this task
		This algorith checks whether offload is possible on the orchestrator
	 		else tries on the orchestrator's leader
	 		else tries on the leader's subordinates
	 		else tries on the Cloud with the increseLifetime
	 		else it fails
	 */
	private Vm leader(String[] architecture, Task task) {
		int host=-1;
		int vm = -1;
		int minTasksCount = -1; // vm with minimum assigned tasks;
		// get best vm for this task
		for (int i=0; i< task.getOrchestrator().getHostList().size(); i++) {
			for (int j = 0; j < task.getOrchestrator().getHost(i).getVmList().size(); j++) {
				if (offloadingIsPossible(task, task.getOrchestrator().getHost(i).getVmList().get(i), architecture) && (minTasksCount == -1||
								minTasksCount > orchestrationHistory.get(vmList.indexOf(task.getOrchestrator().getHost(i).getVmList().get(j))).size())) {
					minTasksCount = orchestrationHistory.get(vmList.indexOf(task.getOrchestrator().getHost(i).getVmList().get(j))).size();
					// if this is the first time,
					// or new min found, so we choose it as the best VM
					// set the first vm as the best one
					host=i;
					vm = j;
				}
			}
		}
		/*
		int edgeDevicesCount = 0;
		double averageCpuUtilization = 0;
		double averageCloudCpuUtilization = 0;
		double averageMistCpuUtilization = 0;
		double averageEdgeCpuUtilization = 0;
		List<? extends DataCenter> datacentersList = simulationManager.getServersManager().getDatacenterList();
		for (DataCenter dc : datacentersList) {
			if (dc.getType() == SimulationParameters.TYPES.CLOUD) {
				averageCloudCpuUtilization += dc.getResources().getAvgCpuUtilization();
			} else if (dc.getType() == SimulationParameters.TYPES.EDGE_DATACENTER) {
				averageEdgeCpuUtilization += dc.getResources().getAvgCpuUtilization();
			} else if (dc.getType() == SimulationParameters.TYPES.EDGE_DEVICE && dc.getResources().getVmList().size() > 0) {
				// only devices with computing capability
				// the devices that have no VM are considered simple sensors, and will not be
				// counted here
				averageMistCpuUtilization += dc.getResources().getAvgCpuUtilization();
				edgeDevicesCount++;
			}
		}


		averageCpuUtilization = (averageCloudCpuUtilization + averageMistCpuUtilization + averageEdgeCpuUtilization)
				/ (edgeDevicesCount + SimulationParameters.NUM_OF_EDGE_DATACENTERS
				+ SimulationParameters.NUM_OF_CLOUD_DATACENTERS);
		averageCloudCpuUtilization = averageCloudCpuUtilization / SimulationParameters.NUM_OF_CLOUD_DATACENTERS;
		averageEdgeCpuUtilization = averageEdgeCpuUtilization / SimulationParameters.NUM_OF_EDGE_DATACENTERS;
		averageMistCpuUtilization = averageMistCpuUtilization / edgeDevicesCount;
		 */
		//TODO ANDAVA BENE
		//if(((LeaderEdgeDevice)task.getOrchestrator()).getLeader()!=null) {
		//	LeaderEdgeDevice old=((LeaderEdgeDevice) task.getOrchestrator()).getLeader().current_tasks.putIfAbsent(task, (LeaderEdgeDevice) task.getOrchestrator());
		//}
		//System.out.println(task.getOrchestrator().getHost(host).getVmList().get(vm).getCloudletScheduler().getCloudletExecList());
		/*
		if(task.getOrchestrator().getHost(host).getVmList().get(vm).getNumberOfPes()==8){
			System.out.println("---");
			System.out.println(task.getOrchestrator().getHost(host).getVmList().get(vm).getCloudletScheduler().getCloudletWaitingList());
			System.out.println(task.getOrchestrator().getHost(host).getVmList().get(vm).getCloudletScheduler().getCloudletExecList());
			System.out.println("---");
		}


		if(task.getOrchestrator().getHost(host).getVmList().get(vm).getCloudletScheduler().getCloudletWaitingList().size()>1) {
			System.out.println(task.getOrchestrator().getHost(host).getVmList().get(vm).getCloudletScheduler().getCloudletWaitingList().size());
		}
		 */
		//if(minTasksCount>(int)(simulationManager.getScenario().getDevicesCount()*SimulationParameters.SIMULATION_TIME/500*SimulationParameters.FACTOR)){
		if(((LeaderEdgeDevice)task.getOrchestrator()).getLeader()!=null){
			//SE NON CONTIENE (LO INSERISCO NEL LEADER) GLI FACCIO VALUTARE
			if(!((LeaderEdgeDevice)task.getOrchestrator()).getLeader().current_tasks.containsKey(task)){
				//if(((LeaderEdgeDevice)task.getOrchestrator()).getLeader().current_tasks.get(task).equals((LeaderEdgeDevice)task.getOrchestrator())){
				//if(task.getOrchestrator().getResources().getAvgCpuUtilization()>30){
				if(task.getOrchestrator().getHost(host).getVmList().get(vm).getCloudletScheduler().getCloudletWaitingList().size()>SimulationParameters.FACTOR){
					//In case of flushing the history
						//orchestrationHistory.get(vmList.indexOf(task.getOrchestrator().getHost(host).getVmList().get(vm))).clear();
						if(!task.getOrchestrator().getType().equals(SimulationParameters.TYPES.CLOUD)) return null;
					}
				}
				//else{
					//if(!task.getOrchestrator().getType().equals(SimulationParameters.TYPES.CLOUD)){
						//System.out.println(((LeaderEdgeDevice)task.getOrchestrator()).getLeader().current_tasks.get(task).getName() +
						//		"è l'originale, io sono" + task.getOrchestrator().getName());
					//}
				//}
			//}
		}

		if(!"LEADER".equals(SimulationParameters.DEPLOY_ORCHESTRATOR)) {
			if (task.getOrchestrator().getHost(host).getVmList().get(vm).getCloudletScheduler().getCloudletWaitingList().size() > SimulationParameters.FACTOR) {
				for (DataCenter dc : simulationManager.getServersManager().getDatacenterList()) {
					if (dc.getType().equals(SimulationParameters.TYPES.CLOUD)) {
						if (!SimulationParameters.CLOUD_LATENCY) task.setMaxLatency(Integer.MAX_VALUE);
						return dc.getResources().getVmList().get(0);
					}
				}
			}
		}

		//if(change) System.out.println("Offload a cloud");
		//simulationManager.scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task);
		//Try to mantain thin the HashMap by removing the tasks offloaded to the cloud
		//current_tasks.remove(task);
		//return;


		// assign the tasks to the found vm
		try{
			return task.getOrchestrator().getHost(host).getVmList().get(vm);
		}
		catch (Exception e){
			return null;
		}
	}

	protected int increseLifetime(String[] architecture, Task task) {
		int vm = -1;
		double minTasksCount = -1; // vm with minimum assigned tasks;
		double vmMips = 0;
		double weight;
		double minWeight = 20;
		// get best vm for this task
		// here trova la VM sul quale schedulare
		// cicla per ogni VM (cioe' la size dell'orchestrationHistory)
		for (int i=0; i< task.getOrchestrator().getHostList().size(); i++) {
			for (int j = 0; j < task.getOrchestrator().getHost(i).getVmList().size(); j++) {
				if (offloadingIsPossible(task, vmList.get(i), architecture)) {
					weight = getWeight(task, ((DataCenter) vmList.get(i).getHost().getDatacenter()));

					if (minTasksCount == -1) { // if it is the first iteration
						// avoid devision by 0 (by adding 1)
						minTasksCount = orchestrationHistory.get(i).size()
								- vmList.get(i).getCloudletScheduler().getCloudletFinishedList().size() + 1;
						// if this is the first time, set the first vm as the
						vm = i; // best one
						vmMips = vmList.get(i).getMips();
						minWeight = weight;
					} else if (vmMips / (minTasksCount * minWeight) < vmList.get(i).getMips()
							/ ((orchestrationHistory.get(i).size()
							- vmList.get(i).getCloudletScheduler().getCloudletFinishedList().size() + 1)
							* weight)) {
						// if this vm has more cpu mips and less waiting tasks
						minWeight = weight;
						vmMips = vmList.get(i).getMips();
						minTasksCount = orchestrationHistory.get(i).size()
								- vmList.get(i).getCloudletScheduler().getCloudletFinishedList().size() + 1;
						vm = i;
					}
				}
			}
		}
		// assign the tasks to the vm found
		return vm;
	}

	private double getWeight(Task task, DataCenter dataCenter) {
		double weight = 1;// if it is not battery powered
		if (dataCenter.getEnergyModel().isBatteryPowered()) {
			if (task.getEdgeDevice().getEnergyModel().getBatteryLevel() > dataCenter.getEnergyModel().getBatteryLevel())
				weight = 20; // the destination device has lower remaining power than the task offloading
				// device, in this case it is better not to offload
				// that's why the weight is high (20)
			else
				weight = 15; // in this case the destination has higher remaining power, so it is okey to
			// offload tasks for it, if the cloud and the edge data centers are absent.
		}
		return weight;
	}

	@Override
	public void resultsReturned(Task task) {
		//Do something when the execution results are returned
	}

	//I modified the original initialize method with the introduction of the "phase" argument in order to dinstinguish
	//	between steps of the leader algorithm
	public Vm my_initialize(Task task) {
		Vm vmfound=null;
		if ("CLOUD_ONLY".equals(architecture)) {
			SimLog.println("");
			simLog.printSameLine("Cloud and Edge must me specified as architecture in order to use LEADER algorithm, please check the simulation parameters file...", "red");
			// Cancel the simulation
			SimulationParameters.STOP = true;
			simulationManager.getSimulation().terminate();
		} else if ("MIST_ONLY".equals(architecture)) {
			SimLog.println("");
			simLog.printSameLine("Cloud and Edge must me specified as architecture in order to use LEADER algorithm, please check the simulation parameters file...", "red");
			// Cancel the simulation
			SimulationParameters.STOP = true;
			simulationManager.getSimulation().terminate();
		} else if ("EDGE_AND_CLOUD".equals(architecture)) {
			//Only good architecture
			vmfound=edgeAndCloud(task);
		} else if ("ALL".equals(architecture)) {
			SimLog.println("");
			simLog.printSameLine("Cloud and Edge must me specified as architecture in order to use LEADER algorithm, please check the simulation parameters file...", "red");
			// Cancel the simulation
			SimulationParameters.STOP = true;
			simulationManager.getSimulation().terminate();
		} else if ("EDGE_ONLY".equals(architecture)) {
			SimLog.println("");
			simLog.printSameLine("Cloud and Edge must me specified as architecture in order to use LEADER algorithm, please check the simulation parameters file...", "red");
			// Cancel the simulation
			SimulationParameters.STOP = true;
			simulationManager.getSimulation().terminate();
		} else if ("MIST_AND_CLOUD".equals(architecture)) {
			SimLog.println("");
			simLog.printSameLine("Cloud and Edge must me specified as architecture in order to use LEADER algorithm, please check the simulation parameters file...", "red");
			// Cancel the simulation
			SimulationParameters.STOP = true;
			simulationManager.getSimulation().terminate();
		}
		else {
			System.err.println("Architecture not recognized, please specify orchestration_architectures in simulation_parameters.properties");
			System.exit(-1);
		}

		return vmfound;
	}

	// If the orchestration scenario is EDGE_AND_CLOUD send Tasks only to edge data
	// centers or cloud virtual machines (vms)
	private Vm edgeAndCloud(Task task) {
		String[] Architecture = { "Cloud", "Edge" };
		Vm vmfound;
		vmfound=my_findVM(Architecture, task);
		if (vmfound!=null){
			assignTaskToVm(vmList.indexOf(vmfound), task);
		}
		return vmfound;

	}

	public boolean offloadingispossible(Task task, Vm vm, String[] architecture) {
		return offloadingIsPossible(task, vm, architecture);
	}
}
