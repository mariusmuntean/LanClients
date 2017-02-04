import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

public class Main {

    public static final String JAR_NAME = "LanClientsScan.jar";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

//        // Check if LanClientsScan already running
//        boolean alreadyRunning = isAlreadyRunning();
//        if (alreadyRunning) {
//            System.out.println("Already started. Leaving now ...");
//            return;
//        }

        NetworkSampler networkSampler = new NetworkSampler();
        ScheduledFuture<?> scheduledFuture = networkSampler.sampleClientsScheduled();
        while (!scheduledFuture.isDone()) {
            scheduledFuture.get();
        }

    }

//    private static boolean isAlreadyRunning() throws IOException {
//        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
//
//        if (isWindows) {
//            //ToDo: Windows still needs to be implemented
//            return false;
//        }
//
//        String runningJavaProcesses = executeCommandAndGetOutput("ps aux");
//
//        return runningJavaProcesses != null && runningJavaProcesses.contains(JAR_NAME);
//    }
//
//    private static String executeCommandAndGetOutput(String command) throws IOException {
//        Process p = Runtime.getRuntime().exec(command);
//        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//        String line;
//        StringBuilder commandOutput = new StringBuilder();
//        while ((line = input.readLine()) != null) {
//            if (!line.trim().equals("")) {
//                // keep only the process name
//                line = line.substring(1);
//                commandOutput.append(line);
//                commandOutput.append("\n");
//            }
//        }
//        return commandOutput.toString();
//    }
}
