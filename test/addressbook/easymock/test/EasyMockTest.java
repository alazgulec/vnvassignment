package addressbook.easymock.test;

import junit.framework.Assert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.sql.Statement;
import java.sql.Connection;

import org.easymock.*;

import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.db.IDataSource;

public class EasyMockTest {

	private AddressDao addressDao;
	private IDataSource dbaccess;
	private Connection connection;
	private PreparedStatement statement;
    /*private PreparedStatement saveStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement getStatement;
    private PreparedStatement delStatement;*/
    private ResultSet results;
    
    private static final String strSaveAddress =
            "INSERT INTO APP.ADDRESS " +
            "   (LASTNAME, FIRSTNAME, MIDDLENAME, PHONE, EMAIL, ADDRESS1, ADDRESS2, " +
            "    CITY, STATE, POSTALCODE, COUNTRY) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
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
    
    private static final String strGetAddress =
            "SELECT * FROM APP.ADDRESS " +
            "WHERE ID = ?";
    
    private static final String strDeleteAddress =
            "DELETE FROM APP.ADDRESS " +
            "WHERE ID = ?";
    
	@Before
	public void setUp() throws SQLException {
		dbaccess = EasyMock.createMock(IDataSource.class);
		connection = EasyMock.createMock(Connection.class);
		statement = EasyMock.createMock(PreparedStatement.class);
		/*saveStatement = EasyMock.createMock(PreparedStatement.class);
		updateStatement = EasyMock.createMock(PreparedStatement.class);
		getStatement = EasyMock.createMock(PreparedStatement.class);
		delStatement = EasyMock.createMock(PreparedStatement.class);*/
		results = EasyMock.createMock(ResultSet.class);
		addressDao = new AddressDao("Test Address Book");
		addressDao.setDbaccess(dbaccess);
	}
	
	@After
	public void tearDown() throws Exception { 
		EasyMock.verify();
	}
			
	@Test
	public void testConnect_returnsTrue() throws SQLException {
		EasyMock.expect(dbaccess.connect()).andReturn(true);
		EasyMock.expect(dbaccess.getConnection()).andReturn(connection);
	    EasyMock.expect(connection.prepareStatement(strSaveAddress, 1)).andReturn(statement);
	    EasyMock.expect(connection.prepareStatement(strUpdateAddress)).andReturn(statement);
	    EasyMock.expect(connection.prepareStatement(strGetAddress)).andReturn(statement);
	    EasyMock.expect(connection.prepareStatement(strDeleteAddress)).andReturn(statement);    
	    
		EasyMock.replay(dbaccess, connection, statement);
		
		boolean expected = true;
		boolean actual = addressDao.connect();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testConnect_returnsFalse() throws SQLException {
		EasyMock.expect(dbaccess.connect()).andReturn(false);
		EasyMock.expect(dbaccess.getConnection()).andReturn(connection);
	    EasyMock.expect(connection.prepareStatement(strSaveAddress, 1)).andReturn(statement);
	    EasyMock.expect(connection.prepareStatement(strUpdateAddress)).andReturn(statement);
	    EasyMock.expect(connection.prepareStatement(strGetAddress)).andReturn(statement);
	    EasyMock.expect(connection.prepareStatement(strDeleteAddress)).andReturn(statement);    
	    
		EasyMock.replay(dbaccess, connection, statement);
		
		boolean expected = false;
		boolean actual = addressDao.connect();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testConnect_SQLExceptionCaughtAndReturnsFalse() throws SQLException {
		EasyMock.expect(dbaccess.connect()).andReturn(true);
		EasyMock.expect(dbaccess.getConnection()).andReturn(connection);
	    EasyMock.expect(connection.prepareStatement(strSaveAddress, 1)).andThrow(new SQLException(""));
	    
		EasyMock.replay(dbaccess, connection);
		
		boolean expected = false;
		boolean actual = addressDao.connect();
		assertEquals(expected, actual);
	}
   
	@Test
	public void testDisconnect(){
		dbaccess.disconnect();
		EasyMock.expectLastCall();
		EasyMock.replay(dbaccess);
		addressDao.disconnect();
	}
	
	@Test
	public void testSaveRecord() throws SQLException{
		int expected = 999;
		
		addressDao.setStmtSaveNewRecord(statement);
		
		statement.clearParameters();
		EasyMock.expectLastCall();
		
		statement.setString(anyInt(), anyString());
		EasyMock.expectLastCall().times(11);
		
		EasyMock.expect(statement.executeUpdate()).andReturn(111);
        EasyMock.expect(statement.getGeneratedKeys()).andReturn(results);
		
        EasyMock.expect(results.next()).andReturn(true);
		EasyMock.expect(results.getInt(1)).andReturn(expected);
		
		EasyMock.replay(dbaccess, connection, statement, results);
		
		Address address = new Address("","","","","","","","","","", 222);
		int actual = addressDao.saveRecord(address);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSaveRecord_returnMinusOne() throws SQLException{
		int expected = -1;
		
		addressDao.setStmtSaveNewRecord(statement);
		
		statement.clearParameters();
		EasyMock.expectLastCall();
		
		statement.setString(anyInt(), anyString());
		EasyMock.expectLastCall().times(11);
		
		EasyMock.expect(statement.executeUpdate()).andThrow(new SQLException(""));
        
		EasyMock.replay(dbaccess, connection, statement, results);
		
		Address address = new Address("","","","","","","","","","", 222);
		int actual = addressDao.saveRecord(address);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEditRecord() throws SQLException{
		boolean expected = true;
		int id = 123;
		
		addressDao.setStmtUpdateExistingRecord(statement);
		
		statement.clearParameters();
		EasyMock.expectLastCall();
		
		statement.setString(anyInt(), anyString());
		EasyMock.expectLastCall().times(11);
		
		statement.setInt(12, id);
		EasyMock.expectLastCall();
		
		EasyMock.expect(statement.executeUpdate()).andReturn(456);
		
		Address address = new Address("","","","","","","","","","", id);
		boolean actual = addressDao.editRecord(address);
		assertEquals(expected, actual);
	}
	
}
	
