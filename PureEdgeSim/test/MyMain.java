package test;
import com.mechalikh.pureedgesim.MainApplication;
import com.mechalikh.pureedgesim.tasksorchestration.DefaultEdgeOrchestrator;
import examples.CustomEdgeOrchestrator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyMain extends MainApplication {
    // Below is the path for the settings folder of this example
    public static String settingsPath = "PureEdgeSim/test/settings/";

    // The custom output folder is
    public static String outputPath = "PureEdgeSim/test/output/";

    // If we want only to use files one by one
    private static String simConfigfile = settingsPath + "simulation_parameters.properties";
    private static String applicationsFile = settingsPath + "applications.xml";
    private static String edgeDataCentersFile = settingsPath + "edge_datacenters.xml";
    private static String edgeDevicesFile = settingsPath + "edge_devices.xml";
    private static String cloudFile = settingsPath + "cloud.xml";
    public static String startTime;
    public MyMain(int fromIteration, int step_) {
        super(fromIteration, step_);
    }

    public static void main(String[] args) {
        startTime= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
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
        //setCustomNetworkModel(PaperNetworkModel.class);

         //= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        // Launch the simulation
        MyMain.launchSimulation();
        File src = new File(settingsPath + "simulation_parameters.properties");
        File target = new File(outputPath + startTime + "/simulation_parameters.properties") ;
        try{
            src.createNewFile();
            java.nio.file.Files.copy(src.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}