package com.predict;


//%mutation
//%mutationStrategy=1��DE/rand/1,
//%mutationStrategy=2��DE/best/1,
//%mutationStrategy=3��DE/rand-to-best/1,
//%mutationStrategy=4��DE/best/2,
//%mutationStrategy=5��DE/rand/2.
//%crossover
//%crossStrategy=1:binomial crossover
//%crossStrategy=2:Exponential crossover
public class DE{
	public static double alf;
	public static double bta;
	public static double[]  DE_Kernel(){
//		int maxIteration=1000;//����������
//		int Generation=0;//�������������ߵ�ǰ��������
//		double Xmax=100;//�����Ͻ磬���Ը�����Ҫ��Ϊ������ʽ
//		double Xmin=0.01;//�����½�
//		int Dim=2;//����ά��
//		int NP=50;//population size,��Ⱥ��ģ
//		double F=0.5;//scaling factor ��������
//		double CR=0.3;//crossover rate �������
//		int mutationStrategy=1;//�������
//		int crossStrategy=1;//�������
//		//step1 ��ʼ��
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
