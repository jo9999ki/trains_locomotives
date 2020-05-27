package de.jk.quarkus.trains.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.jackson.ObjectMapperCustomizer;

@Singleton
public class RegisterCustomModuleCustomizer implements ObjectMapperCustomizer {

    public void customize(ObjectMapper mapper) {
   
    	//mapper.registerModule(new JavaTimeModule());
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	mapper.setDateFormat(df);
 
    }
}