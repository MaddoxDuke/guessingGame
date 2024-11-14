import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Guess {
	public static void main(String[] args) {
	Tree t = new Tree();
	Scanner s = new Scanner(System.in);
	String fileName = "data.csv";
	t.root.question = "Elephant";
	
	
	importTree(fileName , t);
	
	boolean playing = true;
	Node c = t.root;
	String outstring = "";
	String Astr = "";
	char a = ' ';

	while(playing) {
		if(t.isLeaf(c)) {
			outstring = "Are you a/an " + c.question;
		}
		else {
			outstring = c.question;
		}

		System.out.println(outstring + " Y/N/Q >");
		Astr = s.next();
		a = Astr.charAt(0);


		switch(a) {
			case 'Y':
			case 'y':
			if (t.isLeaf(c)) {
				System.out.println("I am so smart. I bet I can beat you again.\n");
				//Play again
				c = t.root;
				System.out.println("\n\nLet's play again");
			}
			else {
				c = c.yes;
			}
			break;

			case 'N':
			case 'n':
				if(t.isLeaf(c)) {

					s.nextLine();

					System.out.println("What is your animal?");
					String animal = s.nextLine();
					System.out.println("Enter a question that determines a " + animal + " from a " + c.question + " > ");

					String q = s.nextLine();

					System.out.println("Is the answer for a " + animal + " Yes or No? (Y/N)>");

					String yn = s.nextLine();
					yn = "" + yn.charAt(0);
					Node newAnimal = new Node(animal);
					Node oldAnimal = new Node(c.question);
					c.question = q;

					if(yn.equalsIgnoreCase("y")) {
						c.yes = newAnimal;
						c.no = oldAnimal;
					}
					else {
						c.no = newAnimal;
						c.yes = oldAnimal;
					}
					//Play again
					c = t.root;
					System.out.println("\n\nLet's play again");
				}
				else {
					c = c.no;
				}
				break;
			case 'q':
			case 'Q':
				playing = false;
				break;
			default:
				System.out.println("*** Y/N/Q Only ***");
				break;
		}
	}
	deleteFile(fileName);


	s.close();
	try {
		exportTree(t.root, 1, fileName);
	} catch (IOException e) {
		e.printStackTrace();
	}
	System.out.println("Goodbye");
}

	public static void deleteFile(String dataFile) {
		try {
			FileWriter fw = new FileWriter(dataFile, false);
			fw.close();
		} catch (IOException e) {
			System.out.println("File Error");
		}
	}
	// Add functionality to importTree() to rebuild the tree t from the array 
	// (into which we have already populated from the file).  Have the program
	// run this import function from the start.  Prime the animals.csv file with
	// the first line "1,Elephant" and replace the existing startup code that 
	// currently starts us off.  (replace the stuff that creates Tree t and the
	// first node)
	private static void importTree(String dataFile, Tree t) {
		
		try {
			FileReader fr = new FileReader(dataFile);
			BufferedReader br = new BufferedReader(fr);
			
			String line = "";
			String splitBy = ",";
			ArrayList<Node> questions = new ArrayList<>();
			
			// initializes the list with null values so we do not run out of space.
			questions.add(null);
			
			
			while ((line = br.readLine()) != null) {
				String[] data = line.split(splitBy);
				
				int index = Integer.parseInt(data[0].trim());
				String question = data[1].trim();
				
				while (questions.size() <= index) questions.add(null); // Addicted to one liners.
				
				questions.set(index, new Node(question));
			}
			
			br.close();
			
			//Initialize root node
			if (questions.size() > 1) t.root = questions.get(1); // Hashtag one liner
	        
			for (int i = 1; i < questions.size(); i++) {

				Node node = questions.get(i);	
				//right child of the new binary tree	
				if (2 * i + 1 < questions.size() && questions.get(2 * i + 1) != null) node.no = questions.get(2 * i + 1);
				//left child of the binary tree
				if (2 * i < questions.size() && questions.get(2 * i) != null) node.yes = questions.get(2 * i);
				
			}
		} catch (IOException e) {
			System.out.println("File Error: " + dataFile);
		}
		

	}

	public static void exportTree(Node r, int nodeID, String dataFile) throws IOException {
		if (r.yes != null) exportTree(r.yes, nodeID * 2, dataFile); //Id get multiplied by 2 if yes
		

		System.out.println(nodeID + " " + r.question);
		writeToFile(dataFile,nodeID,r.question);
		
		if (r.no != null) exportTree(r.no, nodeID * 2 + 1,dataFile); //Id get multiplied by 2 and ++ if no
	}

	private static void writeToFile(String fileName, int nodeID, String question) {
		
		try {
			FileWriter fw = new FileWriter(fileName,true);
			String outString = String.format("%d,%s\n",nodeID,question);
			fw.append(outString);
			fw.close();
		}
		catch (IOException e) {
			System.out.println("File Error");
		}
	}
}
