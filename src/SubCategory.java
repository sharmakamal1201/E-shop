import java.io.Serializable;
import java.util.*;
import java.io.Serializable;

public class SubCategory implements Serializable
{
	public String Path;
	public ArrayList<Product>  ProductList;
	
	public SubCategory(String Path)
	{
		this.ProductList = new ArrayList<Product>();
		this.Path = Path;
	}
	
	public void add_product(String pid, int price, int quantity)
	{
		Product P = new Product(pid,price,quantity);
		ProductList.add(P);
	}
	
	public Product search(String Pro_name)
	{
		int size = ProductList.size();
		for(int i=0;i<size;i++)
		{
			if(ProductList.get(i).getPId().equals(Pro_name))
			{
				return ProductList.get(i);
			}
		}
		return null;
	}
	
	public void print()
	{
		System.out.print("Path : " + Path);
	}
	
	public void remove_product(Product P)
	{
		ProductList.remove(P);
	}
	
}
