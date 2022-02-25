package test;
import com.mechalikh.pureedgesim.MainApplication;
import com.mechalikh.pureedgesim.locationmanager.Location;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.tasksorchestration.DefaultEdgeOrchestrator;
import examples.CustomEdgeOrchestrator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MyMain extends MainApplication {
    // Below is the path for the settings folder of this example
    public static String settingsPath = "PureEdgeSim/test/settings/";

    // The custom output folder is
    public static String outputPath = "PureEdgeSim/test/output/";

    // If we want only to use files one by one
    private static String simConfigfile = settingsPath + "simulation_parameters.properties";
    private static String applicationsFile = settingsPath + "applications.xml";
    private static String edgeDataCentersFile = settingsPath + "edge_datacenters_new.xml";
    private static String edgeDevicesFile = settingsPath + "edge_devices.xml";
    private static String cloudFile = settingsPath + "cloud.xml";
    public static String startTime;
    public MyMain(int fromIteration, int step_) {
        super(fromIteration, step_);
    }

    //Arraylist containing queues of Timesteps
    //  The device N has timesteps queued at index N of the arraylist
    public static ArrayList<ConcurrentLinkedDeque<Timestep>> movements;

    public static void main(String[] args) {
        movements= new ArrayList<>();
        //TODO FIX THE FLAT NUMBER OF DEVICES
        //Initialize the movements arraylist
        for(int i = 0; i<9999; i++){
            movements.add(i, new ConcurrentLinkedDeque());
        }
        startTime= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        //Call the method routeXML that loads SUMO simulation in the arraylist movements, after rotation
        routeXML();
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

    static void routeXML(){
        try {
            File devicesFile = new File(settingsPath+"/sim-output.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(devicesFile);
            doc.getDocumentElement().normalize();
            NodeList timesteps = doc.getElementsByTagName("timestep");

            //Configuration parameters for our map
            double translation_x = 625;
            double translation_y = 505;

            double min_angle = 10000;

            //Check all the timesteps to enstablish the minimum angle
            for (int i = 0; i < timesteps.getLength(); i++) {
                Node step_node = timesteps.item(i);
                if (step_node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) step_node;
                    NodeList vehiclesNodeList = element.getElementsByTagName("vehicle");
                    //Check every coordinate of all vehicles at the current timestep to find the minimum angle
                    for (int j = 0; j < vehiclesNodeList.getLength(); j++) {
                        Node vehinode = vehiclesNodeList.item(j);
                        //int step_time = (int) Float.parseFloat(step_node.getAttributes().getNamedItem("time").getTextContent());
                        //int id = (int) Float.parseFloat(vehinode.getAttributes().getNamedItem("id").getTextContent());
                        double x = Double.parseDouble(vehinode.getAttributes().getNamedItem("x").getTextContent());
                        double y = Double.parseDouble(vehinode.getAttributes().getNamedItem("y").getTextContent());

                        x += translation_x;
                        y += translation_y;
                        double ray = Math.sqrt(x * x + y * y);
                        double angle = Math.asin(y / ray);

                        if (angle != 0)
                            min_angle = Math.min(angle, min_angle);
                    }
                }
            }
            System.out.println("Angolo minimo: " + min_angle);

            //Work for bottom route TODO FLAT 1.4???
            min_angle = (2*Math.PI) - min_angle/1.4;

            //Works for middle route
            //min_angle = (2*Math.PI) - min_angle/2;

            double sin_min_angle = Math.sin(min_angle);
            double cos_min_angle = Math.cos(min_angle);

            double min_x = Double.MAX_VALUE;
            double min_y = Double.MAX_VALUE;

            double max_x = -Double.MAX_VALUE;
            double max_y = -Double.MAX_VALUE;

            for (int i = 0; i < timesteps.getLength(); i++) {
                Node step_node = timesteps.item(i);
                if (step_node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) step_node;
                    NodeList vehiclesNodeList = element.getElementsByTagName("vehicle");

                    for (int j = 0; j < vehiclesNodeList.getLength(); j++) {
                        Node vehinode = vehiclesNodeList.item(j);
                        double x = Double.parseDouble(vehinode.getAttributes().getNamedItem("x").getTextContent());
                        double y = Double.parseDouble(vehinode.getAttributes().getNamedItem("y").getTextContent());
                        double rot_x = Math.round((((x + translation_x) * cos_min_angle) - ((y + translation_y) * sin_min_angle))); // pos_x = x*cos(ang) - y*sin(ang)
                        double rot_y = Math.round(((x + translation_x) * sin_min_angle) + ((y + translation_y) * cos_min_angle));

                        // Calcolo coppia minima, vertice in basso a sinistra
                        if ( rot_x < min_x)
                            min_x = rot_x;

                        if ( rot_y < min_y)
                            min_y = rot_y;

                        // Calcolo coppia massima, vertice in alto a destra
                        if ( rot_x > max_x)
                            max_x = rot_x;

                        if ( rot_y > max_y)
                            max_y = rot_y;
                    }
                }
            }

            System.out.println("Coordinate minime:"+ min_x+" "+ min_y);
            System.out.println("Coordinate massime:"+ max_x+" "+max_y);

            for (int i = 0; i < timesteps.getLength(); i++) {
                Node step_node = timesteps.item(i);
                if (step_node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) step_node;
                    NodeList vehiclesNodeList = element.getElementsByTagName("vehicle");

                    for (int j = 0; j < vehiclesNodeList.getLength(); j++) {
                        Node vehinode = vehiclesNodeList.item(j);
                        int step_time = (int) Float.parseFloat(step_node.getAttributes().getNamedItem("time").getTextContent());
                        int id = (int) Float.parseFloat(vehinode.getAttributes().getNamedItem("id").getTextContent());
                        double x = Double.parseDouble(vehinode.getAttributes().getNamedItem("x").getTextContent());
                        double y = Double.parseDouble(vehinode.getAttributes().getNamedItem("y").getTextContent());
                        double rot_x = Math.round((((x + translation_x) * cos_min_angle) - ((y + translation_y) * sin_min_angle))); // pos_x = x*cos(ang) - y*sin(ang)
                        double rot_y = Math.round(((x + translation_x) * sin_min_angle) + ((y + translation_y) * cos_min_angle));

                        System.out.println("Prima x: " + x +" , y: "+ y +", dopo x:"+ (rot_x-min_x)+", y:"+ (rot_y-min_y-translation_y/2));
                        //Original
                        //Timestep ts = new Timestep(step_time, new Location(rot_x-min_x,rot_y-min_y));

                        //Works for middle
                        //Timestep ts = new Timestep(step_time, new Location(rot_x-min_x,rot_y-min_y+translation_y));

                        //Works for bottom and other
                        Timestep ts = new Timestep(step_time, new Location(rot_x-min_x,rot_y-min_y));

                        //Add the rotated timestep to movement
                        movements.get(id).add(ts);
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}