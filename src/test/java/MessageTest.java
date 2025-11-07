import org.example.Message;
import static org.junit.Assert.*;
import org.junit.Test;

public class MessageTest {

    @Test
    public void testShortMessageWorks() {
        String number = "+27718693002";
        String messageText = "Hi Mike, can yoou join us for dinner tonight";
        Message message = new Message(number, messageText);
        assertNotNull(message);
    }

    @Test
    public void testLongMessageIsTooLong() {
        String number = "+27718693002";
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 251; i++) {
            longMessage.append("a");
        }

        Message message = new Message(number, longMessage.toString());
        int messageLength = message.getMessageText().length();
        assertTrue(messageLength > 250);
    }

    @Test
    public void testGoodPhoneNumber() {
        assertTrue(Message.checkRecipientCell("+27718693002")); 
        assertTrue(Message.checkRecipientCell("27718693002"));   
        assertTrue(Message.checkRecipientCell("0857597588"));    
    }

    @Test
    public void testBadPhoneNumber() {
        assertFalse(Message.checkRecipientCell("12345"));
        assertFalse(Message.checkRecipientCell("081234567"));
        assertFalse(Message.checkRecipientCell(""));
    }

    @Test
    public void testMessageHashCreation() {
        String number = "+27718693002";
        String messageText = "Hi Mike, can you join us for dinner tonight";
        Message message = new Message(number, messageText);

        String hash = message.makeHash();
         assertNotNull(hash);
         assertFalse(hash.isEmpty());

         String[] parts = hash.split(":");
         assertEquals(3, parts.length);
         assertEquals("HITONIGHT", parts[2]);
    }

    @Test
    public void testMessageIDCreation() {
        String number = "08575975889";
        String messageText = "Hi Keegan, did you receive the payment?";
        Message message = new Message(number, messageText);

        assertTrue(message.checkMessageID());

        String messageID = message.getMessageID();
        assertEquals(10, messageID.length());

        for (int i = 0; i < messageID.length(); i++) {
            char c = messageID.charAt(i);
            assertTrue(Character.isDigit(c));
        }

    }

    @Test
    public void testGetDetailsShowsEverything() {
        String number = "+27718693002";
        String messageText = "Hi Mike, can you join us for dinner tonight";
        Message message = new Message(number, messageText);

        String details = message.getDetails();

        assertTrue(details.contains("MessageID"));
        assertTrue(details.contains("Message Hash"));
        assertTrue(details.contains("To"));
        assertTrue(details.contains("Message"));
        assertTrue(details.contains(number));
        assertTrue(details.contains(messageText));
    }

    @Test
    public void testDifferentMessagesHaveDifferentIDs() {
        Message message1 = new Message("+27718693002", "First message");
        Message message2 = new Message("08575975889","Second message");

        assertNotEquals(message1.getMessageID(), message2.getMessageID());
    }

    @Test
    public void testEmptyMessage() {
        String number = "+27718693002";
        String emptyMessage = "";
        Message message = new Message(number, emptyMessage);

        assertNotNull(message);
        assertEquals("", message.getMessageText());
    }

    @Test
    public void testPhoneNumberWithFormatting() {
        assertTrue(Message.checkRecipientCell("+27 71 869 3002"));
        assertTrue(Message.checkRecipientCell("085-759-7588"));
        assertTrue(Message.checkRecipientCell("(27)718693002"));
    }

}
