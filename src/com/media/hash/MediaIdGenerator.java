package com.media.hash;

import java.util.Random;

public class MediaIdGenerator {
	//产生16位随机视频点播id，范围[0-9a-zA-Z]
	public static String generateMediaId(){
		StringBuffer idstr = new StringBuffer();
		Random rand = new Random();
	
		for(int i=0; i<16; i++){
			int value = rand.nextInt(62);
			if(value <= 9)
				idstr.append((char)(value + '0'));
			else if(value <= 35)
				idstr.append((char)(value - 10 + 'a'));
			else
				idstr.append((char)(value - 36 + 'A'));
		}
		
		return idstr.toString();
	}
}
