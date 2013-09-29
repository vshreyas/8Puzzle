package ai.search;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Vector;

public class PuzzleSolver {
	
	Node goal;
	
	PuzzleSolver() {
		goal = new Node();
		goal.state = new Vector<Vector<Integer>>();
		goal.size = 3;
		for(int i = 0;i < goal.size;i++) {
			goal.state.add(new Vector<Integer>());
		}
		for(int i = 0;i < goal.size;i++) {
			for(int j = 0;j < goal.size;j++) {
				goal.state.get(i).add((i*goal.size + j + 1)%(goal.size*goal.size));
			}
		}
	}
	
	static String[] ops = {"up", "down", "left", "right"};
	
	private boolean checkDup(ArrayList<Node> explored, Node soln) {
		boolean isDuplicate = false;
		for(Node n:explored) {
			boolean equal = true;
			for(int i = 0;i < soln.size; i++) {
				for(int j =0 ;j<soln.size;j++) {
					if(soln.state.get(i).get(j) != n.state.get(i).get(j)) {
						equal = false; 
					}
				}
			}
			isDuplicate |= equal;
		}
		return isDuplicate;
	}
	
	private boolean goalCheck(Node soln) {
		if(soln.size != goal.size) return false;
		for(int i = 0;i < soln.size; i++) {
			for(int j =0 ;j<soln.size;j++) {
				if(soln.state.get(i).get(j) != goal.state.get(i).get(j)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private Node bfs(Node root) {
		ArrayDeque<Node> frontier = new ArrayDeque<Node>();
		ArrayList<Node> explored = new ArrayList<Node>();
		Node soln = null;
		frontier.add(root);
		int d  = 0;
		while(true) {
			Node n = frontier.poll();
			if(n == null) return null;
			System.out.println("Visited node:");
			n.print();
			if(goalCheck(n)){
				return n;
			}
			for(String op : ops) {
				if(n.applicable(op)) {
					soln = n.applyOp(op);
					if(!checkDup(explored,soln)) frontier.add(soln);
				}
			}
			explored.add(n);
		}
	}
	
	class DNode{
		Node node;
		int depth;
		DNode(Node n, int d) {
			node = n;
			depth = d;
		}
	}
	
	private Node dls(Node root, int limit) {
		ArrayDeque<DNode> frontier = new ArrayDeque<DNode>();
		ArrayList<Node> explored = new ArrayList<Node>();
		Node soln = null;

		DNode start = new DNode(root, 1);

		frontier.add(start);
		int d = 0;
		while(d < limit) {
			DNode dn = frontier.poll();
			if(dn == null) return null;
			d = dn.depth;
			Node n = dn.node;
			System.out.println("Visited node:");
			n.print();
			if(goalCheck(n)){
				return n;
			}
			for(String op : ops) {
				if(n.applicable(op)) {
					soln = n.applyOp(op);
					if(!checkDup(explored,soln)) frontier.addFirst(new DNode(soln, d + 1));
				}
			}
			explored.add(n);
		}
		return null;
	}
	
	private Node ids(Node root) {
		return null;
	}
	
	
	public static void main(String[] args) {
		PuzzleSolver p = new PuzzleSolver();
		System.out.println("Goal:");
		p.goal.print();
		int a[] = {1, 2, 3, 0 ,5, 6, 4, 7, 8};
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
		System.out.println("Start:");
		n.print();
		Node m = p.bfs(n);
		ArrayDeque<String> moves = new ArrayDeque<String>();
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println(moves.toString());
		moves.clear();
		
		m = p.dls(n, 100);
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println(moves.toString());
	}

}
