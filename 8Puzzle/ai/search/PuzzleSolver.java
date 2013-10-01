package ai.search;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

public class PuzzleSolver {
	
	Node goal;
	int nodes_visited;
	PuzzleSolver(ArrayList<Integer> dest) {
		goal = new Node();
		goal.state = new Vector<Vector<Integer>>();
		goal.size = (int)Math.sqrt(dest.size());
		for(int i = 0;i < goal.size;i++) {
			goal.state.add(new Vector<Integer>());
		}
		for(int i = 0;i < goal.size;i++) {
			for(int j = 0;j < goal.size;j++) {
				
				goal.state.get(i).add(dest.get(i*goal.size + j));;
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
		nodes_visited++;
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
	
	private int h1(Node n) {
		int cost = 0;
		Vector<Vector<Integer>> s1 = n.state;
		Vector<Vector<Integer>> s2 = goal.state;
		for(int i = 0;i < n.size;i++) {
			for(int j = 0;j < n.size;j++) {
				if(s1.get(i).get(j) != s2.get(i).get(j))cost++;
			}
		}
		return cost;
	}
	
	protected int h2(Node n) {
		int cost = 0;
		for(int i = 0;i < n.size;i++) {
			for(int j = 0;j < n.size;j++) {
				Vector<Integer> pos = goal.getTilePosition(n.state.get(i).get(j));
				Integer i1 = pos.get(0);
				Integer j1 = pos.get(1);
				cost += Math.abs(i1 - i) + Math.abs(j1 - j);
			}
		}
		return cost;
	}
	
	private Node bfs(Node root) {
		nodes_visited = 0;
		ArrayDeque<Node> frontier = new ArrayDeque<Node>();
		ArrayList<Node> explored = new ArrayList<Node>();
		Node soln = null;
		frontier.add(root);
		while(true) {
			Node n = frontier.poll();
			if(n == null) return null;
			//System.out.println("Visited node:");
			//n.print();
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
	
	private Node dfs(Node root) {
		nodes_visited = 0;
		ArrayDeque<Node> frontier = new ArrayDeque<Node>();
		ArrayList<Node> explored = new ArrayList<Node>();
		Node soln = null;
		frontier.add(root);
		while(true) {
			Node n = frontier.poll();
			if(n == null) return null;
			//System.out.println("Visited node:");
			//n.print();
			if(goalCheck(n)){
				return n;
			}
			for(String op : ops) {
				if(n.applicable(op)) {
					soln = n.applyOp(op);
					if(!checkDup(explored,soln)) frontier.addFirst(soln);
				}
			}
			explored.add(n);
		}
	}
	
	private Node dls(Node root, int limit) {
		ArrayDeque<DNode> frontier = new ArrayDeque<DNode>();
		ArrayList<Node> explored = new ArrayList<Node>();
		Node soln = null;
		DNode start = new DNode(root, 1);
		frontier.add(start);
		int d = 0;
		while(true) {
			DNode dn = frontier.poll();
			if(dn == null) return null;
			d = dn.depth;
			Node n = dn.node;
			//System.out.println("Visited node:");
			//n.print();
			if(goalCheck(n)){
				return n;
			}
			for(int i = 0;i < ops.length;i++) {
				String op  = ops[i];
				if(n.applicable(op)) {
					soln = n.applyOp(op);
					if(!checkDup(explored,soln) && d + 1 <= limit) frontier.addFirst(new DNode(soln, d + 1));
				}
			}
			explored.add(n);
		}
	}
	
	private Node ids(Node root) {
		nodes_visited = 0;
		int limit = 1;
		Node soln = null;
		while(soln == null) {
			soln = dls(root, limit);
			limit++;
		}
		System.out.println("limit" + limit);
		return soln;
	}	
	
	private Node greedy(Node root, final String h) {
		nodes_visited = 0;
		Node soln = null;
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(10, new Comparator<Node>(){
			@Override
			public int compare(Node n1, Node n2) {
				if(h == "h1") {
					if(h1(n1) > h1(n2)) return 1;
					if(h1(n1) < h1(n2)) return -1;
					return 0;
				}
				if(h == "h2") {
					if(h2(n1) > h2(n2)) return 1;
					if(h2(n1) < h2(n2)) return -1;
					return 0;
				}
				return 0;
			}});
		ArrayList<Node> explored = new ArrayList<Node>();
		frontier.add(root);
		while(true) {
			Node n = frontier.poll();
			if(n == null)return null;
			//System.out.println("Visited");
			//n.print();
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
	
	private Node astar(Node root, final String h) {
		PriorityQueue<DNode> frontier = new PriorityQueue<DNode>(10, new Comparator<DNode>(){
			@Override
			public int compare(DNode n1, DNode n2) {
				if(h == "h1") {
					if(n1.depth + h1(n1.node) > n2.depth + h1(n2.node)) return 1;
					if(n1.depth + h1(n1.node) < n2.depth + h1(n2.node)) return -1;
					return 0;
				}
				if(h == "h2") {
					if(n1.depth + h2(n1.node) > n2.depth + h2(n2.node)) return 1;
					if(n1.depth + h2(n1.node) < n2.depth + h2(n2.node)) return -1;
					return 0;
				}
				return 0;
			}});
		ArrayList<Node> explored = new ArrayList<Node>();
		Node soln = null;
		DNode start = new DNode(root, 1);
		frontier.add(start);
		int d = 0;
		while(true) {
			DNode dn = frontier.poll();
			if(dn == null) return null;
			d = dn.depth;
			Node n = dn.node;
			//System.out.println("Visited node:");
			//n.print();
			if(goalCheck(n)){
				return n;
			}
			for(String op : ops) {
				if(n.applicable(op)) {
					soln = n.applyOp(op);
					if(!checkDup(explored,soln)) frontier.add(new DNode(soln, d + 1));
				}
			}
			explored.add(n);
		}
		
	}
	
	private Node ida(Node root, final String h) {
		PriorityQueue<DNode> frontier = new PriorityQueue<DNode>(10, new Comparator<DNode>(){
			@Override
			public int compare(DNode n1, DNode n2) {
				if(h == "h1") {
					if(n1.depth + h1(n1.node) > n2.depth + h1(n2.node)) return 1;
					if(n1.depth + h1(n1.node) < n2.depth + h1(n2.node)) return -1;
					return 0;
				}
				if(h == "h2") {
					if(n1.depth + h2(n1.node) > n2.depth + h2(n2.node)) return 1;
					if(n1.depth + h2(n1.node) < n2.depth + h2(n2.node)) return -1;
					return 0;
				}
				return 0;
			}});
		ArrayList<Node> explored = new ArrayList<Node>();
		Node soln = null;
		DNode start = new DNode(root, 1);
		frontier.add(start);
		int d = 0;
		int flimit = (h == "h1" ? h1(start.node) : h2(start.node));
		while(true) {
			DNode dn = frontier.poll();
			if(dn == null) {
				/*
				Node child;
				boolean covered = checkDup(explored, root);
				for(String op : ops) {
					if(root.applicable(op)) {
						child = root.applyOp(op);
						if(!checkDup(explored, child)) covered = false;
					}
				}
				if(covered == true) return null;
				*/
				frontier.add(start);
				d = 0;
				flimit++;
				continue;
			}
			d = dn.depth;
			Node n = dn.node;
			//System.out.println("Visited node:");
			//n.print();
			if(goalCheck(n)){
				return n;
			}
			for(int i = 0;i < ops.length;i++) {
				String op = ops[i];
				if(n.applicable(op)) {
					soln = n.applyOp(op);
					int h_cost;
					if(h == "h1") h_cost = h1(soln);
					else h_cost = h2(soln);
					if(!checkDup(explored,soln) && d + 1 + h_cost < flimit)	frontier.add(new DNode(soln, d + 1));
				}
			}
			explored.add(n);
		}
	}
	
	public static void main(String[] args) {
		
		ArrayList<Integer> dest = new ArrayList<Integer>();
		ArrayList<Integer> initial = new ArrayList<Integer>();
		
		Scanner console = new Scanner(System.in);
		System.out.println("Enter the goal state:");
		Scanner in = new Scanner(console.nextLine());
		in.useDelimiter("[() ]");
		while(in.hasNextInt()) {
			dest.add(in.nextInt());
		}
		in.close();
		
		System.out.println("Enter a search method:");
		in = new Scanner(console.nextLine());
		in.useDelimiter("[() ]");
		String algo = in.next();
		while(in.hasNextInt()) {
			initial.add(in.nextInt());
		}
		in.close();
		
		PuzzleSolver p = new PuzzleSolver(dest);
		System.out.println("Goal:");
		p.goal.print();
		//1, 3, 4, 8, 6, 2, 7, 0, 5
		//2, 8, 1, 0, 4, 3, 7, 6, 5
		//5, 6, 7, 4, 0, 8, 3, 2, 1
		
		Node n = new Node();
		n.size = (int)Math.sqrt(initial.size());
		n.state = new Vector<Vector<Integer>>();
		for(int i = 0;i < n.size;i++) {
			n.state.add(new Vector<Integer>());
		}
		for(int i = 0;i < n.size;i++) {
			for(int j =0;j<n.size;j++) {
				n.state.get(i).add(initial.get(i*n.size + j));
			}
		}
		System.out.println("Start:");
		n.print();
		Node m;
		ArrayDeque<String> moves = new ArrayDeque<String>();
	
		m = p.bfs(n);
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println("bfs:"+moves.toString());
		
		moves.clear();
		m = p.ids(n);
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println("ids:"+moves.toString());
		
		moves.clear();
		m = p.greedy(n, "h1");
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println("greedy1:"+moves.toString());
		
		moves.clear();
		m = p.greedy(n, "h2");
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println("greedy2"+moves.toString());
		
		moves.clear();
		m = p.astar(n, "h1");
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println("astar1:"+moves.toString());
		
		moves.clear();
		m = p.astar(n, "h2");
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println("astar2:"+moves.toString());

		moves.clear();
		m = p.ida(n, "h1");
		while(m.parent != null) {
			moves.addFirst(m.operation);
			m = m.parent;
		}
		System.out.println("astar2"+moves.toString() + "\nVisited "+ p.nodes_visited + "nodes");
	}

}
