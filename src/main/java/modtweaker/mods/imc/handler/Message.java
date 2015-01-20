package modtweaker.mods.imc.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;

import cpw.mods.fml.common.event.FMLInterModComms;

public class Message {

	public static String destination;
	public File[] files;
	public Scanner fileReader;

	public Message(String destination) {
		this.destination = destination;

		files = new File(destination).listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".imc")) {
					return true;
				}
				return false;
			}
		});
		try {
			for (File file : files) {

				this.fileReader = new Scanner(file);
				while (fileReader.hasNextLine()) {
					String command = fileReader.nextLine().trim();
					if (command.toLowerCase().startsWith("sendimc(") && command.substring(command.length() - 1).equals(";")) {
						command = command.substring("sendimc(".length()).replace(command.substring(command.length() - 1), "").replace(command.substring(command.length() - 1), "").replaceAll("\"", "");

						String[] input = command.split(",");
						FMLInterModComms.sendMessage(input[0], input[1], input[2]);
						System.out.println(input[0] + ", " + input[1] + ", " + input[2]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
