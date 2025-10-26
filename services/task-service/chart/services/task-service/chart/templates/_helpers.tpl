{{- /*
Expand the name of the chart.
*/ -}}
{{- define "task-service.name" -}}
{{- default .Chart.Name .Values.nameOverride -}}
{{- end -}}

{{- /*
Create a default fully qualified app name.
*/ -}}
{{- define "task-service.fullname" -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
