#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D Mask;
uniform sampler2D PrevFb;
uniform float time;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
//    float flag = step(0.1, texture(Mask, texCoord).r);
//    if (flag == 0) {
//        fragColor = texture(DiffuseSampler, texCoord);
//        return;
//    }
//    fragColor = texture(PrevFb, texCoord);

    float sine = step(sin(time/50), texCoord.x);
    if (sine <= 0) {
        float flag = step(0.1, texture(Mask, texCoord).r);
        if (flag == 0) {
            fragColor = texture(DiffuseSampler, texCoord);
            return;
        }
        fragColor = texture(PrevFb, texCoord);
        return;
    }
    float v = step(0.1, texture(Mask, texCoord).r);
    fragColor = vec4(v, v, v, 1);
//    fragColor = vec4(step(0.01, texture(Mask, texCoord).b));
//    vec4 colour = texture(Mask, texCoord);
//    if (colour.r == colour.b && colour.b == colour.g) {
//        fragColor = vec4(1);
//        return;
//    }
//    fragColor = vec4(0,0,0,1);
}