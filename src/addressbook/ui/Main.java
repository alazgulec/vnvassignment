package addressbook.ui;

import addressbook.db.AddressDao;
import addressbook.db.IAddressDao;

public class Main {

	/**
	 * @param args
	 */
	   public static void main(String[] args) {
	    	IAddressDao db = new AddressDao();
	        AddressFrame app = new AddressFrame(db);
	        app.setVisible(true);
	   }

}
