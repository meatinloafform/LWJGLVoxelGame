package samresp14.src;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import samresp14.src.game.Chunk;
import samresp14.src.game.World;
import samresp14.src.game.blocks.Blocks;
import samresp14.src.game.collider.AABBCollider;
import samresp14.src.game.collider.WorldCollider;
import samresp14.src.game.worldgen.feature.BlobFeature;
import samresp14.src.game.worldgen.feature.SphereFeature;
import samresp14.src.rendering.MeshRenderer;
import samresp14.src.rendering.RendererBundle;
import samresp14.src.shader.ShaderTextured;
import samresp14.src.util.VectorUtil;

// TODO: 3D grid raycast, 3D player controller, multi-chunk rendering
// inventory

public class Main {
	private static float aspect = 1.0f;
	private static boolean aspectChanged = false;
	
	private long window;
	
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		
		init();
		loop();
		
		// End of program
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	private void init() {
		// Setup error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		window = glfwCreateWindow(300, 300, "Hello LWJGL!", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create GLFW window");
		}
		
//		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
//				glfwSetWindowShouldClose(window, true);
//			}
//		});
		
		// what is up with this????
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); //int*
			IntBuffer pHeight = stack.mallocInt(1); //int*
			
			glfwGetWindowSize(window, pWidth, pHeight);
			
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		
		Input.init(window);
		GLFW.glfwSetKeyCallback(window, Input.keyboard);
		GLFW.glfwSetMouseButtonCallback(window, Input.mousePress);
		GLFW.glfwSetFramebufferSizeCallback(window, resize);
		
		glfwMakeContextCurrent(window);
		
		glfwSwapInterval(1);
		
		glfwShowWindow(window);
	}
	
	private void loop() {
		GL.createCapabilities();
		
		GL20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);	
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glCullFace(GL11.GL_CCW);
		
		Texture texture = Texture.loadTexture("res/textures/atlas.png");
		ShaderTextured shader = new ShaderTextured();
		shader.use();

		//Chunk chunk = new Chunk(0);
		
//		for (int z = 0; z < 8; z++) {
//			for (int y = 0; y < 8; y++) {
//				for (int x = 0; x < 8; x++) {
//					if (Math.random() > 0.5) {
//						chunk.setBlock((short)1, x, y, z);
//					}
//				}
//			}
//		}
		
		//chunk.generateMesh(texture);
		
		World world = new World(texture);
//		world.addChunk(new Vector2i(0, 1));
//		world.addChunk(new Vector2i(0, 0));
//		world.addChunk(new Vector2i(0, -1));
		
		world.setBlock(new Vector3i(8, 8, 8), Blocks.BLOCK_BRICK);		
		world.setBlock(new Vector3i(8, 9, 8), Blocks.BLOCK_GHOST);
		
		world.setBlock(new Vector3i(0, 0, 0), Blocks.BLOCK_WHITE);
		
		SphereFeature theEternalSphere = new SphereFeature(new Vector3i(0, 24, 0), Blocks.BLOCK_BRICK, 25);
		theEternalSphere.apply(world);
		
		SphereFeature sphere = new SphereFeature(new Vector3i(8, 8, 0), Blocks.BLOCK_GHOST, 3);
		sphere.apply(world);
		
		SphereFeature sphere2 = new SphereFeature(new Vector3i(8, 8, 16), Blocks.BLOCK_BRICK, 5);
		sphere2.apply(world);
		for (int i = -1; i < 2; i++) {
			for (int j = 1; j <= 16; j++) {
				Vector3i position = new Vector3i(8, 8, i * 16 + j);
				world.setBlock(position, i % 2 == 0 ? Blocks.BLOCK_BRICK : Blocks.BLOCK_GHOST);
			}
		}
		
		BlobFeature blob = new BlobFeature(new Vector3i(8, 16, 8), Blocks.BLOCK_WHITE, new Vector3i(5, 3, 3));
		blob.apply(world);
		
		for (int i = 0; i < 64; i++) {
			world.setBlock(new Vector3i(8, i, 8), Blocks.BLOCK_WHITE);
		}
		
		Camera camera = new Camera();
		camera.position = new Vector3f(0f, 0f, 5f);
		RendererBundle renderer = new RendererBundle(camera);
		AABBCollider collider = new AABBCollider(new Vector3f(), new Vector3f(1f, 2f, 1f));
		WorldCollider worldCollider = new WorldCollider(world);
		//MeshRenderer renderer = new MeshRenderer(shader, camera);
		Input.lockMouse();
		
		while (!glfwWindowShouldClose(window)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			if (aspectChanged) {
				renderer.updateAspect(aspect);
				aspectChanged = false;
			}
			
			if (Input.keyJustPressed(GLFW.GLFW_KEY_ESCAPE)) {
				Input.unlockMouse();
				camera.rotationFrozen = true;
			}
			if (Input.buttonJustPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
				Input.lockMouse();
				camera.rotationFrozen = false;
			}
			
			if (Input.keyJustPressed(GLFW.GLFW_KEY_SPACE)) {
				world.setBlock(World.worldToBlockSpace(camera.position), Blocks.BLOCK_GHOST);
			}
			
			camera.update();
			collider.position = new Vector3f(camera.position).sub(new Vector3f(0.5f, 1f, 0.5f));
			collider.resolve(worldCollider);
			world.update();
			world.render(renderer.mesh);
			
			// draw chunk borders
			Vector3i blockPos = World.worldToBlockSpace(camera.position);
			Vector2i chunkPos = world.getChunkPos(new Vector2i(blockPos.x, blockPos.z));
			Vector3f worldPos = new Vector3f((float)chunkPos.x * 16f - 0.5f, -0.5f, (float)chunkPos.y * 16f - 0.5f);
			Vector3f endPos = new Vector3f(worldPos.x + 16f, 63.5f, worldPos.z + 16f);
			renderer.debug.drawCube(worldPos, endPos, Color.GREEN);
//			Vector3f zero = World.blockToWorldSpace(World.worldToBlockSpace(camera.position));
//			Vector3f one = new Vector3f(zero.x + 1f, zero.y + 1f, zero.z + 1f);
//			renderer.debug.drawCube(zero, one, Color.RED);
			//renderer.draw(chunk.mesh);
			
			GLFW.glfwSwapBuffers(window);
			Input.update();
			glfwPollEvents();
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
	
	private static GLFWFramebufferSizeCallback resize = new GLFWFramebufferSizeCallback() {
		
		@Override
		public void invoke(long window, int width, int height) {
			GL11.glViewport(0, 0, width, height);
			aspect = (float)width / (float)height;
			aspectChanged = true;
		}
	};
}
