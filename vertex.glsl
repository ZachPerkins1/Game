#version 450

layout(location = 0) in vec2 pos;
layout(location = 1) in vec2 inTexCoord;
layout(location = 2) in vec2 inBackCoord;
layout(location = 3) in int inBlock;

uniform mat4 offset;

uniform mat4 camera = mat4(
		1, 0, 0, 0,
		0, 1, 0, 0,
		0, 0, 1, 0,
		0, 0, 0, 1
		);

mat4 projection = mat4(
			0.0025, 0, 0, 0,
			0, -0.0025, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);

out vec2 texCoord;
out vec2 backCoord;
flat out int blockID;

void main() {
	gl_Position = projection * camera * offset * vec4(pos, 0, 1);

	texCoord = inTexCoord;
	backCoord = inBackCoord;
	blockID = inBlock;
}
