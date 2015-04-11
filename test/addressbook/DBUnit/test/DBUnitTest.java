package addressbook.DBUnit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.db.DbSource;
import addressbook.db.IDataSource;

public class DBUnitTest {

	private DbSource dbaccess;
	private Connection connection;

	@Before
	public void setUp() throws Exception {
		dbaccess = new DbSource();
		boolean res = dbaccess.connect();
		if(res){
			System.out.println("Connection set!");
		}
		IDatabaseConnection setupConnection = new DatabaseConnection(
				dbaccess.getConnection());
		setupConnection.getConnection().setSchema("APP");

		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		IDataSet dataSet = builder.build(this.getClass()
				.getResource("data.xml"));

		DatabaseOperation.CLEAN_INSERT.execute(setupConnection, dataSet);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		connection.close();
	}

	@Test
	public void testGetConnection() {
		int idExpected;
		int idActual;
		String firstNameExpected;
		String firstNameActual;
		String lastNameExpected;
		String lastNameActual;

		connection = dbaccess.getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("SELECT * FROM ADDRESS");

			assertTrue(results.next());

//			idExpected = 11;
//			idActual = results.getInt("ID");
//			assertEquals(idExpected, idActual);

			firstNameExpected = "test1";
			firstNameActual = results.getString("FIRSTNAME");
			assertEquals(firstNameExpected, firstNameActual);

			lastNameExpected = "test1";
			lastNameActual = results.getString("LASTNAME");
			assertEquals(lastNameExpected, lastNameActual);

			assertTrue(results.next());

//			idExpected = 12;
//			idActual = results.getInt("ID");
//			assertEquals(idExpected, idActual);

			firstNameExpected = "test2";
			firstNameActual = results.getString("FIRSTNAME");
			System.out.println(firstNameActual);
			assertEquals(firstNameExpected, firstNameActual);

			lastNameExpected = "test2";
			lastNameActual = results.getString("LASTNAME");
			assertEquals(lastNameExpected, lastNameActual);

			assertFalse(results.next());
		} catch (SQLException e) {
			fail("Unable to create SQL statement.");
		}
	}
	
	@Test
	 public void testSaveRecord( ) throws Exception {
		connection = dbaccess.getConnection();
		try {
			Address address=new Address("test3", "test3", "e",  "e",  "e",  "e",  "e",  "e",  "e",  "e");
			address.setCountry("e");
			AddressDao aDao=new AddressDao();
			aDao.connect();
			aDao.saveRecord(address);
			
			IDataSet expectedDataSet;
			FlatXmlDataSetBuilder data = new FlatXmlDataSetBuilder();
			expectedDataSet = data.build( this.getClass( ).getResource(
					"AfterSave.xml" ) );
			ITable expectedTable = expectedDataSet
					.getTable( "ADDRESS" );

			IDataSet databaseDataSet = new DatabaseConnection( dbaccess
					.getConnection( ) ).createDataSet( );
			ITable actualTable = databaseDataSet
					.getTable( "ADDRESS");
			ITable filteredTable = DefaultColumnFilter.includedColumnsTable(actualTable, 
					expectedTable.getTableMetaData().getColumns());
			Assertion.assertEquals( expectedTable, filteredTable );
			
		}
		
		catch(SQLException e) 
		{
			
		}
	 }

}
