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

    	int numQuery = Integer.parseInt(args[2]);
    	readDataGraph(args[0]);
    	BufferedReader queryReader = new BufferedReader(new FileReader(args[1]));
    	String line = null;
   
    	for(int i = 0; i < numQuery; i++) {
    		line = queryReader.readLine();
    		String[] line_split = line.split(" ");
    		numQueryNode = Integer.parseInt(line_split[2]);
    		labelQuery =  new int[numQueryNode];
    		degreeQuery =  new int[numQueryNode];
    		numQueryEdge = new int [numQueryNode];
    		readQueryGraph(queryReader, numQueryNode);
    		buildDAG();
    	}
    	
    }
    
 
	//Variables for data graph
    static int numDataNode = 0;
    static int[] labelData; // holds label of vertex i
    static int[] degreeData; // holds degree of vertex i
    static ArrayList<Integer> adjListData = new ArrayList<Integer>(); // holds edges of vertex in data graph
    static ArrayList<Integer> adjIndexData = new ArrayList<Integer>();

    
    //Variables for query graph
    static int root = -1;
    static int numQueryNode = 0;
    static int[] labelQuery; // holds label of vertex i in query graph
    static int[] degreeQuery; // holds degree of vertex i in degree graph
    static ArrayList<Integer> adjListQuery = new ArrayList<Integer>(); // holds edges of vertex in query graph
    static ArrayList<Integer> adjIndexQuery = new ArrayList<Integer>();
    static int[] numQueryEdge;
    
    
    //additional variable
    static ArrayList<Integer> labelSet = new ArrayList<Integer>();
    static ArrayList<Integer> relabel = new ArrayList<Integer>();
    static ArrayList<Integer> numLabel = new ArrayList<Integer>();
    static int numOfLabel;
	static int [][] numEdge;
    
    
    public static void readDataGraph(String aFileName) throws IOException{
    	File queryFile = new File(aFileName);
    	FileReader reader = new FileReader(queryFile);
    	BufferedReader dataReader = new BufferedReader(reader);

    	int left;
    	int right;
    	String line = null;
    	line = dataReader.readLine();
    	String[] line_split = line.split(" ");
    	
    	numDataNode = Integer.parseInt(line_split[2]);
		labelData = new int[numDataNode];
		degreeData = new int[numDataNode];
		
    	int count = 0;
    	int v = 0;
    	while(count++<numDataNode) {
    		line = dataReader.readLine();
    		line_split = line.split(" ");
    		if(line_split[0].equals("v")) {
    			labelData[v] = Integer.parseInt(line_split[2]);	
    			labelSet.add(labelData[v]);
    			v++;
    		} 
    	}
    	Collections.sort(labelSet);
    	relabel.add(labelSet.get(0));
    	numLabel.add(1);
    	for(int i=1; i<labelSet.size(); i++) {
    		if(labelSet.get(i-1) != labelSet.get(i)) {
    			relabel.add(labelSet.get(i));
    			numLabel.add(1);
    		}else {
    			numLabel.set(numLabel.size()-1, numLabel.get(numLabel.size()-1)+1);
    		}
    	}
	
    	numOfLabel = relabel.size();
    	numEdge = new int [numOfLabel][numOfLabel];
    	
    	while((line = dataReader.readLine()) != null) {
    		line_split = line.split(" ");
    		int index = 0;
    		adjIndexData.add(index);
    		if(line_split[0].equals("e")) {
    			left = Integer.parseInt(line_split[1]);
    			right = Integer.parseInt(line_split[2]);
				degreeData[left] += 1;
				degreeData[right] += 1;
				adjListData.add(right);
				++index;
				
	numEdge[relabel.indexOf(labelData[left])][relabel.indexOf(labelData[right])]++;
	if(labelData[left]!=labelData[right]){
	numEdge[relabel.indexOf(labelData[right])][relabel.indexOf(labelData[left])]++;
			}
		}
    		adjIndexData.add(index);
    	}
    	
    }
    
    public static void readQueryGraph(BufferedReader queryReader, int numQuery) throws IOException {
    	String line;
    	int count=0;
    	int index = 0;
    	
    	adjListQuery.clear();
    	adjIndexQuery.clear();
    	adjIndexQuery.add(index);
    	
    	while(count++<numQuery) {
    		line = queryReader.readLine();
    		String[] line_split = line.split(" ");
    		int vnum = Integer.parseInt(line_split[0]); //v number
       		labelQuery[vnum] = Integer.parseInt(line_split[1]);
       		degreeQuery[vnum] = Integer.parseInt(line_split[2]);
       		for(int i =0;i<Integer.parseInt(line_split[2]);i++) {
       			adjListQuery.add(Integer.parseInt(line_split[i+3]));
       			numQueryEdge[vnum] += numEdge[relabel.indexOf(labelQuery[vnum])]
       				[relabel.indexOf(labelQuery[Integer.parseInt(line_split[i+3])])];      
       			++index;
       		}
       		adjIndexQuery.add(index);
    	}

    }
    
    public static int selectRoot() {
    	int root = 0;

    	int edge;
    	double rank;
    	double rootRank = Double.MAX_VALUE;
    	
    	for(int i = 1; i< numQueryNode; i++) {

    		edge = numQueryEdge[i];
    		rank = edge;
    		if(rank<rootRank){
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

    	int start=0, end=1;

    	int v;
    	
    	DFSUtil(root, visited);
   
    }
    
    private static void DFSUtil(int v, char visited[]) {
    	visited[v] = 1;
    	System.out.print(v + " ");
    	Iterator<Integer> i = adjListQuery.listIterator();
    	while(i.hasNext()) {
    		int n = i.next();
    		if(visited[n] == 0) {
    			DFSUtil(n, visited);
    		}
    	}
    }
}