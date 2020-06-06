package de.jk.quarkus.trains.model;
import java.util.List;
import java.util.Objects;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;
@Entity
@RegisterForReflection
@Schema(name="Function", description="Single function of a locomotive") //OpenAPI
public class Function extends PanacheEntity{
	//Id added by panache
	
	//number or other identifier
	@NotBlank(message="name cannot be blank") //Validation
	@Length(min = 1, max = 30, message="name length must be between 1 and 30")//Validation
	@Column(name= "name", length = 30, nullable = false)//Database
	@Schema(description = "name", minLength = 1, maxLength = 30, required = true, example = "Luftpumpe") //OpenAPI
	public String name;

	//dcc number
	@NotNull
	public Integer dccnumber;
	
	//Picture to be connected in user frontend
	public String imageurl;

	@ManyToOne
    @JsonbTransient
	public Locomotive locomotive;

	//Customized queries ...
	public static List<Function> findAllByNameLike(String name){
        return find("identification LIKE concat('%', :identification, '%')", 
                Parameters.with("name", name)).list();
    }
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Function)) {
            return false;
        }

        Function other = (Function) o;

        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
