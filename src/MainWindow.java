import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
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
	public static Dimension screenSize;
	public static byte chosenGame = C.GOL;
	public static int cellSideSize;
	private int rows;
	private int cols;
	
	private JFrame mainWindow;
	private JPanel controlPanel;
	private JPanel displayPanel; 
	private JPanel leftMargin;
	private JPanel rightMargin;
	private Board board;
	
	private JButton goHomeBtn;
	private JButton pauseBtn;
	private JButton structBtn;
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
	
	private static Timer golAnimationTimer;
	private static Timer wwAnimationTimer;

	public MainWindow() {
		buildMainWindow();
		buildRadioButtons();
		buildControlPanel();
		buildDisplayPanel();
        initAnimationTimers();
	}
	private void buildMainWindow() {
		mainWindow = new JFrame("Uniwersalny automat komorkowy"); 
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainWindow.setSize(screenSize);
		mainWindow.setMaximumSize(screenSize);
		mainWindow.setLayout(new BorderLayout()); 
		
		controlPanel = new JPanel(new GridBagLayout()); 
		displayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0)); 
		leftMargin = new JPanel();
		rightMargin = new JPanel();
		
		//leftMargin.setPreferredSize(new Dimension( (screenSize.width - (cols * cellSideSize))/2 ,screenSize.height)); // tu bedzie zabawa aby odpowiednio dobrac te wartosci!
		//rightMargin.setPreferredSize(new Dimension( (screenSize.width - (cols * cellSideSize))/2 ,screenSize.height));
	
		mainWindow.add(controlPanel,BorderLayout.NORTH);
		mainWindow.add(displayPanel,BorderLayout.CENTER);
		mainWindow.add(leftMargin,BorderLayout.WEST);
		mainWindow.add(rightMargin,BorderLayout.EAST);
		
		mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainWindow.setVisible(true); 
		
	}
	private void buildControlPanel() {
		goHomeBtn = new JButton("go home");
		pauseBtn = new JButton("pause");
		structBtn = new JButton("structs");
		startBtn = new JButton("start");
		rowsTA = new TextArea("10", 1, 8, TextArea.SCROLLBARS_NONE); 
		columnsTA = new TextArea("10", 1, 8, TextArea.SCROLLBARS_NONE);
		numOfGensTA = new TextArea("default value", 1, 8, TextArea.SCROLLBARS_NONE);
		rowsLabel = new JLabel("rows:");
		columnsLabel = new JLabel("columns:");
		numOfGensLabel = new JLabel("number of generations:");
		speedLabel = new JLabel("animation speed:");
		speedSlider = new JSlider(1,10,5);
		currentSpeedLabel = new JLabel("5");
		
		//to jest tylko po to aby ButtonClickListener mogl obslugiwac te przyciski bez dostepu do nich bezposrednio
		goHomeBtn.setActionCommand("goHomeBtn");
		pauseBtn.setActionCommand("pauseBtn");
		structBtn.setActionCommand("structBtn");
		startBtn.setActionCommand("startBtn");
		
		goHomeBtn.addActionListener(new ButtonClickListener());
		pauseBtn.addActionListener(new ButtonClickListener());
		structBtn.addActionListener(new ButtonClickListener());
		startBtn.addActionListener(new ButtonClickListener());
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
		controlPanel.add(pauseBtn,gbc);
		gbc.gridx = 7;
		gbc.gridy = 1;
		controlPanel.add(structBtn,gbc);
		gbc.gridx = 8;
		gbc.gridy = 1;
		controlPanel.add(startBtn,gbc);
		gbc.gridx = 9;
		gbc.gridy = 0;
		controlPanel.add(wwRB);
		gbc.gridx = 9;
		gbc.gridy = 1;
		controlPanel.add(golRB);
	}
	private void buildRadioButtons() {
		wwRB = new JRadioButton("WireWorld", true);
		golRB = new JRadioButton("Game Of Life", false);
		wwRB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				chosenGame = C.WW;
			}
		});
		golRB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				chosenGame = C.GOL;
			}
		});
		chooseGameBG = new ButtonGroup();
		chooseGameBG.add(wwRB);
		chooseGameBG.add(golRB);
		
	}
	private void buildDisplayPanel() { 
		//board = LoadBoardFromFile.loadBoardFromFile("example.life"); //wczytanie pliku
		if(board == null)
			board = new Board(Integer.parseInt(rowsTA.getText())+2, Integer.parseInt(columnsTA.getText())+2); // +2 dla paddingu
		rows = board.getRows(); 
		cols = board.getCols();
		
		cellSideSize = (displayPanel.getSize().height)/rows*9/10;	//tak chyba wygodniej, tez zmniejszylem do 90%
		board.changeCellsSize(new Dimension(cellSideSize,cellSideSize));
		
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++) {
				displayPanel.add(board.getCell(i, j));
			}

	}
	private void initAnimationTimers() {
		golAnimationTimer = new Timer(getCurrentSpeedLabel()*100 , new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.calculateNextStateGOL();
				board.updateBoard();
			}
		});
        wwAnimationTimer = new Timer(getCurrentSpeedLabel()*100 , new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.calculateNextStateWW();
				board.updateBoard();
			}
		});
	}
	private void initSlider() {
		
	}
	public int getCurrentSpeedLabel() {
		return Integer.parseInt(currentSpeedLabel.getText());
	}
	public void setCurrentSpeedLabel(int currentSpeed) {
		currentSpeedLabel.setText(String.valueOf(currentSpeed));
	}
	public static Timer getGolAnimationTimer() {
		return golAnimationTimer;
	}
	public static Timer getWwAnimationTimer() {
		return wwAnimationTimer;
	}

}
