package com.padskiiz.mineclone.main;

import static org.lwjgl.opengl.GL11.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.padskiiz.engine.*;
import com.padskiiz.engine.graph.*;

public class Renderer {
	private ShaderProgram shaderProgram;
	private Transformation transformation;
	
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;
	private float specularPower;
	
	public Renderer() {
		transformation = new Transformation();
	}
	
	public void init(Window window) throws Exception{
		//Create Shader
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
		shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
		shaderProgram.link();
		
		//Create Uniforms for modelView & projection matrices and texture
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("texture_sampler");
		
		//Create Uniform for material
		shaderProgram.createMaterialUniform("material");
		
		//Create Lighting Uniforms
		shaderProgram.createUniform("specularPower");
		shaderProgram.createUniform("ambientLight");
		shaderProgram.createPointLightUniform("pointLight");
		
		window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public void render(Window window, GameItem[] gameItems, Camera camera, Vector3f ambientLight, PointLight pointLight) {
		clear();
		
		if (window.isResized()){
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		
		shaderProgram.bind();
		
		//Update ProjectionMatrix
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		
		//Update viewMatrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		
		//Update Light Uniforms
		shaderProgram.setUniform("ambientLight", ambientLight);
		shaderProgram.setUniform("specularPower", specularPower);
		
		//Copy light object and transform to view coordinates
		PointLight currPointLight = new PointLight(pointLight);
		Vector3f lightPos = currPointLight.getPosition();
		Vector4f aux = new Vector4f(lightPos, 1);
		aux.mul(viewMatrix);
		lightPos.x = aux.x;
		lightPos.y = aux.y;
		lightPos.z = aux.z;
		shaderProgram.setUniform("pointLight", currPointLight);
		
		shaderProgram.setUniform("texture_sampler", 0);
		for(GameItem gameItem : gameItems){
			Mesh mesh = gameItem.getMesh();
			
			//Set model view matrix for item
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
			shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			
			//Render Mesh
			shaderProgram.setUniform("material", mesh.getMaterial());
			mesh.render();
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
