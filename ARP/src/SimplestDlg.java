

import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SimplestDlg extends JFrame implements BaseLayer  	{

	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	BaseLayer UnderLayer;
	public int progress_number;

	private static LayerManager m_LayerMgr = new LayerManager();

	private JTextField ChattingWrite;
	//?åå?ùº?†Ñ?Ü° /*ch_test*/
	static JTextField FileNameArea;

	Container contentPane;

	JTextArea ChattingArea;
	JTextArea srcAddress;
	JTextArea dstAddress;
	JComboBox strCombo;

	JLabel choice;
	JLabel lblsrc;
	JLabel lbldst;

	JButton Setting_Button;
	JButton Chat_send_Button;

	static JButton File_search_Button;	
	static JButton File_send_Button;

	FileDialog fdOpen;

	static JProgressBar progress;
	static JComboBox<String> NICComboBox;

	int adapterNumber = 0;
	int index;
	String Text;
	String FileNameText;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

	}


	public SimplestDlg(String pName) throws IOException {
		pLayerName = pName;

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
	@Override
	public BaseLayer GetUnderLayer(int nindex) {
		return null;
	}



}
