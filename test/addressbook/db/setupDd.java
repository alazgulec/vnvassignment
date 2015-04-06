package addressbook.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class setupDd {
	private static final String addAddress =
           "INSERT INTO APP.ADDRESS " +
           "   (LASTNAME, FIRSTNAME, MIDDLENAME, PHONE, EMAIL, ADDRESS1, ADDRESS2, " +
           "    CITY, STATE, POSTALCODE, COUNTRY) " +
           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	IDataSource dbaccess;
	
	private Connection connection;
	
	@Before
	public void setUp() throws Exception {
		dbaccess = new DbSource();
		dbaccess.connect();
		connection = dbaccess.getConnection();
	}
	
	@Test
	public void testAddAdresses () throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(addAddress, Statement.RETURN_GENERATED_KEYS);
	    stmt.setString(1, "Marc");
        stmt.setString(2, "Messier");
        stmt.setString(3, "Donald");
        stmt.setString(4, "613-555-6767");
        stmt.setString(5, "mmsessier@hoho.com");
        stmt.setString(6, "75 Rue Baule");
        stmt.setString(7, "Secteur D");
        stmt.setString(8, "Ottawa");
        stmt.setString(9, "Ontario");
        stmt.setString(10, "K1K1K1");
        stmt.setString(11, "Canada");
        stmt.executeUpdate();
        // 
	    stmt.setString(1, "Santa");
        stmt.setString(2, "Claus");
        stmt.setString(3, "");
        stmt.setString(4, "999");
        stmt.setString(5, "santa@northpole.ca");
        stmt.setString(6, "1 Freezing");
        stmt.setString(7, "");
        stmt.setString(8, "North Pole");
        stmt.setString(9, "");
        stmt.setString(10, "H0H0H0");
        stmt.setString(11, "Canada");
        stmt.executeUpdate();
	}
	
	@After
	public void tearDown() throws Exception { 
		dbaccess.disconnect();
	}

}
