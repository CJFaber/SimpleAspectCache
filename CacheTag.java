package simple_cache;


//Class for the cache line tag
public class CacheTag {
	private int tag;
	
	CacheTag(){
		tag = 0;
	}
	//Defaults to a fully associative cache - Can change with aspects
	public boolean tagHit(int aTag){
		return (aTag == tag)? true : false;
	}
	public void setTag(int aTag){
		tag = aTag;
	}
	public int getTag(){
		return tag;
	}

}
