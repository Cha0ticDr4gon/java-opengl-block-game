package Utilities;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayUtils {
	
	public static final int WIDTH = 1440;
	public static final int HEIGHT = 1080;
	
	public static int FPS = 60;
	
	public static final String title = "Experimental Engine V0.1";
	
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 3);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(title);
			
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static boolean stillActive() {
		return !Display.isCloseRequested();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS);
		Display.update();
	}
	
	public static void destroyDisplay() {
		Display.destroy();
	}

}
