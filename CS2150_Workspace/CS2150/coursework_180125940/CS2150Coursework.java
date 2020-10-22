/* CS2150Coursework.java
 * 180125940
 * Daniel Taylor
 * BSc Computer Science
 * Year 2 Student
 * This is my own work
 *
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *  +-- [T(seaX, currentSeaLevelY, -3.0)  R(15.0, 0.0, 1.0, 0.0)  S(25.0, 1.0, 20.0)] Water Plane
 *  |
 *  +-- [T(-7.5, currentIceLevelY, -7.0)  R(15.0, 0.0, 1.0, 0.0)  S(15.0, 1.0, 15.0)] Snow Plane
 *  |
 *  +-- [T(-1.05, 4.4, -12.0)  R(90.0, 1.0, 0.0, 0.0)  S(16.0, 1.0, 11.0)] Mountain Plane
 *  |
 *  +-- [T(floating, up, -10.0)  R(15, 0.0, 1.0, 0.0)  S(ice, ice, ice)] First Iceberg
 *  |   |
 *  |   +--[T(floating2,up2, 4.0f)] Second Iceberg
 *  |
 *  +-- [T(-1, shrubY, -8.0)  R(0, 0.0, 1.0, 0.0)  S(0.65, 0.5, 0.5)] Shrub
 *  |
 *  +-- [T(-2.5, currentHouseY, -10.0)  S(2.0, 2.0, 2.0)  R(70.0, 0.0, 1.0, 0.0)] House's Base
 *  |   |
 *  |   +-- [T(0.0, 0.75, 0.0)  S(1.0, 0.5, 1.0)] House's Roof
 *	|       |
 *	|       +-- [T(-0.01, -1.3, 0.0)  S(1, 2.0, 1)] House's Door
 *  |           | 
 *	|           +-- [T(0, 0.1, 0.51)  S(0.5, 0.25, 0.05)] House's Window
 *	|
 *	+-- [T(0.1, -0.6, -4.2)  S(0.05, 0.7, 0.05)  R(70, 0.0, 1.0, 0.0)] Wooden Post Base
 *	    |
 *      +-- [T(3f, 0.4f, 0.3f)  S(20f, 0.2f, 0.5f)] Wooden Post Sign
 *      	|
 *      	+--[T(-0.09f, 0f, 1.9f)  S(0.75f, 0.5f, 0.8f)] Thermometer
 *      	          
 */
package coursework_180125940;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * <p>
 * Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis,
 * respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down
 * cursor keys to increase or decrease the viewpoint's distance from the scene
 * origin
 * <li>Press the g or d key to manually view the sea level animation take place.
 * <li>Press the space bar to reset all animations back to the default.
 * <li>Press the r key to increase the temperature of the environment.
 * <li>Press the e key to decrease the temperature of the environment.
 * <li>Press the p or o key to control the water and icebergs from moving.
 * </ul>
 */
public class CS2150Coursework extends GraphicsLab {
	/** display list id for the house */
	private final int houseList = 1;
	/** display list id for the roof */
	private final int roofList = 2;
	/** display list id for the unit plane */
	private final int planeList = 3;
	/** display list id for the icebergs */
	private final int icebergList = 4;
	/** display list id for the doors */
	private final int doorList = 5;
	/** display list id for the thermometer */
	private final int thermometerList = 6;
	/** display list id for the wooden post */
	private final int woodenPostsList = 7;
	/** display list id for the window */
	private final int windowList = 7;

	/** The colour of the temperature on the thermometer */
	private float temperatureRed = 0.2f;
	private float temperatureBlue = 0.8f;

	/**
	 * The conditions to check whether the temperature has reached its maximum
	 * values and if the temperature should change or not
	 */
	private boolean tempStop = false;
	private boolean tempChange = false;

	/** The current scaling of the icebergs */
	private float ice = 1f;

	/** The current X axis offset of the icebergs */
	private float floating = 2.5f;
	private float floating2 = -0.4f;

	/** The condition for if the icebergs move */
	private boolean canFloat = true;

	/** The condition for whether or not the icebergs are moving */
	private boolean forwards = true;

	/** The current Y axis offset of the icebergs */
	private float up = -0.5f;
	private float up2 = -0.4f;

	/** The condition for if the icebergs are moving downwards */
	private boolean downwards = true;

