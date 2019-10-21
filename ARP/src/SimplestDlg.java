

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


public class SimplestDlg extends JFrame implements BaseLayer     {

   public int nUpperLayerCount = 0;
   public String pLayerName = null;
   public BaseLayer p_UnderLayer = null;
   public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
   BaseLayer UnderLayer;
   public int progress_number;

   private static LayerManager m_LayerMgr = new LayerManager();

   private JTextField ChattingWrite;
   static JTextField FileNameArea;

   Container contentPane;

   JTextArea ChattingArea;

   static JTextArea EthernetSrcAddress;
   static JTextArea EthernetDstAddress;

   static JTextArea IPSrcAddress;
   static JTextArea IPDstAddress;

   JComboBox strCombo;

   JLabel choice;
   JLabel Elblsrc;
   JLabel Elbldst;

   JLabel Ilblsrc;
   JLabel Ilbldst;

   static JButton Setting_Button;

   static JButton src_Setting_Button;

   JButton Chat_send_Button;

   static JButton Cache_Table_Button;

   static JButton File_search_Button;   
   static JButton File_send_Button;

   FileDialog fdOpen;

   static JProgressBar progress;
   static JComboBox<String> NICComboBox;

   int adapterNumber = 0;
   static int index;
   String Text;
   String FileNameText;

   public static void main(String[] args) throws IOException {
      // TODO Auto-generated method stub

      m_LayerMgr.AddLayer(new NILayer("NI"));
      m_LayerMgr.AddLayer(new ApplicationLayer("APP"));
      m_LayerMgr.AddLayer(new TCPLayer("TCP"));
      m_LayerMgr.AddLayer(new IPLayer("IP"));
      m_LayerMgr.AddLayer(new ARPLayer("ARP"));
      m_LayerMgr.AddLayer(new EthernetLayer("Ethernet"));
      m_LayerMgr.AddLayer(new FileAppLayer("File"));
      m_LayerMgr.AddLayer(new ChatAppLayer("Chat"));

      m_LayerMgr.AddLayer(new SimplestDlg("GUI"));
      m_LayerMgr.ConnectLayers(" NI ( *Ethernet ( *ARP ( *IP ( +TCP ( -IP *Chat ( *GUI ) *File ( *GUI ) *APP ) ) ) *IP ( +TCP ( -IP *Chat ( *GUI ) *File ( *GUI ) *APP ) ) ) ) ");


      System.out.println(((IPLayer) m_LayerMgr.GetLayer("IP")).GetUnderLayer(0).GetLayerName());
      System.out.println(((IPLayer) m_LayerMgr.GetLayer("IP")).GetUnderLayer(1).GetLayerName());
      System.out.println(((TCPLayer) m_LayerMgr.GetLayer("TCP")).GetUnderLayer().GetLayerName());
      System.out.println(((TCPLayer) m_LayerMgr.GetLayer("TCP")).GetUpperLayer(0).GetLayerName());
      System.out.println(((TCPLayer) m_LayerMgr.GetLayer("TCP")).GetUpperLayer(1).GetLayerName());
      System.out.println(((EthernetLayer)m_LayerMgr.GetLayer("Ethernet")).GetUpperLayer(0).GetLayerName());
   }


