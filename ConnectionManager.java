package dataServer2;


import java.io.File;
import java.io.FileNotFoundException;


import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;





public abstract class ConnectionManager {
	public int port;
	Thread T;
	boolean active =false;
	 String ip;
	public ConnectionManager(int port) throws UnknownHostException {
		this.ip=InetAddress.getLocalHost().getHostAddress();
		this.port = port;
	}
	
	

	
	public void start() {
		active=true;
	T=new Thread(new Runnable() {

		public void run() {
			try {
				ServerSocket thisServerSocket = new ServerSocket(port);
				int count=0;
				
				
				
				while(!thisServerSocket.isClosed()&&active()) {
					
					//System.out.println("Connections Ready");
			
					Socket thisSocket =thisServerSocket.accept();
					int socketIndex = count;
					new Thread(new Runnable() {

						public void run() {
							try {

								InputStream in = thisSocket.getInputStream();
							
							
								boolean done=true;
								while(done) {
								if(in.available()!=0) {
								byte[] data=in.readAllBytes();
								Message(string(data));
								Data(data);
								
								done=false;
								}
								}
									
								
								in.close();
								
							} catch (IOException e) {
								System.out.println(socketIndex+" IN FAILED");
							}
							//System.out.println(socketIndex+" IN Disconnected");
							
							
						}
					

						private String string(byte[] bytes) {
							String out ="";
							for(byte b :bytes) {
								out+=(char)b;
							}
									
							return out;
						}
						
					}).start();
					
					//System.out.println(count + " Connected");
					count++;
				}
				thisServerSocket.close();
			} catch (IOException e) {
				System.out.println("SERVER FAILED");
			}
			
			
		}
		
	});
	T.start();
	
	}
	public void stop() {
		
		active=false;
		}
	
	
	public String getRest(File file)  {
		Scanner read;
		String rest = "";
		try {
			read = new Scanner(file);
			
			while(read.hasNextLine()) {
				rest+=read.nextLine()+"\n";
			}
			read.close();
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		
		return rest;
	}
	
	
	

	
	public abstract void Message(String s);
	
	public abstract void Data(byte[] data);
	
	public static String string(byte[] bytes) {
		String out ="";
		for(int i =0;i<bytes.length;i++) {
			if(bytes[i]==-60&&bytes[i+1]==-128) {
				out+=(char)256;
				i++;
			}else {
			out+=(char)bytes[i];
			}
		}
				
		return out;
	}
	public static byte[] unString(String s) {
		 byte[] out =new byte[s.getBytes().length];
		 char[] chars=s.toCharArray();
		for(int i =0;i<chars.length;i++) {
			if(chars[i]==256) {
				out[i]=-60;
				out[i+1]=-128;
				i++;
			}else {
			out[i]=(byte)chars[i];
			}
		}
				
		return out;
	}
	
	public boolean active() {
		
		return this.active;
	}
		public void write(String ip , int port, byte[] buf) throws Exception {
			
			Socket s=new Socket(ip,port);
			OutputStream o = s.getOutputStream();
				o.write(buf);
				s.close();
			
		
		
	
		
	}



		public String myIP() {
			// TODO Auto-generated method stub
			return ip;
		}


		public void restart() {
			stop();
			start();
		}
		public void setPort(int port) {
			this.port = port;
			
		}






	


		
	

	

}
