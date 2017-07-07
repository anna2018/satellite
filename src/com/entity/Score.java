package com.entity;

import java.util.Date;

public class Score {
	private int satelliteId;
	private Date time;
	private double score;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int getSatelliteId() {
		return satelliteId;
	}
	public void setSatelliteId(int satelliteId) {
		this.satelliteId = satelliteId;
	}
	
}
