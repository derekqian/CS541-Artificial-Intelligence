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
#include <sys/time.h>
#include <signal.h>

#define DEBUG 1
#ifdef DEBUG
#define DEBUGMSG(cond, msg) if(cond)printf msg;
#else
#define DEBUGMSG(cond, msg) 
#endif

// definition of structures
/*
 *      0   2   4      n-2
 *   +-------------   ----+
 *   |             ...    | 
 *   +-------------   ----+ 
 *      1   3   5      n-1
 */
struct state {
  int total;
  int assigned;
  int rotate; // rotate left
  int score;
  int assignment[0]; 
};
enum bool {
  FALSE = 0,
  TRUE
};

// global variables
enum bool stopworking = FALSE;
int people_num;
int* preference = NULL;
struct state* goal = NULL;
int update_num = 0;

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

int neighbor_score(struct state* ps, int col) {
  int s = 0;
  int p0 = (col*2)%people_num;
  int p1 = (col*2+1)%people_num;
  int p2 = (col*2+2)%people_num;
  int p3 = (col*2+3)%people_num;
  s += h(ps->assignment[p0], ps->assignment[p2]) + h(ps->assignment[p2], ps->assignment[p0]);
  s += (g(ps->assignment[p0]) == g(ps->assignment[p2])) ? 0 : 1;
  s += h(ps->assignment[p1], ps->assignment[p3]) + h(ps->assignment[p3], ps->assignment[p1]);
  s += (g(ps->assignment[p1]) == g(ps->assignment[p3])) ? 0 : 1;
  return s;
}

int max_col(struct state* ps) {
  int i;
  assert(ps->assigned == ps->total);
  int col = ps->total/2-1;
  int score = neighbor_score(ps, ps->total/2-1);
  for(i=0; i<(ps->total/2-1); i++) {
    if(neighbor_score(ps, i) > score) {
      col = i;
      score = neighbor_score(ps, i);
    }
  }
  return col;
}

int score(struct state* ps) {
  int i;
  int s = 0;

  // opposite
  for(i=0; i<(ps->assigned/2); i++) {
    s += h(ps->assignment[i*2], ps->assignment[i*2+1]) + h(ps->assignment[i*2+1], ps->assignment[i*2]);
    s += (g(ps->assignment[i*2]) == g(ps->assignment[i*2+1])) ? 0 : 2;
  }

  // next to each other
  for(i=(0+ps->rotate); i<(ps->assigned/2-1); i++) {
    int p0 = (i*2)%ps->total;
    int p1 = (i*2+1)%ps->total;
    int p2 = (i*2+2)%ps->total;
    int p3 = (i*2+3)%ps->total;
    s += h(ps->assignment[p0], ps->assignment[p2]) + h(ps->assignment[p2], ps->assignment[p0]);
    s += (g(ps->assignment[p0]) == g(ps->assignment[p2])) ? 0 : 1;
    s += h(ps->assignment[p1], ps->assignment[p3]) + h(ps->assignment[p3], ps->assignment[p1]);
    s += (g(ps->assignment[p1]) == g(ps->assignment[p3])) ? 0 : 1;
  }

  if(ps->assigned>2 && (ps->assigned%2)==1) {
    assert(ps->rotate == 0);
    int p0 = ps->assigned-3;
    int p2 = ps->assigned-1;
    s += h(ps->assignment[p0], ps->assignment[p2]) + h(ps->assignment[p2], ps->assignment[p0]);
    s += (g(ps->assignment[p0]) == g(ps->assignment[p2])) ? 0 : 1;
  }

  return s;
}

void dump_state(struct state* ps) {
  int i;

  printf("dump_state:\n");
  printf("\ttotal = %d\n", ps->total);
  printf("\tassigned = %d\n", ps->assigned);
  printf("\trotate = %d\n", ps->rotate);
  printf("\tscore = %d\n", ps->score);
  printf("\t");
  for(i=0; i<ps->total; i++) {
    printf("%02d ", ps->assignment[i]);
  }
  printf("\n");
}

