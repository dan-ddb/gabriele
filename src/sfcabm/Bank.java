package sfcabm;

import java.util.ArrayList;
import java.util.Iterator;
import repast.simphony.random.RandomHelper;

public class Bank {
	ArrayList<BankAccount> accountsList=new ArrayList<BankAccount>();
	BankAccount aBankAccount;
	repast.simphony.context.Context<Object> myContext;
	Iterator bankAccountsListIterator;
	int identity;
	
	public double iL;
	double deposits,loans,demandedCredit,allowedCredit,equity;
	Consumer aConsumer;
	Firm aFirm;

	public Bank(int id,repast.simphony.context.Context<Object> con){
		identity=id;
		myContext=con;
		if(Context.verboseFlag){
		System.out.println("Bank "+identity+" created");
		}
	}
	public void addAccount(BankAccount ba){
		accountsList.add(ba);
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" added account");
		}
	}
	public void computeDemandedCredit(){
		demandedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			demandedCredit=demandedCredit-aBankAccount.getDemandedCredit();
		}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" demanded credit "+demandedCredit);
		}
	}
	public void computeDeposits(){
		deposits=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getAccount()>0){
				deposits=deposits+aBankAccount.getAccount();
		}
	}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" deposits "+deposits);
		}

	}
	public void setupBalance(){
		equity=demandedCredit*0.1;
		double adjustmentFactor=demandedCredit*0.9/deposits;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getAccount()>0){
				aBankAccount.setAccount(aBankAccount.getAccount()*adjustmentFactor);
			}
			else{
				aBankAccount.setAccount(aBankAccount.getDemandedCredit());
			}
		}
		if(Context.verboseFlag){
		System.out.println("   BANK "+identity+" COORDINATING customers' balances with my balance for consistency ");
		}
	}
	public void computeBalanceVariables(){
		deposits=0;
		loans=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getAccount()>0){
				deposits=deposits+aBankAccount.getAccount();
		}
		else{
			loans=loans-aBankAccount.getAccount();
		}
	}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" deposits "+deposits+" loans "+loans);
		}

	}
	public void updateConsumersAccounts(){
		double tmpAccount;
		String tmpOwnerType;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			tmpAccount=aBankAccount.getAccount();
			tmpOwnerType=aBankAccount.getOwnerType();
			if(tmpOwnerType=="consumer"){
				aConsumer=(Consumer)aBankAccount.getOwner();
				if(tmpAccount>0){
						if(Context.verboseFlag){
						System.out.println("     consumer "+aConsumer.getIdentity()+": account "+tmpAccount+" interst rate +"+Context.interestRateOnDeposits);
						}
					aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnDeposits));
					aBankAccount.setDemandedCredit(0);
					aBankAccount.setAllowedCredit(0);
				}
				else{
					if(aConsumer.getIsStudentFlag()){
						if(Context.verboseFlag){
						System.out.println("     student "+aConsumer.getIdentity()+": account "+tmpAccount+" interst rate -"+Context.interestRateOnSubsidizedLoans+" rufund not Asked");
						}
						aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnSubsidizedLoans));
						aBankAccount.setDemandedCredit(aBankAccount.getAccount());
						aBankAccount.setAllowedCredit(aBankAccount.getAccount());
					}
					else{
						if(aConsumer.getIsWorkingFlag()){
						if(Context.verboseFlag){
						System.out.println("     worker "+aConsumer.getIdentity()+": account "+tmpAccount+" interst rate -"+Context.interestRateOnLoans);
						}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnLoans));
							aBankAccount.setDemandedCredit(aBankAccount.getAccount());
							if(RandomHelper.nextDouble()>0.5){
								if(Context.verboseFlag){
									System.out.println("      refund not asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount());
							}
							else{
								if(Context.verboseFlag){
									System.out.println("      refund asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount()*0.9);
							}
						}
						else{
							if(Context.verboseFlag){
								System.out.println("     unemployed "+aConsumer.getIdentity()+": account "+tmpAccount+" interst rate -"+Context.interestRateOnSubsidizedLoans+" rufund not Asked");
							}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnSubsidizedLoans));
							aBankAccount.setDemandedCredit(aBankAccount.getAccount());
							aBankAccount.setAllowedCredit(aBankAccount.getAccount());
						}
					}
				}

			}

		}
	}

	public void updateFirmsAccounts(){
		double tmpAccount;
		String tmpOwnerType;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			tmpAccount=aBankAccount.getAccount();
			tmpOwnerType=aBankAccount.getOwnerType();
			if(tmpOwnerType=="firm"){
				aFirm=(Firm)aBankAccount.getOwner();
				if(tmpAccount>0){
					if(Context.verboseFlag){
						System.out.println("     "+tmpOwnerType+" "+aFirm.getIdentity()+": account "+tmpAccount+" interst rate +"+Context.interestRateOnDeposits);
					}
					aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnDeposits));
					aBankAccount.setDemandedCredit(0);
					aBankAccount.setAllowedCredit(0);
				}
				else{
						if(Context.verboseFlag){
						System.out.println("     "+tmpOwnerType+" "+aFirm.getIdentity()+": account "+tmpAccount+" interst rate -"+Context.interestRateOnLoans);
						}
							aBankAccount.setAccount(tmpAccount*(1+Context.interestRateOnLoans));
							aBankAccount.setDemandedCredit(aBankAccount.getAccount());
							if(RandomHelper.nextDouble()>0.5){
								if(Context.verboseFlag){
									System.out.println("      refund not asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount());
							}
							else{
								if(Context.verboseFlag){
									System.out.println("      refund asked");
								}
								aBankAccount.setAllowedCredit(aBankAccount.getAccount()*0.9);
							}
				}
	
				if(Context.verboseFlag){
					System.out.println("      -----------");
				}
			}
		}

	}



	public void resetConsumersDemandedAndAllowedCredit(){
		demandedCredit=0;
		allowedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getOwnerType()=="consumer"){
				if(aBankAccount.getDemandedCredit()<aBankAccount.getAllowedCredit()){
					aBankAccount.resetAllowedCredit();
				}
				else{
					aBankAccount.resetDemandedCredit();
					aBankAccount.resetAllowedCredit();
				}
			}
		}
	}
	public void resetFirmsDemandedAndAllowedCredit(){
		demandedCredit=0;
		allowedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getOwnerType()=="firm"){
					aBankAccount.resetDemandedCredit();
					aBankAccount.resetAllowedCredit();
/*				if(aBankAccount.getDemandedCredit()<aBankAccount.getAllowedCredit()){
					aBankAccount.resetAllowedCredit();
				}
				else{
					aBankAccount.resetDemandedCredit();
					aBankAccount.resetAllowedCredit();
				}
*/
			}
		}
	}



