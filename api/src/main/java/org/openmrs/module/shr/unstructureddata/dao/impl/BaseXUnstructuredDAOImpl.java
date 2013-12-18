package org.openmrs.module.shr.unstructureddata.dao.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.basex.util.Base64;

import org.openmrs.module.shr.contenthandler.api.Content;
import org.openmrs.module.shr.unstructureddata.dao.UnstructuredDAO;

public class BaseXUnstructuredDAOImpl implements UnstructuredDAO {
	
	public static final Log log = LogFactory.getLog(BaseXUnstructuredDAOImpl.class);

	public static final String HOSTKEY = "host";
	public static final String PORTKEY = "port";
	public static final String DATABASEKEY = "database";
	public static final String USERNAMEKEY = "username";
	public static final String PASSWORDKEY = "password";
	
	private String host = "127.0.0.1";
	private Integer port = 8080;
	private String 	database = "shr";
	private String username = "shr";
	private String password = "shr";
	
	@Override
	public Boolean saveContent(String key, Content content) {
		
		loadBaseXConfig();
		
		try {
			URL url = new URL("http://" + host + ":" + port + "/BaseX772" + "/rest/" + database + "/" + key + ".xml?chop=false");

			if (!content.getContentType().contains("XML")){
				log.info("Non-XML type" + content.getContentType() + " can't be saved by the BaseX handler" );
				return false;
			}
			// Establish the connection to the URL. 
		    HttpURLConnection conn = openConnection(url, "PUT", username, password);

		    OutputStream out = new BufferedOutputStream(conn.getOutputStream());
		    InputStream in = new ByteArrayInputStream(content.getPayload().getBytes());
		    
		    for(int i; (i = in.read()) != -1;) out.write(i);
		    in.close();
		    out.close();

		    log.error("\n* HTTP response: " + conn.getResponseCode() +
			        " (" + conn.getResponseMessage() + ')');
		    
		    if (conn.getResponseCode() == 201){
		    	conn.disconnect();
		    	return true;			//resource created
		    }
		
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;				//error
		}
		
		return false;					//resource not created
	}

	@Override
	public Content getContent(String key) {
		
		String payload = "";
		loadBaseXConfig();
		
		try{
			URL	url = new URL("http://" + host + ":" + port + "/BaseX772" +"/rest/" + database + "/" + key + ".xml");

			// Establish the connection to the URL. 
			HttpURLConnection conn = openConnection(url, "GET", username, password);
		    /* Print the HTTP response code
		    int code = conn.getResponseCode();
			
		    log.info("\n* HTTP response: " + code +
		       " (" + conn.getResponseMessage() + ')');
			*/
			
		    // Check if request was successful
		    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

		    	// Get and cache input as UTF-8 encoded stream
		    	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		      
		    	payload = IOUtils.toString(br);

		    	br.close();
		    
		      conn.disconnect();
		    
		    }
		    
		} catch (Exception e){
			e.printStackTrace();
			return null;		//error
		}
		    
		Content content = new Content(payload, false, "formatCode", "text/XML", "UTF-8", Content.Representation.TXT, null, null);
		
		return content;		
	}

	@Override
	public Boolean purgeContent(String key) {
		
		loadBaseXConfig();
		
		try{
			URL url = new URL("http://" + host + ":" + port + "/BaseX772" + "/rest/" + database + "/" + key + ".xml");
	
		    HttpURLConnection conn = openConnection(url, "DELETE", username, password);
		    
		    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) { 		//resource deleted
		    	conn.disconnect();
		    	return true;
		    } 
		    
		    conn.disconnect();												//resource not found
		    return false;
		    
		} catch (Exception e){												//error
			e.printStackTrace();
			return null;
		}
	
	
	}

	public void loadBaseXConfig(){
		
		try {
			Properties baseXConfig = new Properties();
			baseXConfig.load(new FileInputStream("src/main/resources/basex.properties"));
			
			if (!baseXConfig.getProperty(HOSTKEY).isEmpty())
				host = baseXConfig.getProperty(HOSTKEY);
			
			if (!baseXConfig.getProperty(PORTKEY).isEmpty())
				port = Integer.parseInt(baseXConfig.getProperty(PORTKEY));
			
			if (!baseXConfig.getProperty(DATABASEKEY).isEmpty())
				database = baseXConfig.getProperty(DATABASEKEY);
			
			if (!baseXConfig.getProperty(USERNAMEKEY).isEmpty())
				username = baseXConfig.getProperty(USERNAMEKEY);
		
			if (!baseXConfig.getProperty(PASSWORDKEY).isEmpty())
				password = baseXConfig.getProperty(PASSWORDKEY);
		
		
		} catch (FileNotFoundException e) {
			log.info("BaseX Config File Not Found, Using Default Settings");
		} catch (IOException e) {
			log.info("BaseX Config File Could Not Be Loaded, Using Default Settings");
		}
		
		
	}
	
	public HttpURLConnection openConnection(URL url, String requestMethod, String username, String password){
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
	    	String encoded = Base64.encode(username + ":" + password);
	    	conn.setRequestMethod(requestMethod);
	    	conn.setRequestProperty("Authorization", "Basic " + encoded);
	    	
	    	if (requestMethod=="PUT" || requestMethod=="POST"){
	    		conn.setRequestProperty("Content-Type", "application/xml");
	    		conn.setDoOutput(true);
	    	}
	    	
		    return conn;		    
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}

	}
}
