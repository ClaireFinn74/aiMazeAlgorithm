package ie.gmit.sw.ai;

public interface Trainator
{
	public void train(double[][] data, double[][] desired, double alpha, int epochs);
}
