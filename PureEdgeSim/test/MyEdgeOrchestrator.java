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

public class MyEdgeOrchestrator extends Orchestrator {

	public MyEdgeOrchestrator(SimulationManager simulationManager) {
		super(simulationManager);
	}

	protected int findVM(String[] architecture, Task task) {
		if ("MY_ALGORITHM".equals(algorithm)) {
			if (!arrayContains(architecture, "Cloud") && arrayContains(architecture, "Edge")) {
				SimLog.println("");
				simLog.printSameLine("At least Cloud and Edge must me specified as architecture in order to use '" + algorithm
						+ "', please check the simulation parameters file...", "red");
				// Cancel the simulation
				SimulationParameters.STOP = true;
				simulationManager.getSimulation().terminate();
			}
			String[] edge_first = { "Edge" };
			return myAlgorithm(edge_first, task);
		}
		if ("LEADER".equals(algorithm)) {
			if (!arrayContains(architecture, "Cloud") && arrayContains(architecture, "Edge")) {
				SimLog.println("");
				simLog.printSameLine("At least Cloud and Edge must me specified as architecture in order to use '" + algorithm
						+ "', please check the simulation parameters file...", "red");
				// Cancel the simulation
				SimulationParameters.STOP = true;
				simulationManager.getSimulation().terminate();
			}
			String[] edge_first = { "Edge" };
			return leader(edge_first, task);
		}
		else if ("INCREASE_LIFETIME".equals(algorithm)) {
			return increseLifetime(architecture, task);
		} else {
			SimLog.println("");
			SimLog.println("Default Orchestrator- Unknown orchestration algorithm '" + algorithm
					+ "', please check the simulation parameters file...");
			// Cancel the simulation
			SimulationParameters.STOP = true;
			simulationManager.getSimulation().terminate();
		}
		return -1;
	}

	private int myAlgorithm(String[] architecture, Task task) {
		int vm = -1;
		// get best vm for this task
		//my questo algoritmo controlla se l'offload e' possibile su edge e se non riesce prova su cloud
		// TODO da implementare lo scorre delle VM dei devices subordinates (vedere come si carica vmList e orcjestrationHistory)
		for (int i = 0; i < orchestrationHistory.size(); i++) {
			if (offloadingIsPossible(task, vmList.get(i), architecture)
					&& task.getLength()/vmList.get(i).getMips()<task.getMaxLatency()
					//&& (1 - vmList.get(i).getCpuPercentUtilization() * vmList.get(i).getMips()>vmList.get(i).getMips()/100)
					){
				vm = i;
				//System.out.println(vmList.get(i).getCpuPercentUtilization() + " Ho fatto l'offload su Edge");
			}
		}

		//Se non sono riuscito a trovare una VM su Edge allora la cerco su Cloud
		if (vm<0) {
			String[] Architecture = { "Cloud" };
			//System.out.println("Ho fatto l'offload su Cloud");
			return increseLifetime(Architecture, task);
		}

		// assign the tasks to the found vm
		return vm;
	}

	/***
		Get best vm for this task
		This algorith checks whether offload is possible on the orchestrator
	 		else tries on the orchestrator's leader
	 		else tries on the leader's subordinates
	 		else tries on the Cloud with the increseLifetime
	 		else it fails
	 */
	private int leader(String[] architecture, Task task) {
		int vm = -1;

		//Cycle through all the orchestrator hosts and VMs
		for (Host host_el: task.getOrchestrator().getHostList()) {
			for (Vm vm_el : host_el.getVmList()){
				if (offloadingIsPossible(task, vm_el, architecture)
						//custom conditions can be set here
						&& task.getLength()/vm_el.getMips()<task.getMaxLatency()/10000

					){
					vm = vmList.indexOf(vm_el);
					System.err.println("Offload su Orchestratore "+ vm_el.getHost().getDatacenter().getName());
				}
			}
		}

		//If the task can't be offloaded on the orchestrator then tries on the leader
		//Cycle through all the orchestrator's leader's hosts and VMs
		LeaderEdgeDevice leader=null;
		if (vm<0 && task.getOrchestrator().getType().equals(SimulationParameters.TYPES.EDGE_DATACENTER)) {
			leader = ((LeaderEdgeDevice) task.getOrchestrator()).getLeader();
			if (leader!=null){
				for (Host host_el: leader.getHostList()) {
					for (Vm vm_el : host_el.getVmList()){
						if (offloadingIsPossible(task, vm_el, architecture)
								//custom conditions can be set here
								&& task.getLength()/vm_el.getMips()<task.getMaxLatency()/1000

								){
							vm = vmList.indexOf(vm_el);
							System.err.println("Offload su leader " + vm_el.getHost().getDatacenter().getName());
						}
					}
				}
			}
		}

		//If the task can't be offloaded on the leader then tries on all the subordinates of it
		//Cycle through all the subordinates's hosts and VMs
		if (vm<0 && leader!=null) {
			List<LeaderEdgeDevice>  subordinate = leader.subordinates;
			if (!subordinate.isEmpty()){
				for (LeaderEdgeDevice sub: subordinate) {
					//Avoid checking again on the orchestrator
					if (sub != task.getOrchestrator()) {
						for (Host host_el : sub.getHostList()) {
							for (Vm vm_el : host_el.getVmList()) {
								if (offloadingIsPossible(task, vm_el, architecture)
										//custom conditions can be set here
										&& task.getLength() / vm_el.getMips() < task.getMaxLatency()

								) {
									vm = vmList.indexOf(vm_el);
									System.err.println("Offload su subordinates" + vm_el.getHost().getDatacenter().getName());
								}
							}
						}
					}
				}
			}
		}

		//If the task can't be offloaded on subordinates then it tries on Cloud with INCREASE_LIFETIME algorithm
		if (vm<0) {
			String[] Architecture = { "Cloud" };
			System.out.println("Ho fatto l'offload su Cloud");
			return increseLifetime(Architecture, task);
		}

		// assign the tasks to the found vm
		return vm;
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

}
