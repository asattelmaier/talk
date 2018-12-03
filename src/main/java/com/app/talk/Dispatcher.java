package com.app.talk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.app.talk.communication.Communicator;
import com.app.talk.communication.CommunicatorFactory;

/**
 * The dispatcher waits for clients to connect to its serverSocket and creates a communicator for each connected client.
 */
public class Dispatcher implements Runnable {
	private static ServerSocket server;
    private int port;
    private static boolean acceptClients = true;

    /**
     * Dispatcher constructor.
     *
     * @param port The port to listen to.
     */
    Dispatcher(int port) {
        this.port = port;
    } //constructor

    /**
     * Runnable implementation.
     */
    public void run() {
        try {
            this.listen();
        } catch (IOException | ClassNotFoundException e) {
        	e.printStackTrace();
        } //try-catch
    } //run

    /**
     * Creates communicators for connected clients.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void listen() throws IOException, ClassNotFoundException {
        server = new ServerSocket(this.port);
        boolean firstClient = true;
        System.out.println("Server started. Listening for incoming connection requests on port: " + this.port);
        
        // Steffi: New check client thread that will be started later after first client connects
        Thread checkClientThread = new Thread(new Runnable() {
			public void run() {
				checkClients();
			}
		});       

        while (acceptClients) {
        	
        	// Steffi: catching exception if check client thread stops dispatcher
        	try{
        		Socket client = server.accept();
                
                System.out.println("Connection request from " + client.getInetAddress().toString() + ":" + client.getPort());
                TalkServer.addClient(CommunicatorFactory.getInstance().createCommunicator(client));
                
                System.out.println("Clients: "+TalkServer.getClients().size());
        	} catch (SocketException e){
        		//this is fine
        	}
            
        	// Steffi: Start check client thread after first client - otherwise server stops immediately
            if(firstClient){
            	checkClientThread.start();
            	firstClient = false;
            }
        } //while
    } //listen
    
    /**
     * Checks all clients in servers client list if they're still connected.
     * Keepalive every 2 seconds.
     * 
     * - If not connected, interrupt the sender thread which would wait for next commandQueueElement and remove from client list.
     * - If client list is empty stop server.
     * 
     */
    private void checkClients(){    
    	
    	while(acceptClients){
        	ArrayList<Communicator> tmpList = new ArrayList<>();
        	
    		for(Communicator client : TalkServer.getClients()){
    			if(client.getSocket().isClosed()){
    				System.out.println("Closing client socket: "+client.getSocket().getPort());
    				client.getSenderThread().interrupt();
    			}else{
    				tmpList.add(client);
    			}
        	}
    		
    		// Steffi: This is not nice, but necessary due to concurrentModificationException
    		TalkServer.setClientList(tmpList);
    		
    		if(TalkServer.getClients().isEmpty()){
    			acceptClients = false;
    			try {
					Dispatcher.server.close();
				} catch (IOException e) {
					// This is fine
				}
    		}

    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }//checkClients()
} //Dispatcher Class
