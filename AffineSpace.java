//*****************************************************
// by NonEuclideanDreamer
// An affine d-dim. subspace embedded in Rn
//****************************************************
public class AffineSpace 
{
	public int n,d;
	public Matrix orientation, //Projetion to affine supspace
	position;//Base vectors of the affine subspace
	public Rn a;
	
	public AffineSpace( Matrix or, Matrix pos, Rn loc)
	{
		n=or.n;
		orientation=or;
		a=loc;
		d=pos.m;
		position=pos;
	}
	//Gives the E-plane for projecting the Zn lattice s.t. the base vectors form an n-gon
	public static AffineSpace standardE(int n, Rn a)
	{
		double[][] m=new double[n][n],p=new double[2][n];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				m[i][j]=2*Math.cos(Math.PI*(i-j)/n)/n;
			}
		}
		Matrix or=new Matrix(m);
		p[0]=or.times(Rn.e(0, n)).c;
		if(n%2==0)	p[1]=or.times(Rn.e(n/2, n)).c;
		else		p[1]=or.times((Rn.e(n/2,n).add(Rn.e(n/2+1, n)).times(0.5/Math.cos(Math.PI/(2*n))))).c;
	Matrix pos=new Matrix(p);
	or.print();pos.print();//System.print.println(or.det();)
		return new AffineSpace(or,pos,a);
	}
	
	public static AffineSpace subplane(int n, Rn a)
	{
		double[][]m=new double[n][n],p=new double[2][n];

		m[0][0]=1;m[1][1]=1;
		p[0][0]=1;p[1][1]=1;
		Matrix pos=new Matrix(p); Matrix or=new Matrix(m);
		return new AffineSpace(or,pos,a);
	}
	//embed in a higher dimension
	public AffineSpace embed(int dim)
	{
		Matrix or=Matrix.idmatrix(dim),pos=new Matrix(d,dim);
		Rn anew=Rn.zero(dim);
		for(int i=0;i<n;i++)
		{	
			anew.c[i]=a.get(i);
			for(int j=0;j<n;j++)
				or.entry[i][j]=orientation.entry[i][j];
			
			for(int j=0;j<d;j++)
				pos.entry[j][i]=position.entry[j][i];
		}
		return new AffineSpace(or,pos,anew);
	}
	
	//Rotmatrix needed to get standard subspace to this
	public Matrix rotMatrix()
	{
		Matrix out=Matrix.idmatrix(n);
		for(int i=0;i<d;i++)
			out.setColumn(i, orientation.getColumn(i));
		out.orthonormalize();
		return out;
	}
	
	//Only enter orthogonal matrices!
	public AffineSpace transform(Matrix matrix, Matrix inv)
	{
		Matrix or=matrix.times(orientation.times(inv));
	
		Matrix pos=matrix.times(position.transpone()).transpone();
		return new AffineSpace(or, pos,a);
	}
}
