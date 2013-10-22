package org.openmrs.module.shr.unstructureddata.dao.impl;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.shr.unstructureddata.dao.UnstructuredDAO;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.bucket.Bucket;

public class RiakUnstructuredDAOImpl implements UnstructuredDAO {

	public static final Log log = LogFactory.getLog(RiakUnstructuredDAOImpl.class);
	
	@Override
	public Boolean saveObject(String key, Object value) {
		try{
			
			IRiakClient client = RiakFactory.pbcClient("127.0.0.1", 10017);
			log.error("Riak Connection successful");
			Bucket docBucket = client.fetchBucket("docBucket").execute();
			log.info("Riak Bucket Found");
			log.info("Riak object key: " + key);

			docBucket.store(key, IOUtils.toByteArray((InputStream) value)).execute();

			log.info("Riak Object stored");
			
			client.shutdown();
			
			return true;

		} catch(Exception e)
		{
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	public Object getObject(String key) {
		try{
			IRiakClient client = RiakFactory.pbcClient("127.0.0.1", 10017);
			Bucket docBucket = client.fetchBucket("docBucket").execute();

			byte[] data = docBucket.fetch(key).execute().getValue();

			client.shutdown();
			
			return data;
		
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean purgeObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
