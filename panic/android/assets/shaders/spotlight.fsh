uniform sampler2D u_texture;
uniform vec2 u_playerPos;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords) * v_color;
	vec2 relPos = gl_FragCoord.xy - u_playerPos;
	float len = smoothstep(2.0, 0.0, length(relPos) / 400.0);
	gl_FragColor = vec4(color.rg * len, color.b * len / 2.0, 1.0);
}