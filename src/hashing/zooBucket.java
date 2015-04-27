package hashing;

import java.io.*;
import java.util.Arrays;
import java.io.Serializable;

/**
 * This class realizes buckets for extensible hash tables
 * 
 */
public class zooBucket implements Serializable {

	/**
	 * Once a bucket is loaded in the memory, a pointer to the index
	 * is maintained
	 */
	private transient zooIndex index;

	/**
	 * max number of keys in this bucket
	 */
	private int maxNumKeys;

	/**
	 * depth of bucket
	 */
	private int depth;

	private int keys[];
	private int numKeys = 0;
	
	/**
	 * where this bucket is in the file.
	 * 
	 * Note this is a file offset.
	 */
	private long bucketAddr;

	private int indexFather = -1;
	
	/**
	 * Creates a bucket object.
	 * @param zooIndex the pointer to the index
	 * @param maxNumKeys how many keys can be stored in this bucket
	 * @param bucketAddr where this bucket is loaded from the file
	 * @throws IOException 
	 */
	public zooBucket(zooIndex zooIndex, int maxNumKeys, long bucketAddr) throws IOException{
		this.index=zooIndex;
		this.maxNumKeys=maxNumKeys;
		this.bucketAddr=bucketAddr;
		keys=new int[maxNumKeys];
	}

	public zooBucket(zooIndex index, int maxBucketKeys, long bucketAddr,
			int indexPos) throws IOException {
		this(index,maxBucketKeys,bucketAddr);
		this.indexFather  = indexPos;
		//Add in the indexPosition. Used for splits exclusively.
	}

