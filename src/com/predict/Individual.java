package com.predict;

public class Individual {

	public double params[];
	public double fitness;
	//public double minvalue;
	//public double maxvalue;
	public int paramlength;
	public Individual()
	{
		this.params=new double[Define.PARSNUM];
		for(int i=0;i<Define.PARSNUM;i++)
		{
			this.params[i]=0.0;
		}
		this.fitness=0.0;
		this.paramlength=Define.PARSNUM;
	}
	
	public void CopyObject(Individual ind)
	{
		fitness=ind.fitness;
		for(int i=0;i<Define.PARSNUM;i++)
		{
			params[i]=ind.params[i];
		}
	}
}
