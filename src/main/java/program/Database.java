package program;

public class Database {

	// Private properties
	private static MySinglyLinkedList<Symbol> mS = new MySinglyLinkedList<Symbol>();
	private static Database data = new Database();

	// Public properties
	public static Database database() {
		return data;
	}

	public static MySinglyLinkedList<Symbol> GetMySymbols() {
		return mS;
	}

	/**
	 * Setter method for list of symbols
	 * 
	 * @param newSymbol
	 */
	public static void SetSymbols(Symbol newSymbol) {
		mS.AddHead(newSymbol);
	}

	/**
	 * Database constructor
	 */
	public Database() {

	}

}
