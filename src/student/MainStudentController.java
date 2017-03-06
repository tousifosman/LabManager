package student;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.UnexpectedException;

import javax.swing.JOptionPane;

import labManagerProtocal.JoinRefusedException;
import usbStorage.USBStorage;

public class MainStudentController {
	
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
		
		String[] connectedUSBStorage = USBStorage.getUSBStorageList();
		
		if (connectedUSBStorage.length > 0) {
			JOptionPane.showMessageDialog(null, "Remove All USB Storages (Drive: " + String.join(", ", connectedUSBStorage) + ") and run the software again", "Warning!", JOptionPane.WARNING_MESSAGE);
			USBStorage.viewConnectedUSBStorage();
			System.exit(-1);
		}
		
		//if (!SystemTray.isSupported()) {
            //System.out.println("SystemTray is not supported");
            //return;
        //}
		
		// Process View
		MainView.startView();
		
		//MainView.waitUntilReady();
		
		//System.out.println("View ready ...");
		
		
		
		//Process Connection
		
		
	}
	
	public static void requestConnection(String host, int port, String name, String id) {
		
		
		try {
			
			Client.clientStart(host, port, name, id);
			
			MainView.hideView();
			
			JOptionPane.showMessageDialog(MainView.getInstance(), "You have been connected.", "Success", JOptionPane.INFORMATION_MESSAGE);
			
		} catch (UnexpectedException e) {
			// TODO Server malfunctioning
			
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(MainView.getInstance(), "Please Contact with your supervisor. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			
		} catch (UnknownHostException e) {
			// TODO 
			
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(MainView.getInstance(), "Entered host and port is wrong!", "Error", JOptionPane.ERROR_MESSAGE);
			
			
		} catch (JoinRefusedException e) {
			// TODO Request to join has been refused
			
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(MainView.getInstance(), e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(MainView.getInstance(), "No Connection!!\nYour supervisor's software is not running on the specified host and port.", "Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	

}
