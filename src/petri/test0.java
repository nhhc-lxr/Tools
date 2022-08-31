package petri;

import petri.entity.AlignmentData;
import petri.entity.PetriNet;
import petri.entity.TransitionOfCompositionModel;
import petri.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class test0 {
    public static void main(String[] args) throws IOException {
        String filename1 = System.getProperty("user.dir") + "/resources/petri/clinicalpathway.txt";
        InitializePetriNetService initializePetriNetService;
        GenerateRandomDeviatedTraceService generateRandomDeviatedTraceService;
        GenerateSynchronizationService generateSynchronizationService;
        GenerateProductService generateProductService;
        GenerateOptimalAlignmentService generateOptimalAlignmentService;
        PetriNet eventNet;
        PetriNet sync;
        PetriNet product;
        PetriNet processNet;
        int i, j, k;
        int avgcount1;
        long avgtime1;
        int avgcount2;
        long avgtime2;
        int avgpnum1;
        int avgtnum1;
        int avgarcnum1;
        int avgpnum2;
        int avgtnum2;
        int avgarcnum2;
        int loopTime;
        AlignmentData syncAlignData;
        AlignmentData prodAlignData;
        ArrayList<ArrayList<String>> traces;
        ArrayList<ArrayList<String>> temptraces;
        ArrayList<ArrayList<PetriNet>> eventNetSet;
        ArrayList<PetriNet> eventNets;
        initializePetriNetService = new InitializePetriNetService();
        processNet = initializePetriNetService.initProcessPetriNet(filename1);
        generateSynchronizationService = new GenerateSynchronizationService();
        generateProductService = new GenerateProductService();
        generateRandomDeviatedTraceService = new GenerateRandomDeviatedTraceService(processNet);
        generateOptimalAlignmentService = new GenerateOptimalAlignmentService();
        generateRandomDeviatedTraceService.setLeastLength(10);
        //processNet.printPetriNet();
        traces = generateRandomDeviatedTraceService.getAllTraces();
        for (i = 0; i < 7; ++i) {
            boolean s = true;
            System.out.println("Noise-Ratio:" + i * 5 + "%");
            temptraces = generateRandomDeviatedTraceService.generateDevTraces(false, false, 100, i * 5, traces);
            avgcount1 = 0;
            avgtime1 = 0;
            avgcount2 = 0;
            avgtime2 = 0;
            avgarcnum1 = 0;
            avgpnum1 = 0;
            avgtnum1 = 0;
            avgarcnum2 = 0;
            avgpnum2 = 0;
            avgtnum2 = 0;

            loopTime = 1;

            for (j = 0; j < temptraces.size(); ++j) {
                eventNet = initializePetriNetService.initEventPetriNet(temptraces.get(j));
                sync = generateSynchronizationService.getSynchronizationOfEventNetAndProcessNet(eventNet, processNet);
                product = generateProductService.getProductOfEventNetAndProcessNet(eventNet, processNet);
                for (k = 0; k < loopTime; ++k) {
                    syncAlignData = generateOptimalAlignmentService.generateOptimalAlignment(sync);
                    prodAlignData = generateOptimalAlignmentService.generateOptimalAlignment(product);
                    if (syncAlignData.totalCost != prodAlignData.totalCost){
                        System.err.println("不同");
                        s = false;
                    }
                    avgcount1 += syncAlignData.reachableMarkcount;
                    avgtime1 += syncAlignData.alignTime; //+ sync.modelTime;
                    avgarcnum1 += sync.getArcNum();
                    avgpnum1 += sync.pnum;
                    avgtnum1 += sync.tnum;

                    avgcount2 += prodAlignData.reachableMarkcount;
                    avgtime2 += prodAlignData.alignTime; //+ product.modelTime;
                    avgarcnum2 += product.getArcNum();
                    avgpnum2 += product.pnum;
                    avgtnum2 += product.tnum;
                }
            }
            if (s) System.out.println("全部相同");
            avgpnum1 = calResult(avgpnum1, traces.size() * 100 * loopTime);
            avgtnum1 = calResult(avgtnum1, traces.size() * 100 * loopTime);
            avgarcnum1 = calResult(avgarcnum1, traces.size() * 100 * loopTime);
            avgcount1 = calResult(avgcount1, traces.size() * 100 * loopTime);
            avgtime1 = calResult((int) avgtime1, traces.size() * 100 * loopTime);

            avgpnum2 = calResult(avgpnum2, traces.size() * 100 * loopTime);
            avgtnum2 = calResult(avgtnum2, traces.size() * 100 * loopTime);
            avgarcnum2 = calResult(avgarcnum2, traces.size() * 100 * loopTime);
            avgcount2 = calResult(avgcount2, traces.size() * 100 * loopTime);
            avgtime2 = calResult((int) avgtime2, traces.size() * 100 * loopTime);
            System.out.println("sync平均耗时：" + avgtime1 + ",sync可达标识数：" + avgcount1 + ",库所数：" + avgpnum1 + ",变迁数：" + avgtnum1 + ",弧数：" + avgarcnum1);
            System.out.println("product平均耗时：" + avgtime2 + ",product可达标识数：" + avgcount2 + ",库所数：" + avgpnum2 + ",变迁数：" + avgtnum2 + ",弧数：" + avgarcnum2);
        }
    }

    static int calResult(int num1, int num2) {
        return (int) Math.round(((double) num1) / num2);
    }
}
