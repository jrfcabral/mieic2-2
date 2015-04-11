package labirinto.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import labirinto.logic.*;


public class JogoFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel botoesPanel;
	private JButton opcoesButton;
	private JButton novoJogoButton;
	private JButton sairButton;
	private JButton saveButton;
	private JButton loadButton;
	private JButton mazeBuilderButton;
	private JFileChooser fileChooser;
	private JToolBar terrainPicker;
	
	private JogoPanel jogoPanel;
	
	public enum EditorSelection{WALL, FREE, HERO, DRAGON, SWORD, JAVELIN, SHIELD}
	
	private class JogoPanel extends JPanel implements GridTransformer{

		private static final long serialVersionUID = 573437742923541828L;		
		
		public static final String OPCOES = "OPCOES";
		public static final String PLAY = "PLAY";
		public static final String EMPTY = "EMPTY";
		public static final String BUILDER = "BUILDER";
		
		private Labirinto masmorra;
		private JogoFrame.EditorSelection terrainChoice;
		
		private JPanel emptyPanel;
		private GridPanel<Character> playPanel;		
		private JPanel opcoesPanel;
		private GridPanel<Character> mazeBuilderPanel;
	
		
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
		
		private ConcurrentHashMap<Character, Image> labirintoImages;
		
		
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
		

		public JogoPanel() throws IOException{
			super();
			
			setLayout(new CardLayout());			
			carregaImagens();			
			criaPanels();			
			this.terrainChoice = JogoFrame.EditorSelection.HERO;
		}
		

		/**
		 * <p>Loads the necessary images to memory.</p><p> Aborts execution if the images can't be read.</p>
		 */
		private void carregaImagens() {
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
			labirintoImages = new ConcurrentHashMap<Character, Image>();
			labirintoImages.put(' ', floorTile);
			labirintoImages.put('D', dragonTile);
			labirintoImages.put('d', dragonTile);
			labirintoImages.put('F', dragonTile);
			labirintoImages.put('E', swordTile);
			labirintoImages.put('H', heroTile);
			labirintoImages.put('A', heroTile);
			labirintoImages.put('@', heroTile);
			labirintoImages.put('&', heroTile);
			labirintoImages.put('$', heroTile);
			labirintoImages.put('R', heroTile);
			labirintoImages.put('J', javTile);
			labirintoImages.put('P', shieldTile);
			
			BufferedImage greenImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D ig2 = greenImage.createGraphics();
			ig2.setBackground(Color.GREEN);
			ig2.clearRect(0, 0, 1, 1);
			
			labirintoImages.put('S', greenImage);			
		}
		
		
		/**
		 * <p>Changes the current panel being show to that of given by mode. 
		 * Enables or disables all context-sensitive interface functionality.</p>
		 * @param mode the new application mode to be shown. Can be <code>PLAY</code>, <code>OPCOES</code>, <code>EMPTY</code> or <code>BUILDER</code> 
		 */
		public void change(String mode) {
			if (mode == PLAY){
				if (masmorra == null)
					refazLabirinto();
				criaPlayPanel();
				JogoFrame.this.novoJogoButton.setEnabled(false);
				JogoFrame.this.saveButton.setEnabled(true);
				JogoFrame.this.loadButton.setEnabled(true);
			}
			else if (mode == BUILDER){
				if (masmorra == null)
					refazLabirinto();
				mazeBuilderPanel.setGrid(masmorra);
				mazeBuilderPanel.updateGrid();
				JogoFrame.this.novoJogoButton.setEnabled(true);
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
		
		/**
		 * Transforms the grid given by <code>x, y</code> according to the currently chosen tool
		 * @param x
		 * @param y
		 */		
		public void transform(int x, int y){
			System.out.println("yep"+x+","+y);
			
			switch (terrainChoice){
			case WALL:masmorra.setTerreno(Terreno.PAREDE, x, y);break;
			case FREE: masmorra.setTerreno(Terreno.CHAO, x, y);break;
			case SWORD: masmorra.setEspadaPosicao(x, y);break;
			case HERO: masmorra.setHeroiPosicao(x, y);break;			
			default:
				break;
				
			}
			this.change(BUILDER);
		}
		
		/**
		 * <p>Initializes all of the panels that comprise the JogoPanel interface</p>		 
		 */
		private void criaPanels() {
			emptyPanel = new JPanel();
			add(emptyPanel, EMPTY);
			criaOpcoesPanel();
			refazLabirinto();
			criaPlayPanel();
			criaMazeBuilderPanel();			
		}
		
		/**
		 * Initializes the maze builder panel
		 */
		private void criaMazeBuilderPanel() {
			mazeBuilderPanel = new GridPanel<Character>( masmorra, this);
			mazeBuilderPanel.updateGrid();
			mazeBuilderPanel.paintImmediately(0,0,mazeBuilderPanel.getWidth(), mazeBuilderPanel.getHeight());
			setPreferredSize(new Dimension(GridPanel.GRID_PANEL_DIMENSION,GridPanel.GRID_PANEL_DIMENSION));
			mazeBuilderPanel.setImageMap(labirintoImages);						
			add(mazeBuilderPanel, BUILDER);			
		}


		/**
		 * <p>Initializes and configures all the elements of the interface that allow the user to customize the game.</p>
		 */
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
			opcoesPanel.setPreferredSize(new Dimension(400, 400));
			
			add(opcoesPanel, OPCOES);			
			
		}

		/**
		 * <p>Initializes the maze according to the currently selected user options.</p>
		 */
		protected void refazLabirinto() {
			masmorra = new Labirinto(MazeGenerator.generate(dimensaoSlider.getValue()), dimensaoSlider.getValue(), dragoesSlider.getValue(), (Estrategia)estrategiaBox.getSelectedItem());
				
		}
		
		/**
		 * Prepares the grid display of the labirinth 
		 */
		private void criaPlayPanel() {			
			
			playPanel = new GridPanel<Character>(masmorra, this);
			playPanel.setImageMap(labirintoImages);
			playPanel.updateGrid();
			
			
			atualizaKeybindings();
			
			JogoFrame.this.pack();
			
			this.add(playPanel, PLAY);
			
		}
		
		/**
		 * <p>Sets up keybindings according to user selected options.</p> <p>Subsequent calls refresh said keybindings</p> 
		 */
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
		terrainPicker = new JToolBar();
		terrainPicker.add(new JButton("this"));
		this.add(terrainPicker, BorderLayout.WEST);
	}
	
	private void criaJogoPanel() throws IOException {
		jogoPanel = new JogoPanel();
		getContentPane().add(jogoPanel, BorderLayout.CENTER);
		
	}

	/**
	 * Sets up all the buttons in the game and assigns them their respective actions
	 */
	private void criaButoes() {
		opcoesButton = new JButton("Opções");
		novoJogoButton = new JButton("Novo Jogo");
		sairButton = new JButton("Sair");
		saveButton = new JButton("Save");
		saveButton.setEnabled(false);
		loadButton = new JButton("Load");
		loadButton.setEnabled(false);
		mazeBuilderButton = new JButton("Maze Builder");		
		
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
						JogoFrame.this.jogoPanel.masmorra = Labirinto.loadState(fileChooser.getSelectedFile().getCanonicalPath());
						JogoFrame.this.jogoPanel.criaPlayPanel();
						JogoFrame.this.jogoPanel.change(JogoPanel.PLAY);
					} catch ( IOException e1) {						
						JOptionPane.showMessageDialog(JogoFrame.this, "O ficheiro selecionado não existe ou não pode ser escrito!");
					}
					
				
			}
			
		});
		
		mazeBuilderButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				jogoPanel.change(JogoPanel.BUILDER);
				
			}
			
		});
		botoesPanel.add(novoJogoButton);
		botoesPanel.add(opcoesButton);		
		botoesPanel.add(sairButton);
		botoesPanel.add(saveButton);
		botoesPanel.add(loadButton);
		botoesPanel.add(mazeBuilderButton);
	}

	/**
	 * Creates the panel to hold all the games' buttons.
	 */
	private void criaButoesPanel()
	{
		botoesPanel = new JPanel();
		botoesPanel.setLayout(new FlowLayout());
		getContentPane().add(botoesPanel, BorderLayout.SOUTH);
	}
	
}
