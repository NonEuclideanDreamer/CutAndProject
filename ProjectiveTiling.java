//********************************************************************
// ProjectiveTiling.java
// by NonEuclideanDreamer
// ***********************************************************************


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

//*******************************************
// Draws the projective Tiling for a given lattice and affine plane in Rn
//******************************************************
public class ProjectiveTiling 
{ 
	
	static DecimalFormat df=new DecimalFormat("0000");
	static int[][] colors= {{0,255,255},{255,0,255},{255,255,128},{0,255,128},{255,128,255},{128,255,0},{0,0,0},{255,128,0},{128,0,0},{0,0,255},{72,154,0}};
	static int width=1080,height=1080,depth=(int) (height/6*Math.sqrt(5)),st=10;
	static double scale=6,range=1,r=width/2/Math.PI;
	static BufferedImage canvas;
	static double[][]zBuffer=new double[width][height];
	static int n=6, k=3,counter=st-1;//n=dimension,k=dim. of projection, counter+1=index of first image 
	 static Rn a,v; 
	 static Polytope zerocell; 
	static  Lattice lattice;
	static  AffineSpace plane;	
	static String name="test";
	static boolean inPicture,chunk=false,cylinder=false;
	 static ArrayList<int[]> archive=new ArrayList<int[]>(),archivev=new ArrayList<int[]>(),//To check whether it has been done already
				tiles=new ArrayList<int[]>();
		static	double[][] base=Matrix.idmatrix(n).entry;//lattice.base;//
	static	int[]start=new int[base.length],loc;//To rethink for other types of lattices!

	public static void main(String[] args)
	{ 
		lattice=new Lattice(base);
	base=lattice.base;
		System.out.print("start");
		a=new Rn(new double[] {1,-1,1,-1,0,1}).times(0.01*Math.PI).add(Rn.diag(n).times(0.5));
		v=new Rn(new double[] {1,1,1,1,1,1,1,1,1,1}).times(1.0/1440);
		Matrix rot=new Matrix(new double[][] {{0,0.5,0,-0.5,0},{0.5,Math.sqrt(2)/2,0.5,Math.sqrt(2)/2,0},{Math.sqrt(2)/2,0.5,-Math.sqrt(2)/2,-0.5,0},{0.5,0,0.5,0,0},{0,0,0,0,1}});

	Random rand=new Random();
	
	
	//for slides between two projections
	//AffineSpace plane1=AffineSpace.icosahedral(a,Matrix.idmatrix(3)).embed(10, 3),plane2=AffineSpace.dodecahedral(a,Matrix.idmatrix(3));
	

	for(double t=0/1440.0;t<1;t+=1.0/1440)
	{

	//for slides between two projections
	//rot=plane1.rotMatrix().linComb(plane2.rotMatrix(),new int[] {j}, new double[] {t});rot.orthonormalize();plane=AffineSpace.subspace(n,k, a).transform(rot,rot.transpone());
	
	
	//zooming out
	//	scale*=1.001;
	//	range=range*0.99+depth*(1-1/scale)*0.01;

	//Video translating affine plane: 
	//	v=v.add(Rn.e(rand.nextInt(n),n).times((rand.nextDouble()-0.5)*.01));
	//a=a.add(v)/*new Rn(new double[] {1,1,1,1,1}).times(t)*/;
		
	//Rotate View
	rot=Matrix.hrotate(t*2*Math.PI,0, 2, 3).times(Matrix.hrotate(0.1, 0, 1, 3));
		
	plane=AffineSpace.bipyramidicE(n-1,a,rot);
		//AffineSpace.dodecahedral(a,rot);
		//AffineSpace.bipyramidicE(n-1,a,rot);
		//AffineSpace.standardE(4, a);
		//AffineSpace.tetrahedral(a,rot);//standardE(n, a);
	

		counter++;zerocell=Polytope.zerocell(lattice); 	tiles.clear();archive.clear();archivev.clear();zBuffer=new double[width][height];
		System.out.println(counter);
				
		for(int i=0;i<base.length;i++)
		{
			start[i]=(int)Math.round(a.get(i));
		}
	
		canvas=new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
		
		//start looking for tiles at staring point
		findTiles(start);	archivev.add(start);
		while(!tiles.isEmpty()) 
		{
		//	System.out.print("set up... ");
			loc=tiles.get(0); 
			Rn d=Rn.zero(n);
			for(int i=0;i<k;i++)d=d.add(Rn.e(loc[n+i], n));
		if(chunk)	inPicture=plane.orientation.times(a.substract(lattice.vertex(loc).add(new Matrix(lattice.base).times(d.times(0.5))))).norm()<scale/3;
			int[]st=loc;
			int q=1;
			if(k==3)q=6;
			for(int i=0;i<q;i++) {
				if(k==3) {
					st=new int[n+2];
					for(int j1=0;j1<n;j1++)st[j1]=loc[j1];
					if(i>2)st[loc[n+i-3]]++;
					int r=0;
					for(int j1=0;j1<3;j1++)if(j1!=i%3) {
						st[n+r]=loc[n+j1];
						r++;
					}
				}
			if(doesntContain(archive,st))
			{
			//	System.out.println("draw...");
			//	print(st);System.out.println();
				drawTile(st);
			//	System.out.println("and archive");
				archive.add(st);
		//		print(canvas,counter);counter++;
			}}
			tiles.remove(0);
			
		}
	//	System.out.println("},");System.out.println();
		print(canvas,counter);//counter++;
		
	}
//	rot=plane1.rotMatrix().linComb(plane2.rotMatrix(),new int[] {j}, new double[] {1});rot.orthonormalize();plane1=AffineSpace.subspace(n,k, a).transform(rot,rot.transpone());

	
	}
	private static boolean doesntContain(ArrayList<int[]>archive,int[] array)
	{
		int n=array.length;
		boolean same; 
		for(int[] x : archive)
		{
			same=true;
			for(int i=0;i<n;i++)
			{
				if(array[i]!=x[i])
					same=false;
			}
			if(same)
				return false;
		}
		return true;
		
	 }
	
