package com.padskiiz.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	private final Matrix4f worldMatrix;
	private final Matrix4f projectionMatrix;
	
	public Transformation() {
		worldMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
	}
	
	public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRation = width / height;
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspectRation, zNear, zFar);
		
		return projectionMatrix;
	}
	
	public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale){
		worldMatrix.identity().translate(offset).
			rotateX((float)Math.toRadians(rotation.x)).
			rotateY((float)Math.toRadians(rotation.y)).
			rotateZ((float)Math.toRadians(rotation.z)).
			scale(scale);

		return worldMatrix;
	}
}
