package addressbook.DBUnit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import addressbook.db.DbSource;
import addressbook.db.IDataSource;

public class DBUnitTest {

	private DbSource dbaccess;
	private Connection connection;
	

	
	
	@Before
	public void setUp( ) throws Exception {
		dbaccess=new DbSource();
		boolean res = dbaccess.connect();
		
		IDatabaseConnection setupConnection = new DatabaseConnection( dbaccess.getConnection() );
		
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		IDataSet dataSet = builder.build(this.getClass( ).getResource( "data.xml"));
		
		DatabaseOperation.CLEAN_INSERT.execute( setupConnection, dataSet );
		setupConnection.close( );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown( ) throws Exception {
		connection.close();
	}
	
	@Test
	public void testGetConnection( ) {
		int idExpected;
		int idActual;
		String firstNameExpected;
		String firstNameActual;
		String lastNameExpected;
		String lastNameActual;

		connection = dbaccess.getConnection( );
		try {
			Statement stmt = connection.createStatement( );
			ResultSet results = stmt.executeQuery( "SELECT * FROM APP.ADDRESS" );

			assertTrue( results.next( ) ); 

			idExpected = 11;
		    idActual = results.getInt("ID");
			assertEquals( idExpected, idActual );

			firstNameExpected = "test1";
			firstNameActual = results.getString("FIRSTNAME");
			assertEquals( firstNameExpected, firstNameActual );

			lastNameExpected = "test1";
			lastNameActual = results.getString("LASTNAME");
			assertEquals( lastNameExpected, lastNameActual );

			assertTrue( results.next( ) );

			idExpected = 12;
		    idActual = results.getInt("ID");
			assertEquals( idExpected, idActual );

			firstNameExpected = "test2";
			firstNameActual = results.getString("FIRSTNAME");
			assertEquals( firstNameExpected, firstNameActual );

			lastNameExpected = "test2";
			lastNameActual = results.getString("LASTNAME");
			assertEquals( lastNameExpected, lastNameActual );

			assertFalse( results.next( ) );
		}
		catch ( SQLException e ) {
			fail( "Unable to create SQL statement." );
		}
	}
	
}
