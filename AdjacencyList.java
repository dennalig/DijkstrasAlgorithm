
import java.io.File;
import java.io.IOException;
import java.io.FileWriter; //https://www.w3schools.com/java/java_files_create.asp


public class AdjacencyList { // takes the vertices and edges and makes an adjacency list out of them

	private Vertex [] vertices; // array of Vertices
	private int numVerts; // number of vertices 
	
	
	private Vertex currentVert; // our weird linked structure thing, these are carbon copies of certain vertices on the vertex array that are used. 
	
	
	
	
//	private int totalUsedEdges=0;
	public AdjacencyList(int totalVerts) { // constructor
		
		setVertices(new Vertex[totalVerts]);
		setNumVerts(0);
		

		
		
	}
	
	
 // inner subclasses for objects 
	class Vertex{ // inner vertex object
		protected String name; // vertex name
		protected double latitude; // its latitude 
		protected double longitude; // its longitude 
		
		protected int index; /// index to be used in the table 
		
		protected Vertex next; // next pointer for its linked structure
		
		protected boolean alreadyVisited=false;
		protected boolean insertedInArr=false;
		
		protected Edge head; // node object for array index 
		
		public Vertex(String n, double lat, double lon) { // constructor 
			this.name=n;
			this.latitude=lat;
			this.longitude=lon;
		}
	}
	
	class Edge{ // inner node class  we will call this an edge, it will store an int index to the ending vertex and a name of the edge that is provided 
		
		protected int index;
		protected String edgeName;
		protected double weight; // weight of the edge
		
		protected Edge next;//  edge next pointer 
		public Edge(int index, String n, double w) { // Constructor 
			this.index=index;
			this.edgeName=n;
			this.weight=w;
		}
	}
	
	class Path{ // these will be the paths that will be stored in the table (the array)
		
		protected double currentWeight; // the current weight of the entire path
		protected boolean isSelected;// will show if it is selected or not (*)
		protected int indexFrom; // this will store the (Via vertex) as an index to that vertex
		protected int indexTo;
		protected Path next;// path next pointer
		
		
		public Path() {
			this.currentWeight=1000000000; // this is the infinite weight  (LaFore pg. 704)
			this.isSelected=false;
			indexFrom=-1;
			indexTo=-1;
		}
		

		
	}
	
	class SelectedPaths{ //linked Structure to store all of the selected paths to look at , we will only be able to look at the indices of those selected Paths 
		protected Path head;
		public SelectedPaths() {
			head=null;
		}
		
		public void insertSPath(Path p) {// insertion method
			Path current=head;
			
			if(head==null) {
				head=p;
				return;
			}
			else {
				while(current.next!=null) {
					current=current.next;
				}
				current.next=p;
				return;
			}
		}
	}


	
	
	public void addVertex(String name, double latitude, double longitude) { // adds the vertex to the array (not the edge)

		vertices[numVerts]= new Vertex(name,latitude, longitude);
		vertices[numVerts].index=numVerts;
		numVerts++;
	}
	
	
	public void addEdge(int indexOfStart, int indexOfEnd, String edgeName) { // adds an edge between two vertices in the adj matrix (Node handling here)
		Vertex fromVert=vertices[indexOfStart];
		
		Edge current= fromVert.head;
		
		if(fromVert.head==null) {// head case
//			System.out.println(edgeName+": ");
			fromVert.head=new Edge(indexOfEnd,edgeName, calcEdgeDist(fromVert.latitude,fromVert.longitude, vertices[indexOfEnd].latitude, vertices[indexOfEnd].longitude));
			return;
		}
		else {// pointer case 
			
			while(current.next!=null) {
				current=current.next;
			}
//			System.out.println(edgeName+": ");
			current.next=new Edge(indexOfEnd,edgeName, calcEdgeDist(fromVert.latitude,fromVert.longitude, vertices[indexOfEnd].latitude, vertices[indexOfEnd].longitude));
			return;
		}
	}
	
