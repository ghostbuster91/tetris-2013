package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class HighScorePanel extends JPanel implements ActionListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4635757700150287627L;
	private JButton bMenu;
	private GeneralFrame parent_;
	private String[][] highScoreTable_;
	private JTextArea tarea;
	
	
	HighScorePanel( GeneralFrame parent){
		super();
		
		parent_ = parent;
		setLayout(new BorderLayout());
		bMenu = new JButton("Menu");
		this.add(bMenu, BorderLayout.NORTH);
		bMenu.addActionListener(this);
		
		
		
		
		JPanel middlePanel = new JPanel ();
        middlePanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Top 15 high scores:" ) );

        // create the middle panel components
        tarea = new JTextArea(20,30);
		tarea.setEditable(false);
		//this.add(tarea,BorderLayout.CENTER);
     
        //Add Textarea in to middle panel
        middlePanel.add ( tarea );
        
        this.add(middlePanel,BorderLayout.CENTER);
		//fillTarea();
		
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object z = e.getSource();
		if ( z == bMenu) {
			int currentCard = 1;
            parent_.getCardLayout().show(parent_.getCardPanel(), "" + (currentCard));
		
		}
	}
	public void fillTarea(){
		try {
			StringBuffer tmp = new StringBuffer("");
			Client.cl_state = Client.CL_STATE.GET_SCORE;
			Client.start();
			if( Client.connected ){
				highScoreTable_ = Client.highScoreTable_;
				for( int i = 0; i < 15; i++ )
					if(!highScoreTable_[i][0].equals("null"))
						tmp.append(highScoreTable_[i][0]+" - "+highScoreTable_[i][1]+"\n");
				tarea.setText(tmp.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
