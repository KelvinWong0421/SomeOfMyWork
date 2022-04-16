import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TotalBalanceUI extends ATM
{
    private Transaction balance; // account information database
    
    private JPanel basePanel;
    private JPanel totalBalancePanel;
    private JLabel displayMessage;

    private int accountNum;
    private BankDatabase data;
    private double money;
    
    private String text;
    
    private  String[] names = { "", "", "",
                                "", "", "",
                                "", "", "",
                                "", "", "Return"};
    

    private int row = 4, col = 3;
    
    public TotalBalanceUI(JPanel bPanel,int userInputedAccountNumber,BankDatabase bankDatabase) 
    {
        basePanel = bPanel;
        balance = new BalanceInquiry(userInputedAccountNumber,bankDatabase);
        accountNum= balance.getAccountNumber();
        data= bankDatabase;
        money=data.getTotalBalance(accountNum);
        
        totalBalancePanel = new JPanel();
        totalBalancePanel.setLayout(new GridLayout(row, col, 10, 10));
        
        displayMessage = new JLabel("",JLabel.CENTER );
        JButton[] buttons = new JButton[names.length];
        for(int i = 0; i < names.length; i++) {
            
            
            if(!(i==4)){                           //free space for label
            buttons[i] = new JButton(names[i]);   //create button 
            Handler handler = new Handler();   //create handler
            buttons[i].addActionListener(handler);   //add handler into buttons
            totalBalancePanel.add(buttons[i]);   //add buttons into  panel
            if( buttons[i].getText() == ""){   //do not display the buttons that are not using
                buttons[i].setVisible(false);
            } 
           }
            else{
            displayMessage.setText("Your Balance: "+money);
            totalBalancePanel.add(displayMessage);
            }
        }
        

        basePanel.add(totalBalancePanel);   //add view balance pane into base panel
        basePanel.revalidate();
    }
    
    private class Handler implements ActionListener   // create Handler class
    {
    public void actionPerformed( ActionEvent event ) {      
        String nextUI = event.getActionCommand();   // get what user choose     
        switch (nextUI) {                                   
            case "Return" :
            basePanel.remove(totalBalancePanel);   
            basePanel.repaint();
            BalanceInquiryUI viewBalance=new BalanceInquiryUI(basePanel,accountNum,data);
            break;
            }
        }
    }
}