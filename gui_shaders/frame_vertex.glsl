#version 450

layout(location = 0) in vec2 pos;

mat4 projection = mat4(
			0.0025, 0, 0, 0,
			0, -0.0025, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);

mat4 transform = mat4(
		1, 0, 0, 0,
		0, 1, 0, 0,
		0, 0, 1, 0,
		-1, 1, 0, 1
);


void main() {
	gl_Position = transform * projection * vec4(pos, 0, 1);
}
