package addressbook.db;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

/**
 * Define the DataSource 
 * @author ssome
 *
 */
public class DbSource implements IDataSource {
    
    /** Creates a new instance of DbAccess */
    public DbSource() {
        this("DefaultAddressBook");
    }
    
    public DbSource(String addressBookName) {
        this.dbName = addressBookName;
        
        setDBSystemDir();
        dbProperties = loadDBProperties();
        String driverName = dbProperties.getProperty("derby.driver"); 
        loadDatabaseDriver(driverName);
        if(!dbExists()) {
            createDatabase();
        }
        
    }
    
    private boolean dbExists() {
        boolean bExists = false;
        String dbLocation = getDatabaseLocation();
        File dbFileDir = new File(dbLocation);
        if (dbFileDir.exists()) {
            bExists = true;
        }
        return bExists;
    }
    
    private void setDBSystemDir() {
        // decide on the db system directory
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.addressbook";
        System.setProperty("derby.system.home", systemDir);
        
        // create the db system directory
        File fileSystemDir = new File(systemDir);
        fileSystemDir.mkdir();
    }
    
    private void loadDatabaseDriver(String driverName) {
        // load Derby driver
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
    
    private Properties loadDBProperties() {
        InputStream dbPropInputStream = null;
        dbPropInputStream = DbSource.class.getResourceAsStream("Configuration.properties");
        dbProperties = new Properties();
        try {
            dbProperties.load(dbPropInputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dbProperties;
    }
    
    
    private boolean createTables(Connection dbConnection) {
        boolean bCreatedTables = false;
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.execute(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return bCreatedTables;
    }
    private boolean createDatabase() {
        boolean bCreated = false;
        Connection dbConnection = null;
        
        String dbUrl = getDatabaseUrl();
        dbProperties.put("create", "true");
        
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
            bCreated = createTables(dbConnection);
        } catch (SQLException ex) {
        }
        dbProperties.remove("create");
        return bCreated;
    }
    
    public boolean connect() {
        String dbUrl = getDatabaseUrl();
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);            
            isConnected = dbConnection != null;
        } catch (SQLException ex) {
        	ex.printStackTrace();
            isConnected = false;
        }
        return isConnected;
    }
    
    private String getHomeDir() {
        return System.getProperty("user.home");
    }
    
    public void disconnect() {
        if(isConnected) {
            String dbUrl = getDatabaseUrl();
            dbProperties.put("shutdown", "true");
            try {
                DriverManager.getConnection(dbUrl, dbProperties);
            } catch (SQLException ex) {
            }
            isConnected = false;
        }
    }
    
    public String getDatabaseLocation() {
        String dbLocation = System.getProperty("derby.system.home") + "/" + dbName;
        return dbLocation;
    }
    
    public String getDatabaseUrl() {
        String dbUrl = dbProperties.getProperty("derby.url") + dbName;
        return dbUrl;
    }
    
    private Connection dbConnection;
    private Properties dbProperties;
    private boolean isConnected;
    private String dbName;
    private static final String strCreateAddressTable =
            "create table APP.ADDRESS (" +
            "    ID          INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "    LASTNAME    VARCHAR(30), " +
            "    FIRSTNAME   VARCHAR(30), " +
            "    MIDDLENAME  VARCHAR(30), " +
            "    PHONE       VARCHAR(20), " +
            "    EMAIL       VARCHAR(30), " +
            "    ADDRESS1    VARCHAR(30), " +
            "    ADDRESS2    VARCHAR(30), " +
            "    CITY        VARCHAR(30), " +
            "    STATE       VARCHAR(30), " +
            "    POSTALCODE  VARCHAR(20), " +
            "    COUNTRY     VARCHAR(30) " +
            ")";

	public Connection getConnection() {
		return dbConnection;
	}
    
}
