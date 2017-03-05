package de.marius;

import de.marius.LanClientsCore.domain.Tuple;
import de.marius.LanClientsCore.helper.LocationsHelper;
import de.marius.LanClientsCore.helper.OSHelper;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Marius on 04.02.2017.
 */
public class NetworkSampler {

    public ScheduledFuture<?> sampleClientsScheduled(TimeUnit timeUnit, int delay) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        return executor.scheduleWithFixedDelay(this::sampleClients, 0, delay, timeUnit);
    }

    public void sampleClients() {
        try {
            System.out.println(System.getProperty("line.separator"));
            System.out.println("My IP is: " + Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String subnet = "192.168.0";
        int timeout = 1000;

        System.out.println("Starting checks at: " + new Date());
        List<Tuple<String, String>> reachableClientsIdentities = getReachableClientIdentities(subnet, timeout);
        System.out.println("Finished checks at: " + new Date());

        recordReachableHosts(reachableClientsIdentities);
    }

    private List<Tuple<String, String>> getReachableClientIdentities(String subnet, int timeout) {
        return IntStream.rangeClosed(1, 255).
                parallel().
                mapToObj(idx -> {
                    String currentHostIp = subnet + "." + idx;
                    boolean isReachable = checkIfReachable(currentHostIp, timeout);
                    return new Tuple<>(currentHostIp, isReachable);
                }).filter(Tuple::getY).
                map(stringBooleanTuple -> {
                    String currentHostMAC = OSHelper.getMacForIp(stringBooleanTuple.getX());
                    return new Tuple<>(stringBooleanTuple.getX(), currentHostMAC);
                }).
                collect(Collectors.toList());
    }

    private void recordReachableHosts(List<Tuple<String, String>> reachableHosts) {

        // Prepare the output
        StringBuilder stringBuilder = new StringBuilder();

        // Add the current datetime
        stringBuilder.append(new Date());
        stringBuilder.append(System.getProperty("line.separator"));

        // Add the columns
        stringBuilder.append("IP MAC");
        stringBuilder.append(System.getProperty("line.separator"));

        reachableHosts.forEach(stringStringTuple -> {
            stringBuilder.append(stringStringTuple.getX()).append(" ").append(stringStringTuple.getY());
            stringBuilder.append(System.getProperty("line.separator"));
        });
        try {
            String currentSampleFilePath = getCurrentRecordFilePath();
            Path path = Paths.get(currentSampleFilePath);
            if (!Files.exists(path)) {
                new File(currentSampleFilePath).getParentFile().mkdirs();
                Files.createFile(path);
            }
            Files.write(path, stringBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("At: " + new Date() + " found: " + reachableHosts.size() + " reachable hosts");
    }

    private String getCurrentRecordFilePath() {
        return LocationsHelper.getLanSamplesDirectory().resolve(System.currentTimeMillis() + ".txt").toAbsolutePath().toString();
    }

    private boolean checkIfReachable(String hostIp, int timeoutMillis) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(hostIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            return inetAddress != null && inetAddress.isReachable(timeoutMillis);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
