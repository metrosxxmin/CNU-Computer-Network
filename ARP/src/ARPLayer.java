import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;


public class ARPLayer implements BaseLayer {
   public int nUpperLayerCount = 0;
   public String pLayerName = null;
   public BaseLayer p_UnderLayer = null;
   public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();

   HashMap<String, Object[] > cacheTable = new HashMap<String, Object[]>();
   HashMap<String, Object[]> proxyTable = new HashMap<String, Object[]>();

   private static byte[] arp_mac_srcaddr = null;
   private byte[] arp_mac_dstaddr = null;
   Cache_Timeout thread =null;

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

   private class _ARP_HEADER {
      byte[] arp_hwType;
      byte[] arp_protoAddrType;
      byte[] arp_hwAddrLength;
      byte[] arp_protoAddrLength;
      byte[] arp_opcode;
      _ARP_MAC_ADDR _arp_mac_srcaddr;
      _ARP_PROTOCOL_ADDR _arp_protocol_srcaddr;
      _ARP_MAC_ADDR _arp_mac_dstaddr;
      _ARP_PROTOCOL_ADDR _arp_protocol_dstaddr;   // first sending, it's empty.

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

   _ARP_HEADER m_sHeader = new _ARP_HEADER();

   public ARPLayer(String pName) {
      // super(pName);
      // TODO Auto-generated constructor stub
      pLayerName = pName;
      //m_sHeader = new _ARP_HEADER();
      if(thread==null) {
    	  thread = new Cache_Timeout(this.cacheTable,1,5);
    	  Thread obj = new Thread(thread);
    	  obj.start();
      }
      
   }

   public byte[] ObjToByte(_ARP_HEADER m_sHeader) {
      byte[] buf = new byte[28];

      buf[0] = m_sHeader.arp_hwType[0];
      buf[1] = m_sHeader.arp_hwType[1];
      buf[2] = m_sHeader.arp_protoAddrType[0];
      buf[3] = m_sHeader.arp_protoAddrType[1];
      buf[4] = m_sHeader.arp_hwAddrLength[0];
      buf[5] = m_sHeader.arp_protoAddrLength[0];
      buf[6] = m_sHeader.arp_opcode[0];
      buf[7] = m_sHeader.arp_opcode[1];
      for(int i= 0;i<6;i++) {
         buf[i+8]=m_sHeader._arp_mac_srcaddr.addr[i];
         buf[i+18] = m_sHeader._arp_mac_dstaddr.addr[i];
      }
      for(int i=0;i<4;i++) {
         buf[i+14] = m_sHeader._arp_protocol_srcaddr.addr[i];
         buf[i+24] = m_sHeader._arp_protocol_dstaddr.addr[i];
      }

      return buf;
   }


   /*
    * IP Layer에서 호출되는 Send일 경우, opcode는  1이며, ARP Layer에서 호출되는 Send일 경우 opcode는 2다.
    */
   public boolean Send(byte[] arp_protocol_srcaddr, byte[] arp_protocol_dstaddr, byte[] arp_opcode) {

      Object[] value = new Object[4];
      String ipAddressToString = (arp_protocol_dstaddr[0]&0xFF) +"."+(arp_protocol_dstaddr[1]&0xFF) +"."+(arp_protocol_dstaddr[2]&0xFF) +"."+(arp_protocol_dstaddr[3]&0xFF);

      if(arp_opcode[0]==(byte)0x00 && arp_opcode[1]==(byte)0x01) { 

         if(cacheTable.containsKey(ipAddressToString)) return true; 

         value[0]= cacheTable.size();         // ARP-request Send ("Incomplete")
         value[1]= m_sHeader._arp_mac_dstaddr;
         value[2]="Incomplete";
         value[3] = System.currentTimeMillis();

         cacheTable.put(ipAddressToString, value);

      }

      m_sHeader.arp_hwType[0] =(byte) 0x00; //Ethernet
      m_sHeader.arp_hwType[1] =(byte) 0x01;

      m_sHeader.arp_protoAddrType[0] =(byte) 0x08; //IP
      m_sHeader.arp_protoAddrType[1] =(byte) 0x00;

      m_sHeader.arp_hwAddrLength[0]=6;
      m_sHeader.arp_protoAddrLength[0]=4;

      m_sHeader._arp_mac_srcaddr.addr = arp_mac_srcaddr;
      m_sHeader._arp_protocol_srcaddr.addr = arp_protocol_srcaddr;
      if(arp_mac_dstaddr!=null) m_sHeader._arp_mac_dstaddr.addr = arp_mac_dstaddr;
      m_sHeader._arp_protocol_dstaddr.addr=arp_protocol_dstaddr;

      m_sHeader.arp_opcode = arp_opcode;

      byte[] bytes = ObjToByte(m_sHeader);   

      updateARPCacheTable();
      (this.GetUnderLayer()).Send(bytes,bytes.length);

      return true;
   }



