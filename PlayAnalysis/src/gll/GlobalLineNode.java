package gll;

import java.util.ArrayList;
import java.util.HashMap;

import line.Line; 

public class GlobalLineNode {
	
	HashMap<LineNodeHeader, NodeTransition> transitions = new HashMap<LineNodeHeader, NodeTransition> (); 
	
	private LineNodeHeader header;
	private Line internalLine = null; 
	private int gllIndex;
	
	public void setLine(Line line) {
		this.internalLine = line; 
	}
	
	public Line getLine() {
		return internalLine;
	}
	
	public void setHeader(LineNodeHeader header) {
		this.header = header; 
	}
	
	public LineNodeHeader getHeader() {
		return header;
	}
	
	public int getGllIndex() {
		return gllIndex; 
	}
	
	public GlobalLineNode getNextNode(LineNodeHeader header) {
		NodeTransition transition = transitions.get(header);
		return transition.getNext();
	}
	
	public GlobalLineNode getPreviousNode(LineNodeHeader header) {
		NodeTransition transition = transitions.get(header);
		return transition.getPrevious();
	}
	
	public void setNextNode(LineNodeHeader header, GlobalLineNode nextNode) {
		NodeTransition transition = transitions.get(header);
		transition.setNext(nextNode);
		transitions.put(header, transition);
	}
	
	private void setTransitions(ArrayList<GlobalLineNode> headerNodes) {
		for(int i = 0; i < headerNodes.size(); i++) {
			GlobalLineNode headerNode = headerNodes.get(i);
			NodeTransition transition = transitions.get(headerNode.getHeader());
			transition.setPrevious(headerNode);
			
			// Oh my god, so gross. I'm disgusted with myself.
			if (i == 0 && (header == LineNodeHeader.END ||
						   header == LineNodeHeader.DEFAULT || 
						   header == LineNodeHeader.SCENE || 
						   header == LineNodeHeader.ACT )) {
				headerNode.setNextNode(LineNodeHeader.DEFAULT, this);
			}
			
			if (i == 1 && (header == LineNodeHeader.END ||
						   header == LineNodeHeader.SCENE ||
						   header == LineNodeHeader.ACT)) {
				headerNode.setNextNode(LineNodeHeader.SCENE, this);
			}
			
			if (i == 2 && (header == LineNodeHeader.END ||
						   header == LineNodeHeader.ACT)) {
				headerNode.setNextNode(LineNodeHeader.ACT, this);
			}
			
			transitions.put(headerNode.getHeader(), transition);
		}
		
	}
	
	public void intializeTransitions() {
		for (LineNodeHeader header : LineNodeHeader.values()) {
			transitions.put(header, new NodeTransition());
		}
	}

	public GlobalLineNode(ArrayList<GlobalLineNode> headerNodes, LineNodeHeader header, Line line) {
		this.header = header;
		line.setGllNode(this);
	
		setLine(line);
		setHeader(header);
		
		intializeTransitions();
		if (headerNodes != null) {
			setTransitions(headerNodes); 
		}
	}
}
