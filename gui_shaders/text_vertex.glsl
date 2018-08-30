#version 450

layout(location=0) in vec2 inTexCoord;
layout(location=1) in vec2 pos;

out vec2 texCoord;

void main() {
	texCoord = inTexCoord;
	gl_Position = pos;
}
