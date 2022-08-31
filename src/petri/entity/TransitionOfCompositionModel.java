package petri.entity;

public class TransitionOfCompositionModel {
    //EventNet transition
    public String et;
    //ProcessNet transition
    public String pt;

    public String label;

    public TransitionOfCompositionModel(String et, String pt, String label) {
        this.et = et;
        this.pt = pt;
        this.label = label;
    }

    /**
     * 计算该变迁的likelihood cost
     */
    public int getClc() {
//        if ((!">>".equals(et) && !">>".equals(pt) && !"*".equals(label)) || (">>".equals(et) && !">>".equals(pt) && "*".equals(label))) {
//            return 0;
//        }
//        return 1;

        if ((!">>".equals(et) && !">>".equals(pt) && !"*".equals(label))) {
            return 0;
        }
        return 1;
    }

    public void printTop() {
        System.out.print("(" + et + "," + pt + ")");
    }

    public void printTopWithLabel() {
        System.out.print("(" + et + "," + pt + "|" + label + ")");
    }
}
