import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import components.DecompressedImageLabel;
import components.OriginalImageLabel;
import compression_algorithms.CompressionAlgorithm;
import compression_algorithms.HuffmanCoding;
import compression_algorithms.RunLengthCoding;
import compression_algorithms.RunLengthCodingBitPlane;
import io.Utilities;

/**
 * Main class for Image Compression project.
 * @author JLepere2
 * @date 06/01/2018
 */
public class ImageCompression {

	/**
	 * Main method for the project.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		
		// initialize the main frame
		JFrame mainFrame = new JFrame(FRAME_TITLE);
		mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setLayout(new BorderLayout());
		
		// get default image matrix
		final int[][] image;
		try {
			image = Utilities.getDefaultImage();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was an error loading the default image!", "LOADING IMAGE ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// compression/decompression algorithms
		CompressionAlgorithm[] compressionAlgorithms = new CompressionAlgorithm[]{
			new RunLengthCoding(),
			new RunLengthCodingBitPlane(),
			new HuffmanCoding()
		};
		
		// ---- IMAGE PANEL ---- //
		JPanel imagePanel = new JPanel(new GridLayout(1, 2));
		
		// label for the original image
		imagePanel.add(new OriginalImageLabel(image));
		DecompressedImageLabel decompressedImageLabel = new DecompressedImageLabel();
		imagePanel.add(decompressedImageLabel);
		
		// add to the main frame
		mainFrame.add(imagePanel, BorderLayout.CENTER);
		
		
		// ---- SELECTION PANEL ---- //
		JPanel selectionPanel = new JPanel(new GridLayout(1, 2));
		
		// compression panel
		JPanel compressionPanel = new JPanel(new GridLayout(compressionAlgorithms.length, 1));
		for (CompressionAlgorithm algo : compressionAlgorithms) {
			// button for each compression algo
			JButton algoButton = new JButton(algo.toString());
			algoButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					algo.compress(image);
				}
			});
			compressionPanel.add(algoButton);
		}
		selectionPanel.add(compressionPanel);
		
		// decompression panel
		JPanel decompressionPanel = new JPanel(new GridLayout(compressionAlgorithms.length, 1));
		for (CompressionAlgorithm algo : compressionAlgorithms) {
			// button for each decompression algo
			JButton algoButton = new JButton(algo.toString());
			algoButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					decompressedImageLabel.showImage(algo.decompress());
				}
			});
			decompressionPanel.add(algoButton);
		}
		selectionPanel.add(decompressionPanel);
		
		// add to the main frame
		mainFrame.add(selectionPanel, BorderLayout.SOUTH);
		
		
		// set the frame visible
		mainFrame.setVisible(true);
		
	}
	
	private static final String FRAME_TITLE = "Image Compression";
	private static final int FRAME_WIDTH = 1050;
	private static final int FRAME_HEIGHT = 600;
	
}
