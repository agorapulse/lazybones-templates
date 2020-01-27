POST http://localhost:$port/$functionNameHyphens
Content-Type: application/json
Accept: application/json;text/plain

<%
    File eventFile = new File(templateDir, ".lazybones/assist/events/${inputEventClass}.json")
    if (eventFile.exists()) {
        out << eventFile.text
    } else {
        out << '{\n\n}'
    }
%>
