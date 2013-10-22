package org.openmrs.module.shr.unstructureddata.dao;


public interface UnstructuredDAO {
	
    Boolean saveObject(String key, Object value);
    
    Object getObject(String key);
    
    Boolean purgeObject(String key);

}
