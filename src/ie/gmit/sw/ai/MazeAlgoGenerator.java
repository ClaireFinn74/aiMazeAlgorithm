package ie.gmit.sw.ai;

public interface MazeAlgoGenerator
{
public enum GeneratorAlgorithm {BinaryTree, HuntAndKill, RandomDepthFirst, RecursiveBacktracker, RecursiveDivision, RandomizedPrim, RandomizedKruskal};

	public abstract void setGoalNode();
	public abstract Node getGoalNode();
	public abstract Node[][] getMaze();
	public abstract void generateMaze();
}
