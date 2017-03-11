package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class ClientThread extends Thread {
	
	public final Socket clientSocket;
	public Scanner reader;
	public PrintWriter writer;
	
	private final Server parentServer;
	
	public final Date timeJoined;
	public String name;
	public String id;
	
	public int logEntry;
	
	public ClientThread(Socket clientSocket, Server parentServer) {
		
		this.clientSocket = clientSocket;
		this.parentServer = parentServer;
		
		this.timeJoined = new Date();
		
		try {
			reader = new Scanner( clientSocket.getInputStream() );
			writer = new PrintWriter( clientSocket.getOutputStream(), true );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.start();
		
	}
	
	public void blockUSB() {
		
		writer.println( labManagerProtocal.Instruction.BlockUSB );
		
	}
	
	public void unblockUSB () {
		
		writer.println( labManagerProtocal.Instruction.UnblockUSB );
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		
		if( reader.nextLine().equals( labManagerProtocal.Instruction.AskToJoin ) ) {
			
			name = reader.nextLine();
			id = reader.nextLine();
			
			if(Server.canJoin == true || Server.canAskToJoin && JOptionPane.showConfirmDialog(null, name + "(" + id  +") is trying to join, will you allow him/her?", "Asked to join", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
				
				writer.println( labManagerProtocal.Instruction.AllowedToJoin );
				
				Server.clientList.add( this );
				MainAdminController.clientJoined(this);
				
			} else {
				writer.println( labManagerProtocal.Instruction.RefusedToJoin );
				return;
			}
		} else {
			writer.println("Invalid Message");
			return;
		}
		
		MainAdminController.updateStudentCount();
		
		if(Server.blockAllUSB) {
			blockUSB();
		}
		
		
		try {
			while(clientSocket.isConnected()) {
				reader.nextLine();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		MainAdminController.clientLeft(this);
		
	}
	

}
