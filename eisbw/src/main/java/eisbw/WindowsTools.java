package eisbw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class WindowsTools {

	public static boolean IsProcessRunning(String process) 
	{
		try 
		{
			Process p = Runtime.getRuntime().exec("tasklist.exe");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			while ((line = stdInput.readLine()) != null) 
			{
				if (line.contains(process)) 
				{
					return true;
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void StartChaoslauncher(String race, String map, String sc_dir) throws IOException
	{		
		PopulateInitFile(race,map,sc_dir);
		Runtime.getRuntime().exec(sc_dir+"\\Chaoslauncher\\Chaoslauncher.exe", null, new File(sc_dir+"\\Chaoslauncher\\"));
	}
	
	private static void PopulateInitFile(String race, String map, String sc_dir) {
		String newLine = System.getProperty("line.separator");
		String bwapiDest = sc_dir + "\\bwapi-data\\bwapi.ini";	
		
		String BWINI = "";
		
		BWINI += ";Generated by GOAL launcher" + newLine;
		
		BWINI += "[ai]" + newLine;
		BWINI += "ai     = NULL" + newLine + newLine;

		BWINI += "[auto_menu]" + newLine;
		BWINI += "auto_menu = SINGLE_PLAYER" + newLine + newLine;

		BWINI += "pause_dbg = OFF" + newLine + newLine;

		BWINI += "auto_restart = OFF" + newLine + newLine;
		
		BWINI += "map = maps\\sscai\\" + map + newLine + newLine;

		BWINI += "race = " + race + newLine + newLine;

		BWINI += "enemy_count = 1" + newLine + newLine;
		
		BWINI += "enemy_race = Random" + newLine + newLine;
		
		BWINI += "[config]" + newLine;

		BWINI += "[window]" + newLine;
		BWINI += "windowed = ON" + newLine + newLine;
		BWINI += "width  = 1024" + newLine + newLine;
		BWINI += "height = 768" + newLine + newLine;

		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(bwapiDest)));
			out.write(BWINI);
			out.close();
		} 
		catch (Exception e) 
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
}
