package org.openmrs.module.shr.unstructureddata.obs.handler;

import org.openmrs.Obs;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.shr.unstructureddata.api.UnstructuredDataService;
import org.openmrs.obs.ComplexData;
import org.openmrs.obs.ComplexObsHandler;
import org.openmrs.obs.handler.AbstractHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class UnstructuredDataHandler extends AbstractHandler implements
		ComplexObsHandler {

	@Override
    public Obs saveObs(Obs obs) throws APIException {
         
        String contentType = getContentType(obs.getComplexData().getTitle()); 
        String key = obs.getUuid();

    	if (Context.getService(UnstructuredDataService.class).getUnstructuredDAO(contentType).saveObject(key, obs.getComplexData().getData())){
    		obs.setValueComplex(obs.getComplexData().getTitle());
    		obs.setComplexData(null);
    	} else {
    		obs.setValueComplex("Complex Data Could Not Be Saved For This DataType");
    		obs.setComplexData(null);
    	}
      
        return obs;
    }
     
    @Override
    public boolean purgeComplexData(Obs obs) {
        String contentType = getContentType(obs.getComplexData().getTitle());       
        String key = obs.getUuid();
        return Context.getService(UnstructuredDataService.class).getUnstructuredDAO(contentType).purgeObject(key);
    }
 
    @Override
    public Obs getObs(Obs obs, String view) {
  
        String contentType = getContentType(obs.getComplexData().getTitle());       
        String key = obs.getUuid();
        
      	ComplexData complexData = new ComplexData(obs.getComplexData().getTitle(), Context.getService(UnstructuredDataService.class).getUnstructuredDAO(contentType).getObject(key));
      	obs.setComplexData(complexData);
      	return obs;
    }
 
    String getContentType(String title){
		return "Content/JPG";
    //do parsing here
    }

}
