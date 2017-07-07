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


import com.util.*;
import com.dao.ScoreDao;
import com.entity.*;

public class DetectionTimeItem extends JFrame {
	JSplitPane splitPane = new JSplitPane();
	BorderLayout borderLayout = new BorderLayout();
	GridLayout gridLayout = new GridLayout();
	FlowLayout flowLayout = new FlowLayout();
    JScrollPane scrollPane = new JScrollPane();
    JPanel panel_1 = new JPanel();
    JPanel panel_2 = new JPanel();
    JTable table = new JTable();
    SqlTableModel tablemodel;
    JLabel time = new JLabel("时间: ");
    JTextField time1 = new JTextField(15);
    JTextField time2 = new JTextField(15);
    JButton selectB = new JButton("查询");
    
    Score data = new Score();
    public DetectionTimeItem(int satelliteId) {
    	init(satelliteId);
    	setTitle("异常时间查询");
    	this.setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void init(int satelliteId) {
		setLayout(new FlowLayout(FlowLayout.CENTER/*,0,0*/));
		setSize(800, 530);
		Dimension frm=this.getSize();//得到当前窗口的大小
		splitPane.setLocation(100, 100);
		add(splitPane);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(scrollPane, JSplitPane.TOP);
        scrollPane.getViewport().add(table);
        splitPane.add(panel_2, JSplitPane.BOTTOM);

        gridLayout.setColumns(10);
        gridLayout.setRows(11);
        panel_2.setLayout(gridLayout);
        GridBagLayout gridbag = new GridBagLayout();
        panel_2.setLayout(gridbag);

        
        time.setFont(new Font("微软雅黑",2,15));
        
        panel_2.add(time,new GBC(1,0,1,1));
        panel_2.add(time1,new GBC(2,0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5,5,5,0));
        panel_2.add(time2,new GBC(2,1).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5,5,5,0));
        panel_2.add(selectB,new GBC(2,2).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5,5,5,0));
        add(panel_2);
        panel_1.setLayout(gridbag);
        selectB.setFont(new Font("微软雅黑",2,15));
        add(panel_1);
       
        splitPane.setDividerLocation(180);
 
        buildTable(satelliteId);
        
        selectB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				buildTable(time1.getText().trim(),time2.getText().trim(),satelliteId);			
			}        	
        });
	}
    
    public void buildTable(int satelliteId) {
		String[] name = { "卫星编号","时间", "异常得分"};
		ScoreDao sd=new ScoreDao();
		String sql="select avg(score)+stddev(score)*3 as score from DetectionScore where satelliteId="+satelliteId; //统计异常阈值
		List<Score> list=sd.find(sql);
		double threshold=list.get(0).getScore();
		String select_sql="select satelliteId,time,score from DetectionScore where satelliteId="+satelliteId+" and score>"+threshold+" order by time";  //统计超过阈值的异常时间
		tablemodel = new SqlTableModel(select_sql, name);
		table.setModel(tablemodel);
	}
	public void buildTable(String t1,String t2,int satelliteId) {
		if(t1==null||t1.equals("")||t2==null||t2.equals(null)){
			JOptionPane.showMessageDialog(null, "时间未输入,请重新输入!!!", "系统提示",
	                JOptionPane.ERROR_MESSAGE);
		}
		else if(ValidateTime.valiDateTimeWithLongFormat(t1)&&ValidateTime.valiDateTimeWithLongFormat(t2)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sdf.parse(t1);
				sdf.parse(t2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "时间格式输入错误,请重新输入!!!", "系统提示",
		                JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		String[] name = { "卫星编号","时间", "异常得分"};
		ScoreDao sd=new ScoreDao();
		String sql="select avg(score)+stddev(score)*3 as score from DetectionScore where satelliteId="+satelliteId; //统计异常阈值
		List<Score> list=sd.find(sql);
		double threshold=list.get(0).getScore();
		String select_sql="select satelliteId,time,score from DetectionScore where satelliteId="+satelliteId+" and score>"+threshold+" and time>=(to_date('"+t1+"','yyyy-MM-dd hh24:mi:ss')) and time<=(to_date('"+t2+"','yyyy-MM-dd hh24:mi:ss')) order by time";  //统计超过阈值的异常时间
		//System.out.println(select_sql);
		tablemodel = new SqlTableModel(select_sql, name);
		table.setModel(tablemodel);
		}
		else{
			JOptionPane.showMessageDialog(null, "时间格式输入错误,请重新输入!!!", "系统提示",
	                JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	
	
}