	/** The current Y axis value of the house */
	private float currentHouseY = 0.0f;

	/** The current Y axis value of the shrub */
	private float shrubY = -0.5f;

	/** id for mipmapped texture of the snow ground plane */
	private Texture snowGroundTextures;
	/** id for mipmapped texture of the background plane */
	private Texture waterGroundTextures;
	/** id for mipmapped texture of the water plane */
	private Texture mountainTextures;
	/** id for mipmapped texture of the house */
	private Texture woodTextures;
	/** id for mipmapped texture of the houses wooden door */
	private Texture woodenDoorTextures;
	/** id for mipmapped texture of the houses roof */
	private Texture snowRoofTextures;
	/** id for mipmapped texture of the shrub */
	private Texture shrubTextures;
	/** id for mipmapped texture of the icebergs */
	private Texture icebergTextures;
	/** id for mipmapped texture of the wooden post */
	private Texture postTextures;
	/** id for mipmapped texture of the window */
	private Texture windowTextures;

	/**
	 * the sea level's conditions which include if the level is rising or not as
	 * well as the highest and lowest point of rising. As well as the X axis offset
	 * of the plane from the scene origin
	 */
	private boolean risingSeaLevel;
	private float currentSeaLevelY = -1.0f;
	private float currentIceLevelY = -0.7f;
	private float seaX = 2.5f;

	public static void main(String args[]) {
		new CS2150Coursework().run(WINDOWED, "CS2150 Coursework Submission", 0.01f);
	}

