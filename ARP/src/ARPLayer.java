import java.util.ArrayList;

public class ARPLayer implements BaseLayer {
	
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	
	
	
	
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
