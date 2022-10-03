//************************************
// Author: NonEuclideanDreamer
// Convex(!) Polytope described by the Hyperplanes bounding it
//**************************************

public class Polytope 
{
	public Hyperplane[] face;//The normal vectors should all point inwards!
	
	public Polytope(Hyperplane[] f)
	{
		face=f;
	}
	
	public Polytope shift(Rn v)
	{
		Hyperplane[] f=new Hyperplane[face.length];
		for(int i=0;i<face.length;i++)
		{
			f[i]=face[i].shift(v);
		}
		return new Polytope(f);
	}
	
	public static Polytope zerocell(Lattice l)
	{
		int n=l.n, m=l.base.length;
		Hyperplane[] faces=new Hyperplane[2*m];
		
		for(int i=0;i<m;i++)
		{
			faces[i]=Rn.zero(n).middlenormal(new Rn(l.base[i]));
			faces[i+m]=Rn.zero(n).middlenormal(new Rn(l.base[i]).times(-1));
		}
		return new Polytope(faces);
	}
}
