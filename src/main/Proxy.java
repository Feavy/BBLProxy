package main;

import game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import net.Binary;
import net.SocketAdv;
import net.SocketMessage;
import net.SocketMessageEvent;
import utils.Array;
import utils.ByteArray;
import utils.ByteUtils;
import utils.Debug;
import utils.GlobalProperties;
import utils.PacketUtils;

public class Proxy{

	public static int outCmpt = 12;
	
	public Socket connectionWithServer;
	public OutputStream serverOutput;
	public InputStream serverInputStream;
	
	public static Socket CommunicationWithClient;
	public static Socket CommunicationWithServer;
	
	public int inCmptToClient = 12;
	public int inCmpt = 12;
	public ByteArray inBuffer = new ByteArray();
	
	public int inCmptLength = 4;
	
	public byte response[];
	int rep[];
	
	SocketAdv sender;
	
	public Thread serverResponseThread;
	public Thread clientRequestThread;
	
	boolean messageSent = false;
	
	int mapId = 0;
	
	private int[] infoMessage = {80,784,1872,2112,2904,3552,3800,3520,5280,4944,5928,6336,1424,-3872,3744,7168,-1792,9024,9960,
			9184,9960,9360,10872,10560,12384,-2944,11832,15392,16160,-3456,12536,13440,18336,15888,15504,-3936,-10864,6160,20976,
			59584,16456,23232,18616,24672,24144,4064,-5632,11520,21160,28784,1608,384,384,-16032,17544,11584,14976};
	public Proxy(){
		
	}
	
