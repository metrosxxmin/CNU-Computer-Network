
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat.Encoding;
import javax.swing.*;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
 
public class ApplicationLayer extends JFrame implements BaseLayer {
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();

	String path;

	private static LayerManager m_LayerMgr = new LayerManager();
	int selected_index;
	private JTextField IPAddressWrite;

	Container contentPane;

	JTextArea TotalArea;

	JButton btnAllDelete;
	JButton btnIPSend;
	JButton btnItemDelete;

	FileDialog fd;
	private JTextField H_WAddressWrite;
	/**
	 * @wbp.nonvisual location=108,504
	 */
	private final JPopupMenu popupMenu = new JPopupMenu();

	public static void main(String[] args) {
		
		m_LayerMgr.AddLayer(new ApplicationLayer("GUI"));
		m_LayerMgr.AddLayer(new TCPLayer("TCP"));
		m_LayerMgr.AddLayer(new IPLayer("IP"));
		m_LayerMgr.AddLayer(new ARPLayer("ARP"));
		m_LayerMgr.AddLayer(new EthernetLayer("Ethernet"));
		m_LayerMgr.AddLayer(new NILayer("NI"));
		m_LayerMgr.ConnectLayers(" NI ( *Ethernet ( *ARP (  *IP ( *TCP ( *GUI ) ) ) ");
	}

	public ApplicationLayer(String pName) {
		
		pLayerName = pName;
		setTitle("Chatting & File Transfer");

		setBounds(250, 250, 987, 477);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = this.getContentPane();
		getContentPane().setLayout(null);
		JPanel ARP_Cache = new JPanel();
		ARP_Cache.setBounds(14, 12, 458, 366);
		ARP_Cache.setBorder(new TitledBorder(null, "ARP Cache", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		ARP_Cache.setLayout(null);
		contentPane.add(ARP_Cache);

		TotalArea = new JTextArea();
		TotalArea.setEditable(false);
		TotalArea.setBounds(14, 24, 430, 227);
		ARP_Cache.add(TotalArea);

		IPAddressWrite = new JTextField();
		IPAddressWrite.setBounds(71, 307, 239, 32);// 249
		ARP_Cache.add(IPAddressWrite);
		IPAddressWrite.setColumns(10);

		btnAllDelete = new JButton("All Delete");// setting

		btnAllDelete.setBounds(240, 263, 165, 35);
		ARP_Cache.add(btnAllDelete);

		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
					NetworkInterface netif = NetworkInterface.getByInetAddress(ip);

					if (netif != null) {
						// get mac address
						byte[] mac = netif.getHardwareAddress();
						ARPLayer.SetMacAddrSrcAddr(mac);
				
					}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		btnIPSend = new JButton("Send");
		btnIPSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (IPAddressWrite.getText() != "") {
					String input = IPAddressWrite.getText();

					TotalArea.append(input + "\t"+"??????????\t incomplete\n");
					//((EthernetLayer) m_LayerMgr.GetLayer("Ethernet")).SetEnetType(type);

					byte[] bytes = input.getBytes();
//					m_LayerMgr.GetLayer("TCP").Send(bytes, bytes.length);
					 p_UnderLayer.Send(bytes, bytes.length);
//					 System.out.println("TCP로 "+input +" Send");
				} 
				else {
					JOptionPane.showMessageDialog(null, "주소 설정 오류");
				}
			}
		});
		btnIPSend.setBounds(324, 307, 107, 32);
		ARP_Cache.add(btnIPSend);

		btnItemDelete = new JButton("Item Delete");

		btnItemDelete.setBounds(35, 263, 165, 35);
		ARP_Cache.add(btnItemDelete);
		
		JLabel lblIp = new JLabel("IP \uC8FC\uC18C");
		lblIp.setBounds(14, 310, 56, 27);
		ARP_Cache.add(lblIp);
		
		JPanel Proxy_Entry = new JPanel();
		Proxy_Entry.setToolTipText("Proxy ARP Entry");
		Proxy_Entry.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Proxy ARP Entry", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		Proxy_Entry.setBounds(486, 12, 466, 273);
		getContentPane().add(Proxy_Entry);
		Proxy_Entry.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(14, 30, 430, 173);
		Proxy_Entry.add(textArea);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				new Second_Popup(); //Add 버튼 눌릴시 두번째 팝업창 열기
				
