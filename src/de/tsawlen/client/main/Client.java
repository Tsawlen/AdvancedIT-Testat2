package de.tsawlen.client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	final static String HOSTNAME = "localhost";
	final static int PORT = 7777;
	
	public static void main(String[] args) {
		
		PrintWriter networkOut = null;
		BufferedReader networkIn = null;
		Socket client = null;
		
		while(true) {
			try {
				client = new Socket(HOSTNAME, PORT);
				System.out.println("Bitte geben sie ein Kommando ein: ");
				networkIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
				networkOut = new PrintWriter(client.getOutputStream());
				BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
				String message = userIn.readLine();
				if(message.equals(".")) {
					break;
				}
				networkOut.println(message);
				networkOut.flush();
				System.out.println(networkIn.readLine());
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			finally {
				if(client != null) {
					try {
						client.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
			
		
		
		
	}

}
