package com.predict;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
public class CFunction {
 // public double params[];//参数个数
  public double[] parmin;//参数下线
  public double[] parmax;//参数上线
  public boolean flag;//求解最大值还是最小值
  public int type;//优化函数类型
  public Individual oldpoplation[]=new Individual[Define.POPSIZE];
  public Individual newpoplation[]=new Individual[Define.POPSIZE];
  public static Individual bestindv;
  public CFunction(double[] parmin,double[] parmax,boolean flag,int type)
  {
	  this.parmin=parmin;
	  this.parmax=parmax;
	  this.flag=flag;
	  this.type=type;
  }
  
  public double Compute(double pars[])
  {
	  if(this.type==0){
		  return Function.Func(pars);
	  }
	  else {
		  return Function.PISI(pars);
	  }
  }
  
  public  void InitPoplation()
	{
		for(int i=0;i<Define.POPSIZE;i++)
		{
			oldpoplation[i]=new Individual();
			newpoplation[i]=new Individual();
		}
	}
  
  public void Evalute()
	{
	   	for(int i=0;i<Define.POPSIZE;i++)
	   	{
	   		for(int j=0;j<Define.PARSNUM;j++)
	   		{
	   			oldpoplation[i].params[j]=Math.random()*(parmax[j]-parmin[j])+parmin[j];
	   		}
	   		oldpoplation[i].fitness=Compute(oldpoplation[i].params);
	   		
	   	}
	   	bestindv=new Individual();
	   	bestindv.CopyObject(oldpoplation[0]);
	   	for(int i=0;i<Define.POPSIZE;i++)
	   	{
	   		if(flag==true&&bestindv.fitness<oldpoplation[i].fitness)
	   			bestindv.CopyObject(oldpoplation[i]);
	   		else if(flag==false&&bestindv.fitness>oldpoplation[i].fitness)
	   			bestindv.CopyObject(oldpoplation[i]);
	   	}
	   	try{
	   	for(int t=0;t<Define.GENERATION;t++)
	   	{
	   		//bestindv.CopyObject(oldpoplation[0]);
	   		for(int i=1;i<Define.POPSIZE;i++)
	   		{
	   			if(flag==true&&bestindv.fitness<oldpoplation[i].fitness)
	   			bestindv.CopyObject(oldpoplation[i]);
	   			else if(flag==false&&bestindv.fitness>oldpoplation[i].fitness)
	   			bestindv.CopyObject(oldpoplation[i]);
	   		}
	   		//System.out.println("bestdiv="+bestindv.fitness);
	   		//System.out.println("第%代");
	   		for(int i=0;i<Define.POPSIZE;i++)
	   		{
	   			int x1,x2,x3;
	   			    x1=((int)(Math.random()*Define.POPSIZE))%Define.POPSIZE;
	   			do{
	   				x2=((int)(Math.random()*Define.POPSIZE))%Define.POPSIZE;
	   			}while(x1==x2);
	   			do{
	   				x3=((int)(Math.random()*Define.POPSIZE))%Define.POPSIZE;
	   			}while(x1==x3||x2==x3);
	   			//System.out.println("第%%%%代");
	   			//System.out.println("x1="+x1+"--x2="+x2+"--x3="+x3);
	   			for(int j=0;j<Define.PARSNUM;j++)
	   			{
	   				newpoplation[i].params[j]=oldpoplation[x1].params[j]+Define.ZOOM*(oldpoplation[x2].params[j]-oldpoplation[x3].params[j]);
	   				if(newpoplation[i].params[j]<parmin[j]||newpoplation[i].params[j]>parmax[j])
	   				{
	   					newpoplation[i].params[j]=oldpoplation[i].params[j];
	   				}
	   			}
	   		}
	   		
	   		for(int i=0;i<Define.POPSIZE;i++)
	   		{
	   			for(int j=0;j<Define.PARSNUM;j++)
	   			{
	   				if(Math.random()>Define.CROSSOVER)
	   					newpoplation[i].params[j]=oldpoplation[i].params[j];
	   			}
	   			
	   			newpoplation[i].fitness=Compute(newpoplation[i].params);
	   		}
	   		
	   		for(int i=0;i<Define.POPSIZE;i++)
	   		{
	   			if(flag==true&&newpoplation[i].fitness<oldpoplation[i].fitness)
	   				newpoplation[i].CopyObject(oldpoplation[i]);
	   			else if(flag==false&&newpoplation[i].fitness>oldpoplation[i].fitness)
	   				newpoplation[i].CopyObject(oldpoplation[i]);
	   		}
	   		
	   		
	   		for(int i=0;i<Define.POPSIZE;i++)
	   		{
	   			oldpoplation[i].CopyObject(newpoplation[i]);
	   		}
	   		
	   		//System.out.println("第"+t+"代");
	   	
	   
	   	}
	   	}catch(Exception e)
	   	{
	   		e.printStackTrace();
	   	}
	  
	   	
	}
  
  
	public void Report()
   	{
   		//System.out.println("输出最好的解：");
   		for(int i=0;i<Define.PARSNUM;i++)
   		{
   		  //System.out.println("var("+i+")="+bestindv.params[i]);
   		}
   		//System.out.println("best error="+Compute(bestindv.params));
   		File file = new File("E:/dic_general.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file,true);
            if(this.type==0){
            	writer.write("核半径和惩罚系数：\n");
            }
            else{
            	writer.write("比例系数：\n");
            }
            writer.write("输出最好的解：\n");
            for(int i=0;i<Define.PARSNUM;i++)
       		{
            	writer.write("var("+i+")="+bestindv.params[i]+"\n");
       		}
            writer.write("best error="+Compute(bestindv.params)+"\n");
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
   	}
}
