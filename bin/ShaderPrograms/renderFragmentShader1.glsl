#version 430 core

in vec3 pass_position;
in vec3 pass_normal;

out vec4 out_color;

uniform vec3 lightPosition;
uniform vec3 lightColor;
uniform float ambient;

uniform vec3 albedo;

void main() {
	vec3 unitNormal = normalize(pass_normal);
	vec3 unitToLight = normalize(lightPosition - pass_position);
	float brightness = max(ambient, dot(unitNormal, unitToLight));
	vec3 diffuse = brightness * lightColor;
	out_color = vec4(diffuse, 1.0) * vec4(albedo, 1.0);
}
