package $pkg

<% if (requiresImport(inputEventClass)) { %>import $inputEventClass<% } %>
<% if (requiresImport(outputEventClass)) { %>import $outputEventClass<% } %>
import io.micronaut.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification

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
            $outputEventClassSimple expectedOutput = ${newEventString(outputEventClassSimple)}
        when:
            $outputEventClassSimple output = service.handle(event)
        then:
            thrown UnsupportedOperationException

            // TODO: verify output
            // output == expectedOutput
    }

}