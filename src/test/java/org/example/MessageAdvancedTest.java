package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MessageAdvancedTest {

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @Before
    public void setUp() {
        resetStaticArrays();

        message1 = new Message("+27834557896", "Did you get the cake?");
        message2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        message3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        message4 = new Message("0838884567", "It is dinner time !");
        message5 = new Message("+27838884567", "Ok, I am leaving without you.");

        setMessageNumber(message1, 1);
        setMessageNumber(message2, 2);
        setMessageNumber(message3, 3);
        setMessageNumber(message4, 4);
        setMessageNumber(message5, 5);
    }

    @After
    public void tearDown() {
        File jsonFile = new File("messages.json");
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
    }

    private void resetStaticArrays() {
        try {
            java.lang.reflect.Field sentMessagesField = Message.class.getDeclaredField("sentMessages");
            sentMessagesField.setAccessible(true);
            sentMessagesField.set(null, new ArrayList<Message>());

            java.lang.reflect.Field disregardedMessagesField = Message.class.getDeclaredField("disregardedMessages");
            disregardedMessagesField.setAccessible(true);
            disregardedMessagesField.set(null, new ArrayList<Message>());

            java.lang.reflect.Field storedMessagesField = Message.class.getDeclaredField("storedMessages");
            storedMessagesField.setAccessible(true);
            storedMessagesField.set(null, new ArrayList<Message>());

            java.lang.reflect.Field messageHashField = Message.class.getDeclaredField("messageHash");
            messageHashField.setAccessible(true);
            messageHashField.set(null, new ArrayList<String>());

            java.lang.reflect.Field messageIDField = Message.class.getDeclaredField("messageID");
            messageIDField.setAccessible(true);
            messageIDField.set(null, new ArrayList<String>());

            java.lang.reflect.Field messageCounterField = Message.class.getDeclaredField("messageCounter");
            messageCounterField.setAccessible(true);
            messageCounterField.set(null, 0);

            java.lang.reflect.Field totalMessagesSentField = Message.class.getDeclaredField("totalMessagesSent");
            totalMessagesSentField.setAccessible(true);
            totalMessagesSentField.set(null, 0);
        } catch (Exception e) {
            fail("Failed to reset static arrays: " + e.getMessage());
        }
    }

    private void setMessageNumber(Message message, int number) {
        try {
            java.lang.reflect.Field messageNumberField = Message.class.getDeclaredField("messageNumber");
            messageNumberField.setAccessible(true);
            messageNumberField.set(message, number);
        } catch (Exception e) {
            fail("Failed to set message number: " + e.getMessage());
        }
    }

    private void addMessageToSent(Message message) {
        try {
            java.lang.reflect.Field sentMessagesField = Message.class.getDeclaredField("sentMessages");
            sentMessagesField.setAccessible(true);
            ArrayList<Message> sentMessages = (ArrayList<Message>) sentMessagesField.get(null);
            sentMessages.add(message);

            java.lang.reflect.Field messageHashField = Message.class.getDeclaredField("messageHash");
            messageHashField.setAccessible(true);
            ArrayList<String> messageHash = (ArrayList<String>) messageHashField.get(null);
            messageHash.add(message.makeHash());

            java.lang.reflect.Field messageIDField = Message.class.getDeclaredField("messageID");
            messageIDField.setAccessible(true);
            ArrayList<String> messageID = (ArrayList<String>) messageIDField.get(null);
            messageID.add(message.getMessageID());
        } catch (Exception e) {
            fail("Failed to add message to sent: " + e.getMessage());
        }
    }

    private void addMessageToStored(Message message) {
        try {
            java.lang.reflect.Field storedMessagesField = Message.class.getDeclaredField("storedMessages");
            storedMessagesField.setAccessible(true);
            ArrayList<Message> storedMessages = (ArrayList<Message>) storedMessagesField.get(null);
            storedMessages.add(message);

            java.lang.reflect.Field messageHashField = Message.class.getDeclaredField("messageHash");
            messageHashField.setAccessible(true);
            ArrayList<String> messageHash = (ArrayList<String>) messageHashField.get(null);
            messageHash.add(message.makeHash());

            java.lang.reflect.Field messageIDField = Message.class.getDeclaredField("messageID");
            messageIDField.setAccessible(true);
            ArrayList<String> messageID = (ArrayList<String>) messageIDField.get(null);
            messageID.add(message.getMessageID());

        } catch (Exception e) {
            fail("Failed to add message to stored: " + e.getMessage());
        }
    }

    //Test sent Messages array correctly populated//
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        addMessageToSent(message1);
        addMessageToSent(message4);

        ArrayList<Message> sentMessages = getSentMessages();

        assertEquals("Sent messages array should contain 2 messages", 2, sentMessages.size());

        boolean foundMessage1 = false;
        boolean foundMessage4 = false;

        for (Message msg : sentMessages) {
            if (msg.getMessageText().equals("Did you get the cake?")) {
                foundMessage1 = true;
            }
            if (msg.getMessageText().equals("It is dinner time !")) {
                foundMessage4 = true;
            }
        }

        assertTrue("Should contain 'Did you get the cake?'", foundMessage1);
        assertTrue("Should contain 'It is dinner time !'", foundMessage4);
    }

    //Test displays the longest Message//
    @Test
    public void testDisplayLongestMessage() {
        addMessageToSent(message1);
        addMessageToSent(message2);
        addMessageToSent(message3);
        addMessageToSent(message4);

        Message longest = message1;
        for (Message msg : getSentMessages()) {
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }

        assertEquals("Longest message should be message2",
                "Where are you? You are late! I have asked you to be on time.",
                longest.getMessageText());
        assertEquals("Longest message length should be 60", 60, longest.getMessageText().length());
    }

    //Test searchers for message by recipient//
    @Test
    public void testSearchMessageByRecipient() {
        addMessageToSent(message4);

        ArrayList<Message> messagesForRecipient = new ArrayList<>();
        for (Message msg : getSentMessages()) {
            if (msg.getRecipient().equals("0838884567")) {
                messagesForRecipient.add(msg);
            }
        }

        assertEquals("Should find 1 message for recipient 0838884567", 1, messagesForRecipient.size());
        assertEquals("Found message should be 'It is dinner time !'",
                "It is dinner time !",
                messagesForRecipient.get(0).getMessageText());
    }

    //Test search all messages sent or stored for a particular recipient//
    @Test
    public void testSearchAllMessagesForRecipient() {
        addMessageToStored(message2);
        addMessageToStored(message5);

        ArrayList<String> foundMessages = new ArrayList<>();

        for (Message msg : getStoredMessages()) {
            if (msg.getRecipient().equals("+27838884567")) {
                foundMessages.add(msg.getMessageText());
            }
        }

        assertEquals("Should find 2 messages for recipient +27838884567", 2, foundMessages.size());
        assertTrue("Should contain 'Where are you? You are late! I have asked you to be on time.'",
                foundMessages.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue("Should contain 'Ok, I am leaving without you.'",
                foundMessages.contains("Ok, I am leaving without you."));
    }

    //Test delete a message using message hash//
    @Test
    public void testDeleteMessageByHash() {
        addMessageToStored(message2);

        String messageHash = message2.makeHash();
        int initialSize = getStoredMessages().size();

        boolean deleted = false;
        ArrayList<Message> storedMessages = getStoredMessages();
        for (int i = 0; i < storedMessages.size(); i++) {
            Message msg = storedMessages.get(i);
            if (msg.makeHash().equals(messageHash)) {
                storedMessages.remove(i);
                deleted = true;
                break;
            }
        }
        assertTrue("Message should be successfully deleted", deleted);
        assertEquals("Stored messages array should have one less message",
                initialSize - 1, getStoredMessages().size());
    }

    //Test displays Report - verify report contains required fields//
    @Test
    public void testDisplayReportContainsRequiredFields() {
        addMessageToSent(message1);

        String details = message1.getDetails();

        assertTrue("Report should contain Message Hash", details.contains("Message Hash"));
        assertTrue("Report should contain Recipient", details.contains("To:"));
        assertTrue("Report should contain Message", details.contains("Message:"));
        assertTrue("Report should contain the actual message text",
                details.contains("Did you get the cake?"));
    }

    //Test tests message hash generation//
    @Test
    public void testMessageHashGeneration() {
        String hash = message1.makeHash();

        assertNotNull("Hash should not be null", hash);
        assertTrue("Hash should contain colon separators", hash.contains(":"));

        String[] parts = hash.split(":");
        assertEquals("Hash should have 3 parts", 3, parts.length);
        assertEquals("Third part should be first and last word combined", "DIDCAKE?", parts[2]);
    }

    //Test tests recipient validation//
    @Test
    public void testRecipientValidation() {
        assertTrue("Valid SA number with +27 should be valid",
                Message.checkRecipientCell("+27834557896"));
        assertTrue("Valid SA number without + should be valid",
                Message.checkRecipientCell("27834557896"));
        assertTrue("Valid 10-digit number should be valid",
                Message.checkRecipientCell("0838884567"));
        assertFalse("Invalid short number should be invalid",
                Message.checkRecipientCell("12345"));
        assertFalse("Null should be invalid",
                Message.checkRecipientCell(null));
    }

    private ArrayList<Message> getSentMessages() {
        try {
            java.lang.reflect.Field sentMessagesField = Message.class.getDeclaredField("sentMessages");
            sentMessagesField.setAccessible(true);
            return (ArrayList<Message>) sentMessagesField.get(null);
        } catch (Exception e) {
            fail("Failed to get sent messages: " + e.getMessage());
            return null;
        }
    }

    private ArrayList<Message> getStoredMessages() {
        try {
            java.lang.reflect.Field storedMessagesField = Message.class.getDeclaredField("storedMessages");
            storedMessagesField.setAccessible(true);
            return (ArrayList<Message>) storedMessagesField.get(null);
        } catch (Exception e) {
            fail("Failed to get stored messages: " + e.getMessage());
            return null;
        }
    }
}
