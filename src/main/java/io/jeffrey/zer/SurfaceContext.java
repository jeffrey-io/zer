package io.jeffrey.zer;

public class SurfaceContext {

	public final Camera camera;

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
