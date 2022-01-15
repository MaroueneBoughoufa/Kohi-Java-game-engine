#type vertex
#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec2 aUV;

uniform mat4 uProjMat;
uniform mat4 uViewMat;

out vec4 fColor;
out vec2 fUV;

void main()
{
    fColor = aColor;
    fUV = aUV;
    gl_Position = uProjMat * uViewMat * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

uniform float uTime;
uniform sampler2D TEX_SAMPLER;

in vec4 fColor;
in vec2 fUV;

out vec4 color;

void main()
{
    // Default:
    //color = fColor;

    // With image:
    //color = texture(TEX_SAMPLER, fUV);

    color = sin(uTime) * texture(TEX_SAMPLER, fUV);

    // Black and white:
    //float avg = (fColor.r + fColor.g + fColor.b) / 3;
    //color = vec4(avg, avg, avg, 1);

    // Blinking:
    //color = sin(uTime) * fColor;

    // Noise:
    //float noise = fract(sin(dot(fColor.xy, vec2(12.9898, 78.233))) * 43758.5453);
    //color = fColor * noise;
}
