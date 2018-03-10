package utils;

import java.io.ByteArrayInputStream;

import net.SocketMessage;

public class ByteArray {

	public int[] bytes = {};

	public int length() {

		return bytes.length;

	}

	public void writeByte(int b) {

		int[] newBytes = new int[bytes.length + 1];

		for (int i = 0; i < bytes.length; i++) {

			newBytes[i] = bytes[i];

		}

		newBytes[newBytes.length - 1] = b;
		bytes = newBytes;

	}

	public int get(int index) {

		return bytes[index];

	}

	public void writeBytes(ByteArray msg, int offset, int size) {
		
		int[] input = ByteUtils.ByteArrayTobyteArray(msg.bytes);
		int[] output = (size == 0)?new int[msg.length()-offset]:new int[size];
		
		for(int i = offset; i < output.length+offset; i++){
			
			output[i-offset] = input[i];
			
		}
		
		bytes = output;

	}

	public void writeBoolean(boolean b) {

		int[] newBytes = new int[bytes.length + 1];

		for (int i = 0; i < bytes.length; i++) {

			newBytes[i] = bytes[i];

		}

		newBytes[newBytes.length - 1] = (byte) ((b) ? 1 : 0);
		bytes = newBytes;

	}

}
