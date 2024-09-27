#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D Mask;
uniform sampler2D PrevFb;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
//    float flag = texture(Mask, texCoord).a;
//    float flag = step(.5, texCoord.x);
//    if (flag == 0) {
//        fragColor = texture(DiffuseSampler, texCoord);
//        return;
//    }
    fragColor = texture(Mask, texCoord);
}