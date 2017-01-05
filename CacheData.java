package simple_cache;

//Class for the actual data in the cache line
public class CacheData {
	private int [] data;
	
	CacheData(byte displace){
		data = new int [displace];
		for(int i = 0; i < displace; ++i){
			data[i] = 0;
		}
	}
	public int[] returnDataLine(){
		return data;
	}
	
	public int returnDataSingle(byte offset){
		return data[offset];
	}
	
	public void writeDataLine (int[] data){
		this.data = data.clone();
	}
	
	public void writeDataSingle (int data, byte offset){
		this.data[offset] = data;
		
	}
	
	public String printData(){
		String str = "";
		for (int i = 0; i < data.length; ++i){
			str = str + data[i] + ", ";
		}
		return str;
	}

}
