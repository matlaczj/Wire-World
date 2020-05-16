import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;

public class MainWindow {
	
	private JFrame mainWindow;
	private JSplitPane horizontalSplitPanel;
	private JPanel controlPanel;
	private JPanel displayPanel; //panel do wyswietlania planszy i klikania w ni�
	public static JLabel currentSpeedLabel; // to jest tu tylko dlatego �e potzrebowa�em mie� do tego dost�p z innej klasy, to wyj�tek, nie regu�a
	
	public static double windowHeight;
	public static double windowWidth;
	private int rows = 100; //wartosci domyslne, zmieniane prze usera lub przez wgranie pliku
	private int cols = 100;

	public MainWindow() {
		buildMainWindow();
		buildControlPanel();
		buildDisplayPanel();
	}
	
	private void buildMainWindow() {
		mainWindow = new JFrame("Uniwersalny automat kom�rkowy"); 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		windowHeight = screenSize.height;
		windowWidth = screenSize.width;
		mainWindow.setSize(screenSize);
		mainWindow.setMinimumSize(screenSize);
		mainWindow.setMaximumSize(screenSize);
		mainWindow.setLayout(new BorderLayout()); 
		
		controlPanel = new JPanel(new GridBagLayout()); //to b�dzie nasze menu
		displayPanel = new JPanel(new GridLayout(rows, cols));
		
		mainWindow.add(controlPanel,BorderLayout.NORTH); //kazdy element nale�y doda� do okna, a niekt�re tylko do odpowiadaj�cej struktury kt�ra ju� jest dodana do okna
		mainWindow.add(displayPanel,BorderLayout.CENTER);
		
		mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //klikni�cie zamnkni�cia wy��cza okno
		mainWindow.setVisible(true); //aby okno by�o widoczne, dla false jest niewidzialne
		
	}
	
	private void buildControlPanel() {
		JButton goHomeBtn = new JButton("go home");
		JButton pauseBtn = new JButton("pause");
		JButton structBtn = new JButton("structs");
		TextArea rowsTA = new TextArea("default value", 1, 8, TextArea.SCROLLBARS_NONE); //tekst zach�ty, ilo�� rz�d�w i kolumn, mo�na usun�� scrollbary
		TextArea columnsTA = new TextArea("default value", 1, 8, TextArea.SCROLLBARS_NONE);
		TextArea numOfGensTA = new TextArea("default value", 1, 8, TextArea.SCROLLBARS_NONE);
		JLabel rowsLabel = new JLabel("rows:");
		JLabel columnsLabel = new JLabel("columns:");
		JLabel numOfGensLabel = new JLabel("number of generations:");
		JLabel speedLabel = new JLabel("animation speed:");
		JSlider speedSlider = new JSlider(1,10,5);
		currentSpeedLabel = new JLabel("5");
		
		
		//to jest tylko po to aby ButtonClickListener mogl obslugiwac te przyciski bez dostepu do nich bezposrednio
		goHomeBtn.setActionCommand("goHomeBtn");
		pauseBtn.setActionCommand("pauseBtn");
		structBtn.setActionCommand("structBtn");
		
		//wszystkie actions listenery:
		goHomeBtn.addActionListener(new ButtonClickListener());
		pauseBtn.addActionListener(new ButtonClickListener());
		structBtn.addActionListener(new ButtonClickListener());
		speedSlider.addChangeListener(new SliderChangeListener());
		
		GridBagConstraints gbc = new GridBagConstraints();
	//trzeba je dodac do controlPanel ale nie trzeba ju� bezpo�rednio do mainWindow bo controlPanel jest do niego dodany, wi�c i przyciski po�rednio s� dodane
	//mamy wyobrazon� siatk� a x, y to pozycje na niej a nie odleg�o�ci, tutaj wszystko jest wzgl�dne
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
	}
	
	private void buildDisplayPanel() {
		GridBagConstraints gbc = new GridBagConstraints(); //dla displayPlanel
		gbc.gridwidth = cols;
		gbc.gridheight = rows;
		Board board = new Board(rows,cols);
		gbc.insets = new Insets(1,1,1,1);
		
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++)
			{
				gbc.gridy = i;
				gbc.gridx = j;
				displayPanel.add(board.getCell(i, j),gbc);
			}
	}
}
