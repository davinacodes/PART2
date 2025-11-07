package org.example;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Message {

    private static final String Welcome_Message = "Hi, welcome to the Chat!";
    private static final String Coming_Soon = "This feature is currently being developed.";

    private static int messageCounter = 0;
    private static int totalMessagesSent = 0;
    private static int maxMessages = 0;
    private static int messagesProcessed = 0;

    //Arrays for storing messages//
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageHash = new ArrayList<>();
    private static ArrayList<String> messageID = new ArrayList<>();

    private String messageIDValue;
    private String recipient;
    private String messageText;
    private int messageNumber;

    public String getMessageID() { return messageIDValue; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public int getMessageNumber() { return messageNumber; }

    private static final String JSON_FILE = "messages.json";

    public Message(String recipient, String messageText) {
        this.messageIDValue = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = ++messageCounter;
    }


    private static void loadStoredMessagesFromJSON() {
        try {
            File file = new File(JSON_FILE);
            if (!file.exists() || file.length() == 0) {
                return;
            }

            Scanner reader = new Scanner(file);
            StringBuilder jsonContent = new StringBuilder();
            while (reader.hasNextLine()) {
                jsonContent.append(reader.nextLine());
            }
            reader.close();

            String content = jsonContent.toString().trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();

                String[] objects = content.split("\\},\\s*\\{");
                for (String obj : objects) {
                    String cleanObj = obj.trim();
                    if (cleanObj.startsWith("{")) cleanObj = cleanObj.substring(1);
                    if (cleanObj.endsWith("}")) cleanObj = cleanObj.substring(0, cleanObj.length() - 1);


                    String recipient = extractValue(cleanObj, "recipient");
                    String messageText = extractValue(cleanObj, "messageText");
                    String messageID = extractValue(cleanObj, "messageID");
                    String messageHash = extractValue(cleanObj, "messageHash");
                    String messageNumberStr = extractValue(cleanObj, "messageNumber");

                    if (recipient != null && messageText != null) {
                        Message message = new Message(recipient, messageText);
                        message.messageIDValue = messageID != null ? messageID : message.generateMessageID();

                        try {
                            message.messageNumber = messageNumberStr != null ? Integer.parseInt(messageNumberStr) : ++messageCounter;
                        } catch (NumberFormatException e) {
                            message.messageNumber = ++messageCounter;
                        }

                        storedMessages.add(message);
                        Message.messageHash.add(messageHash != null ? messageHash : message.makeHash());
                        Message.messageID.add(message.messageIDValue);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading JSON file: " + e.getMessage());
        }
    }

    private static String extractValue(String jsonObject, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = jsonObject.indexOf(searchKey);
        if (keyIndex == -1) return null;

        int valueStart = keyIndex + searchKey.length();
        int valueEnd = jsonObject.indexOf(",", valueStart);
        if (valueEnd == -1) valueEnd = jsonObject.length();

        String value = jsonObject.substring(valueStart, valueEnd).trim();

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        return value;
    }

    //Main Program Logic //
    public static void startMessagingApp() {
        Scanner inputReader = new Scanner(System.in);

        JOptionPane.showMessageDialog(null, Welcome_Message);

        boolean running = true;

        while (running) {
            int selection = showMenu();

            switch (selection) {
                case 1:
                    setupMessageLimit();
                    runMessageSession(inputReader);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, Coming_Soon);
                    break;
                case 3:
                    showArrayOperationsMenu();
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Exiting. Thank you! Total Messages Sent: " + totalMessagesSent);
                    running = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.");
            }
        }
    }

    //Array Operations Menu//
    private static void showArrayOperationsMenu() {
        boolean inArrayMenu = true;

        while (inArrayMenu) {
            String menu = "===== Array Operations =====\n"
                    + "a. Display sender and recipient of all sent messages\n"
                    + "b. Display the longest sent message\n"
                    + "c. Search for message ID and display recipient/message\n"
                    + "d. Search for messages sent to a particular recipient\n"
                    + "e. Delete a message using message hash\n"
                    + "f. Display full report of all sent messages\n"
                    + "g. Return to main menu\n"
                    + "Enter choice (a-g):";

            String input = JOptionPane.showInputDialog(menu);

            if (input == null || input.equalsIgnoreCase("g")) {
                inArrayMenu = false;
                continue;
            }

            switch (input.toLowerCase()) {
                case "a":
                    displaySentMessagesSendersRecipients();
                    break;
                case "b":
                    displayLongestMessage();
                    break;
                case "c":
                    searchMessageByID();
                    break;
                case "d":
                    searchMessagesByRecipient();
                    break;
                case "e":
                    deleteMessageByHash();
                    break;
                case "f":
                    displayFullReport();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.");
            }
        }
    }

    //Display sender and recipient of all sent messages//
    private static void displaySentMessagesSendersRecipients() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found.");
            return;
        }

        StringBuilder result = new StringBuilder("=== Sent Messages (Recipient) ===\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            result.append("Message ").append(i + 1).append(": To: ").append(msg.getRecipient()).append("\n");
        }
        JOptionPane.showMessageDialog(null, result.toString());
    }

    //Display the longest sent message//
    private static void displayLongestMessage() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found.");
            return;
        }

        Message longest = sentMessages.get(0);
        for (int i = 1; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }

        JOptionPane.showMessageDialog(null,
                "=== Longest Sent Message ===\n" +
                        "Length: " + longest.getMessageText().length() + " characters\n" +
                        "To: " + longest.getRecipient() + "\n" +
                        "Message: " + longest.getMessageText());
    }

    public boolean checkMessageID() {
        if (messageIDValue == null || messageIDValue.length() != 10) {
            return false;
        }

        for (int i = 0; i < messageIDValue.length(); i++) {
            if (!Character.isDigit(messageIDValue.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //Search for message ID and display recipient or message//
    private static void searchMessageByID() {
        String searchID = JOptionPane.showInputDialog("Enter Message ID to search:");
        if (searchID == null || searchID.trim().isEmpty()) return;

        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            if (msg.getMessageID().equals(searchID)) {
                JOptionPane.showMessageDialog(null,
                        "=== Message Found ===\n" +
                                "Message ID: " + msg.getMessageID() + "\n" +
                                "To: " + msg.getRecipient() + "\n" +
                                "Message: " + msg.getMessageText());
                return;
            }
        }

        for (int i = 0; i < storedMessages.size(); i++) {
            Message msg = storedMessages.get(i);
            if (msg.getMessageID().equals(searchID)) {
                JOptionPane.showMessageDialog(null,
                        "=== Message Found (Stored) ===\n" +
                                "Message ID: " + msg.getMessageID() + "\n" +
                                "To: " + msg.getRecipient() + "\n" +
                                "Message: " + msg.getMessageText());
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Message ID not found.");
    }

    //Search for messages sent to a particular recipient//
    private static void searchMessagesByRecipient() {
        String recipient = JOptionPane.showInputDialog("Enter recipient phone number to search:");
        if (recipient == null || recipient.trim().isEmpty()) return;

        StringBuilder result = new StringBuilder("=== Messages to " + recipient + " ===\n");
        boolean found = false;

        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            if (msg.getRecipient().equals(recipient)) {
                result.append("Message ID: ").append(msg.getMessageID()).append("\n")
                        .append("Message: ").append(msg.getMessageText()).append("\n")
                        .append("---\n");
                found = true;
            }
        }
        if (!found) {
            result.append("No messages found for this recipient.");
        }
        JOptionPane.showMessageDialog(null, result.toString());
    }

    //Delete a message using message hash//
    private static void deleteMessageByHash() {
        String hash = JOptionPane.showInputDialog("Enter message hash to delete:");
        if (hash == null || hash.trim().isEmpty()) return;

        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            if (msg.makeHash().equals(hash)) {
                sentMessages.remove(i);
                messageHash.remove(hash);
                messageID.remove(msg.getMessageID());
                JOptionPane.showMessageDialog(null, "Message deleted from sent messages.");
                return;
            }
        }

        for (int i = 0; i < storedMessages.size(); i++) {
            Message msg = storedMessages.get(i);
            if (msg.makeHash().equals(hash)) {
                storedMessages.remove(i);
                messageHash.remove(hash);
                messageID.remove(msg.getMessageID());
                JOptionPane.showMessageDialog(null, "Message deleted from stored messages.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Message hash not found.");
    }

    //Display full report of all sent messages//
    private static void displayFullReport() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found.");
            return;
        }

        StringBuilder report = new StringBuilder("=== Full Sent Messages Report ===\n\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            report.append("Message #").append(msg.getMessageNumber()).append("\n")
                    .append("ID: ").append(msg.getMessageID()).append("\n")
                    .append("Hash: ").append(msg.makeHash()).append("\n")
                    .append("To: ").append(msg.getRecipient()).append("\n")
                    .append("Message: ").append(msg.getMessageText()).append("\n")
                    .append("Length: ").append(msg.getMessageText().length()).append(" characters\n")
                    .append("------------------------\n\n");
        }

        JOptionPane.showMessageDialog(null, report.toString());
    }

    //Set up the message limit//
    private static void setupMessageLimit() {
        try {
            String userInput = JOptionPane.showInputDialog("Enter maximum number of messages you want to send:");
            maxMessages = Integer.parseInt(userInput);
            messagesProcessed = 0; // Reset counter for new session
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Defaulting to 5 messages.");
            maxMessages = 5;
            messagesProcessed = 0;
        }
    }

    private static void runMessageSession(Scanner scanner) {
        while (messagesProcessed < maxMessages) {
            int choice = showSendOrMenuOption();

            if (choice == 0) {
                processMessage(scanner);
                messagesProcessed++;
            }
            else if (choice == 1) {
                JOptionPane.showMessageDialog(null, "Returning to main menu. " +
                        (maxMessages - messagesProcessed) + " messages remaining in this session.");
                break;
            }
            else {
                JOptionPane.showMessageDialog(null, "Message session cancelled.");
                break;
            }
        }

        if (messagesProcessed >= maxMessages) {
            JOptionPane.showMessageDialog(null,
                    "Message session completed!\n" +
                            "Messages sent in this session: " + messagesProcessed + "\n" +
                            "Total messages sent: " + totalMessagesSent + "\n" +
                            "Returning to main menu.");
        }
    }

    private static int showSendOrMenuOption() {
        String[] options = {"Send Message", "Main Menu"};
        String message = "What would you like to do?\n" +
                "Messages remaining in session: " + (maxMessages - messagesProcessed) +
                "\nTotal messages sent: " + totalMessagesSent;

        return JOptionPane.showOptionDialog(null,
                message,
                "Quick Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    private static int showMenu() {
        String menu = "===== Main Menu =====\n"
                + "1. Send Messages\n"
                + "2. View Settings\n"
                + "3. Array Operations\n"
                + "4. Exit\n"
                + "=====================\n"
                + "Total Messages Sent: " + totalMessagesSent + "\n"
                + "Enter choice (1-4):";

        String input = JOptionPane.showInputDialog(menu);

        if (input == null) {
            return 4;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static void processMessage(Scanner scanner) {
        String recipient = JOptionPane.showInputDialog("Enter recipient cellphone number:");
        if (recipient == null) return;

        if (!checkRecipientCell(recipient)) {
            JOptionPane.showMessageDialog(null, "Invalid phone number format.");
            return;
        }

        String message = JOptionPane.showInputDialog("Enter your message (max 250 chars):");
        if (message == null) return;

        if (message.length() > 250) {
            JOptionPane.showMessageDialog(null, "Message too long. Maximum 250 characters allowed.");
            return;
        }

        Message newMessage = new Message(recipient, message);
        String action = newMessage.askUser();

        messageHash.add(newMessage.makeHash());
        messageID.add(newMessage.getMessageID());

        if (action.equals("Message successfully sent.")) {
            totalMessagesSent++;
            sentMessages.add(newMessage);
            saveMessageToJSON(newMessage);
            JOptionPane.showMessageDialog(null, "Message Sent!\n" + newMessage.getDetails());
        } else if (action.equals("Message successfully stored.")) {
            storedMessages.add(newMessage);
            saveMessageToJSON(newMessage);
            JOptionPane.showMessageDialog(null, "Message Stored!\n" + newMessage.getDetails());
        } else {
            disregardedMessages.add(newMessage);
            JOptionPane.showMessageDialog(null, "Message Disregarded.");
        }
    }

    private static void saveMessageToJSON(Message message) {
        try {
            File file = new File(JSON_FILE);
            StringBuilder jsonContent = new StringBuilder();

            if (!file.exists() || file.length() == 0) {
                jsonContent.append("[\n");
            } else {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    jsonContent.append(reader.nextLine()).append("\n");
                }
                reader.close();

                int lastBracket = jsonContent.lastIndexOf("]");
                if (lastBracket != -1) {
                    jsonContent.deleteCharAt(lastBracket);
                    jsonContent.append(",\n");
                }
            }

            jsonContent.append("  {\n")
                    .append("    \"messageID\": \"").append(message.getMessageID()).append("\",\n")
                    .append("    \"recipient\": \"").append(message.getRecipient()).append("\",\n")
                    .append("    \"messageText\": \"").append(message.getMessageText().replace("\"", "\\\"")).append("\",\n")
                    .append("    \"messageHash\": \"").append(message.makeHash()).append("\",\n")
                    .append("    \"messageNumber\": ").append(message.getMessageNumber()).append("\n")
                    .append("  }\n")
                    .append("]");

            FileWriter writer = new FileWriter(JSON_FILE);
            writer.write(jsonContent.toString());
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to JSON file!");
        }
    }

    //Ask user what to do with the message//
    public String askUser() {
        String[] options = {"Send message", "Disregard message", "Store message"};
        int selection = JOptionPane.showOptionDialog(null, "What would you like to do?",
                "Choose action", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (selection == 0) return "Message successfully sent.";
        if (selection == 2) return "Message successfully stored.";
        return "Message disregarded.";
    }

    //Generate ID, Hash & Validations//
    private String generateMessageID() {
        Random r = new Random();
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            id.append(r.nextInt(10));
        }
        return id.toString();
    }

    public String makeHash() {
        String[] words = this.messageText.split("\\s+");
        String firstWord = words.length > 0 ? words[0].toUpperCase() : "EMPTY";
        String lastWord = words.length > 0 ? words[words.length - 1].toUpperCase() : "EMPTY";
        return this.messageIDValue.substring(0, 2) + ":" + this.messageNumber + ":" + firstWord + lastWord;
    }

    public static boolean checkRecipientCell(String num) {
        if (num == null) return false;
        String clean = num.replaceAll("[^0-9+]", "");
        return (clean.length() == 10 || (clean.startsWith("27") && clean.length() == 11) || (clean.startsWith("+") && clean.length() >= 11));
    }

    public String getDetails() {
        return "MessageID: " + this.messageIDValue +
                "\nMessage Hash: " + makeHash() +
                "\nTo: " + this.recipient +
                "\nMessage: " + this.messageText;
    }
}