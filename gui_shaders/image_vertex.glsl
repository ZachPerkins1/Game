#version 450

#include "vertex_template.glsl"

layout(location = 0) in vec2 pos;
layout(location = 1) in vec2 inTexCoord;

uniform vec2 offset = vec2(0, 0);

out vec2 texCoord;

void main() {
	texCoord = inTexCoord;
	pixelPos = pos + offset;
	gl_Position = transform * projection * vec4(pos + offset, 0, 1);
}
