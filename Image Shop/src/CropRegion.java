import java.awt.Color;
import java.awt.Graphics;

public class Region {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int leftX;
	private int topY;
	
	public Region(int x1, int y1, int x2, int y2, int leftX, int topY) {
	    this.x1 = x1;
	    this.y1 = y1;
	    this.x2 = x2;
	    this.y2 = y2;
	    this.leftX = leftX;
	    this.topY = topY;
	}
	
	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	public void setY1(int y1) {
		this.y1 = y1;
	}
	
	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	public int getX1() {
		return x1;
	}
	
	public int getY1() {
		return y1;
	}
	
	public int getX2() {
		return x2;
	}
	
	public int getY2() {
		return y2;
	}
	
	public void setY2(int y2) {
		this.y2 = y2;
	}
	
	public int getUpperLeftX() {
	    return Math.min(x1, x2);
	}
	
	public int getUpperLeftY() {
	    return Math.min(y1, y2);
	}
	
	public int getWidth() {
	    return Math.abs(x1 - x2);
	}
	
	public int getHeight() {
	    return Math.abs(y1 - y2);
	}
	
	public void sketch(Graphics g) {
		g.setColor(Color.BLACK);
		
		g.drawString(String.format("(%d, %d)", x1 - leftX, y1 - topY), x1 - 4, y1 - 10);
		g.fillOval(x1 - 5, y1 - 5, 10, 10);
		
		g.drawRect(getUpperLeftX(), getUpperLeftY(), getWidth(), getHeight());
		
		g.drawString(String.format("(%d, %d)", x2 - leftX, y2 - topY), x2 + 6, y2 + 6);
		g.fillOval(x2 - 5, y2 - 5, 10, 10);
		
		g.dispose();
	}
}
