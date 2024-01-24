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
        ClientGUI.startGUI();
        
    }
    
    public static void continueToServer(String hostAddress, String portNumber, String username) {
        ServerHandler serverHandler = new ServerHandler(hostAddress, portNumber, username);
        serverHandler.run();
    }
}
