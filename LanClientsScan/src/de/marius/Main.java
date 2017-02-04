import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    public static final String JAR_NAME = "LanClientsScan.jar";

    public static void main(String[] args) throws IOException {

//        // Check if LanClientsScan already running
//        boolean alreadyRunning = isAlreadyRunning();
//        if (alreadyRunning) {
//            System.out.println("Already started. Leaving now ...");
//            return;
//        }

        sampleNetwork();
    }

    private static boolean isAlreadyRunning() throws IOException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        if (isWindows) {
            //ToDo: Windows still needs to be implemented
            return false;
        }

        String runningJavaProcesses = executeCommandAndGetOutput("ps aux");

        return runningJavaProcesses != null && runningJavaProcesses.contains(JAR_NAME);
    }

    private static String executeCommandAndGetOutput(String command) throws IOException {
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        StringBuilder commandOutput = new StringBuilder();
        while ((line = input.readLine()) != null) {
            if (!line.trim().equals("")) {
                // keep only the process name
                line = line.substring(1);
                commandOutput.append(line);
                commandOutput.append("\n");
            }
        }
        return commandOutput.toString();
    }


    private static void sampleNetwork() throws UnknownHostException {
        System.out.println("My IP is: " + Inet4Address.getLocalHost().getHostAddress());
        String subnet = "192.168.0";
        int timeout = 1000;
        List<Callable<Tuple<String, Boolean>>> callables = getReachTestCallables(subnet, timeout);

        Runnable subnetReachabilityCallable = recordReachableHosts(callables);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(subnetReachabilityCallable, 0, 30, TimeUnit.MINUTES);
    }

    private static Runnable recordReachableHosts(List<Callable<Tuple<String, Boolean>>> callables) {
        return () -> {
            List<Tuple<String, String>> reachableHosts = getReachableHosts(callables);

            String currentSampleFilePath = getCurrentRecordFilePath();

            // Prepare the output
            StringBuilder stringBuilder = new StringBuilder();

            // Add the current datetime
            stringBuilder.append(new Date());
            stringBuilder.append(System.getProperty("line.separator"));

            // Add the columns
            stringBuilder.append("IP MAC");
            stringBuilder.append(System.getProperty("line.separator"));

            reachableHosts.stream().forEach(stringStringTuple -> {
                stringBuilder.append(stringStringTuple.getX() + " " + stringStringTuple.getY());
                stringBuilder.append(System.getProperty("line.separator"));
            });
            try {
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
        };
    }

    private static String getCurrentRecordFilePath() {
        String currentUsersHomeDir = System.getProperty("user.home");
        String hostsReachabilityFolder = currentUsersHomeDir + File.separator + "reachability";
        return hostsReachabilityFolder + File.separator + System.currentTimeMillis() + ".txt";
    }

    static List<Tuple<String, String>> getReachableHosts(List<Callable<Tuple<String, Boolean>>> callables) {
        List<Tuple<String, String>> rachableHosts = new ArrayList<>();
        ExecutorService executorService = Executors.newWorkStealingPool();
        try {
            rachableHosts = executorService.invokeAll(callables).stream()
                    .map(tupleFuture -> {
                        try {
                            Tuple<String, Boolean> result = tupleFuture.get();
                            //System.out.println(result.getX() + " is reachable: " + result.getY());
                            return result;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return new Tuple<>("", false);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            return new Tuple<>("", false);
                        }
                    }).filter(stringBooleanTuple -> stringBooleanTuple.getY()).
                            map(stringBooleanTuple -> {
                                String ip = stringBooleanTuple.getX();
                                String MAC = getMACfromIp(ip);
                                return new Tuple<>(ip, MAC);
                            }).collect(Collectors.toList());


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rachableHosts;
    }

    private static List<Callable<Tuple<String, Boolean>>> getReachTestCallables(String subnet, int timeout) {
        List<Callable<Tuple<String, Boolean>>> callables = new ArrayList<>();


        for (int i = 1; i < 255; i++) {
            String host = subnet + "." + i;
            callables.add(() -> {
                InetAddress inetAddress = InetAddress.getByName(host);
                boolean isReachable = checkIfReachable(inetAddress, timeout);
                return new Tuple<>(host, isReachable);
            });
        }
        return callables;
    }

    private static boolean checkIfReachable(InetAddress address, int timeoutMillis) {
        try {
            return address.isReachable(timeoutMillis);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getMACfromIp(String ip) {
        try {
            return run_program_with_catching_output("arp -a " + ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String run_program_with_catching_output(String param) throws IOException {
        Process p = Runtime.getRuntime().exec(param);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            if (!line.trim().equals("")) {
                // keep only the process name
                //line = line.substring(1);
                String mac = extractMacAddr(line);
                if (mac.isEmpty() == false) {
                    return mac;
                }
            }

        }
        return null;
    }

    public static String extractMacAddr(String str) {
        String arr[] = str.split(" ");
        for (String string : arr) {
            if (string.trim().length() == 17) {
                return string.trim().toUpperCase();
            }
        }
        return "";
    }

}
