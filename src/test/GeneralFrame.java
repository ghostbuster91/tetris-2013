package test;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GeneralFrame extends JFrame implements KeyListener{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 623663268465872480L;
	private GamePanel game_panel;
	private CardLayout cardLayout;
	private JPanel cardPanel; 
	private JPanel generalPanel;
	
	public GeneralFrame(){
		super("Tetris");

		cardLayout = new CardLayout();
        cardPanel = new JPanel();
        cardPanel.setLayout(cardLayout);
		
        JPanel highscore_panel = new HighScorePanel(this);
		
		
        try {
			Client.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        generalPanel = new JPanel();
        JPanel score_panel = new ScorePanel(this);
		
        game_panel = new GamePanel(score_panel,this);
        ((ScorePanel) score_panel).setGamePanel( game_panel );
        
		JPanel menu_panel = new MenuPanel(game_panel,this, (HighScorePanel) highscore_panel);
		generalPanel.setLayout(new BorderLayout());
		
		generalPanel.add(game_panel, BorderLayout.CENTER);
		generalPanel.add(score_panel, BorderLayout.EAST);
		
		getContentPane().add(cardPanel);
        
		cardPanel.add(menu_panel, "1");
        cardPanel.add(generalPanel, "2");
        cardPanel.add(highscore_panel, "3");
        cardPanel.setFocusable(true);
        cardPanel.addKeyListener(this);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final GeneralFrame frame = new GeneralFrame();
        frame.pack();
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
 		frame.setVisible(true);
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		 int keycode = e.getKeyCode();

		switch (keycode) {
        case KeyEvent.VK_LEFT:
            game_panel.action_left();
            break;
        case KeyEvent.VK_RIGHT:
            game_panel.action_right();
            break;    
        case KeyEvent.VK_DOWN:
        	game_panel.action_down();
        	break;
     	}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		int keycode = e.getKeyCode();

		switch (keycode) {
        case KeyEvent.VK_UP:
            game_panel.action_rote();
              break;
        case KeyEvent.VK_P:
        	game_panel.PAUSE_GAME();
        	break;
        case KeyEvent.VK_R:
        	game_panel.START_GAME();
        	break;
     	}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}
	void sleep() {
        try {
            Thread.sleep(25);
        } catch (InterruptedException ie) {
        }
    }
	CardLayout getCardLayout(){
		return cardLayout;
	}


	JPanel getCardPanel() {
		return cardPanel;
	}


}
