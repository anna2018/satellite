package com.util;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class Inverse {
	public static Matrix getInverse(Matrix m){
		SingularValueDecomposition svd =m.svd();  
        Matrix S = svd.getS();  
        Matrix V = svd.getV().transpose();  
        Matrix U = svd.getU();  
		//��S�з�0Ԫ��ȡ����  
        Matrix sinv = UnaryNotZeroElement(S);  
        Matrix inv = V.times(sinv).times(U.transpose()); 
		return inv;
	}
	//�������з�0Ԫ��ȡ����  
    private static Matrix UnaryNotZeroElement(Matrix x) {  
        double[][] array=x.getArray();  
        for(int i=0;i<array.length;i++){  
            for(int j=0;j<array[i].length;j++){  
                if(array[i][j]!=0){  
                    array[i][j]=1.0/array[i][j];  
                }  
            }  
        }  
        return new Matrix(array);  
    }  
}
