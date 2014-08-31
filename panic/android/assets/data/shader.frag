uniform sampler2D u_texture;
uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb;
        vec3 grayscale = vec3((color.r + color.g) / 2.0);
        vec3 ambient = vec3(1.0, 1.0, 1.0);
        float blu = min(grayscale.r, color.b);
        gl_FragColor = vec4(color.rgb, 1);
}