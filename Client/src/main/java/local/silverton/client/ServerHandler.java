/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package local.silverton.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Random;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import local.silverton.common.Cryptography;

/**
 *
 * @author Abdullah
 */
public class ServerHandler implements Runnable {
    
    /* Used by MessageSender thread to let ServerHandler thread know if it needs to print a new prompt. */
    public static boolean wantsToSend = true;
    
    public static Scanner keyboard = new Scanner(System.in);
    
    private static InetAddress host;
    
    private static String hostAddress;
    private static int portNumber;
    
    private static String username;
    
    private static String bytePad;
    
    public ServerHandler(String hostAddress, String portNumber, String username) {
        ServerHandler.hostAddress = hostAddress;
        ServerHandler.portNumber = Integer.parseInt(portNumber);
        ServerHandler.username = username;
    }
    
    @Override
    public void run() {
        try {
            handler();
        } catch (NumberFormatException | IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void handler() throws NumberFormatException, IOException {
        System.out.println("Hello ServerHandler Thread!");
        try {
            /* Get server IP-address */
            host = InetAddress.getByName(hostAddress);
        } catch (UnknownHostException e) {
            System.out.println("Host ID not found!");
            System.exit(1);
        }
        
        Socket link = new Socket(host, portNumber);
        
        // Set up input and output streams for the connection
        BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
        PrintWriter out = new PrintWriter(link.getOutputStream(), true);
        
        /********************** HANDSHAKE **********************/
        /* Get g and n from server. */
        BigInteger g = new BigInteger(in.readLine());
        BigInteger n = new BigInteger(in.readLine());
        
        /* Print g, n per rubric. */
        System.out.println("g: " + g);
        System.out.println("n: " + n);
        
        /* 100 <= x <= 200 */
        Random rand = new Random();
        BigInteger x = new BigInteger("" + (rand.nextInt(101) + 100));
        
        /* Send server g^xmod(n) */
        BigInteger clientPartialKey = g.modPow(x, n);
        out.println(clientPartialKey.toString());
        
        /* Get g^ymod(n) from server. */
        BigInteger serverPartialKey = new BigInteger(in.readLine());
        
        BigInteger keyBig = serverPartialKey.modPow(x, n);					// Calculate the key.
        
        // Convert key to a string and print it.
        String keyStr = keyBig.toString();
        System.out.println("Key: " + keyStr);
        
        // Get byte-pad of key and print it.
        bytePad = Cryptography.getBytePad(keyStr);
        System.out.println("Byte-Pad: " + bytePad);
        /********************** END HANDSHAKE **********************/
        /* All messages will now be encrypted/decrypted. */
        
        /* Send server encrypted user-name. */
        out.println(Cryptography.encrypt(bytePad, username));

        /* Create and start thread for prompting user for messages to send to server. */
        MessageSender messageSender = new MessageSender(link, out);
        messageSender.start();

        /* Calculate spaces and backspaces to clear user prompt on current console line for a message. */
        String spaces = "  ";
        String backSpaces = "\b\b";
        for(int i = 0; i < username.length(); i++) {
    		spaces += " ";
    		backSpaces += "\b";
        }

        /* Keep printing messages from server till server sends DONE. */
        String response = decryptMessage(in.readLine());
        while(!response.equals("DONE")) {
            /* Clear current user prompt and put the message. */
            System.out.print(backSpaces + spaces + backSpaces + response + "\n");
            if(wantsToSend)
                /* User still wants to send messages so print out a new prompt. */
                System.out.print(username + "> ");
				response = decryptMessage(in.readLine());
        }
        
        // Close connection.
        try {
            System.out.println("\n!!!!! Closing connection... !!!!!");
            link.close();
        } catch (IOException e) {
            System.out.println("Unable to disconnect!");
            System.exit(1);
        }
    }
    
        /* Threaded class used to send messages to server. */
    public static class MessageSender extends Thread {
        Socket link;
        PrintWriter out;

        /* Constructor */
        public MessageSender(Socket link, PrintWriter out) {
            this.link = link;
            this.out = out;
        }

        /* Keep prompting and sending user messages till they enter DONE. */
        public void run() {
        	String message = "";
            do {
                System.out.print(username + "> ");
                message = keyboard.nextLine();
                out.println(enryptMessage(message));
            } while (!message.equals("DONE"));

            /* Let serverHandler thread know it doesn't need to reprint user prompt anymore. */
            wantsToSend = false;
        }
    }
    
    /* Method is just used to simplify and make code cleaner to read. */
    public static String decryptMessage(String encryptedMessage) {
    	return Cryptography.decrypt(bytePad, encryptedMessage);
    }
    
    /* Method is just used to simplify and make code cleaner to read. */
    public static String enryptMessage(String message) {
    	return Cryptography.encrypt(bytePad, message);
    }
}
