
import javax.swing.JFrame;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.MatteBorder;
import java.awt.GridLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class tictactoe implements Runnable
{


	private JFrame frame;
	JButton b1,b2,b3,b4,b5,b6,b7,b8,b9,reset;
	JLabel player1,player2,score1,score2,turns,turnlbl;
	int s1=0,s2=0;
	ImageIcon boardimg,ipicon,porticon,serinit,clicon,yourX,yourCircle,enemyX,enemyCircle,winnericon,tieicon;

    int[] bclicked=new int[10];
     
    private boolean enemywon=false;
    private boolean won=false;
    private boolean tie=false;
    private boolean yourturn= false;
    private boolean clientplayer=true;
    private boolean accepted;
    private String ip="localhost";
    private int port=22222;
    private Thread t;
    char[] box=new char[10];
    
    private ServerSocket server;
    private Socket client;
    private DataInputStream din;
    private DataOutputStream dout;
    private JLabel label;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JLabel windpl;
    
	public static void main(String[] args) {
		tictactoe window = new tictactoe();
					window.frame.setVisible(true);
	}

	public tictactoe() {
		
		loadallicons();
		
		ip= (String)JOptionPane.showInputDialog(
                null,
                "Enter IP Address of Server",
                "IP ADDRESS",
                JOptionPane.PLAIN_MESSAGE,
                ipicon, 
                null,
                ip);

		port= Integer.parseInt((String)JOptionPane.showInputDialog(
                null,
                "Enter Port Address of Server",
                "Port Address", 
                JOptionPane.PLAIN_MESSAGE,
                porticon, 
                null, 
                port));
		
		frame();
		if(!checkconnection())
			initServer();
				
	

		t=new Thread(this,"NoughtsAndCrosses");
		t.start();
	}
	private void loadallicons(){
		   try{
				boardimg= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/board.png")));
				ipicon= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/ipicon.png")));
				porticon= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/porticon.png")));
				serinit= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/serinit.png")));
				clicon= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/clicon.png")));
				yourX= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/yourX.png")));
				yourCircle= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/yourCircle.png")));
				enemyX= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/enemyX.png")));
				enemyCircle= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/enemyCircle.png")));
				winnericon= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/winnericon.png")));
				tieicon= new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/tie.png")));
		   }catch(IOException e){
				   e.printStackTrace();
			   }
	}
	@Override
	public void run() {
	while(true){
		turn();
		if(!clientplayer && !accepted)
			listenforserver();
	}
	}
	private void turn() {
		if(!yourturn){
			try{
				if(clientplayer)
					turnlbl.setText("Player 1");
				else
					turnlbl.setText("Player 2");
				int space= din.readInt();
				syncclientserver(space);
				if(enemywin()||tie()){
					scores();
				}
				else
					yourturn=true;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		else{
			if(clientplayer)
				turnlbl.setText("Player 2");
			else
				turnlbl.setText("Player 1");
			button();
	}
	}
	private boolean enemywin(){
		if(clientplayer){
		if((box[1]=='X' && box[2]=='X' && box[3]=='X')||(box[4]=='X' && box[5]=='X' && box[6]=='X')||(box[7]=='X' && box[8]=='X' && box[9]=='X')||(box[1]=='X' && box[4]=='X' && box[7]=='X')||(box[2]=='X' && box[5]=='X' && box[8]=='X')||(box[3]=='X' && box[6]=='X' && box[9]=='X')||(box[1]=='X' && box[5]=='X' && box[9]=='X')||(box[3]=='X' && box[5]=='X' && box[7]=='X')){
				enemywon=true;
				return true;
		}
		}
		if(!clientplayer){
			if((box[1]=='O' && box[2]=='O' && box[3]=='O')||(box[4]=='O' && box[5]=='O' && box[6]=='O')||(box[7]=='O' && box[8]=='O' && box[9]=='O')||(box[1]=='O' && box[4]=='O' && box[7]=='O')||(box[2]=='O' && box[5]=='O' && box[8]=='O')||(box[3]=='O' && box[6]=='O' && box[9]=='O')||(box[1]=='O' && box[5]=='O' && box[9]=='O')||(box[3]=='O' && box[5]=='O' && box[7]=='O')){
				enemywon=true;
				return true;
		}
		}
	return false;
	}
private boolean win(){
	if(clientplayer){
	if((box[1]=='O' && box[2]=='O' && box[3]=='O')||(box[4]=='O' && box[5]=='O' && box[6]=='O')||(box[7]=='O' && box[8]=='O' && box[9]=='O')||(box[1]=='O' && box[4]=='O' && box[7]=='O')||(box[2]=='O' && box[5]=='O' && box[8]=='O')||(box[3]=='O' && box[6]=='O' && box[9]=='O')||(box[1]=='O' && box[5]=='O' && box[9]=='O')||(box[3]=='O' && box[5]=='O' && box[7]=='O')){
			won=true;
			return true;
	}
	}
	if(!clientplayer){
		if((box[1]=='X' && box[2]=='X' && box[3]=='X')||(box[4]=='X' && box[5]=='X' && box[6]=='X')||(box[7]=='X' && box[8]=='X' && box[9]=='X')||(box[1]=='X' && box[4]=='X' && box[7]=='X')||(box[2]=='X' && box[5]=='X' && box[8]=='X')||(box[3]=='X' && box[6]=='X' && box[9]=='X')||(box[1]=='X' && box[5]=='X' && box[9]=='X')||(box[3]=='X' && box[5]=='X' && box[7]=='X')){
			won=true;
			return true;
	}
	}
return false;
}

private boolean tie(){
	if(!won && !enemywon){
	for(int i=0;i<10;i++)
	{
		if(box[i] == ' '){
			tie=false;
			return false;
			}
	}
		tie =true;
		return true;
	}
	tie=false;
	return false;
}

	private void listenforserver() {
		Socket client = null;
		try{
			client=server.accept();
			dout= new DataOutputStream(client.getOutputStream());
			din= new DataInputStream(client.getInputStream());
			accepted=true;
		}catch(IOException e){
			e.printStackTrace();
	}
	}
	private void initServer() {
		try{
			server= new ServerSocket(port,8,InetAddress.getByName(ip));
			JOptionPane.showMessageDialog(frame,
				    "SERVER INITIALIZED",
				    "Initialization",
				    JOptionPane.INFORMATION_MESSAGE,
				    serinit);
			windpl.setText("Server");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		yourturn=true;
		clientplayer=false;
	}

	private boolean checkconnection() {
		try{
			client = new Socket(ip, port);
			dout= new DataOutputStream(client.getOutputStream());
			din= new DataInputStream(client.getInputStream());
		}
		catch(IOException e){
	      return false;
	}
		JOptionPane.showMessageDialog(frame,
			    "Connected to the server",
			    "Connection",
			    JOptionPane.INFORMATION_MESSAGE,
			    clicon);
		windpl.setText("Client");
		return true;
	}
	private void frame(){
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 700, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel titlepanel = new JPanel();
		titlepanel.setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(204, 153, 102)));
		titlepanel.setBackground(new Color(0, 153, 102));
		titlepanel.setPreferredSize(new Dimension(700,50));
		frame.getContentPane().add(titlepanel, BorderLayout.NORTH);
		
		JLabel title = new JLabel("Noughts & Crosses");
		title.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 30));
		title.setForeground(new Color(255, 255, 204));
		title.setPreferredSize(new Dimension(350, 40));
		titlepanel.add(title);
		
		JPanel boardPanel = new JPanel();
		boardPanel.setBorder(new MatteBorder(0, 5, 5, 0, (Color) new Color(204, 153, 102)));
		boardPanel.setBackground(new Color(255, 255, 204));
		boardPanel.setPreferredSize(new Dimension(500,500));
		frame.getContentPane().add(boardPanel, BorderLayout.WEST);
		boardPanel.setLayout(new GridLayout(3, 3, 10, 10));
		
		b1 = new JButton("");
		boardPanel.add(b1);
	
		b2 = new JButton("");
		boardPanel.add(b2);
		
		b3 = new JButton("");
		boardPanel.add(b3);
		
		b4 = new JButton("");
		boardPanel.add(b4);
		
		b5 = new JButton("");
		boardPanel.add(b5);
		
		b6 = new JButton("");
		boardPanel.add(b6);
		
		b7 = new JButton("");
		boardPanel.add(b7);
		
		b8 = new JButton("");
		boardPanel.add(b8);
		
		b9 = new JButton("");
		boardPanel.add(b9);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(new MatteBorder(0, 5, 5, 5, (Color) new Color(204, 153, 102)));
		scorePanel.setBackground(new Color(0, 153, 102));
		scorePanel.setPreferredSize(new Dimension(195,500));
		frame.getContentPane().add(scorePanel, BorderLayout.EAST);
		scorePanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Score");
		lblNewLabel.setForeground(new Color(255, 255, 204));
		lblNewLabel.setFont(new Font("Segoe Script", Font.BOLD, 26));
		lblNewLabel.setBounds(60, 23, 75, 28);
		scorePanel.add(lblNewLabel);
		
		player1 = new JLabel("Player 1");
		player1.setForeground(new Color(255, 255, 204));
		player1.setBackground(new Color(255, 255, 204));
		player1.setFont(new Font("Segoe Script", Font.PLAIN, 18));
		player1.setBounds(10, 83, 91, 28);
		scorePanel.add(player1);
		
		player2 = new JLabel("Player 2");
		player2.setForeground(new Color(255, 255, 204));
		player2.setFont(new Font("Segoe Script", Font.PLAIN, 18));
		player2.setBounds(10, 122, 91, 28);
		scorePanel.add(player2);
		
		score1 = new JLabel("0 ");
		score1.setFont(new Font("Segoe Script", Font.PLAIN, 18));
		score1.setForeground(new Color(255, 255, 204));
		score1.setBounds(111, 83, 74, 28);
		scorePanel.add(score1);
		
	    score2 = new JLabel("0");
		score2.setForeground(new Color(255, 255, 204));
		score2.setFont(new Font("Segoe Script", Font.PLAIN, 18));
		score2.setBounds(111, 122, 74, 28);
		scorePanel.add(score2);
		
		reset = new JButton("Reset");
		reset.setBounds(60, 282, 75, 28);
		scorePanel.add(reset);
		
		turns = new JLabel("Now Turn:");
		turns.setBackground(new Color(240, 240, 240));
		turns.setForeground(Color.WHITE);
		turns.setFont(new Font("Book Antiqua", Font.BOLD | Font.ITALIC, 25));
		turns.setBounds(10, 181, 175, 33);
		scorePanel.add(turns);
		
		label = new JLabel(":");
		label.setForeground(new Color(255, 255, 204));
		label.setFont(new Font("Segoe Script", Font.PLAIN, 18));
		label.setBounds(96, 83, 28, 28);
		scorePanel.add(label);
		
		lblNewLabel_1 = new JLabel(":");
		lblNewLabel_1.setForeground(new Color(255, 255, 204));
		lblNewLabel_1.setFont(new Font("Segoe Script", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(96, 122, 29, 28);
		scorePanel.add(lblNewLabel_1);
		
		turnlbl = new JLabel("");
		turnlbl.setFont(new Font("Book Antiqua", Font.BOLD, 25));
		turnlbl.setForeground(Color.WHITE);
		turnlbl.setBounds(10, 225, 164, 33);
		scorePanel.add(turnlbl);
		
		lblNewLabel_2 = new JLabel("*can be pressed by player  with turn");
		lblNewLabel_2.setFont(new Font("Verdana", Font.PLAIN, 8));
		lblNewLabel_2.setBounds(10, 311, 175, 19);
		scorePanel.add(lblNewLabel_2);
		
		windpl = new JLabel("");
		windpl.setFont(new Font("Engravers MT", Font.PLAIN, 30));
		windpl.setBounds(10, 424, 175, 36);
		scorePanel.add(windpl);
		
		
		b1.setIcon(boardimg);
		b2.setIcon(boardimg);
		b3.setIcon(boardimg);
		b4.setIcon(boardimg);
		b5.setIcon(boardimg);
		b6.setIcon(boardimg);
		b7.setIcon(boardimg);
		b8.setIcon(boardimg);
		b9.setIcon(boardimg);
		
		for(int i=1;i<10;i++)
			bclicked[i]=0;
		for(int i=1;i<10;i++)
			box[i]=' ';
	}
	void button()
	{
	
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[1]==0  ){
					bclicked[1]=1;
					if(yourturn){
					if(clientplayer){
							b1.setIcon(yourCircle);
							box[1]='O';
			      	}
				    if(!clientplayer){
					    b1.setIcon(yourX);
						box[1]='X';
					}
				sendinfo(1);
					}
				}
			}
		});
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[2]==0  ){
					bclicked[2]=1;
					if(yourturn){
					if(clientplayer){
							b2.setIcon(yourCircle);
							box[2]='O';
			      	}
				    if(!clientplayer){
					    b2.setIcon(yourX);
						box[2]='X';
					}
				sendinfo(2);
					}
				}
			}
		});
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[3]==0  ){
					bclicked[3]=1;
					if(yourturn){
					if(clientplayer){
							b3.setIcon(yourCircle);
							box[3]='O';
			      	}
				    if(!clientplayer){
					    b3.setIcon(yourX);
						box[3]='X';
					}
				sendinfo(3);
					}
				}
			}
		});
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[4]==0  ){
					bclicked[4]=1;
					if(yourturn){
					if(clientplayer){
							b4.setIcon(yourCircle);
							box[4]='O';
			      	}
				    if(!clientplayer){
					    b4.setIcon(yourX);
						box[4]='X';
					}
				sendinfo(4);
					}
				}
			}
		});
		b5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[5]==0  ){
					bclicked[5]=1;
					if(yourturn){
					if(clientplayer){
							b5.setIcon(yourCircle);
							box[5]='O';
			      	}
				    if(!clientplayer){
					    b5.setIcon(yourX);
						box[5]='X';
					}
				sendinfo(5);
					}
				}
			}
		});
		b6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[6]==0  ){
					bclicked[6]=1;
					if(yourturn){
					if(clientplayer){
							b6.setIcon(yourCircle);
							box[6]='O';
			      	}
				    if(!clientplayer){
					    b6.setIcon(yourX);
						box[6]='X';
					}
				sendinfo(6);
					}
				}
			}
		});
		b7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[7]==0  ){
					bclicked[7]=1;
					if(yourturn){
					if(clientplayer){
							b7.setIcon(yourCircle);
							box[7]='O';
			      	}
				    if(!clientplayer){
					    b7.setIcon(yourX);
						box[7]='X';
					}
				sendinfo(7);
					}
				}
			}
		});
		b8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[8]==0  ){
					bclicked[8]=1;
					if(yourturn){
					if(clientplayer){
							b8.setIcon(yourCircle);
							box[8]='O';
			      	}
				    if(!clientplayer){
					    b8.setIcon(yourX);
						box[8]='X';
					}
				sendinfo(8);
					}
				}
			}
		});
		b9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bclicked[9]==0  ){
					bclicked[9]=1;
					if(yourturn){
					if(clientplayer){
							b9.setIcon(yourCircle);
							box[9]='O';
			      	}
				    if(!clientplayer){
					    b9.setIcon(yourX);
						box[9]='X';
					}
					}
					sendinfo(9);
				}
			}
		});
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendinfo(0);
				reset();
			}
		});

}
private void sendinfo(int a){
			if(yourturn){
				try{
					dout.writeInt(a);
					dout.flush();
				}catch(IOException e1){
					e1.printStackTrace();
				}
				
				if(win() || tie()){
					scores();
				}
				yourturn=false;
			}
	}
