// LaFore Chapter 14
public class Path {
	
	public Path() {
		PathApp pA= new PathApp();
	}
	
	public static void main (String[] args) {
		Path path= new Path();
	}

	class DistPar{ // distance and parent 
		public int distance; // distance from start to this vertex
		public int parentVert; // current parent of this vertex
		
		public DistPar(int pv, int d) { // constructor 
			distance=d;
			parentVert=pv;
		}
	} // end class distPar
	
	class Vertex{ // label, vertex class
		public char label;
		public boolean isInTree;
		
		public Vertex (char lab) {
			label=lab;
			isInTree=false;
		}
	}// end of class vertex
	
	class Graph{
		private final int MAX_VERTS=20; 
		private final int INFINITY=1000000;
		private Vertex [] vertexList;// list of vertices 
		private int [][] adjMat; // adjacency matrix
		private int nVerts;// current number of vertices 
		private int nTree; // number of verts in tree
		private DistPar [] sPath; // array for shortest path data
		private int currentVert; // current Vertex
		private int startToCurrent; // distance to currentVert
		
		public Graph() { // constructor 
			vertexList= new Vertex[MAX_VERTS];
			adjMat= new int[MAX_VERTS][MAX_VERTS];
			nVerts=0;
			nTree=0;
			
			for(int j=0; j<MAX_VERTS;j++) { // set adjacency
				for(int k=0; k<MAX_VERTS; k++) {// matrix
					adjMat[j][k]=INFINITY;// to infinity
				}
			}
			
			sPath= new DistPar[MAX_VERTS];
		} // end graph constructor
		
		public void addVertex(char lab) {
			vertexList[nVerts++]= new Vertex(lab);
		}
		public void addEdge(int start, int end, int weight) {
			adjMat[start][end]= weight; // reduced from the current weight of infinity 
		}
		
		public void path() { // finds all shortest paths
			int startTree=0; // start at vertex 0    --> only does the first index  (will be the index of the find
			vertexList[startTree].isInTree=true;   // --> (make a boolean to conclude its in the tree)
			nTree=1;          // put it in tree   --> count how many will be in the tree bump up one since the first is the start 
			
			//transfer row of distances from adjMat to sPath
			
			for(int j=0;j<nVerts; j++) { // --> this goes across every one that the our start index maps to ( we will probably switch this to loop through the linked list, storing the edgelength)
				int tempDist=adjMat[startTree][j]; // pulling all those from that startTree index --> 
				sPath[j]= new DistPar(startTree,tempDist); // --> could be infinity
			}
			
			// until all vertices are in the tree
			
			while(nTree<nVerts) {
				int indexMin=getMin();
				
				int minDist=sPath[indexMin].distance;
				
				if(minDist==INFINITY) {
					System.out.println("There are unreachable vertices");
					break; // sPath is complete
				}
				else {
										// reset currentVert
					currentVert=indexMin;// to closest vert
					startToCurrent=sPath[indexMin].distance;
					// minimum distance from startTree is
					// to currentVert, and is startToCurrent
				
				}
				// put current vertex in tree 
				vertexList[currentVert].isInTree=true;
				nTree++;
				adjust_sPath();
			}// end while
			
			displayPaths();
			
			nTree=0;
			for(int j=0;j<nVerts;j++) {
				vertexList[j].isInTree=false;
			} //end path
			
		}
		
		public int getMin() {
			int minDist=INFINITY;
			int indexMin=0;
			for(int j=1;j<nVerts;j++) {
				if(!vertexList[j].isInTree && sPath[j].distance< minDist) { // if the vertex is not in the tree and is smaller than the old one
					minDist=sPath[j].distance;
					indexMin=j;// update minimum
				}
			} // end for
			return indexMin;
		} // end getMin()
		
		public void adjust_sPath() {
			// adjust values in shortest- path array sPath
			int column=1;
			
			while(column<nVerts) {
				// if thus column's vertex already in the tree, skip it
				
				if(vertexList[column].isInTree) {
					column++;
					continue;
				}
				// calculate distance for one sPath entry
				// get edge from currentVert to column
				int currentToFringe= adjMat[currentVert][column];
				// add distance from start 
				
				int startToFringe = startToCurrent+ currentToFringe;
							// get distance of current sPath entry 
				int sPathDist=sPath[column].distance;
				
				// compare distance from start with sPath entry 
				
				if(startToFringe<sPathDist) {
					sPath[column].parentVert=currentVert;
					sPath[column].distance= startToFringe;
				}
				
				column++;
				
				
			} // end while 
		} // end adjust sPath
		
		public void displayPaths() {
			for(int j=0;j<nVerts;j++) {
				System.out.println(vertexList[j].label+"=");
				if(sPath[j].distance==INFINITY) {
					System.out.println("inf");
				}
				else {
					System.out.println(sPath[j].distance);
					
				}
				char parent= vertexList[sPath[j].parentVert].label;
				System.out.println("("+parent+") "); // char
			}
			System.out.println("");
		} // end display path
	}// end class graph
	

	class PathApp{
	public PathApp() {
	 Graph theGraph= new Graph();
	 theGraph.addVertex('A');
	 theGraph.addVertex('B');
	 theGraph.addVertex('C');
	 theGraph.addVertex('D');
	 theGraph.addVertex('E');
	 
	 theGraph.addEdge(0, 1, 50);
	 theGraph.addEdge(1, 0, 50);//non Dir
	 theGraph.addEdge(0, 3, 80);
	 theGraph.addEdge(3, 0, 80);//non Dir
	 theGraph.addEdge(1, 2, 60);
	 theGraph.addEdge(2, 1, 80);//non Dir
	 theGraph.addEdge(1, 3, 90);
	 theGraph.addEdge(3, 1, 80);//non Dir
	 theGraph.addEdge(2, 4, 40);
	 theGraph.addEdge(4, 2, 80);//non Dir
	 theGraph.addEdge(3, 2, 20);
	 theGraph.addEdge(2, 3, 80);//non Dir
	 theGraph.addEdge(3, 4, 70);
	 theGraph.addEdge(4, 3, 80);//non Dir
	 theGraph.addEdge(4, 1, 50);
	 theGraph.addEdge(1, 4, 80);//non Dir
	 System.out.println("Shortest Paths");
	 theGraph.path();
	 
	 System.out.println();
	}
	}
}
