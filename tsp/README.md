# Final Exam / Project
CS 441 / 541 Fall 2012  
Bart Massey

For this assignment, you will be tackling the well-known
"Travelling Salesperson Problem".

Your job will be to use an AI method to find a shortest tour
of various Oregon cities. As you know, the tour must visit
each city at least once, and end at the same city it starts
at.

The files you need to complete this assignment are in the
file `final.zip` on the Week 10 schedule page on the Moodle.

The data source for this problem is a highly-sanitized
version of a 2012
[table](http://www.oregon.gov/ODOT/TD/TDATA/otms/docs/mileagechart.pdf)
of highway mileages between Oregon cities provided by the
Oregon Department of Transportation. You can look at an
Oregon map to find the locations of these cities if you are
curious. (It will be legal to visit these cities more than
once on your tour, from a highway perspective, but you must
list each city only once---this is the visit that "counts".)

The file `oregon-mileage.txt` contains mileages between
pairs of cities in Oregon in an obvious format. The file is
redundant, in that the distance from city A to city B is
given, and the identical distance from city B to city A is
also given.  This is version 2 of this file; it has errors
corrected and entries added compared to the first version.

The files `small-instance.txt`, `medium-instance.txt` and
`large-instance.txt` contain lists of cities to tour. The
large instance is simply every listed Oregon city.

You should limit yourselves to 60 seconds of computation per
run, as usual. Find the best solution you can, but it need
not be optimal.

You have several choices of method as discussed in this
course:

  * Complete Search
  * Local Search
  * Genetic Algorithm
  * "Ant Trail" (see the course text)

If you want to try something else, please talk to me first.

Undergraduates (CS 441) may use the "Ant Trail" code from
the course text as part of their solution, but graduates may
not (although they may use the "Ant Trail" approach).

Here is an example instance:

        Bend
        Coos.Bay
        Ontario
        Portland
        Roseburg

and here is an optimal solution to that instance, obtained
by stupid brute force (compare all possible solutions):

        1123
        Bend
        Ontario
        Portland
        Coos.Bay
        Roseburg
        Bend

Please display your solutions in the format shown:

        <mileage>
        <first city>
        ...
        <last city>
        <first city>

Further, for the sake of being able to compare solutions,
please show your tour starting with the first city in
alphabetical order (Bend, in this instance), and continuing
with the neighbor of that city that is earlier in
alphabetical order (Ontario comes before Roseburg in this
instance).

----

As usual, turn in your code, your results, and a writeup. I
am especially interested in the writeup on this one: please
say how your approach worked, and what you think might be
improved.
