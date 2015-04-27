package hashing;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class zooKeepertest {
	zooBucket testBucket;
	zooBucket testBucket1;
	zooIndex testIndex;
	
	@Before
	public void setup() throws IOException {
		testIndex = new zooIndex("indexTest","dataTest",4);
		testBucket = new zooBucket(testIndex,4,0);
	}
	
	@Test
	public void testSeralize() throws IOException {
		
		zooKeeper zk = new zooKeeper();
		
		testBucket.insert(1);
		
		zk.setData("/zookeeper/testSplit", testBucket);
		testBucket1 = (zooBucket) zk.getData("/zookeeper/testSplit");
		
		assertEquals("Test seralize, deseralize: ",testBucket,testBucket1);
	}

	}
