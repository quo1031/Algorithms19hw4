import java.util.*;
import java.lang.*;
import java.io.*;

public class Main{
    public static void main(String[] args)  throws IOException{
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
    
    //Variables for data graph
    static int numDataNode = 0;
    static int[] labelData; // holds label of vertex i
    static int[] degreeData; // holds degree of vertex i
    static int [][] adjListData; // holds edges of vertex i in data graph
    
    //Variables for query graph
    static int root = -1;
    static int numQueryNode = 0;
    static int[] labelQuery; // holds label of vertex i in query graph
    static int[] degreeQuery; // holds degree of vertex i in degree graph
    static int[][] adjListQuery; // holds edges of vertex i in query graph
    
    //Variables for query DAG
    static int[] dagChildQuery;
    static int[] dagParentQuery;
    static int[] dagChildQuerySize;
    static int[] dagParentQuerySize;
    
    
    public static void readDataGraph(String aFileName) throws IOException{
    	BufferedReader dataReader = new BufferedReader(new FileReader(aFileName));
    	String line = null;
    	while((line = dataReader.readLine()) != null) {
    		String[] line_split = line.split(" ");
			int i = 0;
    		if(line_split[0] == "t") {
    			numDataNode = Integer.parseInt(line_split[2]);
    			labelData = new int[numDataNode];
    			degreeData = new int[numDataNode];
    		} else if(line_split[0] == "v") {
    			labelData[i] = Integer.parseInt(line_split[2]);		
    			i++;
    		} else if(line_split[0] == "e") {
    			degreeData[Integer.parseInt(line_split[1])] += 1;
    			degreeData[Integer.parseInt(line_split[2])] += 1;
    			adjListQuery[Integer.parseInt(line_split[1])][0] = Integer.parseInt(line_split[2]);
    		}
    	}
    	
    	
    }
    
    public static void readQueryGraph(String aInFile) throws IOException {
    	BufferedReader queryReader = new BufferedReader(new FileReader(aInFile));
    	String line = null;

    	while((line = queryReader.readLine()) != null) {
    		String[] line_split = line.split(" ");
        	if(line_split[0] == "t") {
        		numQueryNode = Integer.parseInt(line_split[2]);
        		labelQuery =  new int[numQueryNode];
        		degreeQuery =  new int[numQueryNode];
        	}else{
        		labelQuery[Integer.parseInt(line_split[0])] = Integer.parseInt(line_split[1]);
        		degreeQuery[Integer.parseInt(line_split[0])] = Integer.parseInt(line_split[2]);
        		adjListQuery[Integer.parseInt(line_split[0])] = new int [Integer.parseInt(line_split[2])];
        		for(int i =0;i<Integer.parseInt(line_split[2]);i++) {
        			adjListQuery[Integer.parseInt(line_split[0])][i] = Integer.parseInt(line_split[i+3]);
        		}
        	}
    	}

    }
    
    public static int selectRoot() {
    	int root = -1;
    	int degree;
    	double rank;
    	double rootRank = Double.MAX_VALUE;
    	for(int i = 0; i< numQueryNode; i++) {
    		degree = degreeQuery[i];
    		rank = degree;
    		if(rank<rootRank) {
    			root = i;
    			rootRank = rank;
    		}
    	}
    	return root;
    }
    
    public static void buildDAG() {
    	char visited[] = new char[numQueryNode];
    	int queue[] = new int[numQueryNode];
    	
    	
    	
    	root = selectRoot();
    	visited[root] = 1;
    	queue[0] = root;
    	
    	
    }
    
    
    
}
