import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class routerAdd_Popup extends JFrame{

	JTextArea input_Destination;
	JTextArea input_Netmask;
	JTextArea input_Gateway;
	JComboBox<String> selectInterface;
	JLabel lbl_Destination;
	JLabel lbl_Netmask;
	JLabel lbl_Gateway;
	JLabel lbl_Flag;
	JLabel lbl_Interface;
	Container contentPane;
	String[] interfaceName = {"0","1"};
	String interface0 = interfaceName[0];

	JCheckBox flagU;
	JCheckBox flagG;
	JCheckBox flagH;


	public routerAdd_Popup(HashMap<String, Object[] > routerTable, JTextArea routingTable) {

		setTitle("Router Table Entry 추가");
		setSize(450, 350);
		setLocation(1200, 300);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		contentPane = new JPanel();
		((JComponent) contentPane).setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		lbl_Destination = new JLabel("Destination");
		lbl_Destination.setBounds(50, 25, 90, 30);

		input_Destination = new JTextArea();
		input_Destination.setBounds(150, 30, 170, 20);

		contentPane.add(lbl_Destination);
		contentPane.add(input_Destination);

		lbl_Netmask = new JLabel("Netmask");
		lbl_Netmask.setBounds(50, 60, 90, 30);

		input_Netmask = new JTextArea();
		input_Netmask.setBounds(150, 65, 170, 20);

		contentPane.add(lbl_Netmask);
		contentPane.add(input_Netmask);

		lbl_Gateway = new JLabel("Gateway");
		lbl_Gateway.setBounds(50, 95, 90, 30);

		input_Gateway = new JTextArea();
		input_Gateway.setBounds(150, 100, 170, 20);

		contentPane.add(lbl_Gateway);
		contentPane.add(input_Gateway);

		lbl_Flag = new JLabel("Flag");
		lbl_Flag.setBounds(50, 130, 90, 30);
		contentPane.add(lbl_Flag);

		flagU = new JCheckBox("UP");
		flagU.setBounds(150,135,50,20);
		contentPane.add(flagU);

		flagG = new JCheckBox("Gateway");
		flagG.setBounds(205,135,80,20);
		contentPane.add(flagG);

		flagH = new JCheckBox("Host");
		flagH.setBounds(285,135,60,20);
		contentPane.add(flagH);

		lbl_Interface = new JLabel("Interface");
		lbl_Interface.setBounds(50, 165, 90, 30);
		contentPane.add(lbl_Interface);

		selectInterface = new JComboBox<String>(interfaceName);
		selectInterface.setBounds(150, 170, 170, 20);
		selectInterface.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				interface0 = interfaceName[selectInterface.getSelectedIndex()];
			}
		});
		contentPane.add(selectInterface);




		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!input_Destination.getText().equals("") && !input_Netmask.getText().equals("") && !input_Gateway.getText().contentEquals("")) {

					StringTokenizer st = new StringTokenizer(input_Destination.getText(), ".");

					byte[] Destination = new byte[4];
					for (int i = 0; i < 4; i++) {
						String ss = st.nextToken();
						int s = Integer.parseInt(ss);
						Destination[i] = (byte) (s & 0xFF);
					}

					st = new StringTokenizer(input_Netmask.getText(), ".");

					byte[] Netmask = new byte[4];
					for (int i = 0; i < 4; i++) {
						String ss = st.nextToken();
						int s = Integer.parseInt(ss);
						Netmask[i] = (byte) (s & 0xFF);
					}


					st = new StringTokenizer(input_Gateway.getText(), ".");

					byte[] Gateway = new byte[4];
					for (int i = 0; i < 4; i++) {
						String ss = st.nextToken();
						int s = Integer.parseInt(ss);
						Gateway[i] = (byte) (s & 0xFF);
					}


					System.out.println(flagU.isSelected()); //체크여부 알기
					System.out.println(flagG.isSelected());
					System.out.println(flagH.isSelected());
					String interface_Num = interface0;


					/*routing table에 추가하고 업데이트*/


					dispose();
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


