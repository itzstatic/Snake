#version 330

in DATA {
	vec4 color;
} data;

layout (location = 0) out vec4 color;

void main()
{
	color = data.color;
}