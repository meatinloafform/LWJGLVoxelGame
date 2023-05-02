#version 450 core

in vec2 uv;

out vec4 out_color;

uniform sampler2D textureSampler;

void main() {
	out_color = texture(textureSampler, uv);
	//out_color = vec4(uv.x, uv.y, 0.0, 1.0);
}