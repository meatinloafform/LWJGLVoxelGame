package samresp14.src;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryStack;

import org.lwjgl.stb.STBImage;

public class Texture {
	private final int ID;
	private int width;
	private int height;
	
	public Texture() {
		ID = GL11.glGenTextures();
	}
	
	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ID);
	}
	
	public void setParameter(int name, int value) {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, name, value);
	}
	
	public void uploadData(int width, int height, ByteBuffer data) {
		uploadData(GL11.GL_RGBA8, width, height, GL11.GL_RGBA, data);
	}
	
	public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL11.GL_UNSIGNED_BYTE, data);
	}
	
	public void delete() {
		GL11.glDeleteTextures(ID);
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		if (width > 0) {
			this.width = width;
		}
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		if (height > 0) {
			this.height = height;
		}
	}
	
	public static Texture createTexture(int width, int height, ByteBuffer data) {
		Texture texture = new Texture();
		texture.setWidth(width);
		texture.setHeight(height);
		
		texture.bind();
		
        texture.setParameter(GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
        texture.setParameter(GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
        texture.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        texture.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        texture.uploadData(GL11.GL_RGBA8, width, height, GL11.GL_RGBA, data);

        return texture;
	}
	
	public static Texture loadTexture(String path) {
		ByteBuffer image;
		int width, height;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			
			STBImage.stbi_set_flip_vertically_on_load(true);
			image = STBImage.stbi_load(path, w, h, comp, 4);
			
			if (image == null) {
				throw new RuntimeException("Failed to load texture" + System.lineSeparator() + STBImage.stbi_failure_reason());
			}
			
			width = w.get();
			height = h.get();
		}
		
		return createTexture(width, height, image);
	}
}
