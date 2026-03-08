$input v_texcoord0

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

float luminanceToEV100(float luminance) {
    return log2(luminance) + 3.0f;
}

vec3 RRTAndODTFit(vec3 v) {
    vec3 a = v * (v + 0.0245786) - 0.000090537;
    vec3 b = v * (0.983729 * v + 0.4329510) + 0.238081;
    return a / b;
}

vec3 ACESFitted(vec3 rgb) {
    mat3 ACESInputMat = mtxFromRows(
        vec3(0.59719, 0.35458, 0.04823),
        vec3(0.07600, 0.90834, 0.01566),
        vec3(0.02840, 0.13383, 0.83777)
    );
    mat3 ACESOutputMat = mtxFromRows(
        vec3(1.60475, - 0.53108, - 0.07367),
        vec3(-0.10208, 1.10813, - 0.00605),
        vec3(-0.00327, - 0.07276, 1.07602)
    );
    rgb = mul(ACESInputMat, rgb);
    rgb = RRTAndODTFit(rgb);
    rgb = mul(ACESOutputMat, rgb);
    rgb = clamp(rgb, 0.0, 1.0);
    return rgb;
}

void main() {
    vec3 sceneColor = texture2D(s_ColorTexture, v_texcoord0.xy).rgb;
    if (TonemapParams0.z > 0.0)
    {
        sceneColor /= (0.18 / texture2D(s_PreExposureLuminance, vec2_splat(0.5)).x) + 0.0001;
    }
    
    float averageLuminance;
    if (ExposureCompensation.z > 0.5)
    {
        averageLuminance = clamp(texture2D(s_AverageLuminance, vec2_splat(0.5)).x, LuminanceMinMaxAndWhitePointAndMinWhitePoint.x, LuminanceMinMaxAndWhitePointAndMinWhitePoint.y);
    }
    else
    {
        averageLuminance = 0.18;
    }
    
    int exposureCurveType = int(ExposureCompensation.x);
    float compensation = ExposureCompensation.y;
    if (exposureCurveType > 0 && exposureCurveType < 2)
    {
        compensation = 1.03 - (2.0 / (((1.0f / log(10.0f)) * log(averageLuminance + 1.0)) + 2.0));
    }
    else if (exposureCurveType > 1)
    {
        float xCoord;
        if (LuminanceMinMaxAndWhitePointAndMinWhitePoint.x == LuminanceMinMaxAndWhitePointAndMinWhitePoint.y)
        {
            xCoord = 0.5;
        }
        else
        {
            xCoord = (luminanceToEV100(averageLuminance) - luminanceToEV100(LuminanceMinMaxAndWhitePointAndMinWhitePoint.x)) / (luminanceToEV100(LuminanceMinMaxAndWhitePointAndMinWhitePoint.y) - luminanceToEV100(LuminanceMinMaxAndWhitePointAndMinWhitePoint.x));
        }
        compensation = texture2D(s_CustomExposureCompensation, vec2(xCoord, 0.5)).x;
    }
    
    float toneMappedAverageLuminance = 0.18f;
    float exposure = (toneMappedAverageLuminance / averageLuminance) * compensation;
    
    sceneColor = saturate(pow(ACESFitted(exposure * sceneColor), vec3_splat(1.0 / 2.2)));
    
    if (RasterizedColorEnabled.x > 0.0)
    {
        vec4 rasterized = texture2D(s_RasterizedColor, v_texcoord0.xy);
        sceneColor *= 1.0 - rasterized.a;
        sceneColor += rasterized.rgb;
    }
    
    gl_FragColor = vec4(sceneColor, 1);
}
