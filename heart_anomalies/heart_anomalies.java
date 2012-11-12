/*
 * 
*/

import java.io.*;
import java.util.*;

class heart_anomalies {
    private int[][] F = null;
    private int[] N = null;

    private int total = 0;
    private int total_correct = 0;
    private int abnormal = 0;
    private int abnormal_correct = 0;
    private int normal = 0;
    private int normal_correct = 0;

    public heart_anomalies() {
    }
    private void usage() {
	System.out.println("heart_anomalies, version 1.0");
	System.out.println("Usage: heart_anomalies trainfile testfile");
    }
    private int[][] readFile(String infile) {
	String sCurrentLine;
	List<String> sLines = new LinkedList<String>();
	try {
	    BufferedReader inBufReader = new BufferedReader(new FileReader(infile));
	    while((sCurrentLine = inBufReader.readLine()) != null) {
		sLines.add(sCurrentLine);
	    }
	} catch(FileNotFoundException e) {
	    e.printStackTrace(System.out);
	} catch(IOException e) {
	    e.printStackTrace(System.out);
	}

	int rows = sLines.size();
	int colums = sLines.get(0).split(",").length;
	int[][] indata = new int[rows][colums];
	for(int i=0; i<rows; i++) {
	    String[] sRow = sLines.get(i).split(",");
	    for(int j=0; j<colums; j++) {
		indata[i][j] = Integer.parseInt(sRow[j]);
	    }
	}
	return indata;
    }
    private void train(String trainfile) {
	//System.out.println("training...");
	int[][] indata = readFile(trainfile);
	//printData(indata);
	F = new int[2][indata[0].length-1];
	N = new int[2];
	for(int i=0; i<indata.length; i++) {
	    // for each instance
	    N[indata[i][0]]++;
	    for(int j=0; j<indata[0].length-1; j++) {
		if(indata[i][j+1] == 1) {
		    F[indata[i][0]][j]++;
		}
	    }
	}
    }
    private void printData(int[][] indata) {
	System.out.println("data:");
	for(int i=0; i<indata.length; i++) {
	    for(int j=0; j<indata[0].length; j++) {
		System.out.print(indata[i][j]+" ");
	    }
	    System.out.println();
	}
    }
    private void printTrained() {
	System.out.println("N: " + N[0] + ", " + N[1]);
	System.out.println("F:");
	for(int i=0; i<N.length; i++) {
	    for(int j=0; j<F[0].length; j++) {
		System.out.print(F[i][j] + " ");
	    }
	    System.out.println();
	}
    }
    private void test(String testfile) {
	//System.out.println("testing...");
	int[][] indata = readFile(testfile);
	//printData(indata);
	double[][] L = new double[indata.length][2];
	for(int i=0; i<L.length; i++) {
	    for(int j=0; j<L[0].length; j++) {
		L[i][j] = Math.log(N[j]+0.5) - Math.log(N[0]+N[1]+0.5);
		for(int k=0; k<indata[0].length-1; k++) {
		    int s = F[j][k];
		    if(indata[i][k+1] == 0) {
			s = N[j] - s;
		    }
		    L[i][j] = L[i][j] + Math.log(s+0.5) - Math.log(N[j]+0.5);
		}
	    }
	}

	// classify
	int[] predict = new int[indata.length];
	for(int i=0; i<predict.length; i++) {
	    if(L[i][1] > L[i][0]) {
		predict[i] = 1;
	    } else {
		predict[i] = 0;
	    }
	}

	// calculate result
	total = indata.length;
	for(int i=0; i<total; i++){
	    if(indata[i][0]==0) {
		if(predict[i]==0) {
		    total_correct++;
		    abnormal_correct++;
		}
		abnormal++;
	    } else {
		if(predict[i]==1) {
		    total_correct++;
		    normal_correct++;
		}
		normal++;
	    }
	}
    }
    private void printResult(String mark) {
	System.out.format("%s %d/%d(%1.2f) %d/%d(%1.2f) %d/%d(%1.2f)\n", mark, total_correct, total, 1.0*total_correct/total, 
			  abnormal_correct, abnormal, 1.0*abnormal_correct/abnormal,
			  normal_correct, normal, 1.0*normal_correct/normal);
    }
    public static void main(String[] args) {
	heart_anomalies ha = new heart_anomalies();
	if(args.length != 2) {
	    ha.usage();
	    return;
	}
	//System.out.print("training file: " + args[0]);
	//System.out.println("testing file: " + args[1]);
	ha.train(args[0]);
	//ha.printTrained();
	ha.test(args[1]);

	String mark;
	if(args[0].contains("orig")) {
	    mark = new String("orig");
	} else if(args[0].contains("itg")) {
	    mark = new String("itg");
	} else {
	    mark = new String("resplit");
	}
	ha.printResult(mark);
    }
}
