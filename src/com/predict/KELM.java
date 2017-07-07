package com.predict;

import com.util.Inverse;

import Jama.Matrix;

public class KELM {
	public static Matrix Train_Y,Validate_Y,Test_Y;
	public static double getError(double[][] matrix_X,double[][] matrix_Y){
		int C=20;//惩罚系数
		String kernel_type="rbf";//核函数类型
		double kernel_pars=2;//核半径
		Matrix P= new Matrix(matrix_X);
		Matrix T= new Matrix(matrix_Y);
		int n = T.getRowDimension();
		//Omega_train = kernel_matrix(P',Kernel_type, Kernel_para);
		//OutputWeight=((Omega_train+speye(n)/C)\(T')); 
		//Y=(Omega_train * OutputWeight)'; 
		Matrix Omega_train=kernel_matrix(P,kernel_type,kernel_pars);
		Matrix speye = new Matrix(n,n);//生成一个单位矩阵
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(i==j){
					speye.set(i, j, (double)(1/C));//speye(n)/C
				}
			}
		}
		Matrix  O_s = Omega_train.plus(speye);//Omega_train+speye(n)/C
		//Matrix O_s_trans = O_s.inverse();//(Omega_train+speye(n)/C)的逆
		Matrix O_s_trans =Inverse.getInverse(O_s);
		Matrix OutputWeight = O_s_trans.times(T);//((Omega_train+speye(n)/C)\(T'))
		Matrix mul = Omega_train.times(OutputWeight);//Omega_train * OutputWeight
		Matrix Y = mul.transpose();//(Omega_train * OutputWeight)'
