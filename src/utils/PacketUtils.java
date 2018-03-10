package utils;

import java.util.List;

import net.SocketMessage;

public class PacketUtils {

	public static SocketMessage socketDataHandler(List<Integer> param) {
		
		ByteArray inBuffer= new ByteArray();
		
		//param.add(0);// TODO actionscript->java
		int _loc4_ = 0;
		SocketMessage _loc5_;
		int _loc2_ = param.size();
		int _loc3_ = 0;
		while (_loc3_ < _loc2_) {
			_loc4_ = param.get(_loc3_);
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
	
}