				String[] selections = {"Host B", "Host C", "Host D"};
//				JOptionPane.showInputDialog(null, "Device ","Proxy ARP Entry 추가", JOptionPane.QUESTION_MESSAGE, null, selections, "Host B");
//				JOptionPane.showInputDialog(null, "IP 주소","Proxy ARP Entry 추가",3);
//				JOptionPane.showInputDialog(null, "Ethernet 주소","Proxy ARP Entry 추가",3);
				
			}
		});
		
		btnAdd.setBounds(42, 215, 165, 35);
		Proxy_Entry.add(btnAdd);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(249, 215, 165, 35);
		Proxy_Entry.add(btnDelete);
		
		JMenu mnNewMenu = new JMenu("New menu");
		mnNewMenu.setBounds(-206, 226, 375, 183);
		Proxy_Entry.add(mnNewMenu);
		
		JButton btnEnd = new JButton("\uC885\uB8CC");  //종료버튼
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEnd.setBounds(307, 383, 165, 35);
		getContentPane().add(btnEnd);
		btnEnd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JButton btnCancel = new JButton("\uCDE8\uC18C");  //취소버튼
		btnCancel.setBounds(492, 383, 165, 35);
		getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JPanel GratuitousARP = new JPanel();
		GratuitousARP.setBorder(new TitledBorder(null, "Gratuitous ARP", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GratuitousARP.setBounds(486, 295, 466, 83);
		getContentPane().add(GratuitousARP);
		GratuitousARP.setLayout(null);
		
		JLabel lblHw = new JLabel("H/W \uC8FC\uC18C");
		lblHw.setBounds(14, 36, 70, 18);
		GratuitousARP.add(lblHw);
		
		H_WAddressWrite = new JTextField();
		H_WAddressWrite.setColumns(10);
		H_WAddressWrite.setBounds(83, 29, 239, 32);
		GratuitousARP.add(H_WAddressWrite);
		
		JButton button_2 = new JButton("Send");
		button_2.setBounds(340, 29, 107, 32);
		GratuitousARP.add(button_2);

		setVisible(true);

		SetCombobox();
	}

	private void SetCombobox() {
		List<PcapIf> m_pAdapterList = new ArrayList<PcapIf>();
		StringBuilder errbuf = new StringBuilder();

		int r = Pcap.findAllDevs(m_pAdapterList, errbuf);
		if (r == Pcap.NOT_OK || m_pAdapterList.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
			return;
		}
//		for (int i = 0; i < m_pAdapterList.size(); i++)
//			this.comboBox.addItem(m_pAdapterList.get(i).getDescription());
	}
	
	
	public boolean Receive(byte[] input) {
		byte[] data = input;
		String Text = new String(data);
		TotalArea.append("[RECV] : " + Text + "\n");
		return false;
	}

	@Override
	public void SetUnderLayer(BaseLayer pUnderLayer) {
		// TODO Auto-generated method stub
		if (pUnderLayer == null)
			return;
		this.p_UnderLayer = pUnderLayer;
	}

	@Override
	public void SetUpperLayer(BaseLayer pUpperLayer) {
		// TODO Auto-generated method stub
		if (pUpperLayer == null)
			return;
		this.p_aUpperLayer.add(nUpperLayerCount++, pUpperLayer);
		// nUpperLayerCount++;
	}

	@Override
	public String GetLayerName() {
		// TODO Auto-generated method stub
		return pLayerName;
	}

	@Override
	public BaseLayer GetUnderLayer() {
		// TODO Auto-generated method stub
		if (p_UnderLayer == null)
			return null;
		return p_UnderLayer;
	}

	@Override
	public BaseLayer GetUpperLayer(int nindex) {
		// TODO Auto-generated method stub
		if (nindex < 0 || nindex > nUpperLayerCount || nUpperLayerCount < 0)
			return null;
		return p_aUpperLayer.get(nindex);
	}

	@Override
	public void SetUpperUnderLayer(BaseLayer pUULayer) {
		this.SetUpperLayer(pUULayer);
		pUULayer.SetUnderLayer(this);

	}
}
