package test.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Aco {

    private Ant[] ants;            //蚂蚁数组
    private int antNum;            //蚂蚁数量
    private int cityNum;           //城市数量
    private int generation;        //迭代次数
    private double[][] pheromone;  //信息素矩阵
    private double[][] distance;   //城市间距离矩阵
    private double bestLength;     //最佳长度
    private int[] bestTour;        //最佳路径
    private double alpha;          //信息素重要程度系数
    private double beta;           //城市间距离重要程度系数
    private double rho;            //信息素挥发系数
    private double Q;              //蚂蚁循环一周在路径上释放的信息素总量

    /**
     * @param cityNum     城市数量
     * @param antNum      蚂蚁数量
     * @param generation  迭代次数
     * @param alpha       信息素重要程度系数
     * @param beta        城市间距离重要程度系数
     * @param rho         信息素挥发系数
     * @param Q           蚂蚁循环一周在路径上释放的信息素总量
     */
    public Aco(int cityNum, int antNum, int generation, double alpha, double beta, double rho, double Q) {
        this.cityNum = cityNum;
        this.antNum = antNum;
        this.generation = generation;
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.Q = Q;
        this.ants = new Ant[antNum];

    }

    /**
     * main函数
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String tspData = System.getProperty("user.dir") + "/resources/tsp14.txt";
        //String tspData = System.getProperty("user.dir") + "/resources/att48.txt";

        // 平均值、最大值、最小值
        double avg = 0;
        double max = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            Aco aco = new Aco(14, 22, 200, 1.0, 4.0, 0.2, 20);
            //Aco aco = new Aco(48, 100, 1000, 1.0, 5.0, 0.5, 1);
            aco.initAco(tspData);
            aco.solve();
            avg += aco.getBestLength();
            if (aco.getBestLength() > max) {
                max = aco.getBestLength();
            }
            if (aco.getBestLength() < min) {
                min = aco.getBestLength();
            }
        }
        System.out.println("平均:" + String.format("%.2f", avg / 10) + "-" + "最短:" + String.format("%.2f", min) + "-" + "最长:" + String.format("%.2f", max));
    }

    /**
     * 初始化Aco
     *
     * @param filename      文件路径
     * @throws IOException
     */
    public void initAco(String filename) throws IOException {
        //读取本地txt文件中各城市的(x,y)坐标
        double x[];
        double y[];
        distance = new double[cityNum][cityNum];
        String str;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        x = new double[cityNum];
        y = new double[cityNum];
        for (int i = 0; i < cityNum; ++i) {
            str = reader.readLine();
            x[i] = Double.valueOf(str.split(" ")[1]);
            y[i] = Double.valueOf(str.split(" ")[2]);
        }
        //计算城市间距离矩阵
        for (int i = 0; i < cityNum - 1; ++i) {
            distance[i][i] = 0;
            for (int j = i + 1; j < cityNum; ++j) {
                distance[i][j] = Math.sqrt((Math.pow(x[i] - x[j], 2) + Math.pow((y[i] - y[j]), 2)));
                distance[j][i] = distance[i][j];
            }
        }
        //初始化信息素矩阵
        pheromone = new double[cityNum][cityNum];
        //double startp = 1.0 / ((cityNum - 1) * antNum);
        for (int i = 0; i < cityNum; ++i) {
            for (int j = 0; j < cityNum; ++j) {
                pheromone[i][j] = 1;
            }
        }
        //初始化最佳长度和最佳路径
        bestLength = Double.MAX_VALUE;
        bestTour = new int[cityNum + 1];
        //初始化蚂蚁
        Ant.initStatic(cityNum, distance, alpha, beta);
        for (int i = 0; i < antNum; ++i) {
            ants[i] = new Ant();
            ants[i].initAnt();
        }
    }

    /**
     * 更新信息素矩阵
     */
    private void updatePhoromone() {
        //挥发信息素
        for (int i = 0; i < cityNum; ++i) {
            for (int j = 0; j < cityNum; ++j) {
                pheromone[i][j] = pheromone[i][j] * (1 - rho);
            }
        }
        //把每只蚂蚁留下的信息素更新至信息素矩阵中
        for (int i = 0; i < cityNum; ++i) {
            for (int j = 0; j < cityNum; ++j) {
                for (int k = 0; k < antNum; ++k) {
                    pheromone[i][j] += ants[k].getDelta()[i][j];
                }
            }
        }
    }

    /**
     * 解决tsp问题
     */
    private void solve() {
        //迭代generation次
        for (int gen = 0; gen < generation; ++gen) {
            //每只蚂蚁都走遍所有城市
            for (int i = 0; i < antNum; ++i) {
                //随机选取每一步
                for (int j = 0; j < cityNum - 1; ++j) {
                    ants[i].selectNextCity(pheromone);
                }
                //取出相对最佳路径
                if (ants[i].getTourLength() < bestLength) {
                    bestLength = ants[i].getTourLength();
                    for (int j = 0; j <= cityNum; ++j) {
                        bestTour[j] = ants[i].getVisited().get(j);
                    }
                }
                //走过的路径产生信息素变化
                double q = Q / ants[i].getTourLength();
                for (int j = 0; j < cityNum; ++j) {
                    ants[i].getDelta()[ants[i].getVisited().get(j)][ants[i].getVisited().get(j + 1)] = q;
                    ants[i].getDelta()[ants[i].getVisited().get(j + 1)][ants[i].getVisited().get(j)] = q;
                }
            }
            //该轮迭代完成后每只蚂蚁都走完一遍,更新信息素矩阵
            updatePhoromone();
            //重新初始化蚂蚁位置
            for (Ant ant : ants) {
                ant.initAnt();
            }
        }
        //打印
        printOptimal();
    }

    /**
     * 打印最佳路径
     */
    private void printOptimal() {
        System.out.println("最佳长度:" + String.format("%.2f", bestLength));
        System.out.print("最佳路径:" + (bestTour[0] + 1));
        for (int i = 1; i <= cityNum; ++i) {
            System.out.print("-" + (bestTour[i] + 1));
        }
        System.out.println();
    }

    public double getBestLength() {
        return bestLength;
    }
}