	public void start() {
		
		try {

			ServerSocket server = new ServerSocket(843);

			Socket s = server.accept();
			int b = -1;
			InputStream input = s.getInputStream();
			OutputStream output;
			while ((b = input.read()) != 0) {
				System.out.print((char)b);
			}
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			writer.println(new char[] { 60, 33, 'D', 'O', 'C', 'T', 'Y', 'P','E', 32, 'c', 'r', 'o', 's', 's', 45, 'd', 'o', 'm', 'a','i', 'n', 45, 'p', 'o', 'l', 'i', 'c', 'y', 32, 'S', 'Y','S', 'T', 'E', 'M', 32, 34, 'h', 't', 't', 'p', 58, 47, 47,'w', 'w', 'w', 46, 'm', 'a', 'c', 'r', 'o', 'm', 'e', 'd','i', 'a', 46, 'c', 'o', 'm', 47, 'x', 'm', 'l', 47, 'd','t', 'd', 's', 47, 'c', 'r', 'o', 's', 's', 45, 'd', 'o','m', 'a', 'i', 'n', 45, 'p', 'o', 'l', 'i', 'c', 'y', 46,'d', 't', 'd', 34, 62, 13, 10, 60, 'c', 'r', 'o', 's', 's',45, 'd', 'o', 'm', 'a', 'i', 'n', 45, 'p', 'o', 'l', 'i','c', 'y', 62, 13, 10, 9, 60, 'a', 'l', 'l', 'o', 'w', 45,'a', 'c', 'c', 'e', 's', 's', 45, 'f', 'r', 'o', 'm', 32,'t', 'o', 45, 'p', 'o', 'r', 't', 's', 61, 34, '1', '2','3', '0', '1', 45, '1', '2', '3', '2', '1', 34, 32, 'd','o', 'm', 'a', 'i', 'n', 61, 34, 42, 46, 'b', 'l', 'a','b', 'l', 'a', 'l', 'a', 'n', 'd', 46, 'c', 'o', 'm', 34,32, 47, 62, 13, 10, 9, 60, 'a', 'l', 'l', 'o', 'w', 45,'a', 'c', 'c', 'e', 's', 's', 45, 'f', 'r', 'o', 'm', 32,'t', 'o', 45, 'p', 'o', 'r', 't', 's', 61, 34, '1', '2','3', '0', '1', 45, '1', '2', '3', '2', '1', 34, 32, 'd','o', 'm', 'a', 'i', 'n', 61, 34, 'b', 'l', 'a', 'b', 'l','a', 'l', 'a', 'n', 'd', 46, 'c', 'o', 'm', 34, 32, 47, 62,13, 10, 9, 60, 'a', 'l', 'l', 'o', 'w', 45, 'a', 'c', 'c','e', 's', 's', 45, 'f', 'r', 'o', 'm', 32, 't', 'o', 45,'p', 'o', 'r', 't', 's', 61, 34, '1', '2', '3', '0', '1',45, '1', '2', '3', '2', '1', 34, 32, 'd', 'o', 'm', 'a','i', 'n', 61, 34, 42, 46, 'n', 'i', 'v', 'e', 'a', 'u','9', '9', 46, 'c', 'o', 'm', 34, 32, 47, 62, 13, 10, 60,47, 'c', 'r', 'o', 's', 's', 45, 'd', 'o', 'm', 'a', 'i','n', 45, 'p', 'o', 'l', 'i', 'c', 'y', 62 });
			writer.flush();

			s.close();
			
			b = 0;
			
			Socket serverSocket = new Socket("91.121.47.136",12301);
			
			ServerSocket proxyServer = new ServerSocket(12301);
			
			s = proxyServer.accept();
			
			GlobalProperties.initialize();
			
			serverResponseThread = new Thread(new ServerResponseHandler(serverSocket));
			serverResponseThread.start();
			
			clientRequestThread = new Thread(new ClientRequestHandler(proxyServer));
			clientRequestThread.start();
			
			new Thread(new CommandReader()).start();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public void stop(){
		
		serverResponseThread.stop();
		clientRequestThread.stop();
		
	}

	private class CommandReader implements Runnable{

		private Scanner sc;
		private String[] args;
		
		public CommandReader(){}
		
		public void run() {
			
			while(true){
				
				sc = new Scanner(System.in);
				args = sc.nextLine().split(" ");
				if(args[0].equals("/infos")){
					
					System.out.println(Player.username);
					
				}else if(args[0].equals("/pid")){
					
					SocketMessage _loc1_ = new SocketMessage();
					_loc1_.bitWriteUnsignedInt(GlobalProperties.BIT_TYPE, 1);
					_loc1_.bitWriteUnsignedInt(GlobalProperties.BIT_STYPE, 3);
					sender.send(_loc1_, true, outCmpt);
					
				}else if(args[0].equals("/msg")){
					
					String msg = "";
					
					for(int i = 0; i < args.length; i++){
						if(i!=0)msg += args[i]+" ";
					}
					
					SocketMessage _loc3_ = new SocketMessage();
		            _loc3_.bitWriteUnsignedInt(GlobalProperties.BIT_TYPE,1);
		            _loc3_.bitWriteUnsignedInt(GlobalProperties.BIT_STYPE,5);
		            _loc3_.bitWriteString(msg);
		            _loc3_.bitWriteUnsignedInt(3,0);
		            sender.send(_loc3_,false, outCmpt);
					
				}else if(args[0].equals("/map")){
					
					try{
					//inCmptToClient++;
		            SocketMessage _loc4_ = new SocketMessage();
		            _loc4_.bitWriteUnsignedInt(GlobalProperties.BIT_TYPE,3);
		            _loc4_.bitWriteUnsignedInt(GlobalProperties.BIT_STYPE,5);
		            _loc4_.bitWriteUnsignedInt(GlobalProperties.BIT_METHODE_ID,(args.length == 3)?Integer.parseInt(args[2]):1);
		            _loc4_.bitWriteUnsignedInt(GlobalProperties.BIT_CAMERA_ID,1);
		            _loc4_.bitWriteUnsignedInt(GlobalProperties.BIT_MAP_ID,Integer.parseInt(args[1]));
		            _loc4_.bitWriteUnsignedInt(GlobalProperties.BIT_SERVER_ID,0);
		            //param1.mainUser.exportStateToMessage(_loc4_,param3);
		            sender.send(_loc4_, false, outCmpt);
					
					}catch(Exception e){
						e.printStackTrace();
					}
							
				}else if(args[0].equals("/grimpe")){
					
					
					
				}
				
			}
			
		}
		
	}
	
	private class ServerResponseHandler implements Runnable {

		private InputStream input;
		private OutputStream output;
		private int b = 0;

		public ServerResponseHandler(Socket s)  {

			try{
				CommunicationWithServer = s;
				sender = new SocketAdv(CommunicationWithServer);
				input = s.getInputStream();
				output = s.getOutputStream();
			}catch(Exception ex){
				ex.printStackTrace();
			}

		}

		public void run() {

			while (true) {

				try {
					
					List<Integer> received = new LinkedList<Integer>();

					while ((b = input.read()) != 0) {

						received.add(b);

					}

					received.add(0);

					socketDataHandler(received);
					
					sendPacketToClient(CommunicationWithClient.getOutputStream(), received);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
	}

	public void socketDataHandler(List<Integer> param) {
		//param.add(0);// TODO actionscript->java
		int _loc4_ = 0;
		SocketMessage _loc5_;
		SocketMessageEvent _loc6_;
		int _loc2_ = param.size();
		int _loc3_ = 0;
		while (_loc3_ < _loc2_) {
			_loc4_ = param.get(_loc3_);
			if (_loc4_ == 0) {
				this.inCmpt++;
				if (this.inCmpt >= 65530) {
					this.inCmpt = 12;
				}
				_loc5_ = new SocketMessage();
				_loc5_.readMessage(this.inBuffer);
				_loc4_ = _loc5_.bitReadUnsignedInt(16);
				if (!(_loc4_ < this.inCmpt || _loc4_ > this.inCmpt + 20)) {
					_loc6_ = new SocketMessageEvent("onMessage", true, true, param);
					_loc6_.message.writeBytes(_loc5_, 2, 0);
					_loc6_.message.bitLength = _loc6_.message.length() * 8;
					_loc6_.onEvent();
					this.inBuffer = new ByteArray();
				}
			} else {
				this.inBuffer.writeByte((byte) _loc4_);
			}
			_loc3_++;
		}

	}
	
	private class ClientRequestHandler implements Runnable {

		private ServerSocket server;
		private InputStream input;
		private int b;

		public ClientRequestHandler(ServerSocket server)  {

			this.server = server;
			Socket s;
			try {
				s = server.accept();
				CommunicationWithClient = s;
				input = s.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void run() {

			while (true) {

				try {

					List<Integer> received = new LinkedList<Integer>();

					while ((b = input.read()) != 0) {

						received.add(b);

					}

					received.add(0);

					sendPacketToServer(CommunicationWithServer.getOutputStream(), received);

				} catch (Exception ex) {
					stop();
				}

			}

		}

	}

	public byte[] intArrayTobyteArray(int[] array) {

		response = new byte[array.length];

		for (int i = 0; i < array.length; i++) {

			response[i] = (byte) array[i];

		}

		return response;

	}

	public void sendPacketToClient(OutputStream clientOutput, List<Integer> bytes) {

		inCmptToClient++;
		//System.out.println("ServerResponse: ");
		//Debug.debug(bytes);
		//System.out.println("inCmpt="+inCmptToClient);

		bytes = changeCmpt(bytes, inCmptToClient);
		
		SocketMessage sm = PacketUtils.socketDataHandler(bytes);
		int bit_type = sm.bitReadUnsignedInt(5);
		int bit_stype = sm.bitReadUnsignedInt(5);
		
		System.out.println("Server:");
		System.out.println("BIT_TYPE="+bit_type);
		System.out.println("BIT_STYPE="+bit_stype);
		
		int l = getInCmptLength(bytes);
		
		try {
			if(bit_type == 2 && bit_stype == 11){
				int[] newBytes = {1, 3, 26, 18, 194, 1, 3, 240, 161, 1, 3, 0};
				for(int i = l; i < newBytes.length-4; i++){
					bytes.set(i, newBytes[i]);	
				}
			}else if(bit_type == 5 && bit_stype == 10){
				
				
				
			}else if(bit_type == 1 && bit_stype == 6){
			}
			clientOutput.write(intArrayTobyteArray(IntegerListTointArray(bytes)));
			clientOutput.flush();
			
			if(bit_type == 4 && bit_stype == 1){
				
				if(!messageSent){
					sendMessageToClient(decrypt(infoMessage));
				}
			}
			
		} catch (IOException e) {
			stop();
			e.printStackTrace();
		}

	}
	
	public void sendMessageToClient(String message){
		
		inCmptToClient++;
		SocketMessage msg2 = new SocketMessage();
		msg2.bitWriteUnsignedInt(16, inCmptToClient);
		msg2.bitWriteUnsignedInt(GlobalProperties.BIT_TYPE, 1);
		msg2.bitWriteUnsignedInt(GlobalProperties.BIT_STYPE, 6);
		msg2.bitWriteBoolean(false);
		msg2.bitWriteBoolean(false);
		msg2.bitWriteString(message);
		
		try{
			System.out.println("msg= ");
			//Debug.debug(msg2.exportMessage());
			OutputStream clientOutput = CommunicationWithClient.getOutputStream();
			clientOutput.write(intArrayTobyteArray(IntegerListTointArray(msg2.exportMessage())));
			clientOutput.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		messageSent = true;
		
	}
	
	public void sendPacketToServer(OutputStream serverOutput, List<Integer> bytes) {

		outCmpt++;
		//System.out.println("Avant:");
		//Debug.debug(bytes);
		bytes = clientPacketHandler(bytes, outCmpt);
		//System.out.println("Après:");
		//Debug.debug(bytes);
		try {
			
			changeCmpt(bytes, outCmpt);
			serverOutput.write(intArrayTobyteArray(IntegerListTointArray(bytes)));
			serverOutput.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Integer> clientPacketHandler(List<Integer> bytes, int outCmpt){
		
		
		SocketMessage msg = ByteUtils.byteListToSocketMessage(bytes.toArray(new Integer[bytes.size()]));
		
		int bitType=msg.bitReadUnsignedInt(5);
		int bitStype= msg.bitReadUnsignedInt(5);
		
		System.out.println("Client:");
		System.out.println("BIT_TYPE="+bitType);
		System.out.println("BIT_STYPE="+bitStype);
		
		if(bitType == 2 && (bitStype==2 || bitStype == 1)){
			
			
		}
		
		return bytes;
		
	}
	
	public int getInCmptLength(List<Integer> bytes){
		
		SocketMessage sm = new SocketMessage();
		for(int i : bytes.toArray(new Integer[bytes.size()])){
			
			sm.writeByte(i);
			
		}
		int cmpt = sm.bitReadUnsignedInt(16);
		
		SocketMessage sm2 = new SocketMessage();
		sm2.bitWriteUnsignedInt(16, cmpt);
		
		return sm2.exportMessage().size();
		
	}
	
	public List<Integer> changeCmpt(List<Integer> bytes, int cmpt){

		//System.out.println("Avant: ");
		//debug(bytes);
		
		SocketMessage sm = new SocketMessage();
		sm.bitWriteUnsignedInt(16, cmpt);
		List<Integer> _loc4_ = sm.exportMessage();
		
		boolean same = true;
		
		for(int i = 0; i < _loc4_.size(); i++){
			
			if(bytes.get(i) != _loc4_.get(i)){
				same = false;
				break;
			}
			
		}
		
		if(same)
			return bytes;
		
		sm = new SocketMessage();
		sm.bitWriteUnsignedInt(16, cmpt-1);
		List<Integer> _loc4_2 = sm.exportMessage();
		
		if(_loc4_2.size() != _loc4_.size()){
			int i;
			for(i = 0; i < _loc4_.size(); i++){
				
				bytes.set(i, _loc4_.get(i));
				
			}
			
			bytes.remove(i+1);
			
		}else{
			
			for(int i = 0; i < _loc4_.size(); i++){
				
				bytes.set(i, _loc4_.get(i));
				
			}
			
		}
		
		//System.out.println("Après: ");
		//debug(bytes);
		
		return bytes;
		
	}
	
	public List<Integer> intArrayToIntegerList(int[] array){
		
		List<Integer> response = new ArrayList<Integer>();
		
		for(int i = 0; i < array.length; i++){
			
			response.add(array[i]);
			
		}
		
		return response;
		
	}
	
	public int[] IntegerListTointArray(List<Integer> bytes) {

		rep = new int[bytes.size()];
		
		for (int i = 0; i < bytes.size(); i++) {

			try{
			
				rep[i] = bytes.get(i);
			
			}catch(Exception ex){
				ex.printStackTrace();
			}

		}

		return rep;

	}

	public static String decrypt(int[] array){
		
		String response = "";
		int charId;
		
		char chars[] = new char[array.length];
		
		for(int i = 0; i < array.length; i++){
			
			charId = array[i]+48*(i*8);
			charId /= 8*(i+1);
			//charId = charId/8/(i+1);
			chars[i] = (char)charId;
			response += Character.toString((char)charId);
			
		}
		
		return new String(chars);
		
	}
	
}
