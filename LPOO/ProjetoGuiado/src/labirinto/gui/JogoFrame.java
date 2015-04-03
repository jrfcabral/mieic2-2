package labirinto.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import labirinto.logic.Estrategia;
import labirinto.logic.Labirinto;
import labirinto.logic.MazeGenerator;
import labirinto.logic.Posicao;

public class JogoFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel botoesPanel;
	private JButton opcoesButton;
	private JButton novoJogoButton;
	private JButton sairButton;
	
	private JogoPanel jogoPanel;
	
	
	
	
	
	class JogoPanel extends JPanel{

		private static final long serialVersionUID = 573437742923541828L;
		
		public static final String OPCOES = "OPCOES";
		public static final String PLAY = "PLAY";
		public static final String EMPTY = "EMPTY";
		
		private Labirinto labirinto;
		
		private JPanel emptyPanel;
		private JPanel playPanel;
		
		private JPanel opcoesPanel;
		private JLabel dimensaoLabel;
		private JSlider dimensaoSlider;
		private JLabel dragoesLabel;
		private JSlider dragoesSlider;
		private JLabel estrategiaLabel;
		private JComboBox<Estrategia> estrategiaBox;
		
		
		public JogoPanel(){
			super();
			setLayout(new CardLayout());
			criaPanels();			
		}
		public void change(String mode){
			if (mode == PLAY){
				refazLabirinto();
				criaPlayPanel();
				JogoFrame.this.novoJogoButton.setEnabled(false);
			}
			else
				JogoFrame.this.novoJogoButton.setEnabled(true);
			((CardLayout)getLayout()).show(this, mode);
			JogoFrame.this.pack();
		}

		private void criaPanels() {
			emptyPanel = new JPanel();
			add(emptyPanel, EMPTY);
			criaOpcoesPanel();
			refazLabirinto();
			criaPlayPanel();
		}

		

		private void criaOpcoesPanel() {
			opcoesPanel = new JPanel();
			opcoesPanel.setLayout(new GridLayout(0, 1));
			
			JPanel dimensaoPanel = new JPanel();
			dimensaoPanel.setLayout(new FlowLayout());
			dimensaoLabel = new JLabel("Dimensão:");
			dimensaoSlider = new JSlider();
			dimensaoSlider.setMinimum(5);
			dimensaoSlider.setMaximum(41);
			dimensaoSlider.setMajorTickSpacing(5);
			dimensaoSlider.setMinorTickSpacing(2);
			dimensaoSlider.setValue(15);
			dimensaoSlider.setSnapToTicks(true);
			dimensaoSlider.setPaintLabels(true);
			dimensaoPanel.add(dimensaoLabel);
			dimensaoPanel.add(dimensaoSlider);
			opcoesPanel.add(dimensaoPanel);
			
			JPanel dragoesPanel = new JPanel();
			dimensaoPanel.setLayout(new FlowLayout());
			dragoesLabel = new JLabel("Dragoes:");
			dragoesSlider = new JSlider();
			dragoesSlider.setMinimum(1);
			dragoesSlider.setMaximum(10);
			dragoesSlider.setMajorTickSpacing(2);
			dragoesSlider.setMinorTickSpacing(1);
			dragoesSlider.setValue(1);
			dragoesSlider.setSnapToTicks(true);
			dragoesSlider.setPaintLabels(true);
			dragoesPanel.add(dragoesLabel);
			dragoesPanel.add(dragoesSlider);
			opcoesPanel.add(dragoesPanel);
			
			JPanel estrategiaPanel = new JPanel();
			estrategiaLabel = new JLabel("Estratégia:");
			estrategiaBox = new JComboBox<Estrategia>();
			estrategiaBox.addItem(Estrategia.ALTERNADO);
			estrategiaBox.addItem(Estrategia.PARADO);
			estrategiaBox.addItem(Estrategia.SEMPRE_ACORDADO);
			estrategiaPanel.add(estrategiaLabel);
			estrategiaPanel.add(estrategiaBox);
			opcoesPanel.add(estrategiaPanel);
			
			add(opcoesPanel, OPCOES);			
			
		}

		protected void refazLabirinto() {
			labirinto = new Labirinto(MazeGenerator.generate(dimensaoSlider.getValue()), dimensaoSlider.getValue(), dragoesSlider.getValue(), (Estrategia)estrategiaBox.getSelectedItem());
				
		}
		private void criaPlayPanel() {
			playPanel = new JPanel();
			playPanel.setLayout(new GridLayout(dimensaoSlider.getValue(), dimensaoSlider.getValue()));
			for (int j = 0; j < dimensaoSlider.getValue(); j++)
				for (int i = 0; i < dimensaoSlider.getValue(); i++){
					JPanel cell = new JPanel();
					cell.setPreferredSize(new Dimension(50, 50));
					cell.setOpaque(true);
					char cellSymbol = labirinto.getCellSymbol(new Posicao(i, j));
					
					switch (cellSymbol){
					case 'X':
						cell.setBackground(Color.BLACK);
						break;
					case ' ':
						cell.setBackground(Color.CYAN);
						break;
					case 'S':
						cell.setBackground(Color.GREEN);
					}
					
					playPanel.add(cell);
				}
			
			
			
			JogoFrame.this.pack();
			
			this.add(playPanel, PLAY);
			
		}
		
	}
	
	
	public JogoFrame()
	{
		setTitle("As Aventuras de Sir McMataDragalhões");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 100);
		setResizable(false);
		setLayout(new BorderLayout());
		
		criaButoesPanel();
		criaButoes();
		
		criaJogoPanel();
		
	}
	
	private void criaJogoPanel() {
		jogoPanel = new JogoPanel();
		add(jogoPanel, BorderLayout.CENTER);
		
	}

	private void criaButoes() {
		opcoesButton = new JButton("Opções");
		novoJogoButton = new JButton("Novo Jogo");
		sairButton = new JButton("Sair");
		
		opcoesButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				jogoPanel.change(JogoPanel.OPCOES);
				
			}
			
		});
		
		novoJogoButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				jogoPanel.change(JogoPanel.PLAY);
				
			}
			
		});
		
		sairButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
			
		});
		
		
		botoesPanel.add(novoJogoButton);
		botoesPanel.add(opcoesButton);		
		botoesPanel.add(sairButton);
	}

	private void criaButoesPanel()
	{
		botoesPanel = new JPanel();
		botoesPanel.setLayout(new FlowLayout());
		this.add(botoesPanel, BorderLayout.SOUTH);
	}
	
}
