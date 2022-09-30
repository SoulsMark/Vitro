package edu.cornell.mannlib.vitro.webapp.dynapi.data;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Parameter;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.ImplementationType;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.types.ParameterType;

public class Data {
	
    private String string = null;
    private Object object = null;
    private Parameter param = null;

    public Data(Parameter param){
        this.param = param;
    }
    
    protected void setObject(Object object) {
        this.object = object;
    }
    
    protected Object getObject() {
        return object;
    }
    
    public Parameter getParam() {
    	return param;
    }
    
    public void setRawString(String raw) {
        this.string = raw;
    }
    
    protected String getRawString() {
        return string;
    }

	public void earlyInitialization() {
	    initialization();
	}
	
	public void initialization() {
		if (param.isInternal()) {
		    initializeDefault();
			return;
		} 
	    final ParameterType type = param.getType();
	    final ImplementationType implementationType = type.getImplementationType();
		object = implementationType.deserialize(type, string);
	}

	public String getSerializedValue() {
		final ParameterType type = param.getType();
		final ImplementationType implementationType = type.getImplementationType();
		if (object == null) {
			object = implementationType.deserialize(type, string);
		}
		return implementationType.serialize(type, object).toString();
	}

	public void initializeDefault()  {
		ParameterType type = param.getType();
		ImplementationType implementationType = type.getImplementationType();
		String defaultValue = param.getDefaultValue(); 		
		object = implementationType.deserialize(type, defaultValue);
	}
	
}
