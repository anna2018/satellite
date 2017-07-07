package com.entity;

public class Rule {
	private String rule;
	private double support;
	private double confidence;
	private int satelliteId;
	public Rule(){
		
	}
	public Rule(String r,double s,double d,int sa){
		rule=r;
		support=s;
		confidence=d;
		satelliteId=sa;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rules) {
		this.rule = rules;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public double getSupport() {
		return support;
	}
	public void setSupport(double support) {
		this.support = support;
	}
	public int getSatelliteId() {
		return satelliteId;
	}
	public void setSatelliteId(int satelliteId) {
		this.satelliteId = satelliteId;
	}
	
}
