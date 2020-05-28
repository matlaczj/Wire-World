package com.example.universalautomaton;

import java.io.File;
import java.util.regex.Matcher;

import javax.swing.*;

public class StructPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel nameLabel;
	private JButton rotateBtn;
	private JButton mirrorBtn;
	private JPanel displayPanel;
	private Board board;
	private String name;
	private String filename;
	private LoadBoardFromFile loadFromFileObject = new LoadBoardFromFile(C.WW);
	
	public StructPanel(String filename) {
		super();
		this.filename = filename;
		parseName(filename);
		loadFromFileObject.setUsersCatalogPath(filename);
		board = loadFromFileObject.loadBoardFromFile("");
		setupButtons();
		setupLayout();
		setupDisplayPanel();
	}	

	private void parseName(String filename) {
		String[] directories = filename.split("\\\\");
		String file = directories[directories.length - 1];
		name = file.split("\\.")[0];
	}
	
	private void setupButtons() {
		// TODO Auto-generated method stub
		
	}
	
	private void setupLayout() {
		// TODO Auto-generated method stub
		nameLabel = new JLabel(name);
		add(nameLabel);
	}
	
	private void setupDisplayPanel() {
		// TODO Auto-generated method stub
		
	}
}
