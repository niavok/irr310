uniform vec3 inputRotation;
void main()
{
    float x = float(mod(inputRotation.x, 360)) / 360.0;
    float y = float(mod(inputRotation.y, 360)) / 360.0;
    float z = float(mod(inputRotation.z, 360)) / 360.0;

    gl_FragColor = vec4(x , y, z ,1);
}
