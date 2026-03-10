/*
* Chuyển đổi bởi  - Kết hợp hệ thống sao  vào Vibrant Visuals PBR
* - Giữ nguyên Volumetric Fog và Samplers/Uniforms của VV
* - Sử dụng thuật toán lấp lánh noise/rand từ 
*/

// CHÚ Ý: Đã thêm v_extraPos để nhận dữ liệu 'pp' từ Vertex Shader
$input v_color0, v_ndcPosition, v_texcoord0, v_worldPos, v_extraPos

#include <bgfx_shader.sh>

// --- CÁC HÀM NOOP VÀ SAMPLER TEXTURE CỦA VV (GIỮ NGUYÊN) ---
struct NoopSampler { int noop; };
vec4 textureSample(mediump sampler2D _sampler, vec2 _coord) { return texture(_sampler, _coord); }
vec4 textureSample(mediump sampler3D _sampler, vec3 _coord) { return texture(_sampler, _coord); }
vec4 textureSample(mediump samplerCube _sampler, vec3 _coord) { return texture(_sampler, _coord); }
vec4 textureSample(mediump sampler2D _sampler, vec2 _coord, float _lod) { return textureLod(_sampler, _coord, _lod); }
vec4 textureSample(mediump sampler3D _sampler, vec3 _coord, float _lod) { return textureLod(_sampler, _coord, _lod); }
vec4 textureSample(mediump sampler2DArray _sampler, vec3 _coord) { return texture(_sampler, _coord); }
vec4 textureSample(mediump sampler2DArray _sampler, vec3 _coord, float _lod) { return textureLod(_sampler, _coord, _lod); }
vec4 textureSample(mediump samplerCubeArray _sampler, vec4 _coord, float _lod) { return textureLod(_sampler, _coord, _lod); }
vec4 textureSample(mediump samplerCubeArray _sampler, vec4 _coord, int _lod) { return textureLod(_sampler, _coord, float(_lod)); }
vec4 textureSample(NoopSampler noopsampler, vec2 _coord) { return vec4(0, 0, 0, 0); }
vec4 textureSample(NoopSampler noopsampler, vec3 _coord) { return vec4(0, 0, 0, 0); }
vec4 textureSample(NoopSampler noopsampler, vec4 _coord) { return vec4(0, 0, 0, 0); }
vec4 textureSample(NoopSampler noopsampler, vec2 _coord, float _lod) { return vec4(0, 0, 0, 0); }
vec4 textureSample(NoopSampler noopsampler, vec3 _coord, float _lod) { return vec4(0, 0, 0, 0); }
vec4 textureSample(NoopSampler noopsampler, vec4 _coord, float _lod) { return vec4(0, 0, 0, 0); }
struct NoopImage2D { int noop; };
struct NoopImage3D { int noop; };
struct rayQueryKHR { int noop; };
struct accelerationStructureKHR { int noop; };

// --- TOÀN BỘ UNIFORMS CỦA VV ---
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
vec4 ViewRect; mat4 Proj; mat4 View; vec4 ViewTexel; mat4 InvView; mat4 ViewProj; mat4 InvProj; mat4 InvViewProj; mat4 PrevViewProj; mat4 WorldArray[4]; mat4 World; mat4 WorldView; mat4 WorldViewProj; vec4 PrevWorldPosOffset; vec4 AlphaRef4; float AlphaRef;

struct DiscreteLightingContributions { vec3 diffuse; vec3 specular; vec4 ambientTint; };
struct LightData { float lookup; };
struct Light { vec4 position; vec4 color; int shadowProbeIndex; int pad0; int pad1; int pad2; };
struct PBRFragmentInfo { vec2 lightClusterUV; vec3 worldPosition; vec3 viewPosition; vec3 ndcPosition; vec3 worldNormal; vec3 viewNormal; vec3 rf0; vec3 albedo; float metalness; float roughness; float emissive; float subsurface; float blockAmbientContribution; float skyAmbientContribution; vec2 causticsMultiplier; };
struct PBRLightingContributions { vec3 directDiffuse; vec3 directSpecular; vec3 indirectDiffuse; vec3 indirectSpecular; vec3 emissive; };

