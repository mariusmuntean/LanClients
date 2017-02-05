package de.marius;

import de.marius.Util.OSHelper;

import java.io.IOException;
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
        String runningJavaProcesses = OSHelper.executeCommandAndGetOutput("jps -l");

        return runningJavaProcesses.split(JAR_NAME).length > 2;
    }

}
