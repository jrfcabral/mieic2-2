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

/**
 * Class that comprises most of the GUI
 *
 */
public class JogoFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel botoesPanel;
	private JButton opcoesButton;
	private JButton novoLabirintoButton;
	private JButton playModeButton;
	private JButton sairButton;
	private JButton saveButton;
	private JButton loadButton;
	private JButton mazeBuilderButton;
	private JFileChooser fileChooser;
	private JToolBar terrainPicker;
	
	private JogoPanel jogoPanel;
	
	public enum EditorSelection{WALL, FREE, HERO, DRAGON, SWORD, JAVELIN, SHIELD, EXIT;

	public static EditorSelection fromString(String text) {
		switch (text){
		case "Wall":return WALL;
		case "Floor":return FREE;
		case "Hero":return HERO;
		case "Javelin":return JAVELIN;
		case "Dragon": return DRAGON;
		case "Sword": return SWORD;
		case "Shield": return SHIELD;
		case "Exit": return EXIT;
		
		}
		return WALL;
			
	}}
	

	private class JogoPanel extends JPanel implements GridTransformer{

		private static final long serialVersionUID = 573437742923541828L;		
		
		public static final String OPCOES = "OPCOES";
		public static final String PLAY = "PLAY";
		public static final String EMPTY = "EMPTY";
		public static final String BUILDER = "BUILDER";
		public static final String WIN = "WIN";
		public static final String LOSE = "LOSE";
		
		private Labirinto masmorra;
		private JogoFrame.EditorSelection terrainChoice;
		
		private JPanel emptyPanel;
		private GridPanel<Character> playPanel;		
		private JPanel opcoesPanel;
		private GridPanel<Character> mazeBuilderPanel;
		private ImagePanel WinPanel;
		private ImagePanel LosePanel;
		private boolean builderPanelActive = false;	
		
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
		private BufferedImage javhero;
		private BufferedImage javshieldhero;
		private BufferedImage swordshieldhero;
		private BufferedImage swordhero;
		private BufferedImage shieldhero;
		private BufferedImage sleepingDargon;
		private ConcurrentHashMap<Character, Image> labirintoImages;
		
		
		private class LabirintoMoveAction extends AbstractAction{

			private static final long serialVersionUID = 7244590807255937010L;
			private Direcao direcao;
			
			@Override
			public void actionPerformed(ActionEvent e) {


				JogoPanel.this.masmorra.move(direcao);
				if(JogoPanel.this.masmorra.isAcabou()){
					if(JogoPanel.this.masmorra.isPerdeu()){
						jogoPanel.change(LOSE);
						return;
					}
					else{
						//System.out.println("You Wonered!");
						jogoPanel.change(JogoPanel.WIN);
						return;
						//System.out.println("You Wonered!");
					}
				}
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
				swordhero = ImageIO.read(new File("bin/labirinto/resources/images/swordhero.png").getCanonicalFile());
				swordshieldhero = ImageIO.read(new File("bin/labirinto/resources/images/swordshieldhero.png").getCanonicalFile());
				javshieldhero = ImageIO.read(new File("bin/labirinto/resources/images/javshieldhero.png").getCanonicalFile());
				ImageIO.read(new File("bin/labirinto/resources/images/sworddragontile.png").getCanonicalFile());
				javhero = ImageIO.read(new File("bin/labirinto/resources/images/javhero.png").getCanonicalFile());
				shieldhero = ImageIO.read(new File("bin/labirinto/resources/images/shieldhero.png").getCanonicalFile());
				sleepingDargon = ImageIO.read(new File("bin/labirinto/resources/images/sleepdragontile.png").getCanonicalFile());
				}
				catch(IOException e){
					e.printStackTrace();
					System.exit(-1);
				}
			labirintoImages = new ConcurrentHashMap<Character, Image>();
			labirintoImages.put(' ', floorTile);
			labirintoImages.put('D', dragonTile);
			labirintoImages.put('d', sleepingDargon);
			labirintoImages.put('F', dragonTile);
			labirintoImages.put('E', swordTile);
			labirintoImages.put('H', heroTile);
			labirintoImages.put('A', swordhero);
			labirintoImages.put('@', shieldhero);
			labirintoImages.put('&', swordshieldhero);
			labirintoImages.put('$', javshieldhero);
			labirintoImages.put('R', javhero);
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
				JogoFrame.this.playModeButton.setEnabled(false);
				JogoFrame.this.saveButton.setEnabled(true);
				JogoFrame.this.loadButton.setEnabled(true);
				JogoFrame.this.mazeBuilderButton.setEnabled(true);
			}
			else if (mode == BUILDER){
				if (masmorra == null)
					refazLabirinto(); 
				mazeBuilderPanel.setGrid(masmorra);
				mazeBuilderPanel.updateGrid();
				JogoFrame.this.playModeButton.setEnabled(true);
				JogoFrame.this.saveButton.setEnabled(true);
				JogoFrame.this.loadButton.setEnabled(true);
				terrainPicker.setVisible(true);
				builderPanelActive = true;
				mazeBuilderButton.setEnabled(false);
			}
			else{
				JogoFrame.this.playModeButton.setEnabled(true);
				JogoFrame.this.saveButton.setEnabled(false);
				JogoFrame.this.loadButton.setEnabled(false);
			}
			
			if (mode != BUILDER){
				terrainPicker.setVisible(false);
				builderPanelActive = false;
			}
			
			if (mode == WIN || mode == LOSE){
				playModeButton.setEnabled(false);
				mazeBuilderButton.setEnabled(false);
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
			
			if (!builderPanelActive)
				return;
			
			switch (terrainChoice){
			case WALL:masmorra.setTerreno(Terreno.PAREDE, x, y);break;
			case FREE: masmorra.setTerreno(Terreno.CHAO, x, y);break;
			case SWORD: masmorra.setEspadaPosicao(x, y);break;
			case HERO: masmorra.setHeroiPosicao(x, y);break;
			case DRAGON: masmorra.toggleDragon(x, y);break;
			case JAVELIN: masmorra.toggleJavelin(x, y);break;
			case SHIELD: masmorra.setShieldPosition(x,y); break;
			case EXIT: masmorra.setTerreno(Terreno.SAIDA, x, y);
			default:
				break;
				
			}
			this.change(BUILDER);
		}
		
		/**
		 * <p>Initializes all of the panels that comprise the JogoPanel interface</p>		 
		 * @throws IOException 
		 */
		private void criaPanels() throws IOException {
			emptyPanel = new JPanel();
			add(emptyPanel, EMPTY);
			criaOpcoesPanel();
			refazLabirinto();
			criaPlayPanel();
			criaMazeBuilderPanel();
			criaWinPanel();
			criaLosePanel();
		}
		
		/**
		 * Initializes the Win panel
		 * @throws IOException 
		 */
		public void criaWinPanel() throws IOException{
			WinPanel = new ImagePanel();
			WinPanel.setImage(ImageIO.read(new File("bin/labirinto/resources/images/youwon.png")).getScaledInstance(700, 750, Image.SCALE_SMOOTH));
			add(WinPanel, WIN);
		}
		
		/**
		 * Initializes the Lose Panel
		 * @throws IOException 
		 */
		public void criaLosePanel() throws IOException{
			LosePanel = new ImagePanel();
			LosePanel.setImage(ImageIO.read(new File("bin/labirinto/resources/images/youlost.png")).getScaledInstance(700, 700, Image.SCALE_SMOOTH));
			add(LosePanel, LOSE);
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
			dimensaoLabel = new JLabel("Size:");
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
			dragoesLabel = new JLabel("Number of Dragons:");
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
			estrategiaLabel = new JLabel("Strategy:");
			estrategiaBox = new JComboBox<Estrategia>();
			estrategiaBox.addItem(Estrategia.ALTERNADO);
			estrategiaBox.addItem(Estrategia.PARADO);
			estrategiaBox.addItem(Estrategia.SEMPRE_ACORDADO);
			estrategiaPanel.add(estrategiaLabel);
			estrategiaPanel.add(estrategiaBox);
			opcoesPanel.add(estrategiaPanel);
			
			JLabel teclasLabel = new JLabel("Keys");						
			teclasLabel.setHorizontalAlignment(SwingConstants.CENTER);
			opcoesPanel.add(teclasLabel);
			
			
			JPanel teclasPanel = new JPanel();
			teclasPanel.setLayout(new GridLayout(4,2));
			teclasPanel.setPreferredSize(new Dimension(20, 20));
			
			JPanel moveUpPanel = new JPanel();
			moveUpPanel.setLayout(new GridLayout(1, 2));
			JLabel moverCimaLbl = new JLabel("Move up");
			moveCimaField = new JTextField("w");
			moveUpPanel.add(moverCimaLbl);
			moveUpPanel.add(moveCimaField);
			
			JPanel moveDownPanel = new JPanel();
			moveDownPanel.setLayout(new GridLayout(1, 2));
			JLabel moveDownLbl = new JLabel("Move Down");
			moveBaixoField = new JTextField("s");
			moveDownPanel.add(moveDownLbl);
			moveDownPanel.add(moveBaixoField);
			
			JPanel moveLeftPanel = new JPanel();
			moveLeftPanel.setLayout(new GridLayout(1, 2));
			JLabel moveLeftLbl = new JLabel("Move Left");
			moveEsquerdaField = new JTextField("a");
			moveLeftPanel.add(moveLeftLbl);
			moveLeftPanel.add(moveEsquerdaField);
			
			
			JPanel moveRightPanel = new JPanel();
			moveRightPanel.setLayout(new GridLayout(1, 2));
			JLabel moveRightLbl = new JLabel("Move Right");
			moveDireitaField = new JTextField("d");
			moveRightPanel.add(moveRightLbl);
			moveRightPanel.add(moveDireitaField);
			
			moveBaixoField = new JTextField("s");
			moveEsquerdaField = new JTextField("a");
			moveDireitaField = new JTextField("d");
			teclasPanel.add(moveUpPanel);
			teclasPanel.add(moveDownPanel);
			teclasPanel.add(moveLeftPanel);
			teclasPanel.add(moveRightPanel);
			
			JPanel throwUpPanel = new JPanel(); //kek
			throwUpPanel.setLayout(new GridLayout(1, 2));
			JLabel throwUpLbl = new JLabel("Throw up");
			atiraCimaField = new JTextField("u");
			throwUpPanel.add(throwUpLbl);
			throwUpPanel.add(atiraCimaField);
			
			JPanel throwDownPanel = new JPanel();
			throwDownPanel.setLayout(new GridLayout(1, 2));
			JLabel throwDownLbl = new JLabel("Throw Down");
			atiraBaixoField = new JTextField("j");
			throwDownPanel.add(throwDownLbl);
			throwDownPanel.add(atiraBaixoField);
			
			JPanel throwLeftPanel = new JPanel();
			throwLeftPanel.setLayout(new GridLayout(1, 2));
			JLabel throwLeftLbl = new JLabel("Throw Left");
			atiraEsquerdaField = new JTextField("h");
			throwLeftPanel.add(throwLeftLbl);
			throwLeftPanel.add(atiraEsquerdaField);
			
			JPanel throwRightPanel = new JPanel();
			throwRightPanel.setLayout(new GridLayout(1, 2));
			JLabel throwRightLbl = new JLabel("Throw Right");
			atiraDireitaField = new JTextField("k");
			throwRightPanel.add(throwRightLbl);
			throwRightPanel.add(atiraDireitaField);
			
			atiraDireitaField = new JTextField("k");
			teclasPanel.add(throwUpPanel);
			teclasPanel.add(throwDownPanel);
			teclasPanel.add(throwLeftPanel);
			teclasPanel.add(throwRightPanel);
			
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
		setTitle("The Adventures of Ser McKilal Durgons");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 100);
		setResizable(false);		
		setFocusable(true);
		getContentPane().setLayout(new BorderLayout());		
		ActionListener listener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				JButton source = (JButton) arg0.getSource();
				JogoFrame.this.jogoPanel.terrainChoice = JogoFrame.EditorSelection.fromString(source.getText());
				
			}			
		};
		
		fileChooser = new JFileChooser();		
		criaButoesPanel();
		criaButoes();		
		criaJogoPanel();
		terrainPicker = new JToolBar();
		terrainPicker.setLayout(new GridLayout(0,1,10,10));
		terrainPicker.add(new JButton("Hero"));		
		terrainPicker.add(new JButton("Wall"));
		terrainPicker.add(new JButton("Floor"));
		terrainPicker.add(new JButton("Dragon"));
		terrainPicker.add(new JButton("Sword"));
		terrainPicker.add(new JButton("Javelin"));
		terrainPicker.add(new JButton("Shield"));
		terrainPicker.add(new JButton("Exit"));
		
		for(int i= 0; i < terrainPicker.getComponents().length; i++)
			if(terrainPicker.getComponents()[i] instanceof JButton)
				((JButton)terrainPicker.getComponents()[i]).addActionListener(listener);
		
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
		opcoesButton = new JButton("Options"); 
		playModeButton = new JButton("Play Mode");
		novoLabirintoButton = new JButton("New Game");
		sairButton = new JButton("Exit"); 
		saveButton = new JButton("Save");
		saveButton.setEnabled(false);
		loadButton = new JButton("Load");
		loadButton.setEnabled(false);
		mazeBuilderButton = new JButton("Maze Builder");
		
		novoLabirintoButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JogoFrame.this.jogoPanel.refazLabirinto();
				if (JogoFrame.this.jogoPanel.builderPanelActive)
					JogoFrame.this.jogoPanel.change(JogoPanel.BUILDER);
				else
					JogoFrame.this.jogoPanel.change(JogoPanel.PLAY);
				
			}
			
		});
		
		opcoesButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				jogoPanel.change(JogoPanel.OPCOES);
				
			}
			
		});
		
		playModeButton.addActionListener(new ActionListener(){

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
		botoesPanel.add(novoLabirintoButton);
		botoesPanel.add(playModeButton);
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
