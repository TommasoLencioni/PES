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
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import com.mechalikh.pureedgesim.tasksgenerator.Task;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LeaderEdgeDevice extends DefaultDataCenter {
	private static final int COMMUNITY_DISCOVERY = 11000; // Avoid conflicting with CloudSim Plus Tags, custom tag for discover election
	private static final int TASK_SPOOL = 12000; // Avoid conflicting with CloudSim Plus Tags, custom tag for task_check election
	private static final int TASK_REMOVAL = 13000; // Avoid conflicting with CloudSim Plus Tags, custom tag for discover election
	public static final int TASK_ADDITION = 14000;
	public static final int TASK_EXECUTION = 15000;
	public static final int TASK_REJECTION = 16000;
	public static final int LEADER_SETTLE = 17000;
	public static final int LEADER_CONFERMATION = 18000;
	protected LeaderEdgeDevice Orchestrator;
	protected LeaderEdgeDevice leader;
	protected boolean isLeader;
	public List<LeaderEdgeDevice> subordinates;
	public List<LeaderEdgeDevice> cluster;
	public ArrayList<LeaderEdgeDevice> community;
	//public PriorityQueue <Map.Entry<Task, Integer>> current_tasks;
	public ArrayList <Map.Entry<Task, Integer>> current_tasks;
	public PriorityQueue <Map.Entry<Task, Integer>> dev_in_range;

	public LeaderEdgeDevice(SimulationManager simulationManager, List<? extends Host> hostList,
							List<? extends Vm> vmList) {
		super(simulationManager, hostList, vmList);
		subordinates = new ArrayList<LeaderEdgeDevice>();
		leader=null;
		isLeader=false;
		current_tasks=new ArrayList<>();
		dev_in_range=new PriorityQueue<Map.Entry<Task, Integer>>();
		community= new ArrayList<>();
	}

	// The clusters update will be done by scheduling events, the first event has to
	// be scheduled within the startInternal() method:
	@Override
	public void startInternal() {
		schedule(this, SimulationParameters.INITIALIZATION_TIME + 1, COMMUNITY_DISCOVERY);
		schedule(this, SimulationParameters.INITIALIZATION_TIME + 10, LEADER_SETTLE);
		schedule(this, SimulationParameters.INITIALIZATION_TIME + 20, LEADER_CONFERMATION);
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
			case LEADER_CONFERMATION:
				if (this.getType() == SimulationParameters.TYPES.EDGE_DATACENTER
						&& "LEADER".equals(SimulationParameters.DEPLOY_ORCHESTRATOR)){
					confirmation();
				}
				break;
			case TASK_ADDITION:
				//Add the task to the current task hash map of my discover if i'm not able to execute it
				Task task = (Task) ev.getData();
				if (task!=null && leader!=null && !isLeader){
					System.out.println("Inserisco perche' non lo posso eseguire");
					synchronized (leader.current_tasks) {
						java.util.Map.Entry<Task,Integer> t =new java.util.AbstractMap.SimpleEntry<Task,Integer>(task,1);
						this.leader.current_tasks.add(t);
					}
				}
				break;
			case TASK_SPOOL:
				//todo tofix
				if (isLeader){
					synchronized (current_tasks){
						System.out.println("Sono il discover "+ this.getName() + " e ho "+ current_tasks.size() +"  tasks");
						/*
						for (Task t: current_tasks.values()){
							System.out.println(t.getId());
						}

						 */

						for(int i=0; i<current_tasks.size(); i++){
							//Map.Entry<Task, Integer> tmp_task = current_tasks.remove(i);
							scheduleNow(subordinates.get(ThreadLocalRandom.current().nextInt(0, subordinates.size())), TASK_EXECUTION, current_tasks.remove(i).getKey());
						}
						System.out.println("---");
					}
					schedule(this, 20, TASK_SPOOL);
				}
				break;

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


	public void setOrchestrator(LeaderEdgeDevice newOrchestrator) {
		//this device has changed its neighborhood, so it should be removed from the previous one
		if (Orchestrator != null)
			Orchestrator.cluster.remove(this);
		
		// If the new orchestrator is another device (not this one)
		if (this != newOrchestrator) {
			//if this device is no more an orchestrator, its neighborhood will be joined with the neighborhood of the new orchestrator
			if (isOrchestrator()) {
				newOrchestrator.cluster.addAll(this.cluster);
			}
			// now remove it neighborhood after
			cluster.clear();
			//remove this device from orchestrators list
			simulationManager.getServersManager().getOrchestratorsList().remove(this);
			//System.err.println(simulationManager.getServersManager().getOrchestratorsList().size());
			//set the new orchestrator as the parent node ( a tree-like topology)
			//parent = newOrchestrator;
			// this device is no more an orchestrator so set it to false
			this.setAsOrchestrator(false);
			
			//in case the neighborhood doesn't has this device as member
			if (!newOrchestrator.cluster.contains(this))
				newOrchestrator.cluster.add(this);
		}
        // configure the new orchestrator (it can be another device, or this device)
		newOrchestrator.setAsOrchestrator(true);
		newOrchestrator.Orchestrator = newOrchestrator;
		//newOrchestrator.parent = null;
		//in case the neighborhood doesn't has the orchestrator as member
		if (!newOrchestrator.cluster.contains(newOrchestrator))
			newOrchestrator.cluster.add(newOrchestrator);
		//add the new orchestrator to the list
		if (!simulationManager.getServersManager().getOrchestratorsList().contains(newOrchestrator))
			simulationManager.getServersManager().getOrchestratorsList().add(newOrchestrator);

	}


	public LeaderEdgeDevice getOrchestrator() {
		return Orchestrator;
	}

	/*Old discover
	private void discover(){
		//Gauge for max number of MIPS
		double max_MIPS =0;
		for (int i = 0; i < simulationManager.getServersManager().getDatacenterList().size(); i++) {
			DataCenter candidate = simulationManager.getServersManager().getDatacenterList().get(i);
			//Condition for evaluating a datacenter
			if ((this!=candidate)
				&& candidate.getType() == SimulationParameters.TYPES.EDGE_DATACENTER
				&& (getDistance(this, candidate)<= SimulationParameters.EDGE_DATACENTERS_RANGE)) {
				//here debug
				//System.out.println(this.getName() + " " + this.getResources().getTotalMips() +
				//		" " + candidate.getName() + " " + candidate.getResources().getTotalMips());
				//Condition for choosing a discover

				//The election's criterion can be customized

				if (this.getResources().getTotalMips()<candidate.getResources().getTotalMips()
					&& max_MIPS <candidate.getResources().getTotalMips()){
					max_MIPS =candidate.getResources().getTotalMips();
					discover = (LeaderEdgeDevice) candidate;
				}
			}
		}
		//If I'm not the discover
		if (discover!= null) {
			//debug
			//System.out.println("Il mio discover e' " + discover.getId());

			discover.subordinates.add(this);
			discover.isLeader=true;
		}
		//If I'm the discover
		else if (this.getType()==SimulationParameters.TYPES.EDGE_DATACENTER){
			isLeader=true;
			this.setAsOrchestrator(false);
			this.simulationManager.getServersManager().getOrchestratorsList().remove(this);

		}
	}
	*/

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
		if (community.isEmpty() || this.getResources().getTotalMips()>community.get(0).getResources().getTotalMips()){
			this.isLeader=true;
			this.setAsOrchestrator(true);
			this.simulationManager.getServersManager().getOrchestratorsList().add(this);
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
