package com.associate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dao.DBConnection;
import com.dao.PredictionDao;
import com.dao.RuleDao;
import com.dao.SatellitePreprocessingDao;
import com.dao.ScoreDao;
import com.entity.Rule;
import com.entity.Satellite_Preprocessing;
import com.entity.Score;
import com.main.AssociationItem;
import com.util.CreateTable;
import com.util.TableExist;
import com.util.Tmid;

public class Apriori {
	private  double SUPPORT; // 支持度阈值
	private  double CONFIDENCE;
	private String tableName;
	// 置信度阈值
	private final static String ITEM_SPLIT = ",";
	// 项之间的分隔符
	private final static String CON = "->";
	// 项之间的分隔符
	public Apriori(double s,double c,String tn){
		SUPPORT=s;
		CONFIDENCE=c;
		tableName=tn;
	}
	/**
	 * 算法主程序
	 * 
	 * @param dataList
	 * @return
	 */
	public Map<String, Integer> apriori(ArrayList<String> dataList) {
		Map<String, Integer> stepFrequentSetMap = new HashMap<>();
		stepFrequentSetMap.putAll(findFrequentOneSets(dataList));

		Map<String, Integer> frequentSetMap = new HashMap<String, Integer>();// 频繁项集
		frequentSetMap.putAll(stepFrequentSetMap);

		while (stepFrequentSetMap != null && stepFrequentSetMap.size() > 0) {
			Map<String, Integer> candidateSetMap = aprioriGen(stepFrequentSetMap);

			Set<String> candidateKeySet = candidateSetMap.keySet();

			// 扫描D，进行计数
			for (String data : dataList) {
				for (String candidate : candidateKeySet) {
					boolean flag = true;
					String[] strings = candidate.split(ITEM_SPLIT);
					for (String string : strings) {
						if (data.indexOf(string + ITEM_SPLIT) == -1) {
							flag = false;
							break;
						}
					}
					if (flag)
						candidateSetMap.put(candidate, candidateSetMap.get(candidate) + 1);
				}
			}

			// 从候选集中找到符合支持度的频繁项集
			stepFrequentSetMap.clear();
			for (String candidate : candidateKeySet) {
				Integer count = candidateSetMap.get(candidate);
				if (count >= SUPPORT)
					stepFrequentSetMap.put(candidate, count);
			}

			// 合并所有频繁集
			frequentSetMap.putAll(stepFrequentSetMap);
		}

		return frequentSetMap;
	}

	/**
	 * find frequent 1 itemsets
	 * 
	 * @param dataList
	 * @return
	 */
	private Map<String, Integer> findFrequentOneSets(ArrayList<String> dataList) {
		Map<String, Integer> resultSetMap = new HashMap<>();

		for (String data : dataList) {
			String[] strings = data.split(ITEM_SPLIT);
			for (String string : strings) {
				string += ITEM_SPLIT;
				if (resultSetMap.get(string) == null) {
					resultSetMap.put(string, 1);
				} else {
					resultSetMap.put(string, resultSetMap.get(string) + 1);
				}
			}
		}
		return resultSetMap;
	}

	/**
	 * 根据上一步的频繁项集的集合选出候选集
	 * 
	 * @param setMap
	 * @return
	 */
	private Map<String, Integer> aprioriGen(Map<String, Integer> setMap) {
		Map<String, Integer> candidateSetMap = new HashMap<>();

		Set<String> candidateSet = setMap.keySet();
		for (String s1 : candidateSet) {
			String[] strings1 = s1.split(ITEM_SPLIT);
			String s1String = "";
			for (String temp : strings1)
				s1String += temp + ITEM_SPLIT;

			for (String s2 : candidateSet) {
				String[] strings2 = s2.split(ITEM_SPLIT);

				boolean flag = true;
				for (int i = 0; i < strings1.length - 1; i++) {
					if (strings1[i].compareTo(strings2[i]) != 0) {
						flag = false;
						break;
					}
				}
				if (flag && strings1[strings1.length - 1].compareTo(strings2[strings1.length - 1]) < 0) {
					// 连接步：产生候选
					String c = s1String + strings2[strings2.length - 1] + ITEM_SPLIT;
					if (hasInfrequentSubset(c, setMap)) {
						// 剪枝步：删除非频繁的候选
					} else {
						candidateSetMap.put(c, 0);
					}
				}
			}
		}

		return candidateSetMap;
	}

