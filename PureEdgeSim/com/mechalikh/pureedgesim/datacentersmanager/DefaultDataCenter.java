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
package com.mechalikh.pureedgesim.datacentersmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mechalikh.pureedgesim.locationmanager.Location;
import com.mechalikh.pureedgesim.scenariomanager.Scenario;
import com.mechalikh.pureedgesim.tasksgenerator.TasksGenerator;
import org.apache.commons.math3.geometry.Point;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;

import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import test.LeaderMobilityModel;
import test.MyMain;
import test.Timestep;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//here qualsiasi cosa che calcoli
public class DefaultDataCenter extends DataCenter {
	protected static final int UPDATE_STATUS = 2000; // Avoid conflicting with CloudSim Plus Tags

	public DefaultDataCenter(SimulationManager simulationManager, List<? extends Host> hostList,
			List<? extends Vm> vmList) {
		super(simulationManager, hostList, vmList);
	}

	@Override
	public void startInternal() {
		super.startInternal();
		schedule(this, SimulationParameters.INITIALIZATION_TIME, UPDATE_STATUS);
	}

	@Override
	public void processEvent(final SimEvent ev) {
		//here passo per il default datacenter
		switch (ev.getTag()) {
		case UPDATE_STATUS:
			updateStatus();
			//here si autoschedula
			if (!isDead()) {
				schedule(this, SimulationParameters.UPDATE_INTERVAL, UPDATE_STATUS);
			}
			break;
		default:
			super.processEvent(ev);
			break;
		}

	}

	private void updateStatus() {
		// Check if the device is dead
		if (getEnergyModel().isBatteryPowered()
				&& this.getEnergyModel().getTotalEnergyConsumption() > getEnergyModel().getBatteryCapacity()) {
			setDeath(true, simulationManager.getSimulation().clock());
		}

		// Update location
		if (getMobilityManager().isMobile()) {
			//Original "get next location"
			//System.out.println("Ho l'id " + (Integer.parseInt(this.getName().replace("LeaderEdgeDevice",""))));
			//TODO fixa quel + flat
			int id = (int) (this.getId() - (SimulationParameters.NUM_OF_EDGE_DATACENTERS + SimulationParameters.NUM_OF_CLOUD_DATACENTERS + 2));
			//System.out.println("L'id e' "+ (Integer.parseInt(this.getName().replace("LeaderEdgeDevice", "")) - (SimulationParameters.NUM_OF_EDGE_DATACENTERS + SimulationParameters.NUM_OF_CLOUD_DATACENTERS + 2)));
			/*
			if(MyMain.movements==null){
				System.out.println("Eh, e' null");
			}
			else if(MyMain.movements.get(id).isEmpty()){
				System.out.println("Eh, e' vuoto");
			}
			else if(MyMain.movements.get(id).getFirst().getTime() == ((int) simulationManager.getSimulation().clock())){
				System.out.println("Eh, e' minore");
			}
			*/
			if (MyMain.movements!=null) {
				if(!MyMain.movements.get(id).isEmpty()) {
					if(MyMain.movements.get(id).getFirst().getTime() <= ((int) simulationManager.getSimulation().clock())) {
						Timestep tmp_ts = MyMain.movements.get(id).pollFirst();
						//System.out.println("Sono: " + id + ", time: " + tmp_ts.getTime() + ", x: " + tmp_ts.getLocation().getXPos() + " , y: " + tmp_ts.getLocation().getYPos() + ", dimensione:" + MyMain.movements.get(id).size());
						int x=(int)tmp_ts.getLocation().getXPos()%SimulationParameters.AREA_WIDTH;
						((LeaderMobilityModel) getMobilityManager()).my_getNextLocation(x , (int) tmp_ts.getLocation().getYPos()%SimulationParameters.AREA_LENGTH);
					}
				}
				else{
					//((LeaderMobilityModel)getMobilityManager()).my_getNextLocation(-1, -1);
					setDeath(true, getSimulation().clock());
				}
			}

			//Original
			//getMobilityManager().getNextLocation();
		}
	}

}
