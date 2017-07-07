package com.entity;

import java.util.Date;

public class KNN_Neighbor {
	private int satelliteId;
	private Date time;
	private Date neighbor_time;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Date getNeighbor_time() {
		return neighbor_time;
	}
	public void setNeighbor_time(Date neighbor_time) {
		this.neighbor_time = neighbor_time;
	}
	public int getSatelliteId() {
		return satelliteId;
	}
	public void setSatelliteId(int satelliteId) {
		this.satelliteId = satelliteId;
	}
	

}
