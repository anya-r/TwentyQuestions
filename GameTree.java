
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * A model for the game of 20 questions
 *
 * @author Rick Mercer
 */
	
public class GameTree
{
	//nested node class
	class Node 
	{
		String data;
		Node left;
		Node right;

		Node(String data) 
		{
			this.data = data;
			right = null;
			left = null;
		}
	}

	//PIVs
	Node head;
	Node curr;
	String file;

	/**
	 * Constructor needed to create the game.
	 *
	 * @param fileName
	 *          this is the name of the file we need to import the game questions
	 *          and answers from.
	 */
	public GameTree(String fileName)
	{
		file = fileName;
		Scanner scan;
		try {
            scan = new Scanner(new File(fileName));
            head = tree(scan, head);
			curr = head;

			scan.close();

        }
        catch (FileNotFoundException m)
        {
            System.out.println("error: file not found");
        }
		
	}
	private Node tree(Scanner scan, Node root)
	{
		if(scan.hasNextLine() == false)
			return root;
		String str = scan.nextLine().trim();
		Node node = new Node(str);
		if (str.endsWith("?") == false)
			return node;
		node.left = tree(scan, node.left);
		node.right = tree(scan, node.right);

		return node;
		
	}
	/*
	 * Add a new question and answer to the currentNode. If the current node has
	 * the answer chicken, theGame.add("Does it swim?", "goose"); should change
	 * that node like this:
	 */
	// -----------Feathers?-----------------Feathers?------
	// -------------/----\------------------/-------\------
	// ------- chicken  horse-----Does it swim?-----horse--
	// -----------------------------/------\---------------
	// --------------------------goose--chicken-----------
	/**
	 * @param newQ
	 *          The question to add where the old answer was.
	 * @param newA
	 *          The new Yes answer for the new question.
	 */
	public void add(String newQ, String newA)
	{
		String x = curr.data;
		curr.data = newQ;
		curr.left = new Node(newA);
		curr.right = new Node(x);
		
	}

	/**
	 * True if getCurrent() returns an answer rather than a question.
	 *
	 * @return False if the current node is an internal node rather than an answer
	 *         at a leaf.
	 */
	public boolean foundAnswer()
	{
		String temp = curr.data;
		if (temp.endsWith("?"))
			return false;
		return true; 
		
	}

	/**
	 * Return the data for the current node, which could be a question or an
	 * answer.  Current will change based on the users progress through the game.
	 *
	 * @return The current question or answer.
	 */
	public String getCurrent()
	{
		return curr.data; 
	}

	/**
	 * Ask the game to update the current node by going left for Choice.yes or
	 * right for Choice.no Example code: theGame.playerSelected(Choice.Yes);
	 *
	 * @param yesOrNo
	 */
	public void playerSelected(Choice yesOrNo)
	{
		if (yesOrNo == Choice.Yes)
			curr = curr.left;
		else
			curr = curr.right;

	}

	/**
	 * Begin a game at the root of the tree. getCurrent should return the question
	 * at the root of this GameTree.
	 */
	public void reStart()
	{
		curr = head;
	}

	// @Override
	// public String toString()
	// {
	// 	return toString(head);
		
	// }
	// private String toString(Node root)
	// {
	// 	String x = "- " + root.data + "\n";
    //     if (root.left != null) {
    //         x += toString(root.left);
    //     }
    //     if (root.right != null) {
    //         x += toString(root.right);
    //     }
    //     return x;

	// }
	public String toString() {
		return toString(head, "");
	}

	public String toString(Node node, String level)
	{
		if (node == null) 
			return "";

		return toString(node.right, level + "- ") + level + node.data + "\n" + toString(node.left, level + "- ");
	}
	/**
	 * Overwrite the old file for this gameTree with the current state that may
	 * have new questions added since the game started.
	 *
	 **/
	public void saveGame()
	{

		PrintWriter diskFile = null;
		try {
			diskFile = new PrintWriter(new File(file));
		}
		catch (IOException io) {
			System.out.println("Could not create file: " + file);
		}

		saveGame(diskFile, head);
		
		diskFile.close();

	}
		
	private void saveGame(PrintWriter disk, Node root)
	{
		if (root == null)
			return;
		
		disk.println(root.data);
		saveGame(disk, root.left);
		saveGame(disk, root.right);
		
		
	}
}
