#version 450

#include "vertex_template.glsl"

layout(location = 0) in vec2 pos;
uniform vec2 offset = vec2(0, 0);

void main() {
	pixelPos = pos + offset;
	gl_Position = transform * projection * vec4(pos + offset, 0, 1);
}
