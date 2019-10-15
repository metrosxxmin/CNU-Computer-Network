import java.util.ArrayList;

public class TCPLayer implements BaseLayer {
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	public static int upperLayer_num ;
	//    TCP Header의 크기 20byte

	private class _TCPLayer_HEADER {
		byte[] tcp_sport; // source port
		byte[] tcp_dport; // destination port
		byte[] tcp_seq; // sequence number
		byte[] tcp_ack; // acknowledged sequence
		byte[] tcp_offset; // no use
		byte[] tcp_flag; // control flag
		byte[] tcp_window; // no use
		byte[] tcp_cksum; // check sum
		byte[] tcp_urgptr; // no use
		byte[] Padding;
		byte[] tcp_data; // data part

		public _TCPLayer_HEADER() {
			this.tcp_sport = new byte[2];
			this.tcp_dport = new byte[2];
			this.tcp_seq = new byte[4];
			this.tcp_ack = new byte[4];
			this.tcp_offset = new byte[1];
			this.tcp_flag = new byte[1];
			this.tcp_window = new byte[2];
			this.tcp_cksum = new byte[2];
			this.tcp_urgptr = new byte[2];
			this.Padding = new byte[4];
			this.tcp_data = null;
		}
	}

	_TCPLayer_HEADER m_sHeader = new _TCPLayer_HEADER();

	public TCPLayer(String pName) {
		pLayerName = pName;
		m_sHeader = new _TCPLayer_HEADER();
	}

	public byte[] objToByte(_TCPLayer_HEADER Header,byte[] input,int length){
		byte[] buf = new byte[length+24];

		buf[0]=Header.tcp_sport[0];
		buf[1]=Header.tcp_sport[1];
		buf[2]=Header.tcp_dport[0];
		buf[3]=Header.tcp_dport[1];
		buf[4]=Header.tcp_seq[0];
		buf[5]=Header.tcp_seq[1];
		buf[6]=Header.tcp_seq[2];
		buf[7]=Header.tcp_seq[3];
		buf[8]=Header.tcp_ack[0];
		buf[9]=Header.tcp_ack[1];
		buf[10]=Header.tcp_ack[2];
		buf[11]=Header.tcp_ack[3];
		buf[12]=Header.tcp_offset[0];
		buf[13]=Header.tcp_flag[0];
		buf[14]=Header.tcp_window[0];
		buf[15]=Header.tcp_window[1];
		buf[16]=Header.tcp_cksum[0];
		buf[17]=Header.tcp_cksum[1];
		buf[18]=Header.tcp_urgptr[0];
		buf[19]=Header.tcp_urgptr[1];
		buf[20]=Header.Padding[0];
		buf[21]=Header.Padding[1];
		buf[22]=Header.Padding[2];
		buf[23]=Header.Padding[3];

		for(int i=0;i<length;i++){
			buf[24+i]=input[i];
		}

		return buf;

	}

	public synchronized boolean Send(byte[] input, int length) {
		
		
		m_sHeader.tcp_sport[0]=(byte)0x00;
		m_sHeader.tcp_sport[1]=(byte)0x00;	
		m_sHeader.tcp_dport[0]=(byte)0x00;
		m_sHeader.tcp_dport[1]=(byte)0x00;	
		
		byte[] data = objToByte(m_sHeader,input,length);

		this.GetUnderLayer().Send(data,length+24);
		return true;

	}

	public boolean Send(byte[] input, int length, Object ob) {

		//		 상위계층의종류에따라서헤더에상위프로토콜형태저장후물리적계층으로 Ethernet frame 전달(enet_type) 
		//		§ 0x2080: ChattingAppLayer 
		//		§ 0x2090: FileAppLayer 

		m_sHeader.tcp_data=input;

		if(ob == this.GetUpperLayer(0).GetLayerName()) {

			m_sHeader.tcp_sport[0]=(byte)0x20;
			m_sHeader.tcp_sport[1]=(byte)0x80;	
			m_sHeader.tcp_dport[0]=(byte)0x20;
			m_sHeader.tcp_dport[1]=(byte)0x80;	

			byte[] frame = objToByte(m_sHeader,input,length);
			
			
			GetUnderLayer().Send(frame,length+24);

		}else if(ob == this.GetUpperLayer(1).GetLayerName()){

			m_sHeader.tcp_sport[0]=(byte)0x20;
			m_sHeader.tcp_sport[1]=(byte)0x90;	
			m_sHeader.tcp_dport[0]=(byte)0x20;
			m_sHeader.tcp_dport[1]=(byte)0x90;	

			byte[] stream = objToByte(m_sHeader,input,length);
			GetUnderLayer().Send(stream,length+24);

		}

		return true;

	}

	public boolean Receive() {
		p_UnderLayer.Receive();
		return true;
	}

	public boolean Receive(byte[] input) {

		byte[] data;


		if(input[2]==(byte)0x20 && input[3]==(byte)0x80) {//ChatData수신
			data = RemoveCappHeader(input, input.length);

			this.GetUpperLayer(0).Receive(data);
			return true;


		}else if(input[2]==(byte)0x20 && input[3]==(byte)0x90) {//FileData수신

			data = RemoveCappHeader(input, input.length);
		
			this.GetUpperLayer(1).Receive(data);
			return true;
		}

		return false;
	}

	public byte[] RemoveCappHeader(byte[] input, int length) {

		byte[] rebuf = new byte[length-24];

		for (int i = 0; i < length-24; i++) {

			rebuf[i] = input[24 + i];
		}
		return rebuf;
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
