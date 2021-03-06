package action;

import character.Character; 

/*
 * Class Action:
 * Represents a single action conducted by a character in a dialogue sequence.
 * 	For example: BOB. I gave him the money, Charlie! [Gestures violently at CHARLIE]
 * 
 * An action encapsulates the information implicit in the parenthetical.
 * 
 * Code Example:
 * 	Character bob = new Character("Bob", "Mob boss and leader of the Hooligans");
 * 	Action action = new Action(bob, "Gestures");
 */
public class Action {
	private String relation;
	
	// Subject character: The assumed person who is doing to action.
	private Character scharacter;
	
	// Object character (optional): Don't 
	// necessarily need an object character.
	private Character ocharacter;

	public Action(Character scharacter, String relation) {
		this.scharacter = scharacter;
		this.relation = relation;
	}
	
	public Character getSubjectCharacter() {
		return scharacter;
	}
	
	public Character getObjectCharacter() {
		return ocharacter;
	}
	
	public String getRelation() {
		return relation;
	}
	
	public double sentimentAnalysisScore() {
		return -1.00;
	}
	
	// Much room to expand with this action. We can presumably 
	// due some sentiment analysis to determined the 
	// 'goodness'/'badness' of the action.
}