	/**
	 * 使用先验知识，判断候选集是否是频繁项集
	 * 
	 * @param candidate
	 * @param setMap
	 * @return
	 */
	private boolean hasInfrequentSubset(String candidateSet, Map<String, Integer> setMap) {
		String[] strings = candidateSet.split(ITEM_SPLIT);

		// 找出候选集所有的子集，并判断每个子集是否属于频繁子集
		for (int i = 0; i < strings.length; i++) {
			String subString = "";
			for (int j = 0; j < strings.length; j++) {
				if (j != i) {
					subString += strings[j] + ITEM_SPLIT;
				}
			}

			if (setMap.get(subString) == null)
				return true;
		}

		return false;
	}

	/**
	 * 由频繁项集产生关联规则
	 * 
	 * @param frequentSetMap
	 * @return
	 */
	public Map<String, Double> getRelationRules(Map<String, Integer> frequentSetMap,int n,int satelliteId) {
		Map<String, Double> relationsMap = new HashMap<>();
		Set<String> keySet = frequentSetMap.keySet();
		RuleDao rd=new RuleDao();
		for (String key : keySet) {
			List<String> keySubset = subset(key);
			for (String keySubsetItem : keySubset) {
				// 子集keySubsetItem也是频繁项
				keySubsetItem=keySubsetItem.substring(0,keySubsetItem.length()-1);
				Integer count = frequentSetMap.get(keySubsetItem);
				if (count != null) {
					Double confidence = (1.0 * frequentSetMap.get(key)) / (1.0 * frequentSetMap.get(keySubsetItem));
					if (confidence > CONFIDENCE){
						relationsMap.put(keySubsetItem + CON + expect(key, keySubsetItem), confidence);
						Rule m=new Rule(keySubsetItem + CON + expect(key, keySubsetItem),frequentSetMap.get(key)/(double)n,confidence,satelliteId);
						rd.insert(tableName+"_Apriori", m);
					}
				}
			}
		}

		return relationsMap;
	}

	/**
	 * 求一个集合所有的非空真子集
	 * 
	 * @param sourceSet
	 * @return 为了以后可以用在其他地方，这里我们不是用递归的方法
	 * 
	 *         参考：http://blog.163.com/xiaohui_1123@126/blog/static/
	 *         3980524020109784356915/
	 *         思路：假设集合S（A,B,C,D），其大小为4，拥有2的4次方个子集，即0-15，二进制表示为0000，0001，...，
	 *         1111。 对应的子集为空集，{D}，...，{A,B,C,D}。
	 */
	private List<String> subset(String sourceSet) {
		List<String> result = new ArrayList<>();

		String[] strings = sourceSet.split(ITEM_SPLIT);
		// 非空真子集
		for (int i = 1; i < (int) (Math.pow(2, strings.length)) - 1; i++) {
			String item = "";
			String flag = "";
			int ii = i;
			do {
				flag += "" + ii % 2;
				ii = ii / 2;
			} while (ii > 0);
			for (int j = flag.length() - 1; j >= 0; j--) {
				if (flag.charAt(j) == '1') {
					item = strings[j] + ITEM_SPLIT + item;
				}
			}
			result.add(item);
		}

		return result;
	}

