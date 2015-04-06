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
	
	@Before
	public void setUp() {
		mock = EasyMock.createMock(IDataSource.class);
		addressDao = new AddressDao();
		addressDao.setDbaccess(mock);
		connectionMock = EasyMock.createMock(Connection.class);	
		statementMock = EasyMock.createMock(PreparedStatement.class);
	}
	
	@After
	public void tearDown() {
		EasyMock.verify(mock);
	}
	
	@Test
	public void testConnect() throws SQLException{
		Connection dbConnection = null;
		EasyMock.expect(mock.getConnection()).andReturn(connectionMock);
		EasyMock.expect(mock.connect()).andReturn(true);
		EasyMock.replay(mock);
		assertTrue(addressDao.connect());
	}
	
}
