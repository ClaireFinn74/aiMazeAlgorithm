package ie.gmit.sw.traversers;
import ie.gmit.sw.ai.*;

import java.util.*;

public class AStarTraversator implements Traversator
{
	private Node goal;
	private Maze Spidermaze;

	public AStarTraversator(Node goal)
	{
		this.goal = goal;
	}

	public void traverse(Node[][] maze, Node node)
	{
        long time = System.currentTimeMillis();
    	int visitCount = 0;

		PriorityQueue<Node> open = new PriorityQueue<Node>(20, (Node current, Node next)-> (current.getPathCost() + current.getHeuristic(goal)) - (next.getPathCost() + next.getHeuristic(goal)));
		java.util.List<Node> closed = new ArrayList<Node>();

		//get node
		open.offer(node);
		//Initial State
		node.setPathCost(0);

		//This condition guarantees that if a path exists to the goal node we will find it...No Pruning
		while(!open.isEmpty())
		{
	    // Get the lowest f(n) from the queue
			node = open.poll();
			//Add you to closed list
			closed.add(node);
			//Visited
			node.setVisited(true);
			//Visit count up
			visitCount++;

			//Keep pruning
			if (node.isGoalNode())
			{
				//queued nodes(may/may not have been visited before, not the optimal path though)
				System.out.println("open: " + open);
				System.out.println("closed: " + closed);
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount);
				break;
			}

			try
			{ //Simulate processing each expanded node
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			//Process adjacent nodes
			Node[] children = node.children(maze);
			for (int i = 0; i < children.length; i++)
			{
				Node child = children[i];
				//Compute their f(n) value
				int score = node.getPathCost() + 1 + child.getHeuristic(goal);
				int existing = child.getPathCost() + child.getHeuristic(goal);

				//If open queue already contains child or closed or ...
				if ((open.contains(child) || closed.contains(child)) && existing < score)
				{
					continue;
				}
				else
				{
					/* Why is it necessary to remove child from open (priority queue -
					 Priority calculated based on f(n) value) ?
					 Remove this child because the f(n) value used by the priority queue will change
					 because we're changing the path cost. */
					open.remove(child);
					closed.remove(child);
					//Switch parent if on better path (Guarantees Optimality)
					child.setParent(node);
					//Update the path cost to a better value
					child.setPathCost(node.getPathCost() + 1);
					open.add(child);
				}
			}
		}
	}
}
