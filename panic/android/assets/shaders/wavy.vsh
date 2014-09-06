attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 waveData;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
	v_color = a_color;
    v_texCoords = a_texCoord0;
    vec4 newPos = vec4(a_position.x + waveData.y * sin(waveData.x + a_position.x), a_position.y + waveData.y * cos(waveData.x + 2.0 * a_position.x), a_position.zw);
    //vec4 newPos = a_position;
    
    gl_Position = u_projTrans * newPos;
}