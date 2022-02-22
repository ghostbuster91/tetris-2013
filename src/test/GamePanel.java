package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private class Dropper implements Runnable{
		private boolean res;
		//private Random randomGenerator;
		
		public Dropper( boolean resumed ){
			res = resumed;
			next_lvl = false;
			//randomGenerator = new Random();
		}
		
		@Override
		public void run() {
          	    while(game_state.equals( STATE.PLAY ) )
				{
          	    	if( !res )// jezeli granie nie jest wznawiana
          	    		getRandFig(BoardWidth/2,StartBoardY);
          	    		move_able = true;
          	    	

          			while( trymove_down() ){
						fig.move(0,1);
						sleep(TIME_TO_MOVE - Clock_dif_per_lvl*(lvl-1));
						repaint();
						if( !game_state.equals( STATE.PLAY ) )
							break;
						
						
					}
          			if( fig.boom() )
          				bombLanded();
          			else{
          				sleep(TIME_TO_MOVE*Clock_Multiplier_Delay- Clock_dif_per_lvl*(lvl-1)); // Jest potrzebne zeby dalo sie dopasowac klocek
          				move_able = false;
          				if( checkGameOver() ){
          				game_state = STATE.END;
          				break;
          				}
          			
          				dfig.add(fig);
          				checkAndRemove(fig.getLinesY());
          				res = false;
          				if( next_lvl )
          					break;
          			}
			}
          	if( game_state.equals( STATE.PAUSE ) )    
          	   System.out.println("PAUSE");
          	else if( game_state.equals(STATE.END)){
          		System.out.println("GAME OVER!");
          		finish_game();
          	}
          	if( next_lvl ){
          		NEXT_LVL();
          		Dropper drop = new Dropper(false);
        		//game_state = STATE.PLAY;
        		new Thread(drop).start();
        		
          	}
		}
	}
	
	public enum STATE { PLAY, PAUSE, END, NOT_START };
	
	private STATE game_state;
	private static final long serialVersionUID = -578550069411696857L;
	static int StartBoardX = 20;
	static int StartBoardY = 20;
	static int BoardWidth = 10*Klocek.l;
	static int BoardHeight = 30*Klocek.w ;
	private int TIME_TO_MOVE = 20; // czas w ms co ile porusza sie klocek o jeden piksel
	private int Clock_Multiplier_Delay = 3; // wspolczynnik ktory decyduje o tym ile mamy czasu na wcisniecie klocka gdy dotknie dna
	private int Clock_dif_per_lvl = 2;
	private int LineToNextLvl = 5;
	
	private Rectangle2D board;
	
	private Figura fig; // aktualna figura
	private Vector<Figura> dfig; //figury ktore juz zostaly opuszczone
	private int pos_x;
	private int pos_y;
	private boolean move_able;
	private boolean next_lvl; 
	private int lvl = 1;
	
	private int score_line; // ilosc skasowanych lini
	private int score_multi; // ilosc zdobytego mnoznika 
	private int score_; // ilosc punktow, score_line * score_multi = DeltaScore
	private JPanel score_panel; // uchwyt do panelu aby mozna bylo go aktualizowac
	
	private Random randomGenerator;
	
	private GeneralFrame parent_;
	
	Image offscr = null;
	Graphics offscrgr = null;
	
	public GamePanel(JPanel scorePanel, GeneralFrame parent){
		
		parent_ = parent;
		
		if( Client.connected )
			setConstans();
		
		dfig = new Vector<Figura>();
		game_state = STATE.NOT_START;
		setPreferredSize( new Dimension(400,700));
		
		board = new Rectangle2D.Double(StartBoardX,StartBoardY,BoardWidth,BoardHeight);
		
		randomGenerator = new Random();
		
		score_panel = scorePanel;
	}
	
	public void action_right()
	{
		if( try_move( Klocek.w) && move_able )
			fig.move(Klocek.w, 0);
		
	}
	public void action_left()
	{
		if( try_move( -Klocek.w) && move_able )
			fig.move(-Klocek.w, 0);
		
	}
	public void action_rote(){
		if( move_able )
			fig.rote( dfig);
    }
	public void action_down(){
		if(trymove_down() && move_able)
			while (trymove_down()) {
			fig.move(0,1);
			}
	}
	
	// Metoda sprawdza czy figura moze ruszyc sie do dolu
	private boolean trymove_down(){
		if(  fig.max_down() >= BoardHeight + StartBoardY ) 
			return false;
		
		Vector<Integer> lines = fig.getLinesX();
				
		for( int i = 0; i < lines.size(); i++ ){
			Vector<Klocek> klocki = getKlockiInLineX(lines.get(i));
			for( int j = 0; j < klocki.size(); j++){
				if( klocki.get(j).getY() < fig.max_down_in_line(lines.get(i)) )
					continue;
				if( Math.abs(klocki.get(j).getY() - fig.max_down_in_line(lines.get(i))) == 0 )
					return false;
			}
		}
		return true;
	}
	// Metoda sprawdza czy figura moze ruszyc sie na boki
	private boolean try_move( int dx ){
		
		if( ( fig.max_left() + dx < Klocek.w || fig.max_right() + dx > BoardWidth +StartBoardX) )
			return false;
		
		//Pobranie lini Y w ktorych jest okreslony klocek
		Vector<Integer> lines = fig.getLinesY();
		
		for( int i = 0; i < lines.size(); i++ ){
			// Dla kazdej lini pobieramy klocki z planszy ktore w niej sa
			Vector<Klocek> klocki = getKlockiInLineY(lines.get(i));
			
			for( int j = 0; j < klocki.size(); j++){
				if( dx > 0){ // czyli proba ruchu w prawo
					if( klocki.get(j).getX() < fig.max_right_in_line(lines.get(i)))
						continue;
					if( Math.abs(klocki.get(j).getX() - fig.max_right_in_line(lines.get(i))) == 0 )
						return false;
				}
				else if( dx < 0 ){ // proboa ruchu w lewo
					if( klocki.get(j).getX() > fig.max_left_in_line(lines.get(i)))
						continue;
					if( Math.abs((klocki.get(j).getX()+Klocek.w) - fig.max_left_in_line(lines.get(i))) == 0 )
						return false;
				}
			}
		}

		return true;
	}
	// Metoda zwraca Klocki z vectora dfig ktore znajduja sie w danej lini pionowej
	private Vector<Klocek> getKlockiInLineX( int line ){
		Vector<Klocek> tmp = new Vector<Klocek>();
		
		for(int i = 0; i < dfig.size(); i++ ){
			Vector<Klocek> tmp2 = dfig.get(i).getKlockiInLineX(line);
			tmp.addAll(tmp2);
		}
		
		return tmp;
	}
	// Metoda zwraca Klocki z vectora dfig ktore znajduja sie w danej lini pionowej
	private Vector<Klocek> getKlockiInLineY( int line ){
		Vector<Klocek> tmp = new Vector<Klocek>();
		
		for(int i = 0; i < dfig.size(); i++ ){
			Vector<Klocek> tmp2 = dfig.get(i).getKlockiInLineY(line);
			tmp.addAll(tmp2);
		}
		
		return tmp;
	}
	// Metoda sprawdza czy linia jest pelna nastepnie ja kasuje
	private void checkAndRemove( Vector<Integer> lines ){
		Vector<Integer> linesRemoved = new Vector<Integer>();
		
		for(int i = 0; i < lines.size(); i++)
			if( checkFullLine(lines.get(i))){
				if(checkColorLine(lines.get(i)) )
					score_multi+=10;
				removeLine(lines.get(i));
				linesRemoved.add(lines.get(i));
			}
		
		
		if( linesRemoved.size() > 0){
			score_multi += linesRemoved.size() - 1;			
			score_ += score_multi*lvl;
			score_line+= linesRemoved.size();

			((ScorePanel) score_panel).updateScore(score_line, score_multi, score_, lvl );
		}
		if( score_line >= LineToNextLvl ){
			next_lvl = true;
			return;
		}
		Collections.sort(linesRemoved);

		for( int i = 0; i < linesRemoved.size(); i++ )
			allDown(linesRemoved.get(i));
	}
	// Metoda sprawdza czy linia jest pelna
	private boolean checkFullLine( int line ){
		Vector<Klocek> tmp = getKlockiInLineY(line);

		if( tmp.size() == BoardWidth/Klocek.w )
				return true;
		
		return false;
	}
	// Metoda kasuje linie
	private void removeLine( int line ){
		for( int i = 0; i < dfig.size(); i++)
			dfig.get(i).removeKlocekInLine(line);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
	       super.paintComponent(g);
	       Graphics2D g2d = (Graphics2D) g;
	       g2d.draw(board);
	       
	       drawHelpLines(g2d);
	       
	       fig.draw(g2d);
	       
    	   for( int i = 0; i < dfig.size(); i++ )
	    	   dfig.get(i).draw(g2d);
	       
	      // addNotify();
	       //g.drawImage(offscr,0,0,this);
	       
    }
	// Metoda stopuje gre
	public void PAUSE_GAME(){
		if( game_state.equals(STATE.PLAY)) 
				game_state = STATE.PAUSE;
		else if( game_state.equals(STATE.PAUSE))
				RESUME_GAME();
		
	}
	// Metoda wznawia gre
	public void RESUME_GAME(){
		game_state = STATE.PLAY;
		Dropper drop = new Dropper(true);
		new Thread(drop).start();
	}
	
	public void START_GAME(){
		game_state = STATE.PLAY;
		Dropper drop = new Dropper(false);
		new Thread(drop).start();
		score_line = 0;
		score_multi = 1;
		score_ = 0;
		dfig.removeAllElements();
	}
	public void END_GAME(){
		game_state = STATE.NOT_START;
	}
	private void NEXT_LVL(){
		System.out.println("Next lvl");
		score_line = 0;
		lvl++;
		((ScorePanel) score_panel).updateScore(score_line, score_multi, score_, lvl );
		dfig.removeAllElements();
		putRandomFigs(2*lvl);
	}
	
	// Metoda wywolywana po usunieciu lini obniza wszystkie klocki ktore znajdowaly sie
	// ponad ta linia o jeden klocek
	private void allDown(int line ){
		for( int i =  0; i < dfig.size(); i++ )
			dfig.get(i).allDown( line );
	}
	//Pomocnicza metoda do usypiania watku
	private void sleep( int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ie) {
        }
    }
	//Metoda zwracajaca wylosowana figure
	private void getRandFig( int x, int y ){
		
		pos_x = x;
  	    pos_y = y;
  	    Color color;
  	    int random = randomGenerator.nextInt(5);
  	      	    
  	    switch( random ){
  	    	case 1:
  	    		color = Color.MAGENTA;
  	    		break;
  	    	case 2:
  	    		color = Color.GREEN;
  	    		break;
  	    	case 3:
  	    		color = Color.ORANGE;
  	    		break;
  	    	case 4:
  	    		color = Color.RED;
  	    		break;
  	    	case 0:
  	    		color = Color.CYAN;
  	    		break;
  	    	default:
  	    		color = Color.ORANGE;
  	    		break;
  	    }
  	    random = randomGenerator.nextInt(75);
  	    
  	    if( random < 10 )
  	    	fig = new Fig1(pos_x,pos_y,color);
  	    else if( random >= 10 && random < 20 )
  	    	fig = new Fig2(pos_x,pos_y,color);
  	    else if( random >= 20 && random < 30 )
  	    	fig = new Fig3(pos_x,pos_y,color);
  	    else if( random >= 30 && random < 40 )
  	    	fig = new Fig4(pos_x,pos_y,color);
  	    else if( random >= 40 && random < 50 )
  	    	fig = new Fig5(pos_x,pos_y,color);
  	    else if( random >= 50 && random < 60 )
  	    	fig = new Fig6(pos_x,pos_y,color);
  	    else if( random >= 60 && random < 70)
  	    	fig = new Fig7(pos_x,pos_y,color);
  	    else
  	    	fig = new FigBomb(pos_x,pos_y,color);
  	   
  	}
	private void putRandomFigs( int count ){
		int start_x;
		int start_y = StartBoardY;
		move_able = true;
		for( int i =0 ; i < count; i++ ){
			start_x = randomGenerator.nextInt(9)*Klocek.w+StartBoardX + Klocek.w; //+ Klocek.w;
			getRandFig(start_x,start_y);
			action_down();
			dfig.add(fig);
		}
	}
	// Metoda sprawdza czy dana linia jest w jednakowym kolorze
	private boolean checkColorLine( int line){
		Vector<Klocek> tmp = getKlockiInLineY(line);
		Color win_color;
		
		win_color = tmp.get(0).getColor();
		
		for( int i = 1; i < tmp.size(); i++ )
			if( !tmp.get(i).getColor().equals(win_color) )
				return false;
		return true;
	}
	// Metoda sprawdza czy gracz przegral
	private boolean checkGameOver(){
		if( fig.max_up() <= StartBoardY )
			return true;
		return false;
	}
	// Rysuje pomocnicze linie dla spadajacych klockow
	private void drawHelpLines( Graphics offscrgr ){
		Vector<Integer> lines = fig.getLinesX();
		
		Collections.sort(lines);
		
		offscrgr.setColor(Color.WHITE);
		for( int i = 0; i < lines.size(); i++ )
	        offscrgr.drawLine(lines.get(i),StartBoardY + 1,lines.get(i), StartBoardY + BoardHeight - 1 );
		 offscrgr.drawLine(lines.lastElement()+Klocek.w,StartBoardY + 1,lines.lastElement()+Klocek.w, StartBoardY + BoardHeight - 1 );
	}
	private void bombLanded(){
		Vector<Klocek> klockiX1, klockiX2, klockiX3;
		Vector<Klocek> klockiY1, klockiY2, klockiY3;
		Vector<Klocek> finalY1, finalY2, finalY3;
		
		
		finalY1 = new Vector<>();
		finalY2 = new Vector<>();
		finalY3 = new Vector<>();
		
		int x = fig.getLinesX().firstElement();
		int y = fig.getLinesY().firstElement();
		
		klockiX1 = getKlockiInLineX(x-Klocek.w);
		klockiX2 = getKlockiInLineX(x);
		klockiX3 = getKlockiInLineX(x+Klocek.w);
		
		klockiY1 = getKlockiInLineY(y-Klocek.l);
		klockiY2 = getKlockiInLineY(y);
		klockiY3 = getKlockiInLineY(y+Klocek.l);
		
		
		for( int i = 0; i < klockiY1.size(); i++ ){
			boolean exist = false;
			for( int j = 0; j < klockiX1.size(); j++ )
				if( klockiY1.get(i).equals(klockiX1.get(j))){
					exist = true;
					break;
				}
			for( int j = 0; j < klockiX2.size(); j++ )
				if( klockiY1.get(i).equals(klockiX2.get(j))){
					exist = true;
					break;
				}
			for( int j = 0; j < klockiX3.size(); j++ )
				if( klockiY1.get(i).equals(klockiX3.get(j))){
					exist = true;
					break;
				}
			if( exist )
				finalY1.add(klockiY1.get(i));
		}
		for( int i = 0; i < klockiY2.size(); i++ ){
			boolean exist = false;
			for( int j = 0; j < klockiX1.size(); j++ )
				if( klockiY2.get(i).equals(klockiX1.get(j))){
					exist = true;
					break;
				}
			for( int j = 0; j < klockiX2.size(); j++ )
				if( klockiY2.get(i).equals(klockiX2.get(j))){
					exist = true;
					break;
				}
			for( int j = 0; j < klockiX3.size(); j++ )
				if( klockiY2.get(i).equals(klockiX3.get(j))){
					exist = true;
					break;
				}
			if( exist )
				finalY2.add(klockiY2.get(i));
		}
		for( int i = 0; i < klockiY3.size(); i++ ){
			boolean exist = false;
			for( int j = 0; j < klockiX1.size(); j++ )
				if( klockiY3.get(i).equals(klockiX1.get(j))){
					exist = true;
					break;
				}
			for( int j = 0; j < klockiX2.size(); j++ )
				if( klockiY3.get(i).equals(klockiX2.get(j))){
					exist = true;
					break;
				}
			for( int j = 0; j < klockiX3.size(); j++ )
				if( klockiY3.get(i).equals(klockiX3.get(j))){
					exist = true;
					break;
				}
			if( exist )
				finalY3.add(klockiY3.get(i));
		}
		for( int i = 0; i < finalY1.size(); i++ )
			for( int j = 0; j < dfig.size(); j++ )
				if( dfig.get(j).pointInFig(finalY1.get(i).getX(),finalY1.get(i).getY()) )
					dfig.get(j).removeKlocekInPoint(finalY1.get(i).getX(),finalY1.get(i).getY());
		for( int i = 0; i < finalY2.size(); i++ )
			for( int j = 0; j < dfig.size(); j++ )
				if( dfig.get(j).pointInFig(finalY2.get(i).getX(),finalY2.get(i).getY()) )
					dfig.get(j).removeKlocekInPoint(finalY2.get(i).getX(),finalY2.get(i).getY());
		for( int i = 0; i < finalY3.size(); i++ )
			for( int j = 0; j < dfig.size(); j++ )
				if( dfig.get(j).pointInFig(finalY3.get(i).getX(),finalY3.get(i).getY()) )
					dfig.get(j).removeKlocekInPoint(finalY3.get(i).getX(),finalY3.get(i).getY());
			
		
		
	}
	private void setConstans(){
		StartBoardX = Client.StartBoardX;
		StartBoardY = Client.StartBoardY;
		
		 TIME_TO_MOVE = Client.TIME_TO_MOVE; // czas w ms co ile porusza sie klocek o jeden piksel
		 Clock_Multiplier_Delay = Client.Clock_Multiplier_Delay; // wspolczynnik ktory decyduje o tym ile mamy czasu na wcisniecie klocka gdy dotknie dna
		 Clock_dif_per_lvl = Client.Clock_dif_per_lvl;
		 LineToNextLvl = Client.LineToNextLvl;
		
		 Klocek.w = Client.klocek_w;
		 Klocek.l = Client.klocek_l;
		 Klocek.marg = Client.klocek_marg;
		  lvl = Client.base_lvl;
		 
		 
		
		 BoardWidth = 10*Klocek.l;
		 BoardHeight = 30*Klocek.w ;
		 
	}
	private void finish_game(){
		
		//TUTAJ TRZEBA POBRAC SCORY SPRAWDZIC CZY WYNIK MIESCI SIE W TOP15
		// JESLI TAK WYSWIETLAMY GOPanel - game over panel
		// JESLI nie wyswietlamy highScorePanel
		
		int currentCard = 1;
        parent_.getCardLayout().show(parent_.getCardPanel(), "" + (currentCard));
	}
	
	
	/*public void updateOffscrSize(){
		System.out.println(getWidth() + " " + getHeight());
        offscr = createImage(getWidth(), getHeight());
        offscrgr = offscr.getGraphics();
	}
	public void updateOffscreen() {
        offscrgr.clearRect(0, 0, offscr.getWidth(this), offscr.getHeight(this));
        offscrgr.setColor(Color.blue);
    	   //offscrgr.draw(board);
	       
	       fig.draw(offscrgr);

    	   for( int i = 0; i < dfig.size(); i++ )
	    	   dfig.get(i).draw(offscrgr);
    }
	public Dimension getPreferredSize() {
        return new Dimension(BoardWidth+200, BoardHeight+200);
    }
	public void addNotify() {
        super.addNotify();
        offscr = createImage(getPreferredSize().width,
                getPreferredSize().height);
        offscrgr = offscr.getGraphics();
    }*/

	
    
	
}
