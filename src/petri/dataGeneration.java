package petri;

import com.sun.deploy.util.StringUtils;
import petri.entity.PetriNet;
import petri.service.GenerateRandomDeviatedTraceService;
import petri.service.InitializePetriNetService;

import java.io.*;
import java.util.ArrayList;

public class dataGeneration {
    public static void main(String[] args) throws IOException {
        String model = System.getProperty("user.dir") + "/resources/petri/clinicalpathway.txt";
        String eventLogs = System.getProperty("user.dir") + "/resources/petri/generatedTraces.txt";
        InitializePetriNetService initializePetriNetService = new InitializePetriNetService();
        PetriNet processNet = initializePetriNetService.initProcessPetriNet(model);
        GenerateRandomDeviatedTraceService generateRandomDeviatedTraceService = new GenerateRandomDeviatedTraceService(processNet);
        /*  共有:22条迹
            长度为:0的迹有:0条
            长度为:5的迹有:1条
            长度为:6的迹有:1条
            长度为:7的迹有:3条
            长度为:8的迹有:4条
            长度为:9的迹有:4条
            长度为:10的迹有:5条
            长度为:11的迹有:2条
            长度为:12的迹有:1条
            长度为:13的迹有:1条*/
        generateRandomDeviatedTraceService.setLeastLength(10);
        ArrayList<ArrayList<String>> oriTraces = generateRandomDeviatedTraceService.getAllTraces();
        System.out.println(oriTraces.size());
        ArrayList<ArrayList<String>> devTraces = generateRandomDeviatedTraceService.generateDevTraces(true, true, 10, 30, oriTraces);
        System.out.println(devTraces.size());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(eventLogs)));
        for (ArrayList<String> trace : oriTraces){
            writer.write(StringUtils.join(trace,","));
            writer.newLine();
            writer.flush();
        }
        for (ArrayList<String> trace : devTraces){
            writer.write(StringUtils.join(trace,","));
            writer.newLine();
            writer.flush();
        }
        writer.close();
    }
}
