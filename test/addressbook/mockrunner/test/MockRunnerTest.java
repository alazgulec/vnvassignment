package addressbook.mockrunner.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.ui.ListEntry;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;

public class MockRunnerTest extends BasicJDBCTestCaseAdapter  {
	
	 private AddressDao addressDao;
	 private Address address;
	
	 int id = 1;
	 private String lastName = "Doe";
	 private String firstName = "John";
	 private String middleName = "Wolf";
	 private String phone = "613-700-1234";
	 private  String email = "john.doe@gamil.com";
	 private String address1 = "1234 King Edward";
	 private String address2 = "School of Information Technology & Engineering";
	 private String city = "Ottawa";
	 private String state = "Ontario";
	 private String postalCode = "K1N 6N5";
	 private String country = "Canada";

	 private MockResultSet results; 
	 PreparedStatementResultSetHandler stmntResultHandler;
	 
	 private static final String strGetAddress =
			 "SELECT * FROM APP.ADDRESS " +
		     "WHERE ID = ?";
		    
	 private static final String strSaveAddress =
			 "INSERT INTO APP.ADDRESS " +
		     "   (LASTNAME, FIRSTNAME, MIDDLENAME, PHONE, EMAIL, ADDRESS1, ADDRESS2, " +
		     "    CITY, STATE, POSTALCODE, COUNTRY) " +
		     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		    	    
	 private static final String strGetListEntries =
			 "SELECT ID, LASTNAME, FIRSTNAME, MIDDLENAME FROM APP.ADDRESS "  +
		     "ORDER BY LASTNAME ASC";
		    
	 private static final String strUpdateAddress =
			 "UPDATE APP.ADDRESS " +
		     "SET LASTNAME = ?, " +
		     "    FIRSTNAME = ?, " +
		     "    MIDDLENAME = ?, " +
		     "    PHONE = ?, " +
		     "    EMAIL = ?, " +
		     "    ADDRESS1 = ?, " +
		     "    ADDRESS2 = ?, " +
		     "    CITY = ?, " +
		     "    STATE = ?, " +
		     "    POSTALCODE = ?, " +
		     "    COUNTRY = ? " +
		     "WHERE ID = ?";
		    
	 private static final String strDeleteAddress =
			 "DELETE FROM APP.ADDRESS " +
		     "WHERE ID = ?";
		   
	 @Before
	 public void setUp() throws Exception {
		 super.setUp();
		 
		 JDBCMockObjectFactory mockFactory = getJDBCMockObjectFactory();
		 MockConnection connection = getJDBCMockObjectFactory().getMockConnection();
		    
		 stmntResultHandler = connection.getPreparedStatementResultSetHandler();	     
		 results = stmntResultHandler.createResultSet();
		 
		 addressDao = new AddressDao();
		 addressDao.connect();
		 address = new Address(lastName, firstName, middleName, phone, email, address1, address2, city, state, postalCode, country, id);
	 }
	
	 @After
	 public void tearDown() throws Exception { 
		 super.tearDown();
	 }
	
	 @Test
	 public void testSaveRecord() throws Exception {
		 int id = 1;
		 
		 results.addRow(new Object[] { id, lastName, firstName, middleName, phone, email, address1, address2, city, state, postalCode, country });
		 stmntResultHandler.prepareGlobalGeneratedKeys(results);
		 
		 int expected = id;
		 int actual = addressDao.saveRecord(address);
		 assertEquals(expected, actual);
		 
		 verifyPreparedStatementPresent(strSaveAddress);
		 verifySQLStatementExecuted(strSaveAddress);
		 verifySQLStatementParameter(strSaveAddress, 0, 1, lastName);
		 verifySQLStatementParameter(strSaveAddress, 0, 2, firstName);
		 verifySQLStatementParameter(strSaveAddress, 0, 3, middleName);
		 verifySQLStatementParameter(strSaveAddress, 0, 4, phone);
		 verifySQLStatementParameter(strSaveAddress, 0, 5, email);
		 verifySQLStatementParameter(strSaveAddress, 0, 6, address1);
		 verifySQLStatementParameter(strSaveAddress, 0, 7, address2);
		 verifySQLStatementParameter(strSaveAddress, 0, 8, city);
		 verifySQLStatementParameter(strSaveAddress, 0, 9, state);
		 verifySQLStatementParameter(strSaveAddress, 0, 10, postalCode);
		 verifySQLStatementParameter(strSaveAddress, 0, 11, country);
	 }
	 
//	 @Test
//	 public void testSaveRecord_returnMinusOne() throws Exception{
//		 int expected = -1;
//			
//		 stmntResultHandler.prepareThrowsSQLException(strSaveAddress);
//		 
//		 int actual = addressDao.saveRecord(address);
//		 assertEquals(expected, actual);
//	 
//		 verifyPreparedStatementPresent(strSaveAddress);
//		 verifySQLStatementNotExecuted(strSaveAddress);
//	 }
	 
