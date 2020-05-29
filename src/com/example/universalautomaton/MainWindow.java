package com.example.universalautomaton;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

public class MainWindow {
	private int rows = 22;
	private int cols = 22;
	private int cellSideSize;
	private byte chosenGame;
	private int numOfGens;	//liczba generacji nazwa n jest tragiczna ... zwlaszcza dla tak waznej zmiennej
	private boolean isNumOfGensFinite = true; //czy ma isc w nieskonczonosc, dobre nazewnictwo zgodne z przyjetymi wczesniej zasadami
	private SaveBoardToFile saveToFileObject;
	private LoadBoardFromFile loadFromFileObject;
	
	JFrame mainWindow;
	private JPanel controlPanel;
	private JPanel displayPanel;
	private StructPanel[] structPanels;
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
	
	private ComponentAdapter componentAdapter = new ComponentAdapter(){  
        public void componentResized(ComponentEvent evt) {
        	buildDisplayPanel();
			mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainWindow.setVisible(true);
        }
	};
	
	public MainWindow() {
		initIcons(); //pozostawiam tutaj gdybysmy chcieli dac ikony na okno wyboru gry, inaczej dalbym na poczatek buildControlPanel
		mainWindow = new JFrame("Uniwersalny automat komorkowy");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainWindow.setMaximumSize(screenSize);
		mainWindow.setSize(screenSize);
		buildChoiceWindow();
	}
	
	private void buildChoiceWindow() {
		mainWindow.removeComponentListener(componentAdapter);
		Dimension oldSize = mainWindow.getSize();
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
				mainWindow.setSize(oldSize);
				setupWindow();
			}
		});
		choiceGoLBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chosenGame = C.GOL;
				mainWindow.setSize(oldSize);
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
		mainWindow.getContentPane().removeAll();
		mainWindow.getContentPane().repaint();
		mainWindow.setLayout(new BoxLayout(mainWindow.getContentPane(), BoxLayout.Y_AXIS));
				
		controlPanel = new JPanel(new GridBagLayout()); 
		displayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0)); 
		
		mainWindow.add(controlPanel);
		mainWindow.add(displayPanel);
		mainWindow.add(Box.createRigidArea(new Dimension(0,15)));	//troche luzu pod spodem
		
		mainWindow.addComponentListener(componentAdapter);
	}
	
	private void buildControlPanel() {
		goHomeBtn = new JButton(homeIcon);
		structsBtn = new JButton(structsIcon);
		startBtn = new JButton(startIcon);
		pauseBtn = new JButton(pauseIcon);
		styleButtons();
		rowsTA = new JTextField(Integer.toString(rows-2), 1); 
		columnsTA = new JTextField(Integer.toString(cols-2), 1);
		numOfGensTA = new JTextField("", 1);
		if (!isNumOfGensFinite)
			numOfGensTA.setText(Integer.toString(numOfGens));
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
		structsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buildStructsWindow();
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
		if(result == JFileChooser.APPROVE_OPTION) {		//musiałem przenieść do wewnątrz bo inaczej cancel czyścił planszę //pisz bez pl znakow bo mi je psuje w edytorze
			loadFromFileObject.setUsersCatalogPath(chooseFileToLoadFC.getSelectedFile().getAbsolutePath());
			initBoard();
			buildDisplayPanel();
			rowsTA.setText(Integer.toString(rows-2));
			columnsTA.setText(Integer.toString(cols-2));
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
					board.setChosenGame(chosenGame); //by miec pewnosc ze jest tam aktualna gra
					if (chosenGame == C.GOL)
						board.updateBoardOrCalculateNextState(false); //if nie potrzebny bo metoda i tak wie co ma liczyc znajac chosenGame
					else if (chosenGame == C.WW)
						board.updateBoardOrCalculateNextState(false);
					board.updateBoardOrCalculateNextState(true);
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
		board = loadFromFileObject.loadBoardFromFile("notafile.wire"); 
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
	
	private void buildStructsWindow() {
		board.restoreListeners();
		mainWindow.removeComponentListener(componentAdapter);
		mainWindow.getContentPane().removeAll();
		mainWindow.getContentPane().repaint();
		mainWindow.setLayout(new GridLayout(0,6));
		File folder;
		List<String> filenames = new ArrayList<>();
		if (chosenGame == C.WW) {
			folder = new File("src\\structures\\wireworld");
			search(".*\\.wire", folder, filenames);
		}
		else if (chosenGame == C.GOL) {
			folder = new File("src\\structures\\gameoflife");
			search(".*\\.life", folder, filenames);
		}
        structPanels = new StructPanel[filenames.size()];
        int i = 0;
        for (String name : filenames) {
        	mainWindow.add(structPanels[i++] = new StructPanel(name, filenames.size(), this));
        }
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buildMainWindow();
				buildControlPanel();
				buildDisplayPanel();
				mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainWindow.setVisible(true);
			}
        });
        mainWindow.add(backBtn);
        mainWindow.setVisible(true);
	}
	
	public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }

        }
    }

	public void startStructListener(String filename, int i, int j, byte dir) {
		board.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File(filename);
				String[] position = e.getActionCommand().split(" ");
				int y = Integer.parseInt(position[0]);
				int x = Integer.parseInt(position[1]);
				try {
					loadFromFileObject.loadFileIntoBoard(board, file, x - j, y - i, dir);
					board.restoreListeners();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		buildMainWindow();
		buildControlPanel();
		buildDisplayPanel();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
}
