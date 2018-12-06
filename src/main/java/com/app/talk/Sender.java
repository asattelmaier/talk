package com.app.talk;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.app.talk.client.command.set.MessageCommand;
import com.app.talk.command.RemoteCommand;

/**
 * A simple sender of network traffic.
 */
public class Sender implements Runnable {
    /**
     * A DataOutputStream containing the OutputStream of the client Socket.
     */
    private ObjectOutputStream outputStream = null;
    /**
     * once initialized, establishes connection.
     */
    private Socket socket;
    /**
     * queues the given commands.
     */
    private LinkedBlockingQueue<Object> commandQueue = new LinkedBlockingQueue<Object>();
	/**
	 * Timeout before heartbeat/keepalive.
	 */
    private long timeout;
	/**
	 * The Object to be sent as heartbeat/keepalive.
	 */
	private RemoteCommand heartbeat;
    
    
	/**
     * A sender of information over the network.
     *
     * @param socket
     */
    public Sender(Socket socket) throws IOException {
        this.socket = socket;
    } //constructor

    /**
     * An endless loop checking comandQueue end sending RemoteCommands.
     */
    public void run() {
        try {
        	System.out.println("Connection established to remote " + this.socket.getInetAddress() + ":" + this.socket.getPort() + " from local address " + this.socket.getLocalAddress() + ":" + this.socket.getLocalPort());
            this.setOutputStream();
                        
            while(!Thread.currentThread().isInterrupted()) {
            	Object object = commandQueue.poll(timeout, TimeUnit.MILLISECONDS);
            	if (object == null) {
            		object = heartbeat;
            	}	
            	this.outputStream.writeObject(object);
                this.outputStream.flush();              
            } //while
            System.out.println("Connection closed.");
        } catch (IOException e) {
        	// This is fine - Server realizes that the client socket is gone
        } catch (InterruptedException e) {
			// This is fine - Thread is interrupted and should be gone
		} 
		
    } //run

    /**
     * initializes the OutputStream of the Sender object
     */
    private void setOutputStream() throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    } //setOutputStream
  
	/**
     *   
     * @param heartbeat the timeout to set
     */
    public void setHeartbeat(RemoteCommand heartbeat) {
		this.heartbeat = heartbeat;
	}

	/**
	 * @param timeout the timeout to set
	 */
    public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
    /**
     * receives a String message and writes it in sequences of bytes to the other host.
     *
     * @param message - message that should be sent to the other host.
     */
    public void sendMessage(String message) throws IOException {
        MessageCommand messageCommand = new MessageCommand("[" + TalkClient.user.getName() + "]: " + message);
        send(messageCommand);
    } //sendMessage

    /**
     * sends a object to the outputstream.
     *
     * @param object the object to send for.
     * @throws IOException throws an IO Exception
     */
    void send(Object object) throws IOException {
    	commandQueue.offer(object);
    } //send
} //Sender Class
