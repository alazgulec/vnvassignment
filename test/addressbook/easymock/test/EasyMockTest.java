package addressbook.easymock.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.ConnectionEvent;

import org.apache.derby.iapi.sql.ResultSet;
import org.apache.derby.impl.store.raw.data.RecordId;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.db.IAddressDao;
import addressbook.db.IDataSource;

public class EasyMockTest {

	private AddressDao addressDao;
	private IDataSource mock;
	private Connection connectionMock;
	private PreparedStatement statementMock;
	private ResultSet resultSetMock;
	private Address addressMock;
	private RecordId recordMock;

	String strGetAddress = "SELECT * FROM APP.ADDRESS " + "WHERE ID = ?";

	String strSaveAddress = "INSERT INTO APP.ADDRESS "
			+ "   (LASTNAME, FIRSTNAME, MIDDLENAME, PHONE, EMAIL, ADDRESS1, ADDRESS2, "
			+ "    CITY, STATE, POSTALCODE, COUNTRY) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	String strGetListEntries = "SELECT ID, LASTNAME, FIRSTNAME, MIDDLENAME FROM APP.ADDRESS "
			+ "ORDER BY LASTNAME ASC";

	String strUpdateAddress = "UPDATE APP.ADDRESS " + "SET LASTNAME = ?, "
			+ "    FIRSTNAME = ?, " + "    MIDDLENAME = ?, "
			+ "    PHONE = ?, " + "    EMAIL = ?, " + "    ADDRESS1 = ?, "
			+ "    ADDRESS2 = ?, " + "    CITY = ?, " + "    STATE = ?, "
			+ "    POSTALCODE = ?, " + "    COUNTRY = ? " + "WHERE ID = ?";

	String strDeleteAddress = "DELETE FROM APP.ADDRESS " + "WHERE ID = ?";

	
	@Before
	public void setUp() throws SQLException {
		mock = EasyMock.createNiceMock(IDataSource.class);
		addressDao = new AddressDao();
		addressDao.setDbaccess(mock);
		connectionMock = EasyMock.createMock(Connection.class);
		statementMock = EasyMock.createMock(PreparedStatement.class);		
		
		EasyMock.expect(mock.getConnection()).andReturn(connectionMock);
		EasyMock.expect(mock.connect()).andReturn(true);

		EasyMock.expect(connectionMock.prepareStatement(strSaveAddress, 1))
				.andReturn(statementMock);
		EasyMock.expect(connectionMock.prepareStatement(strUpdateAddress))
				.andReturn(statementMock);
		EasyMock.expect(connectionMock.prepareStatement(strGetAddress))
				.andReturn(statementMock);
		EasyMock.expect(connectionMock.prepareStatement(strDeleteAddress))
				.andReturn(statementMock);

		EasyMock.replay(mock);
		EasyMock.replay(connectionMock);

		addressDao.connect();
		
		EasyMock.verify(mock);
		EasyMock.verify(connectionMock);

	}

	@After
	public void tearDown() {
		// EasyMock.verify(mock);
		// EasyMock.verify(mock2);
	}

//	@Test
//	public void testConnect() throws SQLException {
//
//	}

	@Test
	public void testEditRecord() throws SQLException {

		Address addressMock = new Address();
		addressMock.setAddress1("asd");
		addressMock.setAddress2("something");
		addressMock.setCity("Ottawa");
		addressMock.setLastName("Ashraf Kashani");
		addressMock.setFirstName("Hossein");
		addressMock.setPhone("123");
		addressMock.setPostalCode("123");
		addressMock.setId(1);
		addressMock.setState("123");
		
		

		boolean result = addressDao.editRecord(addressMock);
		assertTrue(result);
		
		EasyMock.verify(mock);
		EasyMock.verify(connectionMock);

	}

}
