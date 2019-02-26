import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JFileChooser;

import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageFileSystem {
	private static JFileChooser fileChooser;
	
	private ImageFileSystem() {
		
	}
	
	public static void saveImage(Bitmap bmp) throws IOException {
		JFileChooser save = getFileChooser();
		
		int resp = save.showSaveDialog(null);
		
		if (resp == JFileChooser.APPROVE_OPTION) {
			try {
				File selected = save.getSelectedFile();
				selected = new File(selected.getAbsolutePath() + ".jpg");
				ImageIO.write(bmp.getContent(), "jpg", selected);
			} catch (IOException ex) {
				throw new IOException();
			}
		}
	}
	
	public static BufferedImage loadImage() throws IOException {
		JFileChooser open = getFileChooser();
		BufferedImage loaded = null;
		
		int resp = open.showOpenDialog(null);
		
		if (resp == JFileChooser.APPROVE_OPTION) {
			try {
				File file = open.getSelectedFile();
				
				loaded = ImageIO.read(file);
				Main.IMG_FILE_NAME = file.getName();
			} catch (IOException ex) {
				throw new IOException();
			}
		}
		
		return loaded;
	}
	
	private static JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "bmp", "png"));
		}
		
		return fileChooser;
	}
}

