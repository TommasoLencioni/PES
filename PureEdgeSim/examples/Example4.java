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

import com.mechalikh.pureedgesim.MainApplication;

public class Example4 extends MainApplication {
	/**
	 * This is a simple example showing how to launch simulation using a custom Edge
	 * device/ datacenter class . by removing it, PureEdgeSim will use the default
	 * one. As you can see, this class extends the MyMain class provided by
	 * PureEdgeSim, which is required for this example to work
	 */
	public Example4(int fromIteration, int step_) {
		super(fromIteration, step_);
	}

	public static void main(String[] args) {
		/*
		 * To use your custom Edge datacenters/ devices class, do this: The custom edge
		 * data center class can be found in the examples folder as well. by removing
		 * this line, pureEdgeSim will use the default datacenters/devices class.
		 */
		setCustomEdgeDataCenters(CustomDataCenter.class);

		// To use the PureEdgeSim default edge data centers class you can also uncomment
		// this:
		// setCustomEdgeDataCenters(DefaultEdgeDataCenter.class);

		// Start the simulation
		launchSimulation();
	}

}
