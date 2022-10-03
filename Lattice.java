//********************************************************************************
// by NonEuclideanDreamer
// A lattice living in Rn for a given dimension n
//**********************************************************************************
public class Lattice 
{
double[][]base;
int n;

public Lattice(double[]l1,double[]l2)
{
	base=new double[2][l1.length];
	n=l1.length;

	for(int j=0;j<l1.length;j++)
	{
		base[1][j]=l1[j];
		base[2][j]=l2[j];
	}
}

public Lattice(double[][]b)
{
	base=b.clone();
	n=base[0].length;
}
public Polytope dual2cell(int[]loc,int i, int j, AffineSpace E)
{
	int n=E.n;
	Hyperplane[] out =new Hyperplane[(n-2)*2];//The boundaries of the (n-2)cell we are looking for times E.
	int k=0;
	for(int l=0;l<n;l++)
	{
		if((l!=i)&&(l!=j))
		{
		Rn normal=Rn.e(i, n).add(Rn.e(j, n).add(Rn.e(l, n)));
		Rn pos=vertex(loc).add(normal.times(0.5));
		normal=E.orientation.add(Matrix.idmatrix(n).subtract(Matrix.project(n, i, j, l))).nullspace()[0];
		double sgn=Math.signum(normal.dot(Rn.e(l, n)));
		out[k]=new Hyperplane(normal.times(-sgn),pos);//System.out.println("Hyperplane"+k+": ");out[k].loc.print();out[k].normal.print();
		out[n-2+k]=new Hyperplane(normal.times(sgn),pos.add(Rn.e(l, n).times(-1)));//System.out.println("Hyperplane"+(n-2+k)+": ");out[n-2+k].loc.print();out[n-2+k].normal.print();
		k++;}
	}
	return new Polytope( out);
}

//returns a specific lattice point
public Rn vertex(int[]loc)
{	
	double[] out=new double[n];
	for(int i=0;i<loc.length;i++)
	{	
		for(int j=0;j<n;j++) 
		out[j]+=loc[i]*base[i][j];
	}
	return new Rn(out);
}
}
