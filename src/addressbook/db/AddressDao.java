package addressbook.db;
import addressbook.core.Address;
import addressbook.ui.ListEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Layer for addresses
 * @author ssome
 *
 */
public class AddressDao implements IAddressDao {
    private IDataSource dbaccess;
 
	/** Creates a new instance of AddressDao */
    public AddressDao() {
        this("DefaultAddressBook");
    }
    
    public AddressDao(String addressBookName) {
    	this.dbaccess = new DbSource();
        
    }
    
    public IDataSource getDbaccess() {
		return dbaccess;
	}

	public void setDbaccess(IDataSource dbaccess) {
		this.dbaccess = dbaccess;
	}
    
    public boolean connect() {
    	boolean res = dbaccess.connect();
    	dbConnection = dbaccess.getConnection();
    	try {
    		if (res) {
    			stmtSaveNewRecord = dbConnection.prepareStatement(strSaveAddress, Statement.RETURN_GENERATED_KEYS);
    			stmtUpdateExistingRecord = dbConnection.prepareStatement(strUpdateAddress);
    			stmtGetAddress = dbConnection.prepareStatement(strGetAddress);
    			stmtDeleteAddress = dbConnection.prepareStatement(strDeleteAddress);
    		}
        } catch (SQLException ex) {
            res = false;
        }   	
        return res;
    }
    
    public void disconnect() {
    	dbaccess.disconnect();
    }
    
    public int saveRecord(Address record) {
        int id = -1;
        try {
            stmtSaveNewRecord.clearParameters();
            
            stmtSaveNewRecord.setString(1, record.getLastName());
            stmtSaveNewRecord.setString(2, record.getFirstName());
            stmtSaveNewRecord.setString(3, record.getMiddleName());
            stmtSaveNewRecord.setString(4, record.getPhone());
            stmtSaveNewRecord.setString(5, record.getEmail());
            stmtSaveNewRecord.setString(6, record.getAddress1());
            stmtSaveNewRecord.setString(7, record.getAddress2());
            stmtSaveNewRecord.setString(8, record.getCity());
            stmtSaveNewRecord.setString(9, record.getState());
            stmtSaveNewRecord.setString(10, record.getPostalCode());
            stmtSaveNewRecord.setString(11, record.getCountry());
            int rowCount = stmtSaveNewRecord.executeUpdate();
            ResultSet results = stmtSaveNewRecord.getGeneratedKeys();
            if (results.next()) {
                id = results.getInt(1);
            }
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return id;
    }
    
    public Connection getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	public PreparedStatement getStmtSaveNewRecord() {
		return stmtSaveNewRecord;
	}

	public void setStmtSaveNewRecord(PreparedStatement stmtSaveNewRecord) {
		this.stmtSaveNewRecord = stmtSaveNewRecord;
	}

	public PreparedStatement getStmtUpdateExistingRecord() {
		return stmtUpdateExistingRecord;
	}

	public void setStmtUpdateExistingRecord(
			PreparedStatement stmtUpdateExistingRecord) {
		this.stmtUpdateExistingRecord = stmtUpdateExistingRecord;
	}

	public PreparedStatement getStmtGetAddress() {
		return stmtGetAddress;
	}

	public void setStmtGetAddress(PreparedStatement stmtGetAddress) {
		this.stmtGetAddress = stmtGetAddress;
	}

	public PreparedStatement getStmtDeleteAddress() {
		return stmtDeleteAddress;
	}

	public void setStmtDeleteAddress(PreparedStatement stmtDeleteAddress) {
		this.stmtDeleteAddress = stmtDeleteAddress;
	}

    public boolean editRecord(Address record) {
        boolean bEdited = false;
        try {
            stmtUpdateExistingRecord.clearParameters();
            
            stmtUpdateExistingRecord.setString(1, record.getLastName());
            stmtUpdateExistingRecord.setString(2, record.getFirstName());
            stmtUpdateExistingRecord.setString(3, record.getMiddleName());
            stmtUpdateExistingRecord.setString(4, record.getPhone());
            stmtUpdateExistingRecord.setString(5, record.getEmail());
            stmtUpdateExistingRecord.setString(6, record.getAddress1());
            stmtUpdateExistingRecord.setString(7, record.getAddress2());
            stmtUpdateExistingRecord.setString(8, record.getCity());
            stmtUpdateExistingRecord.setString(9, record.getState());
            stmtUpdateExistingRecord.setString(10, record.getPostalCode());
            stmtUpdateExistingRecord.setString(11, record.getCountry());
            stmtUpdateExistingRecord.setInt(12, record.getId());
            stmtUpdateExistingRecord.executeUpdate();
            bEdited = true;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return bEdited;
        
    }
    
    public boolean deleteRecord(int id) {
        boolean bDeleted = false;
        try {
            stmtDeleteAddress.clearParameters();
            stmtDeleteAddress.setInt(1, id);
            stmtDeleteAddress.executeUpdate();
            bDeleted = true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return bDeleted;
    }
    
    public boolean deleteRecord(Address record) {
        int id = record.getId();
        return deleteRecord(id);
    }
    
    public List<ListEntry> getListEntries() {
        List<ListEntry> listEntries = new ArrayList<ListEntry>();
        Statement queryStatement = null;
        ResultSet results = null;
        
        try {
            queryStatement = dbConnection.createStatement();
            results = queryStatement.executeQuery(strGetListEntries);
            while(results.next()) {
                int id = results.getInt(1);
                String lName = results.getString(2);
                String fName = results.getString(3);
                String mName = results.getString(4);
                
                ListEntry entry = new ListEntry(lName, fName, mName, id);
                listEntries.add(entry);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            
        }
        
        return listEntries;
    }
    
    public Address getAddress(int index) {
        Address address = null;
        try {
            stmtGetAddress.clearParameters();
            stmtGetAddress.setInt(1, index);
            ResultSet result = stmtGetAddress.executeQuery();
            if (result.next()) {
                String lastName = result.getString("LASTNAME");
                String firstName = result.getString("FIRSTNAME");
                String middleName = result.getString("MIDDLENAME");
                String phone = result.getString("PHONE");
                String email = result.getString("EMAIL");
                String add1 = result.getString("ADDRESS1");
                String add2 = result.getString("ADDRESS2");
                String city = result.getString("CITY");
                String state = result.getString("STATE");
                String postalCode = result.getString("POSTALCODE");
                String country = result.getString("COUNTRY");
                int id = result.getInt("ID");
                address = new Address(lastName, firstName, middleName, phone,
                        email, add1, add2, city, state, postalCode,
                        country, id);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return address;
    }
    
    
    private Connection dbConnection;
    private PreparedStatement stmtSaveNewRecord;
    private PreparedStatement stmtUpdateExistingRecord;
    private PreparedStatement stmtGetAddress;
    private PreparedStatement stmtDeleteAddress;
    
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
    
}