class Ant {

    private static int cityNum;          //城市数量
    private static double[][] distance;  //城市间距离矩阵
    private static double alpha;         //信息素重要程度系数
    private static double beta;          //城市间距离重要程度系数
    private ArrayList<Integer> visited;  //已访问过的城市
    private ArrayList<Integer> allowed;  //允许访问的城市
    private double[][] delta;            //信息素变化矩阵

    private double tourLength;              //该蚂蚁的路径长度
    private int startCity;               //起点城市
    private int currentCity;             //当前城市

    /**
     * 构造方法
     */
    public Ant() {
        tourLength = 0;
    }

    /**
     * 初始化静态变量
     *
     * @param cityNum  城市数量
     * @param distance 城市间距离矩阵
     * @param alpha    信息素重要程度系数
     * @param beta     城市间距离重要程度系数
     */
    public static void initStatic(int cityNum, double[][] distance, double alpha, double beta) {
        Ant.cityNum = cityNum;
        Ant.distance = distance;
        Ant.alpha = alpha;
        Ant.beta = beta;
    }

    /**
     * 初始化个体蚂蚁
     */
    public void initAnt() {
        //初始化已访问矩阵,允许访问矩阵,信息素变化量矩阵为空
        visited = new ArrayList<Integer>();
        allowed = new ArrayList<Integer>();
        delta = new double[cityNum][cityNum];
        //随机找一个城市作为起始城市
        Random random = new Random();
        startCity = random.nextInt(cityNum);
        //当前所在城市为起始城市
        currentCity = startCity;
        //当前城市加入已访问矩阵
        visited.add(startCity);
        //剩余城市均加入允许访问矩阵
        for (int i = 0; i < cityNum; ++i) {
            if (i != startCity) {
                allowed.add(i);
            }
        }
    }

    /**
     * 根据信息素矩阵选择下一个城市
     *
     * @param pheromone 信息素矩阵
     */
    public void selectNextCity(double[][] pheromone) {
        double[] probability = new double[cityNum];
        double sum = 0;

        //求分母
        for (int a : allowed) {
            sum += Math.pow(pheromone[currentCity][a], alpha) * Math.pow(1.0 / distance[currentCity][a], beta);
        }
        //统计各可达城市概率
        for (int a : allowed) {
            probability[a] = Math.pow(pheromone[currentCity][a], alpha) * Math.pow(1.0 / distance[currentCity][a], beta) / sum;
        }

        //轮盘赌法随机选择一个可访问城市
        int selectedCity = 0;
        Random random = new Random();
        double result = random.nextDouble();
        double sumP = 0.0;
        for (int i = 0; i < cityNum; ++i) {
            sumP += probability[i];
            if (sumP >= result) {
                selectedCity = i;
                break;
            }
        }

        //设为已访问并移动至该城市,统计路径长度
        if (allowed.contains(selectedCity)) {
            allowed.remove(allowed.indexOf(selectedCity));
        }
        visited.add(selectedCity);
        tourLength += distance[currentCity][selectedCity];
        currentCity = selectedCity;
        //如果到达最后一个城市,则将返回起点城市的路径长度统计进去
        if (allowed.isEmpty()) {
            tourLength += distance[currentCity][startCity];
            visited.add(startCity);
        }
    }


    public ArrayList<Integer> getVisited() {
        return visited;
    }

    public double[][] getDelta() {
        return delta;
    }

    public double getTourLength() {
        return tourLength;
    }

}
