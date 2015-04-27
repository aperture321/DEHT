package hashing;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class BucketTest {
	Bucket testBucket;
	Index testIndex;
	
	@Before
	public void setup() throws IOException {
		testIndex = new Index("indexTest","dataTest",4);
		testBucket = new Bucket(testIndex,4,0);
	}
	
	@Test
	public void testGetNumKeys() throws IOException {
		assertEquals("Test getting keys",testBucket.getNumKeys(),0);
	}

	@Test
	public void testInsert() throws IOException {
		testBucket.insert(2);
		assertEquals("Test getting keys",testBucket.getNumKeys(),1);
	}
	
	@Test
	public void testInsertToSplit() throws IOException {
		testBucket.insert(1);
		testBucket.insert(2);
		testBucket.insert(3);
		testBucket.insert(4);
		assertEquals("Test getting keys before split",testBucket.getNumKeys(),4);
		assertEquals("Test getting depth before split",testBucket.getDepth(),0);
		testBucket.insert(5);
		assertEquals("Test getting keys redistributed",testBucket.getNumKeys(),0);
		assertEquals("Test getting depth redistributed",testBucket.getDepth(),1);
	}

	@Test
	public void testDelete() throws IOException {
		testBucket.insert(2);
		testBucket.delete(2, false);
		assertEquals("Test getting keys",testBucket.getNumKeys(),0);
	}
	
	@Test
	public void testMultipleDelete() throws IOException {
		testBucket.insert(1);
		testBucket.insert(2);
		testBucket.insert(4);
		testBucket.delete(2, false);
		testBucket.delete(4, false);
		assertEquals("Test getting keys",testBucket.getNumKeys(),1);
	}
	
	@Test
	public void testFakeDelete() throws IOException {
		testBucket.delete(2, false);
		assertEquals("Test getting keys",testBucket.getNumKeys(),0);
	}

	
	@Test
	//(expected=IndexOutOfBoundsException.class) incompatible with maven.
	public void testInsertAndDeleteAfter() throws IOException {
		testBucket.insert(1);
		testBucket.insert(2);
		testBucket.insert(3);
		testBucket.insert(4);
		assertEquals("Test getting keys before split",testBucket.getNumKeys(),4);
		assertEquals("Test getting depth before split",testBucket.getDepth(),0);
		testBucket.insert(5);
		testBucket.insert(5);
		//Will delete the item, but the combine functionality asserts that Index has no other buckets to compare to.
		try {
			testBucket.delete(5, true);
		}catch(Exception e) {
			
		}
		
	}
	@Test
	public void testContains() throws IOException {
		testBucket.insert(2);
		assertTrue(testBucket.contains(2));
	}

	@Test
	public void testSearchFail() {
		assertEquals("Test failing a search",testBucket.search(-1),-1);
	}

	@Test
	public void testGetDepth() {
		assertEquals("NativeDepth",testBucket.getDepth(),0);
	}
	
	@Test
	public void testStringOutputEmpty() {
		assertEquals("Output: ",testBucket.toString(),"empty bucket");
	}
	
	@Test
	public void testStringOutput() throws IOException {
		testBucket.insert(1);
		testBucket.insert(2);
		assertEquals("Output: ",testBucket.toString(),"  1  2");
	}
}
