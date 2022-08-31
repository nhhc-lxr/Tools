package test.tools;

/**
 * 栈
 */
public class SqStack {

	int maxSize = 300;
	private char data[];
	private int top;
	
	public SqStack() {
		data = new char[maxSize];
		top = -1;
	}

	public void push(char x) {
		if(top < maxSize-1) {
			data[++top] = x;
		}
	}
	
	public char pop() {
		if(top > -1) {
			return data[top--];
		}
		return '0';
	}
}
