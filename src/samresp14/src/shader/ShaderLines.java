package samresp14.src.shader;

public class ShaderLines extends ShaderProgram {

	public ShaderLines() {
		super("lines.vs", "lines.fs");
	}

	@Override
	protected void bindAttributes() {
		// TODO Auto-generated method stub
		super.bindAttribute(0, "position");
		//super.bindAttribute(1, "in_uvs");
	}

	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		super.findUniformLocation("model");
		super.findUniformLocation("view");
		super.findUniformLocation("projection");
		super.findUniformLocation("color");
	}
	
}
