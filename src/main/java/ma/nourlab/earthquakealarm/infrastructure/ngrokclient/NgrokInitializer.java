package ma.nourlab.earthquakealarm.infrastructure.ngrokclient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NgrokInitializer {

    private Process ngrokProcess;

    @PostConstruct
    public void init() {
        log.info("Initializing ngrok");
        if (!isNgrokRunningLinux()) {
            ProcessBuilder processBuilder = new ProcessBuilder();

            // Command to start ngrok on port 8080 (assuming ngrok is in system PATH)
            processBuilder.command("ngrok", "http", "8080");

            try {
                ngrokProcess = processBuilder.start();

                // You may not want to wait for ngrok to finish because it will block your application
                // So you can comment out the following line if you don't need it
                // ngrokProcess.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isNgrokRunning() {
        log.info("Checking if ngrok is running");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "tasklist");
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("ngrok")) {
                        return true;
                    }
                }
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isNgrokRunningLinux() {
        log.info("Checking if ngrok is running");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("pgrep", "ngrok");
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.readLine() != null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @PreDestroy
    public void tearDown() {
        log.info("Tearing down ngrok");
        if (ngrokProcess != null) {
            ngrokProcess.destroy();
        }
    }
}
