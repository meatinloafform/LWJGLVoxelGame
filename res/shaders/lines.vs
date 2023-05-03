#version 450 core

in vec3 position;

out vec3 vertex_color;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 color;

void main() {
	gl_Position = projection * view * model * vec4(position, 1.0);
	vertex_color = color;
}