//		MAPEtterror= zeros(trainsize,1);
//		for i=1:trainsize
//			    if(T(i)~=0)
//			    tterror(i)=abs((T(i)-Y(i))/T(i));
//			    end 
//			end 
//			tterrorall=sum(tterror);
//
//			tmape=tterrorall/trainsize*100; 
		int trainsize  = T.getRowDimension();
		Matrix terror=new Matrix(trainsize,1);
		double sum_error=0;
		for(int i=0;i<trainsize;i++){
			if(T.get(i, 0)!=0){
				double temp = Math.abs((T.get(i, 0)-Y.get(0, i))/T.get(i, 0));
				terror.set(i, 0, temp);
				sum_error+=temp;
			}
		}
		double mape=sum_error/trainsize*100;//tmape=tterrorall/trainsize*100
		return mape;
	}
	public static Matrix kernel_matrix(Matrix Xtrain, String kernel_type, double kernel_pars){
		int nb_data =Xtrain.getRowDimension();//矩阵的行数
		Matrix omega=new Matrix(nb_data,nb_data);//构造输出矩阵
		if(kernel_type.toLowerCase().equals("rbf")||kernel_type.toLowerCase().equals("rbf_kernel")){
//			XXh = sum(Xtrain.^2,2)*ones(1,nb_data);
//	        omega = XXh+XXh'-2*(Xtrain*Xtrain');
//	        omega = exp(-omega./kernel_pars(1));
			int Xtrain_row= Xtrain.getRowDimension();//行数
			int Xtrain_col= Xtrain.getColumnDimension();//列数
			Matrix X_row=new Matrix(Xtrain_row,1);//构造空的列矩阵
			double sqrt_sum=0;
			for(int i=0;i<Xtrain_row;i++){
				for(int j=0;j<Xtrain_col;j++){
					sqrt_sum+=Xtrain.get(i, j)*Xtrain.get(i, j);//每个位置上的数平方后，按行求和
				}
				X_row.set(i,0,sqrt_sum);
				sqrt_sum=0;
			}
			Matrix ones = new Matrix(1,nb_data,1);//构造全1矩阵
			Matrix XXh=X_row.times(ones);//矩阵相乘
			Matrix XXh_trans = XXh.transpose();//XXh'
			Matrix Xtrain_trans = Xtrain.transpose();//Xtrain'
			Matrix mul = Xtrain.times(Xtrain_trans);//Xtrain*Xtrain'
			Matrix sum = XXh.plus(XXh_trans);//XXh+XXh'
			omega=sum.minus(mul.times(2));//XXh+XXh'-2*(Xtrain*Xtrain')
			Matrix zero = new Matrix(omega.getRowDimension(),omega.getColumnDimension());
			omega = zero.minus(omega);//-omega
			omega =omega.times(1/kernel_pars);//-omega./kernel_pars(1)
			for(int i=0;i<omega.getRowDimension();i++){
				for(int j=0;j<omega.getColumnDimension();j++){
					double temp = Math.exp(omega.get(i, j));
					omega.set(i, j, temp);
				}
			}
		}
		return omega;
	}
	public static Matrix kernel_matrix(Matrix Xtrain, String kernel_type, double kernel_pars,Matrix Xt){//用于测试
		int nb_data =Xtrain.getRowDimension();//矩阵的行数
		Matrix omega=new Matrix(nb_data,nb_data);//构造输出矩阵
		if(kernel_type.toLowerCase().equals("rbf")||kernel_type.toLowerCase().equals("rbf_kernel")){
//			XXh = sum(Xtrain.^2,2)*ones(1,nb_data);
//	        omega = XXh+XXh'-2*(Xtrain*Xtrain');
//	        omega = exp(-omega./kernel_pars(1));
			int Xtrain_row= Xtrain.getRowDimension();//行数
			int Xtrain_col= Xtrain.getColumnDimension();//列数
			Matrix X_row=new Matrix(Xtrain_row,1);//构造空的列矩阵
			double sqrt_sum=0;
			for(int i=0;i<Xtrain_row;i++){
				for(int j=0;j<Xtrain_col;j++){
					sqrt_sum+=Xtrain.get(i, j)*Xtrain.get(i, j);//每个位置上的数平方后，按行求和
				}
				X_row.set(i,0,sqrt_sum);
				sqrt_sum=0;
			}
			Matrix ones = new Matrix(1,Xt.getRowDimension(),1);//构造全1矩阵
			Matrix XXh1=X_row.times(ones);//矩阵相乘
			Matrix X_row2=new Matrix(Xt.getRowDimension(),1);//构造空的列矩阵
			double sqrt_sum2=0;
			for(int i=0;i<Xt.getRowDimension();i++){
				for(int j=0;j<Xt.getColumnDimension();j++){
					sqrt_sum2+=Xt.get(i, j)*Xt.get(i, j);//每个位置上的数平方后，按行求和
				}
				X_row2.set(i,0,sqrt_sum2);
				sqrt_sum2=0;
			}
			Matrix ones2 = new Matrix(1,nb_data,1);//构造全1矩阵
			Matrix XXh2=X_row2.times(ones2);//矩阵相乘		
			Matrix XXh2_trans = XXh2.transpose();//XXh2'
			Matrix Xt_trans = Xt.transpose();//Xt'
			Matrix mul = Xtrain.times(Xt_trans);//Xtrain*Xt'
			Matrix sum = XXh1.plus(XXh2_trans);//XXh1+XXh2'
			omega=sum.minus(mul.times(2));//XXh1+XXh2'-2*(Xtrain*Xt')
			Matrix zero = new Matrix(omega.getRowDimension(),omega.getColumnDimension());
			omega = zero.minus(omega);//-omega
			omega =omega.times(1/kernel_pars);//-omega./kernel_pars(1)
			for(int i=0;i<omega.getRowDimension();i++){
				for(int j=0;j<omega.getColumnDimension();j++){
					double temp = Math.exp(omega.get(i, j));
					omega.set(i, j, temp);
				}
			}
		}
		return omega;
	}
	public static double getError(double kernel_pars,double C,double[][] matrix_X,double[][] matrix_Y){
		String kernel_type="rbf";//核函数类型
//		Matrix P= new Matrix(matrix_X);
//		Matrix T= new Matrix(matrix_Y);
//		int n = T.getRowDimension();
//		//Omega_train = kernel_matrix(P',Kernel_type, Kernel_para);
//		//OutputWeight=((Omega_train+speye(n)/C)\(T')); 
//		//Y=(Omega_train * OutputWeight)'; 
//		Matrix Omega_train=kernel_matrix(P,kernel_type,kernel_pars);
//		Matrix speye = new Matrix(n,n);//生成一个单位矩阵
//		for(int i=0;i<n;i++){
//			for(int j=0;j<n;j++){
//				if(i==j){
//					speye.set(i, j, (double)(1/C));//speye(n)/C
//				}
//			}
//		}
//		Matrix  O_s = Omega_train.plus(speye);//Omega_train+speye(n)/C
//		Matrix O_s_trans = O_s.inverse();//(Omega_train+speye(n)/C)的逆
//		Matrix OutputWeight = O_s_trans.times(T);//((Omega_train+speye(n)/C)\(T'))
//		Matrix mul = Omega_train.times(OutputWeight);//Omega_train * OutputWeight
//		Matrix Y = mul.transpose();//(Omega_train * OutputWeight)'
////		MAPEtterror= zeros(trainsize,1);
////		for i=1:trainsize
////			    if(T(i)~=0)
////			    tterror(i)=abs((T(i)-Y(i))/T(i));
////			    end 
////			end 
////			tterrorall=sum(tterror);
////
////			tmape=tterrorall/trainsize*100; 
//		int trainsize  = T.getRowDimension();
//		Matrix terror=new Matrix(trainsize,1);
//		double sum_error=0;
//		for(int i=0;i<trainsize;i++){
//			if(T.get(i, 0)!=0){
//				double temp = Math.abs((T.get(i, 0)-Y.get(0, i))/T.get(i, 0));
//				terror.set(i, 0, temp);
//				sum_error+=temp;
//			}
//		}
//		double mape=sum_error/trainsize*100;//tmape=tterrorall/trainsize*100
		Matrix P= new Matrix(matrix_X);
		Matrix T= new Matrix(matrix_Y);
		Matrix Train_P=P.getMatrix(0, (int)(Math.round(P.getRowDimension()*0.6)-1), 0, P.getColumnDimension()-1);
		Matrix Validate_P=P.getMatrix((int)Math.round(P.getRowDimension()*0.6),P.getRowDimension()-1,0,P.getColumnDimension()-1);
		Matrix Train_T=T.getMatrix(0, (int)(Math.round(T.getRowDimension()*0.6)-1), 0, 0);
		Matrix Validate_T=T.getMatrix((int)Math.round(T.getRowDimension()*0.6),T.getRowDimension()-1,0,0);
		int n = Train_T.getRowDimension();
		//Omega_train = kernel_matrix(P',Kernel_type, Kernel_para);
		//OutputWeight=((Omega_train+speye(n)/C)\(T')); 
		//Y=(Omega_train * OutputWeight)'; 
		Matrix Omega_train=kernel_matrix(Train_P,kernel_type,kernel_pars);
		Matrix speye = new Matrix(n,n);//生成一个单位矩阵
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(i==j){
					speye.set(i, j, (double)(1/C));//speye(n)/C
				}
			}
		}
		Matrix  O_s = Omega_train.plus(speye);//Omega_train+speye(n)/C
		//Matrix O_s_trans = O_s.inverse();//(Omega_train+speye(n)/C)的逆
		Matrix O_s_trans=Inverse.getInverse(O_s);
		Matrix OutputWeight = O_s_trans.times(Train_T);//((Omega_train+speye(n)/C)\(T'))
		Matrix mul_train = Omega_train.times(OutputWeight);//Omega_train * OutputWeight
		Train_Y = mul_train.transpose();//(Omega_train * OutputWeight)'
		//Omega_validate = kernel_matrix(P',Kernel_type, Kernel_para,V.P');
		//VY=(Omega_validate' * OutputWeight)';
		Matrix Omega_validate=kernel_matrix(Train_P,kernel_type,kernel_pars,Validate_P);
		Matrix mul_validate = Omega_validate.transpose().times(OutputWeight);//Omega_validate * OutputWeight
		Validate_Y = mul_validate.transpose();//(Omega_validate * OutputWeight)'
