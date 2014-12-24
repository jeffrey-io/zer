package io.jeffrey.zer;

/**
 * 
 * a context for operations happening against the surface
 * 
 * @author jeffrey
 *
 */
public class SurfaceContext {

	public final Camera camera;

	/**
	 * the camera
	 * 
	 * @param camera
	 */
	public SurfaceContext(Camera camera) {
		this.camera = camera;
		this.cursor_x = 0;
		this.cursor_y = 0;
		this.width = 1;
		this.height = 1;
	}

	public double cursor_y;
	public double cursor_x;
	public double width;
	public double height;
}
