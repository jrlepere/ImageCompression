package compression_algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import io.Utilities;

/**
 * Huffman Coding
 * @author JLepere2
 * @date 06/01/2018
 */
public class HuffmanCoding implements CompressionAlgorithm {

	public void compress(int[][] image) {
		
		// timer
		long startTime = System.currentTimeMillis();
		
		// calculate frequencies
		int[] frequencies = new int[256];
		for (int y = 0; y < image.length; y ++) {
			for (int x = 0; x < image.length; x ++) {
				frequencies[image[y][x]] ++;
			}
		}
		
		// priority queue for huffman coding
		PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
			public int compare(Node o1, Node o2) {
				return o1.frequency - o2.frequency;
			}
		});
		
		// add initial nodes to pq
		for (int i = 0; i < frequencies.length; i ++) {
			int freq = frequencies[i];
			if (freq != 0) {
				// create new node to be inserted into pq
				Node n = new Node(null, null);
				n.setFrequency(freq);     // set the frequency
				n.setValue(i);            // set the gv
				pq.add(n);
			}
		}
		
		// huffman coding
		while (pq.size() > 1) {
			// pop and combine the smallest two nodes and add back to pq
			pq.add(new Node(pq.remove(), pq.remove()));
		}
		
		// list for the final codings
		//List<CodeNode> codings = new LinkedList<>();
		Map<Integer, String> codings = new HashMap<>();
		
		// set the codings from the root node
		generateCodings(codings, pq.remove(), "");
		
		// compression duration
		long endTime = System.currentTimeMillis();
		
		// num bytes
		int numBytes = 0;
		for (Integer key : codings.keySet()) {
			// byte length to encode * frequency + num bytes to store key
			numBytes += (((codings.get(key).length()) / 8) + 1) * frequencies[key] + 4;
		}
		
		// show compression statistics
		Utilities.showStatistics("COMPRESSION", endTime - startTime, numBytes);
		
		// save the file
		Utilities.saveHuffmanCSV(codings, image, EXTENSION);
		
	}
	
	/**
	 * Generates the codings.
	 * @param codings the codings map
	 * @param n the current node
	 * @param code the current code
	 */
	public void generateCodings(Map<Integer, String> codings, Node n, String code) {
		if (n.leftChild == null && n.rightChild == null) {
			codings.put(n.value, code);
			return;
		}
		generateCodings(codings, n.leftChild, code = "0" + code);
		generateCodings(codings, n.rightChild, code = "1" + code);
	}

	
	public int[][] decompress() {
		
		// get scanner for huffman encoded image
		Scanner s = Utilities.loadHuffmanCSV(EXTENSION);
		
		// validate
		if (s == null) return null;
		
		// timer
		long startTime = System.currentTimeMillis();
		
		// get the number of coded gray values
		int numCodings = Integer.parseInt(s.nextLine().trim());
		
		// hashmap for encodings
		Map<String, Integer> codes = new HashMap<>();
		
		// set code map
		for (int i = 0; i < numCodings; i ++) {
			// get and split the line
			String[] splitLine = s.nextLine().trim().split(",");
			// get the code and gv, and add to hashmap
			codes.put(splitLine[0], Integer.parseInt(splitLine[1]));
		}
		
		// reconstructed image
		int[][] image = new int[IMAGE_SIZE][IMAGE_SIZE];
		
		// set the image
		for (int y = 0; y < IMAGE_SIZE; y ++) {
			// get and split the line
			String[] splitLine = s.nextLine().trim().split(",");
			for (int x = 0; x < IMAGE_SIZE; x ++) {
				image[y][x] = codes.get(splitLine[x]);
			}
		}
		
		// decompression duration
		long endTime = System.currentTimeMillis();
		
		// show decompression statistics
		Utilities.showStatistics("DECOMPRESSION", endTime - startTime, 0);
		
		// return the image
		return image;
	}
	
	public String toString() {
		return "Huffman Coding";
	}

	/*
	 * Node for encoding
	 */
	class Node {
		public Node(Node leftChild, Node rightChild) {
			this.leftChild = leftChild;
			this.rightChild = rightChild;
			if (leftChild != null && rightChild != null)
				this.frequency = leftChild.frequency + rightChild.frequency;
		}
		public void setValue(int val) { this.value = val; }
		public void setFrequency(int freq) { this.frequency = freq; }
		Node leftChild, rightChild;
		int value;
		int frequency;
	}
	
	public class CodeNode {
		public CodeNode(String code, int val) {
			this.code = code;
			this.val = val;
		}
		public String code;
		public int val;
	}
	
	private static final String EXTENSION = ".HCSV";
	private static final int IMAGE_SIZE = 512;
	
}
