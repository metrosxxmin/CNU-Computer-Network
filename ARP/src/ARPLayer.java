import java.util.ArrayList;
import java.util.HashMap;


public class ARPLayer implements BaseLayer {

	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();

	HashMap< byte[], Object[] > cacheTable = new HashMap<byte[], Object[]>();

	_ARP_HEADER m_sHeader = new _ARP_HEADER();

	// inner class for dealing with Mac address
	private class _ARP_MAC_ADDR {
		private byte[] addr = new byte[6];

		public _ARP_MAC_ADDR() {
			this.addr[0] = (byte) 0x00;
			this.addr[1] = (byte) 0x00;
			this.addr[2] = (byte) 0x00;
			this.addr[3] = (byte) 0x00;
			this.addr[4] = (byte) 0x00;
			this.addr[5] = (byte) 0x00;
		}
	}

	// inner class for dealing with Protocol address
	private class _ARP_PROTOCOL_ADDR {
		private byte[] addr = new byte[4];

		public _ARP_PROTOCOL_ADDR() {
			this.addr[0] = (byte) 0x00;
			this.addr[1] = (byte) 0x00;
			this.addr[2] = (byte) 0x00;
			this.addr[3] = (byte) 0x00;
		}
	}

	// inner class for dealing with ARP header
	private class _ARP_HEADER {
		byte[] arp_hwType;
		byte[] arp_protoAddrType;
		byte[] arp_hwAddrLength;
		byte[] arp_protoAddrLength;
		byte[] arp_opcode;
		_ARP_MAC_ADDR _arp_mac_srcaddr;
		_ARP_PROTOCOL_ADDR _arp_protocol_srcaddr;
		_ARP_MAC_ADDR _arp_mac_dstaddr;
		_ARP_PROTOCOL_ADDR _arp_protocol_dstaddr;	// first sending, it's empty.

		public _ARP_HEADER() {
			arp_hwType = new byte[2];
			arp_protoAddrType = new byte[2];
			arp_hwAddrLength = new byte[1];
			arp_protoAddrLength = new byte[1];
			arp_opcode = new byte[2];
			_arp_mac_srcaddr = new _ARP_MAC_ADDR();
			_arp_protocol_srcaddr = new _ARP_PROTOCOL_ADDR();
			_arp_mac_dstaddr = new _ARP_MAC_ADDR();
			_arp_protocol_dstaddr = new _ARP_PROTOCOL_ADDR();
		}
	}

	public void SetIPAddress(byte[] input) {
		// TODO Auto-generated method stub
		m_sHeader._arp_protocol_srcaddr.addr[0] = input[0];
		m_sHeader._arp_protocol_srcaddr.addr[1] = input[1];
		m_sHeader._arp_protocol_srcaddr.addr[2] = input[2];
		m_sHeader._arp_protocol_srcaddr.addr[3] = input[3];

		m_sHeader._arp_protocol_dstaddr.addr[0] = input[4];
		m_sHeader._arp_protocol_dstaddr.addr[1] = input[5];
		m_sHeader._arp_protocol_dstaddr.addr[2] = input[6];
		m_sHeader._arp_protocol_dstaddr.addr[3] = input[7];

	}

	public void SetMacSrcAddress(byte[] srcAddress) {
		// TODO Auto-generated method stub
		m_sHeader._arp_mac_srcaddr.addr[0]= srcAddress[0];
		m_sHeader._arp_mac_srcaddr.addr[1]= srcAddress[1];
		m_sHeader._arp_mac_srcaddr.addr[2]= srcAddress[2];
		m_sHeader._arp_mac_srcaddr.addr[3]= srcAddress[3];
		m_sHeader._arp_mac_srcaddr.addr[4]= srcAddress[4];
		m_sHeader._arp_mac_srcaddr.addr[5]= srcAddress[5];
	}




	public byte[] ObjToByteMessage(_ARP_HEADER Header) {
		byte[] buf = new byte[32];

		buf[0]= m_sHeader.arp_hwType[0];
		buf[1]= m_sHeader.arp_hwType[1];
		buf[2]= m_sHeader.arp_protoAddrType[0];
		buf[3]= m_sHeader.arp_protoAddrType[1];
		buf[4]= m_sHeader.arp_hwAddrLength[0];
		buf[5]= m_sHeader.arp_protoAddrLength[0];
		buf[6]= m_sHeader.arp_opcode[0];
		buf[7]= m_sHeader.arp_opcode[1];

		for (int i = 0; i < 6; i++) buf[8 + i] = m_sHeader._arp_mac_srcaddr.addr[i];
		for (int i = 0; i < 4; i++) buf[14 + i] = m_sHeader._arp_protocol_srcaddr.addr[i];
		for (int i = 0; i < 6; i++) buf[18 + i] = m_sHeader._arp_mac_dstaddr.addr[i];
		for (int i = 0; i < 4; i++) buf[24 + i] = m_sHeader._arp_protocol_dstaddr.addr[i];

		return buf;
	}

