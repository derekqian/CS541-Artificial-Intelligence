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
	    visitednode = new LinkedList<Set<Integer>>(state.visitednode);
	    unvisited = new LinkedList<Edge>(state.unvisited);
	    mileage = state.mileage;
	}
	public State(List<Edge> unvisited) {
	    this.visitededge = new LinkedList<Set<Edge>>();
	    this.visitednode = new LinkedList<Set<Integer>>();
	    this.unvisited = new LinkedList<Edge>(unvisited);
	    this.mileage = 0;
	}
	public int edges() {
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
	    visitededge = state.visitededge;
	    visitednode = state.visitednode;
	    unvisited = state.unvisited;
	    System.out.println("="+state.edges()+"="+instance.size()+"=");
	    if(state.edges() < instance.size()) {
		State temp = new State(state);
		temp.unvisited.remove(0);
		stack.push(temp);

		Set<Edge> senode1 = null;
		Set<Edge> senode2 = null;
		temp = new State(state);
		Edge e = temp.unvisited.get(0);
		temp.unvisited.remove(0);
		for(int i=0; i<temp.visitededge.size(); i++) {
		    Set<Edge> se = temp.visitededge.get(i);
		    Set<Integer> sn = temp.visitednode.get(i);
		    if(sn.contains(e.node1) && sn.contains(e.node2)) {
			senode1 = ;
			break;
		    } else if(sn.contains(e.node1)) {
			se.add(e);
			sn.add(e.node2);
			valid = true;
		    } else if(sn.contains(e.node2)) {
			se.add(e);
			sn.add(e.node1);
			valid = true;
		    } else {

		    }
		}
		stack.push(temp);
	    } else {
		if(best == null) {
		} else if(best == null) {
		}
		System.out.println(state);
	    }
	}

	return null;
    }
}