package utils;

import java.util.List;

import net.SocketMessage;

public class ByteUtils {

	public static int[] ByteListTobyteArray(List<Integer> in) {

		int[] bytes = new int[in.size()];
		int i = 0;
		for (int b : in.toArray(new Integer[in.size()])) {

			bytes[i] = b;
			i++;

		}

		return bytes;

	}
	
	public static SocketMessage byteListToSocketMessage(Integer[] param) {
		//param.add(0);// TODO actionscript->java
		ByteArray inBuffer = new ByteArray();
		
		int _loc4_ = 0;
		SocketMessage _loc5_;
		int _loc2_ = param.length;
		int _loc3_ = 0;
		while (_loc3_ < _loc2_) {
			_loc4_ = param[_loc3_];
			if (_loc4_ == 0) {
				_loc5_ = new SocketMessage();
				_loc5_.readMessage(inBuffer);
				_loc4_ = _loc5_.bitReadUnsignedInt(16);
				SocketMessage _loc6_ = new SocketMessage();
				_loc6_.writeBytes(_loc5_, 2, 0);
				_loc6_.bitLength = _loc6_.length() * 8;
				return _loc6_;
				
			} else {
				inBuffer.writeByte((byte) _loc4_);
			}
			_loc3_++;
		}
		
		return null;

	}
	
	public static int[] ByteArrayTobyteArray(int[] bytes){
		
		int[] response = new int[bytes.length];
		
		int i = 0;
		
		for(int b : bytes){
			
			response[i] = b;
			i++;
			
		}
		
		return response;
		
	}
	
	public static byte[] intArrayToByteArray(List<Integer> in){
		
		byte[] response = new byte[in.size()];
		int i = 0;
		for(int integer : in.toArray(new Integer[in.size()])){
			response[i] = (byte)integer;
			i++;
		}
		
		return response;
		
	}
	
}
