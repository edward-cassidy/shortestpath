import java.util.*;

public class Shortestpathfinder {

    int timeofpath;//This global variable represents the total time/distance of the shortest path between the source and end node

    static class Graph {//Graph class stores all the vertices as a map where the key refers to name e.g. 'A'

        Map<String, Vertex> vertices = new HashMap<>();

    }

    static class Vertex implements Comparable<Vertex> {//Vertex class is used to represent each vertex in the graph

        boolean explored = false;//explored is used to determine if every vertex has been reach by dijkstras algorithm, if one vertex is false then the graph is disconnected
        Vertex come_from;//This is used to store the previous vertex in the shortest path from the source
        String name;
        int distance = Integer.MAX_VALUE;//This represents the distance from this vertex to the source accounting for edge costs. Initially its set to max.
        Map<Vertex, Integer> neighbours = new HashMap<>();//This hash map stores the neighbours of the vertex along with the edge costs

        public Vertex(String name){
            this.name = name;
        }

        @Override//These equals overrides are used so I can use vertices in hashmap
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return Objects.equals(name, vertex.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override//This comparable interface is implemented so the priorityqueue knows how to order vertices
        public int compareTo(Vertex o) {
            return Integer.compare(distance,o.distance);
        }
    }



    Stack<Vertex> dijkstra(Graph graph, Vertex start, Vertex end){//I found the shortest path by implementing dijkstra's algorithm
        start.distance = 0;
        start.explored = true;
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>();//I used a priorityqueue to store the next vertices to be explored
        priorityQueue.add(start);

        while(!priorityQueue.isEmpty()){
            Vertex v = priorityQueue.poll();//I pop out the next minimum cost vertex which priorityqueue does implicitly for me
            for (Vertex neighbour:v.neighbours.keySet()) {//This code effectivley goes through all of v's neighbours and updates/adds them if their own distance is greater than the distance of v's plus the edge cost
                int dist_neighbour = v.distance + v.neighbours.get(neighbour);
                if(dist_neighbour<neighbour.distance){
                    neighbour.distance = dist_neighbour;
                    neighbour.come_from = v;//The come_from is updated to v so we can keep track of the shortest path
                    if(!priorityQueue.contains(neighbour)){
                        priorityQueue.add(neighbour);
                        neighbour.explored = true;
                    }
                }
            }
        }

        if(end.come_from == null){//If no shortest path was found
            return null;
        }else{//Here we backtrack and build up the shortest path starting from the end vertex until we reach the start
            timeofpath = end.distance;
            Stack<Vertex> path = new Stack<>();
            path.push(end);

            while(!path.peek().equals(start)){
                path.push(path.peek().come_from);
            }
            return path;
        }


    }


    public static void main(String args[] ) {
        Vertex start_vertex;
        Vertex end_vertex;
        int time_limit;

        Scanner in = new Scanner(System.in);
        Graph graph = new Graph();
        try {

            String regex1 = "((\\[[A-Z],[A-Z],\\d+] )+)?(\\[[A-Z],[A-Z],\\d+])";//I use regular expressions here to match the strings so only correct arguments are passed
            String regex2 = "[A-Z]->[A-Z],\\d+";
            String firstarg = in.nextLine();
            String secondarg = in.nextLine();

            if(!firstarg.matches(regex1)||!secondarg.matches(regex2)){
                System.out.println("E1");
                return;
            }

            String[] edges = firstarg.split(" ");
            for (String edge : edges) {//This code splits up the firstline and builds up the graph for each edge input

                edge = edge.substring(edge.indexOf("[") + 1, edge.lastIndexOf("]"));
                String[] values = edge.split(",");

                Vertex vertex1 = graph.vertices.getOrDefault(values[0], new Vertex(values[0]));
                graph.vertices.put(values[0], vertex1);
                Vertex vertex2 = graph.vertices.getOrDefault(values[1], new Vertex(values[1]));
                graph.vertices.put(values[1], vertex2);


                if(vertex1.neighbours.containsKey(vertex2)){
                    System.out.println("E2");//This code checks if there are duplicate definitions of edges
                    return;
                }else{
                    vertex1.neighbours.put(vertex2,Integer.parseInt(values[2]));
                }

                if(vertex2.neighbours.containsKey(vertex1)){
                    System.out.println("E2");//This code checks if there are duplicate definitions of edges
                    return;
                }else{
                    vertex2.neighbours.put(vertex1,Integer.parseInt(values[2]));
                }
                //The code above is repeated twice as the graph is undirected and both edges need to be created

            }

            //This code checks if to see if the start_vertex or end_vertex are not present in th actual graph
            if(!graph.vertices.containsKey(String.valueOf(secondarg.charAt(0))) || !graph.vertices.containsKey(String.valueOf(secondarg.charAt(3)))){
                System.out.println("E2");
                return;
            }
            start_vertex = graph.vertices.get(String.valueOf(secondarg.charAt(0)));
            end_vertex = graph.vertices.get(String.valueOf(secondarg.charAt(3)));
            time_limit = Integer.parseInt(secondarg.substring(5));


            Shortestpathfinder solution = new Shortestpathfinder();//We now run dijkstra's algorithm
            Stack<Vertex> path = solution.dijkstra(graph,start_vertex,end_vertex);

            for (String ver:graph.vertices.keySet()) {//This code checks to see if the graph is disconnected
                if(graph.vertices.get(ver).explored == false){
                    System.out.println("E2");
                    return;
                }
            }

           if(path == null || solution.timeofpath > time_limit){//This code is when no shortest path is found or its over the timelimit
                System.out.println("E3");
                return;
            }

            //this outputs the shortest path if there are no other errors
            System.out.print(path.pop().name);
            while(!path.empty()){
                System.out.print("->"+path.pop().name);
            }


        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }
}