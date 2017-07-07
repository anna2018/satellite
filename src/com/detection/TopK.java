package com.detection;

public class TopK {  
    private int[] heap;  
    private int currentSize;  
  
    TopK(int[] a) {  
        if (a != null) {  
            heap = new int[a.length];  
            currentSize = 0;  
            for (int i = 0; i < a.length; i++)  
                heap[currentSize++] = a[i];  
        }  
        heapAdjust();  
    }  
  
    //�����Ե����ϵ������ֲ��Զ����µ�����  
    public void heapAdjust() {  
        int currentPos = (currentSize - 2) / 2;  
        while (currentPos >= 0) {  
            siftDown(currentPos, currentSize - 1);  
            currentPos--;  
        }  
    }  
  
    // ��С�Ѿֲ������㷨  
    // �ٶ�ǰ������Ů���ǳ���С��.  
    private void siftDown(int start, int m) {  
        int i = start;  
        int j = 2 * i + 1;// ��ʼ�ڵ������Ů��  
        int temp = heap[i];  
        while (j <= m) {  
            if (j < m && heap[j] > heap[j + 1])  
                j++;// jָ������Ů�е�С��  
            if (temp <= heap[j])  
                break;// ���ڵ�С����������  
            else {  
                heap[i] = heap[j];// �ؼ���С����Ů����ϸ�.  
                i = j;  
                j = 2 * i + 1;// j����  
            }  
        }  
        heap[i] = temp;  
    }  
  
    /* 
     * a����������� ����ʵ�ֻ�ȡ��С��k��Ԫ��. 
     */  
    public void getTopK(int[] a, int k) {  
        for (int j = currentSize - 1; j >= 0; j--, k--) {  
            if (k > 0) {  
                System.out.println(heap[0]);  
                int temp = heap[0];  
                heap[0] = heap[j];  
                heap[j] = temp;  
                siftDown(0, j - 1);  
            }  
            else  
                break;  
        }  
    }  
  
//    public static void main(String[] args) {  
//        // TODO Auto-generated method stub  
//        int[] a = { 8, 2, 5, 4, 3, 6, 9, 0, 7, 100 };  
//        TopK tk = new TopK(a);  
//        tk.getTopK(a, 6);  
//    }  
  
}  