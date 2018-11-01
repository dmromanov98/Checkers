#version 330 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec2 TexCoord;
  
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform float scrollSpeed;
  
void main()
{	
	vec4 pos = vec4( 2*position.x, 2*position.y, model[3][2], 1.0f );
	gl_Position = pos;
	
	mat4 myView = view; 
	float maxScreenSide = max(1/projection[0][0], 1/projection[1][1]);
	
	myView[0][0] = 1/projection[0][0]/maxScreenSide/myView[0][0]; 
	myView[1][1] = 1/projection[1][1]/maxScreenSide/myView[1][1];
	myView[3][0] = myView[3][0]*scrollSpeed; myView[3][1] = myView[3][1]*scrollSpeed;
	
	pos = projection * myView * model * vec4(.5f - texCoord.y, - texCoord.x + .5f, pos.zw);
	
    TexCoord = vec2(pos.x/2, pos.y/2);
					
} 