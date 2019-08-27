#version 450
#include "fragment_template.glsl"

uniform vec4 color;
out vec4 FragColor;

void main() {
	if (withinBounds())
		FragColor = color;
	else
		FragColor = vec4(0, 0, 0, 0);
}
