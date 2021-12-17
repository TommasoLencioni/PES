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

        //The user can pass as argument the path to the settings and output folder
        if(args.length>0 && args[0]!=null){
            settingsPath=args[0];
        }
        if(args.length>1 && args[1]!=null){
            outputPath=args[1];
        }

        //Set custom folder
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

        //Mobility Model
        setCustomMobilityModel(LeaderMobilityModel.class);
        // Launch the simulation
        MyMain.launchSimulation();

        //Copy the simulation file to the output folder
        File src_param = new File(settingsPath + "simulation_parameters.properties");
        File src_app = new File(settingsPath + "applications.xml");
        File src_devices = new File(settingsPath + "edge_devices.xml");
        File src_edge_dc = new File(settingsPath + "edge_datacenters.xml");
        File src_cloud = new File(settingsPath + "cloud.xml");

        File target_param = new File(outputPath + startTime + "/simulation_parameters.properties") ;
        File target_app= new File(outputPath + startTime + "/applications.xml") ;
        File target_devices = new File(outputPath + startTime + "/edge_devices.xml") ;
        File target_edge_dc = new File(outputPath + startTime + "/edge_datacenters.xml") ;
        File target_cloud  = new File(outputPath + startTime + "/cloud.xml") ;
        try{
            src_param.createNewFile();
            java.nio.file.Files.copy(src_param.toPath(), target_param.toPath(), StandardCopyOption.REPLACE_EXISTING);

            src_app.createNewFile();
            java.nio.file.Files.copy(src_app.toPath(), target_app.toPath(), StandardCopyOption.REPLACE_EXISTING);

            src_devices.createNewFile();
            java.nio.file.Files.copy(src_devices.toPath(), target_devices.toPath(), StandardCopyOption.REPLACE_EXISTING);

            src_edge_dc.createNewFile();
            java.nio.file.Files.copy(src_edge_dc.toPath(), target_edge_dc.toPath(), StandardCopyOption.REPLACE_EXISTING);

            src_cloud.createNewFile();
            java.nio.file.Files.copy(src_cloud.toPath(), target_cloud.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}