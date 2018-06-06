package com.koowalk.shop.graphics;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;

import org.lwjgl.opengl.GL20;

import com.koowalk.shop.Statics;
import com.koowalk.shop.world.Camera;
import com.koowalk.shop.world.World;
import com.koowalk.shop.world.chunk.Block;
import com.koowalk.shop.world.chunk.Chunk;

// This class is a singleton for rendering the blocks in chunks
// Because of OpenGL's nature, it makes sense to only have one buffer
// of vertex data because every chunk is going to be uniform

public class ChunkRenderEngine {
	private static ChunkRenderEngine instance;
	private static boolean loaded = false;
	
	// Where the shaders are located
	public static final String VERTEX_SHADER = "vertex.glsl";
	public static final String FRAGMENT_SHADER = "fragment.glsl";
	
	public static final int VERTEX_PARAMS = 6;
	
	// This is where the actual block textures are stored
	private GLTextureArray blockTextures; 
	private GLTexture[] backdrops;
	
	private GLProgram program;
	private Camera camera;
	
	// For every compound texture, this stores references of where said texture can be found in the texture array
	private CompoundTexture[] textureInfo;
	
	// GL ids for VAO and buffer that stores indices of block textures
	private int vao;
	private int blockBuffer;
	
	public ChunkRenderEngine() {
		blockTextures = new GLTextureArray(World.BLOCK_SIZE, World.BLOCK_SIZE);
		textureInfo = new CompoundTexture[Statics.BLK_TEX_CNT];
		backdrops = new GLTexture[Statics.BDP_TEX_CNT];
				
		initShaderProgram();
		initVAO();
	}
	
	private void initShaderProgram() {
		program = new GLProgram();
		
		try {
			program.loadShader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER);
			program.loadShader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		program.link();
	}
	
	private void initVAO() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		int s = Chunk.SIZE;
		
		// For every coordinate in the vertex array there are VERTEX_PARAMS parameters. Every block has 4 vertices.
		float[] data = new float[s*s*VERTEX_PARAMS*4];
		int[] indices = new int[s*s*6];
		
		int[] blockData = new int[s*s*4];
		
		for (int cx = 0; cx < s; cx++) {
			for (int cy = 0; cy < s; cy++) {
				// The parameters
				
				// The coordinate that block relative to the chunk space
				float[] xs = new float[] {cx*World.BLOCK_SIZE + World.BLOCK_SIZE, cx*World.BLOCK_SIZE + World.BLOCK_SIZE, cx*World.BLOCK_SIZE, cx*World.BLOCK_SIZE};
				float[] ys = new float[] {cy*World.BLOCK_SIZE, cy*World.BLOCK_SIZE + World.BLOCK_SIZE, cy*World.BLOCK_SIZE + World.BLOCK_SIZE, cy*World.BLOCK_SIZE};
				
				// The coordinates of the texture that the block will be referencing (this is the same for all blocks)
				float[] txs = new float[] {1, 1, 0, 0};
				float[] tys = new float[] {1, 0, 0, 1};
				
				// The coordinates of the background texture that the block will be referencing
				float[] bxs = new float[] {(cx + 1)/(s*1.0f), (cx + 1)/(s*1.0f), cx/(s*1.0f), cx/(s*1.0f)};
				float[] bys = new float[] {cy/(s*1.0f), (cy + 1)/(s*1.0f), (cy + 1)/(s*1.0f), cy/(s*1.0f)};
				
				int i = (s*cx + cy);
				int id = i * VERTEX_PARAMS * 4;
				int ic = 4*i;
				
				// The indices of the vertices in the form of 2 triangles as per OpenGL's request
				int[] ind = new int[] {ic, ic + 1, ic + 3, ic + 1, ic + 2, ic + 3};
				
				for (int j = 0; j < 4; j++) {
					int k = id + j*6;
					    data[k] = xs[j];
					data[k + 1] = ys[j];
					data[k + 2] = txs[j];
					data[k + 3] = tys[j];
					data[k + 4] = bxs[j];
					data[k + 5] = bys[j];
				}
				
				int ii = i * 6;
				
				for (int j = 0; j < ind.length; j++) {
					indices[ii + j] = ind[j];
				}
			}
		}
		
		//System.out.println(Arrays.toString(data));
		
		int attrBuff = glGenBuffers();
		blockBuffer = glGenBuffers();
		
		int indexBuff = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, attrBuff);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuff);
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		
		// Main coordinates
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES*6, 0);
		
		// Texture coordinates
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES*6, Float.BYTES*2);
		
		// Background coordinates
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, Float.BYTES*6, Float.BYTES*4);
				
		// Block ids
		glBindBuffer(GL_ARRAY_BUFFER, blockBuffer);
		glBufferData(GL_ARRAY_BUFFER, blockData, GL_STREAM_DRAW);
		glEnableVertexAttribArray(3);
		glVertexAttribIPointer(3, 1, GL_INT, Integer.BYTES, 0);
	}
	
	public void bufferBlockData(Block[][] data) { 
		glBindVertexArray(vao);
		int s = Chunk.SIZE;
		
		int[] blockData = new int[s*s*4];
		
		for (int x = 0; x < s; x++) {
			for (int y = 0; y < s; y++) {
				for (int i = 0; i < 4; i++) {
					int ic = (x*s + y)*4; 
					
					blockData[ic + i] = find(data[x][y]);
				}	
			}
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, blockBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, blockData);
	}
	
	private int find(Block b) {
		if (b.getID() == 0) {
			return -1;
		}
		
		return textureInfo[b.getID()].getSub(b.getSubID());		
	}
	
	public void registerTexture(int id, CompoundTexture texture) {
		textureInfo[id] = texture;
		texture.load(blockTextures);
	}
	
	public void registerBackdrop(int id, String file) {
		backdrops[id] = new GLTexture(file);
	}
	
	public void drawChunk(int x, int y, int backdrop, Block[][][] data) {
		glBindVertexArray(vao);
		program.use();
		
		
		program.setUniformTranslationMatrix("offset", x*Chunk.P_SIZE, y*Chunk.P_SIZE);
		camera.setUniforms(program);
		
		backdrops[backdrop].use(0, program, "backdrop");
		blockTextures.use(1, program, "block");
		
		// There is so definitely a better way to do this but this works fine for now
		program.setUniform("drawBackground", 1);
		bufferBlockData(data[0]);
		glDrawElements(GL_TRIANGLES, Chunk.SIZE*Chunk.SIZE*6, GL_UNSIGNED_INT, 0);
		program.setUniform("drawBackground", 0);
		bufferBlockData(data[1]);
		glDrawElements(GL_TRIANGLES, Chunk.SIZE*Chunk.SIZE*6, GL_UNSIGNED_INT, 0);
	}
	
	public void setCamera(Camera c) {
		camera = c;
	}
	
	public void load() {
		blockTextures.load();
		for (int i = 0; i < backdrops.length; i++) {
			if (backdrops[i] != null) {
				backdrops[i].load();
			}
		}
	}
	
	public static ChunkRenderEngine getInstance() {
		if (loaded) {
			return instance;
		} else {
			throw new IllegalStateException("Must call ChunkRenderEngine.create()");
		}
	}
	
	public static void create() {
		if (!loaded) {
			instance = new ChunkRenderEngine();
			loaded = true;
		} else {
			throw new IllegalStateException("ChunkRenderEngine already created");
		}
	}
}
