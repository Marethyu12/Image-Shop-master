import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1;
	
	private static final boolean DOUBLE_BUFFERED = true;
	
	private Bitmap bmp;
	private int leftX;
	private int topY;
	private int width;
	private int height;
	
	private int mouseX;
	private int mouseY;
	
	private JLabel statusLabel;
	private MouseHandler eventHandler;
	
	private boolean mouseLock;
	private Region region;
	private Function function;
	
	public ImagePanel(Bitmap bmp, int width, int height) {
		super(DOUBLE_BUFFERED);
		
		this.bmp = bmp;
		this.width = width;
		this.height = height;
		
		mouseX = 0;
		mouseY = 0;
		
		statusLabel = new JLabel("Current X: - pixels Current Y: - pixels, Red: - Green: - Blue: -");
		eventHandler = new MouseHandler();
		
		mouseLock = true;
		region = null;
		
		setImage(bmp);
		
		setBackground(Color.WHITE);
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        add(statusLabel, BorderLayout.SOUTH);
        addMouseListener(eventHandler);
        addMouseMotionListener(eventHandler);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (bmp != null) {
        	Image image = bmp.getContent();
        	
        	this.leftX = (getWidth() - image.getWidth(null)) / 2;
        	this.topY = (getHeight() - image.getHeight(null)) / 2;
        	
            g.drawImage(image, leftX, topY, this);
            
            if (region != null)
            	region.sketch(g);
        }
    }
	
	public void setImage(Bitmap bmp) {
		BufferedImage image = bmp.getContent();
		
		if (image == null) return;
		
		Dimension scaled = getScaledDimension(new Dimension(image.getWidth(null), image.getHeight(null)), new Dimension(width, height - 20));
		Image scaledImage = image.getScaledInstance(scaled.width, scaled.height, Image.SCALE_DEFAULT);
		
		bmp.copyImage(Bitmap.castImage(scaledImage));
		
		repaint();
	}
	
	public void enableMouse(Function function) {
		this.function = function;
		mouseLock = false;
	}
	
	public boolean isMouseEnabled() {
		return !mouseLock;
	}
	
	private Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;
	    
	    if (original_width > bound_width) {
	        new_width = bound_width;
	        new_height = (new_width * original_height) / original_width;
	    }
	    
	    if (new_height > bound_height) {
	        new_height = bound_height;
	        new_width = (new_height * original_width) / original_height;
	    }
	    
	    return new Dimension(new_width, new_height);
	}
	
	private void updateStatus() {
		StringBuilder sb = new StringBuilder();
		
		if ((mouseX >= leftX && mouseY >= topY) &&
			(mouseX < leftX + bmp.getContent().getWidth() && mouseY < topY + bmp.getContent().getHeight())) {
			sb.append("Current X: ").append(mouseX - leftX).append(" pixels Current Y: ").append(mouseY - topY).append(" pixels, ");
			
			int rgb = bmp.getPixelRGB(mouseX - leftX, mouseY - topY);
			
			sb.append(" Red: ").append(ColorUtils.getRed(rgb));
			sb.append(" Green: ").append(ColorUtils.getGreen(rgb));
			sb.append(" Blue: ").append(ColorUtils.getBlue(rgb));
		} else {
			sb.append("Current X: -").append(" pixels Current Y: -").append(" pixels, ");
			
			sb.append(" Red: -");
			sb.append(" Green: -");
			sb.append(" Blue: -");
		}
		
		statusLabel.setText(sb.toString());
	}
	
	private boolean inBounds(Region region) {
		return (region.getX1() >= leftX && region.getY1() >= topY) &&
			   (region.getX1() < leftX + bmp.getContent().getWidth() && region.getY1() < topY + bmp.getContent().getHeight()) &&
			   (region.getX2() >= leftX && region.getY2() >= topY) &&
			   (region.getX2() < leftX + bmp.getContent().getWidth() && region.getY2() < topY + bmp.getContent().getHeight());
	}
	
	private class MouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (!mouseLock) {
				region = new Region(e.getX(), e.getY(), e.getX(), e.getY(), leftX, topY);
				
				repaint();
			}
		}
		
		public void mouseReleased(MouseEvent e) {
			if (region != null && !mouseLock) {
				mouseX = e.getX();
				mouseY = e.getY();
				
				region.setX2(mouseX);
				region.setY2(mouseY);
				
				updateStatus();
				
				if (inBounds(region)) {
					function.modify(bmp, region.getX1() - leftX, region.getY1() - topY, region.getX2() - leftX, region.getY2() - topY);
					setImage(bmp);
					
					mouseLock = true;
				} else
					JOptionPane.showMessageDialog(getParent(), "The selected region is out of bounds!\nPlease try again!", "Unable to crop the image!", JOptionPane.ERROR_MESSAGE);
				
				region = null;
				repaint();
			}
		}
		
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			
			if (!Main.IMG_FILE_NAME.equals(" -No Image- ")) updateStatus();
		}
		
		public void mouseDragged(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			
			if (region != null && !mouseLock) {
				region.setX2(mouseX);
				region.setY2(mouseY);
				
				repaint();
			}
		}
	}
}
