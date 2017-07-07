package com.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultDesktopManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import com.util.*;


import com.dao.ScoreDao;
import com.entity.*;

public class DetectionItem extends JFrame {
	String tableName="Test_Apriori";
	JSplitPane splitPane = new JSplitPane();
	BorderLayout borderLayout = new BorderLayout();
	GridLayout gridLayout = new GridLayout();
	FlowLayout flowLayout = new FlowLayout();
    JScrollPane scrollPane = new JScrollPane();
    JPanel panel_1 = new JPanel();
    JPanel panel_2 = new JPanel();
    JTable table = new JTable();
    SqlTableModel tablemodel;
    
    Score data = new Score();
    public DetectionItem(String detectionName) {
    	tableName=detectionName;
    	init();
    	setTitle("表现出异常的规则");
    	this.setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void init() {
		setLayout(new FlowLayout(FlowLayout.CENTER/*,0,0*/));
		setSize(830, 500);
		Dimension frm=this.getSize();//得到当前窗口的大小
		splitPane.setLocation(100, 100);
		table.setPreferredScrollableViewportSize(new Dimension(700,550));
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		add(splitPane);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(scrollPane, JSplitPane.TOP);
        scrollPane.getViewport().add(table);
        splitPane.add(panel_2, JSplitPane.BOTTOM);

        gridLayout.setColumns(20);
        gridLayout.setRows(11);
        panel_2.setLayout(gridLayout);
        GridBagLayout gridbag = new GridBagLayout();
        panel_2.setLayout(gridbag);
        add(panel_2);
        panel_1.setLayout(gridbag);
        
        add(panel_1);
       
        splitPane.setDividerLocation(400);
 
        buildTable();
        
        
        
	}

	public void buildTable() {
		String[] name = { "关联规则"};
		String select_sql="select rule from "+tableName;  //统计超过阈值的异常时间
		//System.out.println(select_sql);
		tablemodel = new SqlTableModel(select_sql, name);
		table.setModel(tablemodel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
	}
	
//	public static void main(String[] args){
//		DetectionItem associateItem = new DetectionItem();
//		associateItem.setLocation(100,100);
//		associateItem.setSize(1000, 650);
//	}
	
	
	
}
