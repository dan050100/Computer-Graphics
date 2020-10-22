package Designer;

import org.lwjgl.opengl.GL11;

import GraphicsLab.Colour;
import GraphicsLab.Normal;
import GraphicsLab.Vertex;

/**
 * The shape designer is a utility class which assits you with the design of 
 * a new 3D object. Replace the content of the drawUnitShape() method with
 * your own code to creates vertices and draw the faces of your object.
 * 
 * You can use the following keys to change the view:
 *   - TAB		switch between vertex, wireframe and full polygon modes
 *   - UP		move the shape away from the viewer
 *   - DOWN     move the shape closer to the viewer
 *   - X        rotate the camera around the x-axis (clockwise)
 *   - Y or C   rotate the camera around the y-axis (clockwise)
 *   - Z        rotate the camera around the z-axis (clockwise)
 *   - SHIFT    keep pressed when rotating to spin anti-clockwise
 *   - A 		Toggle colour (only if using submitNextColour() to specify colour)
 *   - SPACE	reset the view to its initial settings
 *  
 * @author Remi Barillec
 *
 */
public class ShapeDesigner extends AbstractDesigner {
	
	/** Main method **/
	public static void main(String args[])
    {   
		new ShapeDesigner().run( WINDOWED, "Designer", 0.03f);
    }
	
	/** Draw the shape **/
	 protected void drawUnitShape()
	    {
		 Vertex v1 = new Vertex(-0.5f,-0.5f,-0.5f);
	        Vertex v2 = new Vertex(-0.9f, 0.1f, 0.0f);
	        Vertex v3 = new Vertex(-0.5f,-0.5f, 0.5f);
	        Vertex v4 = new Vertex( 0.5f,-0.5f,-0.5f);
	        Vertex v5 = new Vertex( 0.6f, 0.5f, 0.0f);
	        Vertex v6 = new Vertex( 0.5f,-0.5f, 0.5f);
	        Vertex v7 = new Vertex( 0f, 0.1f, 0f);
	        Vertex v8 = new Vertex( -0.2f,0.6f, 0f);
	        Vertex v9 = new Vertex( -0.7f, 0.8f, 0f);
	        Vertex v10 = new Vertex( 0.2f, 0.4f, 0f);
         
         
	         
	         // left gable
	         GL11.glBegin(GL11.GL_TRIANGLES);
	         {
	             new Normal(v3.toVector(),v2.toVector(),v1.toVector()).submit();
	             
	             v3.submit();
	             v2.submit();
	             v1.submit();
	         }
	         GL11.glEnd();
	         
	         // back slope
	         GL11.glBegin(GL11.GL_POLYGON);
	         {
	             new Normal(v1.toVector(),v2.toVector(),v5.toVector(),v4.toVector()).submit();
	             
	             v1.submit();
	             v2.submit();
	             v9.submit();
	             v8.submit();
	             v7.submit();
	             v10.submit();
	             v5.submit();
	             v4.submit();
	         }
	         GL11.glEnd();
	         
	         // front slope
	         GL11.glBegin(GL11.GL_POLYGON);
	         {
	             new Normal(v5.toVector(),v2.toVector(),v3.toVector(),v6.toVector()).submit();
	             
	             v5.submit();
	             v10.submit();
	             v7.submit();
	             v8.submit();
	             v9.submit();
	             v2.submit();
	             v3.submit();
	             v6.submit();
	         }
	         GL11.glEnd();
	         
	         // right gable
	         GL11.glBegin(GL11.GL_TRIANGLES);
	         {
	             new Normal(v5.toVector(),v6.toVector(),v4.toVector()).submit();
	             
	             v5.submit();
	             v6.submit();
	             v4.submit();
	         }
	         GL11.glEnd();   
	    }       
		
}