	public boolean Send(byte[] input, int length) {

		byte[]dstIP = new byte[4];

		Object[] value = new Object[3];

		dstIP[0] = input[4];
		dstIP[1] = input[5];
		dstIP[2] = input[6];
		dstIP[3] = input[7];

		if(cacheTable.containsKey(dstIP)) {
			//			value = cacheTable.get(dstIP);
			//			if(value[3].equals("Complete")) {
			//				return true;
			//			}
			return true;
		}

		//send 보낸게 IP Layer인데, 상대방의 Mac주소를 모르는 경우 
		value[0] = cacheTable.size();
		value[1] = m_sHeader._arp_mac_dstaddr;
		value[2] = "Incomplete";

		cacheTable.put(dstIP, value);

		m_sHeader.arp_hwType[0] = 0;
		m_sHeader.arp_hwType[1] = 1; 


		m_sHeader.arp_protoAddrType[0]= 0x08;
		m_sHeader.arp_protoAddrType[1]= 0x00;

		m_sHeader.arp_hwAddrLength[0] = 6;

		m_sHeader.arp_protoAddrLength[0] = 4;

		m_sHeader.arp_opcode[0]=0x00;
		m_sHeader.arp_opcode[1]=0x01;

		//src,dst 주소 저장하기
		//ip주소는 IP Layer에서 받고, IP헤더에서 찾는다고 가정
		//src mac주소는 GUI에서 이더넷에게 전달할때 ARP한테도 전달하도록 한다고 가정

		SetIPAddress(input);

		byte[] message = ObjToByteMessage(m_sHeader);

		GetUnderLayer().Send(message, message.length);

		return true;

	}

	public boolean Send(byte[] input) {

		byte[] temp = new byte[10];

		//swapping
		for (int i = 0; i < 10; i++) {
			temp[i] = input[i+8];
			input[i+8] = input[i+18];
			input[i+18] = temp[i];
		}

		input[6]=0x00;
		input[7]=0x02;

		GetUnderLayer().Send(input, input.length);

		return true;
	}

	public boolean Receive(byte[] input) {

		if(input[6]==0x00 && input[7]==0x02) { //Reply Opcode = 2

			byte[]dstIP = new byte[4];
			byte[]dstMac = new byte[6];

			Object[] value = new Object[3];

			dstIP[0] = input[24];
			dstIP[1] = input[25];
			dstIP[2] = input[26];
			dstIP[3] = input[27];

			for(int i=0 ; i<6; i++) dstMac[i] = input[18+i];

			value[0] = cacheTable.get(dstIP)[0];
			value[1] = dstMac;
			value[2] = "Complete";

			cacheTable.replace(dstIP, value);

		}else if(input[6]==0x00 && input[7]==0x01) { //Request Opcode = 1

			byte[]dstIP = new byte[4];
			byte[]dstMac = new byte[6];

			Object[] value = new Object[3];

			dstIP[0] = input[24];
			dstIP[1] = input[25];
			dstIP[2] = input[26];
			dstIP[3] = input[27];

			for(int i=0 ; i<6; i++) dstMac[i] = input[18+i];

			if(cacheTable.containsKey(dstIP)) {
				value[0] = cacheTable.get(dstIP)[0];
			}else {
				value[0] = cacheTable.size();
			}

			value[1] = dstMac;
			value[2] = "Complete";

			//src mac주소 Cache 테이블에 업데이트
			cacheTable.put(dstIP, value);

			for(int i = 0;i<4;i++) {
				if(input[i+24]!= m_sHeader._arp_protocol_srcaddr.addr[i]) {
					//dst protocal add가 본인이 아니면  무시
					return false;
				}
			}

			for (int i = 0; i < 6; i++) {
				input[18 + i] = m_sHeader._arp_mac_srcaddr.addr[i];
			}

			Send(input);
		}
		return true;
	}



	@Override
	public void SetUnderLayer(BaseLayer pUnderLayer) {
		if (pUnderLayer == null)
			return;
		p_UnderLayer = pUnderLayer;
	}

	@Override
	public void SetUpperLayer(BaseLayer pUpperLayer) {
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
		if (p_UnderLayer == null)
			return null;
		return p_UnderLayer;
	}

	@Override
	public BaseLayer GetUpperLayer(int nindex) {
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
