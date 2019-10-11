import java.util.ArrayList;


public class IPLayer implements BaseLayer {
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	
	
	private class _IPLayer_HEADER {
		byte[] ip_versionLen;	// ip version -> IPv4 : 4
		byte[] ip_serviceType;	// type of service
		byte[] ip_packetLen;	// total packet length
		byte[] ip_datagramID;	// datagram id
		byte[] ip_offset;		// fragment offset
		byte[] ip_ttl;			// time to live in gateway hops
		byte[] ip_protocol;		// IP protocol
		byte[] ip_cksum;		// header checksum
		byte[] ip_srcaddr;		// IP address of source
		byte[] ip_dstaddr;		// IP address of destination
		byte[] ip_data;			// data
		
		public _IPLayer_HEADER(){
			this.ip_versionLen = new byte[1];
			this.ip_serviceType = new byte[1];
			this.ip_packetLen = new byte[2];
			this.ip_datagramID = new byte[2];
			this.ip_offset = new byte[2];
			this.ip_ttl = new byte[1];
			this.ip_protocol = new byte[1];
			this.ip_cksum = new byte[2];
			this.ip_srcaddr = new byte[4];
			this.ip_dstaddr= new byte[4];
			this.ip_data = null;		
		}
	}

	 _IPLayer_HEADER m_sHeader = new _IPLayer_HEADER();

	public IPLayer(String pName) {
		// super(pName);
		// TODO Auto-generated constructor stub
		pLayerName = pName;
		m_sHeader = new _IPLayer_HEADER(); 
	}

	public byte[] ObjToByte(_IPLayer_HEADER Header, byte[] input, int length) {
		byte[] buf = new byte[length + 20];

		buf[0] = Header.ip_versionLen[0];
		buf[1] = Header.ip_serviceType[0];
		buf[2] = Header.ip_packetLen[0];
		buf[3] = Header.ip_packetLen[1];
		buf[4] = Header.ip_datagramID[0];
		buf[5] = Header.ip_datagramID[1];
		buf[6] = Header.ip_offset[0];
		buf[7] = Header.ip_offset[1];
		buf[8] = Header.ip_ttl[0];
		buf[9] = Header.ip_protocol[0];
		buf[10] = Header.ip_cksum[0];
		buf[11] = Header.ip_cksum[1];
		for (int i = 0; i < 4; i++) {
			buf[12 + i] = Header.ip_srcaddr[i];
			buf[16 + i] = Header.ip_dstaddr[i];
		}
		for (int i = 0; i < length; i++) {
			buf[20 + i] = input[i];
		}
		return buf;
	}

	public boolean Send(byte[] input, int length) {
		System.out.println("IP 들어옴");
		byte[] bytes = ObjToByte(m_sHeader,input,length);
		
		byte[] opcode = new byte[2];
		opcode[0] = (byte)0x00;
		opcode[1] = (byte)0x01;
		
		((ARPLayer)this.GetUnderLayer()).Send(m_sHeader.ip_srcaddr,m_sHeader.ip_dstaddr,opcode);
		return true;
	}


	public byte[] RemoveCappHeader(byte[] input, int length) {
		
		byte[] remvHeader = new byte[length-20];
		for(int i=0;i<length-20;i++) {
			remvHeader[i] = input[i+20];
		}
		return remvHeader;// 변경하세요 필요하시면
	}

	public synchronized boolean Receive(byte[] input) {
		byte[] data = RemoveCappHeader(input, input.length);
		this.GetUpperLayer(0).Receive(data);
		return true;
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
}
