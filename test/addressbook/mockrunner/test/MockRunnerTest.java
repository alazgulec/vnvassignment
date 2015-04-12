package addressbook.mockrunner.test;

import static org.junit.Assert.*;

import java.sql.SQLException;

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

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;

public class MockRunnerTest extends BasicJDBCTestCaseAdapter  {
	
	 private AddressDao addressDao;
	 private Address address;
	
	 private int id = 1;
	 private String lastName = "Ashraf Kashani";
	 private String firstName = "Hossein";
	 private String middleName = "HoCash";
	 private String phone = "6131234567";
	 private String email = "hocash@gmail.com";
	 private String address1 = "900 Paddington St";
	 private String address2 = "1104";
	 private String city = "Ottawa";
	 private String state = "Ontario";
	 private String postalCode = "K2N6X5";
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
	 
	 
	 
	 String sql = "INSERT INTO APP.ADDRESS " +
		     "   (LASTNAME, FIRSTNAME, MIDDLENAME, PHONE, EMAIL, ADDRESS1, ADDRESS2, " +
		     "    CITY, STATE, POSTALCODE, COUNTRY) " +
		     "VALUES (Doe, John, Wolf, 613-700-1234, john.doe@gamil.com, 1234 King Edward, School of Information Technology & Engineering, Ottawa, Ontario, K1N 6N5, Canada))";
}