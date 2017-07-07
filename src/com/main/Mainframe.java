package com.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;



public class Mainframe extends JFrame {
	private static final int DEFAULT_WIDTH = 700;
	private static final int DEFAULT_HEIGTH = 500;
	JDesktopPane desktop = new JDesktopPane();
	private JMenuBar menuBar;
	private JMenu startMenu;
	private JMenu DetectionMenu;
	private JMenu PredictionMenu;
	private JMenu AssociateMenu;
	Action myAction;
	
	private JToolBar toolBar;
	Font font=new Font("����",2,15);//��������
	public Mainframe() {
		
		DetectionMenu = new JMenu("�쳣��ѯ");
		DetectionMenu.setFont(font);
		DetectionMenu.setForeground(Color.black);//���ñ�����ɫ
		PredictionMenu = new JMenu("ʵʱԤ��");
		PredictionMenu.setFont(font);
		PredictionMenu.setForeground(Color.black);//���ñ�����ɫ
		AssociateMenu = new JMenu("�쳣��������");
		AssociateMenu.setFont(font);
		AssociateMenu.setForeground(Color.black);//���ñ�����ɫ
		menuBar = new JMenuBar();		
		toolBar = new JToolBar();	
		myAction = new Action(desktop);	
        setTitle("�����쳣ʵʱ�����Ԥ��ϵͳ");
        add(desktop, BorderLayout.CENTER);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGTH);
		setLocation(50, 50);
		addMenu();
		loadBackgroundImage();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	private void addMenu(){	
		setJMenuBar(menuBar);

		
		JMenuItem detectionCurveItem = new JMenuItem("ʵʱ�쳣�÷�������ʾ");
		detectionCurveItem.setFont(font);
		detectionCurveItem.setForeground(Color.black);//���ñ�����ɫ
		JMenuItem historyDetectionItem = new JMenuItem("��ʷ�쳣�÷ֲ�ѯ");
		historyDetectionItem.setFont(font);
		historyDetectionItem.setForeground(Color.black);//���ñ�����ɫ
		JMenuItem detectionTimeItem = new JMenuItem("�쳣ʱ���ѯ");
		detectionTimeItem.setFont(font);
		detectionTimeItem.setForeground(Color.black);//���ñ�����ɫ
		DetectionMenu.add(detectionCurveItem);
		DetectionMenu.add(historyDetectionItem);
		DetectionMenu.add(detectionTimeItem);
		menuBar.add(DetectionMenu);

		
		JMenuItem predictionCurveItem = new JMenuItem("ʵʱԤ��ֵ������ʾ");
		predictionCurveItem.setFont(font);
		predictionCurveItem.setForeground(Color.black);//���ñ�����ɫ
		JMenuItem predictionValueItem = new JMenuItem("Ԥ��ֵ��ѯ");
		predictionValueItem.setFont(font);
		predictionValueItem.setForeground(Color.black);//���ñ�����ɫ
		PredictionMenu.add(predictionCurveItem);
		PredictionMenu.add(predictionValueItem);
		menuBar.add(PredictionMenu);
		
		JMenuItem associateItem = new JMenuItem("�쳣���������ھ�");
		associateItem.setFont(font);
		associateItem.setForeground(Color.black);//���ñ�����ɫ
		JMenuItem validateItem = new JMenuItem("�쳣����������֤");
		validateItem.setFont(font);
		validateItem.setForeground(Color.black);//���ñ�����ɫ
		AssociateMenu.add(associateItem);
		AssociateMenu.add(validateItem);
		menuBar.add(AssociateMenu);
		

		
		detectionCurveItem.addActionListener(myAction);
		historyDetectionItem.addActionListener(myAction);
		detectionTimeItem.addActionListener(myAction);

		predictionCurveItem.addActionListener(myAction);
		predictionValueItem.addActionListener(myAction);
		
		associateItem.addActionListener(myAction);
		validateItem.addActionListener(myAction);
	}
	protected void loadBackgroundImage(){
        ImageIcon icon = new ImageIcon(".\\images\\background.jpg");
        JLabel desk = new JLabel(icon);
        desk.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
        desktop.add(desk,new Integer(Integer.MIN_VALUE));
	}
	
	public static void main(String[] args) {
		Mainframe frame = new Mainframe();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.show();
    }
}

