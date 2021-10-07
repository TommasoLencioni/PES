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
	protected boolean isLeader;
	public List<LeaderEdgeDevice> subordinates;
	public List<LeaderEdgeDevice> cluster;
	public ArrayList<LeaderEdgeDevice> community;
	//public PriorityQueue <Map.Entry<Task, Integer>> current_tasks;
	//public ArrayList <Map.Entry<Task, Integer>> current_tasks;
	public ConcurrentHashMap<Task, LeaderEdgeDevice> current_tasks;
	public PriorityQueue <Map.Entry<Task, Integer>> dev_in_range;

	public LeaderEdgeDevice(SimulationManager simulationManager, List<? extends Host> hostList,
							List<? extends Vm> vmList) {
		super(simulationManager, hostList, vmList);
		subordinates = new ArrayList<LeaderEdgeDevice>();
		leader=null;
		isLeader=false;
		current_tasks=new ConcurrentHashMap<>();
		dev_in_range=new PriorityQueue<Map.Entry<Task, Integer>>();
		community= new ArrayList<>();
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
						&& "LEADER".equals(SimulationParameters.DEPLOY_ORCHESTRATOR)){
					confirmation();
				}
				break;
			case TASK_OFFLOAD:
				//Add the task to the current task hash map of my discover if i'm not able to execute it
				Task task = (Task) ev.getData();
				if (task!=null){
					LeaderEdgeDevice sub = (LeaderEdgeDevice) task.getOrchestrator();
					if (sub!=null){
						System.out.println(this.getName() + " ricevo un task da "+ sub.getName());
						if(this.equals(sub)){
							//Schedule al Cloud
							//tofix
							for (DataCenter dc: simulationManager.getServersManager().getDatacenterList()){
								if(dc.getType().equals(SimulationParameters.TYPES.CLOUD)){
									task.setVm(dc.getHost(0).getVmList().get(0));
									break;
								}
							}
							scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task);
							SimLog.println("Mi e' arrivato da me perche' ho la community vuota");
							return;
						}
						if(!community.contains(sub)) {
							SimLog.println("");
							SimLog.println("A task arrived to a stranger leader");
							// Cancel the simulation
							SimulationParameters.STOP = true;
							simulationManager.getSimulation().terminate();
						}
						int next = community.indexOf(sub)+1;
						if(next==community.size()){
							//Schedule al Cloud
							SimLog.println("Ho esaurito i datacenter ai quali proporre il task");
							//Schedule al Cloud
							//tofix
							for (DataCenter dc: simulationManager.getServersManager().getDatacenterList()){
								if(dc.getType().equals(SimulationParameters.TYPES.CLOUD)){
									task.setVm(dc.getHost(0).getVmList().get(0));
									break;
								}
							}
							scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task);
							return;
						}
						LeaderEdgeDevice next_orch=community.get(next);
						task.setOrchestrator(next_orch);
						scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_ORCH_TO_DESTINATION, task);
					}
				}
				break;
				/*
			case TASK_SPOOL:
				//todo tofix
				if (isLeader){
					synchronized (current_tasks){
						System.out.println("Sono il discover "+ this.getName() + " e ho "+ current_tasks.size() +"  tasks");

						for (Task t: current_tasks.values()){
							System.out.println(t.getId());
						}


						for(int i=0; i<current_tasks.size(); i++){
							//Map.Entry<Task, Integer> tmp_task = current_tasks.remove(i);
							scheduleNow(subordinates.get(ThreadLocalRandom.current().nextInt(0, subordinates.size())), TASK_EXECUTION, current_tasks.remove(i).getKey());
						}
						System.out.println("---");
					}
					schedule(this, 20, TASK_SPOOL);
				}
				break;

				 */
			case TASK_EXECUTION:
				//Here the edge datacenter should consider executing the task
				try {
					Task task1 = (Task) ev.getData();
					if (task1 != null && !isLeader) {
						task1.setOrchestrator(this);
						scheduleNow(simulationManager, SimulationManager.SEND_TASK_FROM_LEADER_TO_SUBORDINATE, task1);
						System.out.println("Provo ad eseguire il task assegnatomi dal discover");
					}
				}
				catch (ClassCastException e){
					super.processEvent(ev);
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
					&& (getDistance(this, candidate) <= SimulationParameters.EDGE_DATACENTERS_RANGE)) {
				community.add((LeaderEdgeDevice) candidate);
			}
		}
		community.sort(((o1, o2) -> (int) ((o2.getResources().getTotalMips()) - o1.getResources().getTotalMips())));
		System.out.println(this.getName() +" i miei vicini  sono:");
		for (DataCenter dc: community){
			System.out.println(dc.getName() + " "+ dc.getResources().getTotalMips());
		}
		System.out.println("+++++");
	}

	private void settle(){
		boolean should_lead=false;
		if (community.isEmpty() || this.getResources().getTotalMips()>community.get(0).getResources().getTotalMips()){
			should_lead=true;
		}
		else{
			for (LeaderEdgeDevice dc : community) {
				if (!dc.getCommunity().isEmpty() && dc.getCommunity().get(0).equals(this) && dc.getResources().getTotalMips() <= this.getResources().getTotalMips()) {
					System.out.println(this.getName() + " dovrei essere leader");
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
		System.out.println(this.getName() +" nella community ho:");
		for (DataCenter dc: community){
			System.out.println(dc.getName() + " "+ dc.getResources().getTotalMips());
		}
		System.out.println("---");
	}

	private void confirmation(){
		if (community.isEmpty()){
			this.isLeader=true;
			this.setAsOrchestrator(true);
			this.simulationManager.getServersManager().getOrchestratorsList().add(this);
			//Debug
			System.out.println(this.getName() +" sono leader ma sono solo");
			return;
		}
		if (this.getResources().getTotalMips()>community.get(0).getResources().getTotalMips()){
			this.isLeader=true;
			this.setAsOrchestrator(false);
			this.simulationManager.getServersManager().getOrchestratorsList().remove(this);
			//Debug
			System.out.println(this.getName() +" sono il leader di");
			for (DataCenter dc: community){
				System.out.println(dc.getName() + " "+ dc.getResources().getTotalMips());
			}
			System.out.println("######");
			return;
		}
		//Debug
		if(!isLeader){
			this.leader=community.get(0);
			System.out.println(this.getName() +" con "+ this.getResources().getTotalMips() +" il mio leader e' "+ leader.getName() +" con "+ leader.getResources().getTotalMips());
		}

		System.out.println("######");


	}

	public LeaderEdgeDevice getLeader() {
		return leader;
	}

	public ArrayList<LeaderEdgeDevice> getCommunity() {
		return community;
	}


}
