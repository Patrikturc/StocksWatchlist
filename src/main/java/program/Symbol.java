package program;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import gui.CandlestickChart;
import gui.MainWindow;

/**
 * Symbol Library, contains all important methods and variables that are used
 * within the Stock Market Tool program
 * 
 * @author Patrik Turcani
 */
public class Symbol implements Comparable<Symbol> {
	// Private properties
	private String name;
	private String type;
	private String comment;
	private String description;
	private String chartURL;
	private Float todayGain;
	private Float todayClose;
	private boolean isCrypto;

	// Public properties
	public String GetName() {
		return name;
	}

	public String GetType() {
		return type;
	}

	public String GetComment() {
		return comment;
	}

	public String GetDescription() {
		return description;
	}

	public void SetDescription(String value) {
		this.description = value;
	}

	public String GetChartURL() {
		return chartURL;
	}

	public void SetChartURL(String value) {
		this.chartURL = value;
	}

	public float GetTodayGain() {
		return todayGain;
	}

	public float GetTodayClose() {
		return todayClose;
	}

	public boolean GetIsCrypto() {
		return isCrypto;
	}

	/**
	 * Default Constructor
	 */
	public Symbol() {

	}

	/**
	 * constructor that allows creation of new symbols
	 * 
	 * @param name        Please enter the name of the new symbol
	 * @param type        Please enter the type of the new symbol
	 * @param description Please enter the desciption of the new symbol
	 */
	public Symbol(String name, String type, String comment, boolean def) {
		this.name = name;
		this.type = type;
		this.comment = comment;
		isCrypto = false;
		// Check what code requested URL will return. If it is 404 this means the chart
		// doesn't exist
		// on Yahoo finance. Therefore symbol will also not exist on Yahoo finance.
		try {
			chartURL = CreateURL(name);
			if (CheckURL()) {
				CalculateLastRecordedChange();
				NewSymbol(this);
				if (!def)
					JOptionPane.showMessageDialog(null, "$" + this.GetName().toUpperCase() + " added successfully");

				MainWindow.Refresh();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * URL of the symbol(chart) is generated based on the symbol name and current
	 * time.
	 * 
	 * @return Returns usable URL if it exists on Yahoo Finance
	 */
	public String CreateURL(String n) {
		GetMidnightTime();
		long midnightUnixLastYear = GetMidnightTime() - 31536000;

		if (ReadExcelFile.getSize("https://query1.finance.yahoo.com/v7/finance/download/" + n.toUpperCase()
				+ "?period1=" + midnightUnixLastYear + "&period2=" + GetMidnightTime()
				+ "&interval=1d&events=history&includeAdjustedClose=true") > 256) {
			long unixTimeNow = Instant.now().getEpochSecond();
			chartURL = "https://query1.finance.yahoo.com/v7/finance/download/" + n.toUpperCase() + "?period1="
					+ (unixTimeNow - 31536000) + "&period2=" + unixTimeNow
					+ "&interval=1d&events=history&includeAdjustedClose=true";
		} else {
			chartURL = "https://query1.finance.yahoo.com/v7/finance/download/" + n.toUpperCase() + "?period1="
					+ midnightUnixLastYear + "&period2=" + GetMidnightTime()
					+ "&interval=1d&events=history&includeAdjustedClose=true";
		}
		return chartURL;
	}

	/**
	 * Calculates change of asset on the last recorded day
	 */
	public void CalculateLastRecordedChange() {
		String[][] dataArray = ReadExcelFile.Read(chartURL);
		double lastOpen = Double.parseDouble(dataArray[ReadExcelFile.getSize(chartURL) - 1][1]);
		double lastClose = Double.parseDouble(dataArray[ReadExcelFile.getSize(chartURL) - 1][4]);
		todayGain = (float) ((lastClose / lastOpen) * 100) - 100;
		todayClose = (float) Double.parseDouble(dataArray[ReadExcelFile.getSize(chartURL) - 1][4]);
	}

	/**
	 * Checks if generated URL exists
	 * 
	 * @return Returns true if URL exists, false if URL is not accessible
	 */
	private boolean CheckURL() {
		try {
			URL u = new URL(chartURL);
			HttpURLConnection check = (HttpURLConnection) u.openConnection();
			check.setRequestMethod("GET");
			check.connect();
			int code = check.getResponseCode();
			if (code != 404 && code != 401) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "Sorry, couldn't locate the chart");
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (ReadExcelFile.getSize(chartURL) > 260) {
			isCrypto = true;
		}
		return false;
	}

	/**
	 * If asset is not cryptocurrency, Yahoo finance only stores accessible data on
	 * midnight This method will calculate UNIX time today at midnight and return it
	 * 
	 * @return Unix time today at midnight
	 */
	public long GetMidnightTime() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date midnight = cal.getTime();
		// Convert to unix time
		long midnightUnix = (midnight.getTime() / 1000L) + 3600;
		return midnightUnix;
	}

	/**
	 * Method that adds this symbol to the database
	 * 
	 * @param Insert symbol to add to the database
	 */
	public void NewSymbol(Symbol s) {
		try {
			Database.SetSymbols(s);
			MainWindow.Refresh();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Binary search algorithm that searches for a particular symbol.
	 * 
	 * @param symArray Array that should contain the symbol.
	 * @param sym      Symbol to search for.
	 * @param left     current of the search index, usually a 0, unless, looking to
	 *                 search through a specific amount of data.
	 * @param right    end of the array, usually the length of the array, unless
	 *                 looking to search through a specific amount of data.
	 * @return Returns symbol if it was found.
	 */
	public Symbol BinarySearch(Symbol[] symArray, Symbol sym, int left, int right) {
		while (left <= right) {
			int mid = (left + right) / 2;

			int compare = sym.GetName().compareTo(symArray[mid].GetName());

			if (compare == 0)
				return symArray[mid];
			if (compare > 0)
				left = mid + 1;
			else
				right = mid - 1;
		}
		return null;
	}

	/**
	 * Method that removes symbol from the database
	 * 
	 * @param s Symbol to be removed from the database
	 */
	public void RemoveSymbol(Symbol s) {
		try {
			Database.GetMySymbols().DeleteVal(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method that creates chart window
	 */
	public void GetChart() {
		CandlestickChart cChart = new CandlestickChart(name, chartURL);
	}

	/**
	 * Sorts all "Lows" using quick sort
	 */
	public void sortLow() {
		String[][] arrayToSort = ReadExcelFile.Read(chartURL);
		MyArray low = new MyArray(ReadExcelFile.getSize(chartURL));
		// Create Array of lows
		for (int i = 0; i <= arrayToSort.length - 1; i++) {
			for (int j = 0; j <= arrayToSort[0].length; j++) {
				if (j == 3) {
					try {
						low.AddEnd(Double.parseDouble(arrayToSort[i][j]));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		final long current = System.currentTimeMillis();
		low.BubbleSort();
		// low.QuickSort(low.GetMyArray(), 0, ReadExcelFile.getSize(chartURL) - 1);
		final long durationInMilliseconds = System.currentTimeMillis() - current;
		System.out.println("Bubble Sort took " + durationInMilliseconds + "ms.");
	}

	/**
	 * Looks for a particular symbol in the database using binary search
	 * 
	 * @param symName insert symbol to search for
	 * @return Symbol if found
	 */
	public Symbol SearchSymbol(String symName) {
		Symbol[] symArray = new Symbol[Database.GetMySymbols().Count()];
		Symbol sym = null;
		int i = 0;
		for (Symbol symb : Database.GetMySymbols()) {
			try {
				symArray[i] = symb;

			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}
		for (Symbol symb : Database.GetMySymbols()) {
			if (symb.GetName().equals(symName)) {
				sym = symb;
				break;
			}

		}
		symArray = SymbolSortByName(symArray);

		return BinarySearch(symArray, sym, 0, Database.GetMySymbols().Count() - 1);
	}

	/**
	 * Using Selection sort this method sorts the array of symbols
	 * 
	 * @param symArray Insert array to sort
	 * @return Sorted array
	 */
	public Symbol[] SymbolSortByName(Symbol[] symArray) {
		int n = symArray.length;
		int i = 0;
		for (int j = 1; j < n; j++) {
			Symbol key = symArray[j];
			String keyName = symArray[j].GetName();
			i = j - 1;

			while (i >= 0) {
				if (keyName.compareTo(symArray[i].GetName()) > 0) {
					break;
				}
				symArray[i + 1] = symArray[i];
				i--;
			}
			symArray[i + 1] = key;
		}
		return symArray;
	}

	/**
	 * output method to help organise how contents of the symbol are displayed
	 * 
	 * @return desired output
	 */
	public StringBuilder Output() {
		StringBuilder builder = new StringBuilder();

		builder.append("<html><pre>");
		if (name.length() < 6 && todayClose > 100)
			builder.append(String.format("%s \t \t %.2f \t %.3f %s \t %s \t %s", "$" + name.toUpperCase() + ":",
					todayClose, todayGain, "%", type, comment));
		else if (name.length() < 6 && todayClose < 100)
			builder.append(String.format("%s \t \t %.2f \t \t %.3f %s \t %s \t %s", "$" + name.toUpperCase() + ":",
					todayClose, todayGain, "%", type, comment));
		else if (name.length() < 6 && todayClose < 1)
			builder.append(String.format("%s \t \t %.3f \t \t \t %.3f %s \t %s \t %s", "$" + name.toUpperCase() + ":",
					todayClose, todayGain, "%", type, comment));
		else if (name.length() > 6 && todayClose < 10)
			builder.append(String.format("%s \t %.3f \t \t %.3f %s \t %s \t %s", "$" + name.toUpperCase() + ":",
					todayClose, todayGain, "%", type, comment));
		else
			builder.append(String.format("%s \t %.2f \t %.3f %s \t %s \t %s", "$" + name.toUpperCase() + ":",
					todayClose, todayGain, "%", type, comment));
		builder.append("</pre></html>");
		return builder;
	}

	/**
	 * Sorts most recent change in price from highest gain to highest loss, using a
	 * bubble sort algorithm.
	 * 
	 * @param b
	 */
	public static void SortByPrice(boolean b) {
		Symbol[] symArray = new Symbol[Database.GetMySymbols().Count()];
		MyArray temp = new MyArray(Database.GetMySymbols().Count());
		int i = 0;
		for (Symbol sym : Database.GetMySymbols()) {
			try {
				temp.AddEnd((double) sym.todayGain);
				symArray[i] = sym;
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
			i++;
		}
		if (b)
			symArray = temp.SymbolBubbleSortLowToHigh(symArray);
		else
			symArray = temp.SymbolBubbleSortHighToLow(symArray);
		Database.GetMySymbols().Clear();
		for (int j = 0; j < symArray.length; j++) {
			Database.GetMySymbols().AddTail(symArray[j]);
		}
	}

	/**
	 * Sorts the database array of symbols alphabetically
	 */
	public void SortAlphabetically() {

		Symbol[] symArray = new Symbol[Database.GetMySymbols().Count()];
		int i = 0;
		for (Symbol sym : Database.GetMySymbols()) {
			try {
				symArray[i] = sym;
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
			i++;
		}
		SymbolSortByName(symArray);
		Database.GetMySymbols().Clear();
		for (int j = 0; j < symArray.length; j++) {
			Database.GetMySymbols().AddTail(symArray[j]);
		}

	}

	/**
	 * overriding the toString() method to display name of symbol as String.
	 */
	public String toString() {
		String strout = "";
		strout += name;
		return strout;
	}

	/**
	 * Interface Method
	 */
	public int compareTo(Symbol o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
