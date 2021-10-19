package test;

import com.mechalikh.pureedgesim.datacentersmanager.DataCenter;
import com.mechalikh.pureedgesim.network.FileTransferProgress;
import com.mechalikh.pureedgesim.network.NetworkModel;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.simulationmanager.SimLog;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import com.mechalikh.pureedgesim.tasksgenerator.Task;

public class LeaderNetworkModel extends NetworkModel{

    public LeaderNetworkModel(SimulationManager simulationManager) {
        super(simulationManager);
    }

    public void sendResultFromDevToOrch(Task task) {
        if (task.getOrchestrator() != task.getEdgeDevice()) {
            //System.out.println("Sto usando il mio");
            closerNode(task);
            transferProgressList.add(new FileTransferProgress(task, task.getOutputSize() * 8,
                    FileTransferProgress.Type.RESULTS_TO_ORCH));
        }
        else
            scheduleNow(this, NetworkModel.SEND_RESULT_FROM_ORCH_TO_DEV, task);
    }

    public void closerNode(Task task){
        try {
            int minDist = SimulationParameters.AREA_LENGTH;
            LeaderEdgeDevice closerNode = (LeaderEdgeDevice) task.getOrchestrator();
            int tmpDist;
            int actualDistante = (int) task.getOrchestrator().getMobilityManager().distanceTo(task.getEdgeDevice());
            if (((LeaderEdgeDevice) task.getOrchestrator()).getLeader() != null) {
                for (LeaderEdgeDevice dc : ((LeaderEdgeDevice) task.getOrchestrator()).getLeader().community) {
                    tmpDist = (int) task.getEdgeDevice().getMobilityManager().distanceTo(dc);
                    //System.out.println(dc.getName() + " " + tmpDist + " rispetto agli attuali " + task.getOrchestrator().getName() + " " + actualDistante);
                    if (tmpDist < minDist) {
                        minDist = tmpDist;
                        closerNode = dc;
                    }
                }
            }
            if (!closerNode.equals(task.getOrchestrator())) {
                //System.err.println(task.getId() + " Cambio orchestratore da "+ task.getOrchestrator().getName() +" a  " + closerNode.getName());
                task.setOrchestrator(closerNode);
            }
        }
        catch (Exception ignored) {

        }
    }
}
