/*
 */

import java.io.*;
import java.util.*;

class tsp_edge {
    private class Edge implements Comparable<Edge> {
	public int node1;
	public int node2;
	public int mileage;
	public Edge(int node1, int node2, int mileage) {
	    this.node1 = node1;
	    this.node2 = node2;
	    this.mileage = mileage;
	}
	@Override
	public String toString() {
	    return "<("+node1+","+node2+"),"+mileage+">";
	}
	@Override
	public int compareTo(Edge compareEdge) {
	    if(mileage == compareEdge.mileage) {
		return 0;
	    } else if(mileage < compareEdge.mileage) {
		return -1;
	    } else {
		return 1;
	    }
	}
    }
    private class State {
	public List<Set<Edge>> visitededge;
	public List<Set<Integer>> visitednode;
	public List<Edge> unvisited;
	public int mileage;
	public State(State state) {
	    visitededge = new LinkedList<Set<Edge>>();
	    for(Set<Edge> eset : state.visitededge) {
		visitededge.add(new HashSet<Edge>(eset));
	    }
	    visitednode = new LinkedList<Set<Integer>>();
	    for(Set<Integer> nset : state.visitednode) {
		visitednode.add(new HashSet<Integer>(nset));
	    }
	    unvisited = new LinkedList<Edge>(state.unvisited);
	    mileage = state.mileage;
	}
	public State(List<Edge> unvisited) {
	    this.visitededge = new LinkedList<Set<Edge>>();
	    this.visitednode = new LinkedList<Set<Integer>>();
	    this.unvisited = new LinkedList<Edge>(unvisited);
	    this.mileage = 0;
	}
	public int visitededges() {
	    int sum = 0;
	    for(Set<Edge> eset : visitededge) {
		sum += eset.size();
	    }
	    return sum;
	}
	@Override
	public String toString() {
	    return "<"+visitededge+visitednode+unvisited+mileage+">";
	}
    }

    public tsp_edge() {
    }
    private List<Edge> formAjacencyList(Map<String, Integer> mileage, List<String> instance) {
	List<Edge> list = new LinkedList<Edge>();
	for(int i=0; i<instance.size(); i++) {
	    for(int j=0; j<i; j++) {
		String key = new String(instance.get(i)+" "+instance.get(j));
		list.add(new Edge(i, j, mileage.get(key).intValue()));
	    }
	}
	return list;
    }
    public List<Integer> getTravel(Map<String, Integer> mileage, List<String> instance) {
	List<Edge> adjacent = formAjacencyList(mileage, instance);
	System.out.println(adjacent);
	Collections.sort(adjacent);
	System.out.println(adjacent);

	List<Set<Edge>> visitededge;
	List<Set<Integer>> visitednode;
	List<Edge> unvisited = new LinkedList<Edge>(adjacent);
	State state = new State(unvisited);
	System.out.println(state);
	Stack<State> stack = new Stack<State>();
	stack.push(state);

	State best = null;

	while(!stack.empty()) {
	    state = stack.pop();
	    //System.out.println(state);
	    visitededge = state.visitededge;
	    visitednode = state.visitednode;
	    unvisited = state.unvisited;
	    //System.out.println("="+state.visitededges()+"="+instance.size()+"=");
	    if(state.visitededges() < instance.size()-1) {
		if(unvisited.isEmpty()) {
		    continue;
		}

		State temp = new State(state);
		temp.unvisited.remove(0);
		stack.push(temp);

		int node1 = -1;
		int node2 = -1;
		temp = new State(state);
		Edge e = temp.unvisited.remove(0);
		for(int i=0; i<temp.visitededge.size(); i++) {
		    Set<Edge> se = temp.visitededge.get(i);
		    Set<Integer> sn = temp.visitednode.get(i);
		    if(sn.contains(e.node1)) {
			node1 = i;
		    }
		    if(sn.contains(e.node2)) {
			node2 = i;
		    }
		}
		if(node1==-1 && node2==-1) {
		    Set<Edge> te = new HashSet<Edge>();
		    te.add(e);
		    temp.visitededge.add(te);
		    Set<Integer> tn = new HashSet<Integer>();
		    tn.add(e.node1);
		    tn.add(e.node2);
		    temp.visitednode.add(tn);
		    temp.mileage += e.mileage;
		    stack.push(temp);
		} else if(node1 == -1) {
		    Set<Edge> te = temp.visitededge.get(node2);
		    te.add(e);
		    Set<Integer> tn = temp.visitednode.get(node2);
		    tn.add(e.node1);
		    temp.mileage += e.mileage;
		    stack.push(temp);
		} else if(node2 == -1) {
		    Set<Edge> te = temp.visitededge.get(node1);
		    te.add(e);
		    Set<Integer> tn = temp.visitednode.get(node1);
		    tn.add(e.node2);
		    temp.mileage += e.mileage;
		    stack.push(temp);
		} else if(node1 != node2) {
		    Set<Edge> te1 = temp.visitededge.get(node1);
		    Set<Edge> te2 = temp.visitededge.get(node2);
		    te1.addAll(te2);
		    temp.visitededge.remove(te2);
		    te1.add(e);
		    Set<Integer> tn1 = temp.visitednode.get(node1);
		    Set<Integer> tn2 = temp.visitednode.get(node2);
		    tn1.addAll(tn2);
		    temp.visitednode.remove(tn2);
		    temp.mileage += e.mileage;
		    stack.push(temp);
		}
		System.out.println(state);
	    } else if(state.visitededges() == instance.size()-1) {
		Set<Integer> set1 = new HashSet<Integer>();
		Set<Integer> set2 = new HashSet<Integer>();
		for(Edge e : state.visitededge.get(0)) {
		    if(!set1.contains(e.node1)) {
			set1.add(e.node1);
		    } else {
			set2.add(e.node1);
		    }
		    if(!set1.contains(e.node2)) {
			set1.add(e.node2);
		    } else {
			set2.add(e.node2);
		    }
		}
		while(!state.unvisited.isEmpty()) {
		    Edge e = state.unvisited.get(0);
		    if(!(set1.contains(e.node1) && set2.contains(e.node1)) && !(set1.contains(e.node2) && set2.contains(e.node2))) {
			state.visitededge.get(0).add(e);
			state.mileage += e.mileage;
			break;
		    }
		    state.unvisited.remove(0);
		}
		if(!state.unvisited.isEmpty()) {
		    state.unvisited.clear();
		    stack.push(state);
		} else {
		    //throw new RuntimeException();
		}
	    } else {
		System.out.println("=>"+state);
		if(state.mileage < 1180) {
		    throw new RuntimeException();
		}
		if(best == null) {
		    best = state;
		} else if(state.mileage < best.mileage) {
		    best = state;
		}
	    }
	}

	return null;
    }
}