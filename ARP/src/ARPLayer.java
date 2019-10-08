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
		
		
	}
	
	
	
	public boolean Send(byte[] input, int length) {
	
		return false;
	}

	

	public boolean Receive(byte[] input) {
		
		return true;
	}
	
	@Override
	public void SetUnderLayer(BaseLayer pUnderLayer) {
	
	}

	@Override
	public void SetUpperLayer(BaseLayer pUpperLayer) {
		
	}

	@Override
	public String GetLayerName() {
		// TODO Auto-generated method stub
		return pLayerName;
	}

	@Override
	public BaseLayer GetUnderLayer() {
		return  null;
	}

	@Override
	public BaseLayer GetUpperLayer(int nindex) {
		return null;
	}

	@Override
	public void SetUpperUnderLayer(BaseLayer pUULayer) {
		
	}
}