	//returns whether the tile is in the picture at all
	public static void drawTile(int[] tile)
	{ 
		int[][]corner=new int[4][n];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<4;j++)
			{
				corner[j][i]=tile[i];
			}
		}
		/*int color=0,cl=0;
		while(color!=tile[n]&&color!=tile[n+1]) {color++;cl+=n-color;}color++;
		while(color!=tile[n]&&color!=tile[n+1]) {color++;cl++;}*/
		//System.out.print("define corners...");
		corner[1][tile[n]]+=1;
		corner[2][tile[n]]+=1;
		corner[2][tile[n+1]]+=1;
		corner[3][tile[n+1]]+=1;
		Rn[] cs=new Rn[3];
		//System.out.print("project...");
		for(int i=0;i<3;i++) //Normal project to E identified as R2--> "to lattice"
			cs[i]=plane.position.times(plane.orientation.times(lattice.vertex(corner[i])));
	
		int[] color=mix(colors[tile[n]],colors[tile[n+1]]);//new Color(colors[color][0],colors[color][1],colors[color][2]).getRGB();//
		//System.out.print("draw..."); 
		if(!chunk)inPicture= draw(cs[0],cs[1],cs[2],color);	
	//	print(tile);
	//	System.out.println(inPicture);
		if(inPicture) {/*System.out.print("find Tiles...");*/for (int i=0;i<4;i++)
			{if(doesntContain(archivev,corner[i])) {findTiles(corner[i]); archivev.add(corner[i]);	/*cs[i].print();*/}}
	//print(tile);	print(corner[0]);print(corner[1]);print(corner[2]);System.out.println();	print(canvas,counter);counter++;
	if(chunk)draw(cs[0],cs[1],cs[2],color);	}
	} 
	
	private static void findTiles(int[] start) 
	{
		int c=tiles.size();
		//Polytope cell=zerocell.shift(lattice.vertex(start));
		int[]index=new int[k];
		for(int a=1;a<k;a++)index[a]=a;
		index[k-1]=k-2;
		while(nextIndex(index)) 
		{
			int i=index[0],j=index[1],h;
				for(int s=0;s<8;s++)	
				{
					int[]star=start.clone();
					for(int b=0;b<n;b++)star[b]=start[b];
					for(int b=0;b<k;b++)if(s/(int)Math.pow(2, b)%2==1)star[index[b]]--;
						Polytope window=lattice.dual2cell(star,i,j,plane);
						if(k==3) 
						{
							h=index[2];
							window=lattice.dual3cell(star,i,j,h,plane);
						}
						
						boolean contains=a.isIn(window);
						if(contains==true)
						{ //System.out.print("in!"); 
								int[] entry=new int[n+k]; 
								for(int k1=0;k1<n;k1++)
								{
									entry[k1]=star[k1];
								}
								for(int k1=0;k1<k;k1++)
								entry[n+k1]=index[k1]; 
								tiles.add(entry);
						//		print(entry);
							//	print(entry);System.out.println();
						}
					//	else print(star);System.out.print(s+", "+index[0]+", "+index[1]+", "+index[2]+", ");window.print();
						
				}
		}
	//	System.out.print( "For loc=");print(start);System.out.println(" we have"+(tiles.size()-c)+" candidates");
	}
	
	private static boolean nextIndex(int[] index) {
		int c=k-1;
		while(c>-1&&index[c]==n-k+c)c--;
		if(c<0)return false;
		
		index[c]++;
		for(int a=c+1;a<k;a++)index[a]=index[c]+a-c;
		return true;
	}
	//Draw the tile with given vertices...
	public static boolean draw(Rn x, Rn b, Rn c, int[] color)
	{
		Rn s=plane.position.times(plane.orientation.times(a)), middle=new Rn(new double[] {width/2,height/2,range});
		if(cylinder) {
			middle.c[0]=0;
			middle.c[2]=r*(scale+1)/scale;
		}
		double[] xc=(x.substract(s).times(width/scale).add(middle)).c,
				 bc=(b.substract(s).times(width/scale).add(middle)).c,
				cc=(c.substract(s).times(width/scale).add(middle)).c;
	
		return drawRhomb(bc,xc,cc,color);
	}
	private static double[] toCylinder(double[] v) {
		double z=Math.sqrt(v[0]*v[0]+v[2]*v[2]),x=Math.atan2(v[2], v[0]);
		return new double[] {(x/Math.PI+1)*width/2,v[1],r+depth-z};
	}
	public static boolean drawRhomb(double[] u, double[]v, double[]w, int[] color)
	{
		boolean out=false,dim=(u.length>2);
	
		int[] u0=Int(u),v0=Int(v), w0=Int(w),x=new int[u.length];//print(u0);print(v0);print(w0);
		double[]y=new double[u.length];
		double vl=Math.abs(v[0]-u[0])+Math.abs(v[1]-u[1]),//2+(int)Math.sqrt(Math.pow(v0[0]-u0[0], 2)+Math.pow(v0[1]-u0[1], 2)),
				wl=Math.abs(w[0]-u[0])+Math.abs(w[1]-u[1]);//=2+(int)Math.sqrt(Math.pow(w0[0]-u0[0], 2)+Math.pow(w0[1]-u0[1], 2));
	if(cylinder) {vl+=Math.abs(v[2]-u[2]);wl+=Math.abs(w[2]-u[2]);vl*=2;wl*=2;}
		int c=new Color(color[0],color[1],color[2]).getRGB();
		for(int i=0;i<vl;i++)
		{
			for(int j=0;j<wl;j++)
			{
				for(int k=0;k<u.length;k++)
				x[k]=(int)(u0[k]+i*(v0[k]-u0[k])/vl+j*(w0[k]-u0[k])/wl);
				
				if(cylinder)
				{
					for(int k=0;k<u.length;k++)
						y[k]=(int)(u0[k]+i*(v0[k]-u0[k])/vl+j*(w0[k]-u0[k])/wl);
						
					x=toInt(toCylinder(y));
				}
				
				if(x[0]>-1 && x[0]<width&&x[1]>-1&&x[1]<height&&(!dim||(x[2]>-1&&x[2]<depth)))//&&(!dim||(x[2]>-depth&&x[2]<zBuffer[x[0]][x[1]]+depth)))
				{
					if(dim){if(x[2]>zBuffer[x[0]][x[1]]) {
						double factor=1.0*x[2]/depth;
						out=true;
						//System.out.println(factor);
						c=new Color((int)(color[0]*factor),(int)(color[1]*factor),(int)(color[2]*factor)).getRGB();
					zBuffer[x[0]][x[1]]=x[2];
					
					canvas.setRGB(x[0],x[1],c);}}
					else
					{
						out=true;
					
						canvas.setRGB(x[0],x[1],c);
					}
						
				}
			}
		}
		return out;
	}
	private static int[] toInt(double[] v) {
		int[] out= new int[v.length];
		for(int i=0;i<v.length;i++) out[i]=(int)v[i];
		return out;
	}
	public static int[] Int(double[] u)
	{
		int[] out=new int[u.length];
		for(int i=0;i<u.length;i++)
		{
			out[i]=(int)u[i];
		}
		return out;
	} 
	//take the r,g,b of two colors, give the rgb of their average
	public static int[] mix(int[] c1, int[]c2)
	{
		Color col1=new Color(c1[0],c1[1],c1[2]),col2=new Color(c2[0],c2[1],c2[2]);
		
		return (new int[] {(col1.getRed()+col2.getRed())/2,(col1.getGreen()+col2.getGreen())/2,(col1.getBlue()+col2.getBlue())/2 });
	}
	public static  void print(BufferedImage canvas, int k)
	{
		File outputfile = new File(name+df.format(k)+".png");
		try 
		{
			ImageIO.write(canvas, "png", outputfile);
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace(); 
		}	
	}
	public static void print(int[] a)
	{
		System.out.print("{"+a[0]);
		for(int i=1;i<a.length;i++)
			System.out.print(","+a[i]);
		
		System.out.print("}, ");
	}
}