void syncclientserver(int i){
	if(i==0){
		reset();
	}

	bclicked[i]=1;
	if(i==1){
		if(clientplayer){
			b1.setIcon(enemyX);
			box[1]='X';
		}
		else{
			b1.setIcon(enemyCircle);
			box[1]='O';
		}
	}
	if(i==2){
		if(clientplayer){
			b2.setIcon(enemyX);
			box[2]='X';
		}
		else{
			b2.setIcon(enemyCircle);
			box[2]='O';
		}
	}
	if(i==3){
		if(clientplayer){
			b3.setIcon(enemyX);
			box[3]='X';
		}
		else{
			b3.setIcon(enemyCircle);
			box[3]='O';
		}
	}
	if(i==4){
		if(clientplayer){
			b4.setIcon(enemyX);
			box[4]='X';
		}
		else{
			b4.setIcon(enemyCircle);
			box[4]='O';
		}
	}
	if(i==5){
		if(clientplayer){
			b5.setIcon(enemyX);
			box[5]='X';
		}
		else{
			b5.setIcon(enemyCircle);
			box[5]='O';
		}
	}
	if(i==6){
		if(clientplayer){
			b6.setIcon(enemyX);
			box[6]='X';
		}
		else{
			b6.setIcon(enemyCircle);
			box[6]='O';
		}
	}
	if(i==7){
		if(clientplayer){
			b7.setIcon(enemyX);
			box[7]='X';
		}
		else{
			b7.setIcon(enemyCircle);
			box[7]='O';
		}
	}
	if(i==8){
		if(clientplayer){
			b8.setIcon(enemyX);
			box[8]='X';
		}
		else{
			b8.setIcon(enemyCircle);
			box[8]='O';
		}
	}
	if(i==9){
		if(clientplayer){
			b9.setIcon(enemyX);
			box[9]='X';
		}
		else{
			b9.setIcon(enemyCircle);
			box[9]='O';
		}
	}
}
private void scores(){
	if(clientplayer){
		if(won){
			s2++;
			score2.setText(Integer.toString(s2));
			player2wins();
		}
		if(enemywon){
			s1++;
			score1.setText(Integer.toString(s1));
		    player1wins();
		}
		if(tie){
			tiemessage();
		}
	}

	if(!clientplayer){
		if(won){
			s1++;
			score1.setText(Integer.toString(s1));
			player1wins();
		}
		if(enemywon){
			s2++;
			score2.setText(Integer.toString(s2));
		    player2wins();
		}
		if(tie){
			tiemessage();
		}
	}
}
void player1wins(){
	JOptionPane.showMessageDialog(frame,
		    "Player 1 wins",
		    "WINNER",
		    JOptionPane.INFORMATION_MESSAGE,
		    winnericon);
	refresh();
}
void player2wins(){
	JOptionPane.showMessageDialog(frame,
		    "Player 2 wins",
		    "WINNER",
		    JOptionPane.INFORMATION_MESSAGE,
		    winnericon);
	refresh();
}
void tiemessage(){
	JOptionPane.showMessageDialog(frame,
		    "Its a DRAW",
		    "TIE",
		    JOptionPane.INFORMATION_MESSAGE,
		    tieicon);
  refresh();
}
void refresh(){
	
	for(int i=1;i<10;i++)
		bclicked[i]=0;
	for(int i=1;i<10;i++)
		box[i]=' ';

	b1.setIcon(boardimg);
	b2.setIcon(boardimg);
	b3.setIcon(boardimg);
	b4.setIcon(boardimg);
	b5.setIcon(boardimg);
	b6.setIcon(boardimg);
	b7.setIcon(boardimg);
	b8.setIcon(boardimg);
	b9.setIcon(boardimg);
			
  if(!yourturn)
	yourturn=true;
	won=false;
	enemywon=false;
	tie=false;
}
void reset(){
	
	for(int i=1;i<10;i++)
		bclicked[i]=0;
	for(int i=1;i<10;i++)
		box[i]=' ';

	b1.setIcon(boardimg);
	b2.setIcon(boardimg);
	b3.setIcon(boardimg);
	b4.setIcon(boardimg);
	b5.setIcon(boardimg);
	b6.setIcon(boardimg);
	b7.setIcon(boardimg);
	b8.setIcon(boardimg);
	b9.setIcon(boardimg);
			
	won=false;
	enemywon=false;
	tie=false;
	
	s1=0;
	s2=0;
	score1.setText(Integer.toString(s2));
	score2.setText(Integer.toString(s2));

    	yourturn=false;
}

}
