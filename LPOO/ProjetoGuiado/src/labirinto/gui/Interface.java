package labirinto.gui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import labirinto.logic.Labirinto;

import java.awt.BorderLayout;

public class Interface {
	
	private Labirinto labirinto;

	private JFrame frame;
	private JPanel panelLabirinto;

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
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public Interface() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		labirinto = new Labirinto();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
	
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(null);
		frame.setPreferredSize(new Dimension(1000,500));
		
		panelLabirinto = new PainelLabirinto(labirinto);		
		panelLabirinto.setBounds(0, 0, 450, 300);
		Dimension d = new Dimension(frame.getWidth(), frame.getHeight());
		panelLabirinto.setPreferredSize(d);
		frame.getContentPane().add(panelLabirinto);
		frame.pack();
		
		
	}

}
