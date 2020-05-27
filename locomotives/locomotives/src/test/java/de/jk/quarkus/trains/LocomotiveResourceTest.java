package de.jk.quarkus.trains;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;

import de.jk.quarkus.trains.model.Locomotive;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class LocomotiveResourceTest {

	@Test
	@Transactional
	public void testPanacheEntity() {

		//Check preloaded data
		List<Locomotive> listLocomotives = Locomotive.listAll();
		assertEquals(1,listLocomotives.get(0).address);
		
		//Add new record
		Locomotive newLocomotive = new Locomotive();
		newLocomotive.address = 2;
		newLocomotive.identification = "99 6001";
		newLocomotive.revision = LocalDate.of(1985, Month.JANUARY, 1);
		newLocomotive.persist();	
		
		//Find all records like identification
		List<Locomotive> myLocomotiveList = Locomotive.findAllByIdentificationLike("99");
		assertThat("listsize", myLocomotiveList.size()>1);

		//Find all records with paging
		PanacheQuery<Locomotive> pagedLocomotiveList = Locomotive.findAll();
		// make it use pages of 25 entries at a time
		pagedLocomotiveList.page(Page.ofSize(25));
		assertEquals(1, pagedLocomotiveList.pageCount());
		// get the first page
		List<Locomotive> firstPage = pagedLocomotiveList.list();
		assertEquals(2, firstPage.size());
		// get the xxx page
		List<Locomotive> page2 = pagedLocomotiveList.page(Page.of(2, 25)).list();
		assertEquals(0, page2.size());
		
		//Find first locomotive with certain address
		Locomotive myLocomotive = Locomotive.findByAddress(2);
		assertEquals("99 6001", myLocomotive.identification);
		
		//Update Locomotive
		Long id = myLocomotive.id;
	    myLocomotive.address=3;
		myLocomotive.persist();
		Locomotive updatedLocomotive = Locomotive.findById(id);
		assertEquals(3, updatedLocomotive.address);
		
		//Delete Locomotive
		Locomotive.deleteById(id);
		Locomotive deletedLocomotive = Locomotive.findById(id);
		assertEquals(null, deletedLocomotive);
	}
	
	
	@Test
    public void testHelloEndpoint() {
        given()
          .when().get("/locomotives")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}