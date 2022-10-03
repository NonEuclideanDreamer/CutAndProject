//********************************************************************
// ProjectiveTiling.java
// by NonEuclideanDreamer
// ***********************************************************************


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

//*******************************************
// Draws the projective Tiling for a given lattice and affine plane in Rn
//******************************************************
public class ProjectiveTiling 
{
	static int[][] colors= {{0,0,0},{255,0,0},{255,0,255},{255,255,0},{0,255,255},{128,255,128},{128,255,255},{128,128,255},{255,128,128},{0,85,0},{0,0,85},{170,255,255}};
	static int width=2560,height=1440;
	static double scale=10;// 
	static BufferedImage canvas;
	static int n=3, counter=0;//n=dimension, counter=index of first image 
	 static Rn a; 
	 static Polytope zerocell; 
	static  Lattice lattice;
	static  AffineSpace plane;	
	static String name="Powerup";
	static boolean inPicture;
	 static ArrayList<int[]> archive=new ArrayList<int[]>(),archivev=new ArrayList<int[]>(),//To check whether it has been done already
				tiles=new ArrayList<int[]>();
		static	double[][] base=Matrix.idmatrix(n).entry;
	static	int[]start=new int[base.length],loc;//To rethink for other types of lattices!

	public static void main(String[] args)
	{ 
		System.out.print("start");
		lattice=new Lattice(base);
		a=new Rn(new double[] {1,1,-1/*,-0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5*/}).times(Math.PI-3)/*.add(Rn.e(4, n).times(0.5))*/;
		Matrix rot=new Matrix(new double[][] {{0,0.5,0,-0.5,0},{0.5,Math.sqrt(2)/2,0.5,Math.sqrt(2)/2,0},{Math.sqrt(2)/2,0.5,-Math.sqrt(2)/2,-0.5,0},{0.5,0,0.5,0,0},{0,0,0,0,1}});
		rot.orthonormalize();
	
	AffineSpace plane1=AffineSpace.standardE(n-1, a).embed(n),plane2=AffineSpace.standardE(n, a);
		rot=plane1.rotMatrix()/*.linComb(plane2.rotMatrix(),new int[] {0}, 1)*/;rot.orthonormalize();plane1=AffineSpace.subplane(n, a).transform(rot,rot.transpone());
		rot=plane2.rotMatrix();rot.orthonormalize();plane2=AffineSpace.subplane(n, a).transform(rot, rot.transpone());
		zerocell=Polytope.zerocell(lattice); 
		//Video translating affine plane:
		//for(double t=-0.0;t<1/*Math.sqrt(n)*/;t+=0.01) {a=new Rn(new double[] {1,0,0,1,0,0}).times(t).add(Rn.e(0,n).times(0.5));plane=AffineSpace.standardE(n, a);counter++;tiles.clear();archive.clear();archivev.clear();
		
		//Video rotating the plane
		for(double t=1;t>0;t-=0.005) {System.out.println("t="+t);// /*Rotation along certain plane:*/plane=AffineSpace.standardE(n,a).transform(Matrix.drotate(t,1,0,2,n),Matrix.drotate(-t,1,0,2, n));
			/*Rotating plane1 into plane2*/	 rot=plane1.rotMatrix().linComb(plane2.rotMatrix(),new int[] {0,1}, new double[] {t,t});
				 rot.orthonormalize();
				plane=AffineSpace.subplane(n, a).transform(rot, rot.transpone())//.transform(Matrix.hrotate(t,0,3, n),Matrix.hrotate(-t,0,3,n)).transform(Matrix.hrotate(Math.asin(1/Math.sqrt(3)), 1, 2, n), Matrix.hrotate(-Math.asin(1/Math.sqrt(3)), 1, 2, n)).transform(Matrix.hrotate(Math.PI/4,0,2,n),Matrix.hrotate(-Math.PI/4, 0, 2, n))
				; counter++;tiles.clear();archive.clear();archivev.clear();
		//For single image:
		//{
				
		for(int i=0;i<base.length;i++)
		{
			start[i]=(int)Math.round(a.get(i));
		}
		System.out.print("{");
		canvas=new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
		
		int k=0;

		//start looking for tiles at staring point
		findTiles(start);	archivev.add(start);
		while(!tiles.isEmpty()) 
		{
			//System.out.print("set up... ");
			loc=tiles.get(0);
			if(doesntContain(archive,loc))
			{
				//System.out.println("draw...");
				
				drawTile(loc);
				//System.out.println("and archive");
				archive.add(loc);
				//print(canvas,k);k++;
			}
			tiles.remove(0);
			
		}
		System.out.println("},");System.out.println();
		print(canvas,counter);
	}
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
	{ boolean inPicture;
		int[][]corner=new int[4][n];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<4;j++)
			{
				corner[j][i]=tile[i];
			}
		}
		//System.out.print("define corners...");
		corner[1][tile[n]]+=1;
		corner[2][tile[n]]+=1;
		corner[2][tile[n+1]]+=1;
		corner[3][tile[n+1]]+=1;
		Rn[] cs=new Rn[3];
		//System.out.print("project...");
		for(int i=0;i<3;i++) //Normal project to E identified as R2--> "to lattice"
			cs[i]=plane.position.times(plane.orientation.times(lattice.vertex(corner[i])));
	
