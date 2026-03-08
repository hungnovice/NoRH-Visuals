$input a_position, a_texcoord0
$output v_texcoord0

#include "bgfx_shader.sh"

SAMPLER2D_AUTOREG(s_AverageLuminance);
SAMPLER2D_AUTOREG(s_ColorTexture);
SAMPLER2D_AUTOREG(s_CustomExposureCompensation);
SAMPLER2D_AUTOREG(s_PreExposureLuminance);
SAMPLER2D_AUTOREG(s_RasterColor);
SAMPLER2D_AUTOREG(s_RasterizedColor);

uniform vec4 ColorGrading_Contrast_Highlights;
uniform vec4 ColorGrading_Contrast_Midtones;
uniform vec4 ColorGrading_Contrast_Shadows;
uniform vec4 ColorGrading_Gain_Highlights;
uniform vec4 ColorGrading_Gain_Midtones;
uniform vec4 ColorGrading_Gain_Shadows;
uniform vec4 ColorGrading_Gamma_Highlights;
uniform vec4 ColorGrading_Gamma_Midtones;
uniform vec4 ColorGrading_Gamma_Shadows;
uniform vec4 ColorGrading_Misc;
uniform vec4 ColorGrading_Misc2;
uniform vec4 ColorGrading_Offset_Highlights;
uniform vec4 ColorGrading_Offset_Midtones;
uniform vec4 ColorGrading_Offset_Shadows;
uniform vec4 ColorGrading_Saturation_Highlights;
uniform vec4 ColorGrading_Saturation_Midtones;
uniform vec4 ColorGrading_Saturation_Shadows;
uniform vec4 ColorGrading_Temperature_Params;
uniform vec4 ExposureCompensation;
uniform vec4 GenericTonemapperContrastAndScaleAndOffsetAndCrosstalk;
uniform vec4 GenericTonemapperCrosstalkParams;
uniform vec4 LuminanceMinMaxAndWhitePointAndMinWhitePoint;
uniform vec4 RasterizedColorEnabled;
uniform vec4 ScreenSize;
uniform vec4 TonemapParams0;
uniform vec4 ViewportScale;

void main() {
    vec2 uv = a_texcoord0 * ViewportScale.xy;
    v_texcoord0 = vec4(uv, a_texcoord0);
    gl_Position = vec4(2.0 * a_position.xy - 1.0, 0.0, 1.0);
}
