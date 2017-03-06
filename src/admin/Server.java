package admin;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Server extends Thread {
	
	private ServerSocket serverSocket;
	public final int port;
	
	public static boolean canJoin;
	public static boolean canRun;
	public static boolean canAskToJoin;
	private static Server instance;
	
	final static LinkedList<ClientThread> clientList = new LinkedList<>();
	
	public Server(int port) throws IOException {
		
		this.port = port;
		Server.canJoin = true;
		
		serverSocket = new ServerSocket(port);
		
		super.start();
		
	}
	
	@Override
	public void run() {
		
		super.run();
		
		canRun = true;
		
		while(canRun) {
		
			try {
				
				new ClientThread( serverSocket.accept(), this );
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
		}
		
	}
	
	public static void blockAllUSB() {
		for (ClientThread client : clientList) {
			client.blockUSB();
		}
	}
	
	public static void unblockAllUSB() {
		for (ClientThread client : clientList) {
			client.unblockUSB();
		}
	}
	
	public static void startSession() {
		canJoin = true;
	}
	
	public static void stopSession() {
		canJoin = false;
	}
	
	public static void createServer(int port) throws IOException {
		instance = new Server(port);
	}
	
	public static Server getInstance() {
		return instance;
	}
	
	public static void stopServer() throws IOException {
		canRun = false;
		instance.interrupt();
		
		instance.serverSocket.close();
	}
	

}
