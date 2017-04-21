package ie.gmit.sw.ai;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.MazeAlgoGenerator.GeneratorAlgorithm;

public class MazeAlgoGeneratorFactory
{
private static MazeAlgoGeneratorFactory mgf = new MazeAlgoGeneratorFactory();

	private MazeAlgoGeneratorFactory()
	{

	}

	public static MazeAlgoGeneratorFactory getInstance()
	{
		return mgf;
	}

	public MazeAlgoGenerator getMazeGenerator(MazeAlgoGenerator.GeneratorAlgorithm algorithm, int rows, int cols)
	{
		if (algorithm == GeneratorAlgorithm.BinaryTree)
		{
			return new BinaryTreeMazeGenerator(rows, cols);
		}
		else if (algorithm == GeneratorAlgorithm.RecursiveBacktracker)
		{
			return new RecursiveBacktrackerMazeGenerator(rows, cols);
		}
		else if (algorithm == GeneratorAlgorithm.HuntAndKill)
		{
			return new HuntAndKillMazeGenerator(rows, cols);
		}
		else
		{
			return new RandomDepthFirstMazeGenerator(rows, cols);
		}

	}
}
