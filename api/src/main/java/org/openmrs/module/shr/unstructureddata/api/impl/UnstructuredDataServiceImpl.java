package org.openmrs.module.shr.unstructureddata.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.module.shr.unstructureddata.api.UnstructuredDataService;
import org.openmrs.module.shr.unstructureddata.dao.UnstructuredDAO;
import org.openmrs.module.shr.unstructureddata.dao.impl.RiakUnstructuredDAOImpl;
import org.openmrs.module.shr.unstructureddata.exception.AlreadyRegisteredException;

public class UnstructuredDataServiceImpl implements UnstructuredDataService {

	Map<String, UnstructuredDAO> daoMap = new HashMap<String, UnstructuredDAO>();
	UnstructuredDAO defaultDAO;
	
	@Override
	public void onShutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartup() {
		setDefaultDAO(new RiakUnstructuredDAOImpl());
	}

	@Override
	public UnstructuredDAO getUnstructuredDAO(String contentType) {
		if (this.daoMap.containsKey(contentType))
			return this.daoMap.get(contentType);
		else
			return this.defaultDAO;
	}

	@Override
	public void registerUnstructuredDAO(String contentType,
			UnstructuredDAO dao) throws AlreadyRegisteredException, NullPointerException {
		
		if (contentType == null || dao == null){
			throw new NullPointerException();
		}
		else if (this.daoMap.containsKey(contentType)) {
			throw new AlreadyRegisteredException();
		}
		else{
			this.daoMap.put(contentType, dao);
		}
	}

	@Override
	public void deregisterUnstructuredDAO(String contentType) {
		
		this.daoMap.remove(contentType);

	}

	@Override
	public void setDefaultDAO(UnstructuredDAO dao) {
		this.defaultDAO = dao;
	}

	@Override
	public UnstructuredDAO getDefaultDAO() {
		return this.defaultDAO;
	}

}