		int color=mix(colors[tile[n]],colors[tile[n+1]]);
		//System.out.print("draw..."); 
		inPicture= draw(cs[0],cs[1],cs[2],color);
		if(inPicture) {/*System.out.print("find Tiles...");*/for (int i=0;i<4;i++)
			{findTiles(corner[i]); if(doesntContain(archivev,corner[i])&&i<3) {archivev.add(corner[i]);cs[i].print();}}
		}
	} 
	
	private static void findTiles(int[] start) 
	{
		//Polytope cell=zerocell.shift(lattice.vertex(start));

		for(int i=0;i<n;i++)
				{ 
					for(int j=i+1;j<n;j++) 
					{
						Polytope window=lattice.dual2cell(start,i,j,plane);
						boolean contains=a.isIn(window);
						if(contains==true)
						{ //System.out.print("in!"); 
								int[] entry=new int[n+2]; 
								for(int k=0;k<n;k++)
								{
									entry[k]=start[k];
								}
								entry[n]=i; 
								entry[n+1]=j;
								tiles.add(entry);
							}
						start[i]-=1;
						window=lattice.dual2cell(start,i,j,plane);
						
						if(a.isIn(window))
							{  // System.out.print("in!");
								int[] entry=new int[n+2];
								for(int k=0;k<n;k++)
								{
									entry[k]=start[k];
								}
								entry[n]=i;
								entry[n+1]=j;
								tiles.add(entry);
							}
						start[j]-=1;
						window=lattice.dual2cell(start,i,j,plane);
						
						if(a.isIn(window))
							{//System.out.print("in!");
								int[] entry=new int[n+2];
								for(int k=0;k<n;k++)
								{
									entry[k]=start[k];
								}
								entry[n]=i;
								entry[n+1]=j;
								tiles.add(entry);
							}
						start[i]+=1;
 window=lattice.dual2cell(start,i,j,plane);
						
						if(a.isIn(window))
							{  //System.out.print("in!");
								int[] entry=new int[n+2];
								for(int k=0;k<n;k++)
								{
									entry[k]=start[k];
								}
								entry[n]=i;
								entry[n+1]=j;
								tiles.add(entry);
							}
						start[j]+=1;
					}
				}
	}
	
	//Draw the tile with given vertices...
	public static boolean draw(Rn x, Rn b, Rn c, int color)
	{
		Rn s=plane.position.times(a), middle=new Rn(new double[] {width/2,height/2});
		double[] xc=(x.substract(s).times(width/scale).add(middle)).c,
				 bc=(b.substract(s).times(width/scale).add(middle)).c,
				cc=(c.substract(s).times(width/scale).add(middle)).c;//new Rn(bc).print();
		return drawRhomb(bc,xc,cc,color);
	}
	public static boolean drawRhomb(double[] u, double[]v, double[]w, int color)
	{
		boolean out=false;
		int[] u0=Int(u),v0=Int(v), w0=Int(w);
		int x,y;
		int vl=1+(int)Math.sqrt(Math.pow(v0[0]-u0[0], 2)+Math.pow(v0[1]-u0[1], 2)),
				wl=1+(int)Math.sqrt(Math.pow(w0[0]-u0[0], 2)+Math.pow(w0[1]-u0[1], 2));
		for(int i=0;i<vl;i++)
		{
			for(int j=0;j<wl;j++)
			{
				x=u0[0]+i*(v0[0]-u0[0])/vl+j*(w0[0]-u0[0])/wl;
				y=u0[1]+i*(v0[1]-u0[1])/vl+j*(w0[1]-u0[1])/wl;
				
				if(x>-1 && x<width&&y>-1&&y<height)
				{
					out=true;
					canvas.setRGB(x,y,color);
				}
			}
		}
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
	public static int mix(int[] c1, int[]c2)
	{
		Color col1=new Color(c1[0],c1[1],c1[2]),col2=new Color(c2[0],c2[1],c2[2]);
		
		return (new Color((col1.getRed()+col2.getRed())/2,(col1.getGreen()+col2.getGreen())/2,(col1.getBlue()+col2.getBlue())/2 )).getRGB();
	}
	public static  void print(BufferedImage canvas, int k)
	{
		File outputfile = new File(name+k+".jpg");
		try 
		{
			ImageIO.write(canvas, "jpg", outputfile);
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
