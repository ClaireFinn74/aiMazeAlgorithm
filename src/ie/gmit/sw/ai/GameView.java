package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ie.gmit.sw.ai.Node.Direction;

public class GameView extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 600;
	private int cellspan = 5; //5x5 grid
	private int cellpadding = 2;
	private Maze maze;
	private Node[][] Nodemaze;
	private Sprite[] sprites;
	private int enemy_state = 5; //5 states (blinking etc)
	private Timer timer;
	private int currentRow;
	private int currentCol;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private int offset = 48; //The number 0 is ASCII 48.
	//score for getting a MeowthTrophy


	//different types of red
	private Color[] reds = {new Color(255,160,122), new Color(139,0,0), new Color(255, 0, 0)}; //Animate enemy "dots" to make them easier to see
	//Adding in different colour blues to distinguish our MeowthTrophy when Zoomed out
	private Color[] blues = {new Color(0, 0, 102), new Color(0, 102, 255), new Color(102, 204, 255)}; //Animate MeowthTrophy "dots" in blue to make them easier to see

	public GameView(Maze maze, Node goal) throws Exception
	{
		this.maze = maze; //model
		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}

	public void setCurrentRow(int row)
	{
		if (row < cellpadding)
		{
			currentRow = cellpadding;
		}
		else if (row > (maze.size() - 1) - cellpadding)
		{
			currentRow = (maze.size() - 1) - cellpadding;
		}
		else
		{
			currentRow = row;
		}
	}

	public void setCurrentCol(int col)
	{
		if (col < cellpadding)
		{
			currentCol = cellpadding;
		}
		else if (col > (maze.size() - 1) - cellpadding)
		{
			currentCol = (maze.size() - 1) - cellpadding;
		}
		else
		{
			currentCol = col;
		}
	}

	public void paintComponent(Graphics g)
	{
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        cellspan = zoomOut ? maze.size() : 5; //show whole maze if zoomed out, otherwise 5
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);

        //loop over rows/columns
        for(int row = 0; row < cellspan; row++)
        {
        	for (int col = 0; col < cellspan; col++)
        	{
        		int x1 = col * size;
        		int y1 = row * size;

        		//0=hedge
        		char ch = '0';

        		if (zoomOut)
        		{
        			ch = maze.get(row, col);
        			if (ch >= '5') //sword/hedge
        			{
	        			if (row == currentRow && col == currentCol)
	        			{
	        				g2.setColor(Color.YELLOW); //always yellow when you zoom out(character)
	        			}
	        			//MeowthTrophy
	        			else if(ch == '6')
	        			{
	        				//Shown as blue when zoomed out to distinguish where
	        				//the player can earn points (By getting 5 trophies)
	        				g2.setColor(blues[(int) (Math.random() * 3)]);

	        			}
        				else
	        			{
	        				g2.setColor(reds[(int) (Math.random() * 3)]);
	        			}
        				g2.fillRect(x1, y1, size, size);
        			}
        		}
        		else
        		{
        			ch = maze.get(currentRow - cellpadding + row, currentCol - cellpadding + col);
        		}

        		imageIndex = (int) ch; //image character is at
        		imageIndex -= offset;
        		if (imageIndex < 0) //paint character
        		{
        			g2.setColor(Color.LIGHT_GRAY);//Empty cell
        			g2.fillRect(x1, y1, size, size);
        		}
        		else
        		{
        			g2.drawImage(sprites[imageIndex].getNext(), x1, y1, null);
        		}
        	}
        }
	}

	public void toggleZoom()
	{
		zoomOut = !zoomOut;
	}

	public void actionPerformed(ActionEvent e) //might be doing nothing
	{
		if(e.getSource()==timer){
			 repaint();
		}

		if (enemy_state < 0 || enemy_state == 5)
		{
			enemy_state = 6;
		}
		else
		{
			enemy_state = 5;
		}
		this.repaint();
	}

	public void setSprites(Sprite[] sprites)
	{
		this.sprites = sprites;
	}
}