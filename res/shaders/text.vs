#version 330

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 tcs;

out DATA {
	vec2 tcs;
} data;

uniform mat4 proj;
uniform mat4 model;

void main()
{
	gl_Position = proj * model * vec4(pos, 0.0, 1.0);
	data.tcs = tcs;
}