package samresp14.src.util;

import org.joml.Vector3f;
import org.joml.Vector3i;

public final class VectorUtil {
	public static Vector3f fromVector3i(Vector3i vi) {
		return new Vector3f((float)vi.x, (float)vi.y, (float)vi.z);
	}
	
	public static Vector3i fromVector3f(Vector3f vf) {
		return new Vector3i((int)vf.x, (int)vf.y, (int)vf.z);
	}
}
