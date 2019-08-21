package com.hframework.client.decrypt;


import com.hframework.common.resource.ResourceWrapper;
import com.hframework.common.resource.annotation.Key;
import com.hframework.common.resource.annotation.Source;

import java.lang.reflect.InvocationTargetException;

@Source("third/decrypt.properties")
public class DecryptConfig   {

	@Key( "third.decrypt.test_model")
	private String testModel;
	@Key( "third.decrypt.client")
	private String client;
	@Key( "third.decrypt.key")
	private String key;
	@Key( "third.decrypt.interface.des")
	private String des;
  
 
 	
	public String getTestModel(){
		return testModel;
	}

	public void setTestModel(String testModel){
    	this.testModel = testModel;
    }

 	
	public String getClient(){
		return client;
	}

	public void setClient(String client){
    	this.client = client;
    }

 	
	public String getKey(){
		return key;
	}

	public void setKey(String key){
    	this.key = key;
    }

 	
	public String getDes(){
		return des;
	}

	public void setDes(String des){
    	this.des = des;
    }

	private static DecryptConfig instance;

	private DecryptConfig() {
		super();
	}

	public  static DecryptConfig getInstance(){
		if(instance == null) {
			synchronized (DecryptConfig.class) {
				if(instance == null) {
					try {
						return instance = ResourceWrapper.getResourceBean(DecryptConfig.class);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					return instance = new DecryptConfig();
				}
			}
		}
		return instance;
	}

}
