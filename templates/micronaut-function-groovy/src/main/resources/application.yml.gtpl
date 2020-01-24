micronaut:
  function:
    name: $functionNameHyphens
<% for (lib in selectedLibs) { if (lib.configuration) { %>
---
# Added by $lib.name
${toYaml(lib.configuration)}
<% }} %>