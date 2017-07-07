package com.util;

import java.util.Random;


public class RandomDouble {
	public static int getRandom(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
	}
}
