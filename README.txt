README.txt

Programming 1-

This program implements methods to solve the traveling salesman problem (tsp). To run this problem go to Display.java and run it. A GUI will appear. the user then has the ability to input the number of vertices on the graph which they would like to create.

Display.java uses Graph.java. In Graph.java which was provided by the Professor, I edited it so that the generateRandomVertices creates n vertices and places them in the vertexNames property. This is done by running a loop n times where a random value between 0-100 is created for each x and y coordinate. The vertices are named 0, 1, 2, 3,… n-1. And distances between vertices are also displayed.

Each time the Generate Random Graph is pressed the vertices are removed and recreated through this.

When get Nearest Neighbor is pressed a green path appears displaying the edges of a full cycle path estimate of the shortest path where one reaches each vertex and returns to the starting one. In the box it also displays the distance.

When Brute Force is pressed a red path appears displaying the shortest path of edges to reach each vertex. Likewise it returns the distance.

In my program the Brute Force and Nearest Neighbor return the same results.

Random Graph: 8,
Nearest Neighbor: Distance: 457.85, Time: 0 ms,
Brute Force: Distance: 457.85, Time: 0 ms,

Random Graph: 5,
Nearest Neighbor: Distance: 408.77, Time: 0 ms,
Brute Force: Distance: 408.77, Time: 0 ms,

Random Graph: 3,
Nearest Neighbor: Distance: 269.03, Time: 0 ms,
Brute Force: Distance: 269.03, Time: 0 ms,

My program keeps returning an average run time of 0 ms for both brute force and nearest neighbor no matter the number of vertices

I even tested it with a giant graph to test the time and it returned these results and the Brute Force returned faster
Random Graph: 200,
Nearest Neighbor: Distance: 1749.68, Time: 8 ms,
Brute Force: Distance: 1749.68, Time: 3 ms,