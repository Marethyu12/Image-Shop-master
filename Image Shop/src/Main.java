/**
 * TODO:
 * -Add JTabbedPane for tool menu and JScrollPane for image panel
 * -Improve few algorithms (take account of selected region)
 * -Improve zoom feature
 * -Possibly add painting feature
 * -Improve UI
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;

import java.io.IOException;

import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import javax.swing.border.TitledBorder;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public final static int WIDTH = 800;
	public final static int HEIGHT = 700;
	
	public static String IMG_FILE_NAME = " -No Image- ";
	
	private Bitmap bmp;
	private ImagePanel imagePanel;
	
	private Stack<BufferedImage> saved;
	
	private Font buttonFont;
	private Dimension buttonDim;
	private ActionEventHandler handler;
	
	private JPanel picture;
	
	private JMenuBar menuBar;
	
	private JMenu file;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem exit;
	
	private JMenu miscellaneous;
	private JMenuItem shuffle;
	private JMenuItem mirrorDiagonal;
	private JMenuItem mirrorHBox;
	
	private JMenu help;
	private JMenuItem about;
	
	private JButton grayScale;
	private JButton invert;
	private JButton contrast;
	private JButton blur;
	private JButton sharpen;
	private JButton crop;
	private JButton xReflect;
	private JButton yReflect;
	private JButton rotateLeft;
	private JButton rotateRight;
	private JButton zoomIn;
	private JButton undo;
	
	private Main() {
		super("Image Shop");
		
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setIconImage(getIcon("image.png").getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		bmp = new Bitmap(WIDTH, HEIGHT);
		imagePanel = new ImagePanel(bmp, WIDTH - 300, HEIGHT - 100);
		
		saved = new Stack<>();
		
		buttonFont = new Font("arial", Font.PLAIN, 10);
		buttonDim = new Dimension(140, 40);
		handler = new ActionEventHandler();
		
		initializeComponents();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	private void initializeComponents() {
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		
		open = new JMenuItem("Open");
		open.setIcon(getIcon("if_folder_yellow_explorer_52699.png"));
		open.addActionListener(handler);
		
		save = new JMenuItem("Save");
		save.setIcon(getIcon("floppy-disk.png"));
		save.addActionListener(handler);
		
		exit = new JMenuItem("Exit");
		exit.setIcon(getIcon("exit.png"));
		exit.addActionListener(handler);
		
		file.add(open);
		file.add(save);
		file.add(exit);
		
		miscellaneous = new JMenu("Miscellaneous");
		
		shuffle = new JMenuItem("Shuffle");
		shuffle.setIcon(getIcon("shuffle.png"));
		shuffle.addActionListener(handler);
		
		mirrorDiagonal = new JMenuItem("Mirror Diagonally");
		mirrorDiagonal.addActionListener(handler);
		
		mirrorHBox = new JMenuItem("Mirror HBox");
		mirrorHBox.addActionListener(handler);
		
		miscellaneous.add(shuffle);
		miscellaneous.add(mirrorDiagonal);
		miscellaneous.add(mirrorHBox);
		
		help = new JMenu("Help");
		
		about = new JMenuItem("About");
		about.addActionListener(handler);
		
		help.add(about);
		
		menuBar.add(file);
		menuBar.add(miscellaneous);
		menuBar.add(help);
		
		grayScale = new JButton("Grayscale");
		grayScale.setFont(buttonFont);
		grayScale.setPreferredSize(buttonDim);
		grayScale.setIcon(getIcon("gray-scale-icon-md.png"));
		grayScale.addActionListener(handler);
		
		invert = new JButton("Invert");
		invert.setFont(buttonFont);
		invert.setPreferredSize(buttonDim);
		invert.setIcon(getIcon("invert-colors-button.png"));
		invert.addActionListener(handler);
		
		contrast = new JButton("Contrast");
		contrast.setFont(buttonFont);
		contrast.setPreferredSize(buttonDim);
		contrast.setIcon(getIcon("adjust-contrast.png"));
		contrast.addActionListener(handler);
		
		blur = new JButton("Blur");
		blur.setFont(buttonFont);
		blur.setPreferredSize(buttonDim);
		blur.setIcon(getIcon("blur.png"));
		blur.addActionListener(handler);
		
		sharpen = new JButton("Sharpen");
		sharpen.setFont(buttonFont);
		sharpen.setPreferredSize(buttonDim);
		sharpen.setIcon(getIcon("sharpen.png"));
		sharpen.addActionListener(handler);
		
		crop = new JButton("Crop");
		crop.setFont(buttonFont);
		crop.setPreferredSize(buttonDim);
		crop.setIcon(getIcon("cut.png"));
		crop.addActionListener(handler);
		
		xReflect = new JButton("X-Reflection");
		xReflect.setFont(buttonFont);
		xReflect.setPreferredSize(buttonDim);
		xReflect.setIcon(getIcon("icons8-flip-horizontal-100.png"));
		xReflect.addActionListener(handler);
		
		yReflect = new JButton("Y-Reflection");
		yReflect.setFont(buttonFont);
		yReflect.setPreferredSize(buttonDim);
		yReflect.setIcon(getIcon("icons8-voltear-vertical-100.png"));
		yReflect.addActionListener(handler);
		
		rotateLeft = new JButton("Rotate Left");
		rotateLeft.setFont(buttonFont);
		rotateLeft.setPreferredSize(buttonDim);
		rotateLeft.setIcon(getIcon("rotate-left.png"));
		rotateLeft.addActionListener(handler);
		
		rotateRight = new JButton("Rotate Right");
		rotateRight.setFont(buttonFont);
		rotateRight.setPreferredSize(buttonDim);
		rotateRight.setIcon(getIcon("rotating-arrow-to-the-right.png"));
		rotateRight.addActionListener(handler);
		
		zoomIn = new JButton("Zoom In");
		zoomIn.setFont(buttonFont);
		zoomIn.setPreferredSize(buttonDim);
		zoomIn.setIcon(getIcon("zoom-in.png"));
		zoomIn.addActionListener(handler);
		
		undo = new JButton("Undo");
		undo.setFont(buttonFont);
		undo.setPreferredSize(buttonDim);
		undo.setIcon(getIcon("undo.png"));
		undo.addActionListener(handler);
		
		setLayout(new BorderLayout());
		
		picture = new JPanel(new CardLayout());
		picture.setBorder(new TitledBorder(new StringBuilder("Image: ").append(IMG_FILE_NAME).toString()));
		picture.add(imagePanel);
		
		JPanel buttons = new JPanel(new GridLayout(0, 1, 10, 10));
		
		buttons.add(grayScale);
		buttons.add(invert);
		buttons.add(contrast);
		buttons.add(blur);
		buttons.add(sharpen);
		buttons.add(crop);
		buttons.add(xReflect);
		buttons.add(yReflect);
		buttons.add(rotateLeft);
		buttons.add(rotateRight);
		buttons.add(zoomIn);
		buttons.add(undo);
		buttons.setBorder(new TitledBorder("Tools"));
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		
		buttonPanel.add(buttons);
		
		add(menuBar, BorderLayout.NORTH);
        add(picture);
		add(buttonPanel, BorderLayout.LINE_START);
	}
	
	private ImageIcon getIcon(String iconFile) {
		ImageIcon icon = new ImageIcon(Main.class.getResource(new StringBuilder("/Icons/").append(iconFile).toString()));
		
		Image img = icon.getImage();
		Image scaled = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		
		return new ImageIcon(scaled);
	}
	
	private class ActionEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == open && !imagePanel.isMouseEnabled()) {
				BufferedImage bi = null;
				
				try {
					bi = ImageFileSystem.loadImage();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(getParent(), "Unable to open file, sorry!", "I/O Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				bmp.copyImage(bi);
				picture.setBorder(new TitledBorder(new StringBuilder("Image: ").append(IMG_FILE_NAME).toString()));
				imagePanel.setImage(bmp);
				saved.clear();
				saved.push(bmp.getContent());
			}
			
			if (e.getSource() == save && !imagePanel.isMouseEnabled() && !IMG_FILE_NAME.equals(" -No Image- ")) {
				try {
					ImageFileSystem.saveImage(bmp);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(getParent(), "Unable to save file, sorry!", "I/O Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			if (e.getSource() == exit) {
				System.exit(0);
			}
			
			if (e.getSource() == shuffle && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.random_shuffle(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == mirrorDiagonal && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.mirrorDiagonal(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == mirrorHBox && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				
				imagePanel.enableMouse(new Function() {
					public void modify(Bitmap bmp, int x1, int y1, int x2, int y2) {
						ImageAlgorithms.mirrorHorizontallyInBounds(bmp, x1, y1, x2, y2);
					}
				});
			}
			
			if (e.getSource() == about) {
				JOptionPane.showMessageDialog(getParent(), "This application is coded by Jimmy Y. during his free time.\n11/20/18", "About Image Shop", JOptionPane.INFORMATION_MESSAGE);
			}
			
			if (e.getSource() == grayScale && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.grayScale(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == invert && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.invert(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == contrast && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.contrast(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == blur && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.blur(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == sharpen && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.sharpen(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == crop && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				
				imagePanel.enableMouse(new Function() {
					public void modify(Bitmap bmp, int x1, int y1, int x2, int y2) {
						ImageAlgorithms.crop(bmp, x1, y1, x2, y2);
					}
				});
			}
			
			if (e.getSource() == xReflect && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.xReflect(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == yReflect && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.yReflect(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == rotateLeft && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.rotateLeft(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == rotateRight && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.rotateRight(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == zoomIn && !saved.isEmpty() && !imagePanel.isMouseEnabled()) {
				saved.push(bmp.getContent());
				ImageAlgorithms.zoomIn(bmp);
				imagePanel.setImage(bmp);
			}
			
			if (e.getSource() == undo && saved.size() > 1 && !imagePanel.isMouseEnabled()) {
				bmp.copyImage(saved.pop());
				imagePanel.setImage(bmp);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(() -> {
			Main main = new Main();
			main.setVisible(true);
		});
	}
}
