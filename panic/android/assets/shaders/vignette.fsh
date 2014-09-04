uniform sampler2D u_texture;
uniform vec2 u_resolution;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
        vec4 color = texture2D(u_texture, v_texCoords) * v_color;
        vec2 relPos = gl_FragCoord.xy / u_resolution - 0.5;
        float vi = smoothstep(1.0, 0.0, length(relPos));
        color.rgb = mix(color.rgb, color.rgb * vec3(vi * 2.0, vi * 2.0, vi), 1.0);
        gl_FragColor = color;
}