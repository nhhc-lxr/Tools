package petri;


import com.sun.deploy.util.StringUtils;
import petri.entity.PetriNet;
import petri.service.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

public class getEventLogs {
    public static void main(String[] args) throws IOException {
        String filename1 = System.getProperty("user.dir") + "/resources/petri/clinicalpathway.txt";
        String filename2 = System.getProperty("user.dir") + "/resources/petri/eventlogs.txt";
        InitializePetriNetService initializePetriNetService;
        GenerateRandomDeviatedTraceService generateRandomDeviatedTraceService;
        PetriNet processNet;
        ArrayList<ArrayList<String>> traces;
        ArrayList<ArrayList<String>> devtraces;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename2)));
        initializePetriNetService = new InitializePetriNetService();
        processNet = initializePetriNetService.initProcessPetriNet(filename1);
        generateRandomDeviatedTraceService = new GenerateRandomDeviatedTraceService(processNet);
        generateRandomDeviatedTraceService.setLeastLength(10);
        generateRandomDeviatedTraceService.printAllTracesLength();
        traces = generateRandomDeviatedTraceService.getRandomTraces(0, 0, 5);
        devtraces = new ArrayList<>();
        for (ArrayList<String> trace : traces) {
            generateRandomDeviatedTraceService.printTrace(trace);
            writer.write(StringUtils.join(trace, ","));
            writer.newLine();
            writer.flush();
        }
        devtraces = generateRandomDeviatedTraceService.generateDevTraces(true,true, 100, 30, traces);
        for (ArrayList<String> trace : devtraces) {
            generateRandomDeviatedTraceService.printTrace(trace);
            writer.write(StringUtils.join(trace, ","));
            writer.newLine();
            writer.flush();
        }
        writer.close();
    }
}
