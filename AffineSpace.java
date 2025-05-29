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
		//	p[2]=Rn.e(3,4).c;
		Matrix pos=new Matrix(p);
		//or.print();pos.print();//System.print.println(or.det();)
		return new AffineSpace(or,pos,a);
	}
	
	public static AffineSpace bipyramidicE(int n, Rn a,Matrix rot)
	{
		double[][] m=new double[n+1][n+1],p=new double[3][n+1];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				m[i][j]=2*Math.cos(Math.PI*(i-j)/n)/n;
				/*m[n][n]+=m[i][j];
				if(i!=j)m[n][n]+=m[i][j];*/
			}
		}
		m[n][n]=1;
		Matrix or=new Matrix(m);
		p[0]=or.times(Rn.e(0, n+1)).c;
		if(n%2==0)	p[1]=or.times(Rn.e(n/2, n+1)).c;
		else		p[1]=or.times((Rn.e(n/2,n+1).add(Rn.e(n/2+1, n+1)).times(0.5/Math.cos(Math.PI/(2*n))))).c;
		p[2]=Rn.e(n,n+1).c;
		Matrix pos=new Matrix(p);
		pos=rot.times(pos);
		//or.print();pos.print();//System.print.println(or.det();)
		return new AffineSpace(or,pos,a);
	}
	
	public static AffineSpace prismicE(int n,Rn a,Matrix rot)//only for 4 implemendted not what I wantx 
	{
		Matrix[]u=new Matrix[n/2+1];
		for(int i=0;i<u.length;i++)
		{
			u[i]=new Matrix(n,n);
			for(int j=0;j<n;j++)
			{
				u[i].entry[j][(j+i)%n]+=1;
				u[i].entry[j][(j-i+n)%n]+=1;
			}
		}
		double[]factor= {0.75/2,0.25,-0.25};
		if(n==5)factor=new double[] {0.3,.1-1/Math.sqrt(20),.1+1/Math.sqrt(20)};
			Matrix 		or=u[0].times(factor[0]);
			for(int i=1;i<u.length;i++)or=or.add(u[i].times(factor[i]));
			
		double[][]p=new double[3][n];
		p[0]=or.times(Rn.e(0,n)).normalize().c;
		p[1]=or.times(Rn.e(1,n).substract(Rn.e(n-1, n))).normalize().c;
		p[2]=or.times(Rn.diag(n)).normalize().c;
		Matrix pos=new Matrix(p);
		pos=rot.times(pos);
		//or.print();pos.print();//System.print.println(or.det();)
			return new AffineSpace(or,pos,a);
	}
	
	public static AffineSpace standardE(Lattice l, Rn a)
	{
		int n=l.n;
		Matrix lat=new Matrix(l.base),inv=new Matrix(l.inv);
		double[][] m=new double[n][n],p=new double[2][n];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				m[i][j]=2*Math.cos(Math.PI*(i-j)/n)/n;
			}
		}
		Matrix or=lat.times(new Matrix(m)).times(inv);
		p[0]=or.times(Rn.e(0, n)).c;
		if(n%2==0)	p[1]=or.times(Rn.e(n/2, n)).c;
		else		p[1]=or.times((Rn.e(n/2,n).add(Rn.e(n/2+1, n)).times(0.5/Math.cos(Math.PI/(2*n))))).c;
	Matrix pos=new Matrix(p);
	//or.print();pos.print();//System.print.println(or.det();)
		return new AffineSpace(or,pos,a);
	}
	public static AffineSpace tetrahedral(Rn a, Matrix rot)
	{
		Matrix or=new Matrix(new double[][] {{3,-1,-1,-1},{-1,3,-1,-1},{-1,-1,3,-1},{-1,-1,-1,3}}).times(0.25);
		double[][]p=new double[3][4];
		p[0]=or.times(Rn.e(0, 4)).c;
		p[1]=or.times(new Rn(new double[] {1,3,0,0}).times(0.25*Math.sqrt(2))).c;
		p[2]=or.times(new Rn(new double[] {0,0,1,-1}).times(0.5*Math.sqrt(1.5))).c;
		Matrix po=(new Matrix(p));
		po=rot.times(po);
		
	//	System.out.println(new Rn(po.entry[2]).dot(new Rn(po.entry[1])));
	//	
		return new AffineSpace(or, po,a);
	}
	
	public static AffineSpace icosahedral(Rn a, Matrix rot)
	{
		Matrix u=new Matrix(new double[][] {{0,1,1,1,1,1},{1,0,1,-1,-1,1},{1,1,0,1,-1,-1},{1,-1,1,0,1,-1},{1,-1,-1,1,0,1},{1,1,-1,-1,1,0}}),or=Matrix.idmatrix(6).times(0.5).add(u.times(Math.sqrt(0.05)));

		double[][]p=new double[3][6];
		p[0]=or.times(new Rn(new double[] {1,1,0,0,0,0}).times(.6)).c;
		p[1]=or.times(new Rn(new double[] {0,0,-1,0,0,1,}).times(.6)).c;
		p[2]=or.times(new Rn(new double[] {0,0,0,1,1,0}).times(.6)).c;
		Matrix po=(new Matrix(p));
		po=rot.times(po);
		
	//	System.out.println(new Rn(po.entry[2]).dot(new Rn(po.entry[1])));
	//	
		return new AffineSpace(or, po,a);
	}
	public static AffineSpace danzer(Rn a, Matrix rot)
	{
		Matrix u=new Matrix(new double[][] {{1,3,-1,-1,3,1},{3,1,3,-1,-1,1},{-1,3,1,-1,-1,1},{-1,3,1,3,-1,1},{3,-1,-1,3,1,1},{-3,-3,-3,-3,-3,-5}}).times(.5),or=Matrix.idmatrix(6).times(0.5).add(u.times(Math.sqrt(0.05)));

		double[][]p=new double[3][6];
		p[0]=or.times(new Rn(new double[] {1,1,1,1,1,1}).times(.5)).c;
		p[1]=or.times(new Rn(new double[] {1,0,-1,0,0,0,}).times(.6)).c;
		p[2]=or.times(new Rn(new double[] {0,0,0,1,1,0}).times(.6)).c;
		Matrix po=(new Matrix(p));
		po=rot.times(po);
		
	//	System.out.println(new Rn(po.entry[2]).dot(new Rn(po.entry[1])));
	//	
		return new AffineSpace(or, po,a);
	}
	
	public static AffineSpace dodecahedral(Rn a, Matrix rot)
	{
		
		Matrix u=new Matrix(new double[][] {{0,1,1,1,0,0,0,0,0,0},{1,0,0,0,1,0,0,0,0,1},{1,0,0,0,0,1,1,0,0,0},{1,0,0,0,0,0,0,1,1,0},{0,1,0,0,0,1,0,-1,0,0},
			{0,0,1,0,1,0,0,0,-1,0},{0,0,1,0,0,0,0,1,0,-1},{0,0,0,1,-1,0,1,0,0,0},{0,0,0,1,0,-1,0,0,0,1},{0,1,0,0,0,0,-1,0,1,0}}),
			v=new Matrix(new double[][] {{0,0,0,0,1,1,1,1,1,1},{0,0,1,1,0,1,-1,-1,1,0},{0,1,0,1,1,0,0,1,-1,-1},{0,1,1,0,-1,-1,1,0,0,1},{1,0,1,-1,0,0,-1,0,-1,1},
				{1,1,0,-1,0,0,1,-1,0,-1},{1,-1,0,1,-1,1,0,0,-1,0},{1,-1,1,0,0,-1,0,0,1,-1},{1,1,-1,0,-1,0,-1,1,0,0},{1,0,-1,1,1,-1,0,-1,0,0}}),or=Matrix.idmatrix(10).times(0.3).add(u.times(Math.sqrt(0.05))).add(v.times(0.1));
	
		double[][]p=new double[3][10];//todo
		p[0]=or.times(new Rn(new double[] {1,1,0,0,0,0,0,0,0,0}).times(.6)).c;
		p[1]=or.times(new Rn(new double[] {0,0,0,0,0,0,1,1,0,0}).times(.6)).c;
		p[2]=or.times(new Rn(new double[] {0,0,0,0,0,-1,0,0,1,0}).times(.6)).c;
		Matrix po=(new Matrix(p));
		po=rot.times(po);
		
	//	System.out.println(new Rn(po.entry[2]).dot(new Rn(po.entry[1])));
	//	
		return new AffineSpace(or, po,a);
	}
	
	public static AffineSpace subplane(int n, Rn a)
	{
		double[][]m=new double[n][n],p=new double[2][n];

		m[0][0]=1;m[1][1]=1;
		p[0][0]=1;p[1][1]=1;
		Matrix pos=new Matrix(p); Matrix or=new Matrix(m);
		return new AffineSpace(or,pos,a);
	}
	public static AffineSpace subspace(int n, int k,Rn a)
	{
		double[][]m=new double[n][n],p=new double[k][n];
		for(int i=0;i<k;i++)
		{m[i][i]=1;
		p[i][i]=1;}
		Matrix pos=new Matrix(p); Matrix or=new Matrix(m);
		return new AffineSpace(or,pos,a);
	}
	//embed in a higher dimension
	public AffineSpace embed(int dim,int k)
	{
		Matrix or=Matrix.idmatrix(dim),
				pos=new Matrix(k,dim);
		Rn anew=Rn.zero(dim);
		for(int i=0;i<n;i++)
		{	
			anew.c[i]=a.get(i);
			for(int j=0;j<n;j++)
				or.entry[i][j]=orientation.entry[i][j];
			
			for(int j=0;j<d;j++)
				pos.entry[j][i]=position.entry[j][i];
		
		}	
		if(d<k) {Rn[]nullspace=or.nullspace();System.out.print("nullspace:");nullspace[0].print();
		for(int j=d;j<k;j++)
			{
				pos.entry[j]=nullspace[j-d].normalize().c;
			}
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
	public static AffineSpace fullSpace(int n2,Matrix rot,Rn a) {
		
		return new AffineSpace(Matrix.idmatrix(n2),rot,a);
	}
	
	public AffineSpace dualSpace()//pos not done
	{
		Matrix or=Matrix.idmatrix(n).subtract(orientation);
		double[][]	p=new  double[n-d][n];
		for(int i=0;i<n-d;i++)
		{
			//todo
		}
		return new AffineSpace(or,new Matrix(p),a);
	}
}
