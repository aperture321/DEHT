package hashing;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class BucketTest {
	Bucket testBucket;
	//Index has to be a mock file, since it will be required for "store" procedures.
	Index testIndex;
	@Before
	public void setup() {
		testBucket = new Bucket(null,4,0);
	}
	
	@Test
	public void testGetNumKeys() {
		assertEquals("Test getting keys",testBucket.getNumKeys(),0);
	}

	@Test
	public void testInsert() throws IOException {
		testBucket.insert(2);
		assertEquals("Test getting keys",testBucket.getNumKeys(),1);
	}

	@Test
	public void testDelete() {
		fail();
	}

	@Test
	public void testContains() {
		fail();
	}

	@Test
	public void testSearch() {
		fail();
	}

	@Test
	public void testGetDepth() {
		fail();
	}
}