	public double calcEdgeDist(double lat1, double lon1, double lat2, double lon2){// Author: Dr. John Hunt
		double R = 6371; //km
		double radLat1 = Math.toRadians(lat1);
		double radLat2 = Math.toRadians(lat2);
		double radDiffLat = Math.toRadians(lat2-lat1);
		double radDiffLon = Math.toRadians(lon2-lon1);
		
		double a = Math.sin(radDiffLat/2) * Math.sin(radDiffLat/2) +
				Math.cos(radLat1) * Math.cos(radLat2) *
				Math.sin(radDiffLon/2) * Math.sin(radDiffLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a),  Math.sqrt(1-a));
//		System.out.println(R*c);
		return R * c;
	}
	
	
	public void calcDijkstra(String location, String destination, boolean atB) {// method to calculate Dijkstra between two vertices 
		
		// I tried to walk through the video and apply its logic
		//https://www.youtube.com/watch?v=4wJJ_oQzmRE&feature=youtu.be
		// and apply its logic
		// rather than use the LaFore example, which confused me more. 
		
		Vertex startVert=getVertex(location);// start vertex
		Vertex endVert=getVertex(destination); // ending vertexs
		if(startVert==null) {
			System.err.println(location+ " not found. ");
			return;
		}
		if(endVert==null) {
			System.err.println(destination+ " not found. ");
			return;
		}
		
		
		Path [] table= new Path[numVerts];// an array of path objects, this is the data that we will be updating as we apply the algorithm
	
		for(int i=0;i<table.length;i++) {// initializes all paths as infinite (in the Path constructor)
			table[i]= new Path();
			table[i].indexTo=i;
			if(i==startVert.index) {// for the vertices path to itself
				table[i].isSelected=true;// setting it to true now will allow our algorithm to ignore it as it will only pick up non selected paths.
				table[i].currentWeight=0;
				table[i].indexFrom=i;
			}
//			System.out.println(i+ " "+table[i].isSelected);
		}
		
		// initialize the first set of vertices 
//	System.out.println(destination);
		Edge current=startVert.head;// start of the edges the starting vertices has 
		double smallestWeight=-1;
		int selectedVertInd=-1;
		while(current!=null) { // initializes the parts of the path that are important /connected to our starting vertex
			table[current.index].indexFrom=startVert.index; //inside that index in our table array
			table[current.index].currentWeight=current.weight;
			
			if(smallestWeight==-1) {// default weight that changes
				smallestWeight=current.weight;
				selectedVertInd=current.index;
			}
			else if(smallestWeight>current.weight) {// Distinguishes which path has the smallest weight altogether for our first selection
				smallestWeight=current.weight;
				selectedVertInd=current.index;
			}
		
//			System.out.println(current.index);
//			System.out.println(startVert.index);
//			System.out.println(current.weight);
			
			current=current.next;
		}
		
//		System.out.println(table[selectedVertInd].currentWeight);
		table[selectedVertInd].isSelected=true; // we declare the first selected path (we will no longer look at it to add into our finalized paths)
		
		SelectedPaths currentPaths= new SelectedPaths();// we make our selected paths object which will have a linked Structure of selected paths 
		currentPaths.insertSPath(table[selectedVertInd]); // insert the new one into the selected path linked structure
		

		
		while(!table[endVert.index].isSelected) { // this should run until the path to our end destination is finalized
				Path pathCurr=currentPaths.head;// the current SELECTED PATHS that we have to see if any of the ending vertices connect to any other vertices (rather than searching the array every time) 
				
				while(pathCurr!=null) {// loop through our selected structure of paths 
					Vertex position = vertices[pathCurr.indexTo];// Vertex of the current Paths' path To Vertex
					
					Edge eCurr=position.head;// we get the current Path's path- To Vertex's first Edge 
					
					while(eCurr!=null) { // loops through the edges connected to our path's to -- to find which ones are linked to the other vertices
						// we must get the first edge's pathTo
						if(pathCurr.currentWeight+eCurr.weight< table[eCurr.index].currentWeight) { // if the current path weight + the current edge's weight < the edge's ending vert's path weight on the table
							table[eCurr.index].currentWeight=pathCurr.currentWeight+eCurr.weight; // we assign the new path weight 
							table[eCurr.index].indexFrom=position.index; // we assign the index from to the path
							
							
						}
						
						eCurr=eCurr.next;// loops through the edges of the current path's ending vertices that havent been mapped further
					}
					
					pathCurr=pathCurr.next;
				}
				
				// this is where we select the next lightest weight path (and to add to our linked structure)
			
				int pathInd=-1; // stores the lightest path's index
				double lightestWeight=-1; // stores the lightest weight
				
				for(int k=0;k<table.length;k++) {
					
					if(!table[k].isSelected) { // if not selected
						
						if(lightestWeight==-1) { // initial
							lightestWeight=table[k].currentWeight;
							pathInd=k;
						
						}
						else if(lightestWeight>table[k].currentWeight){
							lightestWeight=table[k].currentWeight;
							pathInd=k;
						
						}
					}
				}
			
				currentPaths.insertSPath(table[pathInd]);// we the next lightest path into the linked list
				table[pathInd].isSelected=true; // if this works out  it will put it in the stack before exiting the loop
				
				
				
			}	// end while
	
		// we make the new path stack in here 
		VertexStack vStack=  new VertexStack();
		// we will process data here as well
		int desiredPlace=endVert.index;
	
		while(desiredPlace!=startVert.index) { // while we are not at the desired index we want at the beginning 
			vStack.push(desiredPlace);
//			System.out.println("Pushing "+ vertices[desiredPlace].name);
			desiredPlace=table[desiredPlace].indexFrom;
			
			if(desiredPlace==-1) {
				System.err.println(destination+" is not a reachable Vertex. ");
				System.exit(1); //https://stackoverflow.com/questions/32877405/how-do-i-terminate-a-java-program-if-a-certian-condition-isnt-met
			}
		}
		if(atB==true) { // if we are at the beginning we push the if this is false it means that it is already on the stack and we dont want to add it again
			vStack.push(desiredPlace); // this pushes the startVerex
		}
		while(!vStack.isEmpty()) {
//			System.out.println(vertices[vStack.pop()].name);
			int now= vStack.pop();// we pop off the stack 
			if(currentVert==null) { // we insert a copy of the vertex at the popped off index (it will insert the start as the head and the last one should be the last on the linked structure)
				// these copies do not have edges to other vertices, so we refer to the original list when getting the name of the edge and how it was connected to its .next 
				
				// if we have more than 1 final destination, this allows us to not get pointers confused 
				currentVert=new Vertex(vertices[now].name,vertices[now].latitude,vertices[now].longitude);
				
//				System.out.println("Inserting: "+vertices[now].name);
			}
			else { // insertion into the linked Structure 
				Vertex pointer=currentVert;
				
				while(pointer.next!=null) {
					pointer=pointer.next;
				}
				pointer.next=new Vertex(vertices[now].name,vertices[now].latitude,vertices[now].longitude);
		
//				System.out.println("Inserting: "+vertices[now].name);
			}
		
		}
	
		//printing purposes
//		Vertex pointer=currentVert;
//		
//		while(pointer!=null) {
//			System.out.println(pointer.name);
//			pointer=pointer.next;
//		}
	
//		System.out.println("(new Trip)");
		
		
		
		
		
//		System.out.println(table[endVert.index].currentWeight);
//		System.out.println(vertices[table[endVert.index].indexFrom].name);
		

		}
	
	
	public void dijkstraOutPut(File file) throws IOException{ // outputs to a file 
	
	FileWriter writer= new FileWriter(file); // File writer object
	//https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html
	//https://stackoverflow.com/questions/19084352/how-to-write-new-line-character-to-a-file-in-java
	Vertex pointer= currentVert; // current
	int countVerts=0;// array size for references into the new file 
	int countEdges=-1;
	
	
	System.out.println("Counting Vertices and Edges...");
	while(pointer!=null) {
		
		if(!getVertex(pointer.name).alreadyVisited) {

		countVerts++;
	
		getVertex(pointer.name).alreadyVisited=true;
		}
		countEdges++;
//		System.out.println(pointer.name + " "+ pointer.latitude+", "+ pointer.longitude);
		pointer=pointer.next;
		
	}
	
	pointer=currentVert;
	Vertex [] newVertices= new Vertex[countVerts];
	
	countVerts=0;
	System.out.println("Collecting Vertices...");
	while(pointer!=null) {
		if(!getVertex(pointer.name).insertedInArr) {

			newVertices[countVerts++]=pointer;
			getVertex(pointer.name).insertedInArr=true;
		}
		pointer=pointer.next;
	}
//	System.out.println("-------");
//	System.out.println(countVerts +" "+ countEdges); // gra data
	writer.write(countVerts+" "+countEdges);
	writer.append('\n');
	for(int i=0;i<newVertices.length;i++) {
//		System.out.println(newVertices[i].name + " "+newVertices[i].latitude+ ", "+newVertices[i].longitude); // gra data
	writer.write(newVertices[i].name + " "+newVertices[i].latitude+ ", "+newVertices[i].longitude);
	writer.append('\n');
	}
	
	
	pointer=currentVert; // this goes through the vertices 
	System.out.println("Collecting Relations...");
	while(pointer.next!=null) {// while not at the end 
		
		Vertex nextVert=getVertex(pointer.next.name); // reference to the original array ( the adjacency list)
		
		Edge currEdge=getVertex(pointer.name).head; // gets the edges 
		
		while(currEdge.index!=nextVert.index) {
			currEdge=currEdge.next;
		}
		
		int startInd=0;
		int nextInd=0;
		for(int i=0;i<newVertices.length;i++) {
			if(pointer.name.equals(newVertices[i].name)) {
				startInd=i;
				break;
			}
		}
		
		for(int i=0;i<newVertices.length;i++) {
			if(nextVert.name.equals(newVertices[i].name)) {
				nextInd=i;
				break;
			}
		}
		
//		System.out.println(startInd+" "+nextInd+" "+currEdge.edgeName);  // gra data
		writer.write(startInd+" "+nextInd+" "+currEdge.edgeName);
		writer.append('\n');
		pointer=pointer.next;
		
	}
	writer.flush();
	writer.close();
	


		


	}
	
	public Vertex getVertex(String name) { // finds the Vertex if it exists
		for(int i=0;i<vertices.length;i++) {
			if(vertices[i].name.equals(name)) {
				return vertices[i];
			}
		}
		return null;
	}
	
	
	// print methods
	
	public void printVertices() {// just prints all vertices
		int count=0;
		
		while(count<numVerts) {
			System.out.println(vertices[count].name + " Lat: "+ vertices[count].latitude+ " Lon: "+ vertices[count].longitude);
			count++;
		}
	}
	
	public void printAdjList() { // prints relations 
		
		for(int i=0;i<vertices.length;i++) {
			System.out.println("Relations From: "+ vertices[i].name);
			
			Edge current= vertices[i].head;
			
			while(current!=null) {
				System.out.println(" "+ vertices[current.index].name+" --> Edge: ("+current.edgeName+")");
				current=current.next;
			}
		}
	}
	
	// getters and setters 
	
	public Vertex [] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex [] vertices) {
		this.vertices = vertices;
	}

	public int getNumVerts() {
		return numVerts;
	}

	public void setNumVerts(int numVerts) {
		this.numVerts = numVerts;
	}


//
//	public int getTotalUsedEdges() {
//		return totalUsedEdges;
//	}
//
//
//	public void setTotalUsedEdges(int totalUsedEdges) {
//		this.totalUsedEdges = totalUsedEdges;
//	}



	public Vertex getCurrentVert() {
		return currentVert;
	}


	public void setCurrentVert(Vertex currentVert) {
		this.currentVert = currentVert;
	}
}
