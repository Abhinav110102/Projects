import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorMain implements ActionListener{

    JFrame frame; // Create the window on the screen 
    JTextField textfield; //Create 
    JButton[] numberButtons = new JButton[10];//hold all the numbered buttons
    JButton[] functionButtons = new JButton[9];// Hold all the function buttons
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton , equButton, delButton, clrButton, negButton;
    JPanel panel;
    
    Font myFont = new Font("Times New Roman", Font.BOLD,30);

    double num1 =0, num2=0, result=0;
    char operator;


    CalculatorMain(){
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,550);
        frame.setLayout(null);

        textfield = new JTextField();
        textfield.setBounds(50, 25, 300, 50);
        textfield.setFont(myFont);
        textfield.setEditable(false);

        // addButton
        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("Delete");
        clrButton = new JButton("Clear");
        negButton = new JButton("(-)");

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;
        functionButtons[8] = negButton;

        // For loop for the function buttons
        for(int i=0;i<9;i++ )
        {
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(false);
        }
        // For loop for the numbers 
        for(int i=0;i<10;i++ )
        {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(false);
        }
        // Negative buttons bounds
        negButton.setBounds(50,430,100,50);
        // delete button bounds
        delButton.setBounds(150,430,100,50);
        // clear button bounds
        clrButton.setBounds(250,430,100,50);

        panel = new JPanel();
        // set bound for panel
        panel.setBounds(50,100, 300,300);
        panel.setLayout(new GridLayout(4,4,10,10));
        //panel.setBackground(Color.GRAY);
        //FirstRow
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);
        //SecondRow
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
         //ThirdRow
         panel.add(numberButtons[7]);
         panel.add(numberButtons[8]);
         panel.add(numberButtons[9]);
         panel.add(mulButton);

         panel.add(decButton);
         panel.add(numberButtons[0]);
         panel.add(equButton);
         panel.add(divButton);


        frame.add(panel);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(negButton);
        frame.add(textfield);
        frame.setVisible(true);
    }
    public static void main(String[] args ) {
        CalculatorMain calc = new CalculatorMain();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //Number gets updated on our text field 
        for(int i=0 ; i<10; i++){
            if(e.getSource() == numberButtons[i]){
                textfield.setText(textfield.getText().concat(String.valueOf(i)));
            }
        }
        // Decimal button
        if(e.getSource() == decButton){
            textfield.setText(textfield.getText().concat("."));
        }
        // Add Button 
        if(e.getSource() == addButton){
            num1 = Double.parseDouble(textfield.getText());
            operator = '+';
            textfield.setText("");
        }
        // subtract Button 
        if(e.getSource() == subButton){
            num1 = Double.parseDouble(textfield.getText());
            operator = '-';
            textfield.setText("");
        }
        // Multiply Button 
        if(e.getSource() == mulButton){
            num1 = Double.parseDouble(textfield.getText());
            operator = '*';
            textfield.setText("");
        }
        // divide Button 
        if(e.getSource() == divButton){
            num1 = Double.parseDouble(textfield.getText());
            operator = '/';
            textfield.setText("");
        }
        if(e.getSource()==equButton) {
            num2 = Double.parseDouble(textfield.getText());
            

            switch(operator){
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = num1 / num2;
                break;
            }
            textfield.setText(String.valueOf(result));
            num1= result;
        }
        // Clear Button 
        if(e.getSource() == clrButton){
            textfield.setText("");
        }
        // delete Button 
        if(e.getSource() == delButton){
            String string = textfield.getText();
            textfield.setText("");
            for(int i=0; i<string.length()-1; i++){
                textfield.setText(textfield.getText()+string.charAt(i));
            }
        }
        // Negative number Button
        if(e.getSource() == negButton){
            double temp = Double.parseDouble(textfield.getText());
            temp*=-1;
            textfield.setText(String.valueOf(temp));
        }
    }

}