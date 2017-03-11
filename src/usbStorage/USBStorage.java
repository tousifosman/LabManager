package usbStorage;

import java.util.Scanner;
import java.util.LinkedList;
import java.io.IOException;
import java.lang.InterruptedException;


public class USBStorage {
	
	private static Runtime r = Runtime.getRuntime();

	public static void forceRemoveAllUSBStorage() {

		try {

			for (String drivePath : getUSBStorageList() ) {
				r.exec("mountvol " + drivePath + " /D");
			}

		} catch (IOException e) {
			System.out.println("Error: Device cannot be forcefully removed");
		}

	}

	public static String[] getUSBStorageList() {
	
		LinkedList<String> existingUSB = new LinkedList();
	
		try {	
	
			Process p = r.exec("wmic logicaldisk where drivetype=2 get deviceid, volumename, description");
			
			Scanner s = new Scanner( p.getInputStream() );
			
			if(p.waitFor() != 0) {
				System.out.println("Error: USB Storage device letter list cannot be created.");
			} else {
				while(s.hasNext()){
					if( s.hasNext(".:") ){
						existingUSB.add( s.next(".:").trim() );
					}
					s.next();
				}
			}
			s.close();
		} catch(IOException e) {
			System.out.println("Error: Cannot get USB Storage List");
		} catch(InterruptedException e) {
			System.out.println("Error: Cannot get USB Storage List");
		}
		
		return existingUSB.toArray( new String[0] );
		
	}
	

	public static void viewConnectedUSBStorage() {
		try {
			r.exec("RunDll32.exe shell32.dll,Control_RunDLL hotplug.dll");
		} catch (IOException e) {
			System.out.println("Error: Cannot view Connected USB Storage");
		}
	}
	
	public static void blockUSBStorage() throws IOException {
		r.exec("reg add HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\USBSTOR /v start /d 0x4 /t REG_DWORD /f");
	}
	
	public static void unblockUSBStorage() throws IOException {
		r.exec("reg add HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\USBSTOR /v start /d 0x3 /t REG_DWORD /f");
	}

}
