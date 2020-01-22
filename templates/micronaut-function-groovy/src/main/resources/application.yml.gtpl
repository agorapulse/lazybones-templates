micronaut:
  function:
    name: $functionNameHyphens
<% for (lib in selectedLibs) { if (lib.configuration) { %>
---
${toYaml(lib.configuration)}
<% }} %>