uniform vec3 inputRotation;
uniform vec2 resolution;
//float time = 1.0;
uniform float time;

float PI = 3.14159265358979323846264;

vec3 genTex( in vec2 p )
{
    float c1 = float(mod(floor(p.x / 20.0), 2.0));
    float c2 = float(mod(floor(p.y / 20.0), 2.0));

    return vec3(c1,0,c2);
}


vec3 genTex2( in vec2 p1 )
{
    vec2 p = mod(p1, PI * 2.0);
    float a = sin(p.y);
    float b = sin(p.y);
    


    
    vec3 c = vec3(1.0);
    
    c.x  = (sin(1.1*time) *sin( 0.5*a) + sin(1.5 * time) * sin(0.7*b)) * 0.1 +1.0;
    c.y  = (sin(1.2*time) * sin(0.4*a) + sin(1.3 * time) *sin(0.6*b)) * 0.1 +0.95;
    c.z  = (sin(1.3*time) *sin( 0.45*a) + sin(1.45 * time) *sin(0.64*b)) * 0.15 +0.85;
    
    c.x  = (sin(1.1*time + 0.5*a) * sin(1.5 * time + 0.7*b)) * 0.1 +1.0;
    c.y  = (sin(1.2*time * 0.4*a) * sin(1.3 * time + 0.6*b)) * 0.1 +0.95;
    c.z  = (sin(1.3*time *  0.45*a) * sin(1.45 * time + 0.64*b)) * 0.05 +0.85;
    
    
    
    
    
    
    
    //c.x = 
    //c.y = 0.5;
    //c.z = 0.5;
    
    return c; 

}


vec3 deform( in vec2 p )
{
    vec3 uv;
    

    vec2 q = vec2( sin(1.1*time+p.x),sin(1.2*time+p.y) );

    float a = atan(q.y,q.x);
    float r = sqrt(dot(q,q));

    uv.x = sin(0.0+1.0*time)+p.x*sqrt(r*r+1.0);
    uv.y = sin(0.6+1.1*time)+p.y*sqrt(r*r+1.0);
    uv.z = 0.0;

    uv = mod(uv, PI * 2.0);

    //return vec3(uv.x, uv.y, uv.z);
    return genTex2(uv.xy);
}


void main()
{

    float perspective = 45.0*PI/180.0;
    float ratio = resolution.x/resolution.y;
    float cameraX = inputRotation.z*PI/180.0;
    float cameraY = inputRotation.x*PI/180.0;

    vec2 alpha = vec2(0,0);
    vec2 camera = vec2(cameraX, cameraY);

    if(int(gl_FragCoord.x) > int(resolution.x / 2.0)) {
        alpha.x = atan(tan(ratio * perspective) * (gl_FragCoord.x-resolution.x / 2.0)  / resolution.x);
    }
    
    if(int(gl_FragCoord.x) < int(resolution.x / 2.0)) {
        alpha.x = - atan(tan(ratio * perspective) * (resolution.x / 2.0 - gl_FragCoord.x) / resolution.x);
    }
    
    
    
    
    if(int(gl_FragCoord.y) > int(resolution.y / 2.0)) {
        alpha.y = -atan(tan(perspective) * (gl_FragCoord.y-resolution.y / 2.0)  / resolution.y);
    }
    
    if(int(gl_FragCoord.y) < int(resolution.y / 2.0)) {
        alpha.y = atan(tan(perspective) * (resolution.y / 2.0 - gl_FragCoord.y) / resolution.y);
    }
    
    //vec2 gAlpha = vec2(alpha.x + cameraX, alpha.y + );
    //alpha.x += cameraX;
    //alpha.y += cameraY;
    
    //alpha = mod(alpha, PI * 2.0);
    

    vec2 p = alpha;
    vec2 s = p;

    vec3 total = vec3(0.0);
    vec2 d = (vec2(0.0,0.0)-p)/40.0;
    float w = 1.0;
    for( int i=0; i<40; i++ )
    {
        vec3 res = genTex2(s+camera);
        res = smoothstep(0.1,1.0,res*res);
        total += w*res;
        w *= .99;
        s += d;
    }
    total /= 40.0;
    float r = 1.5/(1.0+dot(p,p));
    gl_FragColor = vec4( total*r,1.0);

    //gl_FragColor = vec4( genTex2(p+camera),1.0);
}
