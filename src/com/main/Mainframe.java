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
	Font font=new Font("宋体",2,15);//设置字体
	public Mainframe() {
		
		DetectionMenu = new JMenu("异常查询");
		DetectionMenu.setFont(font);
		DetectionMenu.setForeground(Color.black);//设置背景颜色
		PredictionMenu = new JMenu("实时预测");
		PredictionMenu.setFont(font);
		PredictionMenu.setForeground(Color.black);//设置背景颜色
		AssociateMenu = new JMenu("异常关联分析");
		AssociateMenu.setFont(font);
		AssociateMenu.setForeground(Color.black);//设置背景颜色
		menuBar = new JMenuBar();		
		toolBar = new JToolBar();	
		myAction = new Action(desktop);	
        setTitle("卫星异常实时检测与预测系统");
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

		
		JMenuItem detectionCurveItem = new JMenuItem("实时异常得分曲线显示");
		detectionCurveItem.setFont(font);
		detectionCurveItem.setForeground(Color.black);//设置背景颜色
		JMenuItem historyDetectionItem = new JMenuItem("历史异常得分查询");
		historyDetectionItem.setFont(font);
		historyDetectionItem.setForeground(Color.black);//设置背景颜色
		JMenuItem detectionTimeItem = new JMenuItem("异常时间查询");
		detectionTimeItem.setFont(font);
		detectionTimeItem.setForeground(Color.black);//设置背景颜色
		DetectionMenu.add(detectionCurveItem);
		DetectionMenu.add(historyDetectionItem);
		DetectionMenu.add(detectionTimeItem);
		menuBar.add(DetectionMenu);

		
		JMenuItem predictionCurveItem = new JMenuItem("实时预测值曲线显示");
		predictionCurveItem.setFont(font);
		predictionCurveItem.setForeground(Color.black);//设置背景颜色
		JMenuItem predictionValueItem = new JMenuItem("预测值查询");
		predictionValueItem.setFont(font);
		predictionValueItem.setForeground(Color.black);//设置背景颜色
		PredictionMenu.add(predictionCurveItem);
		PredictionMenu.add(predictionValueItem);
		menuBar.add(PredictionMenu);
		
		JMenuItem associateItem = new JMenuItem("异常关联规则挖掘");
		associateItem.setFont(font);
		associateItem.setForeground(Color.black);//设置背景颜色
		JMenuItem validateItem = new JMenuItem("异常关联规则验证");
		validateItem.setFont(font);
		validateItem.setForeground(Color.black);//设置背景颜色
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

