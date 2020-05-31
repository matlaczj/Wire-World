package com.example.universalautomaton;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class StructPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String pathSeparator;
	private MainWindow parent;
	private JLabel nameLabel;
	private JButton rotateBtn;
	private JButton mirrorBtn;
	private JPanel displayPanel;
	private Board board;
	private String name;
	private String filename;
	private LoadBoardFromFile loadFromFileObject = new LoadBoardFromFile(C.WW);
	private int rows, cols, count;
	private byte dir = Directions.R;
	private ComponentAdapter componentAdapter = new ComponentAdapter(){  
        public void componentResized(ComponentEvent evt) {
        	setupDisplayPanel();
			parent.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			parent.mainWindow.setVisible(true);
        }
	};
	
	public StructPanel(String filename, int count, MainWindow mainWindow) {
		super();
		if(System.getProperty("os.name").toLowerCase().equals("mac"))
			pathSeparator = "/";
		else {
			pathSeparator = "\\";
		}
		this.filename = filename;
		this.count = count;
		this.count += 6;
		this.count /= 6;
		parent = mainWindow;
		parseName(filename);
		nameLabel = new JLabel(name);
		loadFromFileObject.setUsersCatalogPath(filename);
		displayPanel = new JPanel();
		setupBoard();
		setupDisplayPanel();
		setupButtons();
		setupLayout();
		parent.mainWindow.addComponentListener(componentAdapter);
	}	

	private void parseName(String filename) {
		String[] directories = filename.split(pathSeparator+""+pathSeparator);
		String file = directories[directories.length - 1];
		name = file.split(pathSeparator+".")[0];
	}
	
	private void setupButtons() {
		rotateBtn = new JButton (parent.rotateIcon);
		rotateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int mir = dir/4;
				dir++;
				dir = (byte) (mir*4 + dir%4);
				loadFromFileObject.setUsersCatalogPath(filename);
				if (mirrorBtn.getIcon() == parent.mirrorXIcon)
					mirrorBtn.setIcon(parent.mirrorYIcon);
				else
					mirrorBtn.setIcon(parent.mirrorXIcon);
				setupBoard();
				setupDisplayPanel();
				setupLayout();
				setVisible(true);
				parent.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				parent.mainWindow.setVisible(true);
			}
		});
		mirrorBtn = new JButton (parent.mirrorXIcon);
		mirrorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dir += 4;
				dir %= 8;
				loadFromFileObject.setUsersCatalogPath(filename);
				setupBoard();
				setupDisplayPanel();
				setupLayout();
				setVisible(true);
				parent.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				parent.mainWindow.setVisible(true);
			}
		});
	}
	
	private void setupLayout() {
		removeAll();
		repaint();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1,1,1,1);
		c.gridx = 0;
		c.gridy = 0;
		add(nameLabel, c);
		c.gridx = 1;
		c.weightx = 0.5;
		add(rotateBtn, c);
		c.gridx = 2;
		c.weightx = 0.5;
		add(mirrorBtn, c);
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 4;
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(5,5,5,5);
		add(displayPanel, c);
	}
	
	private void setupBoard() {
		board = loadFromFileObject.loadBoardFromFile("", dir);
		board.setListeners(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] position = e.getActionCommand().split(" ");
				int i = Integer.parseInt(position[0]);
				int j = Integer.parseInt(position[1]);
				parent.startStructListener(filename, i, j, dir);
			}
		});
		rows = board.getRows();
		cols = board.getCols();
	}
	
	private void setupDisplayPanel() {
		displayPanel.removeAll();
		displayPanel.setSize(parent.mainWindow.getSize().width/7, parent.mainWindow.getSize().height/count*4/5);
		int cellSideSize = parent.mainWindow.getSize().width/7/cols;
		if (cellSideSize > parent.mainWindow.getSize().height/count*4/5/rows)
			cellSideSize = parent.mainWindow.getSize().height/count*4/5/rows;
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
}
