// ATM.java
// Represents an automated teller machine
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ATM extends JFrame
{
	private boolean userAuthenticated; // whether user is authenticated
	private BankDatabase bankDatabase; // account information database
	private CashDispenser cashDispenser = new CashDispenser(); // ATM's cash dispenser
	private int inputedtimes=0;
	private int userInputedAccountNumber;
	private int userInputedPIN;
	   
	private JPanel basePanel;
	private JPanel loginPanel;
	private static final String[] labelNames = {"", "<html>Welcome!<br><br><br>Avalible dollar notes: $100</html>", "",
											    "", "" 							 , "",
											    "", ""							 , "Please input the account number: ",
											    "", ""     									}; //the 11th array is for input
	private JLabel[] label = new JLabel[labelNames.length]; //create label
	private JTextField inputAccountNumber;
	private JPasswordField inputPassword;
	Keyhandler keyHandler = new Keyhandler();
	
	private JPanel menuPanel;
	private static final String[] names = {"View Balance" 	, "", "Withdraw",
										   "Transfer"	  	, "", "",
										   ""				, "", "",
										   ""		  		, "", "Exit"};
	private int row = 4, col = 3;
	private MenuHandler menuHandler = new MenuHandler();
	
	private static final String[] exitLabelNames = {"", "", "", 
													"", "<html>Please take the card!<br>Thank You!</html>", "",
													"", "", "", 
													"", "", ""};
	private JPanel exitMessagePanel;
	private JLabel[] exitLabel = new JLabel[exitLabelNames.length];
									
	// no-argument ATM constructor initializes instance variables
	public ATM() 
	{
		super("ATM");
		
		bankDatabase = new BankDatabase(); // create acct info database
		
		userAuthenticated = false; // user is not authenticated to start
		
		basePanel = new JPanel();
		basePanel.setLayout(new GridLayout(1, 1, 0, 0)); //create base panel
		
		add(basePanel);
	}
	
	public void login()  
	{
		userAuthenticated = false;
		userInputedAccountNumber = 0;
		userInputedPIN = 0;
		
		loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(4, 3, 10, 10)); //create login panel
		
		for(int i = 0; i < labelNames.length; i++) {
			label[i] = new JLabel(labelNames[i]);
			label[i].setVerticalAlignment(SwingConstants.BOTTOM);
			loginPanel.add(label[i]);
		}
		
		inputAccountNumber = new JTextField(); //create the input field
		inputAccountNumber.addKeyListener(keyHandler);
		loginPanel.add(inputAccountNumber);
		
		basePanel.add(loginPanel);
		basePanel.revalidate();
		
	} // end no-argument ATM constructor
	
	public void changePasswordField() { 	//change the field that input account number to the field that input password 
		loginPanel.remove(inputAccountNumber); 	//remove text field which is for user input account number
		basePanel.revalidate(); 		// set the basePanel layout
		
		label[4].setText("");	//reset the error message
		
		inputPassword = new JPasswordField(); 	//make a password field which is for user input password
		inputPassword.addKeyListener(keyHandler);
		loginPanel.add(inputPassword);
	}
	
	public void ATMMainMenu(JPanel bPanel,int inputAccountNumber,BankDatabase bankDatabase)   //menu UI
	{
		basePanel = bPanel;
		
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(row, col, 10, 10)); //setting the menu panel
		
		userInputedAccountNumber=inputAccountNumber; //data transparency
		this.bankDatabase=bankDatabase; //data transparency
	
		JButton[] buttons = new JButton[names.length];   //create button array
		for(int i = 0; i < names.length; i++) {      //using for loop to make button
			buttons[i] = new JButton(names[i]);   //create buttons
			buttons[i].addActionListener(menuHandler);   //add handler into buttons
			menuPanel.add(buttons[i]);   //add buttons into main menu panel
			if( buttons[i].getText() == ""){   //do not display the button which is "" button
				buttons[i].setVisible(false);
			} 
		}
		basePanel.add(menuPanel);   //add main menu into base panel
		basePanel.revalidate();
	}
	
	
	
	private class Keyhandler extends KeyAdapter
	{
		public void keyTyped( KeyEvent event) 
		{
			if(KeyEvent.getKeyText(event.getKeyChar()) == "Enter" )  //check the user have click the enter key
			{	
				label[1].setVisible(false);
				if(!userAuthenticated) 
				{//account number field 
					userInputedAccountNumber = Integer.parseInt(inputAccountNumber.getText()); //assume user only can only input the number on the keypad, cannot input character
					boolean isAccount = bankDatabase.checkAccount( userInputedAccountNumber );
					if (isAccount)
					{//the account is 
						userAuthenticated = true;
						label[8].setText("Please input the password:");
						changePasswordField(); //change to password field
					}
					else 
					{
						label[4].setText("<html>Wrong account number or non-exist account!<br/>Please input again.</html>");   //display error message on the ATM screen
						inputAccountNumber.setText("");
					}
				}
				else 
				{//check password field
					userInputedPIN = Integer.parseInt(String.valueOf(inputPassword.getPassword())); //get password from user
					boolean correctAccount = bankDatabase.authenticateUser( userInputedAccountNumber, userInputedPIN );
					if( correctAccount ) 
					{//correct account number and password
						basePanel.remove(loginPanel);
						basePanel.revalidate();
						basePanel.repaint();
						ATMMainMenu(basePanel,userInputedAccountNumber,bankDatabase);
					}
					else
					{//incorrect password, user need to input again
						label[4].setText("<html>Incorrect password!<br>Please input again.</html>");   //display error message on the ATM screen
						inputPassword.setText("");
						inputedtimes++;
						if(inputedtimes==5){
						  basePanel.remove(loginPanel);
						  basePanel.revalidate();
						  basePanel.repaint();
						  login();
						  }
					}
				}
			}
		}
	}
	
	private class MenuHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent event ) {	// maybe use handler is better
			
			String nextUI = event.getActionCommand();	
			if(nextUI == "Exit") {
				basePanel.remove(menuPanel);   //remove the menuPanel
				exitMessagePanel = new JPanel();   //create panel
				exitMessagePanel.setLayout(new GridLayout(row, col, 0, 0));   
				for(int i = 0; i < exitLabelNames.length; i++) {   //create label
					exitLabel[i] = new JLabel(exitLabelNames[i]);
					exitMessagePanel.add(exitLabel[i]);
				}
				basePanel.add(exitMessagePanel);   //add exit panel
				basePanel.revalidate();   //
				Timer timer = new Timer();   //create time object
				timer.schedule(new ExitTask(), 3000);   //after 3 seconds, excute the run() method in class ExitTask
			}
			else 
			{
				basePanel.remove(menuPanel);				
				basePanel.repaint();		
				switch (nextUI) {
				case "View Balance" :
					BalanceInquiryUI viewBalance = new BalanceInquiryUI(basePanel,userInputedAccountNumber,bankDatabase);
					break;
				case "Withdraw" :
					WithdrawalUI withdrawalPanel = new WithdrawalUI (basePanel, userInputedAccountNumber, bankDatabase, cashDispenser);
					break;
				case "Transfer" :
					TransferUI currentTransfer = new TransferUI(userInputedAccountNumber, bankDatabase, cashDispenser); 
					currentTransfer.run(basePanel);
					break;
				}
			}
		}
	}
	
	public class ExitTask extends TimerTask {
		public void run() {
			basePanel.remove(exitMessagePanel);
			basePanel.repaint();
			login();
		}		
	}
	
	public void returnToLogin(BankDatabase bank, JPanel bPanel, CashDispenser cash) 
	{
		bankDatabase = bank;
		basePanel = bPanel;
		cashDispenser = cash;
		login();
		basePanel.revalidate();
	}		
} // end class ATM



/**************************************************************************
 * (C) Copyright 1992-2007 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/