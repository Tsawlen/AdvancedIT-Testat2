package de.tsawlen.client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	//Configuration
	final static String HOSTNAME = "localhost";
	final static int PORT = 7777;
	
	/**
	 * The main method of the client. 
	 * 
	 * @param args - start parameters of the programm UNUSED!
	 */
	public static void main(String[] args) {
		
		//initialize needed variables as null pointers
		PrintWriter networkOut = null;
		BufferedReader networkIn = null;
		Socket client = null;
		
		//loop
		while(true) {
			try {
				//create client socket. Creating a new one, because the server closed every previous connection
				client = new Socket(HOSTNAME, PORT);
				System.out.println("Bitte geben sie ein Kommando ein: ");
				//reader for incoming messages
				networkIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
				//writer for outbound messages
				networkOut = new PrintWriter(client.getOutputStream());
				//reader for user input
				BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
				//read the command the user wants to send
				String message = userIn.readLine();
				//check if the user wants to terminate the programm
				if(message.equals(".")) {
					break;
				}
				//write the message to outbound stream
				networkOut.println(message);
				//send the message
				networkOut.flush();
				System.out.println(networkIn.readLine());
			}
			catch (UnknownHostException uHE) {
				System.out.println("Host not found!");
				break;
			}
			catch(IOException e) {
				System.out.println("Connection failed!");
				break;
			}
			catch (Exception e) {
				System.out.println("Something failed!");
				break;
			}
			finally {
				//close connection if one exists
				if(client != null) {
					try {
						client.close();
					}
					catch (IOException e) {
						
					}
				}
			}
			
		}
			
		
		
		
	}

}
