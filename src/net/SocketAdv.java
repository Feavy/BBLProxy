package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;

import main.Proxy;
import utils.ByteArray;
import utils.ByteUtils;

public class SocketAdv{

	private ByteArray inBuffer;

	private int inCmpt;

	private int outCmpt;

	private Thread flushTimer;

	private Timer keepAliveTimer;

	PrintWriter writer;
	
	private String host;
	private int port;

	private Socket s;
	
	public SocketAdv(Socket s){
		
		this.s = s;
		
	}
	
	public void debugPacket(List<Integer> array){
		
		System.out.println("ServerResponse: ");
		
		for(int i : array.toArray(new Integer[array.size()])){
			
			System.out.print(i+",");
			
		}
		
		System.out.println("");
		
	}
	
	class flushEventTask implements Runnable {

		private long sleepTime;

		public flushEventTask(long sleepTime) {

			this.sleepTime = sleepTime;

		}

		public void run() {

			flushEvent();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void flushEvent() {
		// System.out.println("flush");
		if (s.isConnected()) {
			try {

				OutputStream output = s.getOutputStream();
				output.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		flushTimer.stop();
	}
	
	public void send(SocketMessage param1, boolean param2, int outCmpt) {

		try {

			// TODO Essayer avec printWriter à la place de OutputSream			
			OutputStream output = s.getOutputStream();

			SocketMessage _loc3_ = null;
			List<Integer> _loc4_ = null;
			if (!s.isClosed()) {

				outCmpt++;
				Proxy.outCmpt++;
				if (outCmpt >= 65530) {
					outCmpt = 12;
				}
				_loc3_ = new SocketMessage();
				_loc3_.bitWriteUnsignedInt(16, outCmpt);
				_loc4_ = _loc3_.exportMessage();
				output.write(ByteUtils.intArrayToByteArray(_loc4_));
				List<Integer> _loc5_ = param1.exportMessage();
				output.write(ByteUtils.intArrayToByteArray(_loc5_));
				output.write(0);
				if (param2) {
					output.flush();
					//debugPacket2(_loc4_, _loc5_);
					return;
				} else {
					if (flushTimer != null)
						flushTimer.stop();
					this.flushTimer = new Thread(new flushEventTask(1000L));
					flushTimer.start();
					//debugPacket2(_loc4_, _loc5_);
					return;
				}
				
			}

		} catch (Exception ex) {
			System.err.println("Connection perdue");
		}
	}

	public void debugPacket2(List<Integer>... in){
		
		System.out.println("ClientRequest: ");
		
		for(List<Integer> l : in){
			
			for(int i : l.toArray(new Integer[l.size()])){
				
				System.out.print(i+",");
				
			}
			
		}
		
		System.out.println("");
		
	}

	public void eventMessage(SocketMessageEvent param1) {
		param1.onEvent();
	}

}
