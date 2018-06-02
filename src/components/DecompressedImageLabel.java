package components;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Decompressed Image Label
 * @author JLepere2
 * @date 06/01/2018
 */
public class DecompressedImageLabel extends JLabel {

	/**
	 * Creates a Decompressed Image Label
	 */
	public DecompressedImageLabel() {
		super();
		
		// set centered
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
		
	}
	
	/**
	 * Displayed the decompressed image
	 * @param image
	 */
	public void showImage(int[][] image) {
		
		// test if null
		if (image == null) return;
		
		// create and set the buffered image to display
		BufferedImage buffImage = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_BYTE_GRAY);
		for (int y = 0; y < image.length; y ++) {
			for (int x = 0; x < image[0].length; x ++) {
				int p = image[y][x];
				buffImage.setRGB(x, y, (p<<16) + (p<<8) + p);
			}
		}
		
		// show the buffered image
		this.setIcon(new ImageIcon(buffImage));
	}
	
	private static final long serialVersionUID = 890871L;
	
}
