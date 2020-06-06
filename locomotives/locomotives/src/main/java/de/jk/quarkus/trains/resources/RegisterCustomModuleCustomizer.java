/*
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
}*/
/*
package de.jk.quarkus.trains.resources;
import java.time.LocalDate;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;

import io.quarkus.jsonb.JsonbConfigCustomizer;

@Singleton
public class RegisterCustomModuleCustomizer implements JsonbConfigCustomizer {

    public void customize(JsonbConfig config) {
        config.withDeserializers(new LocalDateDeserializer());
    }
}*/