//		MAPEtterror= zeros(trainsize,1);
//		for i=1:trainsize
//			    if(T(i)~=0)
//			    tterror(i)=abs((T(i)-Y(i))/T(i));
//			    end 
//			end 
//			tterrorall=sum(tterror);
//
//			tmape=tterrorall/trainsize*100; 
		int trainsize  = Validate_T.getRowDimension();
		Matrix terror=new Matrix(trainsize,1);
		double sum_error=0;
		for(int i=0;i<trainsize;i++){
			if(Validate_T.get(i, 0)!=0){
				double temp = Math.abs((Validate_T.get(i, 0)-Validate_Y.get(0, i))/Validate_T.get(i, 0));
				terror.set(i, 0, temp);
				sum_error+=temp;
			}
		}
		double mape=sum_error/trainsize*100;//tmape=tterrorall/trainsize*100
		return mape;
	}
	public static void ELM_Kernel(double kernel_pars,double C,Matrix P,Matrix T,Matrix Test){
		String kernel_type="rbf";
		Matrix Train_P=P.getMatrix(0, (int)(Math.round(P.getRowDimension()*0.6)-1), 0, P.getColumnDimension()-1);
		Matrix Validate_P=P.getMatrix((int)Math.round(P.getRowDimension()*0.6),P.getRowDimension()-1,0,P.getColumnDimension()-1);
		Matrix Train_T=T.getMatrix(0, (int)(Math.round(T.getRowDimension()*0.6)-1), 0, 0);
		Matrix Validate_T=T.getMatrix((int)Math.round(T.getRowDimension()*0.6),T.getRowDimension()-1,0,0);
		int n = Train_T.getRowDimension();
		//Omega_train = kernel_matrix(P',Kernel_type, Kernel_para);
		//OutputWeight=((Omega_train+speye(n)/C)\(T')); 
		//Y=(Omega_train * OutputWeight)'; 
		Matrix Omega_train=kernel_matrix(Train_P,kernel_type,kernel_pars);
		Matrix speye = new Matrix(n,n);//生成一个单位矩阵
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(i==j){
					speye.set(i, j, (double)(1/C));//speye(n)/C
				}
			}
		}
		Matrix  O_s = Omega_train.plus(speye);//Omega_train+speye(n)/C
		//Matrix O_s_trans = O_s.inverse();//(Omega_train+speye(n)/C)的逆
		Matrix O_s_trans =Inverse.getInverse(O_s);
		Matrix OutputWeight = O_s_trans.times(Train_T);//((Omega_train+speye(n)/C)\(T'))
		Matrix mul_train = Omega_train.times(OutputWeight);//Omega_train * OutputWeight
		Train_Y = mul_train.transpose();//(Omega_train * OutputWeight)'
		//Omega_validate = kernel_matrix(P',Kernel_type, Kernel_para,V.P');
		//VY=(Omega_validate' * OutputWeight)';
		Matrix Omega_validate=kernel_matrix(Train_P,kernel_type,kernel_pars,Validate_P);
		Matrix mul_validate = Omega_validate.transpose().times(OutputWeight);//Omega_validate * OutputWeight
		Validate_Y = mul_validate.transpose();//(Omega_validate * OutputWeight)'
		Matrix Omega_test=kernel_matrix(Train_P,kernel_type,kernel_pars,Test.transpose());
		Matrix mul_test = Omega_test.transpose().times(OutputWeight);//Omega_test * OutputWeight
		Test_Y = mul_test.transpose();//(Omega_test * OutputWeight)'
		//System.out.println("Train_P");