	private String expect(String stringA, String stringB) {
		String result = "";

		String[] stringAs = stringA.split(ITEM_SPLIT);
		String[] stringBs = stringB.split(ITEM_SPLIT);

		for (int i = 0; i < stringAs.length; i++) {
			boolean flag = true;
			for (int j = 0; j < stringBs.length; j++) {
				if (stringAs[i].compareTo(stringBs[j]) == 0) {
					flag = false;
					break;
				}
			}
			if (flag)
				result += stringAs[i] + ITEM_SPLIT;
		}

		return result;
	}
	public static ArrayList<String> getData(String tableName,int satelliteId){
    	ArrayList<String> dataList = new ArrayList<>();
		SatellitePreprocessingDao spd=new SatellitePreprocessingDao();
		String sql="select * from "+tableName+" where satelliteId="+satelliteId;
		System.out.println(sql);
		List<Satellite_Preprocessing> list=spd.find(sql);
		List<String> attribute=Tmid.getTmid();
		StringBuffer str=new StringBuffer();
		for(int i=0;i<list.size();i++){
			Satellite_Preprocessing s=list.get(i);
			str.append("1025 "+s.getA1025()+",");
			//str.append("1026 "+s.getA1026()+";");
			//str.append("1027 "+s.getA1027()+";");
			str.append("1028 "+s.getA1028()+",");
			//str.append("1029 "+s.getA1029()+";");
			//str.append("1030 "+s.getA1030()+";");
			//str.append("1031 "+s.getA1031()+";");
			//str.append("1032 "+s.getA1032()+",");
			str.append("1033 "+s.getA1033()+",");
			//str.append("1034 "+s.getA1034()+";");
			//str.append("1035 "+s.getA1035()+";");
			//str.append("1036 "+s.getA1036()+";");
			//str.append("1048 "+s.getA1048()+",");
			//str.append("1049 "+s.getA1049()+",");
			//str.append("1050 "+s.getA1050()+";");
			//str.append("1051 "+s.getA1051()+";");
			//str.append("1052 "+s.getA1052()+",");
			//str.append("1053 "+s.getA1053()+";");
			//str.append("1054 "+s.getA1054()+";");
			//str.append("1055 "+s.getA1055()+",");
			//str.append("1056 "+s.getA1056()+";");
			//str.append("1057 "+s.getA1057()+";");
			//str.append("1058 "+s.getA1058()+";");
			//str.append("1059 "+s.getA1059()+",");
			//str.append("1060 "+s.getA1060()+";");
			//str.append("1061 "+s.getA1061()+";");
			//str.append("1062 "+s.getA1062()+";");
			//str.append("1063 "+s.getA1063()+";");
			//str.append("1075 "+s.getA1075()+";");
			//str.append("1076 "+s.getA1076()+",");
			//str.append("1077 "+s.getA1077()+";");
			//str.append("1078 "+s.getA1078()+";");
			//str.append("4201 "+s.getA4201()+";");
			//str.append("4202 "+s.getA4202()+";");
			//str.append("4203 "+s.getA4203()+";");
			str.append("4204 "+s.getA4204()+",");
			//str.append("4205 "+s.getA4205()+";");
			//str.append("2125 "+s.getA2125()+",");
			str.append("2126 "+s.getA2126()+",");
			//str.append("2127 "+s.getA2127()+",");
			//str.append("2128 "+s.getA2128()+",");
			str.append("2131 "+s.getA2131()+",");
			str.append("2132 "+s.getA2132()+",");
			//str.append("2133 "+s.getA2133()+";");
			//str.append("2136 "+s.getA2136()+";");
			//str.append("2137 "+s.getA2137()+";");
			//str.append("6220 "+s.getA6220()+";");
			str.append("13333 "+s.getA13333()+",");
			//str.append("2140 "+s.getA2140()+";");
			//str.append("2141 "+s.getA2141()+";");
			//str.append("2142 "+s.getA2142()+";");
			//str.append("2143 "+s.getA2143()+";");
			//str.append("14346 "+s.getA14346()+";");
			//str.append("14347 "+s.getA14347()+";");
			str.append("14348 "+s.getA14348()+",");
			//str.append("104349 "+s.getA14349()+";");
			//str.append("14350 "+s.getA14350()+";");
			//str.append("14351 "+s.getA14351()+";");
			str.append("14373 "+s.getA14373()+",");
			//str.append("14374 "+s.getA14374()+";");
			//str.append("14375 "+s.getA14375()+";");
			//str.append("14376 "+s.getA14376()+";");
			//str.append("14377 "+s.getA14377()+",");
			//str.append("14403 "+s.getA14403()+",");
			//str.append("14404 "+s.getA14404()+",");
			//str.append("14405 "+s.getA14405()+",");
			//str.append("14406 "+s.getA14406()+",");
			//str.append("14368 "+s.getA14368()+";");
			//str.append("14371 "+s.getA14371()+";");
			str.append("14417 "+s.getA14417()+",");
			str.append("14418 "+s.getA14418()+",");
			//str.append("14419 "+s.getA14419()+";");
			//str.append("13395 "+s.getA13395()+";");
			//str.append("13396 "+s.getA13396()+";");
			//str.append("13397 "+s.getA13397()+";");
			str.append("14340 "+s.getA14340());
			dataList.add(str.toString());
			str=new StringBuffer();
		}
		return dataList;
    }
    public static ArrayList<String> getData(String tableName,String t1,String t2,int satelliteId){
    	ArrayList<String> dataList = new ArrayList<>();
		SatellitePreprocessingDao spd=new SatellitePreprocessingDao();
		String sql="select * from "+tableName+" where satelliteId="+satelliteId+" and time>=(to_date('"+t1+"','yyyy-MM-dd hh24:mi:ss')) and time<=(to_date('"+t2+"','yyyy-MM-dd hh24:mi:ss'))";
		List<Satellite_Preprocessing> list=spd.find(sql);
		List<String> attribute=Tmid.getTmid();
		StringBuffer str=new StringBuffer();
		for(int i=0;i<list.size();i++){
			Satellite_Preprocessing s=list.get(i);
			str.append("1025 "+s.getA1025()+",");
			//str.append("1026 "+s.getA1026()+";");
			//str.append("1027 "+s.getA1027()+";");
			str.append("1028 "+s.getA1028()+",");
			//str.append("1029 "+s.getA1029()+";");
			//str.append("1030 "+s.getA1030()+";");
			//str.append("1031 "+s.getA1031()+";");
			//str.append("1032 "+s.getA1032()+",");
			str.append("1033 "+s.getA1033()+",");
			//str.append("1034 "+s.getA1034()+";");
			//str.append("1035 "+s.getA1035()+";");
			//str.append("1036 "+s.getA1036()+";");
			//str.append("1048 "+s.getA1048()+",");
			//str.append("1049 "+s.getA1049()+",");
			//str.append("1050 "+s.getA1050()+";");
			//str.append("1051 "+s.getA1051()+";");
			//str.append("1052 "+s.getA1052()+",");
			//str.append("1053 "+s.getA1053()+";");
			//str.append("1054 "+s.getA1054()+";");
			//str.append("1055 "+s.getA1055()+",");
			//str.append("1056 "+s.getA1056()+";");
			//str.append("1057 "+s.getA1057()+";");
			//str.append("1058 "+s.getA1058()+";");
			//str.append("1059 "+s.getA1059()+",");
			//str.append("1060 "+s.getA1060()+";");
			//str.append("1061 "+s.getA1061()+";");
			//str.append("1062 "+s.getA1062()+";");
			//str.append("1063 "+s.getA1063()+";");
			//str.append("1075 "+s.getA1075()+";");
			//str.append("1076 "+s.getA1076()+",");
			//str.append("1077 "+s.getA1077()+";");
			//str.append("1078 "+s.getA1078()+";");
			//str.append("4201 "+s.getA4201()+";");
			//str.append("4202 "+s.getA4202()+";");
			//str.append("4203 "+s.getA4203()+";");
			str.append("4204 "+s.getA4204()+",");
			//str.append("4205 "+s.getA4205()+";");
			//str.append("2125 "+s.getA2125()+",");
			str.append("2126 "+s.getA2126()+",");
			//str.append("2127 "+s.getA2127()+",");
			//str.append("2128 "+s.getA2128()+",");
			str.append("2131 "+s.getA2131()+",");
			str.append("2132 "+s.getA2132()+",");
			//str.append("2133 "+s.getA2133()+";");
			//str.append("2136 "+s.getA2136()+";");
			//str.append("2137 "+s.getA2137()+";");
			//str.append("6220 "+s.getA6220()+";");
			str.append("13333 "+s.getA13333()+",");
			//str.append("2140 "+s.getA2140()+";");
			//str.append("2141 "+s.getA2141()+";");
			//str.append("2142 "+s.getA2142()+";");
			//str.append("2143 "+s.getA2143()+";");
			//str.append("14346 "+s.getA14346()+";");
			//str.append("14347 "+s.getA14347()+";");
			str.append("14348 "+s.getA14348()+",");
			//str.append("104349 "+s.getA14349()+";");
			//str.append("14350 "+s.getA14350()+";");
			//str.append("14351 "+s.getA14351()+";");
			str.append("14373 "+s.getA14373()+",");
			//str.append("14374 "+s.getA14374()+";");
			//str.append("14375 "+s.getA14375()+";");
			//str.append("14376 "+s.getA14376()+";");
			//str.append("14377 "+s.getA14377()+",");
			//str.append("14403 "+s.getA14403()+",");
			//str.append("14404 "+s.getA14404()+",");
			//str.append("14405 "+s.getA14405()+",");
			//str.append("14406 "+s.getA14406()+",");
			//str.append("14368 "+s.getA14368()+";");
			//str.append("14371 "+s.getA14371()+";");
			str.append("14417 "+s.getA14417()+",");
			str.append("14418 "+s.getA14418()+",");
			//str.append("14419 "+s.getA14419()+";");
			//str.append("13395 "+s.getA13395()+";");
			//str.append("13396 "+s.getA13396()+";");
			//str.append("13397 "+s.getA13397()+";");
			str.append("14340 "+s.getA14340());
			System.out.println(str.toString());
			dataList.add(str.toString());
			str=new StringBuffer();
		}
		return dataList;
    }
	public static void getApriori(String tableName,double SUPPORT,double CONFIDENCE,int satelliteId) throws Exception {
		ArrayList<String> dataList = getData(tableName,satelliteId);
		FPTree fptree = new FPTree((int)Math.floor(SUPPORT*dataList.size()));    
//		System.out.println("=数据集合==========");
//		for (String string : dataList) {
//			System.out.println(string);
//		}
		Apriori apriori2 = new Apriori(SUPPORT,CONFIDENCE,tableName);
//		System.out.println("=频繁项集==========");
		List<List<String>> transactions = fptree.loadTransaction(dataList);  
		Map<String, Integer> frequentSetMap=fptree.FPGrowth(transactions, null); 
		//Map<String, Integer> frequentSetMap = apriori2.apriori(dataList);
//		Set<String> keySet = frequentSetMap.keySet();
//		for (String key : keySet) {
//			System.out.println(key + " : " + frequentSetMap.get(key));
//		}
//		System.out.println("=关联规则==========");
		String exist_sql = "select count(*) from user_tables where table_name='"+tableName.toUpperCase()+"_APRIORI'";
		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
			String create_sql = "create table "+tableName+"_Apriori (rule varchar(200),support number,confidence number,satelliteId NUMBER)";
			CreateTable.create(create_sql);
		}
		else{
			String delete_sql="delete from "+tableName+"_Apriori where satelliteId="+satelliteId;
			CreateTable.create(delete_sql);
		}
		Map<String, Double> relationRulesMap = apriori2.getRelationRules(frequentSetMap,dataList.size(),satelliteId);
		String sql="delete from "+tableName+"_Apriori where satelliteId="+satelliteId+" and support<"+SUPPORT;
		System.out.println(sql);
		CreateTable.create(sql);
//		RuleDao rd=new RuleDao();
//		Set<String> rrKeySet = relationRulesMap.keySet();
//		for (String rrKey : rrKeySet) {
//			System.out.println(rrKey + "  :  " + relationRulesMap.get(rrKey));
//		}
	}
	public static void getApriori_detection(String tableName,double SUPPORT,double CONFIDENCE,int satelliteId) throws Exception {
		ArrayList<String> dataList = getData(tableName,satelliteId);
		FPTree fptree = new FPTree((int)Math.floor(SUPPORT*dataList.size()));    
//		System.out.println("=数据集合==========");
//		for (String string : dataList) {
//			System.out.println(string);
//		}
		Apriori apriori2 = new Apriori(SUPPORT,CONFIDENCE,tableName);
//		System.out.println("=频繁项集==========");
		List<List<String>> transactions = fptree.loadTransaction(dataList);  
		Map<String, Integer> frequentSetMap=fptree.FPGrowth(transactions, null); 
		//Map<String, Integer> frequentSetMap = apriori2.apriori(dataList);
//		Set<String> keySet = frequentSetMap.keySet();
//		for (String key : keySet) {
//			System.out.println(key + " : " + frequentSetMap.get(key));
//		}
//		System.out.println("=关联规则==========");
		String exist_sql = "select count(*) from user_tables where table_name='"+tableName.toUpperCase()+"_APRIORI'";
		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
			String create_sql = "create table "+tableName+"_Apriori (rule varchar(200),support number,confidence number,satelliteId NUMBER)";
			CreateTable.create(create_sql);
		}
		else{
			String delete_sql="delete from "+tableName+"_Apriori where satelliteId="+satelliteId;
			CreateTable.create(delete_sql);
		}
		Map<String, Double> relationRulesMap = apriori2.getRelationRules(frequentSetMap,dataList.size(),satelliteId);
		
