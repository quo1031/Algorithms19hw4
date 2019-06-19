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

    	//System.out.println(args.length);
    	//System.out.println(args[0]);
    	
    	int numQuery = Integer.parseInt(args[2]);
    	int [] DAG;
    	readDataGraph(args[0]);
    	BufferedWriter dag = new BufferedWriter(new FileWriter("human_40n.dag"));
    	BufferedReader queryReader = new BufferedReader(new FileReader(args[1]));
    	String line = null;
   
    	for(int i = 0; i < numQuery; i++) {
    		line = queryReader.readLine();
    		String[] line_split = line.split(" ");
    		numQueryNode = Integer.parseInt(line_split[2]);
    		labelQuery =  new int[numQueryNode];
    		degreeQuery =  new int[numQueryNode];
    		numqueryedge = new int [numQueryNode];
    		DAG = new int [numQueryNode];
    		readQueryGraph(queryReader, numQueryNode);
    		DAG = buildDAG();
    		for(int v = 0; v<numQueryNode; v++) {
    			System.out.print(DAG[v]+" ");
    		}
    		System.out.println();
    	}
    	
    }
    
 
	//Variables for data graph
    static int numDataNode = 0;
    static int[] labelData; // holds label of vertex i
    static int[] degreeData; // holds degree of vertex i
//    static int[] adjListData; // holds edges of vertex i in data graph\
    static ArrayList<Integer> adjListData = new ArrayList<Integer>();
    static ArrayList<Integer> adjIndexData = new ArrayList<Integer>();

    
    //Variables for query graph
    static int root = -1;
    static int numQueryNode = 0;
    static int[] labelQuery; // holds label of vertex i in query graph
    static int[] degreeQuery; // holds degree of vertex i in degree graph
    //static int[] adjListQuery; // holds edges of vertex i in query graph
    static ArrayList<Integer> adjListQuery = new ArrayList<Integer>();
    //static int[] adjIndexQuery;
    static ArrayList<Integer> adjIndexQuery = new ArrayList<Integer>();

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

    		File queryFile = new File(aFileName);
    		FileReader reader = new FileReader(queryFile);
    		BufferedReader dataReader = new BufferedReader(reader);

    	
    	int left;
    	int right;
    	

    	
    	//BufferedReader dataReader = new BufferedReader(new FileReader(aFileName));
    	String line = null;
    	line = dataReader.readLine();
    	//System.out.println(line);
    	String[] line_split = line.split(" ");
    	numDataNode = Integer.parseInt(line_split[2]);
		labelData = new int[numDataNode];
		degreeData = new int[numDataNode];
    	int count = 0;
    	while(count++<numDataNode) {
    		line = dataReader.readLine();
    		//System.out.println(line);
    		line_split = line.split(" ");
			int i = 0;
			//System.out.println(line_split[0]);
    		if(line_split[0].equals("v")) {
    			labelData[i] = Integer.parseInt(line_split[2]);	
    			//System.out.println(labelData[i]);
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
    		int index = 0;
    		adjIndexData.add(index);
    		if(line_split[0].equals("e")) {
    			left = Integer.parseInt(line_split[1]);
    			right = Integer.parseInt(line_split[2]);
				degreeData[left] += 1;
				degreeData[right] += 1;
				adjListData.add(right);
				++index;
				if(relabel.contains(labelData[left]) && relabel.contains(labelData[right])) {
					numedge[relabel.indexOf(labelData[left])][relabel.indexOf(labelData[right])]++;
	numedge[relabel.indexOf(labelData[right])][relabel.indexOf(labelData[left])]++;
				}
			}
    		adjIndexData.add(index);
    	}
    	
    }
    
    public static void readQueryGraph(BufferedReader queryReader, int numQuery) throws IOException {
    	
    	String line;
    	int count=0;
    	int index = 0;
    	adjIndexQuery.add(index);
    	while(count++<numQuery) {
    		line = queryReader.readLine();
    		String[] line_split = line.split(" ");
    		int vnum = Integer.parseInt(line_split[0]); //v number
       		labelQuery[vnum] = Integer.parseInt(line_split[1]);
       		degreeQuery[vnum] = Integer.parseInt(line_split[2]);
       		for(int i =0;i<Integer.parseInt(line_split[2]);i++) {
       			adjListQuery.add(Integer.parseInt(line_split[i+3]));
       			numqueryedge[vnum] += numedge[relabel.indexOf(labelQuery[vnum])]
       				[relabel.indexOf(labelQuery[Integer.parseInt(line_split[i+3])])];      
       			++index;
       		}
       		adjIndexQuery.add(index);
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
    
    public static int [] buildDAG() {
    	char visited[] = new char[numQueryNode];
    	int queue[] = new int[numQueryNode];
    	
    	
    	root = selectRoot();
    	//System.out.println("root = "+root);
    	visited[root] = 1;
    	queue[0] = root;
    	int start=0, end=1;
    	//bfs로 만들어진 level에서 num 순으로 정
    	Queue<Integer> bfs = new LinkedList<Integer>();
    	
    	int v;
    	bfs.add(root);
    	while(!bfs.isEmpty()) {
    		v = bfs.poll();
    		start=end;
   // 		end=start+degreeQuery[v]-1;
    		for(int i=0; i<degreeQuery[v]; i++) {
    			if(visited[adjListQuery.get(adjIndexQuery.get(v)+i)] ==0) {
    				bfs.add(adjListQuery.get(adjIndexQuery.get(v)+i));
    				visited[adjListQuery.get(adjIndexQuery.get(v)+i)]=1;
    				queue[end]=adjListQuery.get(adjIndexQuery.get(v)+i);
    				end++;
    			}
    		}
		sortbyedge(queue, start, end);	
    	}	
    	return queue;
    }

	private static void sortbyedge(int[] queue, int start, int end) {
		// TODO Auto-generated method stub
		int temp;
		for(int i=start; i<end-1; i++) {
			for (int j=i+1; j<end; j++) {
				if(numqueryedge[queue[i]]<numqueryedge[queue[j]]) {
					temp = queue[i];
					queue[i]=queue[j];
					queue[j]=temp;
				}
			}
		}
	}
    
    
    
}