/**
 *Bank decides if credit demanded by consumers is extended; outstanding credit cannot be reduced here; Credit reduction was performed by bank in the updateConsumersAccounts method  
 */
	public void setAllowedConsumersCredit(){
		demandedCredit=0;
		allowedCredit=0;
		double anAccountAmount,anAccountDesiredCredit,anAccounAllowedCredit,multiplier;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getOwnerType()=="consumer"){
				aConsumer=(Consumer)aBankAccount.getOwner();
				anAccountAmount=aBankAccount.getAccount();
				anAccountDesiredCredit=aBankAccount.getDemandedCredit();
				demandedCredit+=-anAccountDesiredCredit;
				if(anAccountAmount>=0){
					if(RandomHelper.nextDouble()>0.5){
						multiplier=0.5;
					}
					else{
						multiplier=1.0;
					}
					if(aConsumer.getIsStudentFlag()){
						multiplier=1.0;
					}
					anAccounAllowedCredit=multiplier*anAccountDesiredCredit;
				}
				else{
					if(RandomHelper.nextDouble()>0.5){
						multiplier=0.5;
					}
					else{
						multiplier=1.0;
					}
					if(aConsumer.getIsStudentFlag()){
						multiplier=1.0;
					}
					if(anAccountDesiredCredit<0){
						anAccounAllowedCredit=anAccountAmount+multiplier*(anAccountDesiredCredit-anAccountAmount);
					}
					else{
						anAccounAllowedCredit=0;
					}


				}
				allowedCredit+=-anAccounAllowedCredit;
				System.out.println("      askedCredit "+aBankAccount.getDemandedCredit());
				aBankAccount.setAllowedCredit(anAccounAllowedCredit);
			}
		}
		if(Context.verboseFlag){
			System.out.println("     bank "+identity+" demanded credit "+demandedCredit+" allowed credit "+allowedCredit);
		}
	}

	public void setAllowedFirmsCredit(){
		demandedCredit=0;
		allowedCredit=0;
		double anAccountAmount,anAccountDesiredCredit,anAccounAllowedCredit,multiplier;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			if(aBankAccount.getOwnerType()=="firm"){
				anAccountAmount=aBankAccount.getAccount();
				anAccountDesiredCredit=aBankAccount.getDemandedCredit();
				demandedCredit=demandedCredit-anAccountDesiredCredit;
				if(anAccountAmount>=0){
					if(RandomHelper.nextDouble()>0.5){
						multiplier=0.5;
					}
					else{
						multiplier=1.0;
					}
					anAccounAllowedCredit=multiplier*anAccountDesiredCredit;
				}
				else{
					if(RandomHelper.nextDouble()>0.5){
						multiplier=0.5;
					}
					else{
						multiplier=1.0;
					}
					if(anAccountDesiredCredit<0){
						anAccounAllowedCredit=anAccountAmount+multiplier*(anAccountDesiredCredit-anAccountAmount);
					}
					else{
						anAccounAllowedCredit=0;
					}


				}
				allowedCredit+=-anAccounAllowedCredit;
				System.out.println("      askedCredit "+aBankAccount.getDemandedCredit());
				aBankAccount.setAllowedCredit(anAccounAllowedCredit);
			}
		}
		if(Context.verboseFlag){
			System.out.println("     bank "+identity+" demanded credit "+demandedCredit+" allowed credit "+allowedCredit);
		}
	}
	
	public void removeExitedFirmsBankAccounts(){
		bankAccountsListIterator=accountsList.iterator();
		while(bankAccountsListIterator.hasNext()){
			aBankAccount=(BankAccount)bankAccountsListIterator.next();
			if(aBankAccount.getAccountShutDownFlag()){
				bankAccountsListIterator.remove();
		if(Context.verboseFlag){
			System.out.println("     bank "+identity+" account removed ");
		}
			}
		}

	}


/*
	public void setAllowedFirmsCredit(){
		demandedCredit=0;
		allowedCredit=0;
		for(int i=0;i<accountsList.size();i++){
			aBankAccount=(BankAccount)accountsList.get(i);
			demandedCredit=demandedCredit-aBankAccount.getDemandedCredit();
			if(RandomHelper.nextDouble()>0.5){
				double demCred=aBankAccount.getDemandedCredit();
				double allCred=demCred*0.5;
				aBankAccount.setAllowedCredit(allCred);
				allowedCredit=allowedCredit-allCred;
			}
			else{
				double demCred=aBankAccount.getDemandedCredit();
				double allCred=demCred*1.5;
				aBankAccount.setAllowedCredit(allCred);
				allowedCredit=allowedCredit-allCred;
			}
		}
		if(Context.verboseFlag){
		System.out.println("     bank "+identity+" demanded credit "+demandedCredit+" allowed credit "+allowedCredit);
		}
	}
*/

}
