/*
* Chuyển đổi bởi  - Giữ nguyên toàn bộ Uniforms và Samplers của VV
* Tích hợp logic vị trí từ bản 
*/

[span_1](start_span)// Input và Output của Vibrant Visuals[span_1](end_span)
$input a_color0, a_position, a_texcoord0
#ifdef INSTANCING__ON
$input i_data1, i_data2, i_data3
#endif
$output v_color0, v_ndcPosition, v_texcoord0, v_worldPos, v_extraPos

#include <bgfx_shader.sh>

[span_2](start_span)// --- GIỮ NGUYÊN TOÀN BỘ UNIFORMS CỦA VV ---[span_2](end_span)
uniform mat4 PointLightProj;
uniform vec4 ShadowBias;
uniform vec4 PointLightShadowParams1;
uniform vec4 ShadowSlopeBias;
uniform mat4 DirectionalLightSourceShadowInvProj3[2];
uniform vec4 FirstPersonPlayerShadowsEnabledAndResolutionAndFilterWidthAndTextureDimensions;
uniform vec4 DirectionalLightSourceWorldSpaceDirection[2];
uniform mat4 DirectionalLightSourceInvWaterSurfaceViewProj[2];
uniform vec4 BlockBaseAmbientLightColorIntensity;
uniform vec4 PointLightAttenuationWindowEnabled;
uniform vec4 ManhattanDistAttenuationEnabled;
uniform vec4 CascadeShadowResolutions;
uniform vec4 ClusterNearFarWidthHeight;
uniform vec4 CameraLightIntensity;
uniform vec4 CausticsParameters;
uniform vec4 CausticsTextureParameters;
uniform vec4 WorldOrigin;
uniform mat4 CloudShadowProj;
uniform vec4 ClusterDimensions;
uniform vec4 ShadowQuantizationParameters;
uniform vec4 ClusterSize;
uniform vec4 PreExposureEnabled;
uniform vec4 DiffuseSpecularEmissiveAmbientTermToggles;
uniform mat4 DirectionalLightSourceCausticsViewProj[2];
uniform vec4 DirectionalLightSourceDiffuseColorAndIlluminance[2];
uniform vec4 DirectionalLightSourceIsSun[2];
uniform vec4 PointLightDiffuseFadeOutParameters;
uniform vec4 DirectionalLightSourceShadowCascadeNumber[2];
uniform vec4 DirectionalLightSourceShadowDirection[2];
uniform mat4 DirectionalLightSourceShadowInvProj0[2];
uniform vec4 ShadowFilterOffsetAndRangeFarAndMapSize;
uniform mat4 DirectionalLightSourceShadowInvProj1[2];
uniform mat4 DirectionalLightSourceShadowInvProj2[2];
uniform mat4 DirectionalLightSourceShadowProj0[2];
uniform mat4 DirectionalLightSourceShadowProj1[2];
uniform mat4 DirectionalLightSourceShadowProj2[2];
uniform vec4 LightDiffuseColorAndIlluminance;
uniform mat4 DirectionalLightSourceShadowProj3[2];
uniform mat4 DirectionalLightSourceWaterSurfaceViewProj[2];
uniform vec4 DirectionalLightToggleAndCountAndMaxDistanceAndMaxCascadesPerLight;
uniform vec4 DirectionalLightWaterExtinctionEnabledAndWaterDepthMapCascadeIndex;
uniform vec4 DirectionalShadowModeAndCloudShadowToggleAndPointLightToggleAndShadowToggle;
uniform vec4 EmissiveMultiplierAndDesaturationAndCloudPCFAndContribution;
uniform vec4 LightWorldSpaceDirection;
uniform vec4 MaterialID;
uniform mat4 PlayerShadowProj;
uniform vec4 PointLightAttenuationWindow;
uniform vec4 PointLightSpecularFadeOutParameters;
uniform vec4 ShadowPCFWidth;
uniform vec4 SkyAmbientLightColorIntensity;
uniform vec4 SkyProbeUVFadeParameters;
uniform vec4 StarsColor;
uniform vec4 SubsurfaceScatteringContributionAndDiffuseWrapValueAndFalloffScale;
uniform vec4 Time;
uniform vec4 VolumeDimensions;
uniform vec4 VolumeNearFar;
uniform vec4 VolumeScatteringEnabledAndPointLightVolumetricsEnabled;
uniform vec4 WaterExtinctionCoefficients;

[span_3](start_span)// --- GIỮ NGUYÊN CÁC SAMPLER ---[span_3](end_span)
SAMPLER2D_AUTOREG(s_CausticsTexture);
SAMPLER2DARRAY_AUTOREG(s_PointLightShadowTextureArray);
SAMPLER2D_AUTOREG(s_PreviousFrameAverageLuminance);
SAMPLER2DARRAY_AUTOREG(s_ScatteringBuffer);
SAMPLER2DARRAY_AUTOREG(s_ShadowCascades);

struct VertexInput {
    vec4 color0;
    vec3 position;
    vec2 texcoord0;
};

struct VertexOutput {
    vec4 position;
    vec4 color0;
    vec3 ndcPosition;
    vec2 texcoord0;
    vec3 worldPos;
};

[span_4](start_span)// Hàm xử lý vị trí và NDC[span_4](end_span)
void VertForwardPBRTransparent(VertexInput vertInput, inout VertexOutput vertOutput) {
    vec4 clipPosition = vertOutput.position;
    vertOutput.ndcPosition = clipPosition.xyz / clipPosition.w;
}

[span_5](start_span)// Chuyển đổi ma trận theo phong cách VV[span_5](end_span)
void StandardTemplate_VertSharedTransform(VertexInput vertInput, inout VertexOutput vertOutput) {
    vec3 wpos = mul(u_model[0], vec4(vertInput.position, 1.0)).xyz;
    vertOutput.position = mul(u_viewProj, vec4(wpos, 1.0));
    vertOutput.worldPos = wpos;
}

void main() {
    VertexInput vertexInput;
    VertexOutput vertexOutput;

    vertexInput.color0 = a_color0;
    vertexInput.position = a_position;
    vertexInput.texcoord0 = a_texcoord0;

    [span_6](start_span)// Kích hoạt biến extra cho logic 1.18[span_6](end_span)
    v_extraPos = a_position.xyz; 

    [span_7](start_span)// Chạy pipeline chính của VV[span_7](end_span)
    StandardTemplate_VertSharedTransform(vertexInput, vertexOutput);
    VertForwardPBRTransparent(vertexInput, vertexOutput);

    [span_8](start_span)// Gán dữ liệu ra Fragment[span_8](end_span)
    v_color0 = a_color0;
    v_texcoord0 = a_texcoord0;
    v_ndcPosition = vertexOutput.ndcPosition;
    v_worldPos = vertexOutput.worldPos;
    gl_Position = vertexOutput.position;
}
