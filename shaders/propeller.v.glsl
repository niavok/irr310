uniform vec3 lightDir;
varying float intensity;

void main()
{
	intensity = dot(lightDir,gl_Normal);
	gl_Position = ftransform();
}
