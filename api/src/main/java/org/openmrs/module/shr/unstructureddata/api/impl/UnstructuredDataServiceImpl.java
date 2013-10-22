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
	public void RegisterUnstructuredDAO(String contentType,
			UnstructuredDAO prototype) throws AlreadyRegisteredException {
		this.daoMap.put(contentType, prototype);

	}

	@Override
	public void DeregisterUnstructuredDAO(String contentType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultDAO(UnstructuredDAO prototype) {
		this.defaultDAO = prototype;
	}

	@Override
	public UnstructuredDAO getDefaultDAO() {
		return this.defaultDAO;
	}

}
