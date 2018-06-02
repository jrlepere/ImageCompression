package io;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * IO Utilities
 * @author JLepere2
 * @date 06/01/2018
 */
public class Utilities {

	/**
	 * Gets the default image to display.
	 * @return the default image.
	 * @throws IOException 
	 */
	public static int[][] getDefaultImage() throws IOException {
	
		// read the image into a buffered image object
		BufferedImage buffImage = ImageIO.read(Utilities.class.getResourceAsStream(DEFAULT_IMAGE_NAME));
		
	 	// integer matrix representation of the image
	 	int[][] image = new int[buffImage.getHeight()][buffImage.getWidth()];
		for (int y = 0; y < image.length; y ++) {
			for (int x = 0; x < image[0].length; x ++) {
				image[y][x] = buffImage.getRGB(x, y) & 0xff;
			}
		}
		
		// return the image
		return image;
	}
	
	/**
	 * Saves a byte array to file.
	 * @param byteArray the byte array to save
	 * @param extension the filename extension
	 */
	public static void saveByteArray(byte[] byteArray, String extension) {
		
		// file chooser
		JFileChooser fileChooser = new JFileChooser();
		
		// show file chooser
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			// get the filename of the selected file to save
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			if (!filename.endsWith(extension)) {
				filename += extension;
			}
			
			// try saving the file
			try (FileOutputStream fos = new FileOutputStream(filename)) {
				   fos.write(byteArray);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "There was an error saving the file!", "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	
	/**
	 * Loads a byte array.
	 * @param extension the extension of the filter to filter for.
	 * @return the loaded byte array.
	 */
	public static byte[] loadByteArray(String extension) {
		
		// file chooser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(extension, extension.substring(1)));
		
		// show file chooser
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			// get the filename of the selected file to save
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			
			// try reading bytes
			try {
				byte[] data = Files.readAllBytes(Paths.get(filename));
				return data;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was an error loading the file!", "LOADING ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return null;
	}
	
	/**
	 * Shows statistics.
	 * @param type Compression or Decompression
	 * @param imageTime the time it took to compress/decompress the image.
	 * @param numBytes the number of bytes in compression (0 if decompression)
	 */
	public static void showStatistics(String type, long time, int numBytes) {
		
		String message = "Execution Time: " + time + "ms";
		if (numBytes != 0) {
			message += "\nCompression Ratio: " + (512.0 * 512.0) / numBytes;
		}
		
		JOptionPane.showMessageDialog(null, message, type + " STATISTICS", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Saves huffman code and image to csv.
	 * @param codings the codings
	 * @param image the image
	 * @param extension the file extension
	 */
	public static void saveHuffmanCSV(Map<Integer, String> codings, int[][] image, String extension) {
		
		// file chooser
		JFileChooser fileChooser = new JFileChooser();
		
		// show file chooser
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			// get the filename of the selected file to save
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			if (!filename.endsWith(extension)) {
				filename += extension;
			}
			
			try {
				
				// file writer
				FileWriter writer = new FileWriter(filename);
				
				// write number of codings
				writer.write(""+codings.size());
				
				// write the codings
				for (Integer key : codings.keySet()) {
					writer.write("\n" + codings.get(key) + "," + key);
				}
				
				// write the image
				for (int y = 0; y < image.length; y ++) {
					writer.write("\n");
					for (int x = 0; x < image[0].length; x ++) {
						String gv = codings.get(image[y][x]);
						if (x == 0) {
							writer.write(gv);
						} else {
							writer.write("," + gv);
						}
					}
				}
				
				// close the writer
				writer.close();
				
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was an error saving the file!", "SAVING ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	
	/**
	 * Loads a scanner for huffman csv.
	 * @param extension the extension of the filter to filter for.
	 * @return the huffman csv scanner.
	 */
	public static Scanner loadHuffmanCSV(String extension) {
		
		// file chooser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(extension, extension.substring(1)));
		
		// show file chooser
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			try {
				// return scanner
				return new Scanner(fileChooser.getSelectedFile());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "There was an error loading the file!", "LOADING ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return null;
	}
	
	private static final String DEFAULT_IMAGE_NAME = "lena.jpg";
	
}
