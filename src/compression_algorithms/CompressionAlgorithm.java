package compression_algorithms;

/**
 * Interface for compression algorithms.
 * @author JLepere2
 * @date 06/01/2018
 */
public interface CompressionAlgorithm {

	/**
	 * Compress and save the image.
	 * @param image the image to compress.
	 */
	public void compress(int[][] image);
	
	/**
	 * Decompress the image.
	 * @return the decompressed image.
	 */
	public int[][] decompress();
	
}
