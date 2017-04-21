package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ie.gmit.sw.ai.nn.Activators.Activator;
import ie.gmit.sw.traversers.*;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class GameRunner implements KeyListener
{
	private static final int MAZE_DIMENSION = 100; //100*100(10000)
	private static final int IMAGE_COUNT = 16; //16 images
	private GameView view;
	private Maze model;
	private int currentRow;
	private int currentCol;
	//This will be the score for the player
	private int meowthTrophyScore = 0;
	private Node[][] maze;
	private Node goal;
 	//added in workshop to explain FuzzyLogic
 	private double health = 40;
 	public static double attackPower = 5;

 	/* Needed to start at 1 (As stated in the fcl file) -
 	   as anger starts at 1 there.
 	*/
 	public static double anger = 1;


    //Neural Network weights
 	double[][] data = { //Health, Sword, bomb, Enemies
			{ 2, 0, 0, 0 }, { 2, 0, 0, 1 }, { 2, 0, 1, 1 }, { 2, 0, 1, 2 }, { 2, 1, 0, 2 },
			{ 2, 1, 0, 1 }, { 1, 0, 0, 0 }, { 1, 0, 0, 1 }, { 1, 0, 1, 1 }, { 1, 0, 1, 2 },
			{ 1, 1, 0, 2 }, { 1, 1, 0, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 1 },
			{ 0, 0, 1, 2 }, { 0, 1, 0, 2 }, { 0, 1, 0, 1 } };

	double[][] expected = { //Panic, Attack, avoid, Run
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 },
			{ 0.0, 0.0, 0.0, 1.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 },
			{ 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 },
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 1.0, 0.0, 0.0 },
			{ 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 } };

	//Neural Network states
	public void panic()
	{
		System.out.println("The best course of action is to Panic!");
	}

	public void attack()
	{
		System.out.println("The best thing to do is to Attack!!");
	}

	public void avoid()
	{
		System.out.println("You are better to just Avoid the situation!");
	}

	public void runAway()
	{
		System.out.println("Run Away, you'll never get through unharmed!");
	}


	public GameRunner() throws Exception
	{
		MazeAlgoGeneratorFactory factory = MazeAlgoGeneratorFactory.getInstance();
		MazeAlgoGenerator generator = factory.getMazeGenerator(MazeAlgoGenerator.GeneratorAlgorithm.HuntAndKill, MAZE_DIMENSION, MAZE_DIMENSION);

		maze = generator.getMaze();
		//getting our goal node
		goal = generator.getGoalNode();
		model = new Maze(MAZE_DIMENSION); //1000*1000
		//adds goal to our model
    	view = new GameView(model, goal);

    	Sprite[] sprites = getSprites();
    	view.setSprites(sprites);

    	placePlayer(); //randomly place Spartan Warrior

    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE); //set view
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);

    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development) Claire Finn (G00310769) & Declan Duffy (G00318025)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);

      //Heuristic Search Algo to find our Spartan's starting position in the maze
   	 Traversator t = new AStarTraversator(goal);
     t.traverse(maze, maze[0][0]);
	}

	private void placePlayer()
	{
		//We place the Spartan Warrior (Player) on the goal node
		currentRow = goal.getRow();
    	currentCol = goal.getCol();
    	model.set(currentRow, currentCol, '5'); //A Spartan warrior is at index 5
    	updateView();
	}

	private void updateView()
	{
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
		//AMG.setGoalNode();
		//System.out.println("Current Row Spartan: " + "\n" + currentRow + "\n" + "Current Col Spartan:" + "\n" + currentCol);
	}

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1)
        {
        	if (isValidMove(currentRow, currentCol + 1)) currentCol++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0)
        {
        	if (isValidMove(currentRow, currentCol - 1)) currentCol--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0)
        {
        	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1)
        {
        	if (isValidMove(currentRow + 1, currentCol)) currentRow++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_Z)
        {
        	view.toggleZoom();
        }
        else
        {
        	return;
        }

        updateView();
    }
    public void keyReleased(KeyEvent e)
    {

    } //Ignore
	public void keyTyped(KeyEvent e)
	{

	} //Ignore


	private boolean isValidMove(int row, int col)
	{

		//This handles the anger level. We don't want it going over 100 so
		//we set it to 100 if it 'goes over' 100.
		if (anger >=100)
	 	{
	 		anger = 100;
	 	}

		//Interacting with spaces
		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ' ')
		{
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			return true;
		}
		//Interacting with MeowthTrophy (We collect 5 to win)
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '6')
		{
			//replace the trophy with a space when 'picked up'
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			//10 points every time we get a trophy
			meowthTrophyScore += 10;
			//printing our score
			System.out.println("Score" + "\n" + meowthTrophyScore);
			//Our win condition (reach a score of 50)
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			return true;
		}

		//Interacting with swords
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '1')
		{
			//replace with space when 'picked up'
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '0');
			//attack is ten when a sword is 'picked up' (you lose other weapons/can only have 1 weapon at a time with a certain attack power)
			attackPower = 10;
			//print out attack power
			System.out.println("Attack Power" + "\n" + attackPower);
			return true;
		}

		//Interacting with 'help'
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '2')
		{
			//change help to a space when 'picked up'
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '0');
			//Explain rules of the game to the player with a message dialogue
			JOptionPane.showInputDialog("Zoom Out (Z) to see the whole map, that way you can keep an eye on your targets!"
										+ "\n" + "The red dots are the enemy spiders, use weapons to defeat them!"
										+ "\n" + "The colour of the spider determines their strength."
										+ "\n" + "It's up to you to determine which spiders are stronger by attacking them!"
										+ "\n" + "HINT: Watch out for the dangerous 'Black Widow' spider, it's deadly!!"
										+ "\n" + "Make sure to look for the blue dots - The trophies!"
										+ "\n" + "Collect 5 trophies to win the game!"
										+ "\n" + "Collect weapons along the way to fight - hint: The MOAB will help a lot!!"
										+ "\n" + "Now, go fight those eight-legged critters! Good luck!");
			return true;
		}

		//Interacting with a bomb
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '3')
		{
			//change bomb to a space when 'picked up'
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '0');
			//attack power is now 20
			attackPower = 20;
			//print out our attack power stat
			System.out.println("Attack Power" + "\n" + attackPower);
			return true;
		}

		//Interacting with our hbomb
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '4')
		{
			//change to a space when 'picked up'
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '0');
			//attack power is now 30
			attackPower = 30;
			//print out our attack stat
			System.out.println("Attack Power" + "\n" + attackPower);
			return true;
		}

		//Interacting with our MOAB (Mother Of All Bombs) - The strongest weapon!
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '7')
		{
			//change to a space when 'picked up'
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '0');
			//our attack power is now 50 (The highest)
			attackPower = 50;
			//Printing out our attack power stat
			System.out.println("Attack Power" + "\n" + attackPower);
			return true;
		}

		//Interacting with our black spider (The 'Black Widow' spider!)
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '8')
		{
			//Change to a space when defeated
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			//Score goes up by 1
			meowthTrophyScore += 1;
			//The spider's anger goes up by 20
			anger+=20;
			//The player loses attack power (This spider does the most damage)
			attackPower -= 40;

			//Our damage calculation for player having weapon or not
			if (attackPower <= 0)
			{
				health -=15;
			}
			else
			{
			    health -=10;
			}

			//Printing out the current stats
			System.out.println("Score" + "\n" + meowthTrophyScore);
			System.out.println("Spartan Health" + "\n" + health);
			System.out.println("Attack Power" + "\n" + attackPower);

			//Win condition
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			//lose condition
			else if (health <= 0)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You Lost! Your score is" + "\n" + meowthTrophyScore);
			}
			return true;
		}

		//Continues on as interactions for the rest of the spiders in different
		//'else ifs' from strongest to weakest in the same fashion as the black spider
		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '9')
		{
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			meowthTrophyScore += 1;
			anger+=20;
			attackPower -= 25;
			if (attackPower <= 0)
			{
				health -=11;
			}
			else
			{
			    health -=7;
			}
			System.out.println("Score" + "\n" + meowthTrophyScore);
			System.out.println("Spartan Health" + "\n" + health);
			System.out.println("Attack Power" + "\n" + attackPower);
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			else if (health <= 0)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You Lost! Your score is" + "\n" + meowthTrophyScore);
			}
			return true;
		}

		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ':')
		{
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			anger+=20;
			meowthTrophyScore += 1;
			attackPower -= 15;
			if (attackPower <= 0)
			{
				health -=10;
			}
			else
			{
			    health -=6;
			}
			System.out.println("Score" + "\n" + meowthTrophyScore);
			System.out.println("Spartan Health" + "\n" + health);
			System.out.println("Attack Power" + "\n" + attackPower);
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			else if (health <= 0)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You Lost! Your score is" + "\n" + meowthTrophyScore);
			}
			return true;
		}

		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ';')
		{
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			meowthTrophyScore += 1;
			anger+=20;
			attackPower -= 10;
			if (attackPower <= 0)
			{
				health -=9;
			}
			else
			{
			    health -=5;
			}
			System.out.println("Score" + "\n" + meowthTrophyScore);
			System.out.println("Spartan Health" + "\n" + health);
			System.out.println("Attack Power" + "\n" + attackPower);
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			else if (health <= 0)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You Lost! Your score is" + "\n" + meowthTrophyScore);
			}
			return true;
		}

		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '<')
		{
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			meowthTrophyScore += 1;
			anger+=20;
			attackPower -= 8;
			if (attackPower <= 0)
			{
				health -=8;
			}
			else
			{
			    health -=4;
			}
			System.out.println("Score" + "\n" + meowthTrophyScore);
			System.out.println("Spartan Health" + "\n" + health);
			System.out.println("Attack Power" + "\n" + attackPower);
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			else if (health <= 0)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You Lost! Your score is" + "\n" + meowthTrophyScore);
			}
			return true;
		}

		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '=')
		{
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			meowthTrophyScore += 1;
			anger+=20;
			attackPower -= 7;
			if (attackPower <= 0)
			{
				health -=7;
			}
			else
			{
			    health -=3;
			}
			System.out.println("Score" + "\n" + meowthTrophyScore);
			System.out.println("Spartan Health" + "\n" + health);
			System.out.println("Attack Power" + "\n" + attackPower);
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			else if (health <= 0)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You Lost! Your score is" + "\n" + meowthTrophyScore);
			}
			return true;
		}

		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '>')
		{
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			meowthTrophyScore += 1;
			anger+=20;
			attackPower -= 6;
			if (attackPower <= 0)
			{
				health -=6;
			}
			else
			{
			    health -=2;
			}
			System.out.println("Score" + "\n" + meowthTrophyScore);
			System.out.println("Spartan Health" + "\n" + health);
			System.out.println("Attack Power" + "\n" + attackPower);
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			else if (health <= 0)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You Lost! Your score is" + "\n" + meowthTrophyScore);
			}
			return true;
		}

		else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '?')
		{
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			meowthTrophyScore += 1;
			anger+=20;
			attackPower -= 5;
			if (attackPower <= 0)
			{
				health -=5;
			}
			else
			{
			    health --;
			}
			System.out.println("Score" + "\n" + meowthTrophyScore);
			System.out.println("Spartan Health" + "\n" + health);
			System.out.println("Attack Power" + "\n" + attackPower);
			if (meowthTrophyScore >= 50)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You won! See that number above? It's your score!");
			}
			else if (health <= 0)
			{
				JOptionPane.showInputDialog(meowthTrophyScore, "You Lost! Your score is" + "\n" + meowthTrophyScore);
			}
			return true;
		}
		else
		{
			return false; //Can't move
		}
	}

	//The sprites
	private Sprite[] getSprites() throws Exception
	{
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically be created from the images...
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite("Hedge", "resources/hedge.png");
		sprites[1] = new Sprite("Sword", "resources/sword.png");
		sprites[2] = new Sprite("Help", "resources/help.png");
		sprites[3] = new Sprite("Bomb", "resources/bomb.png");
		sprites[4] = new Sprite("Hydrogen Bomb", "resources/h_bomb.png");
		sprites[5] = new Sprite("Spartan Warrior", "resources/spartan_1.png", "resources/spartan_2.png");
		//Our sprite - the end goal (trophy)
		sprites[6] = new Sprite("Meowth Trophy", "resources/MeowthTrophy.png");
		//The Mother of all Bombs (can only get rid of black spider)
		sprites[7] = new Sprite("The MOAB", "resources/MOAB.png");
		sprites[8] = new Sprite("Black Spider", "resources/black_spider_1.png", "resources/black_spider_2.png");
		sprites[9] = new Sprite("Blue Spider", "resources/blue_spider_1.png", "resources/blue_spider_2.png");
		sprites[10] = new Sprite("Brown Spider", "resources/brown_spider_1.png", "resources/brown_spider_2.png");
		sprites[11] = new Sprite("Green Spider", "resources/green_spider_1.png", "resources/green_spider_2.png");
		sprites[12] = new Sprite("Grey Spider", "resources/grey_spider_1.png", "resources/grey_spider_2.png");
		sprites[13] = new Sprite("Orange Spider", "resources/orange_spider_1.png", "resources/orange_spider_2.png");
		sprites[14] = new Sprite("Red Spider", "resources/red_spider_1.png", "resources/red_spider_2.png");
		sprites[15] = new Sprite("Yellow Spider", "resources/yellow_spider_1.png", "resources/yellow_spider_2.png");
		return sprites;
	}

	//This is our Neural Network stuff
    //We use health/sword/bomb/enemies as parameters here
	public void action(double health, double sword, double bomb, double enemies) throws Exception
	{
			double[] params = {health, sword, bomb, enemies};

		NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.HyperbolicTangent, 4, 3, 4);
		BackPropagationTrainer trainer = new BackPropagationTrainer(nn);
		trainer.train(data, expected, 0.6, 10000);

		double[] result = nn.process(params);

		for(double val : result)
		{
			System.out.println(val);
		}

		System.out.println("==>" + (Utils.getMaxIndex(result) +1));

		int output = (Utils.getMaxIndex(result)+1);

		switch(output)
		{
		case 1:
			panic();
			break;
		case 2:
			attack();
			break;
		case 3:
			avoid();
			break;
			default:
				runAway();
	    } //switch

	} //action


	public static void main(String[] args) throws Exception
	{
		double health =Double.parseDouble("1.0");
		double sword =Double.parseDouble("2.0");
		double bomb =Double.parseDouble("2.0");
		double enemies =Double.parseDouble("3.0");

		//THIS IS OUR FUZZY LOGIC STUFF

		FIS fis = FIS.load("./fcl/fuzzy.fcl", true); //Load and parse the FCL
		FunctionBlock fb = fis.getFunctionBlock("Project");
		JFuzzyChart.get().chart(fb); //Display The Linguistic Variables And Terms

		/* This variable can start out at 0
		   as we have it starting at 0 within the fcl file */
		fis.setVariable("weapon", 1);
		/* This variable must start out at 1 rather than 0
		   as we have it starting at 1 within the fcl file */
		fis.setVariable("anger", 1);

		//These if statements deal with the attack level of the weapons
		//in accordance to the rules in the fcl file using fuzzy logic
		if (attackPower==10)
		{
			fis.setVariable("weapon", 5);
		  //Apply a value to a variable
			Variable tip = fb.getVariable("risk");
			JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
			System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}
		else if (attackPower==20)
		{
			fis.setVariable("weapon", 25);
		  //Apply a value to a variable
			Variable tip = fb.getVariable("risk");
			JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
			System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}
		else if (attackPower==30)
		{
			fis.setVariable("weapon", 40);
		  //Apply a value to a variable
			Variable tip = fb.getVariable("risk");
			JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
			System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}
		else if (attackPower==50)
		{
			fis.setVariable("weapon", 55);
		  //Apply a value to a variable
			Variable tip = fb.getVariable("risk");
			JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
			System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}


		//These if statements deal with the anger level of the enemy spiders
		//in accordance to the rules in the fcl file using fuzzy logic
		if ( anger == 0)
		{
		fis.setVariable("anger", 0);
		Variable tip = fb.getVariable("risk");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}

		else if ( anger == 20)
		{
		fis.setVariable("anger", 20);
		Variable tip = fb.getVariable("risk");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}

		else if ( anger == 40)
		{
		fis.setVariable("anger", 40);
		Variable tip = fb.getVariable("risk");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}

		else if ( anger == 60)
		{
		fis.setVariable("anger", 60);
		Variable tip = fb.getVariable("risk");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}

		else if ( anger == 80)
		{
		fis.setVariable("anger", 80);
		Variable tip = fb.getVariable("risk");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}

		else if ( anger == 100)
		{
		fis.setVariable("anger", 100);
		Variable tip = fb.getVariable("risk");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		System.out.println(fis.getVariable("risk").getValue()); //Output end result

		}

		fis.evaluate(); //Execute the fuzzy inference engine

		Variable tip = fb.getVariable("risk");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		System.out.println(fis.getVariable("risk").getValue()); //Output end result

		//Run game, and pass parameters to evaluate our maze
		new GameRunner().action(health, sword, bomb, enemies);
	}
}
