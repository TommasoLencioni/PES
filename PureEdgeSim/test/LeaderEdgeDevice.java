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
import com.mechalikh.pureedgesim.datacentersmanager.DefaultDataCenter;
import com.mechalikh.pureedgesim.datacentersmanager.ServersManager;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.simulationmanager.SimLog;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import com.mechalikh.pureedgesim.tasksgenerator.Task;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LeaderEdgeDevice extends DefaultDataCenter {
	private static final int COMMUNITY_DISCOVERY = 11000; // Avoid conflicting with CloudSim Plus Tags, custom tag for discover election
	private static final int TASK_SPOOL = 12000; // Avoid conflicting with CloudSim Plus Tags, custom tag for task_check election
	private static final int TASK_REMOVAL = 13000; // Avoid conflicting with CloudSim Plus Tags, custom tag for discover election
	public static final int TASK_OFFLOAD = 14000;
	public static final int TASK_EXECUTION = 15000;
	public static final int TASK_REJECTION = 16000;
	public static final int LEADER_SETTLE = 17000;
	public static final int LEADER_CONFIRMATION = 18000;
	protected LeaderEdgeDevice Orchestrator;
	protected LeaderEdgeDevice leader;
	public boolean isLeader;
	public ArrayList<LeaderEdgeDevice> community;
	public HashMap<Task, LeaderEdgeDevice> current_tasks;

	public LeaderEdgeDevice(SimulationManager simulationManager, List<? extends Host> hostList,
							List<? extends Vm> vmList) {
		super(simulationManager, hostList, vmList);
		leader=null;
		isLeader=false;
		community= new ArrayList<>();
		current_tasks=new HashMap<>();
	}

	// The clusters update will be done by scheduling events, the first event has to
	// be scheduled within the startInternal() method:
	@Override
	public void startInternal() {
		schedule(this, SimulationParameters.INITIALIZATION_TIME + 1, COMMUNITY_DISCOVERY);
		schedule(this, SimulationParameters.INITIALIZATION_TIME + 10, LEADER_SETTLE);
		schedule(this, SimulationParameters.INITIALIZATION_TIME + 20, LEADER_CONFIRMATION);
		super.startInternal();
	}

	@Override
	public void processEvent(SimEvent ev) {
		switch (ev.getTag()) {
			case COMMUNITY_DISCOVERY:
				if (this.getType() == SimulationParameters.TYPES.EDGE_DATACENTER
						&& this.isOrchestrator
						&& "LEADER".equals(SimulationParameters.DEPLOY_ORCHESTRATOR)){
					discover();
				}
				break;
			case LEADER_SETTLE:
				if (this.getType() == SimulationParameters.TYPES.EDGE_DATACENTER
						&& this.isOrchestrator
						&& "LEADER".equals(SimulationParameters.DEPLOY_ORCHESTRATOR)){
					settle();
				}
				break;
			case LEADER_CONFIRMATION:
				if (this.getType() == SimulationParameters.TYPES.EDGE_DATACENTER
						&& this.isOrchestrator
						&& "LEADER".equals(SimulationParameters.DEPLOY_ORCHESTRATOR)){
					confirmation();
				}
				break;
			case TASK_OFFLOAD:
				Task task = (Task) ev.getData();
				if (task!=null){
					LeaderEdgeDevice sub = (LeaderEdgeDevice) task.getOrchestrator();
					if (sub!=null){
						//System.out.println(this.getName() + " ricevo task "+ task.getId() +" da "+ sub.getName());
						//The second condition should be redundant but avoid bugs
						if(this.equals(sub) || community.isEmpty()){
							boolean change=false;
							//I have empty community and I cannot make the offload to my VMs, schedule to Cloud
							//tofix limited to one cloud
							for (DataCenter dc: simulationManager.getServersManager().getDatacenterList()){
								if(dc.getType().equals(SimulationParameters.TYPES.CLOUD)){
									task.setOrchestrator(dc);
									task.setMaxLatency(Integer.MAX_VALUE);
									change=true;
									break;
								}
							}
							//if(change) System.out.println("Offload a cloud");
							scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task);
							//SimLog.println("Mi e' arrivato da me perche' ho la community vuota");
							//Try to mantain thin the HashMap by removing the tasks offloaded to the cloud
							current_tasks.remove(task);
							return;
						}
						if(!community.contains(sub)) {
							//SimLog.println("");
							//SimLog.println("A task arrived to a stranger leader");
							// Cancel the simulation
							SimulationParameters.STOP = true;
							simulationManager.getSimulation().terminate();
						}
						int next;
						//If it's the first time that I see the task I save the orignal orch in an HashMap
						//	and set the next executor to the first dc in the community
						//System.out.println("-----");

						//System.out.println("Nelle curr task ho " + current_tasks.size());
						//for(Map.Entry<Task, LeaderEdgeDevice> x: current_tasks.entrySet()){
							//System.out.println(x.getKey() + " " + x.getValue());
						//}
						//System.out.println("-----");
						if(!current_tasks.containsKey(task)){
						//	System.out.println("Entro 1");
							current_tasks.put(task, sub);
							next=0;
						}
						//Otherwise I go to the next dc in the community
						else next = community.indexOf(sub)+2;
						//System.out.println("Fuori 1, next "+ next);
						//If the first dc is also the original orch I further increment by 1 the position of the next executor
						if(next>=community.size()-1){
							boolean change=false;
							//Schedule al Cloud
							//SimLog.println("Ho esaurito i datacenter ai quali proporre il task");
							//Schedule al Cloud
							//tofix make a non trivial cloud VM assignment
							for (DataCenter dc: simulationManager.getServersManager().getDatacenterList()){
								if(dc.getType().equals(SimulationParameters.TYPES.CLOUD)){
									//task.setVm(dc.getHost(0).getVmList().get(0));
									task.setOrchestrator(dc);
									change=true;
									task.setMaxLatency(Integer.MAX_VALUE);
									break;
								}
							}
							//if(change) System.out.println("Offload a cloud");
							scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task);
							//Try to mantain thin the HashMap by removing the tasks offloaded to the cloud
							current_tasks.remove(task);
							return;
						}
						LeaderEdgeDevice next_orch=community.get(next);
						if (next_orch.equals(current_tasks.get(task))){
						//if (next_orch.equals(task.getOrchestrator()){
							next = community.indexOf(sub)+1;
							//SimLog.println(this.getName() +" " + task.getId() + ", next e' "+ next + " e la dim comm e' "+ community.size());
						}
						//Todo fix boilerplate
						if(next>=community.size()-1){
							boolean change=false;
							//Schedule al Cloud
							//SimLog.println("Ho esaurito i datacenter ai quali proporre il task");
							//Schedule al Cloud
							//tofix make a non trivial cloud VM assignment
							for (DataCenter dc: simulationManager.getServersManager().getDatacenterList()){
								if(dc.getType().equals(SimulationParameters.TYPES.CLOUD)){
									//task.setVm(dc.getHost(0).getVmList().get(0));
									task.setOrchestrator(dc);
									task.setMaxLatency(Integer.MAX_VALUE);
									change=true;
									break;
								}
							}
							//if(change) System.out.println("Offload a cloud");
							scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task);
							//Try to mantain thin the HashMap by removing the tasks offloaded to the cloud
							current_tasks.remove(task);
							return;
						}
						next_orch=community.get(next);
						//System.out.println("Ora e' "+ task.getOrchestrator().getName() + " dopo sara' "+ next_orch.getName());
						task.setOrchestrator(next_orch);
						scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task);
					}
				}
				break;
			case TASK_REJECTION:
				//Here the edge datacenter should consider executing the task
				try {
					Task task2 = (Task) ev.getData();
					for (DataCenter dc: simulationManager.getServersManager().getDatacenterList())
						if (dc.getType().equals(SimulationParameters.TYPES.CLOUD)) {
							task2.setOrchestrator(dc);
							scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task2);
							System.out.println("Provo ad eseguire il task assegnatomi dal discover");
						}
				}
				catch (ClassCastException e){
					super.processEvent(ev);
				}
				break;
			default:
				super.processEvent(ev);
			break;
		}
	}

	/**
	 * Calcola la distanza euclidea dei due DataCenter
	 * @param device1
	 * @param device2
	 * @return la distanza euclidea
	 */
	private double getDistance(LeaderEdgeDevice device1, DataCenter device2) {
		return Math.abs(Math.sqrt(Math
				.pow((device1.getMobilityManager().getCurrentLocation().getXPos()
						- device2.getMobilityManager().getCurrentLocation().getXPos()), 2)
				+ Math.pow((device1.getMobilityManager().getCurrentLocation().getYPos()
						- device2.getMobilityManager().getCurrentLocation().getYPos()), 2)));
	}

	public LeaderEdgeDevice getOrchestrator() {
		return Orchestrator;
	}

	private void discover(){
		for (int i = 0; i < simulationManager.getServersManager().getDatacenterList().size(); i++) {
			DataCenter candidate = simulationManager.getServersManager().getDatacenterList().get(i);
			//Condition for evaluating a datacenter
			if ((this != candidate)
					&& candidate.getType() == SimulationParameters.TYPES.EDGE_DATACENTER
					&& candidate.isOrchestrator()
					&& (getDistance(this, candidate) <= SimulationParameters.EDGE_DATACENTERS_RANGE)) {
				community.add((LeaderEdgeDevice) candidate);
			}
		}
		community.sort(((o1, o2) -> (int) ((o2.getResources().getTotalMips()) - o1.getResources().getTotalMips())));
		if(SimulationParameters.DEBUG) {
			System.out.println(this.getName() + " i miei vicini  sono:");
			for (DataCenter dc : community) {
				System.out.println(dc.getName() + " " + dc.getResources().getTotalMips());
			}
			System.out.println("+++++");
		}
	}

	private void settle(){
		boolean should_lead=false;
		if (community.isEmpty() || this.getResources().getTotalMips()>community.get(0).getResources().getTotalMips()){
			should_lead=true;
		}
		else{
			for (LeaderEdgeDevice dc : community) {
				if (!dc.getCommunity().isEmpty() && dc.getCommunity().get(0).equals(this) && dc.getResources().getTotalMips() <= this.getResources().getTotalMips()) {
					//System.out.println(this.getName() + " dovrei essere leader");
					should_lead = true;
				}
			}
		}
		if(should_lead) {
			for (LeaderEdgeDevice dc : community) {
				//If I should be a leader and either the dc doesn't have me as first in community or the dc have more mips
				if ((!dc.getCommunity().isEmpty() && !dc.getCommunity().get(0).equals(this)) || dc.getResources().getTotalMips() >= this.getResources().getTotalMips()) {
					dc.getCommunity().remove(this);
				}
			}
		}

		if(should_lead) {
			ArrayList<LeaderEdgeDevice> newcommunity= new ArrayList<LeaderEdgeDevice>();
			for (LeaderEdgeDevice dc : community) {
				if (dc.getResources().getTotalMips() < this.getResources().getTotalMips() && (dc.getCommunity().isEmpty() || dc.getCommunity().get(0).equals(this))) {
					newcommunity.add(dc);
				}
			}
			newcommunity.sort(((o1, o2) -> (int) ((o2.getResources().getTotalMips()) - o1.getResources().getTotalMips())));
			community=newcommunity;
		}

		//Debug
		if(SimulationParameters.DEBUG) {
			System.out.println(this.getName() + " nella community ho:");
			for (DataCenter dc : community) {
				System.out.println(dc.getName() + " " + dc.getResources().getTotalMips());
			}
			System.out.println("---");
		}
	}

	private void confirmation(){
		if (community.isEmpty()){
			this.isLeader=true;
			this.setAsOrchestrator(true);
			this.simulationManager.getServersManager().getOrchestratorsList().add(this);
			//Debug
			if(SimulationParameters.DEBUG) System.out.println(this.getName() +" sono leader ma sono solo");
			return;
		}
		if (this.getResources().getTotalMips()>community.get(0).getResources().getTotalMips()){
			this.isLeader=true;
			this.setAsOrchestrator(false);
			this.simulationManager.getServersManager().getOrchestratorsList().remove(this);
			//Debug
			if(SimulationParameters.DEBUG) {
				System.out.println(this.getName() + " sono il leader di");
				for (DataCenter dc : community) {
					System.out.println(dc.getName() + " " + dc.getResources().getTotalMips());
				}
				System.out.println("######");
			}
			return;
		}
		//Debug
		if(SimulationParameters.DEBUG) {
			if (!isLeader) {
				this.leader = community.get(0);
				System.out.println(this.getName() + " con " + this.getResources().getTotalMips() + " il mio leader e' " + leader.getName() + " con " + leader.getResources().getTotalMips());
			}
			System.out.println("######");
		}
	}

	public LeaderEdgeDevice getLeader() {
		return leader;
	}

	public ArrayList<LeaderEdgeDevice> getCommunity() {
		return community;
	}


}