   public boolean Receive(byte[] input) {
      byte[] message = input;

      Object[] value = new Object[4];
      byte[] dstIP = new byte[4];
      byte[] dstMac = new byte[6];
      byte[] targetIP = new byte[4];


      System.arraycopy(message, 14, dstIP, 0, 4); 
      System.arraycopy(message, 8, dstMac, 0, 6);
      System.arraycopy(message, 24, targetIP, 0, 4);

      String ipAddressToString = (dstIP[0]&0xFF) +"."+(dstIP[1]&0xFF) +"."+(dstIP[2]&0xFF) +"."+(dstIP[3]&0xFF);
      String targetIpAddressToString = (targetIP[0]&0xFF) +"."+(targetIP[1]&0xFF) +"."+(targetIP[2]&0xFF) +"."+(targetIP[3]&0xFF);


      if(message[6]==(byte)0x00 && message[7] ==(byte)0x01) { // ARP-request Receive ("Complete")

         if(!cacheTable.containsKey(ipAddressToString)) {

            value[0]=cacheTable.size();
            value[1]= dstMac;
            value[2]= "Complete";
            value[3] = System.currentTimeMillis();

            cacheTable.put(ipAddressToString, value);
            updateARPCacheTable();
         }else {
        	 value[0]=cacheTable.get(ipAddressToString)[0];
             value[1]= cacheTable.get(ipAddressToString)[1];
             value[2]= cacheTable.get(ipAddressToString)[2];
             value[3] = System.currentTimeMillis();
             
             cacheTable.put(ipAddressToString, value);
         }

         byte[] newOp = new byte[2];
         newOp[0] =(byte) 0x00;
         newOp[1] =(byte) 0x02;
         
         SetMacAddrDstAddr(dstMac);

         if(!proxyTable.containsKey(targetIpAddressToString)) {
            for(int i=0;i<4;i++) {
               if(message[i+24] != m_sHeader._arp_protocol_srcaddr.addr[i]) return false;
            }
            Send(m_sHeader._arp_protocol_srcaddr.addr, dstMac, newOp);
            
         }else Send(targetIP, dstMac, newOp);
         

      }else if(message[6]==(byte)0x00 && message[7] ==(byte)0x02) { //ARP-reply Receive ("Incomplete" -> "Complete")
         value[0]= cacheTable.get(ipAddressToString)[0];
         value[1]= dstMac;
         value[2]="Complete";
         value[3] = System.currentTimeMillis();
         cacheTable.replace(ipAddressToString, value);

         updateARPCacheTable();
      }
      return false;
   }

   public void updateARPCacheTable() {

      Set keyS = cacheTable.keySet();
      ApplicationLayer.TotalArea.setText("");

      for (Iterator iterator = keyS.iterator(); iterator.hasNext();) {
         String key = (String)iterator.next();
         Object[] value = (Object[]) cacheTable.get(key);

         if(value[2].equals("Incomplete")) {
            ApplicationLayer.TotalArea.append("       "+key + "\t"+"??????????????\t incomplete\n");      
         }else {
            byte[] maxAddr = (byte[])value[1];
            String macAddress = String.format("%X:", maxAddr[0])+ String.format("%X:", maxAddr[1])+ String.format("%X:", maxAddr[2])+ String.format("%X:", maxAddr[3])+ String.format("%X:", maxAddr[4])+ String.format("%X", maxAddr[5]);
            ApplicationLayer.TotalArea.append("       "+key  + "\t"+ macAddress+"\t complete\n");   
         }

      }

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
   }

   @Override
   public void SetUpperUnderLayer(BaseLayer pUULayer) {
      this.SetUpperLayer(pUULayer);
      pUULayer.SetUnderLayer(this);
   }

   public void SetMacAddrSrcAddr(byte[] srcaddr) {
      arp_mac_srcaddr =srcaddr;
   }
   public void SetMacAddrDstAddr(byte[] dstaddr) {
      this.arp_mac_dstaddr=dstaddr;
   }

	public void SetIPAddrSrcAddr(byte[] srcaddr) {
		m_sHeader._arp_protocol_srcaddr.addr = srcaddr;
	}

	class Cache_Timeout implements Runnable {
		HashMap<String, Object[]> cacheTable;
		int incompleteTimeLimit;
		int completeTimeLimit;

		public Cache_Timeout(HashMap<String, Object[]> cacheTable, int incompleteTimeLimit, int completeTimeLimit) {
			this.cacheTable = cacheTable;
			this.incompleteTimeLimit = incompleteTimeLimit;
			this.completeTimeLimit = completeTimeLimit;
		}

		@Override
		public void run() {
			while (true) {
				Set keyS = this.cacheTable.keySet();
				for (Iterator iterator = keyS.iterator(); iterator.hasNext();) {
			        String key=null; 
					if((key=(String)iterator.next())!=null) {
				         Object[] value = (Object[]) this.cacheTable.get(key);
				         if(value[2].equals("Incomplete")) {
				        	 if((System.currentTimeMillis() - (long)value[3])/60000 >=incompleteTimeLimit) {
					        	 this.cacheTable.remove(key, value);
					        	 updateARPCacheTable();
					         }
				         }else {
				        	 if((System.currentTimeMillis() - (long)value[3])/60000 >=completeTimeLimit) {
					        	 this.cacheTable.remove(key, value);
					        	 updateARPCacheTable();
					         }
				         }
				         
					}
			    }
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

