package com.de;

import com.predict.DataPreprocessing;
import com.predict.KELM;

import Jama.Matrix;

public class DE {
	public static void DE_kernel() {
		int maxIteration = 1000;// 最大迭代次数
		int Generation = 0;// 进化代数，或者当前迭代代数
		double Xmax = 100;// 搜索上界，可以根据需要改为向量形式
		double Xmin = 0.01;// 搜索下界
		int Dim = 2;// 个体维数
		int NP = 50;// population size,种群规模
		double F = 0.5;// scaling factor 缩放因子
		double CR = 0.3;// crossover rate 交叉概率
		int mutationStrategy = 1;// 变异策略
		int crossStrategy = 1;// 交叉策略
		// step1 初始化
		// X represent population
		// Generation=0;
		// X=(Xmax-Xmin)*rand(NP,Dim)+Xmin;
		//第1 步：初始化种群
		Matrix X = new Matrix(NP, Dim);
		for (int i = 0; i < X.getRowDimension(); i++) {//初始化种群
			for (int j = 0; j < X.getColumnDimension(); j++) {
				X.set(i, j, (Xmax-Xmin)*Math.random()+Xmin);
			}
		}
		Matrix fitnessX = new Matrix(NP,1);
		//第2步：mutation,crossover,selection
		while(Generation<maxIteration){
			//求bestX
			double error= KELM.getError(X.get(0,0), X.get(0, 1), DataPreprocessing.matrix_X, DataPreprocessing.matrix_Y);
			fitnessX.set(0, 0, error);
			double min_fitnessX=fitnessX.get(0, 0);
			int indexbestfitnessX=0;
			for(int i=1;i<NP;i++){
				error= KELM.getError(X.get(i,0), X.get(i, 1), DataPreprocessing.matrix_X, DataPreprocessing.matrix_Y);
				fitnessX.set(i, 0, error);
				if(fitnessX.get(i,0)<min_fitnessX){
					min_fitnessX=fitnessX.get(i,0);
					indexbestfitnessX=i;
				}
			}
			Matrix bestX=X.getMatrix(indexbestfitnessX, indexbestfitnessX+1, 0,Dim-1);
			//mutation
			Matrix V=mutation(X,bestX,F,mutationStrategy);
		}
		
	}
	public static Matrix mutation(Matrix X,Matrix bestX,double F,int mutationStrategy){
		int NP=X.getRowDimension();
		for(int i=0;i<NP;i++){
			int nrandl=5;
			Matrix r=new Matrix(1,nrandl);
			for(int j=0;j<r.getRowDimension();j++){
				for(int k=0;k<r.getColumnDimension();k++){
					
				}
			}
		}
		
		return bestX;
		
	}
}
