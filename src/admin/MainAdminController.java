package admin;

import java.io.IOException;
import java.util.Date;

import javax.swing.JOptionPane;

public class MainAdminController {

	public static void main(String[] args) {
		
		try {
		    //javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		    javax.swing.UIManager.setLookAndFeel( "javax.swing.plaf.nimbus.NimbusLookAndFeel" );
		} catch (ClassNotFoundException ex) {
		    //Handle Exception
		} catch (InstantiationException ex) {
		    //Handle Exception
		} catch (IllegalAccessException ex) {
		    //Handle Exception
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
		    //Handle Exception
		}
		
		if(System.getProperty("os.name").toLowerCase().indexOf("win") < 0) {
			JOptionPane.showMessageDialog(null, "This software currenty supports windows only.", "Error",JOptionPane.ERROR_MESSAGE);
			//System.exit(-1);
		}
		
		// Process View
		MainFrame.startView();
		
	}
	
	public static boolean startServer() {
		// Process Server
		try {
			Server.createServer(MainFrame.getPort());
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "Please Change the port.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	public static boolean stopServer() {
		
		ClientThread[] clientList = Server.clientList.toArray( new ClientThread[ Server.clientList.size() ] );
		
		for (ClientThread clientThread : clientList) {
			MainAdminController.clientLeft(clientThread);
		}
		
		try {
			Server.stopServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "Server cannot be stopped.\nYou may need to close the software\nto stop theserver\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	public static void clientJoined(ClientThread client) {
		client.logEntry = MainFrame.getStudentLogDataModel().getRowCount();
		MainFrame.getStudentLogDataModel().addRow(new String[]{client.name, client.id, client.clientSocket.getInetAddress().toString(),client.timeJoined.toString(), "Active"});
	}
	
	public static void lockSession() {
		Server.canJoin = false;
	}
	public static void unlockSession() {
		Server.canJoin = true;
	}
	
	public static void allowAskToJoin() {
		Server.canAskToJoin = true;
	}
	public static void notAllowAskToJoin() {
		Server.canAskToJoin = false;
	}
	
	public static void lockAllUSB() {
		Server.blockAllUSB();
	}
	
	public static void unlockAllUSB() {
		Server.unblockAllUSB();
	}
	
	public static void clientLeft(ClientThread client) {
		
		//MainFrame.get
		MainFrame.getStudentLogDataModel().setValueAt("Left - " + new Date().toString(), client.logEntry, 4);
		Server.clientList.remove(client);
		try {
			client.clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.interrupt();
		
	}

}
