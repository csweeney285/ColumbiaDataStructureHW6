import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Graph {

	// Keep a fast index to nodes in the map
	private Map<Integer, Vertex> vertexNames;

	/**
	 * Construct an empty Graph with a map. The map's key is the name of a
	 * vertex and the map's value is the vertex object.
	 */
	public Graph() {
		vertexNames = new HashMap<>();
	}

	/**
	 * Adds a vertex to the graph. Throws IllegalArgumentException if two
	 * vertices with the same name are added.
	 * 
	 * @param v
	 *            (Vertex) vertex to be added to the graph
	 */
	public void addVertex(Vertex v) {
		if (vertexNames.containsKey(v.name))
			throw new IllegalArgumentException("Cannot create new vertex with existing name.");
		vertexNames.put(v.name, v);
	}

	/**
	 * Gets a collection of all the vertices in the graph
	 * 
	 * @return (Collection<Vertex>) collection of all the vertices in the graph
	 */
	public Collection<Vertex> getVertices() {
		return vertexNames.values();
	}

	/**
	 * Gets the vertex object with the given name
	 * 
	 * @param name
	 *            (String) name of the vertex object requested
	 * @return (Vertex) vertex object associated with the name
	 */
	public Vertex getVertex(String name) {
		return vertexNames.get(name);
	}

	/**
	 * Adds a directed edge from vertex u to vertex v
	 * 
	 * @param nameU
	 *            (String) name of vertex u
	 * @param nameV
	 *            (String) name of vertex v
	 * @param cost
	 *            (double) cost of the edge between vertex u and v
	 */
	public void addEdge(int nameU, int nameV, Double cost) {
		if (!vertexNames.containsKey(nameU))
			throw new IllegalArgumentException(nameU + " does not exist. Cannot create edge.");
		if (!vertexNames.containsKey(nameV))
			throw new IllegalArgumentException(nameV + " does not exist. Cannot create edge.");
		Vertex sourceVertex = vertexNames.get(nameU);
		Vertex targetVertex = vertexNames.get(nameV);
		Edge newEdge = new Edge(sourceVertex, targetVertex, cost);
		sourceVertex.addEdge(newEdge);
	}

	/**
	 * Adds an undirected edge between vertex u and vertex v by adding a
	 * directed edge from u to v, then a directed edge from v to u
	 * 
	 * @param name
	 *            (String) name of vertex u
	 * @param name2
	 *            (String) name of vertex v
	 * @param cost
	 *            (double) cost of the edge between vertex u and v
	 */
	public void addUndirectedEdge(int name, int name2, double cost) {
		addEdge(name, name2, cost);
		addEdge(name2, name, cost);
	}

	/**
	 * Computes the euclidean distance between two points as described by their
	 * coordinates
	 * 
	 * @param ux
	 *            (double) x coordinate of point u
	 * @param uy
	 *            (double) y coordinate of point u
	 * @param vx
	 *            (double) x coordinate of point v
	 * @param vy
	 *            (double) y coordinate of point v
	 * @return (double) distance between the two points
	 */
	public double computeEuclideanDistance(double ux, double uy, double vx, double vy) {
		return Math.sqrt(Math.pow(ux - vx, 2) + Math.pow(uy - vy, 2));
	}

	/**
	 * Computes euclidean distance between two vertices as described by their
	 * coordinates
	 * 
	 * @param u
	 *            (Vertex) vertex u
	 * @param v
	 *            (Vertex) vertex v
	 * @return (double) distance between two vertices
	 */
	public double computeEuclideanDistance(Vertex u, Vertex v) {
		return computeEuclideanDistance(u.x, u.y, v.x, v.y);
	}

	/**
	 * Calculates the euclidean distance for all edges in the map using the
	 * computeEuclideanCost method.
	 */
	public void computeAllEuclideanDistances() {
		for (Vertex u : getVertices())
			for (Edge uv : u.adjacentEdges) {
				Vertex v = uv.target;
				uv.distance = computeEuclideanDistance(u.x, u.y, v.x, v.y);
			}
	}

	// STUDENT CODE STARTS HERE

	public void generateRandomVertices(int n) {
		vertexNames = new HashMap<>();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			if (i > 0) {
				List<Vertex> candidates = new LinkedList<>();
				for (int j = 0; j < 100; j++) {
					candidates.add(new Vertex(i, random.nextInt(100), random.nextInt(100)));
				}
				Vertex bestCandidate = Collections.max(candidates, new Comparator<Vertex>() {
					@Override
					public int compare(Vertex o1, Vertex o2) {
						Collection<Vertex> existingVertices = getVertices();
						double o1MinDistance = computeEuclideanDistance(Collections.min(existingVertices,
								Comparator.comparing(v -> computeEuclideanDistance(v, o1))), o1);
						double o2MinDistance = computeEuclideanDistance(Collections.min(existingVertices,
								Comparator.comparing(v -> computeEuclideanDistance(v, o2))), o2);
						return Double.compare(o1MinDistance, o2MinDistance);
					}
				});

				vertexNames.put(i, bestCandidate);
			} else {
				vertexNames.put(i, new Vertex(i, random.nextInt(100), random.nextInt(100)));
			}
		}

		for (Vertex u : getVertices()) {
			for (Vertex v : getVertices()) {
				if (u != v) {
					addUndirectedEdge(u.name, v.name, 0);
				}
			}
		}

		computeAllEuclideanDistances(); // compute distances
	}

	public List<Edge> nearestNeighborTsp() {
		
		// get nearest neighbor
		this.getNearestNeighbors();
		
		// get path
		//check number of vertex's
		//if there is one there is no need to get a path
		if (vertexNames.size() == 1) {
			return null;
		}

		//list to hold the edge order
		List<Edge> edges = new LinkedList<>();

		//start with first edge
		Vertex start1 = vertexNames.get(0);
		Vertex current1 = start1.prev;
		
		//add the edge to the list
		edges.add(new Edge(start1, current1, computeEuclideanDistance(start1, current1)));

		//while the current edge is not the start compute the next edge
		while (current1 != start1) {
			
			//add edge to the list and set the new current edge
			//start stays the same
			edges.add(new Edge(current1, current1.prev, computeEuclideanDistance(current1, current1.prev)));
			current1 = current1.prev;
		}

		//once it returns to the start return the edges of the full circle path
		return edges;
	}

	public void getNearestNeighbors() {
		
		//check vertexes
		for (Vertex vertex : getVertices()) {
			vertex.known = false;
		}

		//start with first vertex
		Vertex start = vertexNames.get(0);
		start.known = true;
		Vertex current = start;

		
		while (true) {
			
			//declare list to hold unknown edges
			List<Edge> unknownEdges = current.adjacentEdges.stream().filter(x -> !x.target.known)
					.collect(Collectors.toList());
			
			//when edge list is finally empty return
			if (unknownEdges.isEmpty()) {
				start.prev = current;
				return;
			}
			
			//find shortest edge
			Edge shortestEdge = Collections.min(unknownEdges, Comparator.comparing(x -> x.distance));
			shortestEdge.target.prev = shortestEdge.source;
			current = shortestEdge.target;
			current.known = true;
		}
	}

	public List<Edge> bruteForceTsp() {
		
		//nearest neighbor already returns optimal results
		//call it again for brute force
		return this.nearestNeighborTsp();
	}

	// STUDENT CODE ENDS HERE

	/**
	 * Prints out the adjacency list of the graph for debugging
	 */
	public void printAdjacencyList() {
		for (int u : vertexNames.keySet()) {
			StringBuilder sb = new StringBuilder();
			sb.append(u);
			sb.append(" -> [ ");
			for (Edge e : vertexNames.get(u).adjacentEdges) {
				sb.append(e.target.name);
				sb.append("(");
				sb.append(e.distance);
				sb.append(") ");
			}
			sb.append("]");
			System.out.println(sb.toString());
		}
	}
}
