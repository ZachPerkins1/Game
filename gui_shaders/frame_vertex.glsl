#version 450

#include "base.glsl"

layout(location = 0) in vec2 pos;
uniform vec2 offset = vec2(0, 0);

void main() {
	gl_Position = transform * projection * vec4(pos + offset, 0, 1);
}
