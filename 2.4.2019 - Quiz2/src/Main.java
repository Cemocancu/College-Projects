import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main{
	
	Scanner reader = new Scanner(System.in);
	
	
	public  class SportCenter{
		//Attributes
		public String sportcenter_name;
		public String[] Member = new String[10];
		public int count = 0;
		public int count0 = 0;
		//Constructor
		public SportCenter(){
		}
		//Methods
		public void addMember(){
			System.out.println("Enter name");
			String input_name = reader.nextLine();
			System.out.println("Enter surname");
			String input_surname = reader.nextLine();
			System.out.println("Enter weight");
			double input_weight = reader.nextDouble();
			System.out.println("Enter height");
			double input_height = reader.nextDouble();
			reader.nextLine();
			System.out.print("\n");
			Member member = new Member(count, input_name, input_surname, input_weight, input_height);
			Member member0 = member;
			Member[count] = "Id:" + member0.getMemberid() + " Name:" + member0.getName() + " Surname:" + member0.getSurname() + " Weight:" + member0.getWeight() + " Height:" + member0.getHeight();
			count++;
			count0++;
		}
		public void printAllMembers() {
			for(int i= 0; i<=count0 - 1; i++) {
				System.out.println(Member[i]);
			}
			System.out.print("\n");
				
		}
		public void search(String name, String surname) {
			int var;
			int count1 = 0;
			double w = 0;
			double h = 0;
			for(int i = 0; i<= 9; i++) {
				var = Member[i].indexOf(name);
				if (var != -1) {
					var = Member[i].indexOf(surname);
					if (var != -1) {
						Pattern p = Pattern.compile("\\d+\\.\\d+");
						Matcher m = p.matcher(Member[i]);
						while (m.find()) {
						   double m_h = Double.parseDouble(m.group());
						   if (count1 == 0) {
							   h = m_h;
							   count1++;
						   }else if (count1 == 1) {
							   w = m_h;
							   count1 = 0;
						   }
						}
						Member member1 = new Member();
						member1.WeightStatus(w, h);
						break;
					}
				}
			}
		}
	}
	
	public class Member{
		//Attributes
		private int memberid;
		private String name;
		private String surname;
		private double height;
		private double weight;
		//Constructor
		public Member(int memberid0, String name0, String surname0, double height0, double weight0){
			memberid = memberid0;
			name = name0;
			surname = surname0;
			height = height0;
			weight = weight0;
		}
		public Member() {
			
		}
		//Methods
		public void Display(){

		}
		public double BMI(double w, double h) {
			return w / (h * h);
		}
		public void WeightStatus(double w, double h) {
			double var = BMI(w, h);
			if (var < 18.5) {
				System.out.println("Underweight\n");
			} else if (Double.compare(var, 24.9) < 0) {
				System.out.println("Normal\n");
			} else if (var <= 29.9) {
				System.out.println("Overweight\n");
			} else if (var <= 34.9) {
				System.out.println("Obese\n");
			} else if (var >= 35.0) {
				System.out.println("Extremely obese\n");
			}
		}
		public int getMemberid() {
			return memberid;
		}
		public void setMemberid(int memberid) {
			this.memberid = memberid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSurname() {
			return surname;
		}
		public void setSurname(String surname) {
			this.surname = surname;
		}
		public double getHeight() {
			return height;
		}
		public void setHeight(double height) {
			this.height = height;
		}
		public double getWeight() {
			return weight;
		}
		public void setWeight(double weight) {
			this.weight = weight;
		}
	}

	public static void main(String[] args){
		Main main = new Main();
		Scanner reader = new Scanner(System.in);
		int user_choice0;
		SportCenter sportcenter = main.new SportCenter();
		do {
			System.out.println("1-Add a new member\n2-Display all members\n3-Search\n4-Exit\nEnter your choose");
			int user_choice = reader.nextInt();
			reader.nextLine();
			user_choice0 = user_choice;
			if (user_choice == 1) {
				sportcenter.addMember();
			} else if (user_choice == 2) {
				sportcenter.printAllMembers();
			} else if (user_choice == 3) {
				System.out.println("Enter name");
				String user_name = reader.nextLine();
				System.out.println("Enter surname");
				String user_surname = reader.nextLine();
				sportcenter.search(user_name, user_surname);
			}
		} while(user_choice0 != 4);
		reader.close();
	}
}
