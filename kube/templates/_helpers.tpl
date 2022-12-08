{{/* Generate labels for config map */}}
{{- define "config-map-chart.labels" }}
  labels:
    version: {{ .Chart.Version }}
    date: {{ now | htmlDate }}
{{- end }}