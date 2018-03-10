package net;

import game.Blablaland;
import game.ObjectEvent;
import game.Player;
import game.User;
import game.UserObject;

import java.awt.Event;
import java.util.List;

import utils.GlobalProperties;

public class SocketMessageEvent {

	public Thread keepAlive;
	public Thread dailyTimer;

	public SocketMessage message;

	public int serverId;

	public int cacheVersion;

	public List<Integer> bytes;

	public SocketMessageEvent(String param1, boolean param2, boolean param3,
			List<Integer> bytes) {

		this.message = new net.SocketMessage();
		this.bytes = bytes;

	}

	public void onEvent() {

		/*
		 * System.err.println("after");
		 * 
		 * for(int b : message.bytes){
		 * 
		 * System.out.println(b);
		 * 
		 * }
		 */

		int bitType = message.bitReadUnsignedInt(GlobalProperties.BIT_TYPE);
		int bitStype = message.bitReadUnsignedInt(GlobalProperties.BIT_STYPE);

		//System.out.println("BIT_TYPE:" + bitType);
		//System.out.println("BIT_STYPE:" + bitStype);

		this.parsedEventMessage(bitType, bitStype, this);

	}

	public void parsedEventMessage(int param1, int param2,
			SocketMessageEvent param3) {

		// TextEvent _loc4_ = null;

		if (param1 == 3 && param2 == 1) {

			System.out.println("Erreur n° "
					+ param3.message
							.bitReadUnsignedInt(GlobalProperties.BIT_ERROR_ID));
			System.out
					.println("Camera Id: "
							+ param3.message
									.bitReadUnsignedInt(GlobalProperties.BIT_CAMERA_ID));

		}

		if (param1 == 1) {

			if (param2 == 1) {

				float _loc2_ = param3.message.bitReadUnsignedInt(32) * 1000;
				_loc2_ = _loc2_ + param3.message.bitReadUnsignedInt(10);

			} else if (param2 == 2) {

				System.err.println("Fatal Error");
				System.err.println(param3.message.bitReadString());

			} else if (param2 == 3) {

				Blablaland.PID = param3.message.bitReadUnsignedInt(24);
				System.out.println("PID: " + Blablaland.PID);

			} else if (param2 == 4) {

			} else if (param2 == 6) {

				System.out.println("Bytes: " + bytes);

				boolean _loc9_ = param3.message.bitReadBoolean();
				boolean _loc17_ = param3.message.bitReadBoolean();
				String msg = param3.message.bitReadString();

				System.out.println("Message reçu: " + msg);

			} else if (param2 == 7) {

				int nb = param3.message.bitReadUnsignedInt(16);
				System.out.println("Blablas: " + nb);

			} else if (param2 == 17) {

				int _loc4_ = param3.message.bitReadUnsignedInt(8);
				System.out.println("Result: " + _loc4_);
				if (_loc4_ == 1) {

					System.out.println("Radio available: "
							+ param3.message.bitReadBoolean());

				}

			}

		} else if (param1 == 2 && param2 == 1) {
			Player.uid = param3.message
					.bitReadUnsignedInt(GlobalProperties.BIT_USER_ID);
			Player.username = param3.message.bitReadString();
			Player.grade = param3.message
					.bitReadUnsignedInt(GlobalProperties.BIT_GRADE);
			// this.dispatchEvent(new Event("onIdentity"));
			Player.xp = param3.message.bitReadUnsignedInt(32);
			// this.dispatchEvent(new Event("onXPChange"));

			/*
			 * if(keepAlive != null)keepAlive.stop(); keepAlive = new Thread(new
			 * KeepAlive()); keepAlive.start(); //if(dailyTimer !=
			 * null)dailyTimer.stop(); //this.dailyTimer.start(); new
			 * KeepAlive().keepAliveEvent();
			 */

		} else if (param1 == 3 && param2 == 2) {

			int _loc5_ = param3.message	.bitReadUnsignedInt(GlobalProperties.BIT_ERROR_ID);
			int newCameraId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_CAMERA_ID);
			String _loc6_ = param3.message.bitReadString();
			String[] _loc7_ = _loc6_.split("");

			int id = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_MAP_ID);
			int fileId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_MAP_FILEID);

			this.userSmileyEvent(param3.message);
			System.out.println("FriendList");
			while (param3.message.bitReadBoolean()) {
				System.out.println("userId="+ param3.message.bitReadUnsignedInt(GlobalProperties.BIT_USER_ID));
				System.out.println("User=" + param3.message.bitReadString());
			}
			System.out.println("BlackList");
			while (param3.message.bitReadBoolean()) {
				System.out.println("userId="+ param3.message.bitReadUnsignedInt(GlobalProperties.BIT_USER_ID));
				System.out.println("User=" + param3.message.bitReadString());
			}
			ObjectEvent.userObjectEvent(param3.message);

		}

		if (param1 == 5 && param2 == 6) {

			int _loc14_ = param3.message
					.bitReadUnsignedInt(GlobalProperties.BIT_USER_PID);
			boolean _loc17_ = param3.message.bitReadBoolean();

		} else if (param1 == 5 && param2 == 10) {

			Binary _loc19_ = param3.message.bitReadBinaryData();
			param1 = _loc19_.bitReadUnsignedInt(4);
			int _loc20_ = param1 == 0 ? GlobalProperties.BIT_SKIN_ID: param1 == 1 ? GlobalProperties.BIT_MAP_ID: GlobalProperties.BIT_FX_ID;

			if (_loc19_.bitReadBoolean()) {
				while (_loc19_.bitReadBoolean()) {
					int _loc22_ = _loc19_.bitReadUnsignedInt(_loc20_);
					//System.out.println("SkinId= " + _loc22_);

				}
			}

		}

		if (param1 == 4 && param2 == 2) {

			System.out.println("Changement map");

			int newMapId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_MAP_ID);
			serverId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_SERVER_ID);
			int newMapFileId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_MAP_FILEID);
			int methodId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_METHODE_ID);

			System.out.println("newMapId=" + newMapId);
			System.out.println("serverId=" + serverId);
			System.out.println("newMapFileId=" + newMapFileId);
			System.out.println("methodId=" + methodId);

			System.out.println("Byte= " + bytes);

		} else if (param1 == 4 && param2 == 1) {

			System.out.println("bytes:" + bytes);

		}
		if (param1 == 3 && param2 == 5) {

			int cameraId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_CAMERA_ID);
			int mapId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_MAP_ID);
			int serverId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_SERVER_ID);
			int fileId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_MAP_FILEID);
			int methodId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_METHODE_ID);
			int errorId = param3.message.bitReadUnsignedInt(GlobalProperties.BIT_ERROR_ID);
			
			System.out.println("New Map");
			System.out.println("  cameraId="+cameraId);
			System.out.println("  mapId="+mapId);
			System.out.println("  serverId="+serverId);
			System.out.println("  fileId="+fileId);
			System.out.println("  methodId="+methodId);
			System.out.println("  errorId="+errorId);

		}
	}

	public void userSmileyEvent(SocketMessage param1) {
		int _loc3_ = 0;
		int _loc4_ = 0;
		boolean _loc2_ = false;
		while (param1.bitReadBoolean()) {
			_loc3_ = param1.bitReadUnsignedInt(8);
			if (_loc3_ == 0) {
				_loc4_ = param1
						.bitReadUnsignedInt(GlobalProperties.BIT_SMILEY_PACK_ID);
				_loc2_ = true;
			}
		}
		if (_loc2_) {
		}
	}
}
