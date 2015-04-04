package hashing;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexTest {

	Index testIndex;
	
	@Before
	public void setup() throws IOException {
		testIndex =  new Index("indexTest","dataTest",4);
	}
	
	@Test
	public void testGetDepth() {
		assertEquals("Test getting depth",0,testIndex.getDepth());
	}

	@Test
	public void testInsert() throws IOException {
		testIndex.insert(0);
		assertEquals("Test getting depth",0,testIndex.getDepth());
		assertTrue("Test finding key after insert",testIndex.contains(0));
	}
	
	@Test
	public void testInsertDuplicate() throws IOException {
		testIndex.insert(0);
		testIndex.insert(0);
		assertEquals("Test getting depth",0,testIndex.getDepth());
		assertTrue("Test finding key after insert",testIndex.contains(0));
	}

	@Test
	public void testStoreBucket() throws IOException {
		testIndex.insert(3);
		Bucket buck = new Bucket(testIndex,4,0);
		testIndex.storeBucket(buck);
		//Stored bucket retrieved.
		Bucket buck2 = testIndex.loadBucket(0);
		assertFalse(buck2.contains(3));
		assertTrue("Storing bucket",buck2.toString().equals(buck.toString()));
	}

	@Test
	public void testDelete() throws IOException {
		testIndex.insert(4);
		assertTrue(testIndex.contains(4));
		testIndex.delete(4);
		assertFalse(testIndex.contains(4));
	}
	
	@Test
	public void testDeleteDuplicate() throws IOException {
		testIndex.insert(4);
		assertTrue(testIndex.contains(4));
		testIndex.delete(4);
		testIndex.delete(4);
		assertFalse(testIndex.contains(4));
	}

	@Test
	public void testFindIndex()  throws IOException {
		testIndex.insert(3);
		assertEquals(testIndex.findIndex(3),0); //before index increases.
		testIndex.expand();
		assertEquals("Test searching for key within expanded index.",testIndex.findIndex(1),1);
	}

	@Test
	public void testExpand()  throws IOException {
		testIndex.insert(3);
		testIndex.getBucketAddr(0);
		testIndex.expand();
		try {
			testIndex.getBucketAddr(1);
		} catch(Exception e) {
			Assert.fail("Index size did not increase.");
		}
	}

	@Test
	public void testCollapse() throws IOException {
		testIndex.insert(3);
		testIndex.expand();
		testIndex.getBucketAddr(1);
		assertTrue(testIndex.collapse());
	}
	@Test
	public void testCollapseNothingToCollapse() throws IOException {
		assertFalse(testIndex.collapse());
	}

	@Test
	public void testClose() throws IOException {
		testIndex.close();
	}

}
