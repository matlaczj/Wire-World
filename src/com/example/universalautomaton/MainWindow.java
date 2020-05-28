package com.example.universalautomaton;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.*;

public class MainWindow {
	private int rows;
	private int cols;
	private int cellSideSize;
	private byte chosenGame;
	private int numOfGens;	//liczba generacji nazwa n jest tragiczna ... zwlaszcza dla tak waznej zmiennej
	private boolean isNumOfGensFinite = true; //czy ma isc w nieskonczonosc, dobre nazewnictwo zgodne z przyjetymi wczesniej zasadami
	private SaveBoardToFile saveToFileObject;
	private LoadBoardFromFile loadFromFileObject;
	
	private JFrame mainWindow;
	private JPanel controlPanel;
	private JPanel displayPanel; 
	private Board board;
	
	private JButton goHomeBtn;
	private JButton pauseBtn;
	private JButton structsBtn;
	private JButton startBtn;
	private JButton chooseFileToLoadBtn;
	private JButton saveBtn;
	private JButton chooseFileToSaveBtn;
	private JButton choiceWWBtn; // konwencja jest przydatna wiec dodaje Btn ma koncu
	private JButton choiceGoLBtn;
	
	private JFileChooser chooseFileToLoadFC;
	private JFileChooser chooseFileToSaveFC;
	
	private JTextField rowsTA;
	private JTextField columnsTA;
	private JTextField numOfGensTA;
	
	private JLabel rowsLabel;
	private JLabel columnsLabel;
	private JLabel numOfGensLabel;
	private JLabel speedLabel;
	private JLabel currentSpeedLabel;
	
	private JSlider speedSlider;
	
	private Timer animationTimer;
	
	private Icon homeIcon;
	private Icon pauseIcon;
	private Icon startIcon;
	private Icon structsIcon;
	
	private Dimension screenSize;
	private int controlPanelHeight;
	private Color bgColor = new Color(238,238,238); //kolor domyslnego tla okna aplikacji
	
	
	public MainWindow() {
		initIcons(); //pozostawiam tutaj gdybysmy chcieli dac ikony na okno wyboru gry, inaczej dalbym na poczatek buildControlPanel
		mainWindow = new JFrame("Uniwersalny automat komorkowy");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainWindow.setMaximumSize(screenSize);
		buildChoiceWindow();
	}
	
