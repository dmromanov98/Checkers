#version 330 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec2 TexCoord;
  
uniform mat4 projection;
uniform mat4 view;
  
uniform vec4 absolute_position; //vec3 pos + float angle
uniform vec2 scale; 

void main()
{
	float sin_res = sin(absolute_position.w); float cos_res = cos(absolute_position.w);
	
    vec4 general_pos = vec4( absolute_position.x + scale.x*(position.x*cos_res - position.y*sin_res), 
						     absolute_position.y + scale.y*(position.x*sin_res + position.y*cos_res),
						     absolute_position.z + position.z,
						     1.0f );
						
	gl_Position = projection * view * general_pos;
						
    TexCoord = vec2(texCoord.x, 1.0f - texCoord.y);
} 