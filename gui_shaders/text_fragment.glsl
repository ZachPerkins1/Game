#version 450

uniform sampler2D texture;
in vec2 texCoord;

uniform vec4 color;
out vec4 FragColor;

void main() {
	FragColor = vec4(color.rgb, texture(texture, texCoord).a);
}
