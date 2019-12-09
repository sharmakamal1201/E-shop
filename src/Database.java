import java.io.Serializable;
import java.util.*;
public class Database implements Serializable
{
	public ArrayList<SubCategory> SubCatList;
	public int flag;
	public int flag_call_from_sale; 
	ArrayList<Cart> Customer_details = new ArrayList<Cart>();
	
	public Database()
	{
		SubCatList = new ArrayList<SubCategory>();
		flag=0;
		flag_call_from_sale=0;
	}
	
	public void insert(String path, String pid, int price, int quantity) throws ProductAlreadyPresentException
	{
		//System.out.println(" here");
		List<String> L1 = new ArrayList<String>(2);
		L1.add(path);L1.add(pid);
		SubCategory SC = new SubCategory(path);
		SC = search(L1);
		if(SC==null) // if no such subcategory exists
		{
			//System.out.println(path+" is not already present");
			SC = new SubCategory(path);
			SubCatList.add(SC); // add subcategory
			SC.add_product(pid, price, quantity); // add product
		}
		else
		{
			Product P = new Product(pid,price,quantity);
			SC = search(L1);
			if(SC.search(pid)==null) // if product is not in store
			{
				//System.out.println(pid+" is not already present");
				SC.add_product(pid, price, quantity);
			}
			else
			{
				throw new ProductAlreadyPresentException();
				//P = SC.search(pid);
				//int Qty = P.getQuantity();
				//Qty = quantity + Qty;
				// if product is already present increase quantity
				//flag=1;
				//delete(L1);
				//flag=0;
				//SC.add_product(pid, price, Qty); 
			}
		}
		
	}
	
	public SubCategory search(List<String> path)
	{
		String SubCat = path.get(0);
		int size = SubCatList.size();
		for(int i=0;i<size;i++)
		{
			if(SubCatList.get(i).Path.equals(SubCat))
			{
				return SubCatList.get(i);
			}
		}
		return null;
	}
	
	public void print()
	{
		for(int i=0;i<SubCatList.size();i++)
		{
			SubCatList.get(i).print();
		}
	}
	
	public void delete(List<String> path) throws InvalidPathException
	{
		SubCategory SC = new SubCategory(path.get(0));
		SC = search(path);
		List<String> L1 = null;
		try {
			L1 = search(path.get(1));
		} catch (ProductNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(L1==null)
		{
			System.out.println("Item to be deleted is already not in store :)");
			throw new InvalidPathException();
		}
		else if(path.size()>1)// delete product not subcategory
		{
			Product P = new Product("*****",0,1); // random
			//SC = search(path);
			if(SC.search(path.get(1))==null) // if product is not in store
			{
				System.out.println(path.get(1)+" is already not in store :)");
			}
			else
			{
				P = SC.search(path.get(1));
				if(flag==0)
					System.out.println(path.get(1)+" is removed from store");
				else if(flag==1)
				{
					System.out.println(path.get(1)+" is already in store.");
					System.out.println("So, price is updated and Quantity is added");
				}
				SC.remove_product(P); 
			}
		}
		else // remove subcategory
		{
			//SC = search(path);
			System.out.println(path.get(0)+" is removed from store");
			SubCatList.remove(SC);
		}
	}
	
	public void modify(String path, String pid, int price, int quantity) throws ProductNotAvailableException
	{
		List<String> L1 = new ArrayList<String>(2);
		L1.add(path); L1.add(pid);
		SubCategory SC = new SubCategory(pid);
		if(search(L1)==null) // if no such subcategory exists
		{
			//insert(path,pid,price,quantity);
			throw new ProductNotAvailableException();
		}
		else
		{
			Product P = new Product(pid,price,quantity);
			SC = search(L1);
			if(SC.search(pid)==null) // if product is not in store
			{
				//insert(path,pid,price,quantity);
				throw new ProductNotAvailableException();
			}
			else
			{
				flag=2;
				try {
					delete(L1);
				} catch (InvalidPathException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flag=0;
				System.out.println("Updated!!! in store");
				SC.add_product(pid, price, quantity);
				P = SC.search(pid);
				P.print();
			}
		}
	}
	
	public List<String> search (String pid) throws ProductNotAvailableException
	{
		for(int i=0;i<SubCatList.size();i++)
		{
			if(SubCatList.get(i).search(pid)!=null)
			{
				if(flag_call_from_sale==0) // print only is directly searched
				{
					SubCatList.get(i).print();
					System.out.println(">"+pid);
					SubCatList.get(i).search(pid).print();
				}
				List<String> L1 = new ArrayList<String>(2);
				L1.add(SubCatList.get(i).Path); L1.add(pid);
				return L1;
			}
		}
		System.out.println(pid+" : not found!!!");
		throw new ProductNotAvailableException();
		//return null;
	}
	
	public int sale(String pid,int quantity,int remaining_funds_with_customer) throws InsufficientFundException,InsufficientStockInStoreException
	{
		// return -1 if purchasing is not possible
		flag_call_from_sale=1;
		List<String> L1 = null;
		try {
			L1 = search(pid);
		} catch (ProductNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(L1==null)
		{
			flag_call_from_sale=0;
			return -1;
		}
		else
		{
			flag_call_from_sale=1;
			//List<String> L1 = search(pid);
			flag_call_from_sale=0;
			SubCategory SC = search(L1);
			Product P = SC.search(pid);
			if(P.getQuantity()<quantity)
			{
				System.out.println("only "+P.getQuantity()+" piece(s) of "+pid+" is/are available");
				throw new InsufficientStockInStoreException();
				//return -1;
			}
			int worth = quantity * P.getPrice();
			if(worth>remaining_funds_with_customer)
			{
				System.out.println("less remaining funds");
				throw new InsufficientFundException();
				//return -1;
			}
			remaining_funds_with_customer = remaining_funds_with_customer - worth;
		}
		
		return remaining_funds_with_customer;
	}
	
	public static class ProductAlreadyPresentException extends Exception
	{
		ProductAlreadyPresentException() {super("Product alredy present !");}
	}
	
	public static class InvalidPathException extends Exception
	{
		InvalidPathException() {super("Invalid path !");}
	}
	
	public static class ProductNotAvailableException extends Exception
	{
		ProductNotAvailableException() {super("Product is not available in store at present !");}
	}
	
	public static class InsufficientFundException extends Exception
	{
		InsufficientFundException() {super("Not possible ! , because of less funds in account !");}
	}
	
	public static class InsufficientStockInStoreException extends Exception
	{
		InsufficientStockInStoreException() {super("Insufficient stock !");}
	}
	
}





