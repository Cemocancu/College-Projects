import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;

public class assignment3 {
	private static final ArrayList<Squares> SQUARES = new ArrayList<Squares>();
	private static final ArrayList<RngCards> CHANCE = new ArrayList<RngCards>();
	private static final ArrayList<RngCards> COMMUNITY = new ArrayList<RngCards>();
	public static void main(String[] args) {
		initialize();
		readyCards();
		gameOn(args[0]);
    }
	// this method reads property.json and creates the whole monopoly table
	public static void initialize() {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader("property.json"));
		    JSONObject jsonObject = (JSONObject) obj;
			SQUARES.add(new Squares("GO", 1));
			SQUARES.add(new Squares("Income Tax", 5));
			SQUARES.add(new Squares("Jail", 11));
			SQUARES.add(new Squares("Free Parking", 21));
			SQUARES.add(new Squares("Go to Jail", 31));
			SQUARES.add(new Squares("Super Tax", 39));
			SQUARES.add(new Squares("Community Chest", 3));
			SQUARES.add(new Squares("Community Chest", 18));
			SQUARES.add(new Squares("Community Chest", 34));
			SQUARES.add(new Squares("Chance", 8));
			SQUARES.add(new Squares("Chance", 23));
			SQUARES.add(new Squares("Chance", 37));
		    // This loop reads from property.json and putting the information to properties
		    for(int j = 1; j < jsonObject.size() + 1;j++) {
				JSONArray msg = (JSONArray) jsonObject.get(Integer.toString(j));
	            for(int i = 0; i < msg.size(); i++) {
            		JSONObject object = (JSONObject) msg.get(i);
	            	if(j == 1) {	            		//creates Lands at id index
	            		SQUARES.add(new Lands(object.get("name").toString(), Integer.parseInt(object.get("cost").toString()), false, Integer.parseInt(object.get("id").toString()), ""));
	            	}else if(j == 2) {
	            		SQUARES.add(new Railroads(object.get("name").toString(), Integer.parseInt(object.get("cost").toString()), false, Integer.parseInt(object.get("id").toString()), ""));
	            	}else {
	            		SQUARES.add(new Companies(object.get("name").toString(), Integer.parseInt(object.get("cost").toString()), false, Integer.parseInt(object.get("id").toString()), ""));
	            	}
	            }
		    }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
	//this method reads list.json and creates the community and chance cards section
	public static void readyCards() {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader("list.json"));
		    JSONObject jsonObject = (JSONObject) obj;
		    JSONArray msg = (JSONArray) jsonObject.get("chanceList");
		    JSONArray msg0 = (JSONArray) jsonObject.get("communityChestList");
		    for(int i = 0; i < msg.size(); i++) {
		    	JSONObject object = (JSONObject) msg.get(i);
		    	CHANCE.add(new RngCards(Integer.toString(i), object.get("item").toString()));
		    }
		    for(int i = 0; i < msg0.size(); i++) {
		    	JSONObject object = (JSONObject) msg0.get(i);
		    	COMMUNITY.add(new RngCards(Integer.toString(i), object.get("item").toString()));
		    }
		 } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	}
	//this method takes commands.txt as a parameter then starts the game
	public static void gameOn(String s) {
		try {
			Collections.sort(SQUARES);
			Players firstPlayer = new Players(15000);
			Players secondPlayer = new Players(15000);
			Players banker = new Players(100000);
			int counterp1 = 1, counterp2 = 1, jailcountp1 = 3, jailcountp2 = 3, comChestCounter = 0, chaCounter = 0;
			BufferedReader reader = new BufferedReader(new FileReader(s));
			BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
			String str = "";
			String line = reader.readLine();
			//reading line by line and executing the commands
			while(line != null) {
				String temp[] = line.split(";");
				if(line.equals("show()")) {
					for(int i = 0; i < 107; i++) {
						str = str + "-";
					}
					str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
					+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
					+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
					if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
						str = str + "Winner Player 1\n";
					}else {
						str = str + "Winner Player 2\n";
					}
					for(int i = 0; i < 107; i++) {
						str = str + "-";
					}
					str = str + "\n";
				}else {
					if(temp[0].equals("Player 1")) {
						if(jailcountp1 != 3) {
							jailcountp1++;
							str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() +
									"\t" + temp[0] + " in jail (count=" + jailcountp1 + ")\n";
						}else {
							counterp1 += Integer.parseInt(temp[1]);
							if(counterp1 > 40) {
								firstPlayer.setMoney(firstPlayer.getMoney() + 200);
								banker.setMoney(banker.getMoney() - 200);
								counterp1 = counterp1 % 40;
							}
							if(counterp1 % 40 == 1) {
								str = str + temp[0] + "\t" + temp[1] + "\t"
										+ 1 + "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " is in GO square\n";
							}
							else if(counterp1 % 40 == 0) {
								if(SQUARES.get(39).isStatus() == false) {
									if(firstPlayer.getMoney() - SQUARES.get(39).getCost() < 0) {
										str = str + temp[0] + "\t" + temp[1] + "\t"
										+ 40 + "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
										for(int i = 0; i < 107; i++) {
											str = str + "-";
										}
										str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
										+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
										+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
										if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
											str = str + "Winner Player 1\n";
										}else {
											str = str + "Winner Player 2\n";
										}
										for(int i = 0; i < 107; i++) {
											str = str + "-";
										}
										break;
									}else {
										firstPlayer.setMoney(firstPlayer.getMoney() - SQUARES.get(39).getCost());
										banker.setMoney(banker.getMoney() + SQUARES.get(39).getCost());
										if(firstPlayer.getProperties().equals("")) {
											firstPlayer.setProperties(firstPlayer.getProperties() +  SQUARES.get(39).getName());
										}else {
											firstPlayer.setProperties(firstPlayer.getProperties() + "," + SQUARES.get(39).getName());
										}
										SQUARES.get(39).setWhose("Player 1");
										SQUARES.get(39).setStatus(true);
										str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
										+ " bought " + SQUARES.get(39).getName() + "\n";
									}
								}else {
									if((SQUARES).get(39).getWhose().equals("Player 2")) {
										if(firstPlayer.getMoney() - 1400 < 0) {
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
											+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
											+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
											if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
												str = str + "Winner Player 1\n";
											}else {
												str = str + "Winner Player 2\n";
											}
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											break;
										}else {
											firstPlayer.setMoney(firstPlayer.getMoney() - 1400);
											secondPlayer.setMoney(secondPlayer.getMoney() + 1400);
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " paid rent for " + SQUARES.get(39).getName() + "\n";
										}
									}else {
										str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
												+ " has  " + SQUARES.get(39).getName() + "\n";
									}
								}
							}
							else if(SQUARES.get((counterp1 % 40) - 1).getName().equals("Go to Jail")) {
								counterp1 = 11;
								str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " went to jail\n";
								jailcountp1 = 0;
							}else if(SQUARES.get((counterp1 % 40) - 1).getName().equals("Jail")) {
								counterp1 = 11;
								str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " went to jail\n";
								jailcountp1 = 0;
							}else if(SQUARES.get((counterp1 % 40) - 1).getName().equals("Income Tax")) {
								if(firstPlayer.getMoney() - 100 < 0) {
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
									for(int i = 0; i < 107; i++) {
										str = str + "-";
									}
									str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
									+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
									+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
									if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
										str = str + "Winner Player 1\n";
									}else {
										str = str + "Winner Player 2\n";
									}
									for(int i = 0; i < 107; i++) {
										str = str + "-";
									}
									break;
								}else {
									firstPlayer.setMoney(firstPlayer.getMoney() - 100);
									banker.setMoney(banker.getMoney() + 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " paid Tax\n";
								}
							}else if(SQUARES.get((counterp1 % 40) - 1).getName().equals("Super Tax")) {
								if(firstPlayer.getMoney() - 100 < 0) {
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
									for(int i = 0; i < 107; i++) {
										str = str + "-";
									}
									str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
									+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
									+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
									if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
										str = str + "Winner Player 1\n";
									}else {
										str = str + "Winner Player 2\n";
									}
									for(int i = 0; i < 107; i++) {
										str = str + "-";
									}
									break;
								}else {
									firstPlayer.setMoney(firstPlayer.getMoney() - 100);
									banker.setMoney(banker.getMoney() + 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " paid Tax\n";
								}
							}else if(SQUARES.get((counterp1 % 40) - 1).getName().equals("Free Parking")) {
								str = str + temp[0] + "\t" + temp[1] + "\t"
								+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
								+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " is in Free Parking\n";
							}else if(SQUARES.get((counterp1 % 40) - 1).getName().equals("Community Chest")) {
								if(comChestCounter % 11 == 0) {
									counterp1 = 1;
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 200);
									banker.setMoney(banker.getMoney() - 200);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Advance to Go (Collect $200)\n";
									comChestCounter++;
								}else if(comChestCounter % 11 == 1) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 75);
									banker.setMoney(banker.getMoney() - 75);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Bank error in your favor - collect $75\n";
								}else if(comChestCounter % 11 == 2) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() - 50);
									banker.setMoney(banker.getMoney() + 50);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Doctor's fees - Pay $50\n";
								}else if(comChestCounter % 11 == 3) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 10);
									secondPlayer.setMoney(secondPlayer.getMoney() - 10);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw It is "
													+ "your birthday Collect $10 from each player\n";
								}else if(comChestCounter % 11 == 4) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 50);
									secondPlayer.setMoney(secondPlayer.getMoney() - 50);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Grand Opera Night - collect $50 from every player for opening night seats\n";
								}else if(comChestCounter % 11 == 5) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 20);
									banker.setMoney(banker.getMoney() - 20);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Income Tax refund - collect $20\n";
								}else if(comChestCounter % 11 == 6) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 100);
									banker.setMoney(banker.getMoney() - 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Life Insurance Matures - collect $100\n";
								}else if(comChestCounter % 11 == 7) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() - 100);
									banker.setMoney(banker.getMoney() + 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Pay Hospital Fees of $100\n";
								}else if(comChestCounter % 11 == 8) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() - 50);
									banker.setMoney(banker.getMoney() + 50);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Pay School Fees of $50\n";
								}else if(comChestCounter % 11 == 9) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 100);
									banker.setMoney(banker.getMoney() - 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw You inherit $100\n";
								}else if(comChestCounter % 11 == 10) {
									comChestCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 50);
									banker.setMoney(banker.getMoney() - 50);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " From sale of stock you get $50\n";
								}
							}else if(SQUARES.get((counterp1 % 40) - 1).getName().equals("Chance")) {
								if(chaCounter % 6 == 0) {
									counterp1 = 1;
									chaCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 200);
									banker.setMoney(banker.getMoney() - 200);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Advance to Go (Collect $200)\n";
								}else if(chaCounter % 6 == 1) {
									counterp1 = 27;
									chaCounter++;
									if(SQUARES.get(26).isStatus() == true) {
										if(SQUARES.get(26).getWhose().equals("Player 2")) {
											if(firstPlayer.getMoney() - 780 < 0) {
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
												+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
												+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
												if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
													str = str + "Winner Player 1\n";
												}else {
													str = str + "Winner Player 2\n";
												}
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												break;
											}else {
												firstPlayer.setMoney(firstPlayer.getMoney() - 780);
												secondPlayer.setMoney(secondPlayer.getMoney() + 780);
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Advance to Leicester Square Player "
														+ "1 paid rent for Leicester Square\n";
											}
										}else {
											str = str + temp[0] + "\t" + temp[1] + "\t"
													+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
													+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] +  " draw Advance to Leicester Square Player 1"
													+ " has  " + SQUARES.get(counterp1 % 40 - 1).getName() + "\n";
										}
									}else {
										if(firstPlayer.getMoney() - SQUARES.get(counterp1 % 40 - 1).getCost() < 0) {
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
											+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
											+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
											if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
												str = str + "Winner Player 1\n";
											}else {
												str = str + "Winner Player 2\n";
											}
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											break;
										}else {
											firstPlayer.setMoney(firstPlayer.getMoney() - SQUARES.get(counterp1 % 40  - 1).getCost());
											banker.setMoney(banker.getMoney() + SQUARES.get(counterp1 % 40 - 1).getCost());
											if(firstPlayer.getProperties().equals("")) {
												firstPlayer.setProperties(firstPlayer.getProperties() +  SQUARES.get(counterp1 % 40  - 1).getName());
											}else {
												firstPlayer.setProperties(firstPlayer.getProperties() + "," + SQUARES.get(counterp1 % 40  - 1).getName());
											}
											SQUARES.get(counterp1 % 40 - 1).setWhose("Player 1");
											SQUARES.get(counterp1 % 40  - 1).setStatus(true);
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Advance to Leicester Square Player 1"
											+ " bought " + SQUARES.get(counterp1 % 40  - 1).getName() + "\n";
										}
									}
								}else if(chaCounter % 6 == 2) {
									counterp1 -= 3;
									chaCounter++;
									if(SQUARES.get(counterp1 % 40 - 1).isStatus() == true) {
										if(SQUARES.get(counterp1 % 40 - 1).getWhose().equals("Player 2")) {
											int cost;
											int c = 0;
											if(counterp1 % 40 == 6 || counterp1 % 40 == 16 || counterp1 % 40 == 26 || counterp1 % 40 == 36) {
												if(SQUARES.get(5).whose == "Player 2") {
													c++;
												}if(SQUARES.get(15).whose == "Player 2") {
													c++;
												}if(SQUARES.get(25).whose == "Player 2") {
													c++;
												}if(SQUARES.get(35).whose == "Player 2") {
													c++;
												}
												cost = 25 * c;
											}else if(counterp1 % 40 == 13 || counterp1 % 40 == 29 ) {
												cost = Integer.parseInt(temp[1]) * 4;
											}
											else if(SQUARES.get(counterp1 % 40 - 1).getCost() > 3000) {
												cost = SQUARES.get(counterp1 % 40 - 1).getCost() * 35 / 100;
											}else if(SQUARES.get(counterp1 % 40 - 1).getCost() > 2000 && SQUARES.get(counterp1 % 40 - 1).getCost() <= 3000) {
												cost = SQUARES.get(counterp1 % 40 - 1).getCost() * 30 / 100;
											}else {
												cost = SQUARES.get(counterp1 % 40 - 1).getCost() * 40 / 100;
											}
											if(firstPlayer.getMoney() - cost < 0) {
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
												+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
												+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
												if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
													str = str + "Winner Player 1\n";
												}else {
													str = str + "Winner Player 2\n";
												}
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												break;
											}else {
												firstPlayer.setMoney(firstPlayer.getMoney() - cost);
												secondPlayer.setMoney(secondPlayer.getMoney() + cost);
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Go back 3 spaces Player "
														+ "1 paid rent for " + SQUARES.get(counterp1 % 40 - 1).getName() + "\n";
											}
										}else {
											str = str + temp[0] + "\t" + temp[1] + "\t"
													+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
													+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Go back 3 spaces Player 1"
													+ " has  " + SQUARES.get(counterp1 % 40 - 1).getName() + "\n";
										}
									}else {
										if(firstPlayer.getMoney() - SQUARES.get(counterp1 % 40 - 1).getCost() < 0) {
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
											+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
											+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
											if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
												str = str + "Winner Player 1\n";
											}else {
												str = str + "Winner Player 2\n";
											}
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											break;
										}else {
											firstPlayer.setMoney(firstPlayer.getMoney() - SQUARES.get(counterp1 % 40  - 1).getCost());
											banker.setMoney(banker.getMoney() + SQUARES.get(counterp1 % 40 - 1).getCost());
											if(firstPlayer.getProperties().equals("")) {
												firstPlayer.setProperties(firstPlayer.getProperties() +  SQUARES.get(counterp1 % 40  - 1).getName());
											}else {
												firstPlayer.setProperties(firstPlayer.getProperties() + "," + SQUARES.get(counterp1 % 40  - 1).getName());
											}
											SQUARES.get(counterp1 % 40 - 1).setWhose("Player 1");
											SQUARES.get(counterp1 % 40  - 1).setStatus(true);
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]+ " draw Go back 3 spaces Player 1"
											+ " bought " + SQUARES.get(counterp1 % 40  - 1).getName() + "\n";
										}
									}
								}else if(chaCounter % 6 == 3) {
									chaCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() - 10);
									banker.setMoney(banker.getMoney() + 10);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Pay poor tax of $15\n";
								}else if(chaCounter % 6 == 4) {
									chaCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 150);
									banker.setMoney(banker.getMoney() - 150);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Your building loan matures - collect $150\n";
								}else if(chaCounter % 6 == 5) {
									chaCounter++;
									firstPlayer.setMoney(firstPlayer.getMoney() + 100);
									banker.setMoney(banker.getMoney() - 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw You have won a crossword competition - collect $100 \n";
								}
							}else {
								if(SQUARES.get(counterp1 % 40  - 1).isStatus() == false) {
									if(firstPlayer.getMoney() - SQUARES.get(counterp1 % 40 - 1).getCost() < 0) {
										str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
										for(int i = 0; i < 107; i++) {
											str = str + "-";
										}
										str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
										+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
										+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
										if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
											str = str + "Winner Player 1\n";
										}else {
											str = str + "Winner Player 2\n";
										}
										for(int i = 0; i < 107; i++) {
											str = str + "-";
										}
										break;
									}else {
										firstPlayer.setMoney(firstPlayer.getMoney() - SQUARES.get(counterp1 % 40  - 1).getCost());
										banker.setMoney(banker.getMoney() + SQUARES.get(counterp1 % 40 - 1).getCost());
										if(firstPlayer.getProperties().equals("")) {
											firstPlayer.setProperties(firstPlayer.getProperties() +  SQUARES.get(counterp1 % 40  - 1).getName());
										}else {
											firstPlayer.setProperties(firstPlayer.getProperties() + "," + SQUARES.get(counterp1 % 40  - 1).getName());
										}
										SQUARES.get(counterp1 % 40 - 1).setWhose("Player 1");
										SQUARES.get(counterp1 % 40  - 1).setStatus(true);
										str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
										+ " bought " + SQUARES.get(counterp1 % 40  - 1).getName() + "\n";
									}
								}else {
									if((SQUARES).get(counterp1 % 40 - 1).getWhose().equals("Player 2")) {
										int cost;
										int c = 0;
										if(counterp1 % 40 == 6 || counterp1 % 40 == 16 || counterp1 % 40 == 26 || counterp1 % 40 == 36) {
											if(SQUARES.get(5).whose == "Player 2") {
												c++;
											}if(SQUARES.get(15).whose == "Player 2") {
												c++;
											}if(SQUARES.get(25).whose == "Player 2") {
												c++;
											}if(SQUARES.get(35).whose == "Player 2") {
												c++;
											}
											cost = 25 * c;
										}else if(counterp1 % 40 == 13 || counterp1 % 40 == 29 ) {
											cost = Integer.parseInt(temp[1]) * 4;
										}
										if(SQUARES.get(counterp1 % 40 - 1).getCost() > 3000) {
											cost = SQUARES.get(counterp1 % 40 - 1).getCost() * 35 / 100;
										}else if(SQUARES.get(counterp1 % 40 - 1).getCost() > 2000 && SQUARES.get(counterp1 % 40 - 1).getCost() <= 3000) {
											cost = SQUARES.get(counterp1 % 40 - 1).getCost() * 30 / 100;
										}else {
											cost = SQUARES.get(counterp1 % 40 - 1).getCost() * 40 / 100;
										}
										if(firstPlayer.getMoney() - cost < 0) {
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
											+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
											+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
											if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
												str = str + "Winner Player 1\n";
											}else {
												str = str + "Winner Player 2\n";
											}
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											break;
										}else {
											firstPlayer.setMoney(firstPlayer.getMoney() - cost);
											secondPlayer.setMoney(secondPlayer.getMoney() + cost);
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " paid rent for " + SQUARES.get(counterp1 % 40 - 1).getName() + "\n";
										}
									}else {
										str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp1 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
												+ " has  " + SQUARES.get(counterp1 % 40 - 1).getName() + "\n";
									}
								}
							}
						}
					}else if(temp[0].equals("Player 2")) {
						if(jailcountp2 != 3) {
							jailcountp2++;
							str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() +
									"\t" + temp[0] + " in jail (count=" + jailcountp2 + ")\n";
						}else {
							counterp2 += Integer.parseInt(temp[1]);
							if(counterp2 > 40) {
								secondPlayer.setMoney(secondPlayer.getMoney() + 200);
								banker.setMoney(banker.getMoney() - 200);
								counterp2 = counterp2 % 40;
							}
							if(counterp2 % 40 == 1) {
								str = str + temp[0] + "\t" + temp[1] + "\t"
										+ 1 + "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " is in GO square\n";
							}
							else if(counterp2 % 40 == 0) {
									if(SQUARES.get(39).isStatus() == false) {
										if(secondPlayer.getMoney() - SQUARES.get(39).getCost() < 0) {
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ 40 + "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
											+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
											+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
											if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
												str = str + "Winner Player 1\n";
											}else {
												str = str + "Winner Player 2\n";
											}
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											break;
										}else {
											secondPlayer.setMoney(secondPlayer.getMoney() - SQUARES.get(39).getCost());
											banker.setMoney(banker.getMoney() + SQUARES.get(39).getCost());
											if(secondPlayer.getProperties().equals("")) {
												secondPlayer.setProperties(secondPlayer.getProperties() +  SQUARES.get(39).getName());
											}else {
												secondPlayer.setProperties(secondPlayer.getProperties() + "," + SQUARES.get(39).getName());
											}
											SQUARES.get(39).setWhose("Player 2");
											SQUARES.get(39).setStatus(true);
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
											+ " bought " + SQUARES.get(39).getName() + "\n";
										}
									}else {
										if((SQUARES).get(39).getWhose().equals("Player 1")) {
											if(secondPlayer.getMoney() - 1400 < 0) {
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
												+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
												+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
												if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
													str = str + "Winner Player 1\n";
												}else {
													str = str + "Winner Player 2\n";
												}
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												break;
											}else {
												secondPlayer.setMoney(secondPlayer.getMoney() - 1400);
												firstPlayer.setMoney(firstPlayer.getMoney() + 1400);
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " paid rent for " + SQUARES.get(39).getName() + "\n";
											}
										}else {
											str = str + temp[0] + "\t" + temp[1] + "\t"
													+ (40)+ "\t" + firstPlayer.getMoney()
													+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " has  " + SQUARES.get(39).getName() + "\n";
										}
									}
							}
							else if(SQUARES.get((counterp2 % 40) - 1).getName().equals("Go to Jail")) {
								counterp2 = 11;
								str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " went to jail\n";
								jailcountp2 = 0;
							}else if(SQUARES.get((counterp2 % 40) - 1).getName().equals("Jail")) {
								counterp2 = 11;
								str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " went to jail\n";
								jailcountp2 = 0;
							}else if(SQUARES.get((counterp2 % 40) - 1).getName().equals("Income Tax")) {
								if(secondPlayer.getMoney() - 100 < 0) {
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
									for(int i = 0; i < 107; i++) {
										str = str + "-";
									}
									str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
									+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
									+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
									if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
										str = str + "Winner Player 1\n";
									}else {
										str = str + "Winner Player 2\n";
									}
									for(int i = 0; i < 107; i++) {
										str = str + "-";
									}
									break;
								}else {
									secondPlayer.setMoney(secondPlayer.getMoney() - 100);
									banker.setMoney(banker.getMoney() + 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " paid Tax\n";
								}
							}else if(SQUARES.get((counterp2 % 40) - 1).getName().equals("Super Tax")) {
								if(secondPlayer.getMoney() - 100 < 0) {
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
									for(int i = 0; i < 107; i++) {
										str = str + "-";
									}
									str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
									+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
									+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
									if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
										str = str + "Winner Player 1\n";
									}else {
										str = str + "Winner Player 2\n";
									}
									for(int i = 0; i < 107; i++) {
										str = str + "-";
									}
									break;
								}else {
									secondPlayer.setMoney(secondPlayer.getMoney() - 100);
									banker.setMoney(banker.getMoney() + 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " paid Tax\n";
								}
							}else if(SQUARES.get((counterp2 % 40) - 1).getName().equals("Free Parking")) {
								str = str + temp[0] + "\t" + temp[1] + "\t"
								+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
								+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " is in Free Parking\n";
							}else if(SQUARES.get((counterp2 % 40) - 1).getName().equals("Community Chest")) {
								if(comChestCounter % 11 == 0) {
									counterp2 = 1;
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 200);
									banker.setMoney(banker.getMoney() - 200);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Advance to Go (Collect $200)\n";
									comChestCounter++;
								}else if(comChestCounter % 11 == 1) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 75);
									banker.setMoney(banker.getMoney() - 75);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Bank error in your favor - collect $75\n";
								}else if(comChestCounter % 11 == 2) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() - 50);
									banker.setMoney(banker.getMoney() + 50);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Doctor's fees - Pay $50\n";
								}else if(comChestCounter % 11 == 3) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 10);
									firstPlayer.setMoney(firstPlayer.getMoney() - 10);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw It is "
													+ "your birthday Collect $10 from each player\n";
								}else if(comChestCounter % 11 == 4) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 50);
									firstPlayer.setMoney(firstPlayer.getMoney() - 50);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Grand Opera Night - collect $50 from every player for opening night seats\n";
								}else if(comChestCounter % 11 == 5) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 20);
									banker.setMoney(banker.getMoney() - 20);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Income Tax refund - collect $20\n";
								}else if(comChestCounter % 11 == 6) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 100);
									banker.setMoney(banker.getMoney() - 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Life Insurance Matures - collect $100\n";
								}else if(comChestCounter % 11 == 7) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() - 100);
									banker.setMoney(banker.getMoney() + 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Pay Hospital Fees of $100\n";
								}else if(comChestCounter % 11 == 8) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() - 50);
									banker.setMoney(banker.getMoney() + 50);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Pay School Fees of $50\n";
								}else if(comChestCounter % 11 == 9) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 100);
									banker.setMoney(banker.getMoney() - 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw You inherit $100\n";
								}else if(comChestCounter % 11 == 10) {
									comChestCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 50);
									banker.setMoney(banker.getMoney() - 50);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " From sale of stock you get $50\n";
								}
							}else if(SQUARES.get((counterp2 % 40) - 1).getName().equals("Chance")) {
								if(chaCounter % 6 == 0) {
									counterp2 = 1;
									chaCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 200);
									banker.setMoney(banker.getMoney() - 200);
									str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
													+ " draw Advance to Go (Collect $200)\n";
								}else if(chaCounter % 6 == 1) {
									counterp2 = 27;
									chaCounter++;
									if(SQUARES.get(26).isStatus() == true) {
										if(SQUARES.get(26).getWhose().equals("Player 1")) {
											if(secondPlayer.getMoney() - 780 < 0) {
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
												+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
												+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
												if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
													str = str + "Winner Player 1\n";
												}else {
													str = str + "Winner Player 2\n";
												}
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												break;
											}else {
												secondPlayer.setMoney(secondPlayer.getMoney() - 780);
												firstPlayer.setMoney(firstPlayer.getMoney() + 780);
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Advance to Leicester Square Player "
														+ "2 paid rent for Leicester Square\n";
											}
										}else {
											str = str + temp[0] + "\t" + temp[1] + "\t"
													+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
													+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] +  " draw Advance to Leicester Square Player 2"
													+ " has  " + SQUARES.get(counterp2 % 40 - 1).getName() + "\n";
										}
									}else {
										if(secondPlayer.getMoney() - SQUARES.get(counterp2 % 40 - 1).getCost() < 0) {
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
											+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
											+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
											if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
												str = str + "Winner Player 1\n";
											}else {
												str = str + "Winner Player 2\n";
											}
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											break;
										}else {
											secondPlayer.setMoney(secondPlayer.getMoney() - SQUARES.get(counterp2 % 40  - 1).getCost());
											banker.setMoney(banker.getMoney() + SQUARES.get(counterp2 % 40 - 1).getCost());
											if(secondPlayer.getProperties().equals("")) {
												secondPlayer.setProperties(secondPlayer.getProperties() +  SQUARES.get(counterp2 % 40  - 1).getName());
											}else {
												secondPlayer.setProperties(secondPlayer.getProperties() + "," + SQUARES.get(counterp2 % 40  - 1).getName());
											}
											SQUARES.get(counterp2 % 40 - 1).setWhose("Player 2");
											SQUARES.get(counterp2 % 40  - 1).setStatus(true);
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] +" draw Advance to Leicester Square Player 2"
											+ " bought " + SQUARES.get(counterp2 % 40  - 1).getName() + "\n";
										}
									}
								}else if(chaCounter % 6 == 2) {
									counterp2 -= 3;
									chaCounter++;
									if(SQUARES.get(counterp2 % 40 - 1).isStatus() == true) {
										if(SQUARES.get(counterp2 % 40 - 1).getWhose().equals("Player 1")) {
											int cost;
											int c = 0;
											if(counterp2 % 40 == 6 || counterp2 % 40 == 16 || counterp2 % 40 == 26 || counterp2 % 40 == 36) {
												if(SQUARES.get(5).whose == "Player 1") {
													c++;
												}if(SQUARES.get(15).whose == "Player 1") {
													c++;
												}if(SQUARES.get(25).whose == "Player 1") {
													c++;
												}if(SQUARES.get(35).whose == "Player 1") {
													c++;
												}
												cost = 25 * c;
											}else if(counterp2 % 40 == 13 || counterp2 % 40 == 29 ) {
												cost = Integer.parseInt(temp[1]) * 4;
											}
											if(SQUARES.get(counterp2 % 40 - 1).getCost() > 3000) {
												cost = SQUARES.get(counterp2 % 40 - 1).getCost() * 35 / 100;
											}else if(SQUARES.get(counterp2 % 40 - 1).getCost() > 2000 && SQUARES.get(counterp2 % 40 - 1).getCost() <= 3000) {
												cost = SQUARES.get(counterp2 % 40 - 1).getCost() * 30 / 100;
											}else {
												cost = SQUARES.get(counterp2 % 40 - 1).getCost() * 40 / 100;
											}
											if(secondPlayer.getMoney() - cost < 0) {
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
												+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
												+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
												if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
													str = str + "Winner Player 1\n";
												}else {
													str = str + "Winner Player 2\n";
												}
												for(int i = 0; i < 107; i++) {
													str = str + "-";
												}
												break;
											}else {
												secondPlayer.setMoney(secondPlayer.getMoney() - cost);
												firstPlayer.setMoney(firstPlayer.getMoney() + cost);
												str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Go back 3 spaces Player "
														+ "2 paid rent for " + SQUARES.get(counterp2 % 40 - 1).getName() + "\n";
											}
										}else {
											str = str + temp[0] + "\t" + temp[1] + "\t"
													+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
													+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Go back 3 spaces Player 2"
													+ " has  " + SQUARES.get(counterp2 % 40 - 1).getName() + "\n";
										}
									}else {
										if(secondPlayer.getMoney() - SQUARES.get(counterp2 % 40 - 1).getCost() < 0) {
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
											+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
											+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
											if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
												str = str + "Winner Player 1\n";
											}else {
												str = str + "Winner Player 2\n";
											}
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											break;
										}else {
											secondPlayer.setMoney(secondPlayer.getMoney() - SQUARES.get(counterp2 % 40  - 1).getCost());
											banker.setMoney(banker.getMoney() + SQUARES.get(counterp2 % 40 - 1).getCost());
											if(secondPlayer.getProperties().equals("")) {
												secondPlayer.setProperties(secondPlayer.getProperties() +  SQUARES.get(counterp2 % 40  - 1).getName());
											}else {
												secondPlayer.setProperties(secondPlayer.getProperties() + "," + SQUARES.get(counterp2 % 40  - 1).getName());
											}
											SQUARES.get(counterp2 % 40 - 1).setWhose("Player 2");
											SQUARES.get(counterp2 % 40  - 1).setStatus(true);
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Go back 3 spaces Player 2"
											+ " bought " + SQUARES.get(counterp2 % 40  - 1).getName() + "\n";
										}
									}
								}else if(chaCounter % 6 == 3) {
									chaCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() - 10);
									banker.setMoney(banker.getMoney() + 10);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Pay poor tax of $15\n";
								}else if(chaCounter % 6 == 4) {
									chaCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 150);
									banker.setMoney(banker.getMoney() - 150);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw Your building loan matures - collect $150\n";
								}else if(chaCounter % 6 == 5) {
									chaCounter++;
									secondPlayer.setMoney(secondPlayer.getMoney() + 100);
									banker.setMoney(banker.getMoney() - 100);
									str = str + temp[0] + "\t" + temp[1] + "\t"
									+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
									+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " draw You have won a crossword competition - collect $100 \n";
								}
							}else {
								if(SQUARES.get(counterp2 % 40  - 1).isStatus() == false) {
									if(secondPlayer.getMoney() - SQUARES.get(counterp2 % 40 - 1).getCost() < 0) {
										str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
										for(int i = 0; i < 107; i++) {
											str = str + "-";
										}
										str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
										+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
										+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
										if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
											str = str + "Winner Player 1\n";
										}else {
											str = str + "Winner Player 2\n";
										}
										for(int i = 0; i < 107; i++) {
											str = str + "-";
										}
										break;
									}else {
										secondPlayer.setMoney(secondPlayer.getMoney() - SQUARES.get(counterp2 % 40  - 1).getCost());
										banker.setMoney(banker.getMoney() + SQUARES.get(counterp2 % 40 - 1).getCost());
										if(secondPlayer.getProperties().equals("")) {
											secondPlayer.setProperties(secondPlayer.getProperties() +  SQUARES.get(counterp2 % 40  - 1).getName());
										}else {
											secondPlayer.setProperties(secondPlayer.getProperties() + "," + SQUARES.get(counterp2 % 40  - 1).getName());
										}
										SQUARES.get(counterp2 % 40 - 1).setWhose("Player 2");
										SQUARES.get(counterp2 % 40  - 1).setStatus(true);
										str = str + temp[0] + "\t" + temp[1] + "\t"
										+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
										+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
										+ " bought " + SQUARES.get(counterp2 % 40  - 1).getName() + "\n";
									}
								}else {
									if((SQUARES).get(counterp2 % 40 - 1).getWhose().equals("Player 1")) {
										int cost;
										int c = 0;
										if(counterp2 % 40 == 6 || counterp2 % 40 == 16 || counterp2 % 40 == 26 || counterp2 % 40 == 36) {
											if(SQUARES.get(5).whose == "Player 1") {
												c++;
											}if(SQUARES.get(15).whose == "Player 1") {
												c++;
											}if(SQUARES.get(25).whose == "Player 1") {
												c++;
											}if(SQUARES.get(35).whose == "Player 1") {
												c++;
											}
											cost = 25 * c;
										}else if(counterp2 % 40 == 13 || counterp2 % 40 == 29 ) {
											cost = Integer.parseInt(temp[1]) * 4;
										}
										if(SQUARES.get(counterp2 % 40 - 1).getCost() > 3000) {
											cost = SQUARES.get(counterp2 % 40 - 1).getCost() * 35 / 100;
										}else if(SQUARES.get(counterp2 % 40 - 1).getCost() > 2000 && SQUARES.get(counterp2 % 40 - 1).getCost() <= 3000) {
											cost = SQUARES.get(counterp2 % 40 - 1).getCost() * 30 / 100;
										}else {
											cost = SQUARES.get(counterp2 % 40 - 1).getCost() * 40 / 100;
										}
										if(secondPlayer.getMoney() - cost < 0) {
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " goes bankrupt\n";
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
											+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
											+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
											if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
												str = str + "Winner Player 1\n";
											}else {
												str = str + "Winner Player 2\n";
											}
											for(int i = 0; i < 107; i++) {
												str = str + "-";
											}
											break;
										}else {
											secondPlayer.setMoney(secondPlayer.getMoney() - cost);
											firstPlayer.setMoney(firstPlayer.getMoney() + cost);
											str = str + temp[0] + "\t" + temp[1] + "\t"
											+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
											+ "\t" + secondPlayer.getMoney() + "\t" + temp[0] + " paid rent for " + SQUARES.get(counterp2 % 40 - 1).getName() + "\n";
										}
									}else {
										str = str + temp[0] + "\t" + temp[1] + "\t"
												+ (counterp2 % 40)+ "\t" + firstPlayer.getMoney()
												+ "\t" + secondPlayer.getMoney() + "\t" + temp[0]
												+ " has  " + SQUARES.get(counterp2 % 40 - 1).getName() + "\n";
									}
								}
							} }
					}
				}
				line = reader.readLine();
				if(line == null) {
					for(int i = 0; i < 107; i++) {
						str = str + "-";
					}
					str = str + "\nPlayer 1\t" + firstPlayer.getMoney() + "\thave: "
					+ firstPlayer.getProperties() + "\nPlayer 2\t" + secondPlayer.getMoney()
					+ "\thave: " + secondPlayer.getProperties() + "\nBanker\t" + banker.getMoney() + "\n";
					if(firstPlayer.getMoney() > secondPlayer.getMoney()) {
						str = str + "Winner Player 1\n";
					}else {
						str = str + "Winner Player 2\n";
					}
					for(int i = 0; i < 107; i++) {
						str = str + "-";
					}
				}
				}
		writer.write(str);
		writer.close();
		reader.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}