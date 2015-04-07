package addressbook.easymock.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.ConnectionEvent;

import org.apache.derby.iapi.sql.ResultSet;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import addressbook.db.AddressDao;
import addressbook.db.IAddressDao;
import addressbook.db.IDataSource;

public class EasyMockTest {
	
	private AddressDao addressDao;
	private IDataSource mock;
	private Connection connectionMock;
	private PreparedStatement statementMock;
	private ResultSet resultSetMock; 
	String strGetAddress;
	String strSaveAddress;
	String strGetListEntries;
	String strUpdateAddress;
	String strDeleteAddress;
	
	@Before
	public void setUp() {
		mock = EasyMock.createNiceMock(IDataSource.class);
		addressDao = new AddressDao();
		addressDao.setDbaccess(mock);
		connectionMock = EasyMock.createMock(Connection.class);	
		statementMock = EasyMock.createMock(PreparedStatement.class);
		
		strGetAddress =
	            "SELECT * FROM APP.ADDRESS " +
	            "WHERE ID = ?";
	    
	     strSaveAddress =
	            "INSERT INTO APP.ADDRESS " +
	            "   (LASTNAME, FIRSTNAME, MIDDLENAME, PHONE, EMAIL, ADDRESS1, ADDRESS2, " +
	            "    CITY, STATE, POSTALCODE, COUNTRY) " +
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    
	     strGetListEntries =
	            "SELECT ID, LASTNAME, FIRSTNAME, MIDDLENAME FROM APP.ADDRESS "  +
	            "ORDER BY LASTNAME ASC";
	    
	    strUpdateAddress =
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
	    
	     strDeleteAddress =
	            "DELETE FROM APP.ADDRESS " +
	            "WHERE ID = ?";
		
	}
	
	@After
	public void tearDown() {
	//	EasyMock.verify(mock);
	//	EasyMock.verify(mock2);
	}
	
	@Test
	public void testConnect() throws SQLException{
		 
		
	    
		
		
		EasyMock.expect(mock.getConnection()).andReturn(connectionMock);
		EasyMock.expect(mock.connect()).andReturn(true);
		
		EasyMock.expect(connectionMock.prepareStatement(strSaveAddress, 1)).andReturn(statementMock);
		EasyMock.expect(connectionMock.prepareStatement(strUpdateAddress)).andReturn(statementMock);
		EasyMock.expect(connectionMock.prepareStatement(strGetAddress)).andReturn(statementMock);
		EasyMock.expect(connectionMock.prepareStatement(strDeleteAddress)).andReturn(statementMock);
		
		
		EasyMock.replay(mock);
		EasyMock.replay(connectionMock);
		
		boolean result=	addressDao.connect();
		
		assertTrue(result);
		EasyMock.verify(mock);
		EasyMock.verify(connectionMock);
		
	}
	
}
