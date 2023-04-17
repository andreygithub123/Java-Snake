import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600; //some sort of matrix
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 100; //the higher the number of the delay the slower the game is gonna be and vice-versa
	final int x[] = new int[GAME_UNITS]; // x will hold all of the x coordinates of all body parts including the head
	final int y[] = new int[GAME_UNITS]; // y will hold all of the y coordinates of all body parts including the head
	int bodyParts = 6;
	int applesEaten;
	int appleX; //x coordinate where the apple will appear (randomly)
	int appleY; //y coordinate where the apple will appear (randomly)
	char direction = 'R';
	boolean running =false;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT)); //dimension enables acces to screen width and screen height
		this.setBackground(Color.black);
		this.setFocusable(true);  //sets the focus to the keyboard input
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running =true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {  // grid
		//drawing lines on x and y coordinate for better view
		if(running) {
		for (int i =0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); 
			g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
		}
		g.setColor(Color.red); //applle colour
		g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
		//draw the body
		for(int i=0;i< bodyParts;i++) {
			//draw the head - first element 0 is head
			if(i==0) {
				g.setColor(Color.green);
				g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
			}
			else { //now body
				g.setColor(new Color(45,180,0)); //RGB COLOUR - DARKER GREEN
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
		
		//score
		g.setColor(Color.ORANGE);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics = getFontMetrics(g.getFont()); //set the text in the middle of the screen
		g.drawString("Score:" + applesEaten, (SCREEN_WIDTH -metrics.stringWidth("Score:" + applesEaten))/2, g.getFont().getSize());
		
		}
		else {
			g.setColor(Color.red);
			for (int i =0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); 
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			gameOver(g);
			
			for(int i=0;i< bodyParts;i++) {
			g.setColor(Color.white);
			g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
		
	}//end method
		
		
	
	
	
	public void newApple() { //randomly generate a new apple every tune we begin the game or score a point / eat an apple
		appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE; // *UNIT_SIZE we want the apple to perfectly fill 1 slot/box/unit_size
		appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void move() {
		for (int i=bodyParts;i>0;i--) {
			x[i] = x[i-1]; //we're shifting all the elements in this array to the left
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] =y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] =y[0] + UNIT_SIZE;
			break;
		
		case 'L':
			x[0] =x[0] - UNIT_SIZE;
			break;
			
		case 'R':
			x[0] =x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if( (x[0] == appleX && y[0] == appleY) ) {
			newApple();
			bodyParts++;
			applesEaten++;
		}
	}
	
	public void checkCollisions() {
		
		//if head collides with the body
		for( int i=bodyParts;i>0;i--) {
			if( (x[0]) == x[i] && (y[0] == y[i]) ) { 
				running =false; //trigger GAMEOVER method
			}
		}
		//check if head touches LEFT border
		if(x[0]<0) {
			running =false;
		}
		//check if head touches RIGHT border
		if(x[0]>SCREEN_WIDTH) {
			running =false;
				}
		//check if head touches TOP border
		if(y[0]<0) {
			running =false;
				}
		//check if head touches BOTTOM border
		if(y[0]>SCREEN_HEIGHT) {
			running =false;
				}
		
		if(!running) {
			boolean collision =true;
			timer.stop();
			
		}
		
	}
	
	public void gameOver(Graphics g) {
		//Game Over text
		g.setColor(Color.ORANGE);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont()); //set the text in the middle of the screen
		g.drawString("Game Over", (SCREEN_WIDTH -metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
		//Score after game over text
		g.setColor(Color.ORANGE);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics2 = getFontMetrics(g.getFont()); //set the text in the middle of the screen
		g.drawString("Score:" + applesEaten, (SCREEN_WIDTH -metrics2.stringWidth("Score:" + applesEaten))/2, g.getFont().getSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){ // avoidintg the snake to "eat" itself - just 90 degrees moves
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
		case KeyEvent.VK_UP:
			if(direction != 'D') {
				direction = 'U';
			}
			break;
		case KeyEvent.VK_DOWN:
			if(direction != 'U') {
				direction = 'D';
			}
			break;
			}
		}
	}
}
