package admin;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import usbStorage.USBStorage;

public class TestLocker {

	static JFrame mainFrame;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(System.getProperty("os.name").toLowerCase().indexOf("win") < 0) {
			JOptionPane.showMessageDialog(null, "This software currenty supports windows only.", "Error",JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
		//Verify if any USB Storage is connected or not.
		
		String[] connectedUSBStorage = USBStorage.getUSBStorageList();
		
		if (connectedUSBStorage.length > 0) {
			JOptionPane.showMessageDialog(null, "Remove All USB Storages (Drive: " + String.join(", ", connectedUSBStorage) + ") and run the software again", "Warning!", JOptionPane.WARNING_MESSAGE);
			USBStorage.viewConnectedUSBStorage();
			System.exit(-1);
		}
		
		mainFrame = new JFrame("Lab Admin");
		
		final JButton lockButton = new JButton("Lock");
		
		final JButton unlockButton = new JButton("Unlock");
		
		lockButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lockButton.setEnabled(false);
				unlockButton.setEnabled(true);
				
				USBStorage.forceRemoveAllUSBStorage();
				
				try {
					Runtime.getRuntime().exec("reg add HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\USBSTOR /v start /d 0x4 /t REG_DWORD /f");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		
		unlockButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				unlockButton.setEnabled(false);
				lockButton.setEnabled(true);
				
				try {
					Runtime.getRuntime().exec("reg add HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\USBSTOR /v start /d 0x3 /t REG_DWORD /f");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		mainFrame.setLayout(new GridLayout(2, 1));
		
		mainFrame.add(lockButton);
		mainFrame.add(unlockButton);
		
		
		mainFrame.setSize(300, 200);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		

	}

}
