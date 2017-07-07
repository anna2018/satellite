package com.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultDesktopManager;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import com.dao.ScoreDao;
import com.entity.Score;


public class DetectionCurveApplet extends JApplet {

	JFreeChart chart1;
	DataGenerator dataGenerator;
	static int num = 0;
	static List<Double> data1 ;
	static TimeSeries timeseries1; 
	//static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
	static Date d=new Date();
	static Minute m=new Minute();
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
 public DetectionCurveApplet(){
	 if(timeseries1!=null){
		 timeseries1.clear();
	 }
	 if(data1!=null){
		 data1.clear();
	 }
	 if(dataGenerator!=null){
		 dataGenerator.restart();
	 }
	 dataGenerator=new DataGenerator();
	 data1 = new ArrayList<>();
	 num=0;
	 timeseries1=new TimeSeries("Data1");
 }
	public static void direction() throws Exception {
		File directory = new File("");//参数为空 
		String courseFile = directory.getCanonicalPath() ; 
		System.out.println(courseFile); 
	}
	public void stop(){
		dataGenerator.stop();
	}
	public void init(int satelliteId){
		ScoreDao sd=new ScoreDao();
		List<Score> score_list=sd.find("select min(time) as time from DetectionScore where satelliteId="+satelliteId);
		if(score_list.size()>0){
			d=score_list.get(0).getTime();
		}
		m=new Minute(d);
		getInfo(satelliteId);

		updateDataset();
		
		XYDataset dataset1 = new TimeSeriesCollection(timeseries1); 
		
		chart1 = ChartFactory.createTimeSeriesChart( 
				"Outlier", // 图表标题  
				"time", // 目录轴的显示标签--横轴  
				"range", // 数值轴的显示标签--纵轴  
				dataset1, // 数据集  
				false,
				false, // 是否生成工具  
				false // 是否生成URL链接  
				); 
		
		
		double[] min = new double[2];
		double[] max = new double[2];
		min[0] = min[1] = Double.MAX_VALUE;
		max[0] = max[1] = Double.MIN_VALUE;
		
		for(int i=0; i<data1.size(); i++){
			double value1 = data1.get(i);
			if(value1 < min[0]) min[0] = value1;
			if(value1 > max[0]) max[0] = value1;
		}
		//System.out.println(min[0]+"\t"+max[0]+"\t"+min[1]+"\t"+max[1]);

		
		XYPlot plot1 = (XYPlot) chart1.getPlot();
		DateAxis xAxis1 = (DateAxis) plot1.getDomainAxis();
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
		xAxis1.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 1, formatter));
		xAxis1.setFixedAutoRange(600000D);//设置时间轴显示的数据
		NumberAxis yAxis1 = (NumberAxis) plot1.getRangeAxis();
		//yAxis1.setRange(min[0], max[0]);
		yAxis1.setAutoRange(true);      //自动设置数据轴数据范围
		
		
		getContentPane().setLayout(new GridLayout(1,1));
		ChartPanel chartPanel1 = new ChartPanel(chart1);
		
		getContentPane().add(chartPanel1);
		dataGenerator.start();
	}

	class DataGenerator extends Timer implements ActionListener {
		private static final long serialVersionUID = 3977867288743720504L;
		String equID;
		int totalTask;
		String[][] strTask;
		
		public void actionPerformed(ActionEvent actionevent) {
			addTotalObservation();
		}
		
		DataGenerator() {
			super(1000, null);
			addActionListener(this);
			//System.out.println("super");
		}
		
	}
	
	private void addTotalObservation() {
		//设置新的数据集
		updateDataset();
		chart1.getXYPlot().setDataset(new TimeSeriesCollection(timeseries1));
		
		if (chart1 != null) {
			chart1.fireChartChanged();
		}
	}
	
	public static void updateDataset(){
		if(num > 0){
			timeseries1.delete(0, 0);
			long t = m.getFirstMillisecond()+60000;
			Date date = new Date(t);
			m = new Minute(date);
			timeseries1.addOrUpdate(m, data1.get(num));
			num++;
		} 
		else{
			for( ; num<200; num++){
				long t = m.getFirstMillisecond()+60000;
				Date date = new Date(t);
				m = new Minute(date);
				timeseries1.addOrUpdate(m, data1.get(num));
			}
		}
	}
	
	public void paint(Graphics g) {
		if (chart1 != null) {
			chart1.draw((Graphics2D) g, getBounds());
		}
	}
	
	public static void getInfo(int satelliteId){
		ScoreDao sd=new ScoreDao();
		String sql="select * from DetectionScore where satelliteId="+satelliteId+" order by time";
		List<Score> list=sd.find(sql);
		for(int i=0;i<list.size();i++){
			data1.add(list.get(i).getScore());
		}
	}
}
