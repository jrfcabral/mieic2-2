package labirinto.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.CardLayout;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface {

	private static final String MAIN_MENU = "Menu";
	private static final String BUILD = "Build";
	private static final String PLAY = "Play";
	private static final String TUTORIAL = "Tutorial";
	
	private JFrame frame;
	private JPanel menu;
	private JPanel labirinto;
	private JPanel editor;
	
	class MenuListener implements ActionListener {
		private String newCard;
		
		public MenuListener(String newCard){
			this.newCard = newCard;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {			
			if (frame.getLayout() != null && frame.getLayout() instanceof CardLayout){
				((CardLayout) frame.getLayout()).show(frame, newCard);;
				
			}
		}
		
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frame.setVisible(true);
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
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		initApplicationFrame();		
		initMenu();		
	}

	private void initApplicationFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Uma aventura no labirinto encantado");
		frame.getContentPane().setLayout(new CardLayout(0, 0));
	}

	private void initMenu() {
		menu = new JPanel();
		menu.setLayout(new BorderLayout());
		JLabel mazelbl = new JLabel();
		mazelbl.setText("Bem vindo à masmorra");
		
		menu.add(mazelbl, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton buttonTutorial = new JButton();
		JButton buttonPlay = new JButton();
		JButton buttonBuild = new JButton();
		
		buttonTutorial.addActionListener(new MenuListener(TUTORIAL));
		buttonPlay.addActionListener(new MenuListener(PLAY));
		buttonBuild.addActionListener(new MenuListener(BUILD));
		
		buttonTutorial.setText(TUTORIAL);
		buttonPlay.setText(PLAY);
		buttonBuild.setText("Maze builder");
		buttonPanel.add(buttonBuild);
		buttonPanel.add(buttonTutorial);
		buttonPanel.add(buttonPlay);
		menu.add(buttonPanel, BorderLayout.SOUTH);
		
		frame.add(menu, MAIN_MENU);
	}
}
