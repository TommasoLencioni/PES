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
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;

import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import test.LeaderMobilityModel;
import test.MyMain;
import test.Timestep;


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
			//Il +2 flat adatta il conteggio dei devices nel totale dei datacenters
			int id = (int) (this.getId() - (SimulationParameters.NUM_OF_EDGE_DATACENTERS + SimulationParameters.NUM_OF_CLOUD_DATACENTERS + 2));

			if (MyMain.movements!=null) {
				if(!MyMain.movements.get(id).isEmpty()) {
					//Controllo se esiste un movimento per il dispositivo corrente e il timestamp corrente
					//Il caso di < non dovrebbe mai succedere
					if (MyMain.movements.get(id).getFirst().getTime() <= ((int) simulationManager.getSimulation().clock())) {
						Timestep tmp_ts = MyMain.movements.get(id).pollFirst();
						//Assegno come nuova location le coordinate ottenute dalla simulazione su SUMO
						((LeaderMobilityModel) getMobilityManager()).my_getNextLocation(tmp_ts.getLocation());
					}
					/*
					//TODO FIX THE GENERATION OF TASKS FOR DEVICES THAT ARE NOT YET IN MOVEMENT
					//Non c'e' modo di far smettere ai devices di generare tasks
					else if (MyMain.movements.get(id).getFirst().getTime() > ((int) simulationManager.getSimulation().clock())) {
						((LeaderMobilityModel) getMobilityManager()).my_getNextLocation(new Location(0,0));
					}
					*/
				}
				else{
					//Essendo contigui, se non ci sono piu' movimenti per un device significa che non partecipera' piu' alla simulazione
					//	Setto il suo status a dead
					setDeath(true, getSimulation().clock());
				}
			}

			//Original
			//getMobilityManager().getNextLocation();
		}
	}

}
