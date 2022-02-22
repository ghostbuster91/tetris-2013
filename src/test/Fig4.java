package test;

import java.awt.Color;
import java.util.Vector;

//KLOCEK L
public class Fig4 extends Figura {

	public Fig4(int x, int y, Color color) {
		super(x, y, color);
		// TODO Auto-generated constructor stub
		  // dla danego ksztaltu stale
		 //wsp = new int[][]  { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } };
		helpVar1 = -1;
		//helpVar2 = 1;
		wsp = new int[][]  { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } };
		 x_ = x;
		 y_ = y;
		 klocki = new Vector<Klocek>();
		 //nowe
		 klocki.add(new Klocek(x+ wsp[0][0]*Klocek.l, y-wsp[0][1]*Klocek.w, color));
		 klocki.add(new Klocek(x+ wsp[1][0]*Klocek.l, y+wsp[1][1]*Klocek.w, color));
		 klocki.add(new Klocek(x+ wsp[2][0]*Klocek.l, y+wsp[2][1]*Klocek.w, color));
		 klocki.add(new Klocek(x+ wsp[3][0]*Klocek.l, y+wsp[3][1]*Klocek.w, color));

	}
	
}
