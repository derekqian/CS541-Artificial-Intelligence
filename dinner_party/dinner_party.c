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

int main(int argc, char** argv) {
  return 0;
}
