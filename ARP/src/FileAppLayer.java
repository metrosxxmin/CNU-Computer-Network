

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;




public class FileAppLayer implements BaseLayer {


	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	private int nu = 0;

	private byte[] fileT = null;

	private int file_totlen;
	private int file_fill ;

	private int progress_number;
	private File file1;
	private FileOutputStream fos;

	private ByteBuffer fileBuf;

	public class  _FAPP_HEADER{

		byte[] fapp_totlen; //사용자가 입력한 문자열의 길이를 저장
		byte[] fapp_type; //단편화에 대한 정보를 담을 수 있다.
		//0x00 – 단편화되지않음, 0x01 – 단편화첫부분, 0x02 – 단편화중간, 0x03- 단 편화마지막		
		byte fapp_msg_type;
		byte ed;
		byte[] fapp_seq_num;
		byte[] fapp_data;

		public _FAPP_HEADER() {
			this.fapp_totlen = new byte[4];
			this.fapp_type = new byte[2];
			this.fapp_msg_type=0x00;
			this.ed = 0x00;
			this.fapp_seq_num=new byte[4];
			this.fapp_data = null;
		}
	}


	_FAPP_HEADER m_sHeader = new _FAPP_HEADER();

	public FileAppLayer(String pName) { 
		// super(pName);
		// TODO Auto-generated constructor stub
		pLayerName = pName; 

	}

	public byte[] ObjToByte(_FAPP_HEADER Header, byte[] input, int length) {
		byte[] buf = new byte[length + 12];
		buf[0] = Header.fapp_totlen[0];
		buf[1] = Header.fapp_totlen[1];
		buf[2] = Header.fapp_totlen[2];
		buf[3] = Header.fapp_totlen[3];
		buf[4] = Header.fapp_type[0];
		buf[5] = Header.fapp_type[1];
		buf[6] = Header.fapp_msg_type;
		buf[7] = Header.ed;
		buf[8] = Header.fapp_seq_num[0];
		buf[9] = Header.fapp_seq_num[1];
		buf[10] = Header.fapp_seq_num[2];
		buf[11] = Header.fapp_seq_num[3];

		for (int i = 0; i < length; i++) {
			buf[12 + i] = input[i];

		}
		return buf;
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
	public void SetUpperUnderLayer(BaseLayer pUULayer) {
		this.SetUpperLayer(pUULayer);
		pUULayer.SetUnderLayer(this);
	}
	@Override
	public BaseLayer GetUnderLayer(int nindex) {
		return null;
	}

}
