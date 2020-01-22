package $pkg

<%
    Set<String> imports = [
        'spock.lang.AutoCleanup',
        'spock.lang.Specification',
    ] as TreeSet
    if (requiresImport(inputEventClass)) { imports << inputEventClass }
    if (requiresImport(outputEventClass)) { imports << outputEventClass }
    for (i in imports) {
        out << 'import ' << i << '\n'
    }
%>
class ${functionName}HandlerSpec extends Specification {

    ${functionName}Service service = Mock(${functionName}Service)

    @AutoCleanup ${functionName}Handler handler = new ${functionName}Handler()

    void setup() {
        handler.${functionNameProperty}Service = service
    }

    void 'handle event'() {
        given:
            $inputEventClassSimple event = ${newEventString(inputEventClassSimple)}
            $outputEventClassSimple expectedOutput = ${newEventString(outputEventClassSimple)}
        when:
            $outputEventClassSimple output = handler.apply(event)
        then:
            output.is expectedOutput

            1 * service.handle(event) >> expectedOutput
    }

}