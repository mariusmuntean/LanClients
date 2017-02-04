package de.marius;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String JAR_NAME = "LanClientsScan.jar";


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Check if LanClientsScan already running
        boolean alreadyRunning = isAlreadyRunning();
        if (alreadyRunning) {
            System.out.println("Already started. Leaving now ...");
            return;
        }


        NetworkSampler networkSampler = new NetworkSampler();
        ScheduledFuture<?> scheduledFuture = networkSampler.sampleClientsScheduled(TimeUnit.MINUTES, 30);
        while (!scheduledFuture.isDone()) {
            scheduledFuture.get();
        }

    }

    /**
     * Checks if the current application was already started with some predefined argument.
     * If "jps -m" returns more than one entry with the predefined argument
     * it means the application was started multiple times.
     *
     * @return True if the application is already running, false otherwise.
     * @throws IOException
     */
    private static boolean isAlreadyRunning() throws IOException {
        String runningJavaProcesses = executeCommandAndGetOutput("jps -l");

        return runningJavaProcesses.split(JAR_NAME).length > 2;
    }

    private static String executeCommandAndGetOutput(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        StringBuilder commandOutput = new StringBuilder();
        while ((line = input.readLine()) != null) {
            commandOutput.append(line);
            commandOutput.append(System.getProperty("line.separator"));
        }
        return commandOutput.toString();
    }
}
