package utils;

import java.util.Date;

public class GlobalProperties {

    public static int BIT_TYPE = 5;
    
    public static int BIT_STYPE = 5;
    
    public static int BIT_MAP_ID = 12;
    
    public static int BIT_MAP_FILEID = 12;
    
    public static int BIT_MAP_REGIONID = 4;
    
    public static int BIT_MAP_PLANETID = 4;
    
    public static int BIT_SWF_TYPE = 2;
    
    public static int BIT_ERROR_ID = 5;
    
    public static int BIT_CAMERA_ID = 9;
    
    public static int BIT_USER_ID = 24;
    
    public static int BIT_USER_PID = 24;
    
    public static int BIT_METHODE_ID = 6;
    
    public static int BIT_FX_ID = 6;
    
    public static int BIT_FX_SID = 16;
    
    public static int BIT_FX_OID = 2;
    
    public static int BIT_SKIN_ID = 10;
    
    public static int BIT_TRANSPORT_ID = 10;
    
    public static int BIT_SMILEY_ID = 6;
    
    public static int BIT_SMILEY_PACK_ID = 5;
    
    public static int BIT_GRADE = 10;
    
    public static int BIT_SKIN_ACTION = 3;
    
    public static int BIT_SERVER_ID = 2;
    
    public static int BIT_CHANNEL_ID = 16;
    
    public static long lastTime;
    
    public static long keepAliveDelay = 1000*60*5;
    
	public static float serverTimeOffset = 0;
	public static long serverTimeCount;
	public static long serverTimePing;
   	
	public static void initialize(){
		
		lastTime = new Date().getTime();
		
	}
	
	public static void addTime(long time){
		
		lastTime+=time;
		
	}
	
    public static long getTimer()
    {
       return new Date().getTime() - lastTime;
    }
    
    public static void setServerTime(float time){
    	
        if(new Date().getTime() - time < serverTimeOffset || serverTimeOffset == 0 || time == 0)
        {
        	
        	System.out.println("Date="+new Date().getTime());
        	System.out.println("Time= "+time);
        	
           serverTimeOffset = new Date().getTime() - time;
        }
    	
    }
    
    public static float getServerTime(){
    	
    	return new Date().getTime() - serverTimeOffset;
    	
    }
	
}
