package hashing;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

public class zooKeeper{

static ZooKeeper zooKeeper ;
static Integer mutex;
java.util.concurrent.CountDownLatch connectedSignal = new java.util.concurrent.CountDownLatch(1);
    public void connect() throws KeeperException, IOException, InterruptedException {
        zooKeeper = new ZooKeeper("localhost:2181", 10000,
                new Watcher(){
                public void process(WatchedEvent event) {
                    if (event.getState() == KeeperState.SyncConnected) {
                    	connectedSignal.countDown();
                    }
                }   
        });
    }

    zooKeeper(){
		try {
			this.connect();
			System.out.println("Connected to localhost");
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	void createData(Object data, String location){
		String locationOnServer ="/zookeeper/" + location;
		try {
			zooKeeper.create(locationOnServer, serialize(data), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Object getData(String locationOnServer){
		try {
			Stat stat = zooKeeper.exists(locationOnServer, false);;
			if(stat != null){ // if the node exists in the tree
				return deserialize(zooKeeper.getData(locationOnServer, false, stat));			 
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] serialize(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			byte[] byteArray = bos.toByteArray();
			System.out.println(byteArray);
			return byteArray;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
			}
			try {
				bos.close();
			} catch (IOException ex) {
			}
		}
	}

	private Object deserialize(byte[] data) {
		// TODO Auto-generated method stub
		ByteArrayInputStream b = new ByteArrayInputStream(data);
		ObjectInputStream o;
		try {
			o = new ObjectInputStream(b);
			return o.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	void setData(String locationOnServer, Object data){
		try {
			Stat stat = zooKeeper.exists(locationOnServer, false);
			if(stat != null) // if the node exists in the tree
				zooKeeper.setData(locationOnServer,serialize(data), stat.getVersion());			 
			else
				zooKeeper.create(locationOnServer, serialize(data), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}