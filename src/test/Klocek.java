package test;

import java.awt.Color;
import java.awt.Graphics;



public class Klocek
{
	public static int w = 20;
	public  static int l = 20;
	public  static int marg = 2;
	
	private int x;
	private int y;
	private Color color;
	
	Klocek( int x, int y, Color color){
		// PROSTOKAT
		this.x = x;
		this.y = y;
		this.color = color;
	}
	void draw( Graphics offscrgr ){
		//g2d.draw( molecule );
		//g2d.setColor(Color.red);
		//g2d.drawRect(x,y,w,l);
		//g2d.setColor(Color.black);
		//g2d.fillRect(x,y,w,l);
		//g2d.setColor(Color.);
		
		offscrgr.setColor(color);
        offscrgr.fillRect(x + marg/2, y + marg/2,l-marg,w - marg);

        offscrgr.setColor(color.brighter());
        offscrgr.drawLine(x, y + w - marg/2, x, y );
        offscrgr.drawLine(x, y, x +l-marg/2 , y );

        offscrgr.setColor(color.darker());
        offscrgr.drawLine(x + marg/2, y +w - marg/2, x +l - marg/2, y + w -marg/2);
        offscrgr.drawLine(x + l -marg/2, y + w - marg/2, x +l- marg/2, y + marg/2);

	}
	void move( int dx, int dy)
	{
		x+=dx;
		y+=dy;
	}
	int getX(){ return x; }
	int getY(){ return y; }
	void setX(int x) { this.x = x;} 
	void setY(int y) { this.y = y;}
	Color getColor(){ return color; }
	void setColor(Color color){ this.color = color; }
	public boolean equals( Klocek inny ){
		if(this == inny)
			return true;
		if( this.x == inny.getX() && this.y == inny.getY() )
			return true;
		return false;
	}


}