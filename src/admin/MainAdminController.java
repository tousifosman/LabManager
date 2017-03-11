package admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import student.MainView;

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
	
	public static void saveAsCSV() {
		
		 
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");
		
		fileChooser.setSelectedFile(new File("Lab Manager Log - " + new Date().toString() + ".csv"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV File Only", "csv", "CSV"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		 
		int userSelection = fileChooser.showSaveDialog(MainFrame.getInstance());
		 
		if (userSelection != JFileChooser.APPROVE_OPTION) {
		    return;
		}
		
		File fileToSave = fileChooser.getSelectedFile();
		try {
			PrintWriter pw = new PrintWriter(fileToSave);
			
			DefaultTableModel tData = MainFrame.getStudentLogDataModel();
			
			for(int i = 0; i < tData.getColumnCount(); i++) {
				pw.print( tData.getColumnName(i) + ", " );
			}
			pw.println();
			
			for (int row = 0; row < tData.getRowCount(); row++) {
				for (int col = 0; col < tData.getColumnCount(); col++) {
					pw.print( tData.getValueAt(row, col) + ", " );
				}
				pw.println();
			}
			
			System.out.println("Successfully file saved at: " + fileToSave.getAbsolutePath());
			
			//MainFrame.getStudentLogDataTable().column
			
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		updateStudentCount();
	}
	
	public static void updateStudentCount() {
		MainFrame.getInstance().lbl_activeStudentCount.setText( Integer.toString( Server.clientList.size() ) );
		MainFrame.getInstance().lbl_totalStudentCount.setText( Integer.toString( MainFrame.getStudentLogDataModel().getRowCount() ) );
	}

}
