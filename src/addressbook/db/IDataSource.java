package addressbook.db;

import java.sql.Connection;

/**
 * Data source interface definition
 * @author ssome
 *
 */
public interface IDataSource {

	public abstract boolean connect();

	public abstract void disconnect();

	public abstract String getDatabaseLocation();

	public abstract String getDatabaseUrl();

	public abstract Connection getConnection();

}