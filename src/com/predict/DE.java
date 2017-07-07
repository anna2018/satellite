package com.predict;


//%mutation
//%mutationStrategy=1：DE/rand/1,
//%mutationStrategy=2：DE/best/1,
//%mutationStrategy=3：DE/rand-to-best/1,
//%mutationStrategy=4：DE/best/2,
//%mutationStrategy=5：DE/rand/2.
//%crossover
//%crossStrategy=1:binomial crossover
//%crossStrategy=2:Exponential crossover
public class DE{
	public static double alf;
	public static double bta;
	public static double[]  DE_Kernel(){
//		int maxIteration=1000;//最大迭代次数
//		int Generation=0;//进化代数，或者当前迭代代数
//		double Xmax=100;//搜索上界，可以根据需要改为向量形式
//		double Xmin=0.01;//搜索下界
//		int Dim=2;//个体维数
//		int NP=50;//population size,种群规模
//		double F=0.5;//scaling factor 缩放因子
//		double CR=0.3;//crossover rate 交叉概率
//		int mutationStrategy=1;//变异策略
//		int crossStrategy=1;//交叉策略
//		//step1 初始化
//		//X represent population
//		//Generation=0;
//		//X=(Xmax-Xmin)*rand(NP,Dim)+Xmin;
//		Matrix X=new Matrix(NP,Dim);
//		for (int i=0;i<X.getRowDimension();i++){
//			for(int j=0;j<X.getColumnDimension();j++){
//				
//			}
//		}
		double[] min={0,0};
		double[] max={10,100};
	    CFunction cfunc=new CFunction(min,max,false,0);
	    cfunc.InitPoplation();
	    cfunc.Evalute();
	    cfunc.Report();
		return CFunction.bestindv.params;
	}
	public static double[] DE_PISI(){
		double[] min={0.01,0.01};
		double[] max={0.5,0.5};
	    CFunction cfunc=new CFunction(min,max,false,1);
	    cfunc.InitPoplation();
	    cfunc.Evalute();
	    cfunc.Report();
		return CFunction.bestindv.params;
	}
}
