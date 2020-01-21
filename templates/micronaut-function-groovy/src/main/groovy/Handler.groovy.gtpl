package $pkg

<% if (requiresImport(inputEventClass)) { %>import $inputEventClass<% } %>
<% if (requiresImport(outputEventClass)) { %>import $outputEventClass<% } %>
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import io.micronaut.function.executor.FunctionInitializer
import io.micronaut.function.FunctionBean

import javax.inject.Inject
import java.util.function.Function

@CompileStatic
@FunctionBean(
        value = '$functionNameHyphens',
        method = 'apply'
)
@InheritConstructors
class ${functionName}Handler extends FunctionInitializer implements Function<$inputEventClassSimple, $outputEventClassSimple> {

    @Inject ${functionName}Service ${functionNameProperty}Service

    @Override
    $outputEventClassSimple apply($inputEventClassSimple event) {
         return ${functionNameProperty}Service.handle(event)
    }

    /**
     * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar
     * where the argument to echo is the JSON to be parsed.
     */
    static void main(String...args) throws IOException {
        ${functionName}Handler function = new ${functionName}Handler()
        function.run(args, { context -> function.apply(context.get(${inputEventClassSimple}.class)) })
    }
}