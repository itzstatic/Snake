#version 330

in DATA {
	vec4 color;
	vec2 tcs;
} data;

layout (location = 0) out vec4 color;

uniform sampler2D tex;

void main()
{
	color = data.color * texture(tex, data.tcs);		
}