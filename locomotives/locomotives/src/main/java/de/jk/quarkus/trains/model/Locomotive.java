package de.jk.quarkus.trains.model;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;
@Entity
@RegisterForReflection
@Schema(name="Locomotive", description="data required for control of a locomotive") //OpenAPI
public class Locomotive extends PanacheEntity{
	//Id added by panache
	
	//number or other identifier
	@NotBlank(message="identification cannot be blank") //Validation
	@Length(min = 1, max = 20, message="identifier length must be between 1 and 20")//Validation
	@Column(name= "identification", length = 20, nullable = false)//Database
	@Schema(description = "identification, e.g. number or name", minLength = 1, maxLength = 20, required = true, example = "99 5906") //OpenAPI
	public String identification;
	
	//technical identifier model railroad dcc standard (0 ... 9999)
	@NotNull(message="DCC address cannot be empty")//Validation
	@Min(0)//Validation
	@Max(9999)//Validation
	@Schema(description = "DCC decoder address", required = true, example = "59", type=SchemaType.INTEGER) //OpenAPI
	public Integer address;
	
	//Last revision date
	@Past (message="revision date cannot be in the future") //Validation
	@Schema(description = "date of last revision", format = "yyyy-MM-dd", required = false, example = "2020-01-01") //OpenAPI
	@JsonbDateFormat(value = "yyyy-MM-dd")
	public LocalDate revision;
	
	@OneToMany(mappedBy = "locomotive", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	public List<Function> functions;
	
	//Customized queries ...
	public static Locomotive findByAddress(Integer address){
        return find("address", address).firstResult();
    }
	
	public static List<Locomotive> findAllByIdentificationLike(String identification){
        return find("identification LIKE concat('%', :identification, '%')", 
                Parameters.with("identification", identification)).list();
    }
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Locomotive)) {
            return false;
        }

        Locomotive other = (Locomotive) o;

        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
	
}
