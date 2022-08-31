package test.tools;

public class EightQueen {

	private int count = 1;

	public static void main(String[] args) {
		EightQueen eightq = new EightQueen();
		int chess[][] = new int[8][8];// 初始化空数组
		eightq.eightQueen(0, chess);
		System.out.println("执行完毕");
	}

	void eightQueen(int row, int chess[][]) {// row起始行
		if (row == 8) {
			System.out.println("\n第" + count + "种解法：");
			for (int c[] : chess) {
				for (int c1 : c) {
					System.out.print(c1 + " ");
				}
				System.out.println();
			}
			count++;
		} else {
			for (int i = 0; i < 8; i++) {
				if (!inDanger(row, i, chess)) {//判断该点是否危险
					chess[row][i] = 1;
					eightQueen(row + 1, chess);
					chess[row][i] = 0;
				}
			}
		}
	}

	boolean inDanger(int row, int col, int chess[][]) {
		for (int i = 0; i < 8; i++) {
			if (chess[row][i] == 1 || chess[i][col] == 1) {
				return true;
			}
			for (int j = 0; j < 8; j++) {
				if (((i - j == row - col) || i + j == row + col) && chess[i][j] == 1) {
					return true;
				}
			}
		}
		return false;
	}
}
