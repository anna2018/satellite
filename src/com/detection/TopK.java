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
  
    //整体自底向上调整，局部自顶向下调整。  
    public void heapAdjust() {  
        int currentPos = (currentSize - 2) / 2;  
        while (currentPos >= 0) {  
            siftDown(currentPos, currentSize - 1);  
            currentPos--;  
        }  
    }  
  
    // 最小堆局部调整算法  
    // 假定前提是子女已是成最小堆.  
    private void siftDown(int start, int m) {  
        int i = start;  
        int j = 2 * i + 1;// 开始节点的左子女。  
        int temp = heap[i];  
        while (j <= m) {  
            if (j < m && heap[j] > heap[j + 1])  
                j++;// j指向俩子女中的小者  
            if (temp <= heap[j])  
                break;// 父节点小不做调整。  
            else {  
                heap[i] = heap[j];// 关键码小的子女结点上浮.  
                i = j;  
                j = 2 * i + 1;// j下移  
            }  
        }  
        heap[i] = temp;  
    }  
  
    /* 
     * a是输入的序列 函数实现获取最小的k个元素. 
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