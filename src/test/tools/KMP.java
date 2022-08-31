package test.tools;

import java.util.Scanner;

public class KMP {

	private char data[];

	private int next[];

	public KMP(int length) {
		this.data = new char[length + 1];
		this.next = new int[length + 1];
	}

	public static void main(String[] args) {

		System.out.println("请输入需要匹配的字符串：");
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		scanner.close();
		KMP kmp = new KMP(str.length());
		for (int i = 0; i < str.length(); i++) {
			kmp.data[i + 1] = str.charAt(i);
		}
		kmp = kmp.calNextArray(kmp);
		for (char c : kmp.data) {
			System.out.print(c);
		}
		System.out.println();
		for (int i : kmp.next) {
			System.out.print(i);
		}
		System.out.println();
	}

	KMP calNextArray(KMP kmp) {
		kmp.next[0] = 0;
		int i = 1, j = 0;
		while (i < kmp.data.length - 1) {
			if (j == 0 || kmp.data[i] == kmp.data[j]) {
				i++;
				j++;
				if (kmp.data[i] != kmp.data[j]) {
					kmp.next[i] = j;
				} else {
					kmp.next[i] = kmp.next[j];
				}
			} else {
				j = kmp.next[j];
			}
		}
		return kmp;
	}

}
