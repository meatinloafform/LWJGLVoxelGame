#version 450 core

in vec3 vertex_color;

out vec4 out_color;

uniform sampler2D textureSampler;

void main() {
	out_color = vec4(vertex_color, 1.0);
	//out_color = vec4(1.0, 0.0, 0.0, 1.0);
}