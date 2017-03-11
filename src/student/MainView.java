package student;

import java.awt.AWTException;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Component;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import java.awt.Font;

@SuppressWarnings("serial")
public class MainView extends JFrame {

	private JPanel contentPane;
	private JTextField tf_name;
	private JTextField tf_id;
	private JButton btnSubmit;
	private JTextField tf_port;
	
	private static boolean initReady = false;
	
	private static MainView instance;
	private JTextField tf_host;

	/**
	 * Launch the application.
	 */
	public static void startView() {

		new Thread(new Runnable() {
			public void run() {
				try {
					instance = new MainView();
					instance.setVisible(true);
					
					initReady = true;
					
					instance.addIconTray();
					//System.out.println(initReady);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	public static MainView getInstance() {
		return instance;
	}
	
	public static void waitUntilReady() {
		System.out.println(initReady);
		while(!initReady);
		System.out.println(initReady);
	}
	
	public static void hideView() {
		instance.setVisible(false);
	}
	
	public void addIconTray() {
		
		//Im
		
		//Image trayImage = Toolkit.getDefaultToolkit().getImage( getResource("resource/labmanagerLogo-32x32.gif") );
		
		if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "Your OS doesnt have any system tray.\nIf you close the the window you wont be able to recall it.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
        	
        	final SystemTray tray = SystemTray.getSystemTray();
        	
        	//System.out.println( "URl: " + new Object().getClass().getResource("resource/labmanagerLogo-32x32.gif"));
        	
        	Image trayImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resource/labmanagerLogo-32x32.gif"));
        	
        	//final PopupMenu popup = new PopupMenu();
            final TrayIcon trayIcon =  new TrayIcon( trayImage.getScaledInstance(tray.getTrayIconSize().width, tray.getTrayIconSize().height, Image.SCALE_SMOOTH) );
        
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
	public MainView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainView.class.getResource("/resource/labmanagerLogo-256x256.png")));
		setTitle("Lab Manager - Student");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//setUndecorated(true);
		setAlwaysOnTop(true);
		//setVisible(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setFocusable(false);
		contentPane.add(tabbedPane);
		
		JPanel panel_login = new JPanel();
		tabbedPane.addTab("Login", null, panel_login, null);
		panel_login.setLayout(new MigLayout("", "[100%]", "[100%]"));
		
		JPanel panel_input = new JPanel();
		panel_login.add(panel_input, "cell 0 0,alignx center,aligny center");
		panel_input.setLayout(new BoxLayout(panel_input, BoxLayout.Y_AXIS));
		
		JPanel panel_nameWrap = new JPanel();
		panel_nameWrap.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true), "Name", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_input.add(panel_nameWrap);
		panel_nameWrap.setLayout(new BoxLayout(panel_nameWrap, BoxLayout.X_AXIS));
		
		tf_name = new JTextField();
		panel_nameWrap.add(tf_name);
		tf_name.setColumns(20);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		panel_input.add(verticalStrut);
		
		JPanel panel_idWrap = new JPanel();
		panel_idWrap.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true), "ID", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_input.add(panel_idWrap);
		panel_idWrap.setLayout(new BoxLayout(panel_idWrap, BoxLayout.X_AXIS));
		
		tf_id = new JTextField();
		panel_idWrap.add(tf_id);
		tf_id.setColumns(20);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		panel_input.add(verticalStrut_1);
		
		JPanel panel_host = new JPanel();
		panel_host.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true), "Host Address", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_input.add(panel_host);
		panel_host.setLayout(new BoxLayout(panel_host, BoxLayout.X_AXIS));
		
		tf_host = new JTextField();
		tf_host.setText("localhost");
		panel_host.add(tf_host);
		tf_host.setColumns(10);
		
		Component verticalStrut_3 = Box.createVerticalStrut(5);
		panel_input.add(verticalStrut_3);
		
		JPanel panel_port = new JPanel();
		panel_port.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true), "Port", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_input.add(panel_port);
		panel_port.setLayout(new BoxLayout(panel_port, BoxLayout.X_AXIS));
		
		tf_port = new JTextField();
		tf_port.setText("8181");
		panel_port.add(tf_port);
		tf_port.setColumns(10);
		
		Component verticalStrut_2 = Box.createVerticalStrut(10);
		panel_input.add(verticalStrut_2);
		
		JPanel panel_submit = new JPanel();
		panel_submit.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192)), "Connect", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_input.add(panel_submit);
		panel_submit.setLayout(new BorderLayout(0, 0));
		
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainStudentController.requestConnection(tf_host.getText(), Integer.parseInt( tf_port.getText() ), tf_name.getText(), tf_id.getText());
				System.out.println("Server connection requested ...");
			}
		});
		panel_submit.add(btnSubmit);
		
		JPanel panel_info = new JPanel();
		tabbedPane.addTab("Info", null, panel_info, null);
		panel_info.setLayout(new BoxLayout(panel_info, BoxLayout.X_AXIS));
		
		JPanel panel_info_inner = new JPanel();
		panel_info_inner.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true), "Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_info.add(panel_info_inner);
		panel_info_inner.setLayout(new MigLayout("", "[100%]", "[100%]"));
		
		JPanel panel_info_inner2 = new JPanel();
		panel_info_inner.add(panel_info_inner2, "cell 0 0,alignx center,aligny center");
		
		JTextPane txtpnThisIsA = new JTextPane();
		txtpnThisIsA.setContentType("text/html");
		panel_info_inner2.add(txtpnThisIsA);
		txtpnThisIsA.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnThisIsA.setEditable(false);
		txtpnThisIsA.setBackground(new Color(0, 0, 0, 0));
		txtpnThisIsA.setText("This is a project developed by Tousif Osman.<br>\nContact Info: <a href=\"mailto:tousifosman@gmail.com\">tousifosman@gmail.com</a><br>\n<br>\nThank you for using the software.");
	}

}
