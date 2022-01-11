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
	
	
	final static int PORT = 7777;
	
	public static void main(String[] args) {	
		
		String path = System.getProperty("user.home") + "/Desktop/Messages";
		BufferedReader in = null;
		Socket connection = null;
		PrintWriter out = null;
		
		try {
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Server successfully started on port " + PORT + " !");
			while(true) {
				try {
					connection = server.accept();
					in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					out = new PrintWriter(connection.getOutputStream());
					
					String message = in.readLine();
					MessageType msgType = decodeCommand(message);
					
					if(msgType == MessageType.SAVE) {
						String id = saveMessage(message, path);
						if(id != null) {
							out.println("OK " + id);
							out.flush();
						}else {
							out.println("Status 500: INTERNAL SERVER ERROR");
							out.flush();
						}
					}
					else if(msgType == MessageType.GET) {
						String toReturn = getMessage(message, path);
						if(toReturn != null) {
							out.println("OK " + toReturn);
							out.flush();
						}else {
							out.println("FAILED");
							out.flush();
						}
						
					}
					else if(msgType == MessageType.UNKNOWN) {
						out.println("Status 404: Unknown Command");
						out.flush();
					}
				}
				catch (IOException e) {
					
				}
				finally {
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
	
	public static MessageType decodeCommand(String msg) {
		
		String[] components = msg.split(" ");
		
		switch (components[0]) {
		case "SAVE": {
			return MessageType.SAVE;
		}
		case "GET": {
			return MessageType.GET;
		}
		default:
			return MessageType.UNKNOWN;
		}
		
	}
	
	public static String saveMessage(String msg, String path) {
		
		UUID msgID = UUID.randomUUID();
		String filePath = path + "/" + msgID + ".msg";
		String messageToSave = msg.substring(5);
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(messageToSave);
			writer.close();
			return msgID.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String getMessage(String msg, String path) {
		
		
		String id = msg.substring(4);
		String toRead = path + "/" + id + ".msg";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(toRead));
			String toReturn = reader.readLine();
			return toReturn;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	

}
