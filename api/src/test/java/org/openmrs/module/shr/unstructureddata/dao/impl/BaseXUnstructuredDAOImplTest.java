package org.openmrs.module.shr.unstructureddata.dao.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openmrs.module.shr.contenthandler.api.Content;

public class BaseXUnstructuredDAOImplTest {
	public static final Log log = LogFactory.getLog(RiakUnstructuredDAOImpl.class);

/*	@Test
	public void test() {
		BaseXUnstructuredDAOImpl baseXDAO = new BaseXUnstructuredDAOImpl();
		String key = "1";
		File file = new File("src/test/resources/consult_sample.xml");
		
		String payload;
		try {
			payload = FileUtils.readFileToString(file);

		Content content = new Content(payload, false, "formatCode", "text/XML", "UTF-8", Content.Representation.TXT, null, null);
		
		assertTrue(baseXDAO.saveContent(key, content));
		
		Content basexContent = baseXDAO.getContent(key);
		
		File diff = new File("src/test/resources/diff.txt");
	
		
		FileUtils.writeStringToFile(diff, basexContent.getPayload());
		
		assertEquals(content.getPayload(),basexContent.getPayload());
		
		
		//assertTrue(baseXDAO.purgeContent(key));
		
		//Content deleted = riakDAO.getContent(key);
		
		//assertNull(deleted);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
