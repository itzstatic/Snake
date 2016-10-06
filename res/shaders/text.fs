#version 330

in DATA {
	vec2 tcs;
} data;

layout (location = 0) out vec4 color;

uniform sampler2D tex;

void main()
{
	color = texture(tex, data.tcs);
}