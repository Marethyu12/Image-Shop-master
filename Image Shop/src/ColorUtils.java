
public class ColorUtils {
    public static final int ALPHA_MASK = 0xff000000;
    public static final int BLUE_MASK = 0x000000ff;
    public static final int GREEN_MASK = 0x0000ff00;
    public static final int RED_MASK = 0x00ff0000;
    
	private ColorUtils() {
		
	}
	
	public static int toRgbInteger(int alpha, int r, int g, int b) {
        ensureInRange(alpha, 0, 255);
        ensureInRange(r, 0, 255);
        ensureInRange(g, 0, 255);
        ensureInRange(b, 0, 255);
        
    	return ((alpha & 0x000000ff) << 24)
                | ((r & 0x000000ff) << 16)
                | ((g & 0x000000ff) << 8)
                | ((b & 0x000000ff));
    }
	
	public static int getAlpha(int rgb) {
        return (rgb & ALPHA_MASK) >> 24;
    }
	
	public static int getRed(int rgb) {
        return (rgb & RED_MASK) >> 16;
    }
	
	public static int getGreen(int rgb) {
        return (rgb & GREEN_MASK) >> 8;
    }
	
	public static int getBlue(int rgb) {
        return (rgb & BLUE_MASK);
    }
	
	public static void ensureInRange(int value, int min, int max) {
        assert(value < min || value > max);
    }
	
	public static int cont(int color) {
		double gain = 1.2;
		int newColor = 0;
		
		if (color < 128)
			newColor = (int) (color / gain);
		else {
			color = (int) (color * gain);
			
			newColor = (color > 255) ? 255 : color;
		}
		
		return newColor;
	}
	
	public static int clamp(int color) {
		return (color > 255 ? 255 : (color < 0 ? 0 : color));
	}
}