	private void buildChoiceWindow() {
		mainWindow.setSize(600, 400);
		mainWindow.getContentPane().removeAll();
		mainWindow.getContentPane().repaint();
		mainWindow.setLayout(new BorderLayout());
		
		choiceWWBtn = new JButton("WireWorld");
		choiceGoLBtn = new JButton("Game of Life");
		choiceWWBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chosenGame = C.WW;
				setupWindow();
			}
		});
		choiceGoLBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chosenGame = C.GOL;
				setupWindow();
			}
		});
		choiceWWBtn.setPreferredSize(new Dimension(mainWindow.getWidth()/2, mainWindow.getHeight()));
		choiceGoLBtn.setPreferredSize(new Dimension(mainWindow.getWidth()/2, mainWindow.getHeight()));
		mainWindow.add(choiceWWBtn, BorderLayout.WEST);
		mainWindow.add(choiceGoLBtn, BorderLayout.EAST);
		
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true); 
	}
	
	private void setupWindow() {
		buildMainWindow();
		buildControlPanel();
		initAnimationTimers();
		loadFromFileObject = new LoadBoardFromFile(chosenGame);
		initBoard();
		buildDisplayPanel();
		loadFromFileObject = new LoadBoardFromFile(chosenGame);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
	
	private void buildMainWindow() {
		mainWindow.setSize(screenSize);
		mainWindow.getContentPane().removeAll();
		mainWindow.getContentPane().repaint();
		mainWindow.setLayout(new BoxLayout(mainWindow.getContentPane(), BoxLayout.Y_AXIS));
				
		controlPanel = new JPanel(new GridBagLayout()); 
		displayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0)); 
		
		mainWindow.add(controlPanel);
		mainWindow.add(displayPanel);
		mainWindow.add(Box.createRigidArea(new Dimension(0,15)));	//troche luzu pod spodem
	}
	
	private void buildControlPanel() {
		goHomeBtn = new JButton(homeIcon);
		structsBtn = new JButton(structsIcon);
		startBtn = new JButton(startIcon);
		pauseBtn = new JButton(pauseIcon);
		styleButtons();
		rowsTA = new JTextField("10", 1); 
		columnsTA = new JTextField("10", 1);
		numOfGensTA = new JTextField("", 1);
		rowsLabel = new JLabel("  rows:");
		columnsLabel = new JLabel("columns:");
		numOfGensLabel = new JLabel("no. of generations:");
		speedLabel = new JLabel("  animation speed:");
		speedSlider = new JSlider(1,20,10);
		currentSpeedLabel = new JLabel("10");
		chooseFileToLoadBtn = new JButton("load state");
		saveBtn = new JButton("save");
		chooseFileToSaveBtn = new JButton("save as...");
		
		//to jest tylko po to aby ButtonClickListener mogl obslugiwac te przyciski bez dostepu do nich bezposrednio
		goHomeBtn.setActionCommand("goHomeBtn");
		structsBtn.setActionCommand("structBtn");
		startBtn.setActionCommand("startBtn");
		pauseBtn.setActionCommand("pauseBtn");
		chooseFileToLoadBtn.setActionCommand("chooseFileToLoadBtn");
		saveBtn.setActionCommand("saveBtn");
		chooseFileToSaveBtn.setActionCommand("chooseFileToSaveBtn");
		
		goHomeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buildChoiceWindow();
				animationTimer.stop();
			}
		});
		startBtn.addActionListener(new ActionListener() { //postanowilem przeniesc tutaj te Listenery poniewaz chcialem aby zmienne nie byly static a jednoczesnie nie chcialem wszystkiego pogmatwac
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( numOfGensTA.getText() == "" )
					isNumOfGensFinite = true;
				else
					try {
					numOfGens = Integer.parseInt(numOfGensTA.getText());
					isNumOfGensFinite = false;
					} catch (NumberFormatException e1) {
						isNumOfGensFinite = true;
					}
				animationTimer.start();
			}
		});
		pauseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animationTimer.stop();
			}
		});
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int currentSpeed = ((JSlider)e.getSource()).getValue();
				setCurrentSpeedLabel(currentSpeed);
				animationTimer.setDelay(1000/getCurrentSpeedLabel());
			}
		});
		chooseFileToLoadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buildLoadFileChooser();
			}
		});
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveToFileObject.saveBoardToFile();
			}
		});
		chooseFileToSaveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buildSaveFileChooser();
			}
		});
		rowsTA.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadFromFileObject.setUsersCatalogPath("");
				initBoard();
				buildDisplayPanel();
				mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainWindow.setVisible(true);
			}
		});
		columnsTA.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadFromFileObject.setUsersCatalogPath("");
				initBoard();
				buildDisplayPanel();
				mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainWindow.setVisible(true);
			}
		});
		numOfGensTA.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( numOfGensTA.getText() == "" )
					isNumOfGensFinite = true;
				else
					try {
					numOfGens = Integer.parseInt(numOfGensTA.getText());
					isNumOfGensFinite = false;
					} catch (NumberFormatException e1) {
						isNumOfGensFinite = true;
					}
			}
		});
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0,1,0,1);
		gbc.gridx = 0;
		gbc.gridy = 1;
		controlPanel.add(goHomeBtn,gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		controlPanel.add(numOfGensLabel,gbc);
		gbc.gridy = 1;
		controlPanel.add(numOfGensTA,gbc);
		gbc.gridx = 2;
		gbc.gridy = 0;
		controlPanel.add(rowsLabel,gbc);
		gbc.gridy = 1;
		controlPanel.add(rowsTA,gbc);
		gbc.gridx = 3;
		gbc.gridy = 0;
		controlPanel.add(columnsLabel,gbc);
		gbc.gridy = 1;
		controlPanel.add(columnsTA,gbc);
		gbc.gridx = 4;
		gbc.gridy = 0;
		controlPanel.add(speedLabel,gbc);
		gbc.gridy = 1;
		controlPanel.add(speedSlider,gbc);
		gbc.gridy = 2;
		controlPanel.add(currentSpeedLabel);
		gbc.gridx = 6;
		gbc.gridy = 1;
		controlPanel.add(startBtn,gbc);
		gbc.gridx = 7;
		controlPanel.add(pauseBtn,gbc);
		gbc.gridx = 8;
		controlPanel.add(structsBtn,gbc);
		gbc.gridx = 9;
		controlPanel.add(chooseFileToLoadBtn, gbc);
		gbc.gridx = 10;
		controlPanel.add(saveBtn, gbc);
		gbc.gridx = 11;
		controlPanel.add(chooseFileToSaveBtn, gbc);
		controlPanelHeight = controlPanel.getHeight();
	}
	
	private void buildDisplayPanel() { 
		displayPanel.removeAll();
		displayPanel.setSize(mainWindow.getWidth() - 15, mainWindow.getHeight() - controlPanelHeight - 15);
		
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
		displayPanel.setVisible(true);
	}

	private void buildLoadFileChooser() { // aby nazwy byly jak najbardziej spojne dodaje Load bo wszedzie go uzywamy i poprawia przejrzystosc
		if (chooseFileToLoadFC == null)
			chooseFileToLoadFC = new JFileChooser(new File(System.getProperty("user.dir")));
		int result = chooseFileToLoadFC.showOpenDialog(mainWindow);
		if(result == JFileChooser.APPROVE_OPTION) {		//musiałem przenieść do wewnątrz bo inaczej cancel czyścił planszę
			loadFromFileObject.setUsersCatalogPath(chooseFileToLoadFC.getSelectedFile().getAbsolutePath());
			initBoard();
			buildDisplayPanel();
			mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainWindow.setVisible(true);
		}
	}
	
	private void buildSaveFileChooser() {
		if (chooseFileToSaveFC == null) {
			chooseFileToSaveFC = new JFileChooser(new File(System.getProperty("user.dir")));
			chooseFileToSaveFC.setSelectedFile(new File("example.wire"));
		}
		int result = chooseFileToSaveFC.showSaveDialog(mainWindow);
		if(result == JFileChooser.APPROVE_OPTION) {
			saveToFileObject.saveBoardToFile(chooseFileToSaveFC.getSelectedFile().getAbsolutePath());
		}
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
		animationTimer = new Timer(1000/getCurrentSpeedLabel() , new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isNumOfGensFinite || numOfGens>0 ) {
					if (chosenGame == C.GOL)
						board.calculateNextStateGOL();
					else if (chosenGame == C.WW)
						board.calculateNextStateWW();
					board.updateBoard();
					if (!isNumOfGensFinite) {
						numOfGens--;
						numOfGensTA.setText(Integer.toString(numOfGens));
					}
//					saveToFileObject.saveBoardToFile(); //obecnie zapisuje kazdy nowy stan
				}
			}
		});
	}
	
	private void initBoard() {
		board = loadFromFileObject.loadBoardFromFile("notafile.wire"); //juz dziala, sciezka jest juz uniwersalna, wyzszy piorytet ma wybor uzytkownika
		if(board == null)
			board = new Board(Integer.parseInt(rowsTA.getText())+2, Integer.parseInt(columnsTA.getText())+2, chosenGame); // +2 dla paddingu
		rows = board.getRows(); 
		cols = board.getCols();
		saveToFileObject = new SaveBoardToFile(board, chosenGame);
		saveToFileObject.setChosenGame(chosenGame);
	}
	
	private void initIcons() {
		homeIcon = new ImageIcon("src\\icons\\home_icon.png");
		startIcon = new ImageIcon("src\\icons\\start_icon.png");
		pauseIcon = new ImageIcon("src\\icons\\pause_icon.png");
		structsIcon = new ImageIcon("src\\icons\\structs_icon.png");
	}
	
	public int getCurrentSpeedLabel() {
		return Integer.parseInt(currentSpeedLabel.getText());
	}
	
	public void setCurrentSpeedLabel(int currentSpeed) {
		currentSpeedLabel.setText(String.valueOf(currentSpeed));
	}

}
