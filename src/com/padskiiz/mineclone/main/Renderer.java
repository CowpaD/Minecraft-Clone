package com.padskiiz.mineclone.main;

import static org.lwjgl.opengl.GL11.*;
import org.joml.Matrix4f;

import com.padskiiz.engine.*;
import com.padskiiz.engine.graph.*;

public class Renderer {
	private ShaderProgram shaderProgram;
	private Transformation transformation;
	
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;
	
	public Renderer() {
		transformation = new Transformation();
	}
	
	public void init(Window window) throws Exception{
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
		shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
		shaderProgram.link();
		
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("worldMatrix");
		shaderProgram.createUniform("texture_sampler");
		
		window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public void render(Window window, GameItem[] gameItems) {
		clear();
		
		if (window.isResized()){
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		
		shaderProgram.bind();
		
		//Update ProjectionMatrix
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		shaderProgram.setUniform("texture_sampler", 0);
		
		for(GameItem gameItem : gameItems){
			Matrix4f worldMatrix = 
					transformation.getWorldMatrix(
							gameItem.getPosition(), 
							gameItem.getRotation(), 
							gameItem.getScale());
			shaderProgram.setUniform("worldMatrix", worldMatrix);
			//Render Mesh
			gameItem.getMesh().render();
		}
		
		shaderProgram.unbind();
	}
	
	public void clear(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void cleanup() {
		if (shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}
}
