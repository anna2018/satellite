package com.entity;

import java.util.Date;

public class DetectionScore {
	private int satelliteId;
	public int getSatelliteId() {
		return satelliteId;
	}
	public void setSatelliteId(int satelliteId) {
		this.satelliteId = satelliteId;
	}
	private Date time;
	private Double pcos;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Double getPcos() {
		return pcos;
	}
	public void setPcos(Double pcos) {
		this.pcos = pcos;
	}
	

}
