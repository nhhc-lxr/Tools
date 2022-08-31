package petri;

import petri.entity.AlignmentData;
import petri.entity.PetriNet;
import petri.service.GenerateOptimalAlignmentService;
import petri.service.GenerateProductService;
import petri.service.InitializePetriNetService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class test1 {
    public static void main(String[] args) throws IOException {
        String filename1 = System.getProperty("user.dir") + "/resources/petri/clinicalpathway.txt";
        InitializePetriNetService initializePetriNetService;
        GenerateProductService generateProductService;
        GenerateOptimalAlignmentService generateOptimalAlignmentService;
        AlignmentData prodAlignData;
        PetriNet processNet;
        PetriNet eventNet;
        PetriNet product;
        initializePetriNetService = new InitializePetriNetService();
        processNet = initializePetriNetService.initProcessPetriNet(filename1);
        generateProductService = new GenerateProductService();
        generateOptimalAlignmentService = new GenerateOptimalAlignmentService();


        String trace_ = "tran1,tran3,tran18,tran21,tran22,tran29,tran31,tran33";
        ArrayList<String> trace = new ArrayList<>(Arrays.asList(trace_.split(",")));

        eventNet = initializePetriNetService.initEventPetriNet(trace);
        //生成乘积模型
        product = generateProductService.getProductOfEventNetAndProcessNet(eventNet, processNet);



        //计算最优对齐
        prodAlignData = generateOptimalAlignmentService.generateOptimalAlignment(product);




        generateOptimalAlignmentService.printAlignment(prodAlignData.alignment);
    }
}
