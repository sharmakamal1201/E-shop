import java.io.Serializable;

public class Product implements Serializable
{
	private int Price;
	private int Quantity;
	private String PId;
	
	public Product(String pid,int price,int quantity)
	{
		this.Price = price;
		this.Quantity = quantity;
		this.PId = pid;
	}
	
	public int getPrice()
	{
		return Price;
	}
	
	public int getQuantity()
	{
		return Quantity;
	}
	
	public String getPId()
	{
		return PId;
	}
	
	public void print()
	{
		System.out.println("PId : " + getPId());
		System.out.println("Price : " + getPrice());
		System.out.println("Quantity : " + getQuantity());
	}

}
