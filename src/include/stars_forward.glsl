/*
* Available Macros:
*
* Passes:
* - TRANSPARENT_PASS (not used)
*
* Instancing:
* - INSTANCING__OFF (not used)
* - INSTANCING__ON (not used)
*/

///////////////////////////////////////////////////////////
// VERTEX SHADER
///////////////////////////////////////////////////////////
#if BGFX_SHADER_TYPE_VERTEX

$input a_color0, a_position
$output v_color0

vec3 instMul(vec3 _vec, mat3 _mtx) {
 return ( (_vec) * (_mtx) );  // Attention!
}
vec3 instMul(mat3 _mtx, vec3 _vec) {
 return ( (_mtx) * (_vec) );  // Attention!
}
vec4 instMul(vec4 _vec, mat4 _mtx) {
 return ( (_vec) * (_mtx) );  // Attention!
}
vec4 instMul(mat4 _mtx, vec4 _vec) {
 return ( (_mtx) * (_vec) );  // Attention!
}
float rcp(float _a) {
 return 1.0/_a; 
}
vec2 rcp(vec2 _a) {
 return vec2(1.0)/_a; 
}
vec3 rcp(vec3 _a) {
 return vec3(1.0)/_a; 
}
vec4 rcp(vec4 _a) {
 return vec4(1.0)/_a; 
}
vec2 vec2_splat(float _x) {
 return vec2(_x, _x); 
}
vec3 vec3_splat(float _x) {
 return vec3(_x, _x, _x); 
}
vec4 vec4_splat(float _x) {
 return vec4(_x, _x, _x, _x); 
}
uvec2 uvec2_splat(uint _x) {
 return uvec2(_x, _x); 
}
uvec3 uvec3_splat(uint _x) {
 return uvec3(_x, _x, _x); 
}
uvec4 uvec4_splat(uint _x) {
 return uvec4(_x, _x, _x, _x); 
}

uniform mat4 u_modelViewProj;
uniform vec4 CURRENT_COLOR;

void main() {
    // Standard Vanilla Stars vertex logic
    gl_Position = instMul(vec4(a_position, 1.0), u_modelViewProj);
    v_color0 = a_color0 * CURRENT_COLOR;
}

#endif




///////////////////////////////////////////////////////////
// FRAGMENT/PIXEL SHADER
///////////////////////////////////////////////////////////
#if BGFX_SHADER_TYPE_FRAGMENT

$input v_color0

vec3 instMul(vec3 _vec, mat3 _mtx) {
 return ( (_vec) * (_mtx) );  // Attention!
}
vec3 instMul(mat3 _mtx, vec3 _vec) {
 return ( (_mtx) * (_vec) );  // Attention!
}
vec4 instMul(vec4 _vec, mat4 _mtx) {
 return ( (_vec) * (_mtx) );  // Attention!
}
vec4 instMul(mat4 _mtx, vec4 _vec) {
 return ( (_mtx) * (_vec) );  // Attention!
}
float rcp(float _a) {
 return 1.0/_a; 
}
vec2 rcp(vec2 _a) {
 return vec2(1.0)/_a; 
}
vec3 rcp(vec3 _a) {
 return vec3(1.0)/_a; 
}
vec4 rcp(vec4 _a) {
 return vec4(1.0)/_a; 
}
vec2 vec2_splat(float _x) {
 return vec2(_x, _x); 
}
vec3 vec3_splat(float _x) {
 return vec3(_x, _x, _x); 
}
vec4 vec4_splat(float _x) {
 return vec4(_x, _x, _x, _x); 
}
uvec2 uvec2_splat(uint _x) {
 return uvec2(_x, _x); 
}
uvec3 uvec3_splat(uint _x) {
 return uvec3(_x, _x, _x); 
}
uvec4 uvec4_splat(uint _x) {
 return uvec4(_x, _x, _x, _x); 
}

void main() {
    gl_FragColor = vec4(v_color0, 1.0);
}

#endif