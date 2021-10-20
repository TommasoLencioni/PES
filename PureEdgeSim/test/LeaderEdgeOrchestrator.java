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
			//return increseLifetime(architecture, task);
			return null;
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
				if (offloadingIsPossible(task, task.getOrchestrator().getHost(i).getVmList().get(i), architecture) && (minTasksCount == -1
						|| minTasksCount > orchestrationHistory.get(vmList.indexOf(task.getOrchestrator().getHost(i).getVmList().get(j))).size())) {
					minTasksCount = orchestrationHistory.get(vmList.indexOf(task.getOrchestrator().getHost(i).getVmList().get(j))).size();
					// if this is the first time,
					// or new min found, so we choose it as the best VM
					// set the first vm as the best one
					//System.out.println("+++" + (vmList.get(vmList.indexOf(task.getOrchestrator().getHost(i).getVmList().get(j))).getCpuPercentUtilization()));
					host=i;
					vm = j;
				}
			}
		}
		//100*10
		//System.out.println("Simul time "+ (int)(simulationManager.getScenario().getDevicesCount()*SimulationParameters.SIMULATION_TIME/600));
		if(minTasksCount>(int)(simulationManager.getScenario().getDevicesCount()*SimulationParameters.SIMULATION_TIME/500)){
		//if(minTasksCount>250){
			//orchestrationHistory.get(vmList.indexOf(task.getOrchestrator().getHost(host).getVmList().get(vm))).clear();
			if(!task.getOrchestrator().getType().equals(SimulationParameters.TYPES.CLOUD)) return null;
			//return null;
		}
		// assign the tasks to the found vm
		try{
			//System.out.println("Il minimo aveva "+ orchestrationHistory.get(vmList.indexOf(task.getOrchestrator().getHost(host).getVmList().get(vm))).size() + " tasks");
			return task.getOrchestrator().getHost(host).getVmList().get(vm);
		}
		catch (Exception e){
			return null;
		}
		/*
		Vm vm = null;
		out:
		//todo remove randomness
		if ((new Random()).nextBoolean() || (new Random()).nextBoolean()) {
		//if ((new Random()).nextBoolean()) {
			for (Host host_el : task.getOrchestrator().getHostList()) {
				//Use this to choose a VM randomly
				//Vm vm_el = host_el.getVmList().get((new Random()).nextInt(host_el.getVmList().size()));
				for (Vm vm_el : host_el.getVmList()) {
					if (offloadingIsPossible(task, vm_el, architecture)
						//Custom conditions can be set here
						//&& task.getLength()/vm_el.getMips()<task.getMaxLatency()/100

					) {
						vm = vm_el;
						break out;
					}
				}
			}
		}
		return vm;
		 */
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
		for (int i = 0; i < orchestrationHistory.size(); i++) {
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

	public Vm my_initialize2(Task task) {
		Vm vmfound=null;
		if ("CLOUD_ONLY".equals(architecture)) {
			//vmfound=cloudOnly(task);
		} else if ("MIST_ONLY".equals(architecture)) {
			//vmfound=mistOnly(task);
		} else if ("EDGE_AND_CLOUD".equals(architecture)) {
			vmfound=edgeAndCloud(task);
		} else if ("ALL".equals(architecture)) {
			//vmfound=all(task);
		} else if ("EDGE_ONLY".equals(architecture)) {
			//vmfound=edgeOnly(task);
		} else if ("MIST_AND_CLOUD".equals(architecture)) {
			//vmfound=mistAndCloud(task);
		}
		else {
			System.err.println("Architecture not recognized, please specify orchestration_architectures in simulation_parameters.properties");
			System.exit(-1);
		}

		return vmfound;
	}

	// If the orchestration scenario is MIST_ONLY send Tasks only to edge devices
	private int mistOnly(Task task) {
		String[] Architecture = { "Mist" };
		int vmfound;
		vmfound=findVM(Architecture, task);
		if (vmfound<0){
			return -1;
		}
		assignTaskToVm(vmfound, task);
		return vmfound;
	}

	// If the orchestration scenario is ClOUD_ONLY send Tasks (cloudlets) only to
	// cloud virtual machines (vms)
	private int cloudOnly(Task task) {
		String[] Architecture = { "Cloud" };
		int vmfound;
		vmfound=findVM(Architecture, task);
		if (vmfound<0){
			return -1;
		}
		assignTaskToVm(vmfound, task);
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

	// If the orchestration scenario is MIST_AND_CLOUD send Tasks only to edge
	// devices or cloud virtual machines (vms)
	private int mistAndCloud(Task task) {
		String[] Architecture = { "Cloud", "Mist" };
		int vmfound;
		vmfound=findVM(Architecture, task);
		if (vmfound<0){
			return -1;
		}
		assignTaskToVm(vmfound, task);
		return vmfound;
	}

	// If the orchestration scenario is EDGE_ONLY send Tasks only to edge data
	// centers
	private int edgeOnly(Task task) {
		String[] Architecture = { "Edge" };
		int vmfound;
		vmfound=findVM(Architecture, task);
		if (vmfound<0){
			return -1;
		}
		assignTaskToVm(vmfound, task);
		return vmfound;
	}

	// If the orchestration scenario is ALL send Tasks to any virtual machine (vm)
	// or device
	private int all(Task task) {
		String[] Architecture = { "Cloud", "Edge", "Mist" };
		int vmfound;
		vmfound=findVM(Architecture, task);
		if (vmfound<0){
			return -1;
		}
		assignTaskToVm(vmfound, task);
		return vmfound;
	}

	public boolean offloadingispossible(Task task, Vm vm, String[] architecture) {
		return offloadingIsPossible(task, vm, architecture);
	}
}
