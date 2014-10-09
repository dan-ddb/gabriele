package sfcabm;

public class Curriculum {
	int senderDegree;
	int senderID;
	int senderAge;
	double senderReservationWage;

	public Curriculum(int dg,int id,int age,double srw){
		senderDegree=dg;
		senderID=id;
		senderAge=age;
		senderReservationWage=srw;
	}
	public int getSenderDegree(){
		return senderDegree;
	}
	public int getSenderID(){
		return senderID;
	}
	public int getSenderAge(){
		return senderAge;
	}
	public double getSenderReservationWage(){
		return senderReservationWage;
	}
}
