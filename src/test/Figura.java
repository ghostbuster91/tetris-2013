package test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

class Figura
{
	protected int x_; // Wspolrzedna x umownego srodka figury
	
	protected int y_; // Wspolrzedna y umownego srodka figury
	
	protected Color color_; // Kolor figury
	
	protected int[][] wsp; 
	
	protected int helpVar1;
	protected int helpVar2;
	
	
	protected Vector<Klocek> klocki; // Vector klockow z ktorych sklada sie figura
	
	public Figura(){}
	public Figura( int x, int y, Color color ){
		helpVar1 = 1;
		helpVar2 = 1;
	}
	public void draw(Graphics offscrgr){
		for(int i=0;i<klocki.size(); i++)
			klocki.elementAt(i).draw(offscrgr);
	}
	// Metoda obraca figure w prawo
	public void rote ( Vector<Figura> dfig ){

		int[][] wsp1 = new int[4][2];
		for(int i=0; i< 4; i++)
			System.arraycopy(wsp[i], 0, wsp1[i], 0, wsp1[i].length);
		 
		for(int i=0; i< wsp.length; i++)// zamiana wspolrzednych x na y a y na -x
		{
			wsp[i][0] = wsp1[i][1] ;   
			wsp[i][1] = wsp1[i][0] * (-1);  
		}
		
		Vector<Klocek> tmpKlocki = new Vector<Klocek>();
		
		for( int i =0; i < klocki.size(); i++)
			tmpKlocki.add(new Klocek(klocki.get(i).getX(),klocki.get(i).getY(),klocki.get(i).getColor()));
		
		// utworzenie tymczasowych klockow z nowymi wspolrzednymi
		tmpKlocki.elementAt(0).setX( x_+ wsp[0][0]*Klocek.l);
		tmpKlocki.elementAt(0).setY(y_+ helpVar1*wsp[0][1]*Klocek.w );
		tmpKlocki.elementAt(1).setX( x_+ wsp[1][0]*Klocek.l );
		tmpKlocki.elementAt(1).setY(  y_+wsp[1][1]*Klocek.w );
		tmpKlocki.elementAt(2).setX( x_+ wsp[2][0]*Klocek.l);
		tmpKlocki.elementAt(2).setY( y_+wsp[2][1]*Klocek.w );
		tmpKlocki.elementAt(3).setX(x_+ wsp[3][0]*Klocek.l );
		tmpKlocki.elementAt(3).setY(  y_-helpVar1*helpVar2*wsp[3][1]*Klocek.w );
		
		if( tryRote(dfig, tmpKlocki) ) // sprawdzenie czy takie obrocenie jest w ogole mozliwe
			klocki = tmpKlocki;
	}
	public void move(int dx, int  dy ){
		x_+= dx;
		y_+= dy;
		for(int i=0;i<klocki.size(); i++)
			klocki.elementAt(i).move(dx,dy);
	}
	// Metoda zwraca prawa( wsp x ) krawedz figury
	public int max_right(){
		int max = 0 ;
		for( int i=0; i < klocki.size(); i++ ){
			if( klocki.elementAt(i).getX() > max )
				max = klocki.elementAt(i).getX();
		}
		return max + Klocek.w;
	}
	// Metoda zwraca prawa krawedz( wsp x ) figury w danej lini
	public int max_right_in_line(int line){
		int max = 0 ;
		for( int i=0; i < klocki.size(); i++ ){
			if(klocki.get(i).getY() != line)
				continue;
			if( klocki.elementAt(i).getX() > max )
				max = klocki.elementAt(i).getX();
		}
		return max + Klocek.w;
	}
	// Metoda zwraca lewa( wsp x ) krawedz figury
	public int max_left(){
		int max = GamePanel.BoardWidth + GamePanel.StartBoardX ;
		for( int i=0; i < klocki.size(); i++ ){
			if( klocki.elementAt(i).getX() < max )
				max = klocki.elementAt(i).getX();
		}
		return max;
	}
	// Metoda zwraca lewa krawedz( wsp x ) figury w danej lini
	public int max_left_in_line(int line){
		int max = GamePanel.BoardWidth + GamePanel.StartBoardX ;
		for( int i=0; i < klocki.size(); i++ ){
			if(klocki.get(i).getY() != line)
				continue;
			if( klocki.elementAt(i).getX() < max )
				max = klocki.elementAt(i).getX();
		}
		return max;
	}
	// Metoda zwraca wspolrzedna Y dolu figury
	public int max_down(){
		int max = 0;
		for( int i=0; i < klocki.size(); i++ ){
			if( klocki.elementAt(i).getY() > max )
				max = klocki.elementAt(i).getY();
		}
		return max + Klocek.w;
	}
	// Metoda zwraca wspolrzedna Y gory figury
	public int max_up(){ // tutaj tak naprawde szukamy minimum bo Y rosnie do dolu
		int max = GamePanel.BoardHeight + GamePanel.StartBoardY;
		for( int i = 0; i < klocki.size(); i++ )
			if( klocki.elementAt(i).getY() < max )
				max = klocki.elementAt(i).getY();
		return max;
	}
	// Metoda zwraca wspolrzedna Y dolu figury w okreslonej lini pionowej
	public int max_down_in_line(int line){
		int max = 0;
		for( int i=0; i < klocki.size(); i++ ){
			if(klocki.get(i).getX() != line)
				continue;
			if( klocki.elementAt(i).getY() > max )
				max = klocki.elementAt(i).getY();
		}
		return max + Klocek.w;
	}
	// Metoda zwraca linie pionowe w ktorych okreslona jest figura
	public Vector<Integer> getLinesX(){
		Vector<Integer> toRet = new Vector<Integer>();
		boolean dodaj = true;
		int tmp;
		
		for( int i = 0; i < klocki.size(); i++ ){
			
			tmp = klocki.get(i).getX();
			for( int j = 0; j < toRet.size(); j++)
				if( toRet.get(j) == tmp)
					dodaj = false;

			if( dodaj )
				toRet.add(tmp);
			dodaj = true;
		}
		return toRet;
	}
	// Metoda zwraca klocki danej figury w okreslonej lini
	public Vector<Klocek> getKlockiInLineX( int line){
		Vector<Klocek> tmp = new Vector<Klocek>();

		for( int i =0; i < klocki.size(); i++ ){
			if( klocki.get(i).getX() == line )
				tmp.addElement(klocki.get(i));
		}
		return tmp;
	}
	// Metoda zwraca linie poziome w ktorych okreslona jest figura
	public Vector<Integer> getLinesY(){
		Vector<Integer> toRet = new Vector<Integer>();
		boolean dodaj = true;
		int tmp;
		
		for( int i = 0; i < klocki.size(); i++ ){
			
			tmp = klocki.get(i).getY();
			for( int j = 0; j < toRet.size(); j++)
				if( toRet.get(j) == tmp)
					dodaj = false;

			if( dodaj )
				toRet.add(tmp);
			dodaj = true;
		}
		return toRet;
	}
	// Metoda zwraca klocki danej figury w okreslonej lini
	public Vector<Klocek> getKlockiInLineY( int line ){
		Vector<Klocek> tmp = new Vector<Klocek>();

		for( int i =0; i < klocki.size(); i++ ){
			if( ( klocki.get(i).getY() >= line && klocki.get(i).getY() < line + Klocek.w ) ||
					(klocki.get(i).getY() <= line && klocki.get(i).getY() > line - Klocek.w)) // to 10 tutaj zeby sie dalo wciskac klocki nad ziemia
				tmp.addElement(klocki.get(i));
		}
		return tmp;
	}
	// Metoda usuwa z figury klocki ktore znajduja sie w okreslonej poziomej lini
	public void removeKlocekInLine( int line ){
		for( int i =0; i < klocki.size();  ){
			if( klocki.get(i).getY() == line){
				klocki.remove(i);
				i = 0;
			}
			else
				i++;
		}
	}
	//Metoda usuwa klockek z figury o danych wspolrzednych
	public void removeKlocekInPoint( int x, int y){
		for( int i = 0; i < klocki.size();  ){
			if( klocki.get(i).getY() == y && klocki.get(i).getX() == x){
				klocki.remove(i);
				i = 0;
			}
			else 
				i++;
		}
	}
	// Metoda obniza o jeden klocek klocki danej figury ktore znajduja sie powyzej podanej lini
	public void allDown( int line ){
		for( int i = 0; i < klocki.size(); i++ ){
			if( klocki.get(i).getY() < line)
			klocki.get(i).setY(klocki.get(i).getY()+Klocek.w);
		}
	}
	// Metoda sprawdza czy mozna wykonac obrut figury
	private boolean tryRote( Vector<Figura> dfig, Vector<Klocek> tmpFig ){
		for( int i = 0; i < tmpFig.size(); i++ ){
			// sprawdzenie czy w osi X jest ok
			if( tmpFig.get(i).getX() < GamePanel.StartBoardX || tmpFig.get(i).getX() + Klocek.w > GamePanel.StartBoardX + GamePanel.BoardWidth)
				return false;
			// sprawdzenie czy w osi Y jest ok
			if( tmpFig.get(i).getY() + Klocek.w > GamePanel.StartBoardY+GamePanel.BoardHeight )
				return false;
			// sprawdzenie czy nie koliduje z figurami ktore juz spadly
			for( int j = 0; j < dfig.size(); j++ )
				if( dfig.get(j).pointInFig(tmpFig.get(i).getX(),tmpFig.get(i).getY()) )				
					return false;
		}
		return true;
	}
	// Metoda sprawdza czy klocek ktory ma takie wspolrzedne nachodzi na podana figure
	boolean pointInFig( int point_x, int point_y ){
		for( int i = 0; i < klocki.size(); i++){
			if( point_x >= klocki.get(i).getX() && point_x <= klocki.get(i).getX() + Klocek.w ){
				// to bylo sprawdzenie dla osi x teraz bedzie os Y
				if( point_y >= klocki.get(i).getY() - Klocek.w && point_y  <= klocki.get(i).getY()+ Klocek.w)
					return true;}
		}
		return false;
	}
	protected boolean boom(){
		return false;
	}
}