void timer_handler(int param) {
  printf("timer_handler\n");
  stopworking = TRUE;
  return;
}

int main(int argc, char** argv) {
  int i, j;
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

  // begin the real work
  struct itimerval timersetting;
  timersetting.it_value.tv_sec = 5;
  timersetting.it_value.tv_usec = 0;
  timersetting.it_interval.tv_sec = 0;
  timersetting.it_interval.tv_usec = 0;
  setitimer(ITIMER_VIRTUAL, &timersetting, NULL);
  signal(SIGVTALRM, timer_handler);

  // debug purpose only
  struct state* laststate = malloc(sizeof(struct state) + people_num*sizeof(int));

  assert(empty());
  struct state* pstate = malloc(sizeof(struct state) + people_num*sizeof(int));
  if(pstate == NULL) {
    printf("main: malloc for pstate failed\n");
    goto quit_point;
  }
  pstate->total = people_num;
  pstate->assigned = 0;
  pstate->rotate = 0;
  pstate->score = 0;
  for(i=0; i<people_num; i++) {
    pstate->assignment[i] = i;
  }
  push(pstate);

  while(!empty() && !stopworking) {
    pstate = (struct state*)pop();
    int* ind = malloc(pstate->total*sizeof(int));
    if(ind == NULL) {
      printf("main: malloc for ind failed\n");
      goto quit_point;
    }
    for(i=0; i<pstate->total; i++) {
      ind[i] = i;
    }
#if 1
    if(pstate->assigned == 0) {
      int tempscore = h(0,1)+h(1,0);
      int ii = 0, jj = 1;
      for(i=1; i<people_num; i++) {
	for(j=0; j<i; j++) {
	  int temp = (g(i)==g(j)) ? 0 : 2;
	  temp += h(i,j) + h(j,i);
	  if(temp > tempscore) {
	    ii = i;
	    jj = j;
	    tempscore = temp;
	  }
	}
      }
      if(ii != 1) {
	swap(ind, 1, jj);
      }
    }
    int* partialscore = malloc(pstate->total*sizeof(int));
    if(partialscore == NULL) {
      printf("main: malloc for partialscore failed\n");
      goto quit_point;
    }
    pstate->assigned++;
    for(i=pstate->assigned; i<pstate->total; i++) {
      int partialscore = score(pstate);
      for()
    }
    pstate->assigned--;
#endif
    for(i=pstate->total-1; i>=pstate->assigned; i--) {
      struct state* tmpstate = malloc(sizeof(struct state) + people_num*sizeof(int));
      if(tmpstate == NULL) {
	printf("main: malloc for tmpstate failed\n");
	goto quit_point;
      }
      memcpy(tmpstate, pstate, sizeof(struct state) + people_num*sizeof(int));
#if 1
      swap(tmpstate->assignment, ind[i], tmpstate->assigned);
#else
      int tmpval = tmpstate->assignment[i];
      for(j=i; j>tmpstate->assigned; j--) {
	tmpstate->assignment[j] = tmpstate->assignment[j-1];
      }
      tmpstate->assignment[tmpstate->assigned] = tmpval;
#endif
      tmpstate->assigned++;
      if(tmpstate->assigned == tmpstate->total) {
	tmpstate->rotate = max_col(tmpstate);
	tmpstate->score = score(tmpstate);
	//dump_state(tmpstate);
	memcpy(laststate, tmpstate, sizeof(struct state) + people_num*sizeof(int));
	if(goal == NULL) {
	  goal = tmpstate;
	} else if(tmpstate->score > goal->score) {
	  free(goal);
	  goal = tmpstate;
	  update_num++;
	} else {
	  free(tmpstate);
	}
      } else {
	push(tmpstate);
      }
    }
    free(partialscore);
    free(ind);
    free(pstate);
  }
  dump_state(goal);
  printf("updated %d times\n", update_num);

  // debug purpose only
  dump_state(laststate);
  free(laststate);

quit_point:
  if(goal != NULL) {
    free(goal);
  }
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
