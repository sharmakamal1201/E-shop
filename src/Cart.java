import java.io.Serializable;
import java.util.*;

public class Cart implements Serializable
{
	public String username;
	public ArrayList<Product> Pro_in_cart = new ArrayList<Product>();
	public int remaining_funds;
	
	public Cart()
	{
		remaining_funds=0;
	}

	public Product add_Product(String pid,Database D,int quantity) throws Database.InsufficientStockInStoreException
	{
		//Database D = new Database();
		D.flag_call_from_sale=1;
		List<String> L1 = null;
		try {
			L1 = D.search(pid);
		} catch (Database.ProductNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(L1==null)
		{
			D.flag_call_from_sale=0;
			System.out.println("No such item available");
			return null;
		}
		D.flag_call_from_sale=1;
		//List<String> L1 = D.search(pid);
		D.flag_call_from_sale=0;
		SubCategory SC = D.search(L1);
		Product P = SC.search(pid);
		if(P.getQuantity()<quantity)
		{
			int q = P.getQuantity();
			System.out.println("Not possible only "+q+" pieces are available");
			throw new Database.InsufficientStockInStoreException();
			//return null;
		}
		Product pr = new Product(pid,P.getPrice(),quantity);
		return pr;
	}
	
	public int check_out(ArrayList<Product> pids,int rem_funds,Database D)
	{
		//Database D= new Database();
		for(int i=0;i<pids.size();i++)
		{
			int check = -1;
			try {
				check = D.sale(pids.get(i).getPId(),pids.get(i).getQuantity(),rem_funds);
			} catch (Database.InsufficientFundException | Database.InsufficientStockInStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(check!=-1)
			{
				rem_funds=check;
			}
			else
			{
				return -1;
			}
		}
		return rem_funds;
	}
	
	public void print()
	{
		for(int i=0;i<Pro_in_cart.size();i++)
		{
			Pro_in_cart.get(i).print();
		}
	}
}
