import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import org.junit.jupiter.api.Test;

class MainClassTest {
	Database C = new Database();

	@Test
	void testDB() throws Database.ProductAlreadyPresentException,Database.ProductNotAvailableException{
		C.insert("e>p", "sam", 10000, 6);
		WriteObj(C,"Save_DataBase.txt");
		C = (Database)ReadObj("Save_DataBase.txt");
		List<String> P = C.search("sam");
		assertEquals("sam",P.get(1));
	}

	
	
	@Test
	void testC() throws Database.ProductAlreadyPresentException,Database.ProductNotAvailableException,Database.InsufficientStockInStoreException{
		Cart cart = new Cart();
		C.insert("e>p", "oneplus", 12000, 5);
		Product pr = cart.add_Product("oneplus",C,2);
		cart.Pro_in_cart.add(pr);
		C.Customer_details.add(cart);
		WriteObj(C,"Save_DataBase.txt");
		C = (Database)ReadObj("Save_DataBase.txt");
		String check="0";
		for(int i=0;i<cart.Pro_in_cart.size();i++)
		{
			if(cart.Pro_in_cart.get(i).getPId()=="oneplus")
				check="oneplus";
		}

		assertEquals("oneplus",check);
	}

	
	
	
	static void WriteObj(Object obj,String file)
	{
		FileOutputStream fp;
		try {
			fp = new FileOutputStream(file);
			ObjectOutputStream op = new ObjectOutputStream(fp);
			op.writeObject(obj);
			op.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static Object ReadObj(String file)
	{
		Object obj = null;
		FileInputStream fp;
		if(new File(file).exists())
		{
			try {
				fp = new FileInputStream(file);
				ObjectInputStream op = new ObjectInputStream(fp);
				obj = op.readObject();
				op.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj;
	}
}
