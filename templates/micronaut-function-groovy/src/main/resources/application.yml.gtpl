micronaut:
  function:
    name: $functionNameHyphens
<%
    String[] parts = functionNameHyphens.split('-')
    parts.eachWithIndex { it, i ->
        out << (' ' * i) << "$it:" << '\n'
    }
    out << (' ' * parts.length) << 'value: test\n'
%>
<% for (lib in selectedLibs) { if (lib.configuration) { %>
---
# Added by $lib.name
${toYaml(lib.configuration)}
<% }} %>