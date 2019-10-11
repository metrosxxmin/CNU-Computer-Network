import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jnetpcap.PcapIf;

public class Second_Popup extends JFrame {
	
	public Second_Popup() {
		setTitle("Proxy ARP Entry Ãß°¡");
		setSize(450, 350);
		setLocation(1200, 300);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		
		JLabel lbl_Device = new JLabel("Device");
		lbl_Device.setBounds(50, 100, 62, 18);
		
		
		JComboBox DeviceComboBox = new JComboBox();
		DeviceComboBox.setBounds(10, 40, 170, 24);

//		List<PcapIf> macList = nlLayer.m_pAdapterList;
//		for (int i = 0; i < macList.size(); i++) {
//			NICComboBox.addItem(macList.get(i).getDescription());
//		}

		DeviceComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				DeviceComboBox.setModel(new DefaultComboBoxModel(new String[] {"Host B","Host C","Host D"}) );
				
//				if (e.getStateChange() == ItemEvent.SELECTED) {
//					JComboBox jbox2 = (JComboBox) e.getItemSelectable();
//					int index = jbox2.getSelectedIndex();
////					nlLayer.SetAdapterNumber(index);
//					try {
//						String autoAddress = "";
//						for (int i = 0; i < macList.get(index).getHardwareAddress().length; i++) {
//							autoAddress += String.format("%02X%s", macList.get(index).getHardwareAddress()[i],
//									(i < autoAddress.length() - 1) ? "" : "");
//							autoAddress += "-";
//						}
//						System.out.println(autoAddress);
////						srcAddress.setText(autoAddress.substring(0, autoAddress.length() - 1));
//						macList.get(index).getHardwareAddress();
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//				}
			}
		});
		
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		btnOk.setBounds(130, 250, 80, 30);
		getContentPane().add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				dispose();
//				System.exit(0);
			}
		});
		btnCancel.setBounds(220, 250, 80, 30);
		getContentPane().add(btnCancel);
		setVisible(true);
	}
}
