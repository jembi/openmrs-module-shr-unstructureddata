package org.openmrs.module.shr.unstructureddata.dao.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openmrs.module.shr.contenthandler.api.Content;
//import org.springframework.security.crypto.codec.Base64;

public class RiakUnstructuredDAOImplTest {

	//commented out tests as they are dependent on Riak being installed. 
	
/*	@Test
	public void RiakDAOTest() throws Exception{
		
		RiakUnstructuredDAOImpl riakDAO = new RiakUnstructuredDAOImpl();
		String key = "1";
		File file = new File("src/test/resources/riak.png");
		
		byte[] payloadInBytes = FileUtils.readFileToByteArray(file);

		String payload = new String(Base64.encodeBase64(payloadInBytes));
		assertNotNull(payload);
		
		Content content = new Content(payload, false, "formatCode", "image/JPG", null, Content.Representation.B64, null, null);
		
		assertTrue(riakDAO.saveContent(key, content));
		
		Content riakContent = riakDAO.getContent(key);
		
		assertEquals(content,riakContent);
		
		assertTrue(riakDAO.purgeContent(key));
		
		Content deleted = riakDAO.getContent(key);
		
		assertNull(deleted);
		
	}*/

}