package hashing;
//Thomas Turney
//Homework 2
//Dr. Guo
//COT 3400

import java.io.*;

/**
 * 
 * This class realizes the index array for extensible hash tables
 *
 */
public class Index {
	

	/**
	 * depth of index
	 */
	private int depth;
	/**
	 * max number of keys in a bucket
	 */
	private int maxBucketKeys;
		
	/**
	 * addresses of the buckets.
	 * 
	 * each address is actually where a bucket starts in the data file
	 */
	private long [] bucketAddrs;
		
	/**
	 * The file used to store index
	 */
	private RandomAccessFile indexFile;

	/**
	 * The file used to store data
	 */
	private RandomAccessFile dataFile;
	
	/**
	 * Returns the depth of the index
	 * @return the depth of the index
	 */
	public int getDepth(){
		return depth;
	}
	
	/**
	 * 
	 * Creates a new index and data file.
	 * 
	 * @param dbIndexPath path to the index file
	 * @param dataFilePath path to the data file
	 * @param maxBucketKeys
	 * @throws IOException
	 */
	public Index(String dbIndexPath, 
		String dataFilePath, int maxBucketKeys)
		throws IOException{
		
		this.maxBucketKeys=maxBucketKeys;
		depth=0;
		bucketAddrs=new long[1];

		
		/*
		 * Create index and data files. If they exist, eliminate 
		 * all the existing contents.
		 */
		indexFile=new RandomAccessFile(dbIndexPath, "rw");
		indexFile.setLength(0);
		dataFile=new RandomAccessFile(dataFilePath, "rw");
		dataFile.setLength(0);

		/*
		 * To create a bucket, three arguments are
		 * 1. a pointer to the index.
		 * 2. max num of keys
		 * 3. file position
		 */
		Bucket buck=new Bucket(this,maxBucketKeys,0);
		storeBucket(buck);
	}
	
	/**
	 * Stores the index and closes both index and data files
	 * @throws IOException
	 */
	public void close()
		throws IOException{
		indexFile.writeInt(bucketAddrs.length);
		for(int i=0;i<bucketAddrs.length;i++){
			indexFile.writeLong(bucketAddrs[i]);
		}
		dataFile.close();
	}
	
	/**
	 * Inserts a key
	 * @param key the key to insert
	 * @throws IOException
	 */
	public void insert(int key)
		throws IOException{
		//figure out if it's in a bucket.
		if(contains(key)) {
			//no need to do anything, key already exists.
			return;
		}
		//find which bucket we need.
		
		int in = findIndex(key);
		Bucket buck=loadBucket(in);
		buck.insert(key);
	}
	
	/**
	 * Saves a bucket. Should only be used by the initializer, maybe for delete functionality as well.
	 * @param buck the bucket to save
	 * @throws IOException
	 */
	public void storeBucket(Bucket buck)
		throws IOException{
		buck.storeData(dataFile);
	}
	
	/**
	 * Deletes a key
	 * @param key the key to delete
	 * @throws IOException
	 */
	public void delete(int key)
		throws IOException{
		
		//first, search for the key.
		if(contains(key)) { //key is found.
			//find which bucket we need.
			int in = findIndex(key);
			Bucket buck=loadBucket(in);
			buck.delete(key, false);
			
			//don't forget to write the data after.
			storeBucket(buck);

		}
	}
	
	/**
	 * Given the index position, load the corresponding bucket.
	 * @param indexPos
	 * @return the bucket loaded from the data file
	 * @throws IOException
	 */
	public Bucket loadBucket(int indexPos)
		throws IOException{

		long bucketAddr=bucketAddrs[indexPos];
		
		Bucket buck=new Bucket(this,maxBucketKeys,bucketAddr,indexPos); //if you come into insert, new bucket is made already... from debugging.
		buck.loadData(dataFile);
		
		return buck;
	}
	
	/**
	 * 
	 * Sees if a hash key exists in the hash table
	 * 
	 * @param key the test to be looked for
	 * @return true if the key exists; false otherwise
	 * @throws IOException
	 */
	public boolean contains(int key)
		throws IOException{
		int index=findIndex(key);
		Bucket buck=loadBucket(index);
		return buck.contains(key);
	}
	
	/**
	 * Given a hash key, finds its corresponding index.
	 * @param key hash key
	 * @return index position
	 */
	public int findIndex(int key){
		if(depth==0){
			return 0;
		}else{
			int result=0;
			int lowest=0;
			int mask=1;
			for(int i=0;i<depth;i++){
				lowest=key&mask;
				result=result<<1;
				result=result|lowest;
				key=key>>1;
			}
			return result;
		}
	}
	
	public RandomAccessFile getFilePointer(){
		return dataFile;
	}
	
	public long getBucketAddr(int indexPos){
		return bucketAddrs[indexPos];
	}
	
	public void setBucketAddr(int indexPos, long addr){
		bucketAddrs[indexPos]=addr;
	}
	
	/**
	 * Doubles the size of the index.
	 */	
	public void expand(){
		
		//first, we need a replacement bucket.
		long[] newBucketAddrsArray = new long[bucketAddrs.length*2];
		//next, we need to iterate through the old address.
		for(int i = 0; i < bucketAddrs.length; i++) {
			
			newBucketAddrsArray[2*i] = bucketAddrs[i]; //set the old addresses.
			newBucketAddrsArray[2*i+1] = bucketAddrs[i];
		}
		this.bucketAddrs = newBucketAddrsArray;
		depth++;
	}

	/** 
	 * Tries to collapse the index and returns if collapse really 
	 * takes place.
	 * @return true if the index is collapsed; false otherwise 
	 */
public boolean collapse(){
	
		//First, we need to check if collapsable.
		if(depth == 0) { //no further collapsing possible.
			return false;
		}
		//to collapse, we need to half the index.
		long[] newBucketAddrsArray = new long[bucketAddrs.length/2];
		//next, we need to iterate through the old address.
		for(int i = 0; i < newBucketAddrsArray.length; i++) {		
			newBucketAddrsArray[i] = bucketAddrs[2*i]; //set the address on what the current points at.
		}
		this.bucketAddrs = newBucketAddrsArray;
		depth--;
		return true;

	}

}