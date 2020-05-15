import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class MainWindow {
	
	private JFrame mainWindow;
	private JPanel controlPanel;
	public static JLabel currentSpeedLabel; // to jest tu tylko dlatego �e potzrebowa�em mie� do tego dost�p z innej klasy, to wyj�tek, nie regu�a

	public MainWindow() {
		buildMainWindow();
		buildMenu();
	}
	
	private void buildMainWindow() {
		mainWindow = new JFrame("Uniwersalny automat kom�rkowy"); 
		mainWindow.setMinimumSize(new Dimension(1500,1000));
		mainWindow.setMaximumSize(new Dimension(1500,1000));
		mainWindow.setLayout(new GridLayout(3,8)); //dzieli okno na 3 rz�dy i 8 kolumn
		
		controlPanel = new JPanel(); //to b�dzie nasze menu
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		mainWindow.add(controlPanel); //kazdy element nale�y doda� do okna, a niekt�re tylko do odpowiadaj�cej struktury kt�ra ju� jest dodana do okna
		mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //klikni�cie zamnkni�cia wy��cza okno
		mainWindow.setVisible(true); //aby okno by�o widoczne, dla false jest niewidzialne
		
	}
	
	private void buildMenu() {
		JButton goHomeBtn = new JButton("go home");
		JButton pauseBtn = new JButton("pause");
		JButton structBtn = new JButton("structs");
		
		TextArea rowsTA = new TextArea("default value", 1, 8); //tekst zach�ty, ilo�� rz�d�w i kolumn, mo�na usun�� scrollbary
		TextArea columnsTA = new TextArea("default value", 1, 8);
		TextArea numOfGensTA = new TextArea("default value", 1, 8);
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
		
		goHomeBtn.addActionListener(new ButtonClickListener());
		pauseBtn.addActionListener(new ButtonClickListener());
		structBtn.addActionListener(new ButtonClickListener());
		speedSlider.addChangeListener(new SliderChangeListener());

	//trzeba je dodac do controlPanel ale nie trzeba ju� bezpo�rednio do mainWindow bo controlPanel jest do niego dodany, wi�c i przyciski po�rednio s� dodane
		controlPanel.add(goHomeBtn);
		
		controlPanel.add(numOfGensLabel);
		controlPanel.add(numOfGensTA);
		
		controlPanel.add(rowsLabel);
		controlPanel.add(rowsTA);
		
		controlPanel.add(columnsLabel);
		controlPanel.add(columnsTA);
		
		controlPanel.add(speedLabel);
		controlPanel.add(speedSlider);
		controlPanel.add(currentSpeedLabel);
		
		controlPanel.add(pauseBtn);
		controlPanel.add(structBtn);
		
	}
	
	
	public static void main(String[] args) {
		MainWindow window = new MainWindow(); //stworzenie obiektu ktory jest oknem, mozna stworzyc 2 niezalezne identyczne okna
		
	}
	
}
