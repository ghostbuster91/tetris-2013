package test;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 539667226753552808L;
	private JLabel label_line;
	private JLabel label_multi;
	private JLabel label_score;
	private JLabel label_lvl;
	private int linie_;
	private int multi_;
	private int score_;
	private int lvl_;
	private JButton bMenu;
	private GeneralFrame parent_;
	private GamePanel gamePanel;
	
	ScorePanel(GeneralFrame parent){
		super();
		parent_ = parent;
		
		linie_ = 0;
		multi_ = 1;
		
		bMenu = new JButton("Menu");
		label_line = new JLabel("Linie: " + linie_);
		label_multi = new JLabel("Multiplication: " + multi_);
		label_score = new JLabel("Score: " + score_);
		label_lvl = new JLabel("Level: " + lvl_ );
		
		GridLayout gl = new GridLayout(10,0);
		//gl.setVgap(50);
		setLayout(gl);
		this.add(label_line,5,0);
		this.add(label_multi,6,0);
		this.add(label_score,7,0);
		this.add(label_lvl,8,0);
		this.add(bMenu,9,0);
		bMenu.addActionListener(this);
		
	}
	public void setGamePanel( GamePanel game_panel ){
		gamePanel = game_panel;
	}
	public void updateScore( int newLine, int newMulti, int newScore, int newLvl ){
		linie_ = newLine;
		multi_ = newMulti;
		score_ = newScore;
		label_line.setText("Linie: " + linie_);
		label_multi.setText("Multiplication: " + multi_);
		label_score.setText("Score: " + score_);
		label_lvl.setText("Level: " + newLvl );
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object z = e.getSource();
		if ( z == bMenu) {
			int currentCard = 1;
            parent_.getCardLayout().show(parent_.getCardPanel(), "" + (currentCard));
            gamePanel.END_GAME();
			
		}
		
	}
}
