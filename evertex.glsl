#version 450

layout(location = 0) in vec2 pos;
layout(location = 1) in vec2 inTexCoord;

mat4 projection = mat4(
			0.0025, 0, 0, 0,
			0, -0.0025, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);

uniform mat4 camera;
uniform mat4 offset;

out vec2 texCoord;

void main() {
	gl_Position = projection * camera * offset * vec4(pos, 0, 1);

	texCoord = inTexCoord;
}
