package de.jk.quarkus.trains.model;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;
@Entity
@RegisterForReflection
public class Locomotive extends PanacheEntity{
	//Id added by panache
	
	//number or other identifier
	@NotEmpty
	@Column(name= "identification", length = 20, nullable = false)	
	public String identification;
	
	//technical identifier model railroad dcc standard (0 ... 9999)
	@NotEmpty
	public Integer address;
	
	//Last revision date
	public LocalDate revision;
	
	//Customized queries ...
	public static Locomotive findByAddress(Integer address){
        return find("address", address).firstResult();
    }
	
	public static List<Locomotive> findAllByIdentificationLike(String identification){
        return find("identification LIKE concat('%', :identification, '%')", 
                Parameters.with("identification", identification)).list();
    }
	
}
