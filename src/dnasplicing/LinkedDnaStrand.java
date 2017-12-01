package dnasplicing;

import org.apache.commons.lang3.*;

public class LinkedDnaStrand implements DnaStrand {

	private DnaSequenceNode head;
	private DnaSequenceNode currentNode;
	private DnaSequenceNode tail;
	private int nodeCount = 0;
	private int dnaCount = 0;
	private StringBuilder nucleotides;

	public LinkedDnaStrand(String dnaSequence) {
		DnaSequenceNode newNode = new DnaSequenceNode(dnaSequence);
		head = newNode;
		// newNode refers to head.
		newNode.previous = null;
		// newNode is the last node.
		newNode.next = null;
		currentNode = newNode;
		tail = newNode;
		nodeCount = 1;
	}

	public LinkedDnaStrand() {
		this("");
	}

	@Override
	public String toString() {
		nucleotides = new StringBuilder();
		currentNode = head;
		while (currentNode != null) {
			nucleotides.append(currentNode.dnaSequence);
			currentNode = currentNode.next;
		}
		return nucleotides.toString();
	}

	@Override
	public long getNucleotideCount() {
		dnaCount = this.toString().length();
		// X: dnaCount = nucleotides.toString().length();
		return dnaCount;
	}

	@Override
	public void append(String dnaSequence) {
		DnaSequenceNode newNode = new DnaSequenceNode(dnaSequence);
		while (currentNode.next != null) {
			currentNode = currentNode.next;
		}
		// newNode and currentNode refer each other.
		newNode.previous = currentNode;
		currentNode.next = newNode;

		currentNode = newNode;

		newNode.previous = tail;
		tail = newNode;
		// tail.next = null;
		nodeCount++;
		if (head.dnaSequence.isEmpty()) {
			head = head.next;
			head.previous = null;
			nodeCount--;
		}
	}

	@Override
	public DnaStrand cutSplice(String enzyme, String splicee) {
		int pos = 0;
		int start = 0;
		StringBuilder search = new StringBuilder(this.toString());
		boolean first = true;
		LinkedDnaStrand ret = null;

		while ((pos = search.indexOf(enzyme, pos)) >= 0) {
			if (first) {
				ret = new LinkedDnaStrand(search.substring(start, pos));
				first = false;
			} else {
				ret.append(search.substring(start, pos));

			}
			start = pos + enzyme.length();
			ret.append(splicee);
			pos++;
		}

		if (start < search.length()) {
			if (ret == null) {
				ret = new LinkedDnaStrand("");
			} else {
				ret.append(search.substring(start));
			}
		}
		return ret;
	}

	@Override
	public DnaStrand createReversedDnaStrand() {
		LinkedDnaStrand rvStrand = new LinkedDnaStrand();
		currentNode = tail;
		while (currentNode != null) {
			rvStrand.append(StringUtils.reverse(currentNode.dnaSequence));
			currentNode = currentNode.previous;
		}
		return rvStrand;
	}

	@Override
	public int getAppendCount() {
		return nodeCount - 1;
	}

	@Override
	public DnaSequenceNode getFirstNode() {
		return head;
	}

	@Override
	public int getNodeCount() {
		return nodeCount;
	}

}
