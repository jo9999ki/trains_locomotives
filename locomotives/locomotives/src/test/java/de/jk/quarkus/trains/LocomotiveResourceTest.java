package de.jk.quarkus.trains;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.jk.quarkus.trains.model.Locomotive;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class LocomotiveResourceTest {

	private static long identifier;
	
	@Test
	@Transactional
	@Order(1)
	public void testPanacheEntityList() {
		//Check preloaded data
		List<Locomotive> listLocomotives = Locomotive.listAll();
		assertEquals(9,listLocomotives.get(0).address);		
	}
	
	@Test
	@Transactional
	@Order(2)
	public void testPanacheAddNewRecordAndFindByAddress() {
		//Add new record
		Locomotive newLocomotive = new Locomotive();
		newLocomotive.id= null;
		newLocomotive.address = 100;
		newLocomotive.identification = "99 9999";
		newLocomotive.revision = LocalDate.of(1985, Month.JANUARY, 1);
		newLocomotive.persist();	
		
		//Find first locomotive with certain address
		Locomotive myLocomotive = Locomotive.findByAddress(100);
		assertEquals("99 9999", myLocomotive.identification);
	}
	
	@Test
	@Transactional
	@Order(3)
	public void testPanacheFindIdentificationLike() {
		//Find all records like identification
		List<Locomotive> myLocomotiveList = Locomotive.findAllByIdentificationLike("99");
		assertThat("listsize", myLocomotiveList.size()>1);		
	}

	@Test
	@Transactional
	@Order(4)
	public void testPanacheFindAllRecordsWithPaging() {
		//Find all records with paging
		PanacheQuery<Locomotive> pagedLocomotiveList = Locomotive.findAll();
		// make it use pages of 25 entries at a time
		pagedLocomotiveList.page(Page.ofSize(25));
		assertEquals(1, pagedLocomotiveList.pageCount());
		// get the first page
		List<Locomotive> firstPage = pagedLocomotiveList.list();
		assertThat("listsize", firstPage.size() > 0);		
		// get the xxx page
		List<Locomotive> page2 = pagedLocomotiveList.page(Page.of(2, 25)).list();
		assertEquals(0, page2.size());
	}

	@Test
	@Transactional
	@Order(5)
	public void testPanacheUpdateRecord() {
		//Find first locomotive with certain address
		Locomotive myLocomotive = Locomotive.findByAddress(100);
		assertEquals("99 9999", myLocomotive.identification);
		
		//Update Locomotive
		Long id = myLocomotive.id;
	    myLocomotive.address=101;
		myLocomotive.persist();
		Locomotive updatedLocomotive = Locomotive.findById(id);
		assertEquals(101, updatedLocomotive.address);
	}
	
	@Test
	@Transactional
	@Order(6)
	public void testPanacheDeleteRecord() {
		//Find first locomotive with certain address
		Locomotive myLocomotive = Locomotive.findByAddress(101);
		assertEquals("99 9999", myLocomotive.identification);
				
		//Delete Locomotive
		Long id = myLocomotive.id;
		Locomotive.deleteById(id);
		Locomotive deletedLocomotive = Locomotive.findById(id);
		assertEquals(null, deletedLocomotive);
	}
		
	@Test
	@Order(20)
    public void testRESTGetAll() {
        given()
          .when().get("/locomotives")
          .then()
             .statusCode(OK.getStatusCode())
             .body("id", notNullValue())
         	 .body("address", notNullValue());
    }

	@Test
	@Order(21)
    public void testRESTPost() {
		//With RestEasy-JSON-B JSON Deserialization error for LocalDate when creating Input with Locomotive bean, instead use JSON string with correct
        ValidatableResponse response = given().contentType("application/json")
        		.body("{\"id\":2,\"address\":2,\"identification\":\"99 999\",\"revision\":\"2020-01-01\"}")
        		//.body(locomotive)
                .when().post("/locomotives")
                .then()
	                //.log().body()
	                .statusCode(CREATED.getStatusCode())
                	.body("id", notNullValue())
                	.body("address", equalTo(2));

        LocomotiveResourceTest.identifier = Long.parseLong(response.extract().body().
        		jsonPath().get("id").toString());
        System.out.println(LocomotiveResourceTest.identifier);
        assertEquals(true,true);
    }

	@Test
	@Order(22)
    public void testRESTPostInputValidationMandatory() {
        Locomotive locomotive = new Locomotive();
        locomotive.address=null;
        locomotive.identification=null;
        locomotive.revision = null;
        
        ValidatableResponse response = given().contentType("application/json").body(locomotive)
                .when().post("/locomotives")
                .then()
	                //.log().body()
	                .statusCode(BAD_REQUEST.getStatusCode())
	                .body("errorList.findAll {it.code == \"400001\" && it.parameter == \"add.locomotive.identification\" && it.value == \"\"}.message",  
	                		hasItem("identification cannot be blank"))
        			.body("errorList.findAll {it.code == \"400001\" && it.parameter == \"add.locomotive.address\" && it.value == \"\"}.message",  
        					hasItem("DCC address cannot be empty"));
        
    }

   @Test
   @Order(25)
    public void testRestPut() {
        ValidatableResponse response = given().contentType("application/json")
        		//.body(locomotive)
        		.body("{\"id\":"+ LocomotiveResourceTest.identifier + ",\"address\":93,\"identification\":\"99 998\",\"revision\":\"1986-01-01\"}")
                .when().put("/locomotives")
                .then()
                	//.log().body()
                	.statusCode(OK.getStatusCode())
                	//.body("id", is(book.id)) 
                	//-> this doesn't work for long or double values. Need to use JSON Path after
                	.body("address", equalTo(93));
        
        Long id = Long.parseLong(response.extract().body().
        		jsonPath().get("id").toString());
        assertEquals(id, (Long) LocomotiveResourceTest.identifier);
    }
   
   @Test
   @Order(29)
   public void testRestDelete() {
	   given()
            .when().delete("/locomotives/"+ LocomotiveResourceTest.identifier)
            .then().statusCode(NO_CONTENT.getStatusCode());
   }
  
   @Test
   @Order(40)
   void testHealthCheck() {
       given()
           .when().get("/health")
           .then()
           .statusCode(OK.getStatusCode());
   }
}