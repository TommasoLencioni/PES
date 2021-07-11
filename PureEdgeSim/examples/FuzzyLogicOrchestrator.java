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
package examples;

import org.cloudbus.cloudsim.cloudlets.Cloudlet.Status;

import com.mechalikh.pureedgesim.datacentersmanager.DataCenter;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.simulationmanager.SimLog;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import com.mechalikh.pureedgesim.tasksgenerator.Task;

import net.sourceforge.jFuzzyLogic.FIS;


public class FuzzyLogicOrchestrator extends CustomEdgeOrchestrator {

	public FuzzyLogicOrchestrator(SimulationManager simulationManager) {
		super(simulationManager);
	}

	/**
	 * Cerca una VM sulla quale fare offloading del task.
	 * Nel tutorial è chiamato findResources
	 * @param architecture Architettura della simulazione (computing paradigm)
	 * @param task Task del quale fare l'offloading
	 * @return l'indice della VM sulla quale fare offloading
	 */
	protected int findVM(String[] architecture, Task task) {
		//Here Chiamato nel tutorial STATE_OF_THE_ART
		if ("INCREASE_LIFETIME".equals(algorithm))
			return increseLifetime(architecture, task);
		//Here Chiamato nel tutorial PROPOSED
		else if ("FUZZY_LOGIC".equals(algorithm))
			return fuzzyLogic(task);
		else {
			SimLog.println("");
			SimLog.println("Custom Orchestrator- Unknown orchestration algorithm '" + algorithm
					+ "', please check the simulation parameters file...");
			// Cancel the simulation
			SimulationParameters.STOP = true;
			simulationManager.getSimulation().terminate();
		}
		return -1;
	}

	private int fuzzyLogic(Task task) {
		//Loads the FDT from the file stage1.fcl
		String fileName = "PureEdgeSim/examples/Example8_settings/stage1.fcl";
		FIS fis = FIS.load(fileName, true);
		// Error while loading?
		if (fis == null) {
			System.err.println("Can't load file: '" + fileName + "'");
			return -1;
		}
		double vmUsage = 0;
		int count = 0;
		for (int i = 0; i < vmList.size(); i++) {
			if (((DataCenter) vmList.get(i).getHost().getDatacenter()).getType() != SimulationParameters.TYPES.CLOUD) {
				//Aggiunge l'uso attuale della VM
				vmUsage += vmList.get(i).getCpuPercentUtilization() * 100;
				//Incrementa di 1 le VM disponibili
				count++;
				//fix Aggiunge l'uso attuale della CPU del Datacenter della VM (?)
				vmUsage += ((DataCenter) vmList.get(i).getHost().getDatacenter()).getResources().getAvgCpuUtilization();
			}
		}

		// set fuzzy inputs
		fis.setVariable("wan",
				SimulationParameters.WAN_BANDWIDTH / 1000 - simulationManager.getNetworkModel().getWanUtilization());
		fis.setVariable("tasklength", task.getLength());
		fis.setVariable("delay", task.getMaxLatency());
		fis.setVariable("vm", vmUsage / count);

		// Evaluate
		fis.evaluate();
		//fix non ho capito per cosa stia quel 50
		// superato quello dal risultato di defuzzify (non sono riuscito a capire concretamente cosa restituisca)
		// allora utilizzo increaseLifetime con architettura Cloud?
		if (fis.getVariable("offload").defuzzify() > 50) {
			String[] architecture2 = { "Cloud" };
			return increseLifetime(architecture2, task);
		} else {
			//Escludo che possa utilizzare l'architettura cloud farò l'offload solo su edge e mist
			String[] architecture2 = { "Edge", "Mist" };
			return stage2(architecture2, task);
		}

	}

	private int stage2(String[] architecture2, Task task) {
		double min = -1;
		int vm = -1;
		String fileName = "PureEdgeSim/examples/Example8_settings/stage2.fcl";
		FIS fis = FIS.load(fileName, true);
		// Error while loading?
		if (fis == null) {
			System.err.println("Can't load file: '" + fileName + "'");
			return -1;
		}
		for (int i = 0; i < vmList.size(); i++) {
			//Con offloadingIsPossible si checka anche se la distanza è minore del range
			//fix Perché storage >0? Se fosse 1 e lo spazio richiesto dal task fosse >1?
			if (offloadingIsPossible(task, vmList.get(i), architecture2)
					&& vmList.get(i).getStorage().getCapacity() > 0) {
				//fix praticamente questo if è inutile perché l'outcome è lo stesso
				// Nel FDT risulterà sempre low
				if (!task.getEdgeDevice().getMobilityManager().isMobile())
					fis.setVariable("vm_local", 0);
				else
					fis.setVariable("vm_local", 0);

				//Inserisce nel FDT quante migliaia di MIPS il processore può ancora eseguire
				fis.setVariable("vm", (1 - vmList.get(i).getCpuPercentUtilization()) * vmList.get(i).getMips() / 1000);
				fis.evaluate();

				//Se è il primo offload che valuto o se l'output dell'offload del FDT è minore del minimo
				// allora setto il nuovo minimo e stabilisco l'indice della VM sulla quale effettuare l'offload
				if (min == -1 || min > fis.getVariable("offload").defuzzify()) {
					min = fis.getVariable("offload").defuzzify();
					vm = i;
				}
			}
		}
		return vm;
	}


	@Override
	public void resultsReturned(Task task) {
		//Reinforcement Learning Implemetation
		//int ruleIndex = getDecisionRuleFromHistory(task.getId());
		//FuzzyDecisionTree FDT = task.getEdgeDevice().getDecisiontree();

		// How to get the task execution status, (if failed or succeed, which can be
		// used for reinforcement learning based algorithms)
		if (task.getStatus() == Status.FAILED) {
			System.err.println("CustomEdgeOrchestrator, task " + task.getId() + " has been failed, failure reason is: "
					+ task.getFailureReason());
		} else {

			System.out.println("CustomEdgeOrchestrator, task " + task.getId() + " has been successfully executed");
		}

	}

}
