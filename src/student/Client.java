package student;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.UnexpectedException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import labManagerProtocal.JoinRefusedException;
import usbStorage.USBStorage;

public class Client extends Thread {
	
	Socket server;
	String host; 
	int port;
	
//	private static Client instance;
	
	private String name;
	private String id;
	
	static PrintWriter writer;
	static Scanner reader;
	
	public static void clientStart(String host, int port, String name, String id) throws UnexpectedException, UnknownHostException, IOException, JoinRefusedException {
		/*instance =*/ new Client(host, port, name, id);	
	}
	
	private Client(String host, int port, String name, String id) throws UnknownHostException, IOException, UnexpectedException, JoinRefusedException {
		server = new Socket(host, port);
		
		writer = new PrintWriter( server.getOutputStream(), true );
		reader = new Scanner( server.getInputStream() );
		
		this.name = name;
		this.id = id;
		
		writer.println( labManagerProtocal.Instruction.AskToJoin );
		
		String response = reader.nextLine();
		
		if( response.equals( labManagerProtocal.Instruction.RefusedToJoin ) ) {
			
			throw new JoinRefusedException();
			
		} else if ( !response.equals( labManagerProtocal.Instruction.AllowedToJoin ) ) {
			
			throw new UnexpectedException( "Server is not functioning properly." );
			
		}
		
		super.start();
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		writer.println(name);
		writer.println(id);
		
		
		while( server.isConnected() ) {
			try {
				System.out.println("Tried");
				if( !reader.hasNextLine() )
					break;
				
				String request = reader.nextLine();
				
				if(request.equals( labManagerProtocal.Instruction.BlockUSB )) {
					System.out.println("Remove and Block all USB Storage");
					
					USBStorage.forceRemoveAllUSBStorage();
					
					if (USBStorage.getUSBStorageList().length > 0 ) {
						JOptionPane.showMessageDialog(null, "You have USD storage connect.\nRemove all USB storage and try to connect again");
						System.exit(-1);
					}
					
					USBStorage.blockUSBStorage();
					
					//USBStorage.forceRemoveAllUSBStorage();
				} else if (request.equals( labManagerProtocal.Instruction.UnblockUSB )) {
					System.out.println("Restore USB Storage");
					USBStorage.unblockUSBStorage();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Cannot read");
			}	
		}
		
		JOptionPane.showMessageDialog(null, "Thank you for using the the software.", "Thank you", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

}
