/*
 * tsp version 1.0
 * Copyright 2012 - 2012 Dejun Qian - electronseu@gmail.com
 *                                  - http://web.cecs.pdx.edu/~dejun
 */

import java.io.*;
import java.util.*;

class tsp {
    public tsp() {
    }
    private void usage() {
	System.out.println("tsp, version 1.0");
	System.out.println("Usage: tsp [-n|-e] mileagefile instancefile");
	System.out.println("   -n use algorithm by selecting the node.");
	System.out.println("   -e use algorithm by selecting the edge. <Default>");
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
    public static void main(String[] args) {
	//System.out.println("tsp version 1.0");
	//System.out.println();

	boolean use_edge = true;
	String mileage_file = null;
	String instance_file = null;

	tsp _tsp = new tsp();
	if(args.length == 2) {
	    use_edge = true;
	    mileage_file = args[0];
	    instance_file = args[1];
	} else if(args.length == 3) {
	    if(args[0].equals("-n")) {
		use_edge = false;
	    } else if(args[0].equals("-e")) {
		use_edge = true;
	    } else {
		_tsp.usage();
	    }
	    mileage_file = args[1];
	    instance_file = args[2];
	} else {
	    _tsp.usage();
	    return;
	}
	Map<String, Integer> mileage = _tsp.readMileageFile(mileage_file);
	// System.out.println(mileage);
	List<String> instance = _tsp.readInstanceFile(instance_file);
	//System.out.println("Input instance: "+instance);

	List<Integer> result;

	if(!use_edge) {
	    // _tsp_node
	    //System.out.println("=== Result for the first algorithm ===");
	    tsp_node _tsp_node = new tsp_node();
	    result = _tsp_node.getTravel(mileage, instance);
	    System.out.println(result.get(0));
	    for(int i=1; i<result.size(); i++) {
		System.out.println(instance.get(result.get(i).intValue()));
	    }
	} else {
	    // _tsp_edge
	    //System.out.println("=== Result for the second algorithm ===");
	    tsp_edge _tsp_edge = new tsp_edge();
	    result = _tsp_edge.getTravel(mileage, instance);
	    System.out.println(result.get(0));
	    for(int i=1; i<result.size(); i++) {
		System.out.println(instance.get(result.get(i).intValue()));
	    }
	}
    }
}