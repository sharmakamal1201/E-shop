import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class MainClass
{
	public static void main(String[] args)
	{
		Database C = new Database();
		C = (Database)ReadObj("Save_DB.txt");
		if(C==null)
		{
			C = new Database();
		}
		int shutdown=1;
		int TR = 0;
		Scanner sc = new Scanner(System.in);
		
		while(shutdown==1)
		{
			System.out.println("Press 1 for admin, 2 for customer");
			int choice = sc.nextInt();
			int ch = -1;
			Cart cart = new Cart();

			while(ch!=5 && choice==1)
			{
				if(choice==1)
				{
					System.out.println("Menu : ");
					System.out.println("1. Insert Product/Category");
					System.out.println("2. Delete Product/Category");
					System.out.println("3. Search Product");
					System.out.println("4. Modify Product");
					System.out.println("5. Exit as admin");
					System.out.print("Enter your choice : ");
					ch = sc.nextInt();
					//C.print();
					if(ch==1)
					{
						System.out.print("Enter path : ");
						String path = sc.next();
						System.out.print("Enter product name : ");
						String pid = sc.next();
						System.out.print("Enter Price : ");
						int price = sc.nextInt();
						System.out.print("Enter Quantity : ");
						int quantity = sc.nextInt();
						try {
							C.insert(path, pid, price, quantity);
						} catch (Database.ProductAlreadyPresentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(ch==2)
					{
						System.out.print("Enter path : ");
						String path = sc.next();
						int lastIndex = path.lastIndexOf('>');
						String a = path.substring(0, lastIndex);
						String b = path.substring(lastIndex+1,path.length());
						List<String> L1 = new ArrayList<String>();
						L1.add(a); L1.add(b);
						try {
							C.delete(L1);
						} catch (Database.InvalidPathException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(ch==3)
					{
						System.out.print("Enter product name : ");
						String pid = sc.next();
						try {
							C.search(pid);
						} catch (Database.ProductNotAvailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(ch==4)
					{
						System.out.print("Enter path : ");
						String path = sc.next();
						System.out.print("Enter product name : ");
						String pid = sc.next();
						System.out.print("Enter Price : ");
						int price = sc.nextInt();
						System.out.print("Enter Quantity : ");
						int quantity = sc.nextInt();
						try {
							C.modify(path, pid, price, quantity);
						} catch (Database.ProductNotAvailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(ch==5)
					{
						System.out.println("Press 2 for customer 0 to exit");
						choice = sc.nextInt();
						ch=-1;
						break;
					}
				}
			}

			while(ch!=5 && choice==2)
			{
				if(ch==-1 || ch==5)
				{
					System.out.println("Press 1 if you are existing user, 0 for new");
					int exist = sc.nextInt();
					System.out.println("Enter username : ");
					String uname;
					uname =  sc.next();
					if(exist==0)
						cart.username = uname;
					else
					{
						String nm = "#*#";
						int save_ind = 0;
						int sz = C.Customer_details.size();
						for(int i=0;i<sz;i++)
						{
							nm = C.Customer_details.get(i).username;
							if(nm.equals(uname))
							{
								cart = C.Customer_details.get(i);
								save_ind = i;
								break;
							}
						}
						if(!nm.equals(uname))
						{
							System.out.println("Wrong username");
							break;
						}
						else
						{
							C.Customer_details.remove(save_ind); // remove already existing
						}
					}
				}
				if(choice==2)
				{
					System.out.println("Menu : ");
					System.out.println("1. Add funds");
					System.out.println("2. Add Product to cart");
					System.out.println("3. Check out");
					System.out.println("4. Print products in cart");
					System.out.println("5. Exit as customer");
					System.out.print("Enter your choice : ");
					ch = sc.nextInt();
					if(ch==1)
					{
						System.out.println("Enter funds(amt) to add : ");
						int fund = sc.nextInt();
						cart.remaining_funds = cart.remaining_funds + fund;
					}
					if(ch==2)
					{
						System.out.print("Enter product name : ");
						String pid = sc.next();
						System.out.print("Enter quantity : ");
						int qty = sc.nextInt();
						Product pr = null;
						try {
							pr = cart.add_Product(pid,C,qty);
						} catch (Database.InsufficientStockInStoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(pr!=null)
						{
							cart.Pro_in_cart.add(pr);
						}
					}
					if(ch==3)
					{
						if(cart.Pro_in_cart.size()>0)
						{
							int check = cart.check_out(cart.Pro_in_cart, cart.remaining_funds,C);
							if(check==-1)
							{
								System.out.println("Not possible");
							}
							else
							{
								System.out.println("Remaining funds : " + check);
								for(int i=0;i<cart.Pro_in_cart.size();i++)
								{
									String id = cart.Pro_in_cart.get(i).getPId();
									int Quantity = cart.Pro_in_cart.get(i).getQuantity();
									int Price = cart.Pro_in_cart.get(i).getPrice();
									C.flag_call_from_sale=1;
									List<String> L = null;
									try {
										L = C.search(id);
									} catch (Database.ProductNotAvailableException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									int actQty = C.search(L).search(id).getQuantity();
									if(Quantity==actQty)
									{
										try {
											C.delete(L);
										} catch (Database.InvalidPathException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									else
									{
										try {
											C.modify(L.get(0),id,Price,actQty-Quantity);
										} catch (Database.ProductNotAvailableException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								cart.Pro_in_cart.clear();
								TR = TR + cart.remaining_funds;
								cart.remaining_funds=check;
								TR = TR - check;
							}
						}
						else
						{
							System.out.println("Cart is empty!!!");
						}
					}
					if(ch==4)
					{
						cart.print();
					}
					if(ch==5)
					{
						C.Customer_details.add(cart);
						break;
					}
					
				}
			}
			
			System.out.println("Press 0 to exit , 1 to continue");
			shutdown = sc.nextInt();

		}
		
		/*for(int i=0;i<C.Customer_details.size();i++)
		{
			Cart pri = C.Customer_details.get(i); 
			System.out.println(pri.username + pri.remaining_funds);
			pri.print();
			System.out.println("********************************");
		}*/

		
		System.out.println("Total Revenue : "+TR);
		sc.close();
		
		WriteObj(C,"Save_DB.txt");
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















