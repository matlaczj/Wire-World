import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow {
	private int rows;
	private int cols;
	private int cellSideSize;
	private byte chosenGame;
	private SaveBoardToFile saveToFileObject;
	
	private JFrame mainWindow;
	private JPanel controlPanel;
	private JPanel displayPanel; 
	private Board board;
	
	private JButton goHomeBtn;
	private JButton pauseBtn;
	private JButton structsBtn;
	private JButton startBtn;
	
	private TextArea rowsTA;
	private TextArea columnsTA;
	private TextArea numOfGensTA;
	
	private JLabel rowsLabel;
	private JLabel columnsLabel;
	private JLabel numOfGensLabel;
	private JLabel speedLabel;
	private JLabel currentSpeedLabel;
	
	private JSlider speedSlider;
	
	private JRadioButton wwRB;
	private JRadioButton golRB;
	private ButtonGroup chooseGameBG;
	
	private Timer golAnimationTimer;
	private Timer wwAnimationTimer;
	
	private Icon homeIcon;
	private Icon pauseIcon;
	private Icon startIcon;
	private Icon structsIcon; 
	
	private Color bgColor = new Color(238,238,238); //kolor domyslnego tla okna aplikacji
	
	public MainWindow() {
		initIcons();
		buildMainWindow();
		buildRadioButtons();
		buildControlPanel();
		initAnimationTimers();
		initBoard();
		buildDisplayPanel();
		saveToFileObject = new SaveBoardToFile(board, chosenGame);
	}
	
	private void buildMainWindow() {
		mainWindow = new JFrame("Uniwersalny automat komorkowy"); 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainWindow.setSize(screenSize);
		mainWindow.setMaximumSize(screenSize);
		mainWindow.setLayout(new BoxLayout(mainWindow.getContentPane(), BoxLayout.Y_AXIS));
				
		controlPanel = new JPanel(new GridBagLayout()); 
		displayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0)); 
		
		mainWindow.add(controlPanel);
		mainWindow.add(displayPanel);
		mainWindow.add(Box.createRigidArea(new Dimension(0,15)));	//troche luzu pod spodem
		
		mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainWindow.setVisible(true); 
	}
	
	private void buildRadioButtons() {
		wwRB = new JRadioButton("WireWorld", false);
		golRB = new JRadioButton("Game Of Life", false);
		wwRB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				chosenGame = C.WW;
				golAnimationTimer.stop(); //by zatrzymac przy zmianie rodzaju gry
				wwAnimationTimer.stop();
				saveToFileObject.setChosenGame(chosenGame); //aktualizuje typ gry w objekcie zapisujacym do pliku
			}
		});
		golRB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				chosenGame = C.GOL;
				golAnimationTimer.stop();
				wwAnimationTimer.stop();
				saveToFileObject.setChosenGame(chosenGame);
			}
		});
		chooseGameBG = new ButtonGroup();
		chooseGameBG.add(wwRB);
		chooseGameBG.add(golRB);
	}
	
	private void buildControlPanel() {
		goHomeBtn = new JButton(homeIcon);
		structsBtn = new JButton(structsIcon);
		startBtn = new JButton(startIcon);
		pauseBtn = new JButton(pauseIcon);
		styleButtons();
		rowsTA = new TextArea("10", 1, 4, TextArea.SCROLLBARS_NONE); 
		columnsTA = new TextArea("10", 1, 4, TextArea.SCROLLBARS_NONE);
		numOfGensTA = new TextArea("default value", 1, 5, TextArea.SCROLLBARS_NONE);
		rowsLabel = new JLabel("rows:");
		columnsLabel = new JLabel("columns:");
		numOfGensLabel = new JLabel("number of generations:");
		speedLabel = new JLabel("animation speed:");
		speedSlider = new JSlider(1,9,5);
		currentSpeedLabel = new JLabel("5");
		
		//to jest tylko po to aby ButtonClickListener mogl obslugiwac te przyciski bez dostepu do nich bezposrednio
		goHomeBtn.setActionCommand("goHomeBtn");
		structsBtn.setActionCommand("structBtn");
		startBtn.setActionCommand("startBtn");
		pauseBtn.setActionCommand("pauseBtn");
		
		startBtn.addActionListener(new ActionListener() { //postanowilem przeniesc tutaj te Listenery poniewaz chcialem aby zmienne nie byly static a jednoczesnie nie chcialem wszystkiego pogmatwac
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chosenGame == C.GOL)
					golAnimationTimer.start();
				if(chosenGame == C.WW)
					wwAnimationTimer.start();
			}
		});
		pauseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				golAnimationTimer.stop();
				wwAnimationTimer.stop();
			}
		});
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int currentSpeed = ((JSlider)e.getSource()).getValue();
				setCurrentSpeedLabel(currentSpeed);
			}
		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0,15,0,15);
		gbc.gridx = 0;
		gbc.gridy = 1;
		controlPanel.add(goHomeBtn,gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		controlPanel.add(numOfGensLabel,gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		controlPanel.add(numOfGensTA,gbc);
		gbc.gridx = 2;
		gbc.gridy = 0;
		controlPanel.add(rowsLabel,gbc);
		gbc.gridx = 2;
		gbc.gridy = 1;
		controlPanel.add(rowsTA,gbc);
		gbc.gridx = 3;
		gbc.gridy = 0;
		controlPanel.add(columnsLabel,gbc);
		gbc.gridx = 3;
		gbc.gridy = 1;
		controlPanel.add(columnsTA,gbc);
		gbc.gridx = 4;
		gbc.gridy = 0;
		controlPanel.add(speedLabel,gbc);
		gbc.gridx = 4;
		gbc.gridy = 1;
		controlPanel.add(speedSlider,gbc);
		gbc.gridx = 4;
		gbc.gridy = 2;
		controlPanel.add(currentSpeedLabel);
		gbc.gridx = 6;
		gbc.gridy = 1;
		controlPanel.add(startBtn,gbc);
		gbc.gridx = 7;
		gbc.gridy = 1;
		controlPanel.add(pauseBtn,gbc);
		gbc.gridx = 8;
		gbc.gridy = 1;
		controlPanel.add(structsBtn,gbc);
		gbc.gridx = 9;
		gbc.gridy = 0;
		controlPanel.add(wwRB,gbc);
		gbc.gridx = 9;
		gbc.gridy = 1;
		controlPanel.add(golRB,gbc);
	}
	
	private void initIcons() {
		homeIcon = new ImageIcon("src\\icons\\home_icon.png");
		startIcon = new ImageIcon("src\\icons\\start_icon.png");
		pauseIcon = new ImageIcon("src\\icons\\pause_icon.png");
		structsIcon = new ImageIcon("src\\icons\\structs_icon.png");
	}
	
	private void styleButtons() {
		goHomeBtn.setBackground(bgColor);
		startBtn.setBackground(bgColor);
		pauseBtn.setBackground(bgColor);
		structsBtn.setBackground(bgColor);
		goHomeBtn.setBorderPainted(false);
		startBtn.setBorderPainted(false);
		pauseBtn.setBorderPainted(false);
		structsBtn.setBorderPainted(false);
	}
	
	private void initAnimationTimers() {
		golAnimationTimer = new Timer(getCurrentSpeedLabel()*10 , new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.calculateNextStateGOL();
				board.updateBoard();
				saveToFileObject.saveBoardToFile(); //obecnie zapisuje kazdy nowy stan
				//golAnimationTimer.setDelay(getCurrentSpeedLabel()); // to chyba niezbyt dziala trudno mi okreslic, mam wrazenie ze spowalnia
			}
		});
        wwAnimationTimer = new Timer(getCurrentSpeedLabel()*10 , new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.calculateNextStateWW();
				board.updateBoard();
				saveToFileObject.saveBoardToFile();
				//golAnimationTimer.setDelay(getCurrentSpeedLabel());
			}
		});
	}
	
	private void initBoard() {
		board = LoadBoardFromFile.loadBoardFromFile("example.life"); //juz dziala, sciezka jest juz uniwersalna
		if(board == null)
			board = new Board(Integer.parseInt(rowsTA.getText())+2, Integer.parseInt(columnsTA.getText())+2); // +2 dla paddingu 
		board = new Board(50,50); //moznaby bardziej wysrodkowac w pionie gdy plansza jest poziomym prostokatem
		rows = board.getRows(); 
		cols = board.getCols();
	}

	private void buildDisplayPanel() { 
		displayPanel.setSize(mainWindow.getWidth() - 15, mainWindow.getHeight() - displayPanel.getHeight() - 15);
		
		cellSideSize = (displayPanel.getSize().height)/rows*9/10;	
		if (displayPanel.getSize().width/cols < cellSideSize)
			cellSideSize = displayPanel.getSize().width/cols;		//na wypadek gdyby plansza nie miescila sie w poziomie
		board.changeCellsSize(new Dimension(cellSideSize,cellSideSize));
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++) {
				displayPanel.add(board.getCell(i, j));
			}
		displayPanel.setLayout(new GridLayout(rows,cols));
		displayPanel.setPreferredSize(new Dimension(cellSideSize*cols,cellSideSize*rows));
		displayPanel.setMinimumSize(new Dimension(cellSideSize*cols,cellSideSize*rows));
		displayPanel.setMaximumSize(new Dimension(cellSideSize*cols,cellSideSize*rows));
	}
	
	public int getCurrentSpeedLabel() {
		return Integer.parseInt(currentSpeedLabel.getText());
	}
	public void setCurrentSpeedLabel(int currentSpeed) {
		currentSpeedLabel.setText(String.valueOf(currentSpeed));
	}

}
