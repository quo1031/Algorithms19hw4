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
    	
    	BufferedReader queryReader = new BufferedReader(new FileReader(args[2]));
    	String line = null;
   
    	for(int i = 0; i < numQuery; i++) {
    		line = queryReader.readLine();
    		String[] line_split = line.split(" ");
    		numQueryNode = Integer.parseInt(line_split[2]);
    		labelQuery =  new int[numQueryNode];
    		degreeQuery =  new int[numQueryNode];
    		numqueryedge = new int [numQueryNode];
    		readQueryGraph(queryReader, numQueryNode);
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
    static int[] numqueryedge;
    
    //Variables for query DAG
    static int[] dagChildQuery;
    static int[] dagParentQuery;
    static int[] dagChildQuerySize;
    static int[] dagParentQuerySize;
    
    //additional variable
    static ArrayList<Integer> labelset = new ArrayList<Integer>();
    static ArrayList<Integer> relabel = new ArrayList<Integer>();
    static ArrayList<Integer> numlabel = new ArrayList<Integer>();
    static int numoflabel;
	static int [][] numedge;
    
    
    public static void readDataGraph(String aFileName) throws IOException{
    	int left;
    	int right;
    	
    	BufferedReader dataReader = new BufferedReader(new FileReader(aFileName));
    	String line = null;
    	line = dataReader.readLine();
    	String[] line_split = line.split(" ");
    	numDataNode = Integer.parseInt(line_split[2]);
		labelData = new int[numDataNode];
		degreeData = new int[numDataNode];
    	int count = 0;
    	while(count++<numDataNode) {
    		line = dataReader.readLine();
    		line_split = line.split(" ");
			int i = 0;
    		if(line_split[0] == "v") {
    			labelData[i] = Integer.parseInt(line_split[2]);		
    			labelset.add(labelData[i]);
    			i++;
    		} 
    	}
    	Collections.sort(labelset);
    	relabel.add(labelset.get(0));
    	numlabel.add(1);
    	for(int i=1; i<labelset.size()-1; i++) {
    		if(labelset.get(i-1) != labelset.get(i)) {
    			relabel.add(labelset.get(i));
    			numlabel.add(1);
    		}else {
    			numlabel.set(numlabel.size()-1, numlabel.get(numlabel.size()-1)+1);
    		}
    	}
    	numoflabel = relabel.size();
    	numedge = new int [numoflabel][numoflabel];
    	
    	while((line = dataReader.readLine()) != null) {
    		line_split = line.split(" ");
    		if(line_split[0] == "e") {
    			left = Integer.parseInt(line_split[1]);
    			right = Integer.parseInt(line_split[2]);
				degreeData[left] += 1;
				degreeData[right] += 1;
				adjListQuery[Integer.parseInt(line_split[1])][0] = Integer.parseInt(line_split[2]);
				if(relabel.contains(labelData[left]) && relabel.contains(labelData[right])) {
					numedge[relabel.indexOf(labelData[left])][relabel.indexOf(labelData[right])]++;
				}
			}
    	}
    	
    }
    
    public static void readQueryGraph(BufferedReader queryReader, int numQuery) throws IOException {
    	
    	String line;
    	int count=0;
    	while(count++<numQuery) {
    		line = queryReader.readLine();
    		String[] line_split = line.split(" ");
    		int vnum = Integer.parseInt(line_split[0]); //v number
       		labelQuery[vnum] = Integer.parseInt(line_split[1]);
       		degreeQuery[vnum] = Integer.parseInt(line_split[2]);
       		adjListQuery[vnum] = new int [Integer.parseInt(line_split[2])];
       		for(int i =0;i<Integer.parseInt(line_split[2]);i++) {
       			adjListQuery[Integer.parseInt(line_split[0])][i] = Integer.parseInt(line_split[i+3]);
       			numqueryedge[vnum] += numedge[vnum][Integer.parseInt(line_split[i+3])];
    		}
    	}

    }
    
    public static int selectRoot() {
    	int root = 0;
  //  	int degree;
  //	double rank;
  //	double rootRank = Double.MAX_VALUE;
    	for(int i = 1; i< numQueryNode; i++) {
    		if(numqueryedge[i]>numqueryedge[root])
    			root = i;
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
