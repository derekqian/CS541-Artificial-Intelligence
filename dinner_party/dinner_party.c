/*
 *  Suppose you are given a set of n people (with n even) to be seated at a dinner party. The people will be seated along the two sides of a long table.
 *
 *      o   o   o      o
 *   +-------------   ----+
 *   |             ...    | 
 *   +-------------   ----+ 
 *      o   o   o      o
 * Half are male, half are female. The given function g(p) identifies the gender of a given person.
 * As the host, you also know an integer-valued "preference function" h(p1, p2) for a pair of people p1, p2. The preference function indicates how much the first person likes the second; it may be negative.
 *
 * The "score" of a table is determined by the following criteria:
 *
 * 1 point for every adjacent pair (seated next to each other) of people with one female and the other male.
 * 2 points for every opposite pair (seated across from each other) of people with one female and the other male.
 * h(p1, p2) + h(p2, p1) points for every adjacent or opposite pair of people p1, p2.
 * Your job as host is to write a search that will find a "good" table score for a given set of people and preference function.
 *
 * The data is given to you in the form of an ASCII text file that has the even number n of people on the first line. The first n/2 people are assumed to be female, the rest male. The preference matrix follows on the remaining lines: rows with values separated by spaces. The people are assumed to be numbered 1..n. The seats are assumed to be numbered such that the top half of the table has seats 1..n/2 left-to-right, and the bottom half of the table has seats n/2+1..n left-to-right; thus seat n/2 is opposite seat n.
 *
 * The output should be a score, then a series of n rows, each with a person number, then a space, then a seat number.
 * --------------------------------------------------------------------------
 * Construct a program that solves instances of the problem read from an instance file or, if you prefer, stdin, and writes the solution to a solution file or, if you prefer, stdout. You may use any programming language you like. I strongly recommend the use of a build tool such as make, and a source-code management system such as Git or Mercurial.
 *
 * There are three instances I want you to run on: hw1-inst1.txt, hw1-inst2.txt, hw1-inst3.txt. Please produce output files hw1-soln1.txt, hw1-soln2.txt, hw1-soln3.txt containing the best solutions you found in just 60 seconds of runtime.
 *
 * Turn in a tar or zip archive of your source code including the SCMS repository (if possible), your solution files, and a brief writeup saying how your code works, how to build it, how it did, and any other info you think I might find interesting. Describe the hardware and software setup under which you did the experiments. Be sure to put a copyright notice on your source code, and your name and email on your writeup. Do not include object files, executable files, or other large binary files that are likely useless to me.
 *
 * Above all, have fun!
 */

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <string.h>

#define DEBUG 1
#ifdef DEBUG
#define DEBUGMSG(cond, msg) if(cond)printf msg;
#else
#define DEBUGMSG(cond, msg) 
#endif

// definition of structures
/*
 */
struct state {
  int total;
  int assigned;
  int assignment[0]; 
};

// global variables
int people_num;
int* preference = NULL;

void copyright() {
  printf("dinner_party version 1.00.0\n");
  printf("Copyright 2012 - 2012 Derek Qian - http://web.cecs.pdx.edu/~dejun\n");
}

void usage() {
  printf("Usage: dinner_party [input]\n");
  printf("  if input are not provided, then the program will receive input from standard input and output to standard output.\n");
}

void swap(int* buf, int ind1, int ind2) {
  assert(buf != NULL);
  if(ind1 == ind2) return;
  int temp = buf[ind1];
  buf[ind1] = buf[ind2];
  buf[ind2] = temp;
}

/*
 * return the gender of people p
 *     0 - female
 *     1 - male
 */
int g(int p) {
  return (p < people_num/2) ? 0 : 1;
}

/*
 * return the preference which indicates how p1 likes p2.
 */
int h(int p1, int p2) {
  return preference[p1*people_num+p2];
}

int score(struct state* ps) {
  return 0;
}

int main(int argc, char** argv) {
  int i;
  FILE* infile = NULL;
  FILE* outfile = NULL;

  if(argc == 1) {
    infile = stdin;
    outfile = stdout;
  } else if(argc == 2) {
    if(0!=strcmp(argv[1], "hw1-inst1.txt") && 0!=strcmp(argv[1], "hw1-inst2.txt") && 0!=strcmp(argv[1], "hw1-inst3.txt")) {
      printf("main: input file name not supported\n");
      goto quit_point;
    }
    infile = fopen(argv[1], "r+");
    if(infile == NULL) {
      printf("main: open input file (%s) failed\n", argv[1]);
      goto quit_point;
    }
    char outfilename[16];
    strcpy(outfilename, argv[1]);
    outfilename[4] = 's';
    outfilename[5] = 'o';
    outfilename[6] = 'l';
    outfilename[7] = 'n';
    outfile = fopen(outfilename, "w+");
    if(outfile == NULL) {
      printf("main: open output file (%s) failed\n", outfilename);
      goto quit_point;
    }
  } else {
    copyright();
    usage();
    goto quit_point;
  }

  if(1 != fscanf(infile, "%d", &people_num)) {
    printf("main: read the number of people failed\n");
    goto quit_point;
  }
  DEBUGMSG(1, ("people_num = %d\n", people_num));
  preference = malloc(people_num*people_num*sizeof(int));
  if(preference == NULL) {
    printf("main: allocate memory failed\n");
    goto quit_point;
  }
  for(i=0; i<people_num*people_num; i++) {
    if(1 != fscanf(infile, "%d", preference+i)) {
      printf("main: read preference data failed\n");
      goto quit_point;
    }
  }

  assert(empty());
  struct state* pstate = malloc(sizeof(struct state) + people_num*sizeof(int));
  if(pstate == NULL) {
    printf("main: malloc for pstate failed\n");
    goto quit_point;
  }
  pstate->total = people_num;
  pstate->assigned = 0;
  for(i=0; i<people_num; i++) {
    pstate->assignment[i] = i;
  }
  push(pstate);
  dump();

  while(!empty()) {
    pstate = (struct state*)pop();
    dump();
    for(i=pstate->total-1; i>=pstate->assigned; i--) {
      struct state* tmpstate = malloc(sizeof(struct state) + people_num*sizeof(int));
      if(tmpstate == NULL) {
	printf("main: malloc for tmpstate failed\n");
	goto quit_point;
      }
      memcpy(tmpstate, pstate, sizeof(struct state) + people_num*sizeof(int));
      swap(tmpstate->assignment, i, tmpstate->assigned);
      tmpstate->assigned++;
      if(tmpstate->assigned == tmpstate->total) {
	break;
      } else {
	push(tmpstate);
      }
    }
    dump();
    free(pstate);
  }

quit_point:
  while(!empty()) {
    pstate = (struct state*)pop();
    free(pstate);
  }
  if(preference != NULL) {
    free(preference);
  }
  if(outfile != NULL && outfile != stdout) {
    fclose(outfile);
  }
  if(infile != NULL && infile != stdin) {
    fclose(infile);
  }

  return 0;
}
