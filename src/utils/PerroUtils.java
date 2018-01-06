package utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/** 
 * Collezione di piccole utils o funzioni da usare alla bisogna per semplificarsi la vita
 * 
 * @author: G. Perrone
 * @version 1.0 26/08/2016
 * 
 */
public class PerroUtils {

	/**
	 * Scrive sulla console la stringa passata come argomento usando il metodo println 
	 * @param strPrint
	 */
		public static void print(String strPrint) {
			System.out.println(strPrint);
		}

	/**
	 * Scrive sulla console la stringa passata come argomento usando il metodo println 
	 * @param bPrintTime specifies if current time has to be printed on screen
	 * @param strPrint
	 */
		public static void print(String strPrint, boolean bPrintTime) {
			System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " : " + strPrint);
		}

/** 
 * Mostra un messaggio di dialogo di informazione con parametri passati come argomenti
 * @param infoMessage - il testo del messaggio principale
 * @param titleBar - il titolo della finestra
 */
	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "I: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
/**
 * Mostra un messaggio di dialogo di errore con parametri passati come argomenti
 * @param errMessage: il testo del messaggio principale
 * @param titleBar: il titolo della finestra
 */
	public static void errBox(String errMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, errMessage, "ERR: " + titleBar, JOptionPane.ERROR_MESSAGE);
	}
	
	public static int YNBox(String strMessage, String titleBar) {
		return JOptionPane.showConfirmDialog(null, strMessage , titleBar, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
		
	}

	/**
	 * Reads all lines from a file and returns the content in an arraylist
	 * 
	 * @param 	fileName	string containing name of the file to be read
	 * @return	ArrayList	ArrayList object containing the lines of the file read (one item per each line)
	 */
	public static List getFileToList(String fileName) {
		
		List<String> lstString = new ArrayList<String>();
				
		// all lines from files are read and put in an arraylist
		try {
			lstString= Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return lstString;
	}
	
	/**
	 * Returns an ArrayList of Strings with the single items included in the line to be parsed from the csv
	 * Separator to be used in passed as parameter
	 *  
	 * @param strLine	The String to be parsed
	 * @param sep		The char used as separator (normally ";")
	 * @return			ArrayList of String with the items parsed from strLine
	 */
	public static ArrayList<String> parseCSVLine(String strLine, char sep) {
		ArrayList<String> Result = new ArrayList<String>();
		
		if (strLine != null) {
			String[] splitData = strLine.split("\\s*"+ sep + "\\s*");
			for (int i = 0; i < splitData.length; i++) {
				if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
					Result.add(splitData[i].trim());
				}
			}
		}
	return Result;
	}
	

	/**
	 * converts a string to an int
	 * 
	 * @param str
	 * @return
	 */
	public static int StringToInt(String str) {
		return ((Integer.valueOf(str)).intValue());
	}

	/**
	 * converts a string to a double
	 * 
	 * @param str
	 * @return double the contents of str converted to a double
	 */
	public static double StringToDbl(String str) {
		return ((Double.valueOf(str)).doubleValue());
	}

	/**
	 * Writes contents of a list of String objects into a file
	 * 
	 * @param strNomeCSV	Name of the .csv file to be creates (actually could be a generic text file) including full path
	 * @param strOutput		List of String objects that will be written to the file
	 */
	public static boolean writeCSV(String strNomeCSV, List<String> strOutput) {
		try {
			Files.write(Paths.get(strNomeCSV), strOutput, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	
}
