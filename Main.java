import java.util.*;
import java.lang.*;
import java.io.*;

public class Main{
    public static void main(String[] args) {
        //args[1] : name of data query file
        //args[2] : name of query graph file
        //args[3] : the number of query in query graph file

        //read data graph file
        //read the query grap file
        //find the dag for each query graph

    	int numQuery = Integer.parseInt(args[3]);
    	
    	readDataGraph(args[1]);

    	for(int i = 0; i < numQuery; i++) {
    		readQueryGraph(args[2]);
    		buildDAG();
    	}
    	
    }
    
    public static void readDataGraph(String a) {
    	
    }
    
    public static void readQueryGraph(String b) {
    	
    }
    
    public static void buildDAG() {
    	
    }
    
    
    
}
