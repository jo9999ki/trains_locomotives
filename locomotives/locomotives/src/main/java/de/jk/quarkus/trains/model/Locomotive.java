package de.jk.quarkus.trains.model;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;
@Entity
@RegisterForReflection
public class Locomotive extends PanacheEntity{
	//Id added by panache
	
	//number or other identifier
	@NotBlank(message="identification cannot be blank") //Validation
	@Length(min = 1, max = 20, message="identifier length must be between 1 and 20")//Validation
	@Column(name= "identification", length = 20, nullable = false)	
	@NotEmpty //Database
	public String identification;
	
	//technical identifier model railroad dcc standard (0 ... 9999)
	@NotNull(message="DCC address cannot be empty")//Validation
	@Min(0)//Validation
	@Max(9999)//Validation
	public Integer address;
	
	//Last revision date
	@Past (message="revision date cannot be in the future") //Validation
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
