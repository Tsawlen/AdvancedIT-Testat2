package de.tsawlen.server.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Server {
	
	
	//Configuration
	final static int PORT = 7777;
	
	
	/**
	 * Entry-point for program Server.java
	 * 
	 * @param args - unused
	 */
	public static void main(String[] args) {	
		
		//create needed variables
		String path = System.getProperty("user.home") + "/Desktop/Messages";
		BufferedReader in = null;
		Socket connection = null;
		PrintWriter out = null;
		
		//try creating a server socket
		try {
			//create a server socket
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Server successfully started on port " + PORT + " !");
			while(true) {
				//try accepting commands and handle them
				try {
					//accept connection and wait for one if none is incoming
					connection = server.accept();
					//reader for incoming messages
					in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					//writer for outbound messages
					out = new PrintWriter(connection.getOutputStream());
					
					//get command send by user
					String message = in.readLine();
					//determine message and command type
					MessageType msgType = decodeCommand(message);
					
					//if command is for saving a string
					if(msgType == MessageType.SAVE) {
						//try saving the message
						String id = saveMessage(message, path);
						//when id is not null the message has been saved. If id is null it wasn't saved and the server sends an error
						if(id != null) {
							out.println("KEY " + id);
							out.flush();
						}else {
							out.println("Status 500: INTERNAL SERVER ERROR");
							out.flush();
						}
					}
					//if command is for getting a saved string
					else if(msgType == MessageType.GET) {
						//Try to get the saved message
						String toReturn = getMessage(message, path);
						//The message is found and read and the server sends it back to the user
						if(toReturn != null) {
							out.println("OK " + toReturn);
							out.flush();
						}
						//The message was not found or an error appeared. Server sends error
						else {
							out.println("FAILED");
							out.flush();
						}
						
					}
					//if user is sending a unknown command
					else if(msgType == MessageType.UNKNOWN) {
						out.println("Status 404: Unknown Command");
						out.flush();
					}
				}
				catch (IOException e) {
					
				}
				finally {
					//Close a connection if one exists
					try {
						connection.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		catch (Exception e) {
			
		}
		
	}
	
	/**
	 * This method is responsible for decoding the type of the message.
	 *
	 * @param msg - the message the user send
	 * @return MessageType - the type of the message
	 */
	public static MessageType decodeCommand(String msg) {
		
		String[] components = msg.split(" ");
		
		switch (components[0].toUpperCase()) {
		//the first word of the Message is save
		case "SAVE": {
			return MessageType.SAVE;
		}
		//the first word of the Message is get
		case "GET": {
			return MessageType.GET;
		}
		//the first word of the Message does not equal a command
		default:
			return MessageType.UNKNOWN;
		}
		
	}
	
	/**
	 * This method saves a message send by the user.
	 * @param msg - the message the user send
	 * @param path - the path to save to
	 * @return String - the Id of the saved message
	 */
	public static String saveMessage(String msg, String path) {
		
		//generate an unique key for the message. UUID was chosen, because it always is unique by definition
		UUID msgID = UUID.randomUUID();
		//build the path to save the file to
		String filePath = path + "/" + msgID + ".msg";
		//get the message without the command
		String messageToSave = msg.substring(5);
		try {
			//Get a writer to save the message
			FileWriter writer = new FileWriter(filePath);
			//write the message
			writer.write(messageToSave);
			//close the writer
			writer.close();
			//return the Id of the saved message
			return msgID.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return that the process failed
			return null;
		}
		
	}
	
	/**
	 * This method is responsible for getting a saved message by its key
	 * @param msg - the message the user send, including the key
	 * @param path - the path where the system saves messages
	 * @return String - the message
	 */
	public static String getMessage(String msg, String path) {
		
		//Get the id of the message
		String id = msg.substring(4);
		//build the path where the message is stored
		String toRead = path + "/" + id + ".msg";
		try {
			//get a reader to read the message from the file
			BufferedReader reader = new BufferedReader(new FileReader(toRead));
			//read the content of the file
			String toReturn = reader.readLine();
			//return the content
			return toReturn;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return that the process failed
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return that the process failed
			return null;
		}
		
	}
	

}
