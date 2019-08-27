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

out vec2 pixelPos;
