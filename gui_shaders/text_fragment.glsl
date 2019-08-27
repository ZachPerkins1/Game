#version 450
#include "fragment_template.glsl"

uniform sampler2D tex;
in vec2 texCoord;

uniform vec4 color = vec4(1,1,1,1);
out vec4 FragColor;

void main() {
	// FragColor = texture(tex, texCoord);
	if (withinBounds())
		FragColor = vec4(color.rgb, (texture(tex, texCoord).a) - (1 - color.a));
	else
		FragColor = vec4(0, 0, 0, 0);
}
