import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GLTexture {
	private int width;
	private int height;
	
	private int[] raw;
	int glId;
	
	public GLTexture(String file) {
		try {
			glId = -1;
			BufferedImage texture = ImageIO.read(new File(file));
			width = texture.getWidth();
			height = texture.getHeight();
			
			raw = new int[width*height];
			texture.getRGB(0, 0, width, height, raw, 0, width);
			flip();
		} catch (IOException e) {
			raw = null;
			e.printStackTrace();
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getglId() {
		return glId;
	}
	
	public int[] getRaw() {
		return raw;
	}
	
	public void flip() {
		for (int y = 0; y < height / 2; y++) {
			for (int x = 0; x < width; x++) {
				int oy = height - 1 - y;
				int xy = width*y + x;
				int xoy = width * oy + x;
		
				int tmp = raw[xy];
				raw[xy] = raw[xoy];
				raw[xoy] = tmp;
			}
		}
	}
	
	// Call if the texture is to be used by itself
	public void load() {
		glId = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, glId);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);	
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, raw);
		glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	public void use(int binding, GLProgram program, String handle) {
		glActiveTexture(GL_TEXTURE0 + binding);
		glBindTexture(GL_TEXTURE_2D, glId);
		program.setUniform(handle, binding);
	}
}
