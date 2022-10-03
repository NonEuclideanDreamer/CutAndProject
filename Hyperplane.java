//************************************************************
// Author: NonEuclideanDreamer
// an affine n-dimensional subspace defined by a normal vector and a shifting vector
//**************************************************************


public class Hyperplane 
{
	public Rn normal;
	public Rn loc;
	
	public Hyperplane(Rn n, Rn l)
	{
		normal=n;
		loc=l;
	}
	
	//Shift by vector v
	public Hyperplane shift(Rn v)
	{
		return new Hyperplane(normal, loc.add(v));
	}
	

	
}
