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
		return 0;
	}

	protected int my_findVM(String[] architecture, Task task) {
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
		int phase = -1;
		System.out.println("Task : " + task.getId() + ", orchestratore e' " + task.getOrchestrator().getType());

		//I cannot get information about the history of the task, so I discern the phases according to the
		//	type of the orchestrator
		if (task.getOrchestrator().getType().equals(SimulationParameters.TYPES.EDGE_DATACENTER)){
			if (((LeaderEdgeDevice) task.getOrchestrator()).isLeader){
				phase=1;
			}
			else phase=0;
		}
		else if (task.getOrchestrator().getType().equals(SimulationParameters.TYPES.CLOUD)) phase=2;

		//According to the phase of leadering different host are evaluated
		switch (phase){
			//In phase 0 the orchestrator search the VM among its hosts
			case 0:
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
				//If I didn't find a VM and the orchestrator has a leader the new orchestrator became this leader
				if (vm < 0 && ((LeaderEdgeDevice) task.getOrchestrator()).getLeader() != null){
					task.setOrchestrator(((LeaderEdgeDevice) task.getOrchestrator()).getLeader());
					vm=-2;
				}
				break;

			//TODO IN CASE OF CHAINING LET B OFFLOAD TO ITS SUBORDINATES THEN C
			case 1:
				//Cycle through all the orchestrator's leader's hosts and VMs
				for (Host host_el: task.getOrchestrator().getHostList()) {
					for (Vm vm_el : host_el.getVmList()){
						if (offloadingIsPossible(task, vm_el, architecture)
								//custom conditions can be set here
								&& task.getLength()/vm_el.getMips()<task.getMaxLatency()/10000

						){
							vm = vmList.indexOf(vm_el);
							System.err.println("Offload su leader " + vm_el.getHost().getDatacenter().getName());
						}
					}
				}
				// If I don't find a suitable VM in the leader I search in every subordinate
				// I assume to have an omniscent leader
				if (vm < 0){
					List<LeaderEdgeDevice>  subordinate = ((LeaderEdgeDevice) task.getOrchestrator()).subordinates;
					if (!subordinate.isEmpty()){
						for (LeaderEdgeDevice sub: subordinate) {
							//Avoid checking again on the orchestrator
							//	I can no longer do this check
							//if (sub != task.getOrchestrator()) {
								for (Host host_el : sub.getHostList()) {
									for (Vm vm_el : host_el.getVmList()) {
										if (offloadingIsPossible(task, vm_el, architecture)
												//custom conditions can be set here
												&& task.getLength() / vm_el.getMips() < task.getMaxLatency()/10000

										) {
											vm = vmList.indexOf(vm_el);
											System.err.println("Offload su subordinates" + vm_el.getHost().getDatacenter().getName());
										}
									}
								}
							//}
						}
					}
					if (vm < 0){
						vm = -3;
						//I assume that only one Cloud Data Center is orchestrator
						for (DataCenter dc : this.simulationManager.getServersManager().getOrchestratorsList()) {
							if (dc.getType().equals(SimulationParameters.TYPES.CLOUD) && dc.isOrchestrator()) {
								task.setOrchestrator(dc);
							}
						}
					}
				}
				break;

			case 2:
				String[] Architecture = { "Cloud" };
				System.out.println("Offload su Cloud");
				vm = increseLifetime(Architecture, task);
		}

		return vm;
		/*
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
	public int my_initialize(Task task) {
		int vmfound=-1;
		if ("CLOUD_ONLY".equals(architecture)) {
			vmfound=cloudOnly(task);
		} else if ("MIST_ONLY".equals(architecture)) {
			vmfound=mistOnly(task);
		} else if ("EDGE_AND_CLOUD".equals(architecture)) {
			vmfound=edgeAndCloud(task);
		} else if ("ALL".equals(architecture)) {
			vmfound=all(task);
		} else if ("EDGE_ONLY".equals(architecture)) {
			vmfound=edgeOnly(task);
		} else if ("MIST_AND_CLOUD".equals(architecture)) {
			vmfound=mistAndCloud(task);
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
	private int edgeAndCloud(Task task) {
		String[] Architecture = { "Cloud", "Edge" };
		SimLog.println("Sto usando il mio");
		int vmfound;
		vmfound=my_findVM(Architecture, task);
		if (vmfound>=0){
			assignTaskToVm(vmfound, task);
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
}
