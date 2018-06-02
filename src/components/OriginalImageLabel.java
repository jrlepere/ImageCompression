package components;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * The label for the original image
 * @author JLepere2
 * @date 06/01/2018
 */
public class OriginalImageLabel extends JLabel {

	/**
	 * Construct the label for storing and displaying the original image.
	 * @param originalImage the image to display
	 */
	public OriginalImageLabel(int[][] originalImage) {
		super();
		
		// set centered
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
		
		// create and set the buffered image to display
		BufferedImage buffImage = new BufferedImage(originalImage.length, originalImage[0].length, BufferedImage.TYPE_BYTE_GRAY);
		for (int y = 0; y < originalImage.length; y ++) {
			for (int x = 0; x < originalImage[0].length; x ++) {
				int p = originalImage[y][x];
				buffImage.setRGB(x, y, (p<<16) + (p<<8) + p);
			}
		}
		
		// show the buffered image
		this.setIcon(new ImageIcon(buffImage));
		
	}	
	
	private static final long serialVersionUID = 8531L;
	
}
