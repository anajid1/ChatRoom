/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package local.silverton.client;

/**
 *
 * @author Abdullah
 */
public class Driver {
    
    
    
    public static void main(String[] args) {
        // Debug ClientGUI later
        //ClientGUI clientGUI = new ClientGUI();
        
        ChatGUI chatGUI = new ChatGUI();
        
        ServerHandler serverHandler = new ServerHandler("localhost", "20750", "anajid");
        serverHandler.run();
        
    }
    
    public static void continueToServer(String hostAddress, String portNumber, String username) {
        System.out.println("Host Address: " + hostAddress);
        System.out.println("Port Number: " + portNumber);
        System.out.println("username: " + username);
        ServerHandler serverHandler = new ServerHandler(hostAddress, portNumber, username);
        serverHandler.run();
    }
}
