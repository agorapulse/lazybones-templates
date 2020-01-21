package $pkg

<% if (requiresImport(inputEventClass)) { %>import $inputEventClass<% } %>
<% if (requiresImport(outputEventClass)) { %>import $outputEventClass<% } %>
import spock.lang.AutoCleanup
import spock.lang.Specification

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