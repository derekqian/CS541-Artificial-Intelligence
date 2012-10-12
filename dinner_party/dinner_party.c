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
 */

#include <stdlib.h>
#include <stdio.h>

#define DEBUG 1
#ifdef DEBUG
#define DEBUGMSG(cond, msg) if(cond)printf msg;
#else
#define DEBUGMSG(cond, msg) 
#endif

void copyright() {
  printf("dinner_party version 1.00.0\n");
  printf("Copyright 2012 - 2012 Derek Qian - http://web.cecs.pdx.edu/~dejun\n");
}

void usage() {
  printf("Usage: dinner_party [input output]\n");
  printf("  if input and output are not provided, then the program will receive input from standard input and output to standard output.\n");
}

int main(int argc, char** argv) {
  int i;
  FILE* infile = NULL;
  FILE* outfile = NULL;

  if(argc == 1) {
    infile = stdin;
    outfile = stdout;
  } else if(argc == 3) {
    infile = fopen(argv[1], "r+");
    if(infile == NULL) {
      printf("main: open input file (%s) failed\n", argv[1]);
      goto quit_point;
    }
    outfile = fopen(argv[2], "w+");
    if(outfile == NULL) {
      printf("main: open output file (%s) failed\n", argv[2]);
      goto quit_point;
    }
  } else {
    copyright();
    usage();
    goto quit_point;
  }

  int people_num;
  int* preference = NULL;
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
    DEBUGMSG(0, ("%i - %d\n", i, preference[i]));
  }

quit_point:
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
