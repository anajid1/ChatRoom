/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package local.silverton.client;

import java.net.*;

/**
 *
 * @author Abdullah
 */
public class ServerHandler implements Runnable {
    
    private InetAddress host;
    
    private String hostAddress;
    private String portNumber;
    
    private String username;
    
    private String bytePad;
    
    public ServerHandler(String hostAddress, String portNumber, String username) {
        this.hostAddress = hostAddress;
        this.portNumber = portNumber;
        this.username = username;
    }
    
    public void run() {
        System.out.println("Hello ServerHandler Thread!");
    }
}
