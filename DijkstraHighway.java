import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

//Dennali Grissom


public class DijkstraHighway {
	
	public static void main (String [] args) throws FileNotFoundException, IOException{ // main
	
		if(args.length<4) { // inputFileName, outPutFileName, StaringPoint, (optional) point......., EndingPoint 
			
			System.err.println("1. Please Enter a '.gra' input file , 2. Please specify a '.gra' output file, "
					+ "3. Enter the names of at least 2 vertices to find a path through the .'gra' file. ");
			return;
		}
		
	
	
		try {
		File inFile=new File(args[0]); // input file
		File outFile= new File(args[1]); // file we make
		String inFileName=inFile.toString();
		String outFileName=outFile.toString();
		
		
		if(!inFileName.substring(inFileName.length()-4,inFileName.length()).equals(".gra")) { // checks input file name 
			System.err.println("Enter a '.gra' file to scan. ");
			return;
		}
		if(!outFileName.substring(outFileName.length()-4,outFileName.length()).equals(".gra")) {// checks output file name 
			System.err.println("Enter a file name that ends in '.gra' to make to use at : http://courses.teresco.org/metal/hdx/. ");
			return;
		}
		
		String[] destinations= new String[args.length-2]; // makes a sub array of strings of all the listed destinations 
//		System.out.println(destinations.length);
		
		for(int i=0;i<destinations.length;i++) { // pulls from args and initializes the array of destinations 
			destinations[i]=args[2+i];// starts with the 2nd index and goes from there 
//			System.out.println(destinations[i]);
		}
		
		
		Scanner scanIn= new Scanner(inFile);
		
		String headerLine=scanIn.nextLine();// header line with number of vertices and edges 
		
		Scanner lineScan= new Scanner(headerLine);
		int numVerts=lineScan.nextInt(); // counts our vertices
//		System.out.println(numVerts);
		
//		int numEdges= lineScan.nextInt(); // counts number edges
		
		AdjacencyList adjList= new AdjacencyList(numVerts);
		
		String enteredVertex=scanIn.nextLine();
		
		
		
	System.out.println("Adding Vertices... ");
		while((!Character.isDigit(enteredVertex.charAt(0)))) { // goes until the first char in the string is a number 
			lineScan= new Scanner(enteredVertex);// scans the line for the needed data
//			System.out.println(enteredVertex);
			String vertexName=lineScan.next();
			Double vLat= Double.parseDouble(lineScan.next().replace(",", "")); // latitude
			Double vLon=lineScan.nextDouble(); // longitude 
			
			adjList.addVertex(vertexName, vLat, vLon);// we add the text to the adjacencyList

//			System.out.println(vLat);
//			System.out.println(vLon);
			enteredVertex=scanIn.nextLine();
			
		
		}
		// all vertices should be added at this point
		
//		adjList.printVertices();
		
		String enteredEdge=enteredVertex;// the 'last vertex' is actually the first edge 
		System.out.println("Adding Edges... ");
		while(scanIn.hasNext()) {
		
			lineScan=new Scanner(enteredEdge);
			int startIndex=lineScan.nextInt();
//			System.out.println(startIndex);
			int endIndex=lineScan.nextInt();
//			System.out.println(endIndex);
			String eName=lineScan.next();
//			System.out.println(eName);
			
			adjList.addEdge(startIndex, endIndex, eName); 
			adjList.addEdge(endIndex, startIndex, eName); // this is a bi-directional graph, so it must be called again with the vertices reversed 
			enteredEdge=scanIn.nextLine();
			
		}
		// manually add the last one 
		lineScan.close();
		lineScan= new Scanner(enteredEdge);
		int startIndex=lineScan.nextInt();
//		System.out.println(startIndex);
		int endIndex=lineScan.nextInt();
//		System.out.println(endIndex);
		String eName=lineScan.next();
//		System.out.println(eName);
		adjList.addEdge(startIndex, endIndex, eName);
		adjList.addEdge(endIndex, startIndex, eName);
		
		
//		adjList.printAdjList();
		lineScan.close();
		 
		
		scanIn.close();
		
		// here is where we run across the string array with the offered vertices
		
		boolean atBeginning=true; // boolean so that we know whether pop the last vertex or not in calcDijkstra
		System.out.println("Calculating Dijkstra...");
		for(int i=0;i<destinations.length-1;i++) { // this staggers the vertices we look at so it doesnt go past the end 
//			System.out.println(destinations[i]+ " "+ destinations[i+1]);
			
			adjList.calcDijkstra(destinations[i], destinations[i+1], atBeginning); // method that calculates dijkstra

			atBeginning=false;
		}
		System.out.println("Generating File...");
		adjList.dijkstraOutPut(outFile); // method that processes the data and generate the results file
		
		System.out.println(outFileName+" is created.");
		
//		System.out.println(adjList.getPathStack().getTotalCount());
//		System.out.println(adjList.getPathStack().getEdgeCount());
		
		}
		catch(FileNotFoundException e){
			System.err.println(args[0]+" not found. ");
		}
	}

}
