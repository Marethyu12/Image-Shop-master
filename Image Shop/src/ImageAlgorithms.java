import java.util.Random;

public class ImageAlgorithms {
	private ImageAlgorithms() {
		
	}
	
	public static void rotateLeft(Bitmap bmp) {
        assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		int[][] newPixels = new int[cols][rows];
		
		for (int i = 0; i < cols; ++i)
			for (int j = 0; j < rows; ++j)
				newPixels[i][j] = pixels[j][cols - 1 - i];
		
		Bitmap.makeImage(newPixels, bmp);
	}
	
	public static void rotateRight(Bitmap bmp) {
        assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		int[][] newPixels = new int[cols][rows];
		
		for (int i = 0; i < cols; ++i)
			for (int j = 0; j < rows; ++j)
				newPixels[i][j] = pixels[rows - 1- j][i];
		
		Bitmap.makeImage(newPixels, bmp);
	}
	
	public static void xReflect(Bitmap bmp) {
		assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		for (int j = 0; j < cols; ++j) {
			int low = 0;
			int high = rows - 1;
			
			while (low < high) {
				int temp = pixels[low][j];
				
				pixels[low][j] = pixels[high][j];
				pixels[high][j] = temp;
				
				low++;
				high--;
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void yReflect(Bitmap bmp) {
        assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		for (int i = 0; i < rows; ++i) {
			int low = 0;
			int high = cols - 1;
			
			while (low < high) {
				int temp = pixels[i][low];
				
				pixels[i][low] = pixels[i][high];
				pixels[i][high] = temp;
				
				low++;
				high--;
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void mirrorHorizontallyInBounds(Bitmap bmp, int x1, int y1, int x2, int y2) {
		assert(bmp != null); 
		
		if (x2 - x1 < 0 && y2 - y1 > 0) {
        	x1 ^= x2;
        	x2 ^= x1;
        	x1 ^= x2;
        } else if (x2 - x1 < 0 && y2 - y1 < 0) {
        	x1 ^= x2;
        	x2 ^= x1;
        	x1 ^= x2;
        	
        	y1 ^= y2;
        	y2 ^= y1;
        	y1 ^= y2;
        } else if (x2 - x1 > 0 && y2 - y1 < 0) {
        	y1 ^= y2;
        	y2 ^= y1;
        	y1 ^= y2;
        }
		
		int[][] pixels = bmp.getPixelsRGB();
		
		for (int i = y1; i < y2; ++i) {
			int low = x1;
			int high = x2;
			
			while (low < high) {
				pixels[i][high] = pixels[i][low];
				
				low++;
				high--;
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void mirrorDiagonal(Bitmap bmp) {
		assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		int small = Math.min(rows, cols);
		
		for (int i = 1, j = 0; i < small; ++i, ++j) {
			for (int k = i; k < small; ++k) {
				pixels[j][k] = pixels[k][j];
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void grayScale(Bitmap bmp) {
        assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				int rgb = pixels[i][j];
				
				int r = ColorUtils.getRed(rgb);
				int g = ColorUtils.getGreen(rgb);
				int b = ColorUtils.getBlue(rgb);
				
				int k = (r + g + b) / 3;
				
				pixels[i][j] = ColorUtils.toRgbInteger(ColorUtils.getAlpha(rgb), k, k, k);
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void invert(Bitmap bmp) {
        assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				int rgb = pixels[i][j];
				
				int r = ColorUtils.RED_MASK - ColorUtils.getRed(rgb);
				int g = ColorUtils.GREEN_MASK - ColorUtils.getGreen(rgb);
				int b = ColorUtils.BLUE_MASK - ColorUtils.getBlue(rgb);
				
				pixels[i][j] = ColorUtils.toRgbInteger(ColorUtils.getAlpha(rgb), r, g, b);
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void zoomIn(Bitmap bmp) {
        assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		int[][] newPixels = new int[rows * 2][cols * 2];
		
		int x = 0;
		int y = 0;
		
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				int rgb = pixels[i][j];
				
				newPixels[x][y] = newPixels[x][y + 1] = rgb;
				newPixels[x + 1][y] = newPixels[x + 1][y + 1] = rgb;
				
				++y;
				if (++y == cols * 2) y = 0;
			}
			
			x += 2;
		}
		
		Bitmap.makeImage(newPixels, bmp);
	}
	
	public static void contrast(Bitmap bmp) {
        assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				int rgb = pixels[i][j];
				
				int r = ColorUtils.cont(ColorUtils.getRed(rgb));
				int g = ColorUtils.cont(ColorUtils.getGreen(rgb));
				int b = ColorUtils.cont(ColorUtils.getBlue(rgb));
				
				pixels[i][j] = ColorUtils.toRgbInteger(ColorUtils.getAlpha(rgb), r, g, b);
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void blur(Bitmap bmp) {
		assert(bmp != null);
		
		int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
		 
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				int rgb = pixels[i][j];
				
				int r = ColorUtils.getRed(rgb);
				int g = ColorUtils.getGreen(rgb);
				int b = ColorUtils.getBlue(rgb);
				
				int count = 1;
				
				for (int k = 0; k < 8; ++k) {
					int row = i + dr[k];
					int col = j + dc[k];
					
					if (row >= 0 && row < rows && col >= 0 && col < cols) {
						r += ColorUtils.getRed(pixels[row][col]);
						g += ColorUtils.getGreen(pixels[row][col]);
						b += ColorUtils.getBlue(pixels[row][col]);
						
						count++;
					}
				}
				
				r /= count;
				g /= count;
				b /= count;
				
				pixels[i][j] = ColorUtils.toRgbInteger(ColorUtils.getAlpha(rgb), r, g, b);
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void sharpen(Bitmap bmp) {
        assert(bmp != null);
		
		int[] dr = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
		int[] dc = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
		 
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		int r0 = 0;
		int g0 = 0;
		int b0 = 0;
		
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				int rs = 0;
				int gs = 0;
				int bs = 0;
				
				for (int k = 0; k < 9; ++k) {
					int row = i + dr[k];
					int col = j + dc[k];
					
					if (row >= 0 && row < rows && col >= 0 && col < cols) {
						int rgb = pixels[row][col];
						
						int r = ColorUtils.getRed(rgb);
						int g = ColorUtils.getGreen(rgb);
						int b = ColorUtils.getBlue(rgb);
						
						if (dr[k] == 0 && dc[k] == 0) {
							r0 = r;
							g0 = g;
							b0 = b;
						} else {
							rs += r;
							gs += g;
							bs += b;
						}
					}
				}
				
				rs >>= 3;
				gs >>= 3;
		        bs >>= 3;
			    
			    pixels[i][j] = ColorUtils.toRgbInteger(ColorUtils.getAlpha(pixels[i][j]),
			    		                               ColorUtils.clamp(r0 + r0 - rs),
			    		                               ColorUtils.clamp(g0 + g0 - gs),
			    		                               ColorUtils.clamp(b0 + b0 - bs));
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
	
	public static void crop(Bitmap bmp, int x1, int y1, int x2, int y2) {
        assert(bmp != null);
        
        if (x2 - x1 < 0 && y2 - y1 > 0) {
        	x1 ^= x2;
        	x2 ^= x1;
        	x1 ^= x2;
        } else if (x2 - x1 < 0 && y2 - y1 < 0) {
        	x1 ^= x2;
        	x2 ^= x1;
        	x1 ^= x2;
        	
        	y1 ^= y2;
        	y2 ^= y1;
        	y1 ^= y2;
        } else if (x2 - x1 > 0 && y2 - y1 < 0) {
        	y1 ^= y2;
        	y2 ^= y1;
        	y1 ^= y2;
        }
        
		int[][] pixels = bmp.getPixelsRGB();
		
		int[][] newPixels = new int[y2 - y1 + 1][x2 - x1 + 1];
		
		for (int i = y1, j = 0; i <= y2; ++i, ++j)
			System.arraycopy(pixels[i], x1, newPixels[j], 0, x2 - x1 + 1);
		
		Bitmap.makeImage(newPixels, bmp);
	}
	
	public static void random_shuffle(Bitmap bmp) {
		assert(bmp != null);
		
		int[][] pixels = bmp.getPixelsRGB();
		
		int rows = pixels.length;
		int cols = pixels[0].length;
		
		for (int[] row : pixels) {
			for (int i = row.length - 1; i > 0; --i) {
				int j = new Random().nextInt(i + 1) + 0;
				
				row[i] ^= row[j];
				row[j] ^= row[i];
				row[i] ^= row[j];
			}
		}
		
		for (int i = 0; i < cols; ++i) {
			int low = 0;
			int high = rows - 1;
			
			while (low < high) {
				int x = new Random().nextInt(high - low + 1) + low;
				int y = new Random().nextInt(high - low + 1) + low;
				
				pixels[x][i] ^= pixels[y][i];
				pixels[y][i] ^= pixels[x][i];
				pixels[x][i] ^= pixels[y][i];
				
				low++;
				high--;
			}
		}
		
		Bitmap.makeImage(pixels, bmp);
	}
}
