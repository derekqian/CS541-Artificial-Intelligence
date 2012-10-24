/*
 * The game of Connect-Four has been widely studied. A less-widely-studied variant is Connect-Three. We will be working on Connect-Three on a board 3 wide.
 *
 * It turns out that this game is a draw with best play by both sides for any height greater than 3. Your first job is to construct a program that proves that Connect-Three on a board 3 wide and 4 high is a draw with best play by both sides, by doing a minimax search. This should require less than a minute of runtime on a reasonable machine. For the CS 441 students, this is all you need to do.
 *
 * For the CS 541 students, and optionally for 441-ers, make a game out of your code on the 3 wide 10 high board. Provide for some way for humans to input moves and see the board state. This need not be fancy graphics: ASCII displays and move numbers are fine. Verify that the code will beat you in a game when allowed to play first, in spite of the drawish nature of this game :-) . I will discuss in class some programming tricks for this assignment.
 *
 * You may use any programming language you like. I strongly recommend the use of a build tool such as make, and a source-code management system such as Git or Mercurial.
 *
 * Turn in a tar or zip archive of your source code including the SCMS repository (if possible), your solution files, and a brief writeup saying how your code works, how to build it, how it did, and any other info you think the TA and I might find interesting. Describe the hardware and software setup under which you did the experiments. Be sure to put a copyright notice on your source code, and your name and email on your writeup. Do not include object files, executable files, or other large binary files that are likely useless to us.
 *
 * Above all, have fun!
 */

import java.io.*;
import java.util.*;

class State {
    public static final int WIDTH = 3;
    public static final char BLANK = ' ';
    public static final char MAX = 'O';
    public static final char MIN = 'X';
    public int HEIGHT;
    private char[][] board;
    private char player;

    public State(int height, char firstplayer) {
	HEIGHT = height;
	player = firstplayer;
	board = new char[HEIGHT][WIDTH];
	if(board != null) {
	    for(int i=0; i<HEIGHT; i++) {
		for(int j=0; j<WIDTH; j++) {
		    board[i][j] = BLANK;
		}
	    }
	}
    }
    public void display() {
	System.out.println("+-----------+");
	for(int i=0; i<HEIGHT; i++) {
	    System.out.print('|');
	    for(int j=0; j<WIDTH; j++) {
		System.out.format(" %s |", board[i][j]);
	    }
	    System.out.println();
	    System.out.println("+-----------+");
	}
    }
    public char Player() {
	return player;
    }
    public ArrayList<Integer> Actions() {
	ArrayList<Integer> list = new ArrayList<Integer>();
	for(int i=0; i<WIDTH; i++) {
	    if(board[HEIGHT-1][i] == BLANK) {
		list.add(i);
	    }
	}
    }
    public void result(int action) {

    }
}

class connect_three {
    private State state;
    public connect_three() {
	state = new State();
    }
    public void print() {
	state.display();
    }
    public static void main(String[] args) {
	connect_three ct = new connect_three();
	ct.print();
    }
}