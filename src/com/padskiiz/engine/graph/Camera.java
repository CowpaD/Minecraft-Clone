package com.padskiiz.engine.graph;

import org.joml.Vector3f;

public class Camera {
	private Vector3f position;
	private Vector3f rotation;
	
	public Camera() {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
	}
	
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void movePosition(float dX, float dY, float dZ) {
		if (dZ != 0) {
			position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * dZ;
			position.z += (float)Math.cos(Math.toRadians(rotation.y)) * dZ;
		}
		if (dX != 0) {
			position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * dX;
			position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * dX;
		}
		position.y += dY;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public void moveRotation(float dX, float dY, float dZ) {
		rotation.x += dX;
		rotation.y += dY;
		rotation.z += dZ;
	}

}
