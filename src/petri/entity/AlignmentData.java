package petri.entity;

import java.util.ArrayList;

public class AlignmentData {
    public long alignTime;
    public int reachableMarkcount;
    public ArrayList<TransitionOfCompositionModel> alignment;
    public int totalCost;
}