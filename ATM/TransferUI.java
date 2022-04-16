import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TransferUI extends ATM
{
	private int userAccountNumber;
	   private CashDispenser cashDispenser;
	
	private double transferAmount;
	private int transferAccount;
	private JPanel transferPanel;
	private JPanel basePanel;
	private static final String[] labelNames = { "" , "", "",
												 "" , "", "",
												 "" , "", "Please input the transfer amount",
												 "" , "" 		};
	private JLabel[] label = new JLabel[labelNames.length];
	private JTextField userInput;
	private int row = 4, col =3;
	TransferKeyHandler transferKeyHandler = new TransferKeyHandler();
	private boolean enteredAmount = false;
	private boolean correctAmountAndNumber = false;
	
	private Transaction currentTransfer;
	private BankDatabase bankDatabase;
	private double availableBalance;
	private String text;
	private String whatKey;
	private boolean isAccount;
	
	private static final String[] exitLabelNames = {"", "", "", 
													"", "Cancelling transaction...", "",
													"", "", "", 
													"", "", ""};
	private JPanel exitMessagePanel;
	private JLabel[] exitLabel = new JLabel[exitLabelNames.length];
	
	public TransferUI(int userAccountNumber, BankDatabase atmBankDatabase, CashDispenser cash)
	{
		currentTransfer = new Transfer(userAccountNumber, atmBankDatabase);
		this.userAccountNumber = userAccountNumber;
		bankDatabase = atmBankDatabase;
		cashDispenser = cash;
		//double availableBalance = atmBankDatabase.getAvailableBalance( currentTransfer.getAccountNumber() );
	}
	
	public void run(JPanel bPanel) 
	{
		basePanel = bPanel;
		
		transferPanel = new JPanel();
		transferPanel.setLayout(new GridLayout(row, col, 10, 10));
		
		for(int i = 0; i < labelNames.length; i++) {
			label[i] = new JLabel(labelNames[i]);
			label[i].setVerticalAlignment(SwingConstants.BOTTOM);
			transferPanel.add(label[i]);
		}
		
		userInput = new JTextField();
		userInput.addKeyListener(transferKeyHandler);
		transferPanel.add(userInput);
		
		basePanel.add(transferPanel);
		basePanel.validate();
	}
	
	private class TransferKeyHandler extends KeyAdapter
	{
		public void keyTyped( KeyEvent event) 
		{
			availableBalance = bankDatabase.getAvailableBalance( currentTransfer.getAccountNumber() );
			//text = Double.toString( availableBalance );
			
			//label[0].setText(text);
			
			whatKey = KeyEvent.getKeyText(event.getKeyChar());
			if( whatKey == "Escape" )
			{   //user chose to exit ATM
				basePanel.remove(transferPanel);
				basePanel.repaint();
				exitMessagePanel = new JPanel();   //create panel
				exitMessagePanel.setLayout(new GridLayout(row, col, 0, 0));   
				for(int i = 0; i < exitLabelNames.length; i++) {   //create label
					exitLabel[i] = new JLabel(exitLabelNames[i]);
					exitMessagePanel.add(exitLabel[i]);
				}
				basePanel.add(exitMessagePanel);   //add exit panel
				basePanel.revalidate();   //
				Timer timer = new Timer();   //create time object
				timer.schedule(new ChangeTextTask(), 1000);   //after 1 seconds, "cancelling transaction" will change to thank you message and then return to login page.
				timer.schedule(new ExitTask(), 4000);   //
			}
			else if( whatKey == "Enter" )
			{   //user continue to transfer
				label[4].setText("");   //reset the error message if the user insufficient amount last time
				if(!enteredAmount) {   	//when the user have not input the transfer amount, let user input transfer amount first
					transferAmount = Double.parseDouble(userInput.getText());
					if(transferAmount > availableBalance) 
					{   //display error message on the same screen when user do not have enough money to transfer
															  //********they cannot input negative amount, as the keypad do not have '-' key********
						label[4].setText("<html>Your account do not have enough money.<br>Please input again.</html>");
					}
					else
					{   //continue to do the transfer function
						enteredAmount = true;
						label[8].setText("Payee's account:");
						//JOptionPane.showMessageDialog(null,transferAmount);
					}
				}
				else
				{
					transferAccount = Integer.parseInt(userInput.getText());
					isAccount = bankDatabase.checkAccount(transferAccount);
					if(isAccount == false)
						label[4].setText("<html>The account is not exist.<br>Please input again.</html>");
					else if(transferAccount == currentTransfer.getAccountNumber()) 
						label[4].setText("<html>You cannot transfer to the this account.<br>Please input again.</html>");
					else 
					{
						//confim information
						correctAmountAndNumber = true;   //
					}
				}
				userInput.setText("");  //reset user's input if user typed the wrong input
				
				
				if(correctAmountAndNumber) 
				{
					bankDatabase.debit(currentTransfer.getAccountNumber(), transferAmount);
					bankDatabase.transferTo(transferAccount, transferAmount);
					
					basePanel.remove(transferPanel);   
					basePanel.repaint();
					ATMMainMenu(basePanel, userAccountNumber, bankDatabase);
				
					availableBalance = bankDatabase.getAvailableBalance( currentTransfer.getAccountNumber() );
				}
			}
		}
	}
	
	public class ExitTask extends TimerTask {
		public void run() {
			basePanel.remove(exitMessagePanel);
			basePanel.repaint();
			returnToLogin(bankDatabase, basePanel, cashDispenser);
		}		
	}
	
	public class ChangeTextTask extends TimerTask {
		public void run() {
			exitLabel[4].setText("<html>Please take the card!<br>Thank You!</html>");
		}
	}
}