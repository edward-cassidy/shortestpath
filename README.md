# shortestpath
Program takes a list of nodes and edge weights and determines the shortest path given by a source and destination node under a given edge cost limit

Input takes the form of two lines.

The first line for example:

[A,B,3] [B,C,5] [C,D,2]

Represents a graph of nodes A,B,C,D with edges between A and B of weight 3, B and C of weight 5 and C and D of weight 2.

The second line for example:

A->D,15 

Represents the source and destintaion node for the program to calculate the shortest path of. It also provides a maximum total weight cost allowed of 15. Anything over and an error is submitted. So for the graph above its total weight would be 10 for the shortest path.

The output would then be:

A->B->C->D