	protected void initScene() throws Exception {
		// loads all of the scenes textures
		waterGroundTextures = loadTexture("coursework_180125940/textures/waterTexture.bmp");
		snowGroundTextures = loadTexture("coursework_180125940/textures/snowTexture.bmp");
		mountainTextures = loadTexture("coursework_180125940/textures/mountainTexture.bmp");
		woodTextures = loadTexture("coursework_180125940/textures/woodTexture.bmp");
		snowRoofTextures = loadTexture("coursework_180125940/textures/snowRoofTexture.bmp");
		shrubTextures = loadTexture("coursework_180125940/textures/shrub.bmp");
		woodenDoorTextures = loadTexture("coursework_180125940/textures/woodenDoorTexture.bmp");
		icebergTextures = loadTexture("coursework_180125940/textures/icebergTexture.bmp");
		postTextures = loadTexture("coursework_180125940/textures/woodenPosts.bmp");
		windowTextures = loadTexture("coursework_180125940/textures/windowTexture.bmp");

		// global ambient light level
		float globalAmbient[] = { 0.8f, 0.8f, 0.8f, 1.0f };
		// set the global ambient lighting
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbient));

		// the first light for the scene is white...
		float diffuse0[] = { 0.6f, 0.6f, 0.6f, 1.0f };
		// ...with a very dim ambient contribution...
		float ambient0[] = { 0.1f, 0.1f, 0.1f, 1.0f };
		// ...and is positioned above the viewpoint
		float position0[] = { 10f, 10f, 0f, 1.0f };

		// supply OpenGL with the properties for the first light
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
		// enable the first light
		GL11.glEnable(GL11.GL_LIGHT0);

		// enable lighting calculations
		GL11.glEnable(GL11.GL_LIGHTING);
		// ensure that all normals are re-normalised after transformations automatically
		GL11.glEnable(GL11.GL_NORMALIZE);

		// prepare the display lists for later use
		GL11.glNewList(houseList, GL11.GL_COMPILE);
		{
			drawUnitCube();
		}
		GL11.glEndList();
		GL11.glNewList(roofList, GL11.GL_COMPILE);
		{
			drawUnitRoof();
		}
		GL11.glEndList();
		GL11.glNewList(planeList, GL11.GL_COMPILE);
		{
			drawSnowPlane();
		}
		GL11.glEndList();
		GL11.glNewList(planeList, GL11.GL_COMPILE);
		{
			drawWaterPlane();
		}
		GL11.glEndList();
		GL11.glNewList(icebergList, GL11.GL_COMPILE);
		{
			drawUnitIceberg();
		}
		GL11.glEndList();
		GL11.glNewList(doorList, GL11.GL_COMPILE);
		{
			drawUnitDoor();
		}
		GL11.glNewList(windowList, GL11.GL_COMPILE);
		{
			drawUnitWindow();
		}
		GL11.glEndList();
		GL11.glNewList(thermometerList, GL11.GL_COMPILE);
		{
			drawUnitCube();
		}
		GL11.glEndList();
		GL11.glNewList(woodenPostsList, GL11.GL_COMPILE);
		{
			drawUnitCube();
		}
		GL11.glEndList();
	}

	protected void checkSceneInput() {
		// checks which animation is to take place depending
		// on the key which is clicked

		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			tempChange = true;
			tempStop = false;
			if (temperatureRed >= 0.8f) // Prevent the value of red being out of bounds.
			{
				temperatureRed = 0.8f;
				tempStop = true;
			}
			if (temperatureBlue <= 0.3f) // Prevent the value of blue being out of bounds.
			{
				temperatureBlue = 0.3f;
				tempStop = true;
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			tempStop = false;
			tempChange = false;
			if (temperatureRed <= 0.2f) // Prevent the value of red being out of bounds.
			{
				temperatureRed = 0.2f;
				tempStop = true;
			}
			if (temperatureBlue >= 0.8f) // Prevent the value of blue being out of bounds.
			{
				temperatureBlue = 0.8f;
				tempStop = true;
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			risingSeaLevel = true;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			risingSeaLevel = false;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			resetAnimations();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			canFloat = false;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
			canFloat = true;
		}
	}

	private void resetAnimations() {

		// resets all animations back to their original default states
		currentSeaLevelY = -1;
		risingSeaLevel = false;
		currentIceLevelY = -0.7f;
		ice = 1f;
		floating = 2.5f;
		floating2 = -0.3f;
		up = -0.5f;
		up2 = -0.4f;
		forwards = true;
		currentHouseY = 0.0f;
		shrubY = -0.5f;
		temperatureBlue = 0.8f;
		temperatureRed = 0.2f;
		tempStop = false;
		tempChange = false;
	}

	protected void updateScene() {
		// checks if the icebergs should be moving in the x axis
		// as well as if they have reached their boundaries,
		// causing them to move in the opposite direction
		if (canFloat && forwards) {
			floating += 0.02f * getAnimationScale();
			floating2 += 0.01f * getAnimationScale();
			seaX += 0.03f * getAnimationScale();
			if (floating >= 3f) {
				forwards = false;
			}
		} else if (canFloat && !forwards) {
			floating -= 0.02f * getAnimationScale();
			floating2 -= 0.01f * getAnimationScale();
			seaX -= 0.03f * getAnimationScale();
			if (floating <= 2f) {
				forwards = true;
			}
		}

		// checks if the icebergs should be moving in the y axis
		// as well as if they have reached their boundaries,
		// causing them to move in the opposite direction
		if (canFloat && downwards) {
			up -= 0.02f * getAnimationScale();
			up2 += 0.01f * getAnimationScale();
			if (up <= -0.6f) {
				downwards = false;
			}
		} else if (canFloat && !downwards) {
			up += 0.02f * getAnimationScale();
			up2 -= 0.01f * getAnimationScale();
			if (up >= -0.5f) {
				downwards = true;
			}
		}

		// checks if the sea level is rising
		// and if it is the shrub, land and the house all move downwards
		// as well as the icebergs decreasing in size
		// and vice-versa if the sea level is going down
		if (risingSeaLevel && (currentSeaLevelY < -0.65)) {
			currentSeaLevelY += 0.015f * getAnimationScale();
			if (currentIceLevelY > -0.8) {
				currentIceLevelY -= 0.016f * getAnimationScale();
				ice -= 0.02f * getAnimationScale();
				currentHouseY -= 0.01f * getAnimationScale();
				shrubY -= 0.01f * getAnimationScale();
			}
		} else if (!risingSeaLevel && (currentSeaLevelY > -1)) {
			currentSeaLevelY -= 0.015f * getAnimationScale();
			if (currentIceLevelY < -0.7) {
				currentIceLevelY += 0.016f * getAnimationScale();
				ice += 0.02f * getAnimationScale();
				currentHouseY += 0.01f * getAnimationScale();
				shrubY += 0.01f * getAnimationScale();
			}
		}

		// checks if the temperature is changing
		// and if it is rising, the sea level begins to rise
		// and the colour of the thermometer changed from blue to red
		// and vice-versa if the temperature changing variable is false
		if (tempChange && !tempStop) {
			temperatureRed += 0.025f * getAnimationScale(); // Make the thermometer gradually turn red
			temperatureBlue -= 0.035f * getAnimationScale(); // Reduce the blue value of the thermometer
			risingSeaLevel = true;

			if (temperatureRed >= 0.8f) // Prevent the value of red being out of bounds.
			{
				temperatureRed = 0.8f;
				tempStop = true;
			}
			if (temperatureBlue <= 0.3f) // Prevent the value of blue being out of bounds.
			{
				temperatureBlue = 0.3f;
				tempStop = true;
			}
		}

		if (!tempChange && !tempStop) {
			temperatureRed -= 0.025f * getAnimationScale(); // Make the thermometer gradually turn less red
			temperatureBlue += 0.035f * getAnimationScale(); // Increase the blue value of the thermometer
			risingSeaLevel = false;

			if (temperatureRed <= 0.2f) // Prevent the value of red being out of bounds.
			{
				temperatureRed = 0.2f;
				tempStop = true;
			}
			if (temperatureBlue >= 0.8f) // Prevent the value of blue being out of bounds.
			{
				temperatureBlue = 0.8f;
				tempStop = true;
			}
		}
	}

	protected void renderScene() {
		// draw the ground plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterGroundTextures.getTextureID());

			// position, scale and draw the ground plane using its display list
			GL11.glTranslatef(seaX, currentSeaLevelY, -3.0f);
			GL11.glRotatef(15f, 0.0f, 1.0f, 0.0f);
			GL11.glScalef(25.0f, 1.0f, 20.0f);
			GL11.glCallList(planeList);

			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// Draws the snow plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, snowGroundTextures.getTextureID());

			// position, scale and draw the ground ice plane using its display list
			GL11.glTranslatef(-7.5f, currentIceLevelY, -7f);
			GL11.glRotatef(15f, 0.0f, 1.0f, 0.0f);
			GL11.glScalef(15f, 1.0f, 15f);
			GL11.glCallList(planeList);

			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// draw the background mountain plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mountainTextures.getTextureID());

			// position, scale and draw the back plane using its display list
			GL11.glTranslatef(-1.05f, 4.4f, -12.0f);
			GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			GL11.glScalef(16f, 1f, 11f);
			GL11.glCallList(planeList);

			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// draw the first iceberg
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, icebergTextures.getTextureID());

			// position, scale and draw the first iceberg
			GL11.glTranslatef(floating, up, -10.0f);
			GL11.glRotatef(15f, 0.0f, 1.0f, 0.0f);
			GL11.glScalef(ice, ice, ice);

			// draw the first iceberg using its display list
			GL11.glCallList(icebergList);
			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();

			// draw the second iceberg

			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, icebergTextures.getTextureID());

			// position, scale and draw the second iceberg
			GL11.glTranslatef(floating2, up2, 4.0f);

			// draw the second iceberg using its display list
			GL11.glCallList(icebergList);
			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// draw the shrub
		GL11.glPushMatrix();
		{

			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, shrubTextures.getTextureID());
			GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
			GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
			GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
			GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);

			// how shiny are the front faces of the shrub (specular exponent)
			float headFrontShininess = 2.0f;
			// specular reflection of the front faces of the shrub
			float headFrontSpecular[] = { 0.1f, 0f, 0f, 1.0f };
			// diffuse reflection of the front faces of the shrub
			float headFrontDiffuse[] = { 0.1f, 0.1f, 0.1f, 1.0f };

			// set the material properties for the shrub using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, headFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(headFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(headFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(headFrontDiffuse));

			// position and draw the shrub using a sphere quadric object
			GL11.glTranslatef(-1f, shrubY, -8.0f);
			GL11.glRotatef(0f, 0.0f, 1.0f, 0.0f);
			GL11.glScalef(0.65f, 0.5f, 0.5f);

			new Sphere().draw(0.8f, 10, 10);
			GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
			GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

		// draw the house and its roof
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, woodTextures.getTextureID());
			// position and scale the house
			GL11.glTranslatef(-2.5f, currentHouseY, -10.0f);
			GL11.glScalef(2.0f, 2.0f, 2.0f);
			// rotate the house a little so that we can see more of it
			GL11.glRotatef(70.0f, 0.0f, 1.0f, 0.0f);

			// how shiny are the front faces of the house (specular exponent)
			float houseFrontShininess = 2.0f;
			// specular reflection of the front faces of the house
			float houseFrontSpecular[] = { 0.1f, 0f, 0f, 1.0f };
			// diffuse reflection of the front faces of the house
			float houseFrontDiffuse[] = { 0.1f, 0.1f, 0.1f, 1.0f };

			// set the material properties for the house using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, houseFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(houseFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(houseFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(houseFrontDiffuse));

			// draw the base of the house using its display list
			GL11.glCallList(houseList);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();

			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			Colour.WHITE.submit();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, snowRoofTextures.getTextureID());
			// position and scale the house's roof relative to the base of the house
			GL11.glTranslatef(0.0f, 0.75f, 0.0f);
			GL11.glScalef(1.0f, 0.5f, 1.0f);

			// how shiny are the front faces of the roof (specular exponent)
			float roofFrontShininess = 2.0f;
			// specular reflection of the front faces of the roof
			float roofFrontSpecular[] = { 0.1f, 0.2f, 0.2f, 1.0f };
			// diffuse reflection of the front faces of the roof
			float roofFrontDiffuse[] = { 0.2f, 0f, 0f, 1.0f };

			// Set the material properties for the roof using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, roofFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(roofFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(roofFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(roofFrontDiffuse));

			// draw the roof using its display list
			GL11.glCallList(roofList);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();

			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, woodenDoorTextures.getTextureID());

			// position and scale the door
			GL11.glTranslatef(-0.01f, -1.3f, 0.0f);
			GL11.glScalef(1f, 2.0f, 1f);

			// how shiny are the front faces of the door (specular exponent)
			float doorFrontShininess = 2.0f;
			// specular reflection of the front faces of the door
			float doorFrontSpecular[] = { 0.1f, 0.2f, 0.2f, 1.0f };
			// diffuse reflection of the front faces of the door
			float doorFrontDiffuse[] = { 0.8f, 0.2f, 0.2f, 1.0f };

			// set the material properties for the door using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, doorFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(doorFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(doorFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(doorFrontDiffuse));

			// draw the door of the house using its display list
			GL11.glCallList(doorList);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();

			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			Colour.WHITE.submit();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, windowTextures.getTextureID());

			// position and scale the house's window
			GL11.glTranslatef(0f, 0.1f, 0.51f);
			GL11.glScalef(0.5f, 0.25f, 0.0f);

			// how shiny are the front faces of the window (specular exponent)
			float tempFrontShininess = 2.0f;
			// specular reflection of the front faces of the window
			float tempFrontSpecular[] = { 0.1f, 0f, 0f, 1.0f };
			// diffuse reflection of the front faces of the window
			float tempFrontDiffuse[] = { 0.6f, 0.2f, 0.2f, 1.0f };

			// Set the material properties for the window using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, tempFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(tempFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(tempFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(tempFrontDiffuse));

			// draw the window using its display list
			GL11.glCallList(windowList);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();

		}
		GL11.glPopMatrix();

		// draw the wooden sign
		GL11.glPushMatrix();
		{
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			Colour.WHITE.submit();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, postTextures.getTextureID());

			// position and scale the wooden sign
			GL11.glTranslatef(0.1f, -0.6f, -4.2f);
			GL11.glScalef(0.05f, 0.7f, 0.05f);
			GL11.glRotatef(70f, 0.0f, 1.0f, 0.0f);

			// how shiny are the front faces of the wooden sign (specular exponent)
			float woodenFrontShininess = 2.0f;
			// specular reflection of the front faces of the wooden sign
			float woodenFrontSpecular[] = { 0.1f, 0.2f, 0.2f, 1.0f };
			// diffuse reflection of the front faces of the wooden sign
			float woodenFrontDiffuse[] = { 0.2f, 0f, 0f, 1.0f };

			// Set the material properties for the wooden sign using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, woodenFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(woodenFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(woodenFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(woodenFrontDiffuse));

			// draw the wooden sign using its display list
			GL11.glCallList(woodenPostsList);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();

			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			Colour.WHITE.submit();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, postTextures.getTextureID());
			// position and scale the wooden sign
			GL11.glTranslatef(3f, 0.4f, 0.3f);
			GL11.glScalef(20f, 0.2f, 0.5f);

			// how shiny are the front faces of the wooden sign (specular exponent)
			float woodenSignFrontShininess = 2.0f;
			// specular reflection of the front faces of the wooden sign
			float woodenSignFrontSpecular[] = { 0.1f, 0.2f, 0.2f, 1.0f };
			// diffuse reflection of the front faces of the wooden sign
			float woodenSignFrontDiffuse[] = { 0.2f, 0f, 0f, 1.0f };

			// Set the material properties for the wooden sign using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, woodenSignFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(woodenSignFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(woodenSignFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(woodenSignFrontDiffuse));

			// draw the wooden sign using its display list
			GL11.glCallList(woodenPostsList);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();

			// Draw the thermometer

			// position and scale the thermometer
			GL11.glTranslatef(-0.09f, 0f, 1.9f);
			GL11.glScalef(0.75f, 0.5f, 0.8f);

			// how shiny are the front faces of the thermometer (specular exponent)
			float tempFrontShininess = 2.0f;
			// specular reflection of the front faces of the thermometer
			float tempFrontSpecular[] = { 0.1f, 0f, 0f, 1.0f };
			// diffuse reflection of the front faces of the thermometer
			float tempFrontDiffuse[] = { temperatureRed, 0.2f, temperatureBlue, 1.0f };

			// Set the material properties for the thermometer using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, tempFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(tempFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(tempFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(tempFrontDiffuse));

			// draw the thermometer using its display list
			GL11.glCallList(thermometerList);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();
	}

	protected void setSceneCamera() {
		// call the default behaviour defined in GraphicsLab. This will set a default
		// perspective projection
		// and default camera settings ready for some custom camera positioning below...
		super.setSceneCamera();

	}

	protected void cleanupScene() {
	}

	/**
	 * Draws a plane aligned with the X and Z axis, with its front face toward
	 * positive Y. The plane is of unit width and height, and uses the current
	 * OpenGL material settings for its appearance
	 */
	private void drawWaterPlane() {
		Vertex v1 = new Vertex(-0.5f, 0.0f, -0.5f); // left, back
		Vertex v2 = new Vertex(0.5f, 0.0f, -0.5f); // right, back
		Vertex v3 = new Vertex(0.5f, 0.0f, 0.5f); // right, front
		Vertex v4 = new Vertex(-0.5f, 0.0f, 0.5f); // left, front

		// draw the plane geometry. order the vertices so that the plane faces up
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v4.toVector(), v3.toVector(), v2.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(0.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();
		}
		GL11.glEnd();

		// if the user is viewing an axis, then also draw this plane
		// using lines so that axis aligned planes can still be seen
		if (isViewingAxis()) {
			// also disable textures when drawing as lines
			// so that the lines can be seen more clearly
			GL11.glPushAttrib(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			{
				v4.submit();
				v3.submit();
				v2.submit();
				v1.submit();
			}
			GL11.glEnd();
			GL11.glPopAttrib();
		}
	}

	/**
	 * Draws a plane aligned with the X and Z axis, with its front face toward
	 * positive Y. The plane is of unit width and height, and uses the current
	 * OpenGL material settings for its appearance
	 */
	private void drawSnowPlane() {
		Vertex v1 = new Vertex(-0.5f, 0.0f, -0.5f); // left, back
		Vertex v2 = new Vertex(0.5f, 0.0f, -0.5f); // right, back
		Vertex v3 = new Vertex(0.5f, 0.0f, 0.5f); // right, front
		Vertex v4 = new Vertex(-0.5f, 0.0f, 0.5f); // left, front

		// draw the plane geometry. order the vertices so that the plane faces up
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v4.toVector(), v3.toVector(), v2.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(0.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();
		}
		GL11.glEnd();

		// if the user is viewing an axis, then also draw this plane
		// using lines so that axis aligned planes can still be seen
		if (isViewingAxis()) {
			// also disable textures when drawing as lines
			// so that the lines can be seen more clearly
			GL11.glPushAttrib(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			{
				v4.submit();
				v3.submit();
				v2.submit();
				v1.submit();
			}
			GL11.glEnd();
			GL11.glPopAttrib();
		}
	}

	/**
	 * Draws a iceberg of unit length, width and height. The iceberg uses the
	 * current OpenGL material settings for its appearance
	 */
	private void drawUnitIceberg() {
		Vertex v1 = new Vertex(-0.5f, -0.5f, -0.5f);
		Vertex v2 = new Vertex(-0.9f, 0.1f, 0.0f);
		Vertex v3 = new Vertex(-0.5f, -0.5f, 0.5f);
		Vertex v4 = new Vertex(0.5f, -0.5f, -0.5f);
		Vertex v5 = new Vertex(0.6f, 0.5f, 0.0f);
		Vertex v6 = new Vertex(0.5f, -0.5f, 0.5f);
		Vertex v7 = new Vertex(0f, 0.1f, 0f);
		Vertex v8 = new Vertex(-0.2f, 0.6f, 0f);
		Vertex v9 = new Vertex(-0.7f, 0.8f, 0f);
		Vertex v10 = new Vertex(0.2f, 0.4f, 0f);

		// left gable
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			new Normal(v3.toVector(), v2.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 0.0f);
			v3.submit();
			GL11.glTexCoord2f(0.5f, 0.3f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v1.submit();
		}
		GL11.glEnd();

		// back slope
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v2.toVector(), v5.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(0.2f, 0.0f);
			v1.submit();
			GL11.glTexCoord2f(0f, 0.3f);
			v2.submit();
			GL11.glTexCoord2f(0.4f, 1.0f);
			v9.submit();
			GL11.glTexCoord2f(0.6f, 0.8f);
			v8.submit();
			GL11.glTexCoord2f(0.7f, 0.3f);
			v7.submit();
			GL11.glTexCoord2f(0.8f, 0.5f);
			v10.submit();
			GL11.glTexCoord2f(0.8f, 0.5f);
			v5.submit();
			GL11.glTexCoord2f(0.8f, 0f);
			v4.submit();
		}
		GL11.glEnd();

		// front slope
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v5.toVector(), v2.toVector(), v3.toVector(), v6.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 0.7f);
			v5.submit();
			GL11.glTexCoord2f(0.8f, 0.5f);
			v10.submit();
			GL11.glTexCoord2f(0.7f, 0.3f);
			v7.submit();
			GL11.glTexCoord2f(0.6f, 0.8f);
			v8.submit();
			GL11.glTexCoord2f(0.4f, 1.0f);
			v9.submit();
			GL11.glTexCoord2f(0f, 0.3f);
			v2.submit();
			GL11.glTexCoord2f(0.2f, 0f);
			v3.submit();
			GL11.glTexCoord2f(0.8f, 0f);
			v6.submit();
		}
		GL11.glEnd();

		// right gable
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			new Normal(v5.toVector(), v6.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(0.5f, 0.3f);
			v5.submit();
			GL11.glTexCoord2f(1f, 0.0f);
			v6.submit();
			GL11.glTexCoord2f(0f, 0f);
			v4.submit();
		}
		GL11.glEnd();

		// bottom side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v3.toVector(), v1.toVector(), v4.toVector(), v6.toVector()).submit();

			GL11.glTexCoord2f(0f, 1f);
			v3.submit();
			GL11.glTexCoord2f(0f, 0.0f);
			v1.submit();
			GL11.glTexCoord2f(1f, 0f);
			v4.submit();
			GL11.glTexCoord2f(1f, 1f);
			v6.submit();
		}
		GL11.glEnd();
	}

	/**
	 * Draws a roof of unit length, width and height aligned along the x axis. The
	 * roof uses the current OpenGL material settings for its appearance
	 */
	private void drawUnitRoof() {
		Vertex v1 = new Vertex(-0.5f, -0.5f, -0.5f);
		Vertex v2 = new Vertex(-0.5f, 0.5f, 0.0f);
		Vertex v3 = new Vertex(-0.5f, -0.5f, 0.5f);
		Vertex v4 = new Vertex(0.5f, -0.5f, -0.5f);
		Vertex v5 = new Vertex(0.5f, 0.5f, 0.0f);
		Vertex v6 = new Vertex(0.5f, -0.5f, 0.5f);

		// left gable
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			new Normal(v3.toVector(), v2.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(0f, 0f);
			v3.submit();
			GL11.glTexCoord2f(0.5f, 1f);
			v2.submit();
			GL11.glTexCoord2f(1f, 0f);
			v1.submit();
		}
		GL11.glEnd();

		// back slope
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v2.toVector(), v5.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(0f, 0f);
			v1.submit();
			GL11.glTexCoord2f(0f, 1f);
			v2.submit();
			GL11.glTexCoord2f(1f, 1f);
			v5.submit();
			GL11.glTexCoord2f(1f, 0f);
			v4.submit();
		}
		GL11.glEnd();

		// front slope
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v5.toVector(), v2.toVector(), v3.toVector(), v6.toVector()).submit();

			GL11.glTexCoord2f(1f, 1f);
			v5.submit();
			GL11.glTexCoord2f(0f, 1f);
			v2.submit();
			GL11.glTexCoord2f(0f, 0f);
			v3.submit();
			GL11.glTexCoord2f(1f, 0f);
			v6.submit();
		}
		GL11.glEnd();

		// right gable
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			new Normal(v5.toVector(), v6.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(0.5f, 1f);
			v5.submit();
			GL11.glTexCoord2f(0f, 0f);
			v6.submit();
			GL11.glTexCoord2f(1f, 0f);
			v4.submit();
		}
		GL11.glEnd();
	}

	/**
	 * Draws a rectangle for a door of unit length, width and height using the
	 * current OpenGL material settings
	 */
	private void drawUnitDoor() {
		Vertex v1 = new Vertex(-0.5f, -0.5f, -0.15f);
		Vertex v2 = new Vertex(-0.5f, -0.5f, 0.15f);
		Vertex v3 = new Vertex(-0.5f, 0f, 0.15f);
		Vertex v4 = new Vertex(-0.5f, 0f, -0.15f);

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v3.toVector(), v4.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 0.2f);
			v2.submit();
			GL11.glTexCoord2f(1.0f, 0.88f);
			v3.submit();
			GL11.glTexCoord2f(0.0f, 0.88f);
			v4.submit();
			GL11.glTexCoord2f(0.0f, 0.2f);
			v1.submit();
		}
		GL11.glEnd();
	}

	/**
	 * Draws a square for a window of unit length, width and height using the
	 * current OpenGL material settings
	 */
	private void drawUnitWindow() {
		Vertex v1 = new Vertex(-0.5f, -0.5f, 0f);
		Vertex v2 = new Vertex(-0.5f, 0.5f, 0f);
		Vertex v3 = new Vertex(0.5f, -0.5f, 0f);
		Vertex v4 = new Vertex(0.5f, 0.5f, 0f);

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v1.toVector(), v3.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(0.0f, 1f);
			v2.submit();
			GL11.glTexCoord2f(0f, 0f);
			v1.submit();
			GL11.glTexCoord2f(1.0f, 0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v4.submit();
		}
		GL11.glEnd();
	}

	/**
	 * Draws a cube of unit length, width and height using the current OpenGL
	 * material settings
	 */
	private void drawUnitCube() {
		// the vertices for the cube (note that all sides have a length of 1)
		Vertex v1 = new Vertex(-0.5f, -0.5f, 0.5f);
		Vertex v2 = new Vertex(-0.5f, 0.5f, 0.5f);
		Vertex v3 = new Vertex(0.5f, 0.5f, 0.5f);
		Vertex v4 = new Vertex(0.5f, -0.5f, 0.5f);
		Vertex v5 = new Vertex(-0.5f, -0.5f, -0.5f);
		Vertex v6 = new Vertex(-0.5f, 0.5f, -0.5f);
		Vertex v7 = new Vertex(0.5f, 0.5f, -0.5f);
		Vertex v8 = new Vertex(0.5f, -0.5f, -0.5f);

		// draw the near face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v3.toVector(), v2.toVector(), v1.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v1.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v4.submit();

		}
		GL11.glEnd();

		// draw the left face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v6.toVector(), v5.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v1.submit();
		}
		GL11.glEnd();

		// draw the right face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v7.toVector(), v3.toVector(), v4.toVector(), v8.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v7.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v8.submit();
		}
		GL11.glEnd();

		// draw the top face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v7.toVector(), v6.toVector(), v2.toVector(), v3.toVector()).submit();

			v7.submit();
			v6.submit();
			v2.submit();
			v3.submit();
		}
		GL11.glEnd();

		// draw the bottom face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v4.toVector(), v1.toVector(), v5.toVector(), v8.toVector()).submit();

			v4.submit();
			v1.submit();
			v5.submit();
			v8.submit();
		}
		GL11.glEnd();

		// draw the far face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v6.toVector(), v7.toVector(), v8.toVector(), v5.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v7.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v8.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v5.submit();
		}
		GL11.glEnd();
	}

}
