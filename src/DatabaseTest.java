import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;

class DatabaseTest {

	Database D = new Database();
	@Test
	void testInsert() throws Database.ProductAlreadyPresentException{
		//insert(String path, String pid, int price, int quantity)
		System.out.println("Testing insert");
		String Cat = "a>b";
		String id = "c";
			D.insert("a>b", "c", 1, 1);
		System.out.println("Adding duplicate");
		assertThrows(Database.ProductAlreadyPresentException.class,() -> {D.insert("a>b", "c", 1, 1);});
		System.out.println("Testing finished");
	}

	@Test
	void testDelete(){
		System.out.println("Testing delete");
		String Cat = "a>b";
		String id = "c";
		Cat = "a>b1";
		id = "d1";
		List<String> L1 = new ArrayList<String>();
		L1.add(Cat);
		L1.add(id);
		try {
			D.delete(L1);
		} catch (Database.InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//assertThrows(Database.InvalidPathException.class,() -> D.delete(L1));
		System.out.println("Testing finished");
	}

	@Test
	void testSearchString() {
		
		System.out.println("Testing search");
		String id = "erer";
		try {
			D.search(id);
		} catch (Database.ProductNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Testing finished");
		
	}

	@Test
	void testSale() throws Database.ProductAlreadyPresentException{
		System.out.println("Testing sale");
		String id = "erer";
		D.insert("e>p", id, 10, 1);
		assertThrows(Database.InsufficientFundException.class,() -> D.sale(id,1,0));
		System.out.println("Testing finished");
	}
	
	@Test
	void testSale1() throws Database.ProductAlreadyPresentException{
		System.out.println("Testing sale");
		String id = "erer";
		D.insert("e>p", id, 10, 1);
		assertThrows(Database.InsufficientStockInStoreException.class,() -> D.sale(id,2,0));
		System.out.println("Testing finished");
	}

}