//		for(int i=0;i<Train_P.getRowDimension();i++){
//			for(int j=0;j<Train_P.getColumnDimension();j++){
//				System.out.print(Train_P.get(i,j)+"\t");
//			}
//			System.out.println();
//		}
//		System.out.println("Validate_P");
//		for(int i=0;i<Validate_P.getRowDimension();i++){
//			for(int j=0;j<Validate_P.getColumnDimension();j++){
//				System.out.print(Validate_P.get(i,j)+"\t");
//			}
//			System.out.println();
//		}
//		System.out.println("Train_T");
//		for(int i=0;i<Train_T.getRowDimension();i++){
//			for(int j=0;j<Train_T.getColumnDimension();j++){
//				System.out.print(Train_T.get(i,j)+"\t");
//			}
//			System.out.println();
//		}
//		System.out.println("Validate_T");
//		for(int i=0;i<Validate_T.getRowDimension();i++){
//			for(int j=0;j<Validate_T.getColumnDimension();j++){
//				System.out.print(Validate_T.get(i,j)+"\t");
//			}
//			System.out.println();
//		}
//		System.out.println("Train_Y");
//		for(int i=0;i<KELM.Train_Y.getRowDimension();i++){
//			for(int j=0;j<KELM.Train_Y.getColumnDimension();j++){
//				System.out.print(KELM.Train_Y.get(i,j)+"\t");
//			}
//			System.out.println();
//		}
//		System.out.println("Validate_Y");
//		for(int i=0;i<KELM.Validate_Y.getRowDimension();i++){
//			for(int j=0;j<KELM.Validate_Y.getColumnDimension();j++){
//				System.out.print(KELM.Validate_Y.get(i,j)+"\t");
//			}
//			System.out.println();
//		}
//		System.out.println("Test_Y");
//		for(int i=0;i<KELM.Test_Y.getRowDimension();i++){
//			for(int j=0;j<KELM.Test_Y.getColumnDimension();j++){
//				System.out.print(KELM.Test_Y.get(i,j)+"\t");
//			}
//			System.out.println();
//		}
	}
	
}
