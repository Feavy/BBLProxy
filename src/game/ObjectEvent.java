package game;

import java.util.ArrayList;

import utils.GlobalProperties;
import net.SocketMessage;

public class ObjectEvent {

	static ArrayList<UserObject> objectList = new ArrayList<UserObject>();

	public static void userObjectEvent(SocketMessage param1) {

		System.out.println("UserObjectEvent called");
		
		int _loc4_ = 0;
		boolean _loc2_ = false;
		UserObject _loc3_ = null;
		while (param1.bitReadBoolean()) {
						
			_loc4_ = param1.bitReadUnsignedInt(8);
			if (_loc4_ == 0) {
				_loc3_ = new UserObject();
				_loc3_.id = param1.bitReadUnsignedInt(32);
				_loc3_.fxFileId = param1.bitReadUnsignedInt(GlobalProperties.BIT_FX_ID);
				_loc3_.objectId = param1.bitReadUnsignedInt(GlobalProperties.BIT_FX_SID);
				_loc3_.count = param1.bitReadUnsignedInt(32);
				_loc3_.expire = param1.bitReadUnsignedInt(32) * 1000;
				_loc3_.visibility = param1.bitReadUnsignedInt(3);
				_loc3_.genre = param1.bitReadUnsignedInt(5);
				_loc3_.data = param1.bitReadBinaryData();
				objectList.add(_loc3_);
				debug(_loc3_);
				_loc2_ = true;
			} else if (_loc4_ == 1) {
				_loc3_ = getObjectById(param1.bitReadUnsignedInt(32));
				if (_loc3_ != null) {
					_loc3_.count = param1.bitReadUnsignedInt(32);
					_loc3_.expire = param1.bitReadUnsignedInt(32) * 1000;
					_loc3_.data = param1.bitReadBinaryData();
					System.out.println("ObjectChanged");
					// _loc3_.dispatchEvent(new Event("onChanged"));
					debug(_loc3_);
				}
			}
		}
		if (_loc2_) {
			System.out.println("ObjectList initialized");
			// this.dispatchEvent(new Event("onObjectListChange"));
		}
	}

	public static void debug(UserObject obj){
		
		System.out.println("Id="+obj.id);
		System.out.println("fxFileId="+obj.fxFileId);
		System.out.println("objectId="+obj.objectId);
		System.out.println("count="+obj.count);
		System.out.println("expire="+obj.expire);
		System.out.println("visibility="+obj.visibility);
		System.out.println("genre="+obj.genre);
		System.out.println("data="+obj.data);
		
	}
	
	public static UserObject getObjectById(float param1) {
		int _loc2_ = 0;
		while (_loc2_ < objectList.size()) {
			if (objectList.get(_loc2_).id == param1) {
				return objectList.get(_loc2_);
			}
			_loc2_++;
		}
		return null;
	}

}
