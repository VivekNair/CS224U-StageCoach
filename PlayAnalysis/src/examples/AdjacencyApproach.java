package examples;

import java.util.ArrayList;

import line.Line;
import play.StageCoach; 
import configuration.PlayConfig;
import configuration.PlayType;
import java.util.HashMap;
import character.Character; 
import line.quote.Quote;
import gll.GlobalLineNode;
import gll.LineNodeHeader;
import line.LineType;

public class AdjacencyApproach {

	private static StageCoach currentPlay; 
	private PlayConfig playConfig;
	//Represents the character edges
	static HashMap<String, Character> characters = new HashMap<String, Character>();
	static HashMap <Character, HashMap<Character, Integer>> edgeWeights = new HashMap <Character, HashMap<Character, Integer>>();

	private static void constructAdjacency() {
		characters = currentPlay.returnCharacters(null, null);
		for (String charName : characters.keySet()) {
			Character currentCharacter = characters.get(charName);
			ArrayList<Quote> quotes = currentCharacter.getQuotes();
			for (Quote quote : quotes) {
				System.out.println("Character " + charName + " has " + quotes.size() + " quotes.");
				int totalTokenCount = quote.getTotalTokens();
				GlobalLineNode nodeQuote = quote.getGllNode();
				GlobalLineNode prevNodeQuote = nodeQuote.getPreviousNode(LineNodeHeader.DEFAULT);
				GlobalLineNode nextNodeQuote = nodeQuote.getNextNode(LineNodeHeader.DEFAULT);
				// System.out.println("prevNodeQuote is " + prevNodeQuote + " and nextNodeQuote is " + nextNodeQuote);
				
				Quote previousQuote = null;
				Quote nextQuote = null;
				
				if (prevNodeQuote != null) { 
					Line previousLine = prevNodeQuote.getLine();
					if (previousLine.getType() == LineType.QUOTE) {
						previousQuote = (Quote) previousLine; 
					}
					if (previousQuote != null) {
						// System.out.println("Previous node is not null");
						System.out.println("The total token count is " + totalTokenCount);
						HashMap<Character, Integer> characterMap = edgeWeights.get(currentCharacter);
						if (characterMap == null) {
							characterMap = new HashMap<Character, Integer>();
						}
						Integer characterCount = characterMap.get(previousQuote.getCharacter());
						if (characterCount != null) {
							characterMap.put(previousQuote.getCharacter(), characterCount.intValue() + totalTokenCount);
						} else {
							characterMap.put(previousQuote.getCharacter(), totalTokenCount);
						}
						edgeWeights.put(currentCharacter, characterMap);
					}
				}
				
				if (nextNodeQuote != null) {
					Line nextLine = prevNodeQuote.getLine();
					if (nextLine.getType() == LineType.QUOTE) {
						nextQuote = (Quote) nextLine; 
					}
					if (nextQuote != null && nextQuote.getCharacter() != previousQuote.getCharacter()) {
						HashMap<Character, Integer> characterMap = edgeWeights.get(currentCharacter);
						if (characterMap == null) {
							characterMap = new HashMap<Character, Integer>();
						}
						Integer characterCount = characterMap.get(nextQuote.getCharacter());
						if (characterCount != null) {
							characterMap.put(nextQuote.getCharacter(), characterCount.intValue() + totalTokenCount);
						} else {
							characterMap.put(nextQuote.getCharacter(), totalTokenCount);
						}
						edgeWeights.put(currentCharacter, characterMap);
					}
				}
			}
		}
	}
	
	private static void outputAdjacency() {
		for (Character character : edgeWeights.keySet()) {
			HashMap<Character, Integer> hashedCharacterMap = edgeWeights.get(character);
			System.out.println("Character: " + character.getName());
			for (Character targetCharacter : hashedCharacterMap.keySet()) {
				System.out.println("   Target Character: " + targetCharacter.getName() + ", Value: " + hashedCharacterMap.get(targetCharacter));
			}
		}
	}
	
	public static void main(String args[]) {
		PlayConfig config = new PlayConfig(PlayType.DEFAULT);
		config.set("fileName", args[0]);
		
		currentPlay = new StageCoach(config);
		currentPlay.instantiatePlay();
		
		constructAdjacency();
		outputAdjacency();
	}
}
