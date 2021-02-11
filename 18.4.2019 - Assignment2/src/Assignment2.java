import java.io.*;

public class Assignment2 {
	public static final int MAX_EMPLOYER = 5;
	public static final int MAX_WAITER = 5;
	public static final int ALLOWED_MAX_TABLES = 2;
	public static final int MAX_TABLE_SERVICES = 3;
	public static final int MAX_TABLES = 5;
	public static final int MAX_ORDERS = 5;
	public static final int MAX_ITEMS = 10;
	
	public static void main(String[] args)throws Exception {
		File setup = new File("setup.dat");
		BufferedReader br = new BufferedReader(new FileReader(setup));
		File commands = new File("commands.dat");
		String st;
		int itemsIndex = 0;
		int employersIndex = 0;
		int waitersIndex = 0;
		Orders[] orders = new Orders[99];
		Waiter[] waiters = new Waiter[MAX_WAITER];
		Items[] items = new Items[MAX_ITEMS];
		Employer[] employers = new Employer[MAX_EMPLOYER];
		/* 
		 * 
		 * Initialization phase of the program from setup.dat 
		 * 
		 */
		while((st = br.readLine()) != null) {
			if(st.indexOf("add_item") != -1) {
				st = st.substring(9);
				String stArray[] = st.split(";");
				for(int i=0; i < stArray.length; i++) {
					if(i % 3 == 0){
						items[itemsIndex] = new Items();
						items[itemsIndex].setName(stArray[i]);
					}
					else if(i % 3 == 1) {
						items[itemsIndex].setCost(Float.parseFloat(stArray[i]));
					}
					else{
						items[itemsIndex].setAmount(Integer.parseInt(stArray[i]));
						itemsIndex++;
					}
				}
			}
			else if (st.indexOf("add_employer") != -1) {
				st = st.substring(13);
				String stArray[] = st.split(";");
				for(int i=0; i < stArray.length; i++) {
					if(i % 2 == 0) {
						employers[employersIndex] = new Employer();
						employers[employersIndex].setName(stArray[i]);
					}
					else {
						employers[employersIndex].setSalary(Float.parseFloat(stArray[i]));
						employersIndex++;
					}
				}
			}
			else {
				st = st.substring(11);
				String stArray[] = st.split(";");
				for(int i=0; i < stArray.length; i++) {
					if(i % 2 == 0) {
						waiters[waitersIndex] = new Waiter();
						waiters[waitersIndex].setName(stArray[i]);
					}
					else {
						waiters[waitersIndex].setSalary(Float.parseFloat(stArray[i]));
						waitersIndex++;
					}
				}
			}
		/* 
		 * 
		 * end of initialization 
		 *
		 */
		}
		BufferedReader br0 = new BufferedReader(new FileReader(commands));
		/*
		 * 
		 * Start of reading commands from commands.dat and executes them
		 * 
		 */
		int tableCounter = 1;
		int tablesIndex = 0;
		Tables[] tables = new Tables[MAX_TABLES];
		while((st = br0.readLine()) != null) {
			int temp = 0;
			if(st.indexOf("create_table") != -1) {
				System.out.println("***********************************\nPROGRESSING COMMAND: create_table");
				boolean mistake = false;
				int createTableCounter = 0;
				st = st.substring(13);
				String stArray[] = st.split(";");
				for(int i = 0; i < stArray.length; i++) {
					if(i % 2 == 0) {
						for(int j = 0; j < employersIndex; j++) {
							if(stArray[i].equals(employers[j].getName())) {
								employers[j].setTablesCreated((employers[j].getTablesCreated()) + 1);
								temp = employers[j].getTablesCreated();
								break;
							}
							else {
								createTableCounter++;
							}
						}
						if(createTableCounter == employersIndex) {
							System.out.println("There is no employer named " + stArray[i]);
							mistake = true;
						}
						else if(tableCounter >= MAX_TABLES + 1) {
							System.out.println("Not allowed to exceed max. number of tables, " + MAX_TABLES);
							mistake = true;
						}
						else if(temp >= ALLOWED_MAX_TABLES + 1) {
							System.out.println(stArray[i] + " has already created " + ALLOWED_MAX_TABLES + " tables!");
							mistake = true;
						}
						else {
							tableCounter++;
							tables[tablesIndex] = new Tables();
							tables[tablesIndex].setId(tablesIndex);
							tables[tablesIndex].setStatus(true);
							tables[tablesIndex].setWhoCreated(stArray[i]);
						}
					}
					if(i % 2 == 1) {
						if(mistake != true) {
							tables[tablesIndex].setCapacity(Integer.parseInt(stArray[i]));
							tablesIndex++;
							System.out.println("A new table has successfully been added");
						}
					}
				}
			}
			else if(st.indexOf("new_order") != -1) {
				System.out.println("***********************************\nPROGRESSING COMMAND: new_order");
				boolean mistake = false;
				int newOrderCounter = 0;
				int waitersTemp = 0;
				int tableId = 0;
				int newOrderItemCounter = 0;
				String itemNameTemp = null;
				st = st.substring(10);
				String stArray[] = st.split(";");
				String stTemp = null;
				for(int i = 0; i < stArray.length; i++) {
					if(i % 3 == 0) {
						for(int j = 0; j < waitersIndex; j++) {
							if(stArray[i].equals(waiters[j].getName())) {
								stTemp = stArray[i];
								waitersTemp = j;
								break;
							}
							else {
								newOrderCounter++;
							}
						}
						if(newOrderCounter == waitersIndex) {
							System.out.println("There is no waiter named " + stArray[i]);
							mistake = true;
						}
					}
					else if(i % 3 == 1) {
						if(mistake != true) {
							for(int j = 0; j < waitersIndex; j++) {
								if(j == waitersTemp) {
									waiters[j].setTablesServed((waiters[j].getTablesServed())+ 1);
									temp = waiters[j].getTablesServed();
								}
							}
							int tableTemp = 0;
							if(temp >= MAX_TABLE_SERVICES + 1) {
								System.out.println("Not allowed to service max. number of tables, " + MAX_TABLE_SERVICES);
								mistake = true;
								break;
							}
							for(int j = 0; j < tablesIndex; j++) {
								if(Integer.parseInt(stArray[i]) <= tables[j].getCapacity()) {
										if(tables[j].getStatus()) {
											tables[j].setStatus(false);
											tableId = tables[j].getId();
											tableTemp++;
											tables[j].setWhoOperates(stTemp);
											tables[j].setTotalOrderCount(tables[j].getTotalOrderCount() + 1);
											break;
										}
								}
							}
							if(tableTemp == 1){
								tableTemp = 0;
								System.out.println("Table (= ID " + tableId + ") has been taken into service");
							}
							else {
								System.out.println("There is no appropriate table for this order!");
								mistake = true;
							}
						}
					}
					else if(i % 3 == 2) {
						int itemTemp0 = 0;
						if(mistake != true) {
							for(int j = 0; j < waitersIndex; j++) {
								if(waiters[j].getName().equals(stTemp)) {
									waiters[j].setTotalOrder(waiters[j].getTotalOrder() + 1);
								}
							}
							orders[tableId] = new Orders();
							orders[tableId].setId(tableId);
							String st0Array[] = stArray[i].split(":");
							for(int j = 0; j < st0Array.length; j++) {
								String st1Array[] = st0Array[j].split("-");
								for(int k = 0; k < st1Array.length; k++) {
									if(k % 2 == 0) {
										for(int l = 0; l < itemsIndex; l++) {
											if(st1Array[k].equals(items[l].getName())) {
												itemNameTemp = st1Array[k];
												break;
											}
											else {
												newOrderItemCounter++;
											}
										}
										if(newOrderItemCounter == itemsIndex) {
											System.out.println("Unknown item " + st1Array[k]);
											mistake = true;
											break;
										}
									}
									else if(k % 2 == 1) {
										int itemTemp = 0;
										for(int l = 0; l < itemsIndex; l++) {
											if(itemNameTemp.equals(items[l].getName())) {
													for(int h = 0; h < Integer.parseInt(st1Array[k]); h++) {
														if(items[l].getAmount() > 0) {
															System.out.println("Item " + items[l].getName() + " added into order");
															items[l].setAmount(items[l].getAmount() - 1);
															itemTemp++;
															itemTemp0++;
															
														}
														else {
															System.out.println("Sorry! No " + items[l].getName() + " in the stock!");
														}
													}
													orders[tableId].setCapacity(itemTemp0);
													orders[tableId].setOrder(orders[tableId].getOrder() + items[l].getName() + ":\t" );
													orders[tableId].setOrder(orders[tableId].getOrder() + String.format("%.3f", items[l].getCost()) + " (x " + itemTemp + ") " + String.format("%.3f",(items[l].getCost() * itemTemp)) + " $");
													orders[tableId].setTotalCost(orders[tableId].getTotalCost() + items[l].getCost() * itemTemp);
													if(j + 1 == st0Array.length) {
													}
													else {
														orders[tableId].setOrder(orders[tableId].getOrder() + "\n");
													}
													itemTemp = 0;
											}
										}
									}
									
								}
							}
							orders[tableId].setItemCount(orders[tableId].getItemCount() + "\n" + "\t\t" + itemTemp0 + " item(s)");
						}
					}
				}
			}
			else if(st.indexOf("add_order") != -1) {
				System.out.println("***********************************\nPROGRESSING COMMAND: add_order");
				boolean mistake = false;
				int addOrderCounter = 0;
				int addOrderTableCounter = 0;
				st = st.substring(10);
				int addOrderItemCounter = 0;
				String itemNameTemp = null;
				String stTemp = null;
				int tablesTemp = 0;
				String stArray[] = st.split(";");
				for(int i = 0; i < stArray.length; i++) {
					if(i % 3 == 0) {
						for(int j = 0; j < waitersIndex; j++) {
							if(stArray[i].equals(waiters[j].getName())) {
								stTemp = stArray[i];
								break;
							}
							else {
								addOrderCounter++;
							}
						}
						if(addOrderCounter == waitersIndex) {
							System.out.println("There is no waiter named " + stArray[i]);
							mistake = true;
						}
					}
					else if(i % 3 == 1) {
						if(mistake != true) {
							for(int j = 0; j < tablesIndex; j++) {
								if(Integer.parseInt(stArray[i]) == tables[j].getId()) {
									tablesTemp = tables[j].getId();
									break;
								}
								else {
									addOrderTableCounter++;
								}
							}
							if(addOrderTableCounter == tablesIndex) {
								System.out.println("This table is either not in service now or " + stTemp + " cannot be assigned this table!");
								mistake = true;
							}
							else if(!(tables[tablesTemp].getWhoOperates().equals(stTemp))) {
								System.out.println("This table is either not in service now or " + stTemp + " cannot be assigned this table!");
								mistake = true;
							}
							else if(tables[tablesTemp].getTotalOrderCount() >= MAX_ORDERS){
								System.out.println("Not allowed to exceed max number of orders!");
								mistake = true;
							}
							else {
								tables[tablesTemp].setTotalOrderCount(tables[tablesTemp].getTotalOrderCount() + 1);
							}
						}
					}
					else if(i % 3 == 2) {
						int itemTemp0 = 0;
						if(mistake != true) {
							for(int j = 0; j < waitersIndex; j++) {
								if(waiters[j].getName().equals(stTemp)) {
									waiters[j].setTotalOrder(waiters[j].getTotalOrder() + 1);
								}
							}
							String st0Array[] = stArray[i].split(":");
							for(int j = 0; j < st0Array.length; j++) {
								String st1Array[] = st0Array[j].split("-");
								for(int k = 0; k < st1Array.length; k++) {
									if(k % 2 == 0) {
										for(int l = 0; l < itemsIndex; l++) {
											if(st1Array[k].equals(items[l].getName())) {
												itemNameTemp = st1Array[k];
												break;
											}
											else {
												addOrderItemCounter++;
											}
										}
										if(addOrderItemCounter == itemsIndex) {
											System.out.println("Unknown item " + st1Array[k]);
											mistake = true;
											break;
										}
									}
									else if(k % 2 == 1) {
										int itemTemp = 0;
										for(int l = 0; l < itemsIndex; l++) {
											if(itemNameTemp.equals(items[l].getName())) {
													for(int h = 0; h < Integer.parseInt(st1Array[k]); h++) {
														if(items[l].getAmount() > 0) {
															System.out.println("Item " + items[l].getName() + " added into order");
															items[l].setAmount(items[l].getAmount() - 1);
															itemTemp++;
															itemTemp0++;
														}
														else {
															System.out.println("Sorry! No " + items[l].getName() + " in the stock!");
														}
													}
													orders[tablesTemp].setOrder(orders[tablesTemp].getOrder() + "\n" + items[l].getName() + ":\t" );
													orders[tablesTemp].setOrder(orders[tablesTemp].getOrder() + String.format("%.3f", items[l].getCost()) + " (x " + itemTemp + ") " + String.format("%.3f",(items[l].getCost() * itemTemp)) + " $");
													orders[tablesTemp].setTotalCost(orders[tablesTemp].getTotalCost() + items[l].getCost() * itemTemp);
													if(j + 1 == st0Array.length) {
													}
													else {
														orders[tablesTemp].setOrder(orders[tablesTemp].getOrder());
													}
													itemTemp = 0;
											}
										}
									}
								}
							}
							orders[tablesTemp].setItemCount(orders[tablesTemp].getItemCount() + "\n" + "\t\t" + itemTemp0 + " item(s)");
						}
					}
				}
			}
			else if(st.indexOf("check_out") != -1) {
				System.out.println("***********************************\nPROGRESSING COMMAND: check_out");
				boolean mistake = false;
				st = st.substring(10);
				String stArray[] = st.split(";");
				String stTemp = null;
				int checkOutCounter = 0;
				int tablesTemp = 0;
				int checkOutTableCounter = 0;
				for(int i = 0; i < stArray.length; i++) {
					if(i % 2 == 0) {
						for(int j = 0; j < waitersIndex; j++) {
							if(stArray[i].equals(waiters[j].getName())) {
								stTemp = stArray[i];
								break;
							}
							else {
								checkOutCounter++;
							}
						}
						if(checkOutCounter == waitersIndex) {
							System.out.println("There is no waiter named " + stArray[i]);
							mistake = true;
						}
					}
					else if(i % 2 == 1) {
						if(mistake != true) {
							for(int j = 0; j < tablesIndex; j++) {
								if(Integer.parseInt(stArray[i]) == tables[j].getId()) {
									tablesTemp = tables[j].getId();
									break;
								}
								else {
									checkOutTableCounter++;
								}
							}
							if(checkOutTableCounter == tablesIndex) {
								System.out.println("This table is either not in service now or " + stTemp + " cannot be assigned this table!");
								mistake = true;
							}
							else if(!(tables[tablesTemp].getWhoOperates().equals(stTemp))) {
								System.out.println("This table is either not in service now or " + stTemp + " cannot be assigned this table!");
								mistake = true;
							}
							else {
								if(mistake != true) {
									orders[tablesTemp].setOrder(orders[tablesTemp].getOrder() + "\nTotal:\t" + String.format("%.3f", orders[tablesTemp].getTotalCost()) + " $");
									System.out.println(orders[tablesTemp].getOrder());
									tables[tablesTemp].setStatus(true);
									tables[tablesTemp].setTotalOrderCount(0);
									tables[tablesTemp].setWhoOperates("");
									orders[tablesTemp].setItemCount("");
									
								}
							}
						}
					}
				}
			}
			else if(st.indexOf("stock_status") != -1) {
				System.out.println("***********************************\nPROGRESSING COMMAND: stock_status");
				for(int i = 0; i < itemsIndex; i++) {
					System.out.println(items[i].getName() + ":\t" + items[i].getAmount());
				}
			}
			else if(st.indexOf("get_table_status") != -1) {
				System.out.println("***********************************\nPROGRESSING COMMAND: get_table_status");
				for(int i = 0; i < tablesIndex; i++) {
					if(tables[i].getStatus()) {
						System.out.println("Table " + tables[i].getId() + ": Free");
					}
					else {
						System.out.println("Table " + tables[i].getId() + ": Reserved (" + tables[i].getWhoOperates() + ")");
					}
				}
			}
			else if(st.indexOf("get_order_status") != -1) {
				System.out.println("***********************************\nPROGRESSING COMMAND: get_order_status");
				for(int i = 0; i < tablesIndex; i++) {
					System.out.print("Table: " + tables[i].getId() + "\n\t" + tables[i].getTotalOrderCount() + " order(s)");
						System.out.println(orders[i].getItemCount());
				}
			}
			else if(st.indexOf("get_employer_salary") != -1) {
				System.out.println("***********************************\nPROGRESSING COMMAND: get_employer_salary");
				for(int i = 0; i < employersIndex; i++) {
					System.out.println("Salary for " + employers[i].getName() + ": " + (employers[i].getSalary() + (0.1 * employers[i].getSalary() * employers[i].getTablesCreated())));
				}
			}
			else {
				System.out.println("***********************************\nPROGRESSING COMMAND: get_waiter_salary");
				for(int i = 0; i < waitersIndex; i++) {
					System.out.println("Salary for " + waiters[i].getName() + ": " + (waiters[i].getSalary() + (0.05 * waiters[i].getSalary() * waiters[i].getTotalOrder())));
				}
			}
		}
		br.close();
		br0.close();
	}
}
