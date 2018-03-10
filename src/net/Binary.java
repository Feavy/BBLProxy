package net;

import java.util.LinkedList;
import java.util.List;

import utils.ByteArray;

public class Binary extends ByteArray {

	public static List<Integer> powList;

	public static boolean init = init();

	public int bitLength;

	public int bitPosition;

	public Binary() {
		super();
		this.bitLength = 0;
		this.bitPosition = 0;
	}

	public static boolean init() {
		powList = new LinkedList<Integer>();
		int loc1 = 0;
		while (loc1 <= 32) {
			powList.add((int) Math.pow(2, loc1));
			loc1++;
		}
		return true;
	}

	public String getDebug() {
		String[] loc1 = new String[2 + bytes.length];
		loc1[0] = this.bitPosition + "";
		loc1[1] = this.bitLength + "";
		int i = 0;
		while (i < bytes.length) {
			loc1[i] = String.valueOf(bytes[i]);
			i++;
		}
		return String.join("=", loc1);
	}

	public void setDebug(String param1) {
		String[] loc2 = param1.split("=");
		this.bitPosition = Integer.parseInt(loc2[0]);
		this.bitLength = Integer.parseInt(loc2[1]);
		int loc3 = 2;
		while (loc3 < loc2.length) {
			this.writeByte(Integer.parseInt(loc2[loc3]));
			loc3++;
		}
	}

	public String bitReadString() {
		float loc4 = 0;
		String loc1 = "";
		float loc2 = this.bitReadUnsignedInt(16);
		int loc3 = 0;
		while (loc3 < loc2) {
			loc4 = this.bitReadUnsignedInt(8);
			if (loc4 == 255) {
				loc4 = 8364;
			}

			loc1 += (char) loc4;
			loc3++;
		}
		return loc1;
	}

	public boolean bitReadBoolean() {
		if (this.bitPosition == this.bitLength) {
			return false;
		}
		int loc1 = (int) Math.floor(this.bitPosition / 8);
		int loc2 = (int) (this.bitPosition % 8);
		//System.out.println("1: "+loc1);
		//System.out.println("2: "+loc2);
		this.bitPosition++;
		//System.out.println("byte: "+(bytes[loc1] >>> 7 - loc2 & 1));
		return (bytes[loc1] >> 7 - loc2 & 1) == 1;
	}

	public int bitReadUnsignedInt(int param1) {
		int loc4 = 0;
		int loc5 = 0;
		int loc6 = 0;
		int loc7 = 0;
		int loc8 = 0;
		if (this.bitPosition + param1 > this.bitLength) {
			this.bitPosition = this.bitLength;
			return 0;
		}
		int loc2 = 0;
		int loc3 = param1;
		while (loc3 > 0) {
			loc4 = (int) Math.floor(this.bitPosition / 8);
			loc5 = this.bitPosition % 8;
			loc6 = (int) (8 - loc5);
			loc7 = (int) Math.min(loc6, loc3);
			loc8 = bytes[loc4] >> loc6 - loc7 & powList.get(loc7)
					- 1;
			loc2 = (int) (loc2 + loc8 * powList.get(loc3 - loc7));
			loc3 = loc3 - loc7;
			this.bitPosition = this.bitPosition + loc7;
		}
		return loc2;
	}

	public int bitReadSignedInt(int param1) {
		boolean loc2 = this.bitReadBoolean();
		return this.bitReadUnsignedInt(param1 - 1) * (!!loc2 ? 1 : -1);
	}

	public Binary bitReadBinaryData() {
		float loc1 = this.bitReadUnsignedInt(16);
		return this.bitReadBinary(loc1);
	}

	public Binary bitReadBinary(float loc1) {
		int loc5 = 0;
		Binary loc2 = new Binary();
		int loc3 = this.bitPosition;
		while (this.bitPosition - loc3 < loc1) {
			if (this.bitPosition == this.bitLength) {
				return loc2;
			}
			loc5 = (int) Math.min(8, loc1 - this.bitPosition + loc3);
			loc2.bitWriteUnsignedInt(loc5, this.bitReadUnsignedInt(loc5));
		}
		return loc2;
	}

	public void bitWriteString(String param1) {
		int loc4 = 0;
		int loc2 = Math.min(param1.length(), powList.get(16) - 1);
		this.bitWriteUnsignedInt(16, loc2);
		int loc3 = 0;
		while (loc3 < loc2) {
			loc4 = param1.charAt(loc3);
			if (loc4 == 8364) {
				loc4 = 255;
			}
			this.bitWriteUnsignedInt(8, loc4);
			loc3++;
		}
	}
	
	public void bitWriteUnsignedInt(int param1, long l) {
		int loc4 = 0;
		int loc5 = 0;
		int loc6 = 0;
		int loc7 = 0;
		l = Math.min(powList.get(param1) - 1, l);
		int loc3 = param1;
		
		while (loc3 > 0) {
			
			loc4 = (this.bitLength % 8);
			if (loc4 == 0) {
				writeBoolean(false);
			}
			loc5 = 8 - loc4;	
			loc6 = Math.min(loc5, loc3);	
			loc7 = this.Rshift(l, (int) (loc3 - loc6));		
			bytes[bytes.length - 1] += loc7*powList.get(loc5 - loc6);			
			l = l - loc7 * powList.get(loc3 - loc6);		
			loc3 -= loc6;
			this.bitLength = this.bitLength + loc6;
						
		}
	}

	public void bitWriteBoolean(boolean param1) {
		int loc2 = this.bitLength % 8;
		if (loc2 == 0) {
			writeBoolean(false);
		}
		if (param1) {
			bytes[bytes.length - 1] = (bytes[bytes.length - 1] + powList
					.get(7 - loc2));
		}
		this.bitLength++;
	}

	public void bitWriteBinaryData(Binary param1) {
		int loc2 = Math.min(param1.bitLength, powList.get(16) - 1);
		this.bitWriteUnsignedInt(16, loc2);
		this.bitWriteBinary(param1);
	}

	public void bitWriteBinary(Binary param1) {
		int loc3 = 0;
		int loc4 = 0;
		param1.bitPosition = 0;
		int loc2 = param1.bitLength;
		while (loc2 > 0) {
			loc3 = Math.min(8, loc2);
			loc4 = param1.bitReadUnsignedInt(loc3);
			this.bitWriteUnsignedInt(loc3, loc4);
			loc2 = loc2 - loc3;
		}
	}

    public void bitWriteSignedInt(int param1, int param2)
    {
       this.bitWriteBoolean(param2 >= 0);
       this.bitWriteUnsignedInt(param1 - 1,Math.abs(param2));
    }
	
	public int Rshift(double param1, int param2) {
		return (int)Math.floor(param1 / powList.get(param2));
	}

	public float Lshift(float param1, int param2) {
		return param1 * powList.get(param2);
	}

}
