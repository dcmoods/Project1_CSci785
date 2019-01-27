package com.moody;

import java.io.*;
import java.util.*;

public class PropertyParser {

	private Properties prop = null;
    
    public PropertyParser(Properties propData){
         
        InputStream is = null;
        this.prop = propData;
    }
     
    public Set<Object> getAllKeys(){
        Set<Object> keys = prop.keySet();
        return keys;
    }
     
    public String getPropertyValue(String key){
        return this.prop.getProperty(key);
    }
     
}
