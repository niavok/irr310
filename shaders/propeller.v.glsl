#version 120
varying vec3 normalV;

void main()
{
    gl_FrontColor = gl_Color;
	normalV = gl_NormalMatrix * gl_Normal;
	gl_Position = ftransform();

}