// --- CẬP NHẬT STRUCT THÊM EXTRA POS TỪ VERTEX ---
struct FragmentInput {
    vec4 color0;
    vec3 ndcPosition;
    vec2 texcoord0;
    vec3 worldPos;
    vec3 extraPos; // Added
};

struct FragmentOutput { vec4 Color0; };

SAMPLER2D_AUTOREG(s_CausticsTexture);
SAMPLER2DARRAY_AUTOREG(s_PointLightShadowTextureArray);
SAMPLER2D_AUTOREG(s_PreviousFrameAverageLuminance);
SAMPLER2DARRAY_AUTOREG(s_ScatteringBuffer);
SAMPLER2DARRAY_AUTOREG(s_ShadowCascades);
BUFFER_RW_AUTOREG(s_LightLookupArray, LightData);
BUFFER_RW_AUTOREG(s_Lights, Light);

struct StandardSurfaceInput {
    vec2 UV;
    vec3 Color;
    float Alpha;
    vec4 color0;
    vec3 ndcPosition;
    vec3 extraPos; // Added
};

StandardSurfaceInput StandardTemplate_DefaultInput(FragmentInput fragInput) {
    StandardSurfaceInput result;
    result.UV = vec2(0, 0);
    result.Color = vec3(1, 1, 1);
    result.Alpha = 1.0;
    result.color0 = fragInput.color0;
    result.ndcPosition = fragInput.ndcPosition;
    result.extraPos = fragInput.extraPos; // Map from FragmentInput
    return result;
}

struct StandardSurfaceOutput { vec3 Albedo; float Alpha; float Metallic; float Roughness; float Occlusion; float Emissive; float Subsurface; vec3 AmbientLight; vec3 ViewSpaceNormal; };

StandardSurfaceOutput StandardTemplate_DefaultOutput() {
    StandardSurfaceOutput result;
    result.Albedo = vec3(1, 1, 1); result.Alpha = 1.0; result.Metallic = 0.0; result.Roughness = 1.0; result.Occlusion = 0.0; result.Emissive = 0.0; result.Subsurface = 0.0; result.AmbientLight = vec3(0.0, 0.0, 0.0); result.ViewSpaceNormal = vec3(0, 1, 0);
    return result;
}

float linearToLogDepth(float linearDepth) { return log((exp(4.0) - 1.0) * linearDepth + 1.0) / 4.0; }
vec3 ndcToVolume(vec3 ndc, mat4 inverseProj, vec2 nearFar) {
    vec2 uv = 0.5 * (ndc.xy + vec2(1.0, 1.0));
    vec4 view = ((inverseProj) * (vec4(ndc, 1.0)));
    float viewDepth = -view.z / view.w;
    float wLinear = (viewDepth - nearFar.x) / (nearFar.y - nearFar.x);
    return vec3(uv, linearToLogDepth(wLinear));
}
vec4 sampleVolume(highp sampler2DArray volume, ivec3 dimensions, vec3 uvw) {
    float depth = uvw.z * float(dimensions.z) - 0.5;
    int index = clamp(int(depth), 0, dimensions.z - 2);
    float offset = clamp(depth - float(index), 0.0, 1.0);
    vec4 a = textureSample(volume, vec3(uvw.xy, index), 0.0).rgba;
    vec4 b = textureSample(volume, vec3(uvw.xy, index + 1), 0.0).rgba;
    return mix(a, b, offset);
}

vec3 PreExposeLighting(vec3 color, float averageLuminance) { return color * (0.18f / averageLuminance + 1e - 4); }

// =======================================================================
// THUẬT TOÁN TẠO SAO TỪ 1.18 ĐƯỢC MANG XUỐNG ĐÂY
// =======================================================================
vec2 rand(vec2 p) {
    p = vec2(dot(p, vec2(12.9898,78.233)), dot(p, vec2(26.65125, 83.054543)));
    return fract(sin(p) * 43758.5453);
}

