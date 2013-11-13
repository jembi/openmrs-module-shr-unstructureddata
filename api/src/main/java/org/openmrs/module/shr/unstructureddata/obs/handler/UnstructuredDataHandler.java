package org.openmrs.module.shr.unstructureddata.obs.handler;

import org.openmrs.Obs;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.shr.contenthandler.api.Content;
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
		String contentType = "";
		Content content = new Content("", "", "");
        String key = obs.getUuid();
        
		try {
			content = ((Content) obs.getComplexData().getData());
		}
		catch (ClassCastException e){
			throw new APIException("ComplexData.data not of class Content for Complex Observation with Obs_id:" + obs.getObsId());
		}
		
		contentType= content.getContentType();
    	if (Context.getService(UnstructuredDataService.class).getUnstructuredDAO(contentType).saveContent(key, content)){
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
        String contentType = obs.getValueComplex();  
        String key = obs.getUuid();
        return Context.getService(UnstructuredDataService.class).getUnstructuredDAO(contentType).purgeContent(key);
    }
 
    @Override
    public Obs getObs(Obs obs, String view) {
  
        String contentType = obs.getValueComplex();      
        String key = obs.getUuid();
        
      	ComplexData complexData = new ComplexData(obs.getComplexData().getTitle(), Context.getService(UnstructuredDataService.class).getUnstructuredDAO(contentType).getContent(key));
      	obs.setComplexData(complexData);
      	return obs;
    }
 

}
