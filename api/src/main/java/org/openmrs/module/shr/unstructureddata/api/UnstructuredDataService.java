package org.openmrs.module.shr.unstructureddata.api;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.shr.unstructureddata.dao.UnstructuredDAO;
import org.openmrs.module.shr.unstructureddata.exception.AlreadyRegisteredException;

public interface UnstructuredDataService extends OpenmrsService {

    /** To be called by the ComplexObsHandler/ComplexNoteHandler */
    UnstructuredDAO getUnstructuredDAO(String contentType);
 
    /** For each DAO a call to this method will be added in the ModuleActivator willStart() method */
    void registerUnstructuredDAO (String contentType, UnstructuredDAO prototype) throws AlreadyRegisteredException, NullPointerException;
   
    /** For each DAO a call to this method will be added in the ModuleActivator willStop() method */
    void deregisterUnstructuredDAO(String contentType);

    void setDefaultDAO(UnstructuredDAO prototype);
    
    UnstructuredDAO getDefaultDAO();
}
