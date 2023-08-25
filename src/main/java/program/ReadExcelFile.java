package program;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * 
 * @author Patrik Turcani
 * @author Binkan Salaryman Inspiration
 *         https://stackoverflow.com/questions/29252702/turning-a-text-file-into-a-2d-array-in-java/31181543
 */
public class ReadExcelFile {

	// private variables
	private static URL rowdata;
	private static URLConnection data;

	public ReadExcelFile() {

	}

	/**
	 * Read the database type file contained within the url provided to the method
	 * 
	 * @param url to scan
	 * @return 2D Array containing identical data to the file within the url
	 *         provided
	 */
	public static String[][] Read(String url) {

		String[][] dataArray = new String[getSize(url)][]; // Date, Open, High, Low, Close, adj Close, Volume
		try {
			rowdata = new URL(url);
			data = rowdata.openConnection();
			Scanner input = new Scanner(data.getInputStream());
			int row = 0;
			input.nextLine();

			while (input.hasNextLine()) {
				String line = input.nextLine();
				dataArray[row] = line.split(",");
				row++;
			}
		} catch (Exception e) {
			System.out.print(e);
		}
		return dataArray;
	}

	/**
	 * Method that counts all rows in file/url that it receives
	 * 
	 * @param url Insert url containing a database file
	 * @return
	 */
	public static int getSize(String url) {
		int size = -1;
		try {
			URL rowdata = new URL(url);
			URLConnection data = rowdata.openConnection();
			Scanner input = new Scanner(data.getInputStream());
			while (input.hasNextLine()) {
				size++;
				input.nextLine();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return size;
	}

}