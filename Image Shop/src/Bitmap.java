import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;

public class Bitmap {
	private BufferedImage bi;
	private int intialPixel;
	
	public Bitmap(BufferedImage image) {
		this(image.getWidth(null), image.getHeight(null));
		
		copyImage(image);
	}
	
	public Bitmap(int width, int height) {
		bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		intialPixel = bi.getRGB(0, 0);
	}
	
	public void setSize(int width, int height) {
        Image img = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        
        bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = bi.createGraphics();
        
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
    }
	
	public int getPixelRGB(int x, int y) {
        ColorUtils.ensureInRange(x, 0, bi.getWidth() - 1);
        ColorUtils.ensureInRange(y, 0, bi.getHeight() - 1);
        
    	int rgb = bi.getRGB(x, y);
        if (ColorUtils.getAlpha(rgb) == 0)
            return ColorUtils.toRgbInteger(255, 255, 255, 255);
        else
            return rgb;
    }
	
	public int[][] getPixelsRGB() {
        int[][] pixels = new int[bi.getHeight()][bi.getWidth()];
        
        int backgroundRGB = ColorUtils.toRgbInteger(255, 255, 255, 255);
        
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                int px = bi.getRGB(col, row);
                
                if (ColorUtils.getAlpha(px) == 0)
                    pixels[row][col] = backgroundRGB;
                else
                    pixels[row][col] = px;
            }
        }
        
        return pixels;
    }
	
	public void copyImage(BufferedImage image) {
		if (image != null) {
			int width = image.getWidth();
			int height = image.getHeight();
			
			setSize(width, height);
			clear();
			
			Graphics2D g2d = bi.createGraphics();
			
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();
		}
	}
	
	public BufferedImage getContent() {
		return bi;
	}
	
	public void clear() {
        int[] pixels = new int[bi.getWidth() * bi.getHeight()];
        
        for (int i = 0; i < pixels.length; ++i)
            pixels[i] = intialPixel;
        
        bi.setRGB(0, 0, bi.getWidth(), bi.getHeight(), pixels, 0, 1);
    }
	
	public static BufferedImage castImage(Image toolkitImage) {
		BufferedImage bi = new BufferedImage(toolkitImage.getWidth(null), toolkitImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		bi.createGraphics().drawImage(toolkitImage, 0, 0, null);
		
		return bi;
	}
	
	public static void makeImage(int[][] pixels, Bitmap bmp) {
		int height = pixels.length;
		int width = pixels[0].length;
		
		int[] pixels1D = new int[width * height];
		
		for (int i = 0; i < height; ++i)
			System.arraycopy(pixels[i], 0, pixels1D, i * width, width);
		
		Image img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pixels1D, 0, width));
		
		bmp.copyImage(castImage(img));
	}
}
