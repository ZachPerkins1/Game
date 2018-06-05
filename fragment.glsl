#version 450

out vec4 FragColor;

in vec2 texCoord;
in vec2 backCoord;
flat in int blockID;

uniform int hasBlock = 0;
uniform int drawBackground = 0;

uniform sampler2D backdrop;
uniform sampler2DArray block;

void main() {
	vec4 backColor;
	if (drawBackground == 1)
		backColor = texture(backdrop, backCoord);
	else
		backColor = vec4(0, 0, 0, 0);


	if (blockID >= 0) {
		vec4 blockColor = texture(block, vec3(texCoord, blockID));
		FragColor = mix(backColor, blockColor, blockColor.a);
	} else {
		FragColor = backColor;
	}
}
