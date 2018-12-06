package com.app.talk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.app.talk.communication.CommunicatorFactory;

/**
 * The dispatcher waits for clients to connect to its serverSocket and creates a communicator for each connected client.
 */
public class Dispatcher implements Runnable {
	private ServerSocket server;
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
        System.out.println("Server started. Listening for incoming connection requests on port: " + this.port);   

        while (acceptClients) {     	
        	try{
        		Socket client = server.accept();
                System.out.println("Connection request from " + client.getInetAddress().toString() + ":" + client.getPort());
                TalkServer.addClient(CommunicatorFactory.getInstance().createCommunicator(client));
        	} catch (SocketException e){
        		//this is fine
        	}
        } //while
    } //listen

	public void close() {
		try {
			acceptClients = false;
			server.close();
		} catch (IOException e) {
			// Doesn't matter if already closed.			
		}		
	}//close()
} //Dispatcher Class
