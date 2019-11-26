import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RoutingTable {

  
   public static class CustomizedHashMap implements Comparator<Map.Entry<Integer, Node>> {

      @Override
      public int compare(Entry<Integer, Node> o1, Entry<Integer, Node> o2) {
         // TODO Auto-generated method stub
         return -o1.getKey().compareTo(o2.getKey());
      }
   }

   public static Map<Integer,Node> rountingTable;
   public Node head;
   public static List<Map.Entry<Integer, Node>> entries;

   public RoutingTable() {
      rountingTable = new HashMap<Integer,Node>();
   }

   public static void add(Object[] value) {

      Node head = null;
      byte[] netmask = (byte[]) value[1];
      int netnum = computeNetnum(netmask);
      System.out.println(netnum);

      if(rountingTable.containsKey(netnum)) {
         head = rountingTable.get(netnum);
         head.next = new Node(head.next,value);
      }else {
         rountingTable.put(netnum, new Node(null,value));
         Sorting();

      }
   }

   public static void Sorting() {
      entries = new ArrayList<Map.Entry<Integer, Node>>(rountingTable.entrySet());
       Collections.sort(entries, new CustomizedHashMap());
       
   }
   public boolean remove(Object[] value) {
      byte[] netmask = (byte[]) value[1];
      int netnum = computeNetnum(netmask);

      Node head = rountingTable.get(netnum);
      if(head==null) return false;
      
      byte[] destIP;
      byte[] nodeNetmask;
      String destIP2String;
      String nodeNetmask2String;

      /* Object[] value를 String으로 변환 : 비교하기 위함 */
      byte[] valueDestIP = (byte[]) value[0];
      byte[] valueNetmask = (byte[]) value[1];
      String valueDestIPString = (valueDestIP[0] & 0xFF) + "." + (valueDestIP[1] & 0xFF) + "."
            + (valueDestIP[2] & 0xFF) + "." + (valueDestIP[3] & 0xFF);
      String valueNetmaskString = (valueNetmask[0] & 0xFF) + "." + (valueNetmask[1] & 0xFF) + "."
            + (valueNetmask[2] & 0xFF) + "." + (valueNetmask[3] & 0xFF);
      
      Node prev = null;
      for (Node p = head; p != null; p = p.next) {
         
         destIP = (byte[]) p.value[0];
         nodeNetmask = (byte[]) p.value[1];
         destIP2String = (destIP[0] & 0xFF) + "." + (destIP[1] & 0xFF) + "." + (destIP[2] & 0xFF) + "."
               + (destIP[3] & 0xFF);
         nodeNetmask2String = (nodeNetmask[0] & 0xFF) + "." + (nodeNetmask[1] & 0xFF) + "." + (nodeNetmask[2] & 0xFF)
               + "." + (nodeNetmask[3] & 0xFF);
         /* Destination IP주소와 Netmask만 같은지 확인 */
         System.out.println(destIP2String);
         System.out.println(nodeNetmask2String);
         if (destIP2String.equals(valueDestIPString) && nodeNetmask2String.equals(valueNetmaskString)) {
            if(p==head) {
               System.out.println("here");
               rountingTable.remove(netnum);
               Sorting();
            }else if(p.next==null) {
               prev.next = null;
            }else {
               p.next = p.next.next;
            }
            return true;
         }
         prev = p;
      }
      return false;
   }

   public static int computeNetnum(byte[] netmask) {
      int cnt=0;

      for(int i=0;i<4;i++) {
         if((netmask[i]&0xFF) == 255) cnt += 8;
         else {
            int n= (netmask[i]&0xFF);
            while(n!=0) {
               cnt+=n%2;
               n/=2;
            }
         }
      }
      
      return cnt;
   }



   public Object[] findEntry(byte[] realDestination) {
      if(this.entries == null) return null;
      for(Map.Entry<Integer, Node> entry : this.entries) {
         Node head = entry.getValue();
         for (Node p = head; p.next != null; p = p.next) {
            byte[] destIP = (byte[])p.value[0];
            byte[] netmask = (byte[])p.value[1];

            if ((destIP[0]==(realDestination[0]&netmask[0]))&&(destIP[1]==(realDestination[1]&netmask[1]))&&(destIP[2]==(realDestination[2]&netmask[2]))&&(destIP[3]==(realDestination[3]&netmask[3]))) {
               return p.value;
            }
         }
      }
      return null;

   }
   
   public static String updateRoutingTable() {
      String printResult = "";
      
      for(Entry<Integer, Node> entry : entries) {
         Node head = entry.getValue();
         for (Node p = head; p.next != null; p = p.next) {
            byte[] desIP_Byte = (byte[])p.value[0];
            byte[] netmask = (byte[])p.value[1];
            byte[] gateway_Byte = (byte[])p.value[2];
            String destIP_String = "";
            String mask_String = "";
            String gateway_String = "";
            for (int j = 0; j < 3; j++) {
               mask_String = mask_String + (netmask[j]&0xFF)+".";
               destIP_String = destIP_String + (desIP_Byte[j]&0xFF)+".";
               gateway_String = gateway_String + (gateway_Byte[j]&0xFF)+".";
            }
            mask_String = mask_String + (netmask[3]&0xFF);
            destIP_String = destIP_String + (desIP_Byte[3]&0xFF);
            gateway_String = gateway_String + (gateway_Byte[3]&0xFF)+".";
            
            String flag_String = "";
            String interface_String = p.value[6] + "";
            if ((boolean) p.value[3]) {
               flag_String += "U";
            }
            if ((boolean) p.value[4]) {
               flag_String += "G";
            }
            if ((boolean) p.value[5]) {
               flag_String += "H";
            }

            printResult =printResult + "    " +destIP_String + "    " + mask_String + "      " + gateway_String + "         " + flag_String
                  + "        " + interface_String + "\n";
            
         }
         
      }
      return printResult;
   }
}
