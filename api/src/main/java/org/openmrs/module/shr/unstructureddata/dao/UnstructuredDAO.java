package org.openmrs.module.shr.unstructureddata.dao;

import org.openmrs.module.shr.contenthandler.api.Content;

public interface UnstructuredDAO {
	
    Boolean saveContent(String key, Content content);
    
    Object getContent(String key);
    
    Boolean purgeContent(String key);

}
