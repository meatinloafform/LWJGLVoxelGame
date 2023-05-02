package samresp14.src.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class Shader {
	// ROOM 315
	private int programID;
	private Map<String, Integer> uniformLocations;
	
	// 4x4
	private FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
	
	public Shader(String vert_source, String frag_source) {
		int vertexID = loadShader(vert_source, GL20.GL_VERTEX_SHADER);
		int fragmentID = loadShader(frag_source, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexID);
		GL20.glAttachShader(programID, fragmentID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		uniformLocations = new HashMap<>();
		getAllUniformLocations();
	}
	
	public void use() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	protected abstract void bindAttributes();
	protected abstract void getAllUniformLocations();
	
	protected void findUniformLocation(String uniformName) {
		uniformLocations.put(uniformName, GL20.glGetUniformLocation(programID, uniformName));
	}
	
	public int getUniformLocation(String uniform) {
		return uniformLocations.get(uniform);
	}
		
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	public void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	public void loadVector3(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	public void loadBoolean(int location, boolean value) {
		GL20.glUniform1f(location, value ? 1.0f : 0.0f);
	}
	
	public void loadMatrix(int location, Matrix4f mat) {
		mat.get(matrix);
		GL20.glUniformMatrix4fv(location, false, matrix);
	}
	
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("res/shaders/"+file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Can't read file");
			e.printStackTrace();
			System.exit(-1);
		}
		int ID = GL20.glCreateShader(type);
		GL20.glShaderSource(ID, shaderSource);
		GL20.glCompileShader(ID);
		if (GL20.glGetShaderi(ID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(ID, 512));
			System.err.println("Couldn't compile the shader");
			System.exit(-1);
		}
		return ID;
	}
}
