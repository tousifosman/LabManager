package admin;

import java.awt.AWTException;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;

import net.miginfocom.swing.MigLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import java.awt.SystemColor;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.SpringLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel studentLogDataModel;
	private JTextField textField_port;
	
	//private static Thread mainThread;
	
	private static MainFrame instance;
	private JToggleButton tbtn_startServer;
	private JToggleButton tbtn_sessionLock;
	private JToggleButton tbtn_usbLock;
	private JButton btn_csvSave;
	private JButton btn_exit;
	private JToggleButton tbtn_askToJoin;
	public JLabel lbl_activeStudentCount;
	public JLabel lbl_totalStudentCount;
	

	public static void startView() {
		
		//UIManager.put("TabbedPane.selected", Color.WHITE);
		
		//mainThread =  new Thread(new Runnable() {
			//public void run() {
				try {
					
					instance = new MainFrame();
					instance.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			//}
		//});
		//mainThread.start();
		//mainThread.
				instance.addIconTray();
				
	}
	
	public static MainFrame getInstance() {
		return instance;
	}
	
	public static void toggleView() {
		if (instance.isVisible()) {
			instance.setVisible(false);
		} else {
			instance.setVisible(true);
		}
	}
	
	public static int getPort() {
		return Integer.parseInt( instance.textField_port.getText() );
	}
	
	public static DefaultTableModel getStudentLogDataModel() {
		return instance.studentLogDataModel;
	}
	
	public static JTable getStudentLogDataTable() {
		return instance.table;
	}
	
	public void addIconTray() {
		if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "Your OS doesnt have any system tray.\nIf you close the the window you wont be able to recall it.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
        	
        	final SystemTray tray = SystemTray.getSystemTray();
        	
        	Image trayImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resource/labmanagerLogo-32x32.gif"));
        	
        	
        	final PopupMenu popup = new PopupMenu();
            final TrayIcon trayIcon =  new TrayIcon( trayImage.getScaledInstance(tray.getTrayIconSize().width, tray.getTrayIconSize().height, Image.SCALE_SMOOTH) );
            
            // Create a pop-up menu components
            MenuItem showItem = new MenuItem("Show");
            
            showItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					MainFrame.getInstance().setVisible(true);
					
				}
			});
            
            //Add components to pop-up menu
            popup.add(showItem);
            
            trayIcon.setPopupMenu(popup);
            
            trayIcon.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					MainFrame.toggleView();
				}
				
			});
            
            try {
                tray.add(trayIcon);
                
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
            }
        	
        }
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/resource/labmanagerLogo-256x256.png")));
		setBackground(SystemColor.controlHighlight);
		setTitle("Lab Manager - Teacher");
		setBounds(100, 100, 1200, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setFont(new Font("Dialog", Font.BOLD, 13));
		
		tabbedPane.setFocusable(false);
		
		
		contentPane.add(tabbedPane);
		
		JPanel panel_action = new JPanel();
		panel_action.setBorder(new EmptyBorder(10, 10, 10, 10));
		tabbedPane.addTab("Manage", null, panel_action, null);
		panel_action.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_studentList = new JPanel();
		panel_studentList.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true), "Student Log", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		panel_action.add(panel_studentList, BorderLayout.CENTER);
		panel_studentList.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_studentListInner = new JPanel();
		panel_studentListInner.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel_studentList.add(panel_studentListInner, BorderLayout.CENTER);
		panel_studentListInner.setLayout(new BoxLayout(panel_studentListInner, BoxLayout.X_AXIS));
		
		table = new JTable();
		table.setShowVerticalLines(false);
		
		studentLogDataModel = new DefaultTableModel(
				new Object[0][],
				new String[] {
						"Student Name", "Student ID", "Student IP", "Time Joined", "Status"
				}
		) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
					String.class, String.class, String.class, String.class, String.class
			};
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
		
		table.setModel(studentLogDataModel);
		JScrollPane scrollPane = new JScrollPane(  table );
		panel_studentListInner.add(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel panel_studentInfo = new JPanel();
		panel_studentList.add(panel_studentInfo, BorderLayout.SOUTH);
		panel_studentInfo.setLayout(new BoxLayout(panel_studentInfo, BoxLayout.X_AXIS));
		
		JPanel panel_activeStudentCount = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_activeStudentCount.getLayout();
		flowLayout.setHgap(15);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_studentInfo.add(panel_activeStudentCount);
		
		JLabel lbl_activeStudent = new JLabel("Active Students:");
		panel_activeStudentCount.add(lbl_activeStudent);
		
		lbl_activeStudentCount = new JLabel("0");
		lbl_activeStudent.setLabelFor(lbl_activeStudentCount);
		panel_activeStudentCount.add(lbl_activeStudentCount);
		
		JPanel panel_totalStudentCount = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_totalStudentCount.getLayout();
		flowLayout_1.setHgap(10);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_studentInfo.add(panel_totalStudentCount);
		
		JLabel lbl_totalStudent = new JLabel("Total Students:");
		panel_totalStudentCount.add(lbl_totalStudent);
		
		lbl_totalStudentCount = new JLabel("0");
		lbl_totalStudent.setLabelFor(lbl_totalStudentCount);
		panel_totalStudentCount.add(lbl_totalStudentCount);
		
		JPanel panel_actionButtons = new JPanel();
		panel_actionButtons.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true), "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_action.add(panel_actionButtons, BorderLayout.EAST);
		panel_actionButtons.setLayout(new BoxLayout(panel_actionButtons, BoxLayout.X_AXIS));
		
		JPanel panel_innerButtonContainer = new JPanel();
		panel_actionButtons.add(panel_innerButtonContainer);
		panel_innerButtonContainer.setLayout(new GridLayout(7, 1, 0, 5));
		
		tbtn_sessionLock = new JToggleButton("Session Lock");
		tbtn_sessionLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				if (tbtn_sessionLock.isSelected()) {
					System.out.println("Lock Session");
					MainAdminController.lockSession();
				} else {
					System.out.println("Unlock Session");
					MainAdminController.unlockSession();
				}
				
			}
		});
		tbtn_sessionLock.setEnabled(false);
		panel_innerButtonContainer.add(tbtn_sessionLock);
		
		tbtn_usbLock = new JToggleButton("Lock All USB Storage");
		tbtn_usbLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				if(tbtn_usbLock.isSelected()) {
					System.out.println("Lock All USB...");
					MainAdminController.lockAllUSB();
					
					
					tbtn_usbLock.setText("Unlock All USB Storage");
				} else {
					System.out.println("Unlock All USB...");
					MainAdminController.unlockAllUSB();
					
					tbtn_usbLock.setText("Lock All USB Storage");
				}
				
			}
		});
		
		tbtn_askToJoin = new JToggleButton("Ask To Join Not Allowed");
		tbtn_askToJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(tbtn_askToJoin.isSelected()) {
					MainAdminController.allowAskToJoin();
				} else {
					MainAdminController.notAllowAskToJoin();
				}
			}
		});
		tbtn_askToJoin.setEnabled(false);
		panel_innerButtonContainer.add(tbtn_askToJoin);
		tbtn_usbLock.setEnabled(false);
		panel_innerButtonContainer.add(tbtn_usbLock);
		
		Component rigidArea = Box.createRigidArea(new Dimension(200, 20));
		panel_innerButtonContainer.add(rigidArea);
		
		btn_exit = new JButton("Exit");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(JOptionPane.showConfirmDialog(MainFrame.instance, "Are you sure you want to close the program ?", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		panel_innerButtonContainer.add(btn_exit);
		btn_exit.setToolTipText("Disable students to add to server");
		
		tbtn_startServer = new JToggleButton("Start Server");
		tbtn_startServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(tbtn_startServer.isSelected()) {
					
					if (!MainAdminController.startServer()) {
						tbtn_startServer.setSelected(false);
						return;
					}
					
					tbtn_startServer.setText("Server Running ...");
					tbtn_startServer.setBackground(new Color(68, 158, 82));
					btn_exit.setEnabled(false);
					
					tbtn_sessionLock.setEnabled(true);
					tbtn_askToJoin.setEnabled(true);
					tbtn_usbLock.setEnabled(true);
					//btn_csvSave.setEnabled(true);
					
					
				} else if (JOptionPane.showConfirmDialog(null, "Are you sure you want to stop the server?\nAll Connected students will be disconnected", "Are you sure?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					
					if (!MainAdminController.stopServer()) {
						tbtn_startServer.setSelected(true);
						return;
					}
					
					tbtn_startServer.setText("Start Server");
					tbtn_startServer.setBackground(null);
					btn_exit.setEnabled(true);
					

					tbtn_sessionLock.setSelected(false);
					tbtn_askToJoin.setSelected(false);
					tbtn_usbLock.setSelected(false);
					
					tbtn_sessionLock.setEnabled(false);
					tbtn_askToJoin.setEnabled(false);
					tbtn_usbLock.setEnabled(false);
					//btn_csvSave.setEnabled(false);
					
				} else {
					tbtn_startServer.setSelected(true);
				}
			}
		});
		
		btn_csvSave = new JButton("Save as CSV");
		btn_csvSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainAdminController.saveAsCSV();
			}
		});
		panel_innerButtonContainer.add(btn_csvSave);
		
		panel_innerButtonContainer.add(tbtn_startServer);
		
		JPanel panel_settings = new JPanel();
		tabbedPane.addTab("Settings", null, panel_settings, null);
		panel_settings.setLayout(new BoxLayout(panel_settings, BoxLayout.Y_AXIS));
		
		JPanel panel_inner = new JPanel();
		panel_settings.add(panel_inner);
		panel_inner.setLayout(new MigLayout("", "[100%]", "[100%]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true), "Port", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		panel_inner.add(panel, "cell 0 0,alignx center,aligny center");
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		textField_port = new JTextField();
		textField_port.addKeyListener(new KeyAdapter() {
			String tempPort;
			@Override
			public void keyPressed(KeyEvent e) {
				tempPort = textField_port.getText();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if ( !textField_port.getText().matches("[0-9]{0,6}") ) {
					JOptionPane.showMessageDialog(instance, "Port Number can only be a 6 digit positive number", "Error", JOptionPane.ERROR_MESSAGE);
					textField_port.setText(tempPort);
				}
			}
		});
		
		panel.add(textField_port);
		textField_port.setText("8181");
		textField_port.setColumns(10);
		
		JPanel panel_info = new JPanel();
		panel_info.setBorder(new EmptyBorder(10, 10, 10, 10));
		tabbedPane.addTab("Info", null, panel_info, null);
		panel_info.setLayout(new BoxLayout(panel_info, BoxLayout.X_AXIS));
		
		JPanel panel_info_inner = new JPanel();
		panel_info_inner.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192)), "Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_info.add(panel_info_inner);
		panel_info_inner.setLayout(new MigLayout("", "[100%]", "[100%]"));
		
		JPanel panel_inner2 = new JPanel();
		panel_info_inner.add(panel_inner2, "cell 0 0,alignx center,aligny center");
		
		JTextPane txtpn_info = new JTextPane();
		txtpn_info.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpn_info.setEditable(false);
		panel_inner2.add(txtpn_info);
		txtpn_info.setBackground(new Color(0, 0, 0, 0));
		txtpn_info.setContentType("text/html");
		txtpn_info.setText("This is a project developed by Tousif Osman.<br>\nContact Info: <a href=\"mailto:tousifosman@gmail.com\">tousifosman@gmail.com</a><br>\n<br>\nThank you for using the software.");
	}
}