	/**
	 * Stores this bucket back to the file.
	 * 
	 * The information to be stored includes 
	 * <ul>
	 * <li> number of keys
	 * <li> depth of bucket
	 * <li> all the elements in array keys
	 * </ul>
	 * 
	 * Note each bucket occupies equal amount of space on the disk no
	 * matter how many keys are actually in the bucket. 164 bytes.
	 * 
	 * @param
	 * @throws IOException
	 */
	public void storeZooData() throws IOException {

		zooKeeper zk = new zooKeeper();
		
		System.out.println("Save: " + this.toString());
		
		zk.setData("/zookeeper/buck--" + findMyIndex() + "/" + this.bucketAddr, this);
		try {
			zk.zooKeeper.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Given a hash key, finds its corresponding index.
	 * 
	 * @param key
	 *            hash key
	 * @return index position
	 */
	public int findMyIndex() {
		
		int key = this.keys[0];
		
		if (depth == 0) {
			return 0;
		} else {
			int result = 0;
			int lowest = 0;
			int mask = 1;
			for (int i = 0; i < depth; i++) {
				lowest = key & mask;
				result = result << 1;
				result = result | lowest;
				key = key >> 1;
			}
			return result;
		}
	}

	public int getNumKeys(){
		return numKeys;
	}
	
	/**
	 * Tries to insert a key in this bucket.
	 * @param key the key to insert
	 * @throws IOException
	 */
	public void insert(int key)
		throws IOException{
		
		//Index already check if there was a duplicate, there was not.
		//we check to see if a split needs to be done.
		//if it doesn't, we just insert the key.
		
		/* hint: do not forget to write the file sometimes. */
		if(numKeys == maxNumKeys) {
			split();
			index.insert(key);
		}
		else { //No need to split, or anything, insert as normal.
			keys[numKeys++] = key;
			index.storeBucket(this);
		}
	}
	
	/**
	 * Deletes a key from this bucket.
	 *  
	 * @param key the key to be delete.
	 * @param tryCombine true if this bucket is examined to see if it can 
	 *                   be combined with its buddy, false if the user only 
	 *                   wants to delete a key.
	 * @throws IOException
	 */
	public void delete(int key, boolean tryCombine)
		throws IOException{
		
		//First, see if key is there.
		if(!contains(key)) {
			return;
		}
		else { //remove the key from the list.
			int[] newkeys = new int[maxNumKeys];
			int newcount = 0;
			for(int i = 0; i < numKeys; i++) {
				if(keys[i] != key) { //only save the old keys
					newkeys[newcount++] = keys[i];
				}
			}
			numKeys = newcount;
			keys = newkeys;
			index.storeBucket(this);
			//once stored, try to combine.
			if(tryCombine) {
				combine();
			}
		}
	}
	
	/**
	 * Sees if a key is in the bucket.
	 * @param key the key to be looked for
	 * @return true if key exist; false otherwise
	 */
	public boolean contains(int key){
		return search(key)!=-1;
	}
	
	/**
	 * Loads the data from the file
	 * @param
	 * @throws IOException
	 */
	void loadZooData() throws IOException {
		
		zooKeeper zk = new zooKeeper();
		zooBucket loadBuck = 
				(zooBucket) zk.getData("/zookeeper/buck--" + findMyIndex() + "/" + this.bucketAddr);

		this.numKeys = loadBuck.numKeys;
		this.depth = loadBuck.depth;
		for(int i = 0; i < this.numKeys; i++) { //grab every key.
			this.keys[i] = loadBuck.keys[i];
		}
		
		System.out.println("Load: " + loadBuck.toString());
		
		try {
			zk.zooKeeper.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Searches for a key.
	 * 
	 * @param key the key to be searched for
	 * @return the index of the key if search succeeds, -1 otherwise
	 */
	public int search(int key){
		for(int i=0;i<numKeys;i++){
			if(keys[i]==key)
				return i;
		}
		return -1;
	}
	
	/**
	 * Finds the index position of the buddy bucket if a buddy exists.
	 * @return buddy index when a buddy exists; -1 otherwise.
	 */
	private int findBuddy(){

		if(depth == index.getDepth()) { //buddy is only found if these are of equal depths
			 //buddy position should be one bit off from cell position
			//first, get your own index.
			int indexpos = index.findIndex(keys[0]); //gives our own position.
			//buddy is 1 bit off.
			if(indexpos == 0) { //left side case, buddy is on right.
				return 1;
			}
			else { //all other cases, buddy is on the left
				return indexpos-1; 
			}
		}
		else {
			return -1;
		}
	}
	
	/**
	 * Tries to combine with its buddy.
	 * 
	 * If the buddy can be combined, move all the buddy's keys to here. 
	 * Additionally, redirect the index cell which used to point to the buddy
	 * to point to here.
	 * @throws IOException
	 */
	private void combine() throws IOException {
		
		if(depth < index.getDepth()) {
			return;
		}
		int bud = findBuddy();
		zooBucket buddybuck = index.loadBucket(bud);
		if((numKeys + buddybuck.numKeys) > maxNumKeys) { //see if buckets can merge
			return;
		}
		//move all of buddybuck's keys to this bucket.
		for(int i = 0; i < buddybuck.numKeys; i++) {
			keys[numKeys++] = buddybuck.keys[i];
		}
		depth++;
		index.storeBucket(this); //save changes
		//redirect the pointer.
		index.setBucketAddr(bud, this.bucketAddr); //set index's pointer to this bucket.
		if(index.collapse()) {
			combine();
		}

	}
	
	/**
	 * Split this bucket.
	 * 
	 * A bucket can only be split when it is pointed to by multiple
	 * index cells.
	 * 
	 * @throws IOException
	 */
	private void split() throws IOException {
		if(depth == index.getDepth()) { //check to see if index needs to grow.
			index.expand();
		}
		//once the index grew, all the new cells point to the old bucket.
		int x = index.getDepth() - depth++;
		int amount_of_points = (int) Math.pow(2, x); //how many index cells point to this bucket.
		
		//We need to make a new bucket.
		zooBucket newbuck = new zooBucket(index, maxNumKeys, index.getFilePointer().length());
		newbuck.depth = depth;
		
		index.storeBucket(newbuck); //bucket needs to be saved immediately upon being made. This will assign our bucket address to file.
		
		//This is a 50% split, the index pointers are reassigned to the correct bucket.
		//That i is times some offset for the index.
		for(int i = amount_of_points/2; i < amount_of_points; i++) { //set each index pointing to the new bucket address.
			index.setBucketAddr(this.indexFather+i, newbuck.bucketAddr);
		}
		
		//We offload the old array of keys. We need to clear the bucket. Reset items inside.
		int[] offload_keys = new int[maxNumKeys];
		offload_keys = Arrays.copyOf(this.keys, this.numKeys);
		Arrays.fill(this.keys, 0);
		this.numKeys = 0;
		//keep in mind, should depth be reset in some way?
		//save.
		index.storeBucket(this);
		for(int i = 0; i < offload_keys.length; i++) {
			index.insert(offload_keys[i]);
		}
		//After we complete this, we'll need to insert the key.
		return;
	}
	
	public int getDepth(){
		return depth;
	}
	
	public String toString(){
		if(numKeys==0){
			return "empty bucket";
		}
		String str="";
		for(int i=0;i<numKeys;i++){
			str=String.format("%s%3d",str,keys[i]);
		}
		return str;
	}
}