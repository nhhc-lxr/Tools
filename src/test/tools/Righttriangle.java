package test.tools;

import java.util.Scanner;

public class Righttriangle {

	public static void main(String[] args) {
		System.out.println("请输入行数：");
		Scanner scanner = new Scanner(System.in);
		int line = scanner.nextInt();
		scanner.close();
		for (int i = 0; i < line; i++) {
			for (int j = 0; j < line; j++) {
				if (i >= j) {
					System.out.print("*");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}

}
