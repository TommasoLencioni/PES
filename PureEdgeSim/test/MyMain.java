package test;
import com.mechalikh.pureedgesim.MainApplication;
import com.mechalikh.pureedgesim.tasksorchestration.DefaultEdgeOrchestrator;
import examples.CustomEdgeOrchestrator;

public class MyMain extends MainApplication {
    // Below is the path for the settings folder of this example
    private static String settingsPath = "PureEdgeSim/test/settings/";

    // The custom output folder is
    private static String outputPath = "PureEdgeSim/test/output/";

    // If we want only to use files one by one
    private static String simConfigfile = settingsPath + "simulation_parameters.properties";
    private static String applicationsFile = settingsPath + "applications.xml";
    private static String edgeDataCentersFile = settingsPath + "edge_datacenters.xml";
    private static String edgeDevicesFile = settingsPath + "edge_devices.xml";
    private static String cloudFile = settingsPath + "cloud.xml";

    public MyMain(int fromIteration, int step_) {
        super(fromIteration, step_);
    }

    public static void main(String[] args) {

        //From example 6
        setCustomOutputFolder(outputPath);
        // changing the simulation settings folder
        setCustomSettingsFolder(settingsPath);

        // tell PureEdgeSim to use this custom orchestrator and orchestration algorithm
        setCustomEdgeOrchestrator(LeaderEdgeOrchestrator.class);
        //setCustomEdgeOrchestrator(DefaultEdgeOrchestrator.class);

        // Leadering
        setCustomEdgeDataCenters(LeaderEdgeDevice.class);

        //Network Model
        setCustomNetworkModel(LeaderNetworkModel.class);
        // Launch the simulation
        MyMain.launchSimulation();
    }
}