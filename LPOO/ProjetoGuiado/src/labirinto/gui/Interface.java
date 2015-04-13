package labirinto.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Class responsible for initializing the GUI
 *
 */
public class Interface {

	private static final String MAIN_MENU = "Menu";
	private static final String BUILD = "Build";
	private static final String PLAY = "Play";
	private static final String TUTORIAL = "Tutorial";
	
	private JFrame frame;
	private JPanel mainMenu;
	private JPanel playMenu;
	class MenuListener implements ActionListener {
		private String newCard;
		
		public MenuListener(String newCard){
			this.newCard = newCard;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {			
			if (frame.getContentPane().getLayout() != null && frame.getContentPane().getLayout() instanceof CardLayout){
				((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), newCard);;
				
			}
			else
				throw new UnsupportedOperationException();
		}
		
	}
	class PositiveIntegerFilter extends DocumentFilter{
		
		public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws BadLocationException
		{
			System.out.println("s");
			try{
				Integer.parseInt(string);
			}catch (NumberFormatException e){return;}
			super.insertString(fb, offset, string, attr);
			
		}
		
		@Override
		public void replace(FilterBypass fb, int offset, int length, String string, javax.swing.text.AttributeSet attr) throws BadLocationException
		{			
			try{
				Integer.parseInt(string);
			}catch (NumberFormatException e){return;}
			super.replace(fb, offset, length, string, attr);
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JogoFrame window = new JogoFrame();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the main interface
	 */
	private void initialize() {
		initApplicationFrame();		
		initMenu();	
		initPlayMenu();
		
		
	}

	
	/**
	 * Initialize the application frame
	 */
	private void initApplicationFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Uma aventura no labirinto encantado");
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
	}
	
	

	/**
	 * Initialize the Labyrinth
	 */
	private void initPlayMenu() {
		playMenu = new JPanel();
		playMenu.setLayout(new BorderLayout());
		
		
		
		
		frame.getContentPane().add(playMenu, PLAY);		
	}
	
	/**
	 * Initialize the menu
	 */
	private void initMenu() {
		mainMenu = new JPanel();
		mainMenu.setLayout(new BorderLayout());
		JLabel mazelbl = new JLabel();
		mazelbl.setText("Bem vindo à masmorra");
		mazelbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		mainMenu.add(mazelbl, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton buttonTutorial = new JButton();
		JButton buttonPlay = new JButton();
		JButton buttonBuild = new JButton();
		
		buttonTutorial.addActionListener(new MenuListener(TUTORIAL));
		buttonPlay.addActionListener(new MenuListener(PLAY));
		buttonBuild.addActionListener(new MenuListener(BUILD));
		
		buttonTutorial.setText("Tutorial");
		buttonPlay.setText("Play");
		buttonBuild.setText("Maze builder");
		buttonPanel.add(buttonBuild);
		buttonPanel.add(buttonTutorial);
		buttonPanel.add(buttonPlay);
		mainMenu.add(buttonPanel, BorderLayout.SOUTH);
		
		frame.add(mainMenu, MAIN_MENU);
	}
}
