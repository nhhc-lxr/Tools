package petri.service;

import petri.entity.Mark;
import petri.entity.PetriNet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GenerateRandomDeviatedTraceService {
    public PetriNet processNet;
    public HashMap<Integer, ArrayList<ArrayList<String>>> tracesMap;
    Random random;
    int totalCount;
    int leastLength;

    public GenerateRandomDeviatedTraceService(PetriNet processNet) {
        this.processNet = processNet;
        this.random = new Random();
        this.tracesMap = new HashMap<>();
        tracesMap.put(0, new ArrayList<>());
        totalCount = 0;
        findAllTraces();
    }

    public void setLeastLength(int length) {
        this.leastLength = length;
        for (Integer key : tracesMap.keySet()) {
            if (key >= length)
                tracesMap.get(0).addAll(tracesMap.get(key));
        }
    }

    public ArrayList<ArrayList<String>> getAllTraces() {
        return new ArrayList<>(tracesMap.get(0));
    }

    public ArrayList<ArrayList<String>> getRandomTraces(int traceLen, int devRate, int count) {
        ArrayList<Integer> traceIndex;
        ArrayList<ArrayList<String>> newTraces;
        ArrayList<ArrayList<String>> traces;
        traces = (ArrayList<ArrayList<String>>) tracesMap.get(traceLen).clone();
        traceIndex = new ArrayList<>();
        newTraces = new ArrayList<>();
        int index;
        while (count > 0) {
            index = random.nextInt(traces.size());
            while (traceIndex.contains(index)) {
                index = random.nextInt(traces.size());
            }
            newTraces.add(generate1DevTrace(devRate, traces.get(index)));
            traceIndex.add(index);
            traces.remove(index);
            --count;
        }
        System.out.println("本次选取迹长" + (traceLen == 0 ? "大于等于" + leastLength : traceLen) + "的下标集合为：" + traceIndex);
        return newTraces;
    }

    public ArrayList<ArrayList<String>> generateDevTraces(boolean ranDevRate,boolean withChgOrder, int devTraceNumPerTrace, int deviationRate, ArrayList<ArrayList<String>> traces) {
        ArrayList<ArrayList<String>> tempTraces = new ArrayList<>();
        ArrayList<String> tempTrace;
        for (ArrayList<String> trace : traces) {
            for (int i = 0; i < devTraceNumPerTrace; ++i) {
                tempTrace = new ArrayList<>(trace);
                if(withChgOrder){
                    tempTrace = generate1DevTrace(ranDevRate ? (random.nextInt(deviationRate / 10) + 1) * 10 : deviationRate, tempTrace);
                }else{
                    tempTrace = generate1DevTraceWithoutChgOrder(ranDevRate ? (random.nextInt(deviationRate / 10) + 1) * 10 : deviationRate, tempTrace);
                }
                tempTraces.add(tempTrace);
            }
        }
        return tempTraces;
    }

    public ArrayList<String> generate1DevTraceWithoutChgOrder(int deviationRate, ArrayList<String> trace) {
        if (deviationRate == 0) return trace;
        return removeOrAdd((int) Math.round(deviationRate * 0.01 * trace.size()), trace, 0);
    }

    public ArrayList<String> generate1DevTrace(int deviationRate, ArrayList<String> trace) {
        if (deviationRate == 0) return trace;
        return removeOrAddOrChgOrder((int) Math.round(deviationRate * 0.01 * trace.size()), trace, 0);
    }

    public ArrayList<String> removeOrAdd(int remainNum, ArrayList<String> trace, int devnum) {
        if (remainNum == 0) return trace;
        if (random.nextInt(2) == 0) {
            return removeRandomAct(remainNum, trace, devnum);
        } else {
            return addRandomAct(remainNum, trace, devnum);
        }
    }

    public ArrayList<String> removeOrAddOrChgOrder(int remainNum, ArrayList<String> trace, int devnum) {
        if (remainNum == 0) return trace;
        int index = random.nextInt(3);
        if (index == 0) {
            return removeRandomAct(remainNum, trace, devnum);
        } else if (index == 1) {
            return addRandomAct(remainNum, trace, devnum);
        } else {
            return changeOrder(remainNum, trace, devnum);
        }
    }

    public ArrayList<String> removeRandomAct(int remainNum, ArrayList<String> trace, int devnum) {
        if (remainNum == 0) return trace;
        int i, j;
        int bound = trace.size() - devnum;
        if (bound <= 0) return addRandomAct(remainNum, trace, devnum);
        int index = random.nextInt(bound);
        for (i = 0, j = 0; i <= index; ++j) {
            if (j == trace.size()) {
                System.out.println(i + " " + j + " " + remainNum + " " + devnum + " " + index + "" + trace);
            }
            if (!trace.get(j).contains("devTran")) ++i;
        }
        trace.remove(--j);
        return removeOrAdd(--remainNum, trace, devnum);
    }

    public ArrayList<String> addRandomAct(int remainNum, ArrayList<String> trace, int devnum) {
        if (remainNum == 0) return trace;
        trace.add(random.nextInt(trace.size() + 1), "devTran" + (devnum + 1));
        return removeOrAdd(--remainNum, trace, ++devnum);
    }

    public ArrayList<String> changeOrder(int remainNum, ArrayList<String> trace, int devnum) {
        if (remainNum == 0) return trace;
        int bound = trace.size() - devnum;
        if (bound <= 1) return removeOrAdd(remainNum, trace, devnum);
        int index1 = random.nextInt(bound);
        while (trace.get(index1).contains("devTran")) {
            index1 = random.nextInt(bound);
        }
        int index2 = random.nextInt(bound);
        while (trace.get(index2).contains("devTran") || index2 == index1) {
            index2 = random.nextInt(bound);
        }
        String tempAct = trace.get(index1);
        trace.set(index1, trace.get(index2));
        trace.set(index2, tempAct);
        return removeOrAddOrChgOrder(--remainNum, trace, devnum);
    }

    public HashMap<Integer, ArrayList<ArrayList<String>>> findAllTraces() {
        ArrayList<String> tempArr = new ArrayList<>();
        findNextMark(processNet.im, tempArr);
        printAllTracesLength();
        return tracesMap;
    }

    public void findNextMark(Mark fromMark, ArrayList<String> tempArr) {
        int i;
        Mark nextMark;
        if (fromMark == null) return;
        if (fromMark.equal(processNet.fm)) {
            ArrayList<String> temp = new ArrayList<>(tempArr);
            temp.removeIf("*"::equals);
            if (tracesMap.get(temp.size()) == null) {
                tracesMap.put(temp.size(), new ArrayList<>());
            }
            tracesMap.get(temp.size()).add(temp);
            ++totalCount;
        } else {
            for (i = 0; i < processNet.tnum; ++i) {
                nextMark = processNet.tranHappen(fromMark, i);
                if (nextMark != null) {
                    tempArr.add(processNet.tranArray[i]);
                    findNextMark(nextMark, tempArr);
                }
            }
        }
        if (!tempArr.isEmpty()) tempArr.remove(tempArr.size() - 1);
    }

    public void printTrace(ArrayList<String> trace) {
        System.out.print("迹长度：" + trace.size() + " ");
        for (String label : trace) {
            System.out.print(label);
            if (trace.indexOf(label) != trace.size() - 1) System.out.print("->");
        }
        System.out.println();
    }

    public void printAllTracesLength() {
        System.out.println("共有:" + totalCount + "条迹");
        for (Integer key : tracesMap.keySet()) {
            System.out.println("长度为:" + key + "的迹有:" + tracesMap.get(key).size() + "条");
        }
    }
}
