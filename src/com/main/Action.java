package com.main;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DesktopManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.dao.DBConnection;
import com.detection.Detection2;
import com.util.GBC;
import com.util.ValidateTime;
import com.associate.Apriori;
import com.associate.Discretization;
import com.dao.*;
import com.entity.*;
import com.predict.Predict;

public class Action extends AbstractAction {
	JDesktopPane desktop = null;
	private String EventName = "";
	private DesktopManager desktopManager;
	String tableName="satellite188_1";
	String preprocessingName = "DataPreprocessing3";
	String knnName = "KnnNeighbor3";
	String snnName = "SnnNeighbor3";
	String meanName = "MeanNeighbor3";
	String scoreName = "DetectionScore3";
	String predictName="prediciton3";
	boolean flag = false;
	int first=0;
	int second=0;
	public Action(JDesktopPane desktop) {
		this.desktop = desktop;
		desktopManager = desktop.getDesktopManager();
	}

	public void actionPerformed(ActionEvent e) {
		int num = 0;
		String com = e.getActionCommand();
		if (com.equals("开启异常检测子系统")) {
			// frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			JFrame aFrame1 = new JFrame("开启系统");
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string = new JLabel("卫星编号: ");
			JButton selectB = new JButton("开始异常检测 子系统");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 0, 1, 1));
			panel_1.add(attribute, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 110);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					if (attribute.getSelectedItem() != null) {
						JOptionPane.showMessageDialog(null, "系统已开启!!!", "系统提示", JOptionPane.INFORMATION_MESSAGE);
						aFrame1.dispose();
						int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
						flag = true;
						if(first==0){
							first=1;
							Detection2.calculateScore(tableName,preprocessingName,knnName,snnName,meanName,scoreName,satelliteId);
						}
					} else {
						JOptionPane.showMessageDialog(null, "卫星编号输入错误,请重新输入!!!", "系统提示", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		if (com.equals("开启参数预测子系统")) {
			// frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			JFrame aFrame1 = new JFrame("开启系统");
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string = new JLabel("卫星编号: ");
			JButton selectB = new JButton("开始参数预测 子系统");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 0, 1, 1));
			panel_1.add(attribute, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 110);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					if (attribute.getSelectedItem() != null) {
						JOptionPane.showMessageDialog(null, "系统已开启!!!", "系统提示", JOptionPane.INFORMATION_MESSAGE);
						aFrame1.dispose();
						int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
						flag = true;
						if(second==0){
							second=1;
							Predict.Prediction(preprocessingName, predictName,satelliteId);
						}
					} else {
						JOptionPane.showMessageDialog(null, "卫星编号输入错误,请重新输入!!!", "系统提示", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		if (com.equals("关闭系统")) {
			JFrame aFrame1 = new JFrame("关闭系统");
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string = new JLabel("卫星编号: ");
			JButton selectB = new JButton("开始");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 0, 1, 1));
			panel_1.add(attribute, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 100);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					if (attribute.getSelectedItem() != null) {
						int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
							flag = false;
							System.exit(0);
							JOptionPane.showMessageDialog(null, "系统已关闭!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "卫星编号输入错误,请重新输入!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		if (com.equals("初始化系统")) {
			PreparedStatement pstmt = null;
			Connection connection = null;
			DBConnection con = new DBConnection();
			try {
				connection = con.getConnection();
				String sql = "delete from " + preprocessingName + "; delete from " + knnName + "; delete from " + snnName
						+ "; delete from " + meanName + ";delete from " + scoreName+"; delete from "+predictName;
				// System.out.println(sql);
				pstmt = connection.prepareStatement(sql);
				// pstmt.execute();
				JOptionPane.showMessageDialog(null, "初始化系统已成功!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "初始化系统出现错误,请重新操作!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
				e2.printStackTrace();
			}
		}

		if (com.equals("实时异常得分曲线显示")) {
			JFrame aFrame1 = new JFrame("实时异常得分曲线显示");
			// JTextField attribute = new JTextField(10);
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string = new JLabel("卫星编号: ");
			JButton selectB = new JButton("查询");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 0, 1, 1));
			panel_1.add(attribute, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 100);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					if (attribute.getSelectedItem() != null) {
						JFrame aFrame = new JFrame("实时异常得分曲线显示");
						DetectionCurveApplet applet = new DetectionCurveApplet();
						aFrame.add(applet, BorderLayout.CENTER);
						aFrame.setSize(800, 500);
						aFrame.setLocation(100, 100);
						int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
						applet.init(satelliteId);
						aFrame.setVisible(true);
						aFrame.addWindowListener(new WindowAdapter() {
							public void windowClosing(WindowEvent e) {
								applet.removeAll();
								applet.stop();
								applet.destroy();
								aFrame.removeAll();
								aFrame.dispose();
							}
						});
					} else {
						JOptionPane.showMessageDialog(null, "卫星编号输入错误,请重新输入!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}

		if (com.equals("历史异常得分查询")) {
			JFrame aFrame1 = new JFrame("历史异常得分查询");
			// JTextField attribute = new JTextField(10);
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string = new JLabel("卫星编号: ");
			JButton selectB = new JButton("查询");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 0, 1, 1));
			panel_1.add(attribute, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 100);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					if (attribute.getSelectedItem() != null) {
						int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
						HistoryDetectionItem historyDetectionItem = new HistoryDetectionItem(satelliteId);
						historyDetectionItem.setLocation(100, 100);
					} else {
						JOptionPane.showMessageDialog(null, "卫星编号输入错误,请重新输入!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		if (com.equals("异常时间查询")) {
			JFrame aFrame1 = new JFrame("异常时间查询");
			// JTextField attribute = new JTextField(10);
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string = new JLabel("卫星编号: ");
			JButton selectB = new JButton("查询");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 0, 1, 1));
			panel_1.add(attribute, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 100);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					if (attribute.getSelectedItem() != null) {
						int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
						DetectionTimeItem detectionTimeItem = new DetectionTimeItem(satelliteId);
						detectionTimeItem.setLocation(100, 100);
					} else {
						JOptionPane.showMessageDialog(null, "卫星编号输入错误,请重新输入!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}

		if (com.equals("实时预测值曲线显示")) {
			JFrame aFrame1 = new JFrame("实时预测值曲线显示");
			JTextField attribute = new JTextField(10);
			JLabel string = new JLabel("属性: ");
			JComboBox attribute2 = new JComboBox();
			attribute2.addItem("188");
			attribute2.addItem("5052");
			attribute2.addItem("5564");
			JLabel string2 = new JLabel("卫星编号: ");
			JButton selectB = new JButton("查询");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 0, 1, 1));
			panel_1.add(attribute, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(string2, new GBC(2, 1, 1, 1));
			panel_1.add(attribute2, new GBC(2, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 3).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 100);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					if ((attribute.getText() != null || !attribute.getText().trim().equals(""))&&attribute2.getSelectedItem() != null) {
						int satelliteId = Integer.parseInt((String) attribute2.getSelectedItem());
						PredictionCurveApplet applet = new PredictionCurveApplet(attribute.getText().trim(),satelliteId);
						JFrame aFrame = new JFrame("实时预测值曲线显示");
						aFrame.add(applet, BorderLayout.CENTER);
						aFrame.setSize(1000, 700);
						aFrame.setLocation(100, 100);
						applet.init();
						aFrame.setVisible(true);
						aFrame1.dispose();
						aFrame.addWindowListener(new WindowAdapter() {
							public void windowClosing(WindowEvent e) {
								applet.stop();
								aFrame.dispose();
							}
						});
					} else {
						JOptionPane.showMessageDialog(null, "属性输入错误,请重新输入!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			// aFrame.addWindowListener(new WindowAdapter(){
			// public void windowClosing(WindowEvent e) {
			// System.exit(0);
			// }
			// });
		}

		if (com.equals("预测值查询")) {
			JFrame aFrame1 = new JFrame("预测值查询");
			// JTextField attribute = new JTextField(10);
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string = new JLabel("卫星编号: ");
			JButton selectB = new JButton("查询");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 0, 1, 1));
			panel_1.add(attribute, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 100);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					if (attribute.getSelectedItem() != null) {
						int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
						PredictionValueItem predictionValueItem = new PredictionValueItem(satelliteId);
						predictionValueItem.setLocation(100, 100);
					} else {
						JOptionPane.showMessageDialog(null, "卫星编号输入错误,请重新输入!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}

		if (com.equals("异常关联规则挖掘")) {
			JFrame aFrame1 = new JFrame("异常关联规则挖掘");
			JTextField support = new JTextField(15);
			JLabel string = new JLabel("最小支持度: ");
			JTextField confidence = new JTextField(15);
			JLabel string2 = new JLabel("最小置信度: ");
			JTextField fenduan = new JTextField(15);
			JLabel fenduan_string = new JLabel("分段个数: ");
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string3 = new JLabel("卫星编号: ");
			JButton selectB = new JButton("挖掘");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 1, 1, 1));
			panel_1.add(support, new GBC(1, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(string2, new GBC(2, 1, 1, 1));
			panel_1.add(confidence, new GBC(2, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(fenduan_string, new GBC(1, 2, 1, 1));
			panel_1.add(fenduan, new GBC(2, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(string3, new GBC(1, 2, 1, 1));
			panel_1.add(attribute, new GBC(2, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 3).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			// final JProgressBar pBar_appriori1 = new JProgressBar();
			// pBar_appriori1.setStringPainted(true);
			// pBar_appriori1.setBounds(142, 92, 190, 15);
			// panel_1.add(pBar_appriori1,new
			// GBC(2,4).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5,5,5,0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(300, 200);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
					String detection = "detectionscore";
					String preprocessingName = "dataPreprocessing188";
					String tableName = "yichang";
					String fen = fenduan.getText();
					if (fen == null || fen.equals("")||support.getText()==null||support.getText().equals("")||confidence.getText()==null||confidence.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "分段数未输入,请重新输入!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					} else {
						int fenduanNum = Integer.parseInt(fen);
						writeFenduan(fenduanNum);
						Discretization d = new Discretization(tableName, Integer.parseInt(fenduan.getText()), detection,
								preprocessingName,satelliteId);
						try {
							Apriori.getApriori(tableName, Double.parseDouble(support.getText()),
									Double.parseDouble(confidence.getText()),satelliteId);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						aFrame1.dispose();
						AssociationItem associateItem = new AssociationItem(satelliteId);
						associateItem.setLocation(100, 100);
						associateItem.setSize(1000, 650);
					}
				}
			});
		}
		if (com.equals("异常关联规则验证")) {
			JFrame aFrame1 = new JFrame("异常关联规则验证");
			JLabel string = new JLabel("时间: ");
			JTextField time1 = new JTextField(15);
			JTextField time2 = new JTextField(15);
			JComboBox attribute = new JComboBox();
			attribute.addItem("188");
			attribute.addItem("5052");
			attribute.addItem("5564");
			JLabel string3 = new JLabel("卫星编号: ");
			JButton selectB = new JButton("验证");
			JPanel panel_1 = new JPanel();
			panel_1.add(string, new GBC(1, 1, 1, 1));
			panel_1.add(time1, new GBC(1, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(time2, new GBC(2, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(string3, new GBC(1, 2, 1, 1));
			panel_1.add(attribute, new GBC(2, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5, 5, 5, 0));
			panel_1.add(selectB, new GBC(2, 3).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5, 5, 5, 0));
			// final JProgressBar pBar_appriori1 = new JProgressBar();
			// pBar_appriori1.setStringPainted(true);
			// pBar_appriori1.setBounds(142, 92, 190, 15);
			// panel_1.add(pBar_appriori1,new
			// GBC(2,4).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(5,5,5,0));
			aFrame1.add(panel_1);
			selectB.setFont(new Font("微软雅黑", 2, 15));
			aFrame1.setSize(450, 100);
			aFrame1.setLocation(100, 100);
			aFrame1.setVisible(true);
			selectB.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					String preprocessingName = "dataPreprocessing188";
					String tableName = "Test";
					String ruleName = "yichang_Apriori";
					String detectionName="Test_Apriori";
					String t1 = time1.getText().trim();
					String t2 = time2.getText().trim();
					if (ValidateTime.valiDateTimeWithLongFormat(t1) && ValidateTime.valiDateTimeWithLongFormat(t2)&&attribute.getSelectedItem()!=null) {
						int satelliteId = Integer.parseInt((String) attribute.getSelectedItem());
						int fenduan = getFenduan();
						Discretization d = new Discretization(tableName, fenduan, preprocessingName, t1, t2,satelliteId);
						boolean flag = false;
						try {
							flag = Apriori.validate(tableName, ruleName, time1.getText().trim(),
									time2.getText().trim(),satelliteId,detectionName);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						aFrame1.dispose();
						if (flag) {
							//double[] support=getSupport();
//							try {
//								Apriori.getApriori_detection(tableName, support[0],support[1],satelliteId);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
							JOptionPane.showMessageDialog(null, "出现异常!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
							DetectionItem associateItem = new DetectionItem(detectionName);
							associateItem.setLocation(100, 100);
							associateItem.setSize(1000, 650);
						} else {
							JOptionPane.showMessageDialog(null, "未出现异常!!!", "系统提示", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "时间格式输入错误,请重新输入!!!", "系统提示", JOptionPane.ERROR_MESSAGE);
					}

				}
			});
		}

	}

	public void writeFenduan(int fenduan) {
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter("fenduan.txt", false));
			wr.write(fenduan + "\n");
			wr.flush();
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getFenduan() {
		File file = new File("fenduan.txt");
		BufferedReader reader = null;
		int fenduan = 3;
		try {
			reader = new BufferedReader(new FileReader(file));
			String c;
			while ((c = reader.readLine()) != null) {
				fenduan = Integer.parseInt(c);
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fenduan;
	}
	public void writeSupport(double support,double confidence) {
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter("support.txt", false));
			wr.write(support + ","+confidence + "\n");
			wr.flush();
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static double[] getSupport() {
		File file = new File("support.txt");
		BufferedReader reader = null;
		double support[] = {0.9,0.9};
		try {
			reader = new BufferedReader(new FileReader(file));
			String c;
			while ((c = reader.readLine()) != null) {
				String[] array=c.split(",");
				support[0] = Double.parseDouble(array[0]);
				support[1] = Double.parseDouble(array[1]);
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return support;
	}
}
