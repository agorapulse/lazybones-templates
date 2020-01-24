package $pkg

<%
    Set<String> imports = [
        'spock.lang.AutoCleanup',
        'spock.lang.Specification',
        'io.micronaut.context.ApplicationContext',
    ] as TreeSet
    if (requiresImport(inputEventClass)) { imports << inputEventClass }
    if (requiresImport(outputEventClass)) { imports << outputEventClass }
    for (i in imports) {
        out << 'import ' << i << '\n'
    }
%>
class ${functionName}ServiceSpec extends Specification {

    @AutoCleanup ApplicationContext context

    ${functionName}Service service

    void setup() {
        context = ApplicationContext.build('test').build()
        // TODO: register mock collaborators
        // context.registerSingleton(Foo, fooMock)
        context.start()

        service = context.getBean(${functionName}Service)
    }

    void 'handle event'() {
        given:
            $inputEventClassSimple event = ${newEventString(inputEventClassSimple)}
            // $outputEventClassSimple expectedOutput = ${newEventString(outputEventClassSimple)}
        when:
            /* $outputEventClassSimple output = */ service.handle(event)
        then:
            thrown UnsupportedOperationException

            // TODO: verify output
            // output == expectedOutput
    }

}
