package ai.search;

import java.util.Collections;
import java.util.Vector;

public class Node {
	Vector<Vector<Integer>> state = new Vector<Vector<Integer>>();
	int size;
	String operation;
	Node parent;

	public Vector<Integer> getBlankPosition() {
		Vector<Integer> v = new Vector<Integer>();
		for(int i = 0;i < size;i++) {
			for(int j = 0;j < size;j++) {
				if(state.get(i).get(j) == 0) {
					v.add(i);
					v.add(j);
				}
			}
		}

		return v;
	}

	public boolean applicable(String op) {
		Vector<Integer> pos = getBlankPosition();
		Integer x = pos.get(0);
		Integer y = pos.get(1);
		if(op == "left") {
			if(y <= 0) return false;
			else return true;
		}
		else if(op == "right") {
			if(y >= size - 1) return false;
			else return true;
		}
		else if(op == "up") {
			if(x <= 0) return false;
			else return true;
		}
		else if(op == "down") {
			if(x >= size - 1) return false;
			else return true;
		}
		else return false;
	}
	
	public Node applyOp(String op) {
		Node child = new Node();
		child.parent = this;
		child.operation = op;
		child.size = size;
		Vector<Vector<Integer>> newstate = new Vector<Vector<Integer>>();
		for(int i = 0;i < size;i++) {
			newstate.add(new Vector<Integer>());
		}
		for(int i = 0; i < size;i++) {
			for(int j = 0;j < size;j++) {
				newstate.get(i).add(state.get(i).get(j));
			}
		}
		Vector<Integer> pos = getBlankPosition();
		Integer x = pos.get(0);
		Integer y = pos.get(1);
		if(op == "left") {
			if(y <= 0) return null;
			Collections.swap(newstate.get(x), y, y - 1);
		}
		else if(op == "right") {
			if(y >= size - 1) return null;
			Collections.swap(newstate.get(x), y, y + 1);
		}
		else if(op == "up") {
			if(x <= 0) return null;
			Integer temp = newstate.get(x).get(y);
			newstate.get(x).set(y, newstate.get(x-1).get(y));
			newstate.get(x-1).set(y, temp);
		}
		else if(op == "down") {
			if(x >= size - 1) return null;
			Integer temp = newstate.get(x).get(y);
			newstate.get(x).set(y, newstate.get(x + 1).get(y));
			newstate.get(x+1).set(y, temp);
		}
		else return null;
		child.state = newstate;
		return child;
	}

	public void print() {
		System.out.println("Operation: " + operation);
		System.out.println("State:");
		for(int i = 0;i < size;i++) {
			for(int j = 0;j < size;j++) {
				System.out.print(state.get(i).get(j) + " ");
			}
			System.out.println();
		}
		/*
		System.out.println("Parent state:");
		if(parent == null) System.out.println("null");
		else {
			for(int i = 0;i < size;i++) {
				for(int j = 0;j < size;j++) {
					System.out.print(parent.state.get(i).get(j) + " ");
				}
				System.out.println();
			}
		}
		*/
	}
	
	public static void main(String[] args) {
		int a[] = {1, 2, 3, 4 ,6, 0, 7, 5, 8};
		Node n = new Node();
		n.size = 3;
		n.state = new Vector<Vector<Integer>>();
		for(int i = 0;i < n.size;i++) {
			n.state.add(new Vector<Integer>());
		}
		for(int i = 0;i < n.size;i++) {
			for(int j =0;j<n.size;j++) {
				n.state.get(i).add(a[i*n.size + j]);
			}
		}
		/*
		n.size = 3;
		n.state = new Vector<Vector<Integer>>();
		for(int i = 0;i < n.size;i++) {
			n.state.add(new Vector<Integer>());
		}
		for(int i = 0;i < n.size;i++) {
			for(int j =0;j<n.size;j++) {
				n.state.get(i).add((j*n.size + i + 1)%(n.size*n.size));
			}
		}

		Node m = n.applyOp("up");
		m.print();
		*/
		Node m = null;
		String moves[] = {"left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "down", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "down", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "up", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "right", "right", "down", "left", "left", "up", "up", "right", "right", "down", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right", "up", "left", "left", "down", "right", "right"};
		for(int i = 0;i < moves.length;i++) {
			m = n.applyOp(moves[i]);
		}
		m.print();
	}

}

