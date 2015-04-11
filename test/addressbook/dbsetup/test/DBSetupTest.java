package addressbook.dbsetup.test;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import addressbook.db.DbSource;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

public class DBSetupTest {
	
	
		 private Connection connection;
		 private DbSource dbaccess;
		 
		 
		 @Before
		    public void prepare() throws Exception {
		  dbaccess = new DbSource();
		  
		  Operation operation =
		             sequenceOf(
		              deleteAllFrom("APP.Address"),
		                 insertInto("APP.Address")
		                  .columns("LASTNAME", "FIRSTNAME", "MIDDLENAME", "PHONE", "EMAIL", "ADDRESS1", "ADDRESS2", "CITY", "STATE", "POSTALCODE", "COUNTRY")
		                  .values("Deans","Jane", "p", "p", "p", "p","p","p","p","p","p" )
		                  .values("Chawla","Shruti", "p", "p", "p", "p","p","p","p","p","p")
		                  .build());
		  
		  DriverManagerDataSource dataSource=new DriverManagerDataSource();
		  dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		  dataSource.setUrl("jdbc:derby:" + "DefaultAddressBook");
		  dataSource.setUsername("addressuser");
		  dataSource.setPassword("addressuser");  
		  DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);

		  dbSetup.launch();
		 }
		 
		 @Test
		 public void testGetConnection() {
		  //int idExpected;
		  //int idActual;
		  String firstNameExpected;
		  String firstNameActual;
		  String lastNameExpected;
		  String lastNameActual;
		  dbaccess.connect();
		  connection = dbaccess.getConnection();
		  try {
		   Statement stmt = connection.createStatement();
		   ResultSet results = stmt.executeQuery("SELECT * FROM APP.ADDRESS");

		   assertTrue(results.next());

		//   idExpected = 11;
		//   idActual = results.getInt("ID");
		//   assertEquals(idExpected, idActual);

		   firstNameExpected = "Jane";
		   firstNameActual = results.getString("FIRSTNAME");
		   assertEquals(firstNameExpected, firstNameActual);

		   lastNameExpected = "Deans";
		   lastNameActual = results.getString("LASTNAME");
		   assertEquals(lastNameExpected, lastNameActual);

		   assertTrue(results.next());

		//   idExpected = 12;
		//   idActual = results.getInt("ID");
		//   assertEquals(idExpected, idActual);

		   firstNameExpected = "Shruti";
		   firstNameActual = results.getString("FIRSTNAME");
		   System.out.println(firstNameActual);
		   assertEquals(firstNameExpected, firstNameActual);

		   lastNameExpected = "Chawla";
		   lastNameActual = results.getString("LASTNAME");
		   assertEquals(lastNameExpected, lastNameActual);

		   assertFalse(results.next());
		  } catch (SQLException e) {
		   fail("Unable to create SQL statement.");
		  }
		  
		 }
	}


