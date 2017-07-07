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



public class Mainframe2 extends JFrame {
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
	public Mainframe2() {
		startMenu = new JMenu("菜单");
		startMenu.setFont(font);
		startMenu.setForeground(Color.black);//设置背景颜色
		
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
//		JMenuItem registerItem = new JMenuItem("管理员登录");
//		registerItem.setFont(font);
//		registerItem.setForeground(Color.black);//设置背景颜色
		JMenuItem startItem = new JMenuItem("开启异常检测子系统");
		startItem.setFont(font);
		startItem.setForeground(Color.black);//设置背景颜色
		JMenuItem startItem2 = new JMenuItem("开启参数预测子系统");
		startItem2.setFont(font);
		startItem2.setForeground(Color.black);//设置背景颜色
		JMenuItem closeItem = new JMenuItem("关闭系统");
		closeItem.setFont(font);
		closeItem.setForeground(Color.black);//设置背景颜色
		JMenuItem newItem = new JMenuItem("初始化系统");
		newItem.setFont(font);
		newItem.setForeground(Color.black);//设置背景颜色
//		startMenu.add(registerItem);
		startMenu.add(startItem);
		startMenu.add(startItem2);
		startMenu.add(closeItem);
		startMenu.add(newItem);	
		menuBar.add(startMenu);
		
		
		
		startItem.addActionListener(myAction);
		startItem2.addActionListener(myAction);
		closeItem.addActionListener(myAction);
		newItem.addActionListener(myAction);
		
		
	}
	protected void loadBackgroundImage(){
        ImageIcon icon = new ImageIcon(".\\images\\background.jpg");
        JLabel desk = new JLabel(icon);
        desk.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
        desktop.add(desk,new Integer(Integer.MIN_VALUE));
	}
	
//	public static void main(String[] args) {
//		Mainframe2 frame = new Mainframe2();
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//		frame.show();
//    }
}

