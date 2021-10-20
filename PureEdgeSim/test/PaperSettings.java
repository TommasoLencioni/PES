package test;

public class PaperSettings {
	public static double ORCHESTRATION_ALGORITHM_INTERVAL = 30.0; // Time since next orchestration algorithm iteration, in seconds  
	public static long ORCH_TO_ORCH_MSG_SIZE = 1; // size, in kbytes, of a notification msg between orchestrators
	public static double LATENCY_DELAY = 0.00001; // latency delay for "point" of distance, in seconds
	public static double MIN_LATENCY_DELAY = 0.02; // minimum latency for the channel, in seconds
}
