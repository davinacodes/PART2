package org.example;

import javax.swing.*;
import java.util.Scanner;
import java.util.Random;
import javax.swing.JOptionPane;

public class Message {

    private static final String Welcome_Message = "Hi welcome to the Chat";
    private static final String Coming_Soon = "This feature is currently being development. It will be coming soon";
    private static int messageCounter = 0;
    private static int totalMessagesSent = 0;
    private String messageID;
    private String recipient;
    private String messageText;
    private int messageNumber;

    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageCounter + 1;
        messageCounter++;

    }

    public static void startMessagingApp() {
        Scanner inputReader = new Scanner(System.in);

        System.out.println("***********************************");
        System.out.println(Welcome_Message);
        System.out.println("***********************************");
        System.out.println("Please set your maximum message count:");
        int maxMessages;
        try {
            maxMessages = inputReader.nextInt();
            inputReader.nextLine();
        }
        catch(java.util.InputMismatchException e) {
            System.out.println("Invalid entry, defaulting to 5 messages.");
            inputReader.nextLine();
            maxMessages = 5;
        }

        int messagesSent = 0;
        int userSelection = 0;

        while (userSelection != 3 && messagesSent < maxMessages) {
            showMenu();
            System.out.println("Select an option (1-3): ");

            if(inputReader.hasNextInt()) {
                userSelection = inputReader.nextInt();
                inputReader.nextLine();

                switch (userSelection) {
                    case 1:
                        processMessage (inputReader,maxMessages,messagesSent);
                        break;

                    case 2:
                        System.out.println("\n---");
                        System.out.println(Coming_Soon);
                        System.out.println("---\n");
                        break;

                    case 3:
                        System.out.println("\nThanks for using FastMessages! Farewell!");
                        break;

                    default:
                        System.out.println("Invalid selection. Please choose 1,2, or 3");
                        break;
                }
            }
            else {
                System.out.println("Invalid input. Please enter a numeric value.");
                inputReader.nextLine();
            }

        }

        System.out.println("Total messages sent: " + totalMessagesSent);
        JOptionPane.showMessageDialog(null, "Total messages sent: " + totalMessagesSent);
    }

    private static void showMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Send a Message");
        System.out.println("2. View Settings");
        System.out.println("3. Exit");
        System.out.println("=====================");
    }

    private static void processMessage(Scanner scanner, int maxMsg, int currentMsg) {
        if (currentMsg < maxMsg) {

            System.out.println("Enter your cellphone number: ");
            String recipient = scanner.nextLine();

            if (!checkRecipientCell(recipient)) {
                System.out.println("Error: Invalid phone number format");
                return ;
            }

            System.out.println("Enter your message: ");
            String message = scanner.nextLine();

            if (message.length() > 250) {
                System.out.println("Error: Message is too long. Only maximum of 250 characters.");
                return ;
            }

            Message newMessage = new Message(recipient, message);
            String result = newMessage.askUser();

            System.out.println("Message sent: " + message);
            currentMsg++;

            if (result.equals("Message successfully sent.") || result.equals("Message successfully stored.")) {
                String details = newMessage.getDetails();
                JOptionPane.showMessageDialog(null, details);
            }

            currentMsg = currentMsg + 1;
    }
        else {
            System.out.println("Message limit reached! Cannot send more messages.");
        }
}

private String generateMessageID() {
        Random r = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + r.nextInt(10);
        }
        return id;

    }

    public static boolean checkRecipientCell(String num) {
        if (num == null) return false;

        String clean = num.replaceAll ("[^0-9+]", "");

        if (clean.startsWith("27") && clean.length() == 11) return true;
        if (clean.length() == 10) return true;
        if (clean.startsWith("+") && clean.length() >= 11) return true;

        return false;
    }
    public  String makeHash() {
        String firstTwo = this.messageID.substring(0,2);

        String[] words = this.messageText.split("\\s+");   // CORRECT
        String first = "EMPTY";
        String last = "EMPTY";

        if (words.length > 0) {
            first = words[0].toUpperCase();
            last = words[words.length - 1].toUpperCase();
        }

        return firstTwo + ":" + this.messageNumber + ":" + first + last;
    }

    public String askUser() {
        String[] choices = {"Send message", "Disregard message", "Store message"};

        int pick = JOptionPane.showOptionDialog(
                null,
                "What to do with this message?",
                "Choose",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                choices[0]
        );

        if (pick == 0) {
            totalMessagesSent = totalMessagesSent + 1;
            return "Message successfully sent.";
        } else if (pick == 1) {
            return "Message disregarded.";
        } else if (pick == 2) {
            return "Message successfully stored.";
        } else {
            return "Cancelled";
        }
    }

    public String getDetails() {
        String hash = makeHash();
        return "MessageID: " + this.messageID +
                "\nMessage Hash: " + hash +
                "\nTo: " + this.recipient +
                "\nMessage: " + this.messageText;
    }

    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    public boolean checkMessageID() {
        return this.messageID.length() == 10;
    }

    public static void main (String[] args) {
        startMessagingApp();
    }

    public String getMessageID() {
        return this.messageID;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getMessageText() {
        return this.messageText;
    }

    public int getMessageNumber() {
        return this.messageNumber;
    }
}

