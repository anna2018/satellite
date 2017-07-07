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

public class AssociationItem extends JFrame {
	String tableName="yichang_Apriori";
	JSplitPane splitPane = new JSplitPane();
	BorderLayout borderLayout = new BorderLayout();
	GridLayout gridLayout = new GridLayout();
	FlowLayout flowLayout = new FlowLayout();
    JScrollPane scrollPane = new JScrollPane();
    JPanel panel_1 = new JPanel();
    JPanel panel_2 = new JPanel();
    JTable table = new JTable();
    SqlTableModel tablemodel;
    JLabel string = new JLabel("支持度: ");
    JTextField support = new JTextField(10);
    JLabel attribute = new JLabel("置信度: ");
    JTextField confidence = new JTextField(10);
    JButton selectB = new JButton("查询");
    
    Score data = new Score();
    public AssociationItem(int satelliteId) {
    	init(satelliteId);
    	setTitle("异常关联规则挖掘");
    	this.setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void init(int satelliteId) {
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
        panel_2.add(string,new GBC(1,0,1,1));
        panel_2.add(support,new GBC(2,0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5,5,5,0));
        panel_2.add(attribute,new GBC(1,2,1,1));
        panel_2.add(confidence,new GBC(2,2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5,5,5,0));
        panel_2.add(selectB,new GBC(2,3).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5,5,5,0));
        add(panel_2);
        panel_1.setLayout(gridbag);
        selectB.setFont(new Font("微软雅黑",2,15));
        
        add(panel_1);
       
        splitPane.setDividerLocation(400);
 
        buildTable(satelliteId);
        
        selectB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				buildTable(support.getText().trim(),confidence.getText().trim(),satelliteId);			
			}        	
        });
        
        
	}

	public void buildTable(int satelliteId) {
		String[] name = { "关联规则", "支持度","置信度"};
		String select_sql="select rule,support,confidence from "+tableName+" where satelliteId="+satelliteId+" order by support desc";  //统计超过阈值的异常时间
		System.out.println(select_sql);
		tablemodel = new SqlTableModel(select_sql, name);
		table.setModel(tablemodel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
	}
	public void buildTable(String support,String confidence,int satelliteId) {
		if(support==null||support.equals("")||confidence==null||confidence.equals("")){
			JOptionPane.showMessageDialog(null, "支持度或置信度未输入,请重新输入!!!", "系统提示",
	                JOptionPane.ERROR_MESSAGE);
		}
		else{
		String[] name = { "关联规则", "支持度","置信度"};
		String select_sql="select rule,support,confidence from "+tableName+" where satelliteId="+satelliteId+" and support>="+support+" and confidence>="+confidence+" order by support desc ";  //统计超过阈值的异常时间
		//System.out.println(select_sql);
		tablemodel = new SqlTableModel(select_sql, name);
		table.setModel(tablemodel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
		}
	}
//	public static void main(String[] args){
//		AssociationItem associateItem = new AssociationItem(188);
//		associateItem.setLocation(100,100);
//		associateItem.setSize(1000, 650);
//	}
	
	
	
}
