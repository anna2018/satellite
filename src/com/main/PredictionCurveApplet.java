package com.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

import com.dao.PredictionDao;
import com.dao.ScoreDao;
import com.entity.Prediction;
import com.entity.Score;
import com.main.DetectionCurveApplet.DataGenerator;




public class PredictionCurveApplet extends JApplet {
	static String tableName="prediction188_1";
	JFreeChart chart1;
	DataGenerator dataGenerator=new DataGenerator();
	static int num ;
	static List<Double> data1;
	static List<Double> data2 ;
	static List<Double> data3 ;
	static List<Double> data4;
	static List<Date> data5 ;
	static TimeSeries timeseries1 ; //增加一条走势曲线
	static TimeSeries timeseries2 ; //增加一条走势曲线
	static TimeSeries timeseries3 ; //增加一条走势曲线
	static TimeSeries timeseries4 ; //增加一条走势曲线
	static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
	static Date d=new Date();
	static Minute m=new Minute();
	//static Millisecond ms = new Millisecond();
	//static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
	static String attribute;
	static int satelliteId;
	public PredictionCurveApplet(String str,int sa){
		attribute=str;
		satelliteId=sa;
		if(timeseries1!=null){
			 timeseries1.clear();
		 }
		if(timeseries2!=null){
			 timeseries2.clear();
		 }
		if(timeseries3!=null){
			 timeseries3.clear();
		 }
		if(timeseries4!=null){
			 timeseries4.clear();
		 }
		 if(data1!=null){
			 data1.clear();
		 }
		 if(data2!=null){
			 data2.clear();
		 }
		 if(data3!=null){
			 data3.clear();
		 }
		 if(data4!=null){
			 data4.clear();
		 }
		 if(data5!=null){
			 data5.clear();
		 }
		 if(dataGenerator!=null){
			 dataGenerator.restart();
		 }
		 dataGenerator=new DataGenerator();
		 data1 = new ArrayList<>();
		 data2 = new ArrayList<>();
		 data3 = new ArrayList<>();
		 data4 = new ArrayList<>();
		 data5 = new ArrayList<>();
		 num=0;
		 timeseries1 = new TimeSeries("predictedValue"); //增加一条走势曲线
		 timeseries2 = new TimeSeries("predictedUpper"); //增加一条走势曲线
		 timeseries3 = new TimeSeries("predictedLower"); //增加一条走势曲线
		 timeseries4 = new TimeSeries("actualvalue"); //增加一条走势曲线
		 
	}
	public static void direction() throws Exception {
		File directory = new File("");//参数为空 
		String courseFile = directory.getCanonicalPath() ; 
		//System.out.println(courseFile); 
	}
	public void stop(){
		dataGenerator.stop();
	}
	
	public void init(){
		PredictionDao sd=new PredictionDao();
		String s="select min(time) as time from "+tableName+" where attribute='"+attribute+"' and satelliteId="+satelliteId;
		List<Prediction> score_list=sd.find(s);
		d=score_list.get(0).getTime();
		System.out.println(d.toLocaleString());
		m=new Minute(d);
		//System.out.println(m.toString());
		getInfo();

		updateDataset();
		
		
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		  //将所有走势曲线加入到时间条中
		timeseriescollection.addSeries(timeseries1);
		timeseriescollection.addSeries(timeseries2);
		timeseriescollection.addSeries(timeseries3);
		timeseriescollection.addSeries(timeseries4);
		//XYDataset dataset1 = timeseriescollection; 
		//XYDataset dataset2 = new TimeSeriesCollection(timeseries2); 
		//XYDataset dataset3 = new TimeSeriesCollection(timeseries3); 
		chart1 = ChartFactory.createTimeSeriesChart( 
				"Value", // 图表标题  
				"time", // 目录轴的显示标签--横轴  
				"range", // 数值轴的显示标签--纵轴  
				timeseriescollection, // 数据集  
				true,  // 是否显示图例
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
		//SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		//xAxis1.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 1, sdf));
		//xAxis1.setFixedAutoRange(600000D);//设置时间轴显示的数据
		//xAxis1.setAutoRangeMinimumSize(20);
		//xAxis1.setDateFormatOverride(sdf);
		NumberAxis yAxis1 = (NumberAxis) plot1.getRangeAxis();
		//yAxis1.setRange(min[0], max[0]);
		yAxis1.setAutoRange(true);      //自动设置数据轴数据范围
		yAxis1.setVisible(true);//设置是否显示数据轴
		plot1.setDomainGridlinesVisible(false); 
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
			super(2000, null);
			addActionListener(this);
			//System.out.println("super");
		}
		
	}
	
	private void addTotalObservation() {
		//设置新的数据集
		updateDataset();
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		  //将所有走势曲线加入到时间条中
		timeseriescollection.addSeries(timeseries1);
		timeseriescollection.addSeries(timeseries2);
		timeseriescollection.addSeries(timeseries3);
		timeseriescollection.addSeries(timeseries4);
		chart1.getXYPlot().setDataset(timeseriescollection);
		

		if (chart1 != null) {
			chart1.fireChartChanged();
		}
	}
	
	public static void updateDataset(){
		if(num > 0){
			timeseries1.delete(0, 0);
			timeseries2.delete(0, 0);
			timeseries3.delete(0, 0);
			timeseries4.delete(0, 0);
			//System.out.println(m.toString());
			//long t = m.getFirstMillisecond()+60000*20;
			//Date date = new Date(t);
			m = new Minute(data5.get(num));
			timeseries1.add(m, data1.get(num));
			timeseries2.add(m, data2.get(num));
			timeseries3.add(m, data3.get(num));
			timeseries4.add(m, data4.get(num));
			num++;
		} 
		else{
			for( ; num<20; num++){
				//System.out.println(m.toString());
				//long t = m.getFirstMillisecond()+60000*20;
				//Date date = new Date(t);
				m = new Minute(data5.get(num));
				timeseries1.add(m, data1.get(num));
				timeseries2.add(m, data2.get(num));
				timeseries3.add(m, data3.get(num));
				timeseries4.add(m, data4.get(num));
			}
		}
	}
	
	public void paint(Graphics g) {
		if (chart1 != null) {
			chart1.draw((Graphics2D) g, getBounds());
		}
	}
	
	public static void getInfo(){
		PredictionDao sd=new PredictionDao();
		String sql="select * from "+tableName+" where attribute='"+attribute+"' and satelliteId="+satelliteId+" order by time";
		//System.out.println(sql);
		List<Prediction> list=sd.find(sql);
		for(int i=0;i<list.size();i++){
			data1.add(list.get(i).getPredictedvalue());
			data2.add(list.get(i).getUpper());
			data3.add(list.get(i).getLower());
			data4.add(list.get(i).getActualvalue());
			data5.add(list.get(i).getTime());
		}
	}

}

