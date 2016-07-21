package com.padskiiz.engine.graph;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

public class Mesh {
	public final int vaoId;
	private final List<Integer> vboIdList;
	private final int vertexCount;
	private Material material;
	
	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
		vertexCount = indices.length;
		vboIdList = new ArrayList<Integer>();
		
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		
		//Position VBO
		int vboId = glGenBuffers();
        vboIdList.add(vboId);
		FloatBuffer posBuffer = BufferUtils.createFloatBuffer(positions.length);
		posBuffer.put(positions).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		//Vertex normals VBO
		vboId = glGenBuffers();
		vboIdList.add(vboId);
		FloatBuffer vecNormalsBuffer = BufferUtils.createFloatBuffer(normals.length);
		vecNormalsBuffer.put(normals).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		
		//Texture VBO
		vboId = glGenBuffers();
		vboIdList.add(vboId);
		FloatBuffer textCoordsBuffer = BufferUtils.createFloatBuffer(textCoords.length);
		textCoordsBuffer.put(textCoords).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		//Indices VBO
		vboId = glGenBuffers();
        vboIdList.add(vboId);
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public int getVaoId() {
		return this.vaoId;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public void render() {	
		Texture texture = material.getTexture();
		if (texture != null) {
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
		
		//Draw mesh
		glBindVertexArray(getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		
		//Restore State
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void cleanUp() {
		glDisableVertexAttribArray(0);
		
		// Delete VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }
        
        //Delete texture
        Texture texture = material.getTexture();
        if (texture != null) {
        	texture.cleanup();
        }
        
		// Delete VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
