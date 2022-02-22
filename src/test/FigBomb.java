package test;

import java.awt.Color;
import java.util.Vector;

public class FigBomb extends Figura {
	private Color color1_ = Color.RED;
	private Color color2_ = Color.ORANGE;
	
	public FigBomb(int x, int y, Color color) {
		super(x, y, color);
		// TODO Auto-generated constructor stub
		  // dla danego ksztaltu stale
		
		wsp = new int[][] { { 0, 0 },  { 0, 0 },   { 0, 0 },   { 0, 0 } };
		
		 x_ = x;
		 y_ = y;
		 klocki = new Vector<Klocek>();

		 klocki.add(new Klocek(x+ wsp[0][0]*Klocek.l, y+wsp[0][1]*Klocek.w, color));


	}
	public void rote( Vector<Figura> dfig ){
	}
	public void move(int dx, int  dy ){
		x_+= dx;
		y_+= dy;
		
		Color color_tmp;
		
		if( klocki.firstElement().getColor().equals(color1_) )
			color_tmp = color2_;
		else
			color_tmp = color1_;
		for(int i=0;i<klocki.size(); i++){
			klocki.elementAt(i).move(dx,dy);
			klocki.elementAt(i).setColor( color_tmp );
		}
	}
	protected boolean boom(){
		return true;
	}
}
