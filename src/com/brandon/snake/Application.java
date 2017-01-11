package com.brandon.snake;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.brandon.snake.game.Game;
import com.brandon.snake.input.Input;
import com.brandon.snake.render.GameRenderer;
 
public class Application {
 
    private long window;
    
    private Input input;
    private Game game;
    private GameRenderer renderer;
    
    //Main Constants
    final private static double UPDATE_DELAY = 125; //ms 125
    final private static int WINDOW_WIDTH = 512; //px
    final private static int WINDOW_HEIGHT = 512; //px
    
    //Game Constants
    final private static int GAME_WIDTH = 16;
    final private static int GAME_HEIGHT = 16;
    
    public Application() {
    	game = new Game(GAME_WIDTH, GAME_HEIGHT);
    	renderer = new GameRenderer(
            game,
            GAME_WIDTH,
            GAME_HEIGHT,
            WINDOW_WIDTH, 
            WINDOW_HEIGHT
        );
    	input = new Input(
    		game,
    		renderer,
        	GLFW_KEY_W, 
        	GLFW_KEY_S, 
           	GLFW_KEY_A, 
            GLFW_KEY_D, 
            GLFW_KEY_P, 
            GLFW_KEY_ENTER,
            GLFW_KEY_ESCAPE
        );      
    }
    
    public void run() {
        try {
            init();
            loop();
 
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
            
            if (renderer != null) {
            	renderer.destroy();
            }
        }
    }
 
    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
     	if (!glfwInit())
     		throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Snake", NULL, NULL);
        
        glfwSetKeyCallback(window, input);
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            window,
            (vidmode.width() - WINDOW_WIDTH) / 2,
            (vidmode.height() - WINDOW_HEIGHT) / 2
        );
 
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_ALPHA);
        
        renderer.init();
        
        game.reset();
        renderer.reset();
    }
    
    private void loop() {
    	double initialTime;
        while (!glfwWindowShouldClose(window)) {
        	initialTime = System.currentTimeMillis();
        	
        	//Game Update Cycle        	
        	input.pollNext();
        	game.update();
        	if (!game.isPaused()) {
        		renderer.update();
        		game.straighten();
        	}
        	
        	while (System.currentTimeMillis() - initialTime < UPDATE_DELAY) {
        		//UI Update Cycle
        		glfwPollEvents();
        		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        		renderer.render();
        		
        		glfwSwapBuffers(window);
        		try {
        			Thread.sleep(1);
        		} catch (InterruptedException e) {
        			e.printStackTrace();
        		}
        	}
        }
	}
    
    public static void main(String[] args) {
    	new Application().run();
    	
    }
    
 
}