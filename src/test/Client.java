package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;


public class Client {
	
	
	static int StartBoardX;
	static int StartBoardY;
	static int TIME_TO_MOVE;
	static int Clock_Multiplier_Delay;
	static int Clock_dif_per_lvl;
	static int LineToNextLvl;
	static int klocek_w;
	static int klocek_l;
	static int klocek_marg;
	static int base_lvl;
	static String HOST = null;
	
	static boolean connected = false;
	//static boolean get_conf = true;// w przecuiwnym wypadku send high score
	enum CL_STATE { GET_CONF, GET_SCORE, UPDATE_SCORE };
	static CL_STATE cl_state = CL_STATE.GET_CONF;
	
	
	static String[][] highScoreTable_;
	
	private static int PORT = 8080;
	public static boolean serverReachable() throws IOException{
		InetAddress addr = null;
		
		addr = InetAddress.getByName(HOST);
		
		return addr.isReachable(3000);
		
		
		
	}
	  public static void start()
	      throws IOException {
		  highScoreTable_ = new String[15][2];
		  connected = false;
	    // Passing null to getByName() produces the
	    // special "Local Loopback" IP address, for
	    // testing on one machine w/o a network:
	    InetAddress addr = 
	      InetAddress.getByName(HOST);
	    // Alternatively, you can use 
	    // the address or name:
	    // InetAddress addr = 
	    //    InetAddress.getByName("127.0.0.1");
	    // InetAddress addr = 
	    //    InetAddress.getByName("localhost");
	    System.out.println("addr = " + addr);
	    Socket socket = null;
	    try{
	    	 socket = new Socket(addr, PORT); 
	    } catch( ConnectException e ){
	    	System.out.println("Can't connect to server! ");
	    	return;
	    }
	      connected = true;
	    // Guard everything in a try-finally to make
	    // sure that the socket is closed:
	    try {
	      System.out.println("socket = " + socket);
	      BufferedReader in =
	        new BufferedReader(
	          new InputStreamReader(
	            socket.getInputStream()));
	      // Output is automatically flushed
	      // by PrintWriter:
	      PrintWriter out =
	        new PrintWriter(
	          new BufferedWriter(
	            new OutputStreamWriter(
	              socket.getOutputStream())),true);
	     
	     /* for(int i = 0; i < 10; i ++) {
	        out.println("howdy " + i);
	        String str = in.readLine();
	        System.out.println(str);
	      }*/
	      //boolean ok = Boolean.getBoolean(in.readLine());
	      out.println("secret password connection");
	      
	      String str = in.readLine();
	      if( str.equals("OK")){
		      //int ile = Integer.parseInt(in.readLine());
		      //System.out.println("ile" + ile);
	    	  if( cl_state.equals(CL_STATE.GET_CONF) ){
	    		  
	    		  out.println("GET_CONF");
	    		  
			      StartBoardX  = Integer.parseInt(in.readLine());  
			      StartBoardY = Integer.parseInt(in.readLine()); 
			      TIME_TO_MOVE = Integer.parseInt(in.readLine()); 
			      Clock_Multiplier_Delay = Integer.parseInt(in.readLine()); 
			      Clock_dif_per_lvl = Integer.parseInt(in.readLine()); 
			      LineToNextLvl = Integer.parseInt(in.readLine()); 
			      klocek_w = Integer.parseInt(in.readLine()); 
			      klocek_l = Integer.parseInt(in.readLine()); 
			      klocek_marg = Integer.parseInt(in.readLine()); 
			      base_lvl = Integer.parseInt(in.readLine());
	    	  }
	    	  else if( cl_state.equals(CL_STATE.GET_SCORE) ) {
	    		  out.println("GET_SCORE");
	    		  for( int i = 0; i < 15; i++ ){
	    			  String tmp = in.readLine();
	    			  //System.out.println("1:"+tmp);
	    			  highScoreTable_[i][0] = tmp.split("-")[0];
	    			  highScoreTable_[i][1] = tmp.split("-")[1];
	    			  //System.out.println(highScoreTable_[i][0]+"+"+highScoreTable_[i][1]);
	    		  }
	    	  }
	    	  else if( cl_state.equals(CL_STATE.UPDATE_SCORE) ){
	    		  
	    	  }
	      }
	      else{
	    	  System.out.println(str);
	      }
	      
	      out.println("END");
	      
	     // System.out.println( StartBoardX + " " + StartBoardY + " " + TIME_TO_MOVE + " " + Clock_Multiplier_Delay + " " + Clock_dif_per_lvl + " " + LineToNextLvl + " " + klocek_w + " "+ klocek_l + " " + klocek_marg + " " + base_lvl );
	    } finally {
	      System.out.println("closing...");
	      socket.close();
	    }
	  }
	} ///: