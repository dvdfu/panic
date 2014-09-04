uniform sampler2D u_texture;
uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
        vec4 color = texture2D(u_texture, v_texCoords);
        gl_FragColor = vec4(color.rgb, 1) * v_color;
}