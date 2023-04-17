import javax.swing.JFrame;

public class GameFrame extends JFrame {  //frame of the app

	GameFrame(){
		
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack(); //* Sets the minimum size of this window to a constant
		this.setVisible(true); // * Makes the Window visible
		this.setLocationRelativeTo(null);//Avoid being placed off the edge of the screen:
	}
}