   public SimplestDlg(String pName) throws IOException {
      pLayerName = pName;

      setTitle("simplest");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(250, 250, 644, 425);
      contentPane = new JPanel();
      ((JComponent) contentPane).setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      JPanel chattingPanel = new JPanel();// chatting panel
      chattingPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "chatting",
            TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
      chattingPanel.setBounds(10, 5, 360, 276);
      contentPane.add(chattingPanel);
      chattingPanel.setLayout(null);

      JPanel chattingEditorPanel = new JPanel();// chatting write panel
      chattingEditorPanel.setBounds(10, 15, 340, 210);
      chattingPanel.add(chattingEditorPanel);
      chattingEditorPanel.setLayout(null);

      ChattingArea = new JTextArea();
      ChattingArea.setEditable(false);
      ChattingArea.setBounds(0, 0, 340, 210);
      chattingEditorPanel.add(ChattingArea);// chatting edit

      JPanel chattingInputPanel = new JPanel();// chatting write panel
      chattingInputPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      chattingInputPanel.setBounds(10, 230, 250, 20);
      chattingPanel.add(chattingInputPanel);
      chattingInputPanel.setLayout(null);

      ChattingWrite = new JTextField();
      ChattingWrite.setBounds(2, 2, 250, 20);// 249
      chattingInputPanel.add(ChattingWrite);
      ChattingWrite.setColumns(10);// writing area

      /*ch_test*/
      //FileNameArea
      JPanel FileNamePanel = new JPanel();// FileName panel
      FileNamePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "파일 전송",
            TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
      FileNamePanel.setBounds(10, 280, 360, 90);
      contentPane.add(FileNamePanel);
      FileNamePanel.setLayout(null);


      JPanel FileNameInputPanel = new JPanel();
      FileNameInputPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      FileNameInputPanel.setBounds(10, 25, 250, 20);      
      FileNamePanel.add(FileNameInputPanel);
      FileNameInputPanel.setLayout(null);

      FileNameArea = new JTextField();
      FileNameArea.setBounds(2, 2, 250, 20);   
      FileNameArea.setEditable(false);
      FileNameInputPanel.add(FileNameArea);
      FileNameArea.setColumns(10);// writing area


      /*progress bar*/
      progress = new JProgressBar();
      progress.setValue(0);
      progress.setBounds(10, 55, 250, 20);
      FileNamePanel.add(progress);
      progress.setStringPainted(true);


      JPanel settingPanel = new JPanel();
      settingPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "setting",
            TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
      settingPanel.setBounds(380, 5, 236, 371);
      contentPane.add(settingPanel);
      settingPanel.setLayout(null);

      JPanel EsourceAddressPanel = new JPanel();
      EsourceAddressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      EsourceAddressPanel.setBounds(10, 96, 170, 20);
      settingPanel.add(EsourceAddressPanel);
      EsourceAddressPanel.setLayout(null);


      Elblsrc = new JLabel("Ethernet Source");
      Elblsrc.setBounds(10, 75, 170, 20);
      settingPanel.add(Elblsrc);


      EthernetSrcAddress = new JTextArea();
      EthernetSrcAddress.setBounds(2, 2, 170, 20);
      EthernetSrcAddress.setEditable(false);
      EsourceAddressPanel.add(EthernetSrcAddress);// src address

      Elbldst = new JLabel("Ethernet Destination");
      Elbldst.setBounds(10, 120, 170, 20);
      settingPanel.add(Elbldst);

      JPanel EdstAddressPanel = new JPanel();
      EdstAddressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      EdstAddressPanel.setBounds(10, 140, 170, 20);
      settingPanel.add(EdstAddressPanel);
      EdstAddressPanel.setLayout(null);

      EthernetDstAddress = new JTextArea();
      EthernetDstAddress.setBounds(2, 2, 170, 20);
      EdstAddressPanel.add(EthernetDstAddress);


      JPanel ISrcAddressPanel = new JPanel();
      ISrcAddressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      ISrcAddressPanel.setBounds(10, 212, 170, 20);
      settingPanel.add(ISrcAddressPanel);
      ISrcAddressPanel.setLayout(null);


      choice = new JLabel("NIC 선택");
      choice.setBounds(10, 30, 170, 20);
      settingPanel.add(choice);

      String[] adapterna= new String[((NILayer) m_LayerMgr.GetLayer("NI")).m_pAdapterList.size()];


      for(int i=0;i<((NILayer) m_LayerMgr.GetLayer("NI")).m_pAdapterList.size();i++)
         adapterna[i] = ((NILayer) m_LayerMgr.GetLayer("NI")).m_pAdapterList.get(i).getDescription();

      strCombo= new JComboBox(adapterna);
      strCombo.setBounds(10, 50, 165, 20);
      strCombo.setVisible(true);
      settingPanel.add(strCombo);
      strCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox) e.getSource(); 
            index = cb.getSelectedIndex();

