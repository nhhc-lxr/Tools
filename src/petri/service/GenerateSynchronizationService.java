package petri.service;

import petri.entity.Mark;
import petri.entity.PetriNet;
import petri.entity.TransitionOfCompositionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 同步合成模型生成
 */
public class GenerateSynchronizationService {

    /**
     * -------------eventNet------------------
     * ||||| t1' | t2'
     * |p1'
     * |p2'
     * |p3'
     * -------------processNet----------------
     * ||||| t1 | t2 | t3
     * |p1
     * |p2
     * -------------syncNet-----------------
     * ||||| (t1',t1) | (t2',t2) | (>>,t3)
     * |p1'
     * |p2'
     * |p3'
     * |p1
     * |p2
     */
    public PetriNet getSynchronizationOfEventNetAndProcessNet(PetriNet eventNet, PetriNet processNet) {
        int i, j, k;
        int syncTnum = 0;

        long start = System.currentTimeMillis();
        //初始化sync变迁数组
        ArrayList<TransitionOfCompositionModel> tocmArray = new ArrayList<>();
        TransitionOfCompositionModel tran;

        //同名变迁所对应的eventNet中的变迁下标与processNet中的变迁下标
        ArrayList<Integer> syncTranInEvent = new ArrayList<>();
        ArrayList<Integer> syncTranInProcess = new ArrayList<>();
        //计算同名变迁数量
        List<String> processNetTrans = Arrays.asList(processNet.tranArray);
        for (i = 0; i < eventNet.tnum; ++i) {
            if (processNetTrans.contains(eventNet.tranArray[i])) {
                int index = processNetTrans.indexOf(eventNet.tranArray[i]);
                //tran = new TransitionOfCompositionModel("t" + (i + 1) + "'", "t" + (index + 1), eventNet.tranArray[i]);
                //tocmArray.add(tran);
                syncTranInEvent.add(i);
                syncTranInProcess.add(index);
                ++syncTnum;
            }
        }

        //初始化关联矩阵
        int pnum = eventNet.pnum + processNet.pnum;
        int tnum = eventNet.tnum + processNet.tnum - syncTnum;
        int[][] array = new int[pnum][tnum];

        //初始化sync库所下标数组
        String[] placeIndex = new String[pnum];
        System.arraycopy(eventNet.placeIndex, 0, placeIndex, 0, eventNet.pnum);
        System.arraycopy(processNet.placeIndex, 0, placeIndex, eventNet.pnum, processNet.pnum);

        for (i = 0; i < eventNet.tnum; ++i) {
            for (j = 0; j < eventNet.pnum; ++j) {
                array[j][i] = eventNet.array[j][i];
            }
            if (syncTranInEvent.contains(i)) {
                tran = new TransitionOfCompositionModel("t" + (i + 1) + "'", "t" + (syncTranInProcess.get(syncTranInEvent.indexOf(i)) + 1), eventNet.tranArray[i]);
            } else {
                tran = new TransitionOfCompositionModel("t" + (i + 1) + "'", ">>", eventNet.tranArray[i]);
            }
            tocmArray.add(tran);
        }

        //初始化关联矩阵中的process变迁列
        for (i = 0, k = eventNet.tnum; i < processNet.tnum; ++i) {
            if (syncTranInProcess.contains(i)) {
                for (j = eventNet.pnum; j < pnum; ++j) {
                    array[j][syncTranInEvent.get(syncTranInProcess.indexOf(i))] = processNet.array[j - eventNet.pnum][i];
                }
            } else {
                for (j = eventNet.pnum; j < pnum; ++j) {
                    array[j][k] = processNet.array[j - eventNet.pnum][i];
                }
                tran = new TransitionOfCompositionModel(">>", "t" + (i + 1), processNet.tranArray[i]);
                tocmArray.add(tran);
                ++k;
            }
        }

        int[] ims = new int[pnum];
        int[] fms = new int[pnum];
        System.arraycopy(eventNet.im.m, 0, ims, 0, eventNet.im.m.length);
        System.arraycopy(processNet.im.m, 0, ims, eventNet.im.m.length, processNet.im.m.length);
        System.arraycopy(eventNet.fm.m, 0, fms, 0, eventNet.fm.m.length);
        System.arraycopy(processNet.fm.m, 0, fms, eventNet.fm.m.length, processNet.fm.m.length);
        Mark ic = new Mark(ims);
        Mark fc = new Mark(fms);
        long end = System.currentTimeMillis();
        return new PetriNet(array, pnum, tnum, ic, fc, tocmArray, placeIndex, end - start);
    }
}
