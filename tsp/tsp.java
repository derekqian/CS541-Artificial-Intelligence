
import java.io.*;
import java.util.*;

class tsp {
    public tsp() {
    }
    private void usage() {
	System.out.println("tsp, version 1.0");
	System.out.println("Usage: tsp mileagefile instancefile");
    }
    private String[][] readMileageFile(String mileagefile) {
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
        int colums = sLines.get(0).split(" ").length;
        String[][] mileagedata = new String[rows][colums];
        for(int i=0; i<rows; i++) {
            String[] sRow = sLines.get(i).split(" ");
            for(int j=0; j<colums; j++) {
                mileagedata[i][j] = sRow[j];
            }
        }
        return mileagedata;
    }
    private String[] readInstanceFile(String instancefile) {
        String sCurrentLine;
        List<String> sLines = new LinkedList<String>();
        try {
            BufferedReader inBufReader = new BufferedReader(new FileReader(instancefile));
            while((sCurrentLine = inBufReader.readLine()) != null) {
                sLines.add(sCurrentLine);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch(IOException e) {
            e.printStackTrace(System.out);
        }

        int size = sLines.size();
        String[] instancedata = new String[size];
        for(int i=0; i<size; i++) {
	    instancedata[i] = sLines.get(i);
        }
        return instancedata;
    }
    public static void main(String[] args) {
	tsp _tsp = new tsp();
	if(args.length != 2) {
	    _tsp.usage();
	    return;
	}
	String[][] mileage = _tsp.readMileageFile(args[0]);
	System.out.println(mileage);
	String[] instance = _tsp.readInstanceFile(args[1]);
	System.out.println(instance);
    }
}