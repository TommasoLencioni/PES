package tutorial;
import com.mechalikh.pureedgesim.MainApplication;
import examples.ClusterEdgeDevice;

public class MyMain extends MainApplication {

    public MyMain(int fromIteration, int step_) {
        super(fromIteration, step_);
    }

    public static void main(String[] args) {
        // Load the custom devices class
        MyMain.setCustomEdgeDataCenters(ClusterEdgeDevice.class);
        // Launch the simulation
        MyMain.launchSimulation();
    }
}