//		RuleDao rd=new RuleDao();
//		Set<String> rrKeySet = relationRulesMap.keySet();
//		for (String rrKey : rrKeySet) {
//			System.out.println(rrKey + "  :  " + relationRulesMap.get(rrKey));
//		}
	}
	public static Boolean validate(String tableName,String ruleName,String t1,String t2,int satelliteId,String detectionName) throws Exception {
		String exist_sql = "select count(*) from user_tables where table_name='" + detectionName.toUpperCase()  //判断表格是否存在
		+ "'";
		if (!TableExist.exist(exist_sql)) {//表格不存在，则建立表格
			String create_sql = "create table " + detectionName
			+ " (rule varchar(200))";
			CreateTable.create(create_sql);
		}
		else{
			String create_sql = "drop table " + detectionName;
			CreateTable.create(create_sql);
			create_sql = "create table " + detectionName
					+ " (rule varchar(200))";
			CreateTable.create(create_sql);
		}
		RuleDao rd=new RuleDao();
		ArrayList<String> dataList = getData(tableName,satelliteId);
		String sql="select rule,support,confidence from "+ruleName+" where satelliteId="+satelliteId;
		System.out.println(sql);
		List<Rule> ruleList=rd.find(sql);
		System.out.println("ruleList.size()"+ruleList.size());
		int count=0;
		for (Rule key : ruleList) {
			boolean flag = true;
			int count1=0;
			for (String data : dataList) {
				int num=0;
				String rule=key.getRule();
				String[] rules=rule.split(CON);
				for(String str:rules){
					String[] array=str.split(ITEM_SPLIT);
					for (String string : array) {
						if (data.indexOf(string + ITEM_SPLIT) == -1) {
							
						}
						else{
							num++;
						}
					}
				}
				if((double)num/(rules[0].split(ITEM_SPLIT).length+rules[1].split(ITEM_SPLIT).length)>=0.9){
					count1++;
				}
				
			}
			System.out.println((double)count1/dataList.size());
			if((double)count1/dataList.size()>=0.5){
				count++;
				rd.insertByDetection(detectionName, key);
			}
		}
		//System.out.println(count);
		System.out.println((double)count/ruleList.size());
		if((double)count/ruleList.size()>=0.1){
			return true;
		}
		else{
			return false;
		}
	}
//	public static void main(String[] args) throws Exception{
//		Apriori.getApriori("yichang",0.8,0.8,5564);
//		AssociationItem associateItem = new AssociationItem(188);
//		associateItem.setLocation(100,100);
//		associateItem.setSize(1000, 650);
//		//Apriori.validate("Test","yichang_Apriori","2014-07-15 09:00:00","2014-07-16 11:00:00",188,"Test_Apriori");
//	}
}
