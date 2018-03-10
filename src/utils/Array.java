package utils;

import java.util.ArrayList;

public class Array {

    public static ArrayList splice(ArrayList array, int startIndex, int itemCount){
    	
    	ArrayList newArray = new ArrayList();
    	
    	for(int i = 0; i < itemCount; i++){
    		
    		newArray.add(array.get(i+startIndex));
    		
    	}
    	
    	return newArray;
    	
    }
	
}
