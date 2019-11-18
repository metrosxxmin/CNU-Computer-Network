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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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

	public static boolean exist = false;
	
	String path;
	JTextArea proxyArea;
	
	int selected_index;
	private JTextField IPAddressWrite;

	Container contentPane;

	static JTextArea TotalArea;

	JButton btnAllDelete;
	JButton btnIPSend;
	JButton btnItemDelete;
	JButton Setting_Button;

	JLabel choice;
	static JComboBox<String> NICComboBox;
	JComboBox strCombo;
	int index;

	FileDialog fd;
	private JTextField H_WAddressWrite;

	/**
	 * @wbp.nonvisual location=108,504
	 */
	private final JPopupMenu popupMenu = new JPopupMenu();

	
    public ApplicationLayer (String pName) {
        pLayerName = pName;
    }

	public void App_pop(LayerManager m_LayerMgr) {

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
		
		exist = true;
		
		IPAddressWrite = new JTextField();
		IPAddressWrite.setBounds(71, 307, 239, 32);// 249
		ARP_Cache.add(IPAddressWrite);
		IPAddressWrite.setColumns(10);

		btnAllDelete = new JButton("All Delete");// setting

	      	btnAllDelete.setBounds(240, 263, 165, 35);
	      	ARP_Cache.add(btnAllDelete);

	      	btnAllDelete.addActionListener(new ActionListener() {

	         public void actionPerformed(ActionEvent arg0) {
	            Set key = ((ARPLayer) m_LayerMgr.GetLayer("ARP")).cacheTable.keySet();
	            ArrayList<String> deleteKey = new ArrayList<String>();
	            for(Iterator iterator = key.iterator();iterator.hasNext();) {
	               String keyValue = (String)iterator.next();
	               Object[] value = ((ARPLayer) m_LayerMgr.GetLayer("ARP")).cacheTable.get(keyValue);
	               if(System.currentTimeMillis()-(long)value[3]/100 <= 5) {
	                  try {
	                     Thread.sleep(System.currentTimeMillis()-(long)value[3]);
	                  } catch (InterruptedException e) {
	                     // TODO Auto-generated catch block
	                     e.printStackTrace();
	                  }
	                  deleteKey.add(keyValue);
	               }else deleteKey.add(keyValue);
	            }
	            
	            for(int i=0;i<deleteKey.size();i++) ((ARPLayer) m_LayerMgr.GetLayer("ARP")).cacheTable.remove(deleteKey.get(i));
	            ((ARPLayer) m_LayerMgr.GetLayer("ARP")).updateARPCacheTable();
	         }
	      });

		btnIPSend = new JButton("Send");
		btnIPSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (IPAddressWrite.getText() != "") {

					String input = IPAddressWrite.getText();
					byte[] bytes = input.getBytes();

					String[] ipAddr_st = input.split("\\.");
					byte[] ipAddr_dst = new byte[4];
					for(int i=0;i<4;i++) ipAddr_dst[i] = (byte)Integer.parseInt(ipAddr_st[i]);

					((IPLayer) m_LayerMgr.GetLayer("IP")).SetIPDstAddress(ipAddr_dst);
					
					p_UnderLayer.Send(bytes, bytes.length);

				} 
				else {
					JOptionPane.showMessageDialog(null, "�썒�슣�닔占쎄틬 �뜝�럡�맟�뜝�럩�젧 �뜝�럩沅롳옙紐닷뜝占�");
				}
			}
		});
		btnIPSend.setBounds(324, 307, 107, 32);
		ARP_Cache.add(btnIPSend);

		btnItemDelete = new JButton("Item Delete");

	      btnItemDelete.setBounds(35, 263, 165, 35);
	      ARP_Cache.add(btnItemDelete);

	      btnItemDelete.addActionListener(new ActionListener() {

	         public void actionPerformed(ActionEvent arg0) {
	            String del_ip = JOptionPane.showInputDialog("Item's IP Address");
	            if(del_ip != null) {
	               if(((ARPLayer) m_LayerMgr.GetLayer("ARP")).cacheTable.containsKey(del_ip)) {
	                  Object[] value = ((ARPLayer) m_LayerMgr.GetLayer("ARP")).cacheTable.get(del_ip);
	                  if(System.currentTimeMillis()-(long)value[3]/1000 > 1) { 
	                     ((ARPLayer) m_LayerMgr.GetLayer("ARP")).cacheTable.remove(del_ip);
	                     ((ARPLayer) m_LayerMgr.GetLayer("ARP")).updateARPCacheTable();
	                  }
	               }
	            }
	         }
	      });

		JLabel lblIp = new JLabel("IP \uC8FC\uC18C");
		lblIp.setBounds(14, 310, 56, 27);
		ARP_Cache.add(lblIp);

		JPanel Proxy_Entry = new JPanel();
		Proxy_Entry.setToolTipText("Proxy ARP Entry");
		Proxy_Entry.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Proxy ARP Entry", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		Proxy_Entry.setBounds(486, 12, 466, 273);
		getContentPane().add(Proxy_Entry);
		Proxy_Entry.setLayout(null);

		proxyArea = new JTextArea();
		proxyArea.setEditable(false);
		proxyArea.setBounds(14, 30, 430, 173);
		Proxy_Entry.add(proxyArea);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ARPLayer arpLayer = (ARPLayer) m_LayerMgr.GetLayer("ARP");
				if(arpLayer!=null) 	new Second_Popup(((ARPLayer) m_LayerMgr.GetLayer("ARP")).proxyTable,proxyArea); 

			}
		});

		btnAdd.setBounds(42, 215, 165, 35);
		Proxy_Entry.add(btnAdd);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(249, 215, 165, 35);
		Proxy_Entry.add(btnDelete);
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String del_ip = JOptionPane.showInputDialog("Host's IP Address");
				if(del_ip != null) {
					if(((ARPLayer) m_LayerMgr.GetLayer("ARP")).proxyTable.containsKey(del_ip)) {
						((ARPLayer) m_LayerMgr.GetLayer("ARP")).proxyTable.remove(del_ip);
						
						String printResult ="";
						for(Iterator iterator = ((ARPLayer) m_LayerMgr.GetLayer("ARP")).proxyTable.keySet().iterator(); iterator.hasNext();) {
							String keyIP = (String)iterator.next();
							Object[] obj = ((ARPLayer) m_LayerMgr.GetLayer("ARP")).proxyTable.get(keyIP);
							printResult = printResult+"    "+(String)obj[0]+"\t";
							byte[] mac = (byte[])((ARPLayer) m_LayerMgr.GetLayer("ARP")).proxyTable.get(keyIP)[1];
							String ip_String =keyIP;
							String mac_String ="";
							
							for(int j=0;j<5;j++) mac_String = mac_String + String.format("%X:",mac[j]);
							mac_String = mac_String + String.format("%X",mac[5]);
							
							printResult = printResult+ip_String+"\t    "+mac_String+"\n";
						}
						int proxySize = ((ARPLayer) m_LayerMgr.GetLayer("ARP")).proxyTable.size();
						proxyArea.setText(printResult);
					}
				}
			}
		});

		JMenu mnNewMenu = new JMenu("New menu");
		mnNewMenu.setBounds(-206, 226, 375, 183);
		Proxy_Entry.add(mnNewMenu);

		JButton btnEnd = new JButton("\uC885\uB8CC");  
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEnd.setBounds(307, 383, 165, 35);
		getContentPane().add(btnEnd);
		btnEnd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				exist = false;
				dispose();
			}
		});

		JButton btnCancel = new JButton("\uCDE8\uC18C"); 
		btnCancel.setBounds(492, 383, 165, 35);
		getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				exist = false;
				dispose();
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
		
		button_2.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent arg0) {
	            //////////////////////////////
	            if (H_WAddressWrite.getText() != "") {
	               String input = H_WAddressWrite.getText();
	               StringTokenizer st = new StringTokenizer(input, ":");
	               
	               byte[] hwAddress = new byte[6];
	               for (int i = 0; i < 6; i++) {
	                  String ss = st.nextToken();
	                  int s = Integer.parseInt(ss,16);
	                  hwAddress[i] = (byte) (s & 0xFF);
	               }
	               System.out.println("GARP app send");
	               
	              
	               p_UnderLayer.Send(hwAddress, hwAddress.length,"GARP");
	               
	               String macAddress = String.format("%X:", hwAddress[0]) + String.format("%X:", hwAddress[1])
					+ String.format("%X:", hwAddress[2]) + String.format("%X:", hwAddress[3])
					+ String.format("%X:", hwAddress[4]) + String.format("%X", hwAddress[5]);
			
	               SimplestDlg.serSRCAddr(macAddress);

	            } else {
	               JOptionPane.showMessageDialog(null, "H_W 雅뚯눘�꺖 占쎄퐬占쎌젟 占쎌궎�몴占�");
	            }
	         }
	      });


		setVisible(true);

	}


	public boolean Receive(byte[] input) {
		byte[] data = input;
		return false;
	}



	@Override
	public void SetUnderLayer(BaseLayer pUnderLayer) {
		// TODO Auto-generated method stub
		if (pUnderLayer == null) {
			return;
		}
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
	
	@Override
	public BaseLayer GetUnderLayer(int nindex) {
		return null;
	}
}
