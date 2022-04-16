import java.util.Scanner;

public class Transfer extends Transaction{
	private double transferAmount;
	private int transferAccount;
	
	//Transfer constructor
	Transfer(int userAccountNumber, BankDatabase atmBankDatabase) 
	{
		super(userAccountNumber, atmBankDatabase);
	} // end of constructor
	
	public void execute() {
		BankDatabase bankDatabase = getBankDatabase(); 
		double availableBalance = bankDatabase.getAvailableBalance( getAccountNumber() );
		
		/*//display the information of the current account
		screen.displayMessageLine("\nAccount number: " + getAccountNumber());    
		screen.displayMessage("Available Balance: ");
		screen.displayDollarAmount(availableBalance);
		screen.displayMessageLine("");
		
		//get transfer amount and account 
		transferAmount = getTransferAmount(availableBalance);
		if(transferAmount == 0) return; //return to main menu if user do not want to transfer money
		transferAccount = getTransferAccount();
		if(transferAccount == 0) return; //return to main menu if user do not want to transfer money
		
		//minus the amount in current account
		bankDatabase.debit(getAccountNumber(), transferAmount);  
		//adds the amount into another account
		bankDatabase.transferTo(transferAccount, transferAmount);
		
		//display the information of the current account after transfer money
		availableBalance = bankDatabase.getAvailableBalance( getAccountNumber() );
		screen.displayMessageLine("\nAfter transferred:\nAccount number: " + getAccountNumber());    
		screen.displayMessage("Available Balance: ");
		screen.displayDollarAmount(availableBalance);
		screen.displayMessageLine("");*/
	}
	
	/*public double getTransferAmount(double availableBalance) {
		Screen screen = getScreen();
		
		//let user input the amount
		screen.displayMessage("\nAmount(or enter 0 to cancel this transaction): ");
		double amount = keypad.getDoubleInput();
		
		//do the while loop if user's account don't have enough money or the input amount is less than or equal to 0
		while(amount > availableBalance || amount < 0 ) {
			if(amount < 0) 
				screen.displayMessage("\nYou cannot transfer the amount which is less than 0.\nPlease input a sufficient amount");
			else
				screen.displayMessage("\nYou do not have enough money to transfer.\nPlease choose a smaller amount");
			screen.displayMessage("(or enter 0 to cancel this transaction): ");
			amount = keypad.getInput();
		}	
		return amount;
	}
	
	public int getTransferAccount(){
		BankDatabase bankDatabase = getBankDatabase(); 
		Screen screen = getScreen();
		boolean isAccount;
		
		//let user input the payee's account number
		screen.displayMessage("Payee(or enter 0 to cancel this transaction): ");
		int transferAccount = keypad.getInput();
		isAccount = bankDatabase.checkAccount(transferAccount);
		
		//do the while loop if the account number is incorrect, untill the correct account number is inputed
		while(isAccount == false || transferAccount == getAccountNumber()){      //let user keep input untill he input a account which is exist in our bank
			if (transferAccount == getAccountNumber())
				screen.displayMessage("\nYou can not transfer to this account.\nPlase give another account number");
			else if (transferAccount == 0) 
				return transferAccount;
			else
				screen.displayMessage("\nThat account does not exist in our bank.\nPlease give the correct account number");
			screen.displayMessage("(or enter 0 to cancel this transaction): ");
			transferAccount = keypad.getInput();
			isAccount = bankDatabase.checkAccount(transferAccount);
		}
		return transferAccount;
	}*/
}