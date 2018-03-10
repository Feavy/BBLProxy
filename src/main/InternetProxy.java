package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class InternetProxy {

	public ServerSocket client;
	public boolean connected = false;
	public Thread requestHandler;
	
	public InternetProxy(){
		

		
	}
	
	public void start() throws IOException{
		
		connected = true;
		this.client = new ServerSocket(44444);
		requestHandler = new Thread(new RequestHandler());
		requestHandler.start();
		
	}
	
	public void stop(){
		
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connected = false;
		requestHandler.stop();
		
	}
	
	private class RequestHandler implements Runnable{


		private Socket req;
		private InputStream  clientInputStream;
		private OutputStream clientOutputStream;
		
		Socket serverSocket;
		private InputStream serverInputStream;
		private OutputStream serverOutputStream;
		
		private int b;
		private String stringData;
		
		String host;
		int port;
		
		public void run() {
			
			while(connected){
			
				try {
					
					req = client.accept();
					new Thread(new Request(req)).start();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
		}
		
	}
		
	private class Request implements Runnable{
		
		private OutputStream output;
		private ArrayList<Byte> request;
		private String host;
		private int port;
		
		InputStream clientInputStream;
		OutputStream clientOutputStream;
		ArrayList<Byte> data;
		ArrayList<Byte> splittedData;
		int b;
		String stringData;
		String connexionType;
		Socket req;
		
		public Request(Socket req) throws IOException{
		
			clientInputStream = req.getInputStream();
			clientOutputStream = req.getOutputStream();
			b = 0;
			this.req = req;
			
		}

		public void run() {
			
			try{
				
				connexionType = "default";
				data = new ArrayList<Byte>();
				splittedData = new ArrayList<Byte>();
				
				while((b = clientInputStream.read()) != -1){
					
					data.add((byte)b);
					splittedData.add((byte)b);
					if(b == 10){
						stringData = new String(ByteListTobyteArray(splittedData), 0, splittedData.size());
						if(stringData.toLowerCase().contains("host:")){
							host = getHost(stringData);
							port = stringData.trim().split(":").length==2?80:Integer.parseInt(stringData.trim().split(":")[2]);
						}else if(stringData.toLowerCase().contains("Connection")){
							
							connexionType = stringData.trim().split(":")[1];
							
						}else if(splittedData.size() == 2){
							break;
						}
						splittedData = new ArrayList<Byte>();
					}
					
				}
				
				stringData = new String(ByteListTobyteArray(data), 0, data.size());
				System.out.println("REQ:");
				System.out.println(stringData);
				
				System.out.println("Host:");
				System.out.println(host+":"+port);
				
				int b = 0;
				Socket s = new Socket(host, port);
				
				OutputStream serverOutputStream = s.getOutputStream();
				InputStream serverInputStream = s.getInputStream();
				serverOutputStream.write(ByteListTobyteArray(data));
				serverOutputStream.flush();
				
				ArrayList<Byte> data = new ArrayList<Byte>();
				
				while((b = serverInputStream.read()) != -1){
					
					data.add((byte)b);
					
				}
				
				System.out.println("REP:");
				System.out.println(new String(ByteListTobyteArray(data), 0, data.size()));
				
				clientOutputStream.write(ByteListTobyteArray(data));
				clientOutputStream.flush();
				
				if(connexionType.equals("default")){
					req.close();
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		
	}
	
	public String getHost(String line){
		
		line = line.trim().split(":")[1];
		if(line.split(".").length == 3)
			return line;
		String[] splittedLine = line.split("\\.");
		return splittedLine[splittedLine.length-2]+"."+splittedLine[splittedLine.length-1];
		
	}
	
	public byte[] ByteListTobyteArray(ArrayList<Byte> array){
		
		byte[] rep = new byte[array.size()];
		for(int i = 0; i < array.size(); i++){
			rep[i] = array.get(i);
		}
		return rep;
	}
	
}
