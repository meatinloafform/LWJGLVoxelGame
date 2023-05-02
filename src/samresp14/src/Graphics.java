//package samresp14.src;
//
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL15;
//import org.lwjgl.opengl.GL20;
//import org.lwjgl.opengl.GL30;
//
//public class Graphics {
//	public static final String VERTEX_SOURCE = 
//	""" 	
//		#version 330 core
//		layout (location = 0) in vec3 aPos;
//		
//		void main()
//		{
//		    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
//		}
//	""";
//	
//	public static final String FRAGMENT_SOURCE = 
//	"""
//		#version 330 core
//		out vec4 FragColor;
//		
//		void main()
//		{
//		    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
//		} 
//	""";
//	
//	public static final float[] VERTICES = {
//		-0.5f, -0.5f, 0.0f,
//		 0.5f, -0.5f, 0.0f,
//		 0.0f,  0.5f, 0.0f
//	};
//	
//	public int program;
//	public int vbo;
//	public int vao;
//	
//	public Graphics() {
//		try {
//			createProgram();
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//		createVAO();
//	}
//	
//	public void createProgram() throws Exception {
//		int vbo = GL15.glGenBuffers();
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VERTICES, GL15.GL_STATIC_DRAW);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//		this.vbo = vbo;
//		
//		int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
//		GL20.glShaderSource(vertexShader, VERTEX_SOURCE);
//		GL20.glCompileShader(vertexShader);
//		
//		if (GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS) == 0) {
//			throw new Exception("Error compiling vertex shader code " + GL20.glGetShaderInfoLog(vertexShader, 1024));
//		}
//		
//		int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
//		GL20.glShaderSource(fragmentShader, FRAGMENT_SOURCE);
//		GL20.glCompileShader(fragmentShader);
//		
//		if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == 0) {
//			throw new Exception("Error compiling fragment shader code " + GL20.glGetShaderInfoLog(vertexShader, 1024));
//		}
//		
//		int shaderProgram = GL20.glCreateProgram();
//		
//		GL20.glAttachShader(shaderProgram, vertexShader);
//		GL20.glAttachShader(shaderProgram, fragmentShader);
//		GL20.glLinkProgram(shaderProgram);
//		
//		if (GL20.glGetProgrami(fragmentShader, GL20.GL_COMPILE_STATUS) == 0) {
//			throw new Exception("Error compiling fragment shader code " + GL20.glGetShaderInfoLog(vertexShader, 1024));
//		} else {
//			this.program = shaderProgram;
//		}
//		
//		GL20.glDeleteShader(vertexShader);
//		GL20.glDeleteShader(fragmentShader);
//	}
//	
//	public void createVAO() {
//		this.vao = GL30.glGenVertexArrays();
//		
//		GL30.glBindVertexArray(this.vao);
//		
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VERTICES, GL15.GL_STATIC_DRAW);
//		
//		GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.SIZE * 3, 0);
//		GL30.glEnableVertexAttribArray(0);
//	}
//	
//	public void render() {
//		GL20.glUseProgram(this.program);
//		GL30.glBindVertexArray(this.vao);
//		
//		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
//	}
//	
//	public void free() {
//		GL20.glDeleteShader(0);
//	}
//}
