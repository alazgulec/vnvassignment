package addressbook.dbsetup.test;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import sun.util.logging.resources.logging;
import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.db.DbSource;
import addressbook.ui.ListEntry;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

public class DBSetupTest {

	private Connection connection;
	private DbSource dbaccess;
	AddressDao addressDao;

	@Before
	public void prepare() throws Exception {
		dbaccess = new DbSource();

		Operation operation = sequenceOf(
				deleteAllFrom("APP.Address"),
				insertInto("APP.Address")
						.columns("LASTNAME", "FIRSTNAME", "MIDDLENAME",
								"PHONE", "EMAIL", "ADDRESS1", "ADDRESS2",
								"CITY", "STATE", "POSTALCODE", "COUNTRY")
						.values("Deans", "Jane", "Fox", "+1-613-1234567",
								"deansjane@uottawa.ca", "900 Kane St.", "1104",
								"Ottawa", "Ontario", "K1NJ1N", "Canada")
						.values("Kashani", "Hossein", "p", "p", "p", "p", "p",
								"p", "p", "p", "p").build());

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		dataSource.setUrl("jdbc:derby:" + "DefaultAddressBook");
		dataSource.setUsername("addressuser");
		dataSource.setPassword("addressuser");
		DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource),
				operation);
		dbSetup.launch();
	}

	@After
	public void tearDown() throws Exception {
		if (addressDao != null) {
			addressDao.disconnect();
		}
	}

	@Test
	public void testGetConnection() {

		addressDao = new AddressDao();
		addressDao.connect();
		dbaccess = (DbSource) addressDao.getDbaccess();
		connection = dbaccess.getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("SELECT * FROM APP.ADDRESS");

			assertTrue(results.next());

			String expected = "Jane";
			String actual = results.getString("FIRSTNAME");
			assertEquals(expected, actual);

			expected = "Deans";
			actual = results.getString("LASTNAME");
			assertEquals(expected, actual);

			expected = "Deans";
			actual = results.getString("LASTNAME");
			assertEquals(expected, actual);

			expected = "900 Kane St.";
			actual = results.getString("ADDRESS1");
			assertEquals(expected, actual);

			expected = "1104";
			actual = results.getString("ADDRESS2");
			assertEquals(expected, actual);

			expected = "Fox";
			actual = results.getString("MIDDLENAME");
			assertEquals(expected, actual);

			expected = "+1-613-1234567";
			actual = results.getString("PHONE");
			assertEquals(expected, actual);

			expected = "deansjane@uottawa.ca";
			actual = results.getString("EMAIL");
			assertEquals(expected, actual);

			expected = "Ottawa";
			actual = results.getString("CITY");
			assertEquals(expected, actual);

			expected = "Ontario";
			actual = results.getString("STATE");
			assertEquals(expected, actual);

			expected = "K1NJ1N";
			actual = results.getString("POSTALCODE");
			assertEquals(expected, actual);

			expected = "Canada";
			actual = results.getString("COUNTRY");
			assertEquals(expected, actual);

			assertTrue(results.next());

			expected = "Hossein";
			actual = results.getString("FIRSTNAME");
			assertEquals(expected, actual);

			expected = "Kashani";
			actual = results.getString("LASTNAME");
			assertEquals(expected, actual);

			expected = "p";
			actual = results.getString("ADDRESS1");
			assertEquals(expected, actual);

			expected = "p";
			actual = results.getString("ADDRESS2");
			assertEquals(expected, actual);

			expected = "p";
			actual = results.getString("CITY");
			assertEquals(expected, actual);

			expected = "p";
			actual = results.getString("STATE");
			assertEquals(expected, actual);

			expected = "p";
			actual = results.getString("POSTALCODE");
			assertEquals(expected, actual);

			expected = "p";
			actual = results.getString("COUNTRY");
			assertEquals(expected, actual);

			assertFalse(results.next());

		} catch (SQLException e) {
			System.out.println("SQL Exception thrown in testGetConnection: " + e.getMessage() + " at SQL State: " + e.getSQLState());
			fail("Unable to create SQL statement.");
		}

	}

	@Test
	public void testSaveRecord() {

		Address address = new Address("added", "added", "e", "e", "e", "e",
				"e", "e", "e", "e");
		address.setCountry("e");

		addressDao = new AddressDao();
		addressDao.connect();
		dbaccess = (DbSource) addressDao.getDbaccess();

		addressDao.saveRecord(address);

		connection = dbaccess.getConnection();
		try {
			PreparedStatement stmtPreparedStatement = connection
					.prepareStatement("SELECT * FROM APP.ADDRESS WHERE LASTNAME= ?");
			stmtPreparedStatement.setString(1, "added");
			ResultSet results = stmtPreparedStatement.executeQuery();

			results.next();

			String expected = "added";
			String actual = results.getString("FIRSTNAME");
			assertEquals(expected, actual);

			expected = "added";
			actual = results.getString("LASTNAME");
			assertEquals(expected, actual);

			expected = "e";
			actual = results.getString("MIDDLENAME");
			assertEquals(expected, actual);

			expected = "e";
			actual = results.getString("ADDRESS1");
			assertEquals(expected, actual);

			expected = "e";
			actual = results.getString("ADDRESS2");
			assertEquals(expected, actual);

			expected = "e";
			actual = results.getString("CITY");
			assertEquals(expected, actual);

			expected = "e";
			actual = results.getString("STATE");
			assertEquals(expected, actual);

			expected = "e";
			actual = results.getString("POSTALCODE");
			assertEquals(expected, actual);

			expected = "e";
			actual = results.getString("COUNTRY");
			assertEquals(expected, actual);
		}

		catch (SQLException e) {
			System.out.println("SQL Exception thrown in testSaveRecord: " + e.getMessage() + " at SQL State: " + e.getSQLState());
			fail("Unable to create SQL statement.");
		}

	}

	@Test
	public void testDeleteRecord() {

		addressDao = new AddressDao();
		addressDao.connect();
		dbaccess = (DbSource) addressDao.getDbaccess();
		int id = 0;
		
		try {
		List<ListEntry> list = addressDao.getListEntries();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getLastName().equals("Kashani")) {
				id = list.get(i).getId();
			}
		}
		addressDao.deleteRecord(id);
		connection = dbaccess.getConnection();
			PreparedStatement stmtPreparedStatement = connection
					.prepareStatement("SELECT * FROM APP.ADDRESS WHERE LASTNAME= ?");
			stmtPreparedStatement.setString(1, "Kashani");
			ResultSet results = stmtPreparedStatement.executeQuery();
			assertFalse(results.next());
		}

		catch (SQLException e) {
			System.out.println("SQL Exception thrown in testDeleteRecord: " + e.getMessage() + " at SQL State: " + e.getSQLState());
			fail("Unable to create SQL statement.");
		}
	}

	@Test
	public void testEditRecord() {

		addressDao = new AddressDao();
		addressDao.connect();
		dbaccess = (DbSource) addressDao.getDbaccess();

		Address address = new Address("AshrafKashani", "Hossein", "p", "p",
				"p", "p", "p", "p", "p", "p");
		address.setCountry("P");

		int id = 0;
		List<ListEntry> list = addressDao.getListEntries();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getLastName().equals("Kashani")) {
				id = list.get(i).getId();
			}
		}
		address.setId(id);

		addressDao.editRecord(address);

		connection = dbaccess.getConnection();

		try {
			PreparedStatement stmtPreparedStatement = connection
					.prepareStatement("SELECT * FROM APP.ADDRESS WHERE LASTNAME= ?");
			stmtPreparedStatement.setString(1, "AshrafKashani");
			ResultSet results = stmtPreparedStatement.executeQuery();

			assertTrue(results.next());

		}

		catch (SQLException e) {
			System.out.println("SQL Exception thrown in testEditRecord: " + e.getMessage() + " at SQL State: " + e.getSQLState());
			fail("Unable to create SQL statement.");
		}

	}

	@Test
	public void testGetListEntry() {

		addressDao = new AddressDao();
		addressDao.connect();
		dbaccess = (DbSource) addressDao.getDbaccess();

		int id = 0;
		List<ListEntry> list = addressDao.getListEntries();

		dbaccess = (DbSource) addressDao.getDbaccess();
		connection = dbaccess.getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("SELECT * FROM APP.ADDRESS");

			assertTrue(results.next());

			String expected = results.getString("FIRSTNAME");
			String actual = list.get(0).getFirstName();
			assertEquals(expected, actual);

			expected = results.getString("LASTNAME");
			actual = list.get(0).getLastName();
			assertEquals(expected, actual);

			expected = results.getString("MIDDLENAME");
			actual = list.get(0).getMiddleName();
			assertEquals(expected, actual);

			assertTrue(results.next());

			expected = list.get(1).getFirstName();
			actual = results.getString("FIRSTNAME");
			assertEquals(expected, actual);

			expected = results.getString("LASTNAME");
			actual = list.get(1).getLastName();
			assertEquals(expected, actual);

			expected = results.getString("MIDDLENAME");
			actual = list.get(1).getMiddleName();
			assertEquals(expected, actual);

			assertFalse(results.next());

		} catch (SQLException e) {
			System.out.println("SQL Exception thrown in testGetListEntry: " + e.getMessage() + " at SQL State: " + e.getSQLState());
			fail("Unable to create SQL statement.");
		}

	}

	@Test
	public void testGetAddress() {

		Address address = new Address("Kashani", "Hossein", "p", "p", "p", "p",
				"p", "p", "p", "p");
		address.setCountry("P");

		Address actualAddress = new Address();

		addressDao = new AddressDao();
		addressDao.connect();

		try {
			int id = 0;
			List<ListEntry> list = addressDao.getListEntries();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getLastName().equals("Kashani")) {
					id = list.get(i).getId();
				}
			}
			address.setId(id);
			actualAddress = addressDao.getAddress(id);
		} catch (Exception e) {
			System.out.println("SQL Exception thrown in testGetAddress: " + e.getMessage() );
		}
		assertEquals(address, actualAddress);

	}

}
