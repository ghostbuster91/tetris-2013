package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel implements ActionListener 
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6843691155599638868L;
	private JButton bStart, bHighScore, bExit;
	private JPanel pHelp;
	private GamePanel gamePanelRef;
	private HighScorePanel hspanelRef;
	private int currentCard = 1;
	private GeneralFrame genFramRef;
	
	
	MenuPanel(GamePanel gamePanel, GeneralFrame parent, HighScorePanel highScorePanel){
		super();
		setLayout(new BorderLayout());
		
		genFramRef = parent;
		hspanelRef = highScorePanel;
		
		bStart = new JButton("Start");
		bHighScore = new JButton("HighScore");
		bExit = new JButton("Exit");
		
		bStart.addActionListener(this);
		bHighScore.addActionListener(this);
		bExit.addActionListener(this);
		
		pHelp = new JPanel();

		pHelp.add(bStart, BorderLayout.NORTH);
		pHelp.add(bHighScore, BorderLayout.CENTER);
		pHelp.add(bExit, BorderLayout.SOUTH);

		this.add(pHelp, BorderLayout.CENTER);
		
		this.gamePanelRef = gamePanel; 
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object z = e.getSource();
		if ( z == bStart) {
			currentCard = 2;
			gamePanelRef.START_GAME();
            genFramRef.getCardLayout().show(genFramRef.getCardPanel(), "" + (currentCard));
		}
		else if ( z == bHighScore){
				hspanelRef.fillTarea();
                currentCard = 3;
                genFramRef.getCardLayout().show(genFramRef.getCardPanel(), "" + (currentCard));   
		}
		else if ( z == bExit){
			System.exit(0);
		}
		
	}
	
	
}