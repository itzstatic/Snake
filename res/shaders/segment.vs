#version 330

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 tcs;

out DATA {
	vec4 color;
	vec2 tcs;
} data;

uniform vec3 color;
uniform mat4 model;
uniform mat4 proj;

void main()
{
	gl_Position = proj * model * vec4(pos, 0.0, 1.0);
	data.tcs = tcs;		
	data.color = vec4(color, 1.0);
}