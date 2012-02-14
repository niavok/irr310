#version 120
#extension GL_EXT_gpu_shader4 : enable

noperspective varying vec3 dist;
varying float meshSize;
const vec4 WIRE_COL = vec4(0.7,0.7,0.8,1);
const vec4 FILL_COL = vec4(0.9,0.9,0.9,1);

void main(void)
{
	float d = min(dist[0],min(dist[1],dist[2]));
	float dMax = max(dist[0],max(dist[1],dist[2]));
 	float I = exp2(-2*d*d);

 	gl_FragColor = I*WIRE_COL + (1.0 - I)*FILL_COL;
 	
}

