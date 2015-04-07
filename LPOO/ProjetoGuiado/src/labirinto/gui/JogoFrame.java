package labirinto.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import labirinto.logic.Direcao;
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
	private JButton saveButton;
	private JButton loadButton;
	private JFileChooser fileChooser;
	
	private JogoPanel jogoPanel;
	
	
	private class JogoPanel extends JPanel{

		private static final long serialVersionUID = 573437742923541828L;
		
		public static final String OPCOES = "OPCOES";
		public static final String PLAY = "PLAY";
		public static final String EMPTY = "EMPTY";
		
		private Labirinto masmorra;		
		
		private JPanel emptyPanel;
		private JPanel playPanel;		
		private JPanel opcoesPanel;
		
		private JLabel dimensaoLabel;
		private JSlider dimensaoSlider;
		private JLabel dragoesLabel;
		private JSlider dragoesSlider;
		private JLabel estrategiaLabel;
		private JComboBox<Estrategia> estrategiaBox;
		private JTextField moveCimaField;
		private JTextField moveBaixoField;
		private JTextField moveEsquerdaField;
		private JTextField moveDireitaField;
		private JTextField atiraCimaField;
		private JTextField atiraBaixoField;
		private JTextField atiraEsquerdaField;
		private JTextField atiraDireitaField;
		
		private BufferedImage floorTile;
		private BufferedImage heroTile;
		private BufferedImage dragonTile;
		private BufferedImage swordTile;
		private BufferedImage shieldTile;
		private BufferedImage javTile;

		private class LabirintoMoveAction extends AbstractAction{

			private static final long serialVersionUID = 7244590807255937010L;
			private Direcao direcao;
			
			@Override
			public void actionPerformed(ActionEvent e) {


				JogoPanel.this.masmorra.move(direcao);							
				JogoPanel.this.criaPlayPanel();				
				((CardLayout)JogoPanel.this.getLayout()).show(JogoPanel.this, PLAY);

			}
			public LabirintoMoveAction(Direcao dir){
				super();
				direcao = dir;
			}
			
		}
		

		private class LabirintoAtiraAction extends AbstractAction{

			
			private static final long serialVersionUID = 3131845997347731773L;
			private Direcao direcao;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JogoPanel.this.masmorra.atiraDardo(direcao);						
				JogoPanel.this.criaPlayPanel();				
				((CardLayout)JogoPanel.this.getLayout()).show(JogoPanel.this, PLAY);

			}
			public LabirintoAtiraAction(Direcao dir){
				super();
				direcao = dir;
			}
			
		}
		

		public class ImagePanel extends JPanel{

			private static final long serialVersionUID = 1L;
			private Image tile;
			
			public ImagePanel(){
				super();
			}
			
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				g.drawImage(tile, 0, 0, null);
			}
			
			public void setImg(Image img){
				tile = img;
			}
		}
		
		
		public JogoPanel() throws IOException{
			super();
			setLayout(new CardLayout());
			
			criaPanels();			
		}
		public void change(String mode) {
			if (mode == PLAY){
				refazLabirinto();
				criaPlayPanel();
				JogoFrame.this.novoJogoButton.setEnabled(false);
				JogoFrame.this.saveButton.setEnabled(true);
				JogoFrame.this.loadButton.setEnabled(true);
			}
			else{
				JogoFrame.this.novoJogoButton.setEnabled(true);
				JogoFrame.this.saveButton.setEnabled(false);
				JogoFrame.this.loadButton.setEnabled(false);
			}
			((CardLayout)getLayout()).show(this, mode);
			JogoFrame.this.pack();
		}

		private void criaPanels() throws IOException {
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
			
			JLabel teclasLabel = new JLabel("Teclas");						
			teclasLabel.setHorizontalAlignment(SwingConstants.CENTER);
			opcoesPanel.add(teclasLabel);
			
			
			JPanel teclasPanel = new JPanel();
			teclasPanel.setLayout(new GridLayout(4,2));
			teclasPanel.setPreferredSize(new Dimension(20, 20));
			
			moveCimaField = new JTextField("w");
			moveBaixoField = new JTextField("s");
			moveEsquerdaField = new JTextField("a");
			moveDireitaField = new JTextField("d");
			teclasPanel.add(moveCimaField);
			teclasPanel.add(moveBaixoField);
			teclasPanel.add(moveEsquerdaField);
			teclasPanel.add(moveDireitaField);
			
			atiraCimaField = new JTextField("u");
			atiraBaixoField = new JTextField("j");
			atiraEsquerdaField = new JTextField("h");
			atiraDireitaField = new JTextField("k");
			teclasPanel.add(atiraCimaField);
			teclasPanel.add(atiraBaixoField);
			teclasPanel.add(atiraEsquerdaField);
			teclasPanel.add(atiraDireitaField);
			
			opcoesPanel.add(teclasPanel);
			
			add(opcoesPanel, OPCOES);			
			
		}

		protected void refazLabirinto() {
			masmorra = new Labirinto(MazeGenerator.generate(dimensaoSlider.getValue()), dimensaoSlider.getValue(), dragoesSlider.getValue(), (Estrategia)estrategiaBox.getSelectedItem());
				
		}
		private void criaPlayPanel() {
			try{
			floorTile = ImageIO.read(new File("bin/labirinto/resources/images/floortile3.png").getCanonicalFile());
			dragonTile = ImageIO.read(new File("bin/labirinto/resources/images/dragontile.png").getCanonicalFile());
			heroTile = ImageIO.read(new File("bin/labirinto/resources/images/herotile.png").getCanonicalFile());
			shieldTile = ImageIO.read(new File("bin/labirinto/resources/images/shieldtile.png").getCanonicalFile());
			swordTile = ImageIO.read(new File("bin/labirinto/resources/images/swordtile.png").getCanonicalFile());
			javTile = ImageIO.read(new File("bin/labirinto/resources/images/javtile.png").getCanonicalFile());
			}
			catch(IOException e){
				e.printStackTrace();
				System.exit(-1);
			}
			
			playPanel = new JPanel();
			playPanel.setLayout(new GridLayout(dimensaoSlider.getValue(), dimensaoSlider.getValue()));

			drawMaze();
			atualizaKeybindings();
			
			JogoFrame.this.pack();
			
			this.add(playPanel, PLAY);
			
		}
		private void atualizaKeybindings() {
			playPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(moveCimaField.getText().toUpperCase()), "movecima");
			playPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(moveBaixoField.getText().toUpperCase()), "movebaixo");
			playPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(moveEsquerdaField.getText().toUpperCase()), "moveesquerda");
			playPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(moveDireitaField.getText().toUpperCase()), "movedireita");
			playPanel.getActionMap().put("movecima", new LabirintoMoveAction(Direcao.CIMA));
			playPanel.getActionMap().put("movebaixo", new LabirintoMoveAction(Direcao.BAIXO));
			playPanel.getActionMap().put("moveesquerda", new LabirintoMoveAction(Direcao.ESQUERDA));
			playPanel.getActionMap().put("movedireita", new LabirintoMoveAction(Direcao.DIREITA));
			
			
			playPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(atiraCimaField.getText().toUpperCase()), "atiracima");
			playPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(atiraBaixoField.getText().toUpperCase()), "atirabaixo");
			playPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(atiraEsquerdaField.getText().toUpperCase()), "atiraesquerda");
			playPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(atiraDireitaField.getText().toUpperCase()), "atiradireita");
			playPanel.getActionMap().put("atiracima", new LabirintoAtiraAction(Direcao.CIMA));
			playPanel.getActionMap().put("atirabaixo", new LabirintoAtiraAction(Direcao.BAIXO));
			playPanel.getActionMap().put("atiraesquerda", new LabirintoAtiraAction(Direcao.ESQUERDA));
			playPanel.getActionMap().put("atiradireita", new LabirintoAtiraAction(Direcao.DIREITA));
		}
		
		public void drawMaze(){
			for (int j = 0; j < dimensaoSlider.getValue(); j++)
				for (int i = 0; i < dimensaoSlider.getValue(); i++){
					ImagePanel cell = new ImagePanel();
					cell.setPreferredSize(new Dimension(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue()));
					cell.setOpaque(true);
					char cellSymbol = masmorra.getCellSymbol(new Posicao(j, i));
					
					switch (cellSymbol){
					case 'X':
						cell.setBackground(Color.BLACK);
						break;
					case ' ':
						cell.setImg(floorTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));
						break;
					case 'S':
						cell.setBackground(Color.GREEN);
						break;
					case 'D':
						cell.setImg(dragonTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));
						break;
					case 'H': cell.setToolTipText("Heroi");cell.setImg(heroTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));break;
					case '@': cell.setToolTipText("Heroi com escudo");cell.setImg(heroTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));break;
					case 'A': cell.setToolTipText("Heroi com espada");cell.setImg(heroTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));break;
					case 'R': cell.setToolTipText("Heroi com dardo");cell.setImg(heroTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));break;
					case '$': cell.setToolTipText("Heroi com dardo e escudo");cell.setImg(heroTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));break;
					case '&': cell.setToolTipText("Heroi com espada e escudo");cell.setImg(heroTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));break;
						
					case 'J':
						cell.setImg(javTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));
						break;
					case 'E':
						cell.setImg(swordTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));
						break;
					case 'P':
						cell.setImg(shieldTile.getScaledInstance(800/dimensaoSlider.getValue(), 800/dimensaoSlider.getValue(), Image.SCALE_SMOOTH));
						break;
					}
					
					playPanel.add(cell);
				}
			
		}

	}
	
	
	public JogoFrame() throws IOException
	{
		setTitle("As Aventuras de Sir McMataDragalhões");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 100);
		setResizable(false);		
		setFocusable(true);
		getContentPane().setLayout(new BorderLayout());
		
		fileChooser = new JFileChooser();		
		criaButoesPanel();
		criaButoes();		
		criaJogoPanel();
		
	}
	
	private void criaJogoPanel() throws IOException {
		jogoPanel = new JogoPanel();
		getContentPane().add(jogoPanel, BorderLayout.CENTER);
		
	}

	private void criaButoes() {
		opcoesButton = new JButton("Opções");
		novoJogoButton = new JButton("Novo Jogo");
		sairButton = new JButton("Sair");
		saveButton = new JButton("Save");
		saveButton.setEnabled(false);
		loadButton = new JButton("Load");
		loadButton.setEnabled(false);
		
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
		
		saveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showSaveDialog(JogoFrame.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION)
					try {
						JogoFrame.this.jogoPanel.masmorra.saveState(fileChooser.getSelectedFile().getCanonicalPath());
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(JogoFrame.this, "Erro ao guardar o labirinto atual!");
					}				
			}			
		});
		
		loadButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(JogoFrame.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION)
					try {
						JogoFrame.this.jogoPanel.masmorra = JogoFrame.this.jogoPanel.masmorra.loadState(fileChooser.getSelectedFile().getCanonicalPath());
						JogoFrame.this.jogoPanel.criaPlayPanel();
					} catch ( IOException e1) {						
						e1.printStackTrace();
						JOptionPane.showMessageDialog(JogoFrame.this, "Erro ao carregar o labirinto atual!");
					}
					
				
			}
			
		});
		botoesPanel.add(novoJogoButton);
		botoesPanel.add(opcoesButton);		
		botoesPanel.add(sairButton);
		botoesPanel.add(saveButton);
		botoesPanel.add(loadButton);
	}

	private void criaButoesPanel()
	{
		botoesPanel = new JPanel();
		botoesPanel.setLayout(new FlowLayout());
		getContentPane().add(botoesPanel, BorderLayout.SOUTH);
	}
	
}
