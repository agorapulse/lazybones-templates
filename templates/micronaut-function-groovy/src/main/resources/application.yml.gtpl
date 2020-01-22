micronaut:
  function:
    name: $functionNameHyphens
<% for (lib in selectedLibs) { if (lib.configuration) { %>
---
# Required by $lib.name
${toYaml(lib.configuration)}
<% }} %>