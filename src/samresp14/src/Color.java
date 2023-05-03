package samresp14.src;

import org.joml.Vector3f;

public class Color {
	public static final Color RED = new Color(1f, 0f, 0f);
	public static final Color GREEN = new Color(0f, 1f, 0f);
	public static final Color BLUE = new Color(0f, 0f, 1f);
	
	public float r;
	public float g;
	public float b;
	
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Vector3f asVector3f() {
		return new Vector3f(r, g, b);
	}
}
