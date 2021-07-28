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
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;

public class LeaderEdgeDevice extends DefaultDataCenter {
	private static final int LEADER_ELECTION = 11000; // Avoid conflicting with CloudSim Plus Tags, custom tag for leader election
	protected LeaderEdgeDevice Orchestrator;
	protected LeaderEdgeDevice leader;
	protected boolean isLeader;
	public List<LeaderEdgeDevice> subordinate;
	public List<LeaderEdgeDevice> cluster;

	public LeaderEdgeDevice(SimulationManager simulationManager, List<? extends Host> hostList,
							List<? extends Vm> vmList) {
		super(simulationManager, hostList, vmList);
		subordinate = new ArrayList<LeaderEdgeDevice>();
		leader=null;
		isLeader=false;
	}

	// The clusters update will be done by scheduling events, the first event has to
	// be scheduled within the startInternal() method:
	@Override
	public void startInternal() {
		schedule(this, SimulationParameters.INITIALIZATION_TIME + 1, LEADER_ELECTION);
		super.startInternal();
	}

	// The scheduled event will be processed in processEvent(). To update the
	// clusters continuously (a loop) another event has to be scheduled right after
	// processing the previous one:
	@Override
	public void processEvent(SimEvent ev) {
		//System.out.println("--- " + this.getType() + " --- " + this.Orchestrator);
		switch (ev.getTag()) {
		case LEADER_ELECTION:
			if (this.getType() == SimulationParameters.TYPES.EDGE_DATACENTER
					&& this.isOrchestrator
					&& "LEADER".equals(SimulationParameters.DEPLOY_ORCHESTRATOR)){
				leader();
			}
			break;
		default:
			//Task task = (Task) ev.getData();
			//if task.getMaxLatency()
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
		//this device has changed its cluster, so it should be removed from the previous one
		if (Orchestrator != null)
			Orchestrator.cluster.remove(this);
		
		// If the new orchestrator is another device (not this one)
		if (this != newOrchestrator) {
			//if this device is no more an orchestrator, its cluster will be joined with the cluster of the new orchestrator
			if (isOrchestrator()) {
				newOrchestrator.cluster.addAll(this.cluster);
			}
			// now remove it cluster after
			cluster.clear();
			//remove this device from orchestrators list
			simulationManager.getServersManager().getOrchestratorsList().remove(this);
			//System.err.println(simulationManager.getServersManager().getOrchestratorsList().size());
			//set the new orchestrator as the parent node ( a tree-like topology)
			//parent = newOrchestrator;
			// this device is no more an orchestrator so set it to false
			this.setAsOrchestrator(false);
			
			//in case the cluster doesn't has this device as member
			if (!newOrchestrator.cluster.contains(this))
				newOrchestrator.cluster.add(this);
		}
        // configure the new orchestrator (it can be another device, or this device)
		newOrchestrator.setAsOrchestrator(true);
		newOrchestrator.Orchestrator = newOrchestrator;
		//newOrchestrator.parent = null;
		//in case the cluster doesn't has the orchestrator as member
		if (!newOrchestrator.cluster.contains(newOrchestrator))
			newOrchestrator.cluster.add(newOrchestrator);
		//add the new orchestrator to the list
		if (!simulationManager.getServersManager().getOrchestratorsList().contains(newOrchestrator))
			simulationManager.getServersManager().getOrchestratorsList().add(newOrchestrator);

	}


	public LeaderEdgeDevice getOrchestrator() {
		return Orchestrator;
	}
 
	//my
	private void leader(){
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
				//Condition for choosing a leader

				//The election's criterion can be customized
				if (this.getResources().getTotalMips()<candidate.getResources().getTotalMips()
					&& max_MIPS <candidate.getResources().getTotalMips()){
					max_MIPS =candidate.getResources().getTotalMips();
					leader = (LeaderEdgeDevice) candidate;
				}
			}
		}
		//If I'm not the leader
		if (leader!= null) {
			//debug
			System.out.println("Il mio leader e' " + leader.getId());
			//necessary
			//this.setAsOrchestrator(false);
			//this.simulationManager.getServersManager().getOrchestratorsList().remove(this);
			//this.setAsOrchestrator(true);
			leader.subordinate.add(this);
			///
		}
		//If I'm the leader
		else if (this.getType()==SimulationParameters.TYPES.EDGE_DATACENTER){
			isLeader=true;
			this.setAsOrchestrator(false);
			this.simulationManager.getServersManager().getOrchestratorsList().remove(this);
			System.out.println("I miei sottoposti sono:"+ subordinate.size());
			for (DataCenter el: subordinate){
				System.out.println(el.getName());
			}
		}
	}

	public LeaderEdgeDevice getLeader() {
		return leader;
	}

}
