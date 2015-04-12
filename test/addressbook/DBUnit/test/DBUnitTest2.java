package addressbook.DBUnit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
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
import addressbook.ui.ListEntry;

public class DBUnitTest2 {

	private DbSource dbaccess;
	private Connection connection;

	@Before
	public void setUp() throws Exception {
		dbaccess = new DbSource();
		boolean res = dbaccess.connect();
		if (res) {
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
		//connection.close()
		dbaccess.disconnect();
	}

	@Test
	public void testDeleteRecord() throws SQLException, DatabaseUnitException {
		AddressDao aDao = new AddressDao();
		aDao.connect();
		int id = 0;
		List<ListEntry> list = aDao.getListEntries();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getLastName().equals("test2")) {
				id = list.get(i).getId();
			}
		}
		aDao.deleteRecord(id);
		IDataSet expectedDataSet;
		FlatXmlDataSetBuilder data = new FlatXmlDataSetBuilder();
		expectedDataSet = data.build(this.getClass().getResource(
				"AfterDelete.xml"));
		ITable expectedTable = expectedDataSet.getTable("ADDRESS");

		IDataSet databaseDataSet = new DatabaseConnection(
				dbaccess.getConnection()).createDataSet();
		ITable actualTable = databaseDataSet.getTable("ADDRESS");
		ITable filteredTable = DefaultColumnFilter.includedColumnsTable(
				actualTable, expectedTable.getTableMetaData().getColumns());
		Assertion.assertEquals(expectedTable, filteredTable);
	}
	
	@Test
	public void getListEntries() {
		AddressDao aDao = new AddressDao();
		aDao.connect();
		List<ListEntry> list = aDao.getListEntries();
		String firstNameExpected = "test1";
		String firstNameActual = list.get(0).getFirstName();
		assertEquals(firstNameExpected, firstNameActual);

		String lastNameExpected = "test1";
		String lastNameActual = list.get(0).getLastName();
		assertEquals(lastNameExpected, lastNameActual);

		firstNameExpected = "test2";
		firstNameActual = list.get(1).getFirstName();
		System.out.println(firstNameActual);
		assertEquals(firstNameExpected, firstNameActual);

		lastNameExpected = "test2";
		lastNameActual = list.get(1).getLastName();
		assertEquals(lastNameExpected, lastNameActual);
	}
	
	@Test
	public void testEditRecord() {
		connection = dbaccess.getConnection();
		try {
			AddressDao aDao=new AddressDao();
			aDao.connect();
			List<ListEntry> list = aDao.getListEntries();
			int id = list.get(0).getId();
			Address address=new Address("test3", "test3", "e",  "e",  "e",  "e",  "e",  "e",  "e",  "e");
			address.setCountry("e");
			address.setId(id);
			aDao.editRecord(address);
			IDataSet expectedDataSet;
			FlatXmlDataSetBuilder data = new FlatXmlDataSetBuilder();
			expectedDataSet = data.build( this.getClass( ).getResource(
					"AfterUpdate.xml" ) );
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
			
		} catch (DataSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseUnitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	@Test
	public void testGetAddress() {
		AddressDao aDao=new AddressDao();
		aDao.connect();
		List<ListEntry> list = aDao.getListEntries();
		int id = list.get(0).getId();
		Address address = aDao.getAddress(id);
		String firstNameExpected = address.getFirstName();
		String firstNameActual = "test1";
		assertEquals(firstNameExpected, firstNameActual);
		String lastNameExpected = "test1";
		String lastNameActual = address.getLastName();
		assertEquals(lastNameExpected, lastNameActual);
	}
	
	@Test
	public void testSaveRecordFail() {
		AddressDao aDao=new AddressDao();
		aDao.connect();
		List<ListEntry> list = aDao.getListEntries();
		int id = list.get(0).getId();
		Address address=new Address();
		int result = aDao.saveRecord(address);
		System.out.println(result);
	}
	
}
