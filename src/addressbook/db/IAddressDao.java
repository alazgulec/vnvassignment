package addressbook.db;

import java.util.List;

import addressbook.core.Address;
import addressbook.ui.ListEntry;

/**
 * Interface for Addresses data access layer
 * @author ssome
 *
 */
public interface IAddressDao {

	public abstract boolean connect();

	public abstract void disconnect();

	public abstract int saveRecord(Address record);

	public abstract boolean editRecord(Address record);

	public abstract boolean deleteRecord(int id);

	public abstract boolean deleteRecord(Address record);

	public abstract List<ListEntry> getListEntries();

	public abstract Address getAddress(int index);

}