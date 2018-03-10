package net;

import java.util.LinkedList;
import java.util.List;

import utils.ByteArray;

public class SocketMessage extends Binary {

	public SocketMessage() {
		super();
	}

	public SocketMessage duplicate() {
		SocketMessage _loc1_ = new SocketMessage();
		_loc1_.writeBytes(this, 0, bytes.length);
		_loc1_.bitLength = bitLength;
		_loc1_.bitPosition = bitPosition;
		return _loc1_;
	}

	public void readMessage(ByteArray param) {
		int _loc2_ = 0;
		while (_loc2_ < param.length()) {
			if (param.get(_loc2_) == 1) {
				_loc2_++;
				this.writeByte(param.get(_loc2_) == 2 ? (byte) 1 : (byte) 0);
			} else {
				this.writeByte(param.get(_loc2_));
			}
			_loc2_++;
		}
		bitLength = length() * 8;
	}

	public List<Integer> exportMessage() {
		List<Integer> _loc1_ = new LinkedList<Integer>();
		int _loc2_ = 0;
		while (_loc2_ < length()) {
			if (bytes[_loc2_] == 0) {
				_loc1_.add(1);
				_loc1_.add(3);
			} else if (bytes[_loc2_] == 1) {
				_loc1_.add(1);
				_loc1_.add(2);
			} else {
				_loc1_.add(bytes[_loc2_]);
			}
			_loc2_++;
		}
		return _loc1_;
	}

}