float noise(vec2 p) {
    return fract(sin(dot(p.xy ,vec2(54.90898,18.233))) * 4337.5453);
}

float stars(in vec2 x, float numCells, float size, float br) {
    vec2 n = x * numCells;
    vec2 f = floor(n);
    float d = 1.0e10;
    for (int i = -1; i <= 1; ++i) {
        for (int j = -1; j <= 1; ++j) {
            vec2 g = f + vec2(float(i), float(j));
            g = n - g - rand(mod(g, numCells)) + noise(g);
            g *= 1. / (numCells * size);
            d = min(d, dot(g, g));
        }
    }
    return br * (smoothstep(.95, 1., (1. - sqrt(d))));
}
// =======================================================================

void FragForwardPBRTransparent(StandardSurfaceInput fragInput, inout vec4 outColor) {
    // 1. Áp dụng logic tính toán sao lấp lánh màu Cyan từ bản 1.18
    vec3 custom118StarColor = vec3(0.0, 1.0, 1.0) * stars(fragInput.extraPos.xz, 9.0, 9.0, 8.0);
    
    // 2. Nhân với StarsColor của VV để giữ hiệu ứng "mờ dần khi bình minh" và Alpha
    vec3 starColor = custom118StarColor * StarsColor.rgb * fragInput.color0.a;

    // 3. Hệ thống Volumetric Sương mù gốc của VV (Không can thiệp để tránh lỗi hiển thị PBR)
    vec3 fogAppliedColor;
    if (VolumeScatteringEnabledAndPointLightVolumetricsEnabled.x != 0.0) {
        vec3 uvw = ndcToVolume(fragInput.ndcPosition, InvProj, VolumeNearFar.xy);
        vec4 sourceExtinction = sampleVolume(s_ScatteringBuffer, ivec3(VolumeDimensions.xyz), uvw);
        fogAppliedColor = sourceExtinction.a * starColor.rgb;
    } else {
        fogAppliedColor = starColor.rgb;
    }
    
    outColor = vec4(fogAppliedColor, fragInput.color0.a);
}

struct CompositingOutput { vec3 mLitColor; };
vec4 standardComposite(StandardSurfaceOutput stdOutput, CompositingOutput compositingOutput) { return vec4(compositingOutput.mLitColor, stdOutput.Alpha); }
void StandardTemplate_FinalColorOverrideIdentity(FragmentInput fragInput, StandardSurfaceInput surfaceInput, StandardSurfaceOutput surfaceOutput, inout FragmentOutput fragOutput) {}
void StandardTemplate_CustomSurfaceShaderEntryIdentity(vec2 uv, vec3 worldPosition, inout StandardSurfaceOutput surfaceOutput) {}
struct DirectionalLight { vec3 ViewSpaceDirection; vec3 Intensity; };
vec3 computeLighting_Unlit(FragmentInput fragInput, StandardSurfaceInput stdInput, StandardSurfaceOutput stdOutput, DirectionalLight primaryLight) { return stdOutput.Albedo; }

#ifdef FORWARD_PBR_TRANSPARENT_PASS
void StarsSurfaceFunction(StandardSurfaceInput fragInput, inout StandardSurfaceOutput surfaceOutput) {
    vec4 fogAppliedColor;
    FragForwardPBRTransparent(fragInput, fogAppliedColor);
    if (PreExposureEnabled.x > 0.0) {
        float exposure = textureSample(s_PreviousFrameAverageLuminance, vec2(0.5, 0.5)).r;
        fogAppliedColor.rgb = PreExposeLighting(fogAppliedColor.rgb, exposure);
    }
    surfaceOutput.Albedo = fogAppliedColor.rgb;
    surfaceOutput.Alpha = fogAppliedColor.a;
}
#endif

