package com.padskiiz.engine.graph;

import java.nio.ByteBuffer;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture {
	private final int id;
	
	public Texture(String fileName) throws Exception {
		this(loadTexture(fileName));
	}

	public Texture(int id) {
        this.id = id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }
	
	public static int loadTexture(String fileName) throws Exception {
		
		PNGDecoder decoder = new PNGDecoder(
			Texture.class.getResourceAsStream(fileName));
		
		ByteBuffer buf = ByteBuffer.allocateDirect(
				4 * decoder.getWidth() * decoder.getHeight());
		
		decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
		buf.flip();
		
		int textureId = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, textureId);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);	
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		return textureId;
	}
	
	public void cleanup() {
		glDeleteTextures(id);
	}
}
