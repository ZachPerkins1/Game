#version 450
#include "fragment_template.glsl"

in vec2 texCoord;
uniform sampler2D tex;
out vec4 FragColor;

void main() {
	if (withinBounds()) FragColor = texture(tex, texCoord);
	else FragColor = vec4(0, 0, 0, 0);
	// FragColor = vec4(128, 0, 0, 1);
}
