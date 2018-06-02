package compression_algorithms;

import java.util.LinkedList;

import io.Utilities;

/**
 * Run Length Coding compression algorithm
 * @author JLepere2
 * @date 06/01/2018
 */
public class RunLengthCoding implements CompressionAlgorithm {

	public void compress(int[][] image) {
		
		// timer
		long startTime = System.currentTimeMillis();
				
		// Linked List for run length encoding where
		LinkedList<Node> runLength = new LinkedList<>();
		
		// calculate run length coding
		Node currentNode = null;
		for (int y = 0; y < image.length; y ++) {
			for (int x = 0; x < image[0].length; x ++) {
				// get the gray value
				int gv = image[y][x];
				
				if (currentNode == null || gv != currentNode.gv) {
					// new gv
					Node newNode = new Node(gv);   // create new node
					runLength.add(newNode);        // add to list
					currentNode = newNode;         // set new current node
				} else {
					// same gv
					currentNode.increment();
				}
			}
		}
		
		// byte array for saving where first is gv, second is count
		byte[] byteArray = new byte[runLength.size()*2];
		
		// set byte array
		int i = 0;
		for (Node n : runLength) {
			// get gv and count
			byte gv = (byte) n.gv;
			byte count = (byte) n.count;
			
			// set byte array
			byteArray[i] = gv;
			byteArray[i+1] = count;
			
			// increment counter
			i += 2;
		}
		
		// compression duration
		long endTime = System.currentTimeMillis();
		
		// show compression statistics
		Utilities.showStatistics("COMPRESSION", endTime - startTime, byteArray.length);
		
		// save byte array
		Utilities.saveByteArray(byteArray, EXTENSION);
		
	}

	public int[][] decompress() {
		
		// load the byte array file
		byte[] data = Utilities.loadByteArray(EXTENSION);
		
		// timer
		long startTime = System.currentTimeMillis();
		
		// check if null
		if (data == null) { return null; }
		
		// decompressed image
		int[][] image = new int[IMAGE_SIZE][IMAGE_SIZE];
		
		// variables
		int y = 0;     // current image y
		int x = 0;     // current image x
		int b = 0;     // current byte
		
		// set image
		while (b < data.length) {
			
			// get gv and count
			int gv = data[b];
			int count = data[b+1] & 0xFF;
			
			// set image
			while (count > 0) {
				// set pixel
				image[y][x] = gv;
				
				// increment image x and y
				x += 1;
				if (x == IMAGE_SIZE) {
					x = 0;
					y += 1;
				}
				
				// decrement count
				count --;
			}
			
			// increment b
			b += 2;
		}
		
		// decompression duration
		long endTime = System.currentTimeMillis();
		
		// show decompression statistics
		Utilities.showStatistics("DECOMPRESSION", endTime - startTime, 0);
		
		return image;
	}
	
	public String toString() {
		return "Run Length Coding";
	}
	
	/*
	 * Node for Linked List
	 */
	class Node {
		public Node(int gv) {
			this.gv = gv;
			this.count = 1;
		}
		public void increment() { count ++; }
		int gv;
		int count;
	}
	
	private static final String EXTENSION = ".RLC";
	private static final int IMAGE_SIZE = 512;

}
