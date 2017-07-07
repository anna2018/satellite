package com.predict;

import Jama.Matrix;

public class Function {

	
	public static double Func(double pars[])
	{
		return KELM.getError(pars[0],pars[1],DataPreprocessing.matrix_X,DataPreprocessing.matrix_Y);
	}
	public static double PISI(double pars[])
	{
		double lmd=2;
		double eta=50;
		double mu=0.9;
		Matrix T=new Matrix(DataPreprocessing.matrix_Y);
		Matrix Validate_T=T.getMatrix((int)Math.round(T.getRowDimension()*0.6),T.getRowDimension()-1,0,0);
		Matrix Validate_Y=KELM.Validate_Y;
		int size=Validate_Y.getColumnDimension();
		Matrix U=Validate_Y.times(1+pars[0]);
		Matrix L=Validate_Y.times(1-pars[1]);
		Matrix K=new Matrix(size,1);
		Matrix SMWP=new Matrix(size,1);
		Matrix Apcl=new Matrix(size,1);
		double sum_K=0;
		double sum_Apcl=0;
		double sum_SMWP=0;
		double sum_error=0;
		for(int i=0;i<size;i++){
			if(Validate_T.get(i, 0)>=L.get(0, i)&&Validate_T.get(i, 0)<=U.get(0, i)){
				K.set(i, 0, 1);
				sum_K+=1;
			}
			if(Validate_T.get(i, 0)>U.get(0, i)){
				//K.set(i, 0, 0);
				double temp=(Validate_T.get(i, 0)-U.get(0, i))/(U.get(0, i)-L.get(0, i));
				Apcl.set(i, 0, temp);
				sum_Apcl+=temp;
			}
			if(Validate_T.get(i, 0)<L.get(0, i)){
				//K.set(i, 0, 0);
				double temp=(L.get(0,i)-Validate_T.get(i, 0))/(U.get(0, i)-L.get(0, i));
				Apcl.set(i, 0, temp);
				sum_Apcl+=temp;
			}
			if(Validate_T.get(i, 0)!=0){
				double temp=(U.get(0, i)-L.get(0, i))/Validate_T.get(i, 0);
				SMWP.set(i, 0, temp);
				sum_SMWP+=temp;
			}
		}
		//System.out.println("sum_K="+sum_K);
		double KP=sum_K/size;
		double MWP=sum_SMWP/size;
		double AD=sum_Apcl/size;
		double PISI=(1-(1+lmd*AD)*MWP*(1+Math.exp(-eta*(KP-mu))));
		//System.out.println("KP="+KP);
		//System.out.println("MWP="+MWP);
		//System.out.println("AD="+AD);
		//System.out.println("PISI="+PISI);
		if(PISI<0)
	    	PISI=0;
		double result=1-PISI;
		//System.out.println("result="+result);
		//System.out.println("alf="+pars[0]+",bta="+pars[1]);
		return result;
	}
	
}