	 @Test
	 public void testEditRecord() throws SQLException {
		 int id = 1;
		 results.addRow(new Object[] { id, "Gulec", "Alaz", middleName, phone, email, address1, address2, city, state, postalCode, country });
		 String changedLastName = "Gulec";
		 String changedFirstName = "Alaz";
		 results.getRow(1).set(1, changedLastName);
		 results.getRow(1).set(2, changedFirstName);
		 stmntResultHandler.prepareGlobalGeneratedKeys(results);
		 addressDao.saveRecord(address);
		 address.setLastName("Gulec");
		 address.setFirstName("Alaz");
		 boolean expected = true;
		 boolean actual = addressDao.editRecord(address);
		 assertEquals(expected, actual);

		 verifyPreparedStatementPresent(strUpdateAddress);
		 verifySQLStatementExecuted(strUpdateAddress);
		 verifySQLStatementParameter(strUpdateAddress, 0, 1, changedLastName);
		 verifySQLStatementParameter(strUpdateAddress, 0, 2, changedFirstName);
		 verifySQLStatementParameter(strUpdateAddress, 0, 3, middleName);
		 verifySQLStatementParameter(strUpdateAddress, 0, 4, phone);
		 verifySQLStatementParameter(strUpdateAddress, 0, 5, email);
		 verifySQLStatementParameter(strUpdateAddress, 0, 6, address1);
		 verifySQLStatementParameter(strUpdateAddress, 0, 7, address2);
		 verifySQLStatementParameter(strUpdateAddress, 0, 8, city);
		 verifySQLStatementParameter(strUpdateAddress, 0, 9, state);
		 verifySQLStatementParameter(strUpdateAddress, 0, 10, postalCode);
		 verifySQLStatementParameter(strUpdateAddress, 0, 11, country);
	 }
	 
	 @Test
	 public void testDeleteRecord() throws SQLException {
		 int id = 1;
		 results.addRow(new Object[] { id, lastName, firstName, middleName, phone, email, address1, address2, city, state, postalCode, country });
		 addressDao.saveRecord(address);
		 boolean expected = true;
		 boolean actual = addressDao.deleteRecord(id);
		 assertEquals(expected, actual);
		 verifyPreparedStatementPresent(strDeleteAddress);
		 verifySQLStatementExecuted(strDeleteAddress);
		 verifySQLStatementParameter(strDeleteAddress, 0, 1, id);
		 verifySQLStatementParameter(strDeleteAddress, 0, 2, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 3, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 4, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 5, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 6, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 7, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 8, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 9, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 10, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 11, null);
	 }
	 
//	 @Test
//	 public void testGetAddress() {
//		 results.addRow(new Object[] { id, lastName, firstName, middleName, phone, email, address1, address2, city, state, postalCode, country });
//		 stmntResultHandler.prepareGlobalGeneratedKeys(results);
//		 addressDao.saveRecord(address);
//		 Address retrievedAddress = addressDao.getAddress(0);
//		 int expected = id;
//		 int actual = retrievedAddress.getId();
//		 assertEquals(expected, actual);
//		 verifyPreparedStatementPresent(strGetAddress);
//		 verifySQLStatementExecuted(strGetAddress);
//		 verifySQLStatementParameter(strGetAddress, 0, 1, lastName);
//		 verifySQLStatementParameter(strGetAddress, 0, 2, firstName);
//		 verifySQLStatementParameter(strGetAddress, 0, 3, middleName);
//		 verifySQLStatementParameter(strGetAddress, 0, 4, phone);
//		 verifySQLStatementParameter(strGetAddress, 0, 5, email);
//		 verifySQLStatementParameter(strGetAddress, 0, 6, address1);
//		 verifySQLStatementParameter(strGetAddress, 0, 7, address2);
//		 verifySQLStatementParameter(strGetAddress, 0, 8, city);
//		 verifySQLStatementParameter(strGetAddress, 0, 9, state);
//		 verifySQLStatementParameter(strGetAddress, 0, 10, postalCode);
//		 verifySQLStatementParameter(strGetAddress, 0, 11, country);
//	 }
	 
//	 @Test
//	 public void testGetListEntries() {
//		 results.addRow(new Object[] { id, lastName, firstName, middleName, phone, email, address1, address2, city, state, postalCode, country });
//		 stmntResultHandler.prepareGlobalGeneratedKeys(results);
//		 addressDao.saveRecord(address);
//		 List<ListEntry> listEntries = addressDao.getListEntries();
//		 int entryId = listEntries.get(0).getId();
//		 verifyPreparedStatementPresent(strGetListEntries);
//		 verifySQLStatementExecuted(strGetListEntries);
//		 verifySQLStatementParameter(strGetListEntries, 0, 1, lastName);
//		 verifySQLStatementParameter(strGetListEntries, 0, 2, firstName);
//		 verifySQLStatementParameter(strGetListEntries, 0, 3, middleName);
//		 verifySQLStatementParameter(strGetListEntries, 0, 4, phone);
//		 verifySQLStatementParameter(strGetListEntries, 0, 5, email);
//		 verifySQLStatementParameter(strGetListEntries, 0, 6, address1);
//		 verifySQLStatementParameter(strGetListEntries, 0, 7, address2);
//		 verifySQLStatementParameter(strGetListEntries, 0, 8, city);
//		 verifySQLStatementParameter(strGetListEntries, 0, 9, state);
//		 verifySQLStatementParameter(strGetListEntries, 0, 10, postalCode);
//		 verifySQLStatementParameter(strGetListEntries, 0, 11, country);
//	 }
}