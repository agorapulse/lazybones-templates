package $pkg

<%
    Set<String> imports = [
        'groovy.transform.InheritConstructors',
        'io.micronaut.function.executor.FunctionInitializer',
        'io.micronaut.function.FunctionBean',
        'groovy.transform.CompileStatic',
        'javax.inject.Inject',
        'java.util.function.Function',
    ] as TreeSet
    if (requiresImport(inputEventClass)) { imports << inputEventClass }
    if (requiresImport(outputEventClass)) { imports << outputEventClass }
    for (i in imports) {
        out << 'import ' << i << '\n'
    }
%>
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