            try {
               byte[] mac = ((NILayer) m_LayerMgr.GetLayer("NI")).m_pAdapterList.get(index).getHardwareAddress();
               final StringBuilder buf = new StringBuilder();
               for(byte b:mac) {
                  if(buf.length()!=0) buf.append(":");
                  if(b>=0 && b<16) buf.append('0');
                  buf.append(Integer.toHexString((b<0)? b+256:b).toUpperCase());
               }
               byte[] ipSrcAddress = ((((NILayer)m_LayerMgr.GetLayer("NI")).m_pAdapterList.get(index).getAddresses()).get(0)).getAddr().getData();
               final StringBuilder buf2 = new StringBuilder();
               for(byte b:ipSrcAddress) {
                  if(buf2.length()!=0) buf2.append(".");
                  buf2.append(b&0xff);
               }
               IPSrcAddress.setText(buf2.toString());
               EthernetSrcAddress.setText(buf.toString());
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });


      src_Setting_Button = new JButton("set");// setting
      src_Setting_Button.setBounds(178, 50, 53, 20);
      src_Setting_Button.addActionListener(new setAddressListener());
      settingPanel.add(src_Setting_Button);// setting


      JPanel IdestinationAddressPanel = new JPanel();
      IdestinationAddressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
      IdestinationAddressPanel.setBounds(10, 256, 170, 20);
      settingPanel.add(IdestinationAddressPanel);
      IdestinationAddressPanel.setLayout(null);

      Ilblsrc = new JLabel("IP Source");
      Ilblsrc.setBounds(10, 187, 190, 20);
      settingPanel.add(Ilblsrc);

      IPSrcAddress = new JTextArea();
      IPSrcAddress.setBounds(2, 2, 170, 20);
      //      IPSrcAddress.setEditable(false);
      ISrcAddressPanel.add(IPSrcAddress);// 

      Ilbldst = new JLabel("IP Destination");
      Ilbldst.setBounds(10, 232, 190, 20);
      settingPanel.add(Ilbldst);

      IPDstAddress = new JTextArea();
      IPDstAddress.setBounds(2, 2, 170, 20);
      IdestinationAddressPanel.add(IPDstAddress);// dst address

      Setting_Button = new JButton("Setting");// setting
      Setting_Button.setBounds(60, 300, 100, 20);
      Setting_Button.addActionListener(new setAddressListener());
      settingPanel.add(Setting_Button);// setting
      Setting_Button.setEnabled(false);

      Cache_Table_Button = new JButton("cache table");
      Cache_Table_Button.setBounds(60, 330, 100, 20);
      Cache_Table_Button.addActionListener(new setAddressListener());
      settingPanel.add(Cache_Table_Button);
      Cache_Table_Button.setEnabled(false);

      Chat_send_Button = new JButton("Send");
      Chat_send_Button.setBounds(270, 230, 80, 20);
      Chat_send_Button.addActionListener(new setAddressListener());
      chattingPanel.add(Chat_send_Button);// chatting send button

      File_search_Button = new JButton("file...");
      File_search_Button.setBounds(270, 25, 80, 20);
      File_search_Button.addActionListener(new setAddressListener());
      FileNamePanel.add(File_search_Button);

      fdOpen = new FileDialog(this, "file open", FileDialog.LOAD);


      File_send_Button = new JButton("Send");
      File_send_Button.setBounds(270, 55, 80, 20);
      File_send_Button.addActionListener(new setAddressListener());
      FileNamePanel.add(File_send_Button);
      File_send_Button.setEnabled(false);

      setVisible(true);

   }




   class setAddressListener implements ActionListener  {
      @Override
      public void actionPerformed(ActionEvent e) {

         if(e.getSource() == Cache_Table_Button) {
            //   new ApplicationLayer(); 
            ((ApplicationLayer)m_LayerMgr.GetLayer("APP")).App_pop(m_LayerMgr);
         }
         if(e.getSource() == src_Setting_Button) {   

            if(EthernetSrcAddress.getText().compareTo("") != 0 && IPSrcAddress.getText().compareTo("") !=0) {
               IPSrcAddress.setEditable(false);
               src_Setting_Button.setEnabled(false);
               Setting_Button.setEnabled(true);
               Cache_Table_Button.setEnabled(true);

               String[] valuesES = EthernetSrcAddress.getText().split(":");
               byte[] Esrc = new byte[6];
               for(int i=0;i<6;i++) {
                  Esrc[i] = (byte) Integer.parseInt(valuesES[i],16);
               }

               ((EthernetLayer) m_LayerMgr.GetLayer("Ethernet")).SetEnetSrcAddress(Esrc);
               ((ARPLayer) m_LayerMgr.GetLayer("ARP")).SetMacAddrSrcAddr(Esrc);



               String[] valuesIS = IPSrcAddress.getText().split("\\.");

               byte[] Isrc = new byte[4];
               for(int i=0;i<4;i++) {
                  Isrc[i] = (byte) Integer.parseInt(valuesIS[i]);
               }



               ((IPLayer) m_LayerMgr.GetLayer("IP")).SetIPSrcAddress(Isrc);
               ((ARPLayer) m_LayerMgr.GetLayer("ARP")).SetIPAddrSrcAddr(Isrc);

               ((NILayer) m_LayerMgr.GetLayer("NI")).SetAdapterNumber(index);
            }
         }
         if (e.getSource() == Setting_Button) {
            if(Setting_Button.getText() == "Setting") {
               String IdstAd = IPDstAddress.getText();
               String EdstAd = EthernetDstAddress.getText();

               if(IdstAd.compareTo("") == 0 || EdstAd.compareTo("") ==0 ) {
                  ChattingArea.setText("주소설정오류: 주소 setting이 안됐습니다.\n");
               }else {
                  String[] valuesES = EthernetSrcAddress.getText().split(":");
                  byte[] Esrc = new byte[6];
                  for(int i=0;i<6;i++) {
                     Esrc[i] = (byte) Integer.parseInt(valuesES[i],16);
                  }

                  ((EthernetLayer) m_LayerMgr.GetLayer("Ethernet")).SetEnetSrcAddress(Esrc);
                  ((ARPLayer) m_LayerMgr.GetLayer("ARP")).SetMacAddrSrcAddr(Esrc);

                  String[] valuesED = EdstAd.split(":");

                  byte[] Edst = new byte[6];
                  for(int i=0;i<6;i++) {
                     Edst[i] = (byte) Integer.parseInt(valuesED[i],16);
                  }


                  String[] valuesID = IdstAd.split("\\.");

                  byte[] Idst = new byte[4];
                  for(int i=0;i<4;i++) {
                     Idst[i] = (byte) Integer.parseInt(valuesID[i]);
                  }

                  ((EthernetLayer) m_LayerMgr.GetLayer("Ethernet")).SetEnetDstAddress(Edst);
                  ((IPLayer) m_LayerMgr.GetLayer("IP")).SetIPDstAddress(Idst);
                  ((IPLayer) m_LayerMgr.GetLayer("IP")).chatDST_mac=Edst;
                  //                  ((IPLayer) m_LayerMgr.GetLayer("IP")).chatDST_ip=Idst;


                  ChattingArea.setText("");
                  Setting_Button.setText("Reset");


                  EthernetDstAddress.setEnabled(false);
                  EthernetSrcAddress.setEnabled(false);
                  IPDstAddress.setEnabled(false);
                  IPSrcAddress.setEnabled(false);
                  strCombo.setEnabled(false);

               }

            }else { 

               EthernetDstAddress.setEnabled(true);
               IPDstAddress.setEnabled(true);

               EthernetDstAddress.setText("");
               IPDstAddress.setText("");

               ChattingArea.setText("");
               Setting_Button.setText("Setting");
            }
         }

         if(e.getSource() == Chat_send_Button) {
            if(Setting_Button.getText() == "Reset") {

               Text = ChattingWrite.getText();
               ChattingWrite.setText("");

               String ex = ChattingArea.getText();
               ChattingArea.setText(ex+"[SEND]: "+Text+"\n");

               byte[] sbchange = Text.getBytes();
               ((ChatAppLayer) m_LayerMgr.GetLayer("Chat")).Send(sbchange,Text.getBytes().length);


            }else {
               //주소값이없으면“주소설정오류” MessageDialog를띄운다.
               ChattingArea.setText("주소설정오류: 주소 setting이 안됐습니다.\n");
            }
         }
         if(e.getSource() == File_search_Button) {

            fdOpen.setVisible(true);

            String name = fdOpen.getFile();
            String path = fdOpen.getDirectory() + name;
            FileNameText = path;

            if(name != null) {            
               FileNameArea.setText(name);
            }else {
               JOptionPane.showMessageDialog(null, "파일을 선택하지않았습니다.",
                     "경고", JOptionPane.WARNING_MESSAGE);

               FileNameArea.setText("");
               File_send_Button.setEnabled(false);

               return;
            }
            File_send_Button.setEnabled(true);

         }
         if(e.getSource() == File_send_Button) {
            if(Setting_Button.getText() == "Reset") {

               FileNameArea.setText("");
               byte[] fichange = FileNameText.getBytes();

               ((FileAppLayer) m_LayerMgr.GetLayer("File")).Send(fichange,fichange.length);
               File_send_Button.setEnabled(false);

               progress_number = 0;

            }else {
               //주소값이없으면“주소설정오류” MessageDialog를띄운다.
               ChattingArea.setText("주소설정오류: 주소 setting이 안됐습니다.\n");
            }

         }

      }
   }


   public boolean Receive(byte[] input) {   

      String ex = ChattingArea.getText();
      String ne = new String(input);

      ChattingArea.setText(ex+"[RECV]: "+ne+"\n");

      return true;
   }

   public static void serSRCAddr(String mactemp) {
      if(EthernetSrcAddress.getText().compareTo("") != 0 && IPSrcAddress.getText().compareTo("") !=0) {
         //         IPSrcAddress.setEditable(false);
         //src_Setting_Button.setEnabled(true);
         //Setting_Button.setEnabled(false);
         Cache_Table_Button.setEnabled(true);

         EthernetSrcAddress.setText(mactemp);
         String[] valuesES = EthernetSrcAddress.getText().split(":");

         byte[] Esrc = new byte[6];
         for(int i=0;i<6;i++) {
            Esrc[i] = (byte) Integer.parseInt(valuesES[i],16);
         }

         String[] valuesIS = IPSrcAddress.getText().split("\\.");

         byte[] Isrc = new byte[4];
         for(int i=0;i<4;i++) {
            Isrc[i] = (byte) Integer.parseInt(valuesIS[i]);
         }
         String IdstAd = IPDstAddress.getText();
         String EdstAd = EthernetDstAddress.getText();
         
         if(!EdstAd.equals("")) {
            String[] valuesED = EdstAd.split(":");

            byte[] Edst = new byte[6];
            for(int i=0;i<6;i++) {
               Edst[i] = (byte) Integer.parseInt(valuesED[i],16);
            }


            String[] valuesID = IdstAd.split("\\.");

            byte[] Idst = new byte[4];
            for(int i=0;i<4;i++) {
               Idst[i] = (byte) Integer.parseInt(valuesID[i]);
            }

            ((EthernetLayer) m_LayerMgr.GetLayer("Ethernet")).SetEnetDstAddress(Edst);
            ((IPLayer) m_LayerMgr.GetLayer("IP")).SetIPDstAddress(Idst);
            ((IPLayer) m_LayerMgr.GetLayer("IP")).chatDST_mac=Edst;   
         }
         

         ((EthernetLayer) m_LayerMgr.GetLayer("Ethernet")).SetEnetSrcAddress(Esrc);
         ((ARPLayer) m_LayerMgr.GetLayer("ARP")).SetMacAddrSrcAddr(Esrc);

         ((IPLayer) m_LayerMgr.GetLayer("IP")).SetIPSrcAddress(Isrc);
         ((ARPLayer) m_LayerMgr.GetLayer("ARP")).SetIPAddrSrcAddr(Isrc);
         //         
         //         ((NILayer) m_LayerMgr.GetLayer("NI")).SetAdapterNumber(index);
      }
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