#ifdef FORWARD_PBR_TRANSPARENT_SKY_PROBE_PASS
void StarsSkyProbeSurfaceFunction(StandardSurfaceInput fragInput, inout StandardSurfaceOutput surfaceOutput) {
    vec4 fogAppliedColor;
    FragForwardPBRTransparent(fragInput, fogAppliedColor);
    vec2 uv = (fragInput.ndcPosition.xy + vec2(1.0, 1.0)) / 2.0;
    float fadeStart = SkyProbeUVFadeParameters.x;
    float fadeEnd = SkyProbeUVFadeParameters.y;
    float fadeRange = fadeStart - fadeEnd + 1e - 5;
    float fade = (clamp(uv.y, fadeEnd, fadeStart) - fadeEnd) / fadeRange;
    fogAppliedColor *= fade;
    if (PreExposureEnabled.x > 0.0) { fogAppliedColor.rgb = PreExposeLighting(fogAppliedColor.rgb, 1.0); }
    surfaceOutput.Albedo = fogAppliedColor.rgb;
    surfaceOutput.Alpha = max(fogAppliedColor.a, SkyProbeUVFadeParameters.z);
}
#endif

void StandardTemplate_Opaque_Frag(FragmentInput fragInput, inout FragmentOutput fragOutput) {
    StandardSurfaceInput surfaceInput = StandardTemplate_DefaultInput(fragInput);
    StandardSurfaceOutput surfaceOutput = StandardTemplate_DefaultOutput();
    surfaceInput.UV = fragInput.texcoord0;
    surfaceInput.Color = fragInput.color0.xyz;
    surfaceInput.Alpha = fragInput.color0.a;
    
    #ifdef FORWARD_PBR_TRANSPARENT_PASS
    StarsSurfaceFunction(surfaceInput, surfaceOutput);
    #endif
    
    #ifdef FORWARD_PBR_TRANSPARENT_SKY_PROBE_PASS
    StarsSkyProbeSurfaceFunction(surfaceInput, surfaceOutput);
    #endif
    
    StandardTemplate_CustomSurfaceShaderEntryIdentity(surfaceInput.UV, fragInput.worldPos, surfaceOutput);
    DirectionalLight primaryLight;
    vec3 worldLightDirection = LightWorldSpaceDirection.xyz;
    primaryLight.ViewSpaceDirection = ((View) * (vec4(worldLightDirection, 0))).xyz;
    primaryLight.Intensity = LightDiffuseColorAndIlluminance.rgb * LightDiffuseColorAndIlluminance.w;
    
    CompositingOutput compositingOutput;
    compositingOutput.mLitColor = computeLighting_Unlit(fragInput, surfaceInput, surfaceOutput, primaryLight);
    fragOutput.Color0 = standardComposite(surfaceOutput, compositingOutput);
    StandardTemplate_FinalColorOverrideIdentity(fragInput, surfaceInput, surfaceOutput, fragOutput);
}

void main() {
    FragmentInput fragmentInput;
    FragmentOutput fragmentOutput;
    
    // Ánh xạ các biến Input từ Vertex
    fragmentInput.color0 = v_color0;
    fragmentInput.ndcPosition = v_ndcPosition;
    fragmentInput.texcoord0 = v_texcoord0;
    fragmentInput.worldPos = v_worldPos;
    fragmentInput.extraPos = v_extraPos; // NHẬN EXTRA POS (PP) TỪ VERTEX SHADER
    
    fragmentOutput.Color0 = vec4(0, 0, 0, 0);
    ViewRect = u_viewRect; Proj = u_proj; View = u_view; ViewTexel = u_viewTexel; InvView = u_invView; ViewProj = u_viewProj; InvProj = u_invProj; InvViewProj = u_invViewProj; PrevViewProj = u_prevViewProj;
    
    { WorldArray[0] = u_model[0]; WorldArray[1] = u_model[1]; WorldArray[2] = u_model[2]; WorldArray[3] = u_model[3]; }
    World = u_model[0]; WorldView = u_modelView; WorldViewProj = u_modelViewProj; PrevWorldPosOffset = u_prevWorldPosOffset;
    AlphaRef4 = u_alphaRef4; AlphaRef = u_alphaRef4.x;
    
    // Thực thi Shader
    StandardTemplate_Opaque_Frag(fragmentInput, fragmentOutput);
    gl_FragColor = fragmentOutput.Color0;
}
