/*
 */

import java.io.*;
import java.util.*;

class State {
    public List<Integer> visited;
    public List<Integer> unvisited;
    public Integer mileage;
    public State(State state) {
	visited = new LinkedList<Integer>(state.visited);
	unvisited = new LinkedList<Integer>(state.unvisited);
	mileage = new Integer(state.mileage);
    }
    public State(List<Integer> visited, List<Integer> unvisited, Integer mileage) {
	this.visited = new LinkedList<Integer>(visited);
	this.unvisited = new LinkedList<Integer>(unvisited);
	this.mileage = new Integer(mileage);
    }
    @Override
    public String toString() {
	return "<"+visited+unvisited+mileage+">";
    }
}

class tsp {
    public tsp() {
    }
    private void usage() {
	System.out.println("tsp, version 1.0");
	System.out.println("Usage: tsp mileagefile instancefile");
    }
    private Map<String, Integer> readMileageFile(String mileagefile) {
        String sCurrentLine;
        List<String> sLines = new LinkedList<String>();
        try {
            BufferedReader inBufReader = new BufferedReader(new FileReader(mileagefile));
            while((sCurrentLine = inBufReader.readLine()) != null) {
                sLines.add(sCurrentLine);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch(IOException e) {
            e.printStackTrace(System.out);
        }

        int rows = sLines.size();
        Map<String, Integer> mileagedata = new LinkedHashMap<String, Integer>();
        for(int i=0; i<rows; i++) {
            String[] sRow = sLines.get(i).split(" ");
	    mileagedata.put(sRow[0]+" "+sRow[1], Integer.valueOf(sRow[2]));
        }
        return mileagedata;
    }
    private List<String> readInstanceFile(String instancefile) {
        String sCurrentLine;
        List<String> instancedata = new LinkedList<String>();
        try {
            BufferedReader inBufReader = new BufferedReader(new FileReader(instancefile));
            while((sCurrentLine = inBufReader.readLine()) != null) {
                instancedata.add(sCurrentLine);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch(IOException e) {
            e.printStackTrace(System.out);
        }
        return instancedata;
    }
    private Integer[][] formAjacentMatrix(Map<String, Integer> mileage, List<String> instance) {
	Integer[][] matrix = new Integer[instance.size()][instance.size()];
	for(int i=0; i<instance.size(); i++) {
	    for(int j=0; j<instance.size(); j++) {
		String key = new String(instance.get(i)+" "+instance.get(j));
		matrix[i][j] = mileage.get(key);
	    }
	}
	return matrix;
    }
    private boolean checkAjacentMatrix(Integer[][] matrix) {
	for(int i=0; i<matrix.length; i++) {
	    if(matrix[i][i] != null) {
		System.out.println("AjacentMatrix invalid");
		return false;
	    }
	    for(int j=0; j<i; j++) {
		if(matrix[i][j]==null || matrix[j][i]==null || !matrix[i][j].equals(matrix[j][i])) {
		    System.out.println("AjacentMatrix invalid");
		    return false;
		}
	    }
	}
	return true;
    }
    private void printAjacentMatrix(Integer[][] matrix) {
	for(int i=0; i<matrix.length; i++) {
	    for(int j=0; j<matrix[0].length; j++) {
		System.out.print(matrix[i][j]+" ");
	    }
	    System.out.println();
	}
    }
    private List<Integer> getTravel(Integer[][] adjacent) {
	List<Integer> visited = new LinkedList<Integer>();
	visited.add(new Integer(0));
	List<Integer> unvisited = new LinkedList<Integer>();
	for(int i=1; i<adjacent.length; i++) {
	    unvisited.add(new Integer(i));
	}
	State state = new State(visited, unvisited, 0);
	System.out.println(state);
	Stack<State> stack = new Stack<State>();
	stack.push(state);

	while(!stack.empty()) {
	    state = stack.pop();
	    visited = state.visited;
	    unvisited = state.unvisited;
	    if(unvisited.size() > 1) {
	    } else {
	    }
	}

	return state.visited;
    }
    public static void main(String[] args) {
	tsp _tsp = new tsp();
	if(args.length != 2) {
	    _tsp.usage();
	    return;
	}
	Map<String, Integer> mileage = _tsp.readMileageFile(args[0]);
	// System.out.println(mileage);
	List<String> instance = _tsp.readInstanceFile(args[1]);
	System.out.println(instance);
	Integer[][] adjacent = _tsp.formAjacentMatrix(mileage, instance);
	if(!_tsp.checkAjacentMatrix(adjacent)) {
	    _tsp.printAjacentMatrix(adjacent);
	}

	List<Integer> result = _tsp.getTravel(adjacent);
	System.out.println(result);
    }
}