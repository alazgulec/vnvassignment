package addressbook.DBUnit.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.db.DbSource;
import addressbook.ui.ListEntry;

public class DBUnitTest2 {

	private DbSource dbaccess;
	private Connection connection;

	@Before
	public void setUp() throws Exception {
		dbaccess = new DbSource();
		boolean res = dbaccess.connect();
		if (res) {
			System.out.println("Connection set!");
		}
		IDatabaseConnection setupConnection = new DatabaseConnection(
				dbaccess.getConnection());
		setupConnection.getConnection().setSchema("APP");

		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		IDataSet dataSet = builder.build(this.getClass()
				.getResource("data.xml"));

		DatabaseOperation.CLEAN_INSERT.execute(setupConnection, dataSet);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		//connection.close();
	}

	@Test
	public void testDeleteRecord() throws SQLException, DatabaseUnitException {
		AddressDao aDao = new AddressDao();
		aDao.connect();
		int id = 0;
		List<ListEntry> list = aDao.getListEntries();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getLastName().equals("test2")) {
				id = list.get(i).getId();
			}
		}
		aDao.deleteRecord(id);
		IDataSet expectedDataSet;
		FlatXmlDataSetBuilder data = new FlatXmlDataSetBuilder();
		expectedDataSet = data.build(this.getClass().getResource(
				"AfterDelete.xml"));
		ITable expectedTable = expectedDataSet.getTable("ADDRESS");

		IDataSet databaseDataSet = new DatabaseConnection(
				dbaccess.getConnection()).createDataSet();
		ITable actualTable = databaseDataSet.getTable("ADDRESS");
		ITable filteredTable = DefaultColumnFilter.includedColumnsTable(
				actualTable, expectedTable.getTableMetaData().getColumns());
		Assertion.assertEquals(expectedTable, filteredTable);
	}
}
