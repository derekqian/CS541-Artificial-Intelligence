/*
 */

import java.io.*;
import java.util.*;

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

	List<Integer> result;

	// _tsp_node
	System.out.println("==============================");
	tsp_node _tsp_node = new tsp_node();
	result = _tsp_node.getTravel(mileage, instance);
	System.out.println(result);

	// _tsp_edge
	System.out.println("==============================");
	tsp_edge _tsp_edge = new tsp_edge();
	result = _tsp_edge.getTravel(mileage, instance);
	System.out.println(result);
    }
}