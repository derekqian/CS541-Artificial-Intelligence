/*
 */

import java.io.*;
import java.util.*;

class State implements Comparable<State> {
    public List<Integer> visited;
    public List<Integer> unvisited;
    public int mileage;
    public State(State state) {
	visited = new LinkedList<Integer>(state.visited);
	unvisited = new LinkedList<Integer>(state.unvisited);
	mileage = state.mileage;
    }
    public State(List<Integer> visited, List<Integer> unvisited, int mileage) {
	this.visited = new LinkedList<Integer>(visited);
	this.unvisited = new LinkedList<Integer>(unvisited);
	this.mileage = mileage;
    }
    @Override
    public String toString() {
	return "<"+visited+unvisited+mileage+">";
    }
    @Override
    public int compareTo(State compareState) {
	if(mileage == compareState.mileage) {
	    return 0;
	} else if(mileage < compareState.mileage) {
	    return -1;
	} else {
	    return 1;
	}
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
    private int[][] formAjacentMatrix(Map<String, Integer> mileage, List<String> instance) {
	int[][] matrix = new int[instance.size()][instance.size()];
	for(int i=0; i<instance.size(); i++) {
	    for(int j=0; j<instance.size(); j++) {
		String key = new String(instance.get(i)+" "+instance.get(j));
		matrix[i][j] = mileage.get(key)==null?-1:mileage.get(key).intValue();
	    }
	}
	return matrix;
    }
    private boolean checkAjacentMatrix(int[][] matrix) {
	for(int i=0; i<matrix.length; i++) {
	    if(matrix[i][i] != -1) {
		System.out.println("AjacentMatrix invalid");
		return false;
	    }
	    for(int j=0; j<i; j++) {
		if(matrix[i][j]==-1 || matrix[j][i]==-1 || matrix[i][j]!=matrix[j][i]) {
		    System.out.println("AjacentMatrix invalid");
		    return false;
		}
	    }
	}
	return true;
    }
    private void printAjacentMatrix(int[][] matrix) {
	for(int i=0; i<matrix.length; i++) {
	    for(int j=0; j<matrix[0].length; j++) {
		System.out.print(matrix[i][j]+" ");
	    }
	    System.out.println();
	}
    }
    private State getTravel(int[][] adjacent) {
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

	State best = null;

	while(!stack.empty()) {
	    state = stack.pop();
	    visited = state.visited;
	    unvisited = state.unvisited;
	    if(unvisited.size() > 1) {
		Integer temp1 = visited.get(visited.size()-1);
		List<State> sons = new LinkedList<State>();
		for(Integer temp2 : unvisited) {
		    State son = new State(state);
		    son.mileage += adjacent[temp1.intValue()][temp2.intValue()];
		    son.visited.add(temp2);
		    son.unvisited.remove(temp2);
		    sons.add(son);
		}
		// System.out.println(sons);
		Collections.sort(sons);
		// System.out.println(sons);
		for(int i=sons.size()-1; i>=0; i--) {
		    stack.push(sons.get(i));
		}
	    } else {
		Integer temp1 = visited.get(visited.size()-1);
		Integer temp2 = unvisited.get(0);
		visited.add(temp2);
		unvisited.clear();
		state.mileage += adjacent[temp1.intValue()][temp2.intValue()];
		state.mileage += adjacent[temp2.intValue()][visited.get(0).intValue()];
		if(best == null) {
		    best = state;
		} else if(state.mileage < best.mileage) {
		    best = state;
		}
		System.out.println(state);
	    }
	}

	return best;
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
	int[][] adjacent = _tsp.formAjacentMatrix(mileage, instance);
	if(!_tsp.checkAjacentMatrix(adjacent)) {
	    _tsp.printAjacentMatrix(adjacent);
	}
	_tsp.printAjacentMatrix(adjacent);

	State result = _tsp.getTravel(adjacent);
	System.out.println(result);
    }
}