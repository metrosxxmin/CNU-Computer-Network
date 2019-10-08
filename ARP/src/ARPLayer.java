import java.util.ArrayList;

public class ARPLayer implements BaseLayer {
	
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	
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
	
	
	
	public boolean Send(byte[] input, int length) {
	
		return false;
	}

	

	public boolean Receive(byte[] input) {
		
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
