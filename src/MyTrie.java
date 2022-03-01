import java.util.*;

public class MyTrie {
	
	class TrieNode {
		private Map<Character,TrieNode> children;  // HashMap showing all the children for a char
		private String data;  // value of node
		private boolean isEndOfWord;  //Is this the end
		
		// Constructor
		public TrieNode(String input) {
			children = new TreeMap<Character,TrieNode>();
			data = input;
			isEndOfWord = false;
		}

		public boolean isEndOfWord() {
			return isEndOfWord;
		}

		public void setEndOfWord(boolean isEndOfWord) {
			this.isEndOfWord = isEndOfWord;
		}

		public Map<Character,TrieNode> getChildren() {
			return children;
		}

		public String getData() {
			return data;
		}
		
		
	}
	
	private TrieNode root;
	
	// overload for root
	public MyTrie() {
		root = new TrieNode("");
	}
	
	public boolean find(String element) {
		TrieNode current = root;
		for (int i=0; i < element.length(); i++) {
			Character ch = element.charAt(i);
			TrieNode node = current.getChildren().get(ch);
			if (node == null) return false;
			current = node;
		}
		return current.isEndOfWord();
	}
	
	public boolean insert(String element) {
		TrieNode current = root;
		for (int i=0; i < element.length(); i++) {
			Character ch = element.charAt(i);
			// checks to see if character in HashMap
			if (!current.getChildren().containsKey(ch))
				// Inserts in HashMap
				current.getChildren().put(ch, new TrieNode(ch.toString()));
			// Get Children node from HashMap (may be blank value)
			current = current.getChildren().get(ch);
		}
		current.setEndOfWord(true);
		return true;
	}
	
	public boolean delete(String element) {
		Stack<TrieNode> stack = new Stack<TrieNode>();
		TrieNode current = root;
		
		for (int i=0; i < element.length(); i++) {
			Character ch = element.charAt(i);
			TrieNode node = current.getChildren().get(ch);
			if (node == null) return false;
			stack.push(node);
			current = node;
		}
		
		if (!current.isEndOfWord()) return false;
		
		current.setEndOfWord(false);
		
		while (stack.size() > 1) {
			TrieNode node = stack.pop();
			TrieNode prev = stack.peek();
			if (prev.getChildren().size() > 1)
				prev.getChildren().remove(node.getData().charAt(0));
			else break;
		}
		
		return true;
	}
	
	/* Implement this method */
	public List<String> allWordsStartingWith(String prefix) {
		
		// List to return
		List<String> words = new ArrayList<String>();
		
		// Set to store words
		Set<String> store_words = new HashSet<String>();
		
		// check if prefix is null; return empty list
		if (prefix == null) {return words;}
		
		// check if prefix is empty; return empty list
		//if (prefix.compareTo("") == 0) {return words;}
		
		TrieNode current = root;
		
		// Check to see if prefix is in TrieNode
		// Check each letter in prefix
		for (int i=0; i < prefix.length(); i++) {
			Character ch = prefix.charAt(i);
			// checks to see if character child
			if (current.getChildren().containsKey(ch)) {
				// update current to new child node
				current = current.getChildren().get(ch);
			} else {
				// Prefix is not in structure; return empty list
				return words;
			}

		}
		
		//System.out.println("Prefix: "+ prefix);
		//System.out.println("current value: "+ current.data);
		
		// look at 
		if (current.isEndOfWord) {
			store_words.add(prefix);
			//System.out.println("word: "+ prefix);
			//System.out.println("Add store_words: "+ store_words);
		}
		
		// check each child node
		if (current.getChildren() != null) {
			for (Map.Entry<Character, TrieNode> entry : current.children.entrySet()) {
				TrieNode child = entry.getValue();
				store_words.addAll(getWords(prefix, child, store_words ));
			}
		}

		words.addAll(store_words);
		//System.out.println("Add words: "+ words);
		return words;
		
	}
	
	
	public Set<String> getWords(String prefix, TrieNode current, Set<String> store_words) {
		
		
		// variable to store prefix and data
		String word = prefix;
		word = word.concat(current.data);
		
		// part to add words to temp_word
		if (current.isEndOfWord) {
			store_words.add(word);
			//System.out.println("word: "+ word);
			//System.out.println("Add words: "+ store_words);
		}
		
		// iterate through children
		if (current.getChildren() != null) {
			for (Map.Entry<Character, TrieNode> entry : current.children.entrySet()) {

				TrieNode child = entry.getValue();
				store_words = getWords(word, child, store_words);
			}
		}
		return store_words;
	}
	
	
	
	public static void main(String[] args) {
		MyTrie dict = new MyTrie();
		dict.insert("cap");
		dict.insert("cape");
		dict.insert("caper");
		dict.insert("car");
		dict.insert("carry");
		dict.insert("care");
		dict.insert("carp");
		dict.insert("cart");
		dict.insert("cat");
		dict.insert("cater");
		dict.insert("dog");
		dict.insert("dearly");
		dict.insert("dealt");
		dict.insert("dean");
		
		List<String> test1 = dict.allWordsStartingWith(null);
		List<String> test2 = dict.allWordsStartingWith("hello");
		List<String> test3 = dict.allWordsStartingWith("car"); // not all
		List<String> test4 = dict.allWordsStartingWith("ca"); // not all 
		List<String> test5 = dict.allWordsStartingWith("dea");
		List<String> test6 = dict.allWordsStartingWith(""); // wrong logic
		
		Collections.sort(test1);
		Collections.sort(test2);
		Collections.sort(test3);
		Collections.sort(test4);
		Collections.sort(test5);
		Collections.sort(test6);
		
		boolean allPassed = true;
		
		if (!test1.toString().equals("[]")) {
			allPassed = false;
			System.out.println("Test 1 did not pass");
		}
		if (!test2.toString().equals("[]")) {
			allPassed = false;
			System.out.println("Test 2 did not pass");
		}
		
		if (!test3.toString().equals("[car, care, carp, carry, cart]")) {
			allPassed = false;
			System.out.println("Test 3 did not pass");
		}
		if (!test4.toString().equals("[cap, cape, caper, car, care, carp, carry, cart, cat, cater]")) {
			allPassed = false;
			System.out.println("Test 4 did not pass");
		}
		if (!test5.toString().equals("[dealt, dean, dearly]")) {
			allPassed = false;
			System.out.println("Test 5 did not pass");
		}
		if (!test6.toString().equals("[cap, cape, caper, car, care, carp, carry, cart, cat, cater, dealt, dean, dearly, dog]")) {
			allPassed = false;
			System.out.println("Test 6 did not pass");
		}
		
		if (allPassed) System.out.println("All tests passed!");
		
	}

}