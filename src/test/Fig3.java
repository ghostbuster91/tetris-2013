package test;

import java.awt.Color;
import java.util.Vector;

//KLOCEK PROSTY 4x1
public class Fig3 extends Figura {
	//private int[][] wsp =  { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } };
	public Fig3(int x, int y, Color color) {
		super(x, y, color);
		// TODO Auto-generated constructor stub
		  // dla danego ksztaltu stale
		 wsp = new int[][]  { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } };
		 
		 //helpVar1 = 1;
		 //helpVar2 = 1;
		 x_ = x;
		 y_ = y;
		 klocki = new Vector<Klocek>();
		 //nowe
		 klocki.add(new Klocek(x+ wsp[0][0]*Klocek.l, y+wsp[0][1]*Klocek.w, color));
		 klocki.add(new Klocek(x+ wsp[1][0]*Klocek.l, y+wsp[1][1]*Klocek.w, color));
		 klocki.add(new Klocek(x+ wsp[2][0]*Klocek.l, y+wsp[2][1]*Klocek.w, color));
		 klocki.add(new Klocek(x+ wsp[3][0]*Klocek.l, y-wsp[3][1]*Klocek.w, color));

	}
	
}
