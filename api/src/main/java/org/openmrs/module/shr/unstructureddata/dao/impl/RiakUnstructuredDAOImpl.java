package org.openmrs.module.shr.unstructureddata.dao.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.shr.contenthandler.api.Content;
import org.openmrs.module.shr.unstructureddata.dao.UnstructuredDAO;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.bucket.Bucket;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class RiakUnstructuredDAOImpl implements UnstructuredDAO {

	public static final Log log = LogFactory.getLog(RiakUnstructuredDAOImpl.class);

	public static final String HOSTKEY = "host";
	public static final String PORTKEY = "port";
	public static final String BUCKETKEY = "bucket";
	
	private String host = "127.0.0.1";
	private Integer port = 10017;
	private String 	bucket = "docBucket";

	public RiakUnstructuredDAOImpl(){
		loadRiakConfig();
	}
	
	@Override
	public Boolean saveContent(String key, Content content) {
		try{
			
			IRiakClient client = RiakFactory.pbcClient(host, port);
			Bucket docBucket = client.fetchBucket(bucket).execute();
			
			String contentAsJson = new Gson().toJson(content, Content.class);
			docBucket.store(key, contentAsJson).execute();

			client.shutdown();
			
			return true;

		} catch(Exception e)
		{
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	public Content getContent(String key) {
		try{

			IRiakClient client = RiakFactory.pbcClient(host, port);
			Bucket docBucket = client.fetchBucket(bucket).execute();
			IRiakObject contentRiakObject = docBucket.fetch(key).execute();
			client.shutdown();
			
			if (contentRiakObject == null){
				return null;
			}
			else {	
				
				try {
					Content content = new Gson().fromJson(contentRiakObject.getValueAsString(), Content.class);
					return content;
				} catch (JsonParseException e){
					log.error("Unstructured Data is not a Content Object, returning null");
					return null;
				}
			}
		
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean purgeContent(String key) {

		try {
			IRiakClient client = RiakFactory.pbcClient(host, port);
			Bucket docBucket = client.fetchBucket(bucket).execute();
			docBucket.delete(key).execute();
			client.shutdown();
			
			return true;
			
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}
	
	public void loadRiakConfig(){
	
		try {
			Properties riakConfig = new Properties();
			riakConfig.load(new FileInputStream("src/main/resources/riak.properties"));
			
			if (!riakConfig.getProperty(HOSTKEY).isEmpty())
				host = riakConfig.getProperty(HOSTKEY);
			
			if (!riakConfig.getProperty(PORTKEY).isEmpty())
				port = Integer.parseInt(riakConfig.getProperty(PORTKEY));
			
			if (!riakConfig.getProperty(BUCKETKEY).isEmpty())
				bucket = riakConfig.getProperty(BUCKETKEY);
		
		} catch (FileNotFoundException e) {
			log.info("Riak Config File Not Found, Using Default Settings");
		} catch (IOException e) {
			log.info("Riak Config File Could Not Be Loaded, Using Default Settings");
		}
	}
	

}
