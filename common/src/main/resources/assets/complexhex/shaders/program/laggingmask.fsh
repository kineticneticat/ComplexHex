#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D Mask;
uniform sampler2D PrevFb;
uniform float time;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
        float flag = step(0.1, texture(Mask, texCoord).r);
        if (flag == 0) {
            fragColor = texture(DiffuseSampler, texCoord);
            return;
        }
        fragColor = texture(PrevFb, texCoord);
//        vec4 pix = texture(PrevFb, texCoord);
//        fragColor = vec4(1-pix.r, 1-pix.g, 1-pix.b, pix.a);
}