# CutAndProject
Create Patterns with deBrujin's CutAndProject Method

Basically, you are in an n-dimensional vector space containing a lattice. (I only implemented the Standard Zn-lattice so far)
Now we want to project it to a affine subplane, defined by a shifting vector a and the ProjectionMatrix onto the plane.
(I might work on projecting onto 1- and 3- dimensional subspaces in the future).
For this we intersect the CellComplex dual to our lattice with the affine plane ("cut"),
and then orthogonally project those cells of the lattice, whose dual does indeed intersect the affine plane.
This ensures that as long the affine plane is in general position relative to the lattice,
we get a full tesselation of the plane without any holes or overlaps.

This is a Java Project that creates series of images that can be put together to videos.
The Class "ProjectiveMethod.java" is the main class with the main method. In it one can fiddle around with the settings.
In the main method one can comment/uncomment the parts defining a rotation or a shift for a video.
I might try to make it more user friendly in the future, for now consider it a view into the inner workings of my brain.

Feel free to ask questions e.g. on Discord: https://discord.gg/3XNxnGZRVs
