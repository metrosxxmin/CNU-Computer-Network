import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.jnetpcap.PcapIf;

public class Second_Popup extends JFrame {
	JTextArea inputProtocol;
	JTextArea inputMac;
	JComboBox<String> selectHost;
	JLabel lbl_Device;
	JLabel lbl_protocol;
	JLabel lbl_mac;
	Container contentPane;
	String[] hostsName = {"Host B","Host C","Host D"};
	String host = "";
	int tableSize = 0;
	
	HashMap<String, Object[] > proxyTable = new HashMap<String, Object[]>();
	
	public Second_Popup() {
		
		setTitle("Proxy ARP Entry 眠啊");
		setSize(450, 350);
		setLocation(1200, 300);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		contentPane = new JPanel();
		((JComponent) contentPane).setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	
		lbl_Device = new JLabel("Device");
		lbl_Device.setBounds(50, 40, 90, 30);
		
		
		selectHost = new JComboBox<String>(hostsName);
		selectHost.setBounds(150, 40, 180, 30);
		selectHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				host = hostsName[selectHost.getSelectedIndex()];
			}
		});
		contentPane.add(lbl_Device);
		contentPane.add(selectHost);
		
		lbl_protocol = new JLabel("IP 林家");
		lbl_protocol.setBounds(50, 90, 90, 30);
		inputProtocol = new JTextArea();
		inputProtocol.setBounds(150, 90, 180, 30);
		inputProtocol.setEnabled(true);
		
		contentPane.add(lbl_protocol);
		contentPane.add(inputProtocol);
		
		lbl_mac = new JLabel("Ethernet 林家");
		lbl_mac.setBounds(50, 140, 90, 30);
		inputMac = new JTextArea();
		inputMac.setBounds(150, 140, 180, 30);
		inputMac.setEnabled(true);
		
		contentPane.add(lbl_mac);
		contentPane.add(inputMac);
	
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!inputProtocol.getText().equals("") && !inputMac.getText().equals("")) {
					String hostName = host;
					
					StringTokenizer st = new StringTokenizer(inputProtocol.getText(), ".");
					byte[] ipAddress = new byte[4];
					for (int i = 0; i < 4; i++) {
						String ss = st.nextToken();
						int s = Integer.parseInt(ss, 16);
						ipAddress[i] = (byte) (s & 0xFF);
					}
					
					st = new StringTokenizer(inputMac.getText(), ":");
					byte[] macAddress = new byte[6];
					for (int i = 0; i < 6; i++) {
						String ss = st.nextToken();
						int s = Integer.parseInt(ss, 16);
						macAddress[i] = (byte) (s & 0xFF);
					}
					Object[] value = new Object[2];
					value[0] = ipAddress;
					value[1] = macAddress;
					
					proxyTable.put(hostName, value);
					tableSize =proxyTable.size();
					if(proxyTable.size()!=tableSize) dispose();
				}

			}
		});
		
		btnOk.setBounds(130, 250, 80, 30);
		getContentPane().add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(220, 250, 80, 30);
		getContentPane().add(btnCancel);
		setVisible(true);
	}
}
