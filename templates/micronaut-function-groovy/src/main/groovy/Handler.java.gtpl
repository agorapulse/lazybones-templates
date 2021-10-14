package $pkg;

<%
    Set<String> imports = [
        'io.micronaut.context.ApplicationContext',
        'io.micronaut.function.executor.FunctionInitializer',
        'io.micronaut.function.FunctionBean',
        'java.io.IOException',
        'java.util.function.Function',
        'javax.inject.Inject',
    ] as TreeSet
    if (requiresImportIntoJavaFile(inputEventClass)) { imports << inputEventClass }
    if (requiresImportIntoJavaFile(outputEventClass)) { imports << outputEventClass }
    for (i in imports) {
        out << 'import ' << i << ';' << '\n'
    }
%>
@FunctionBean(
        value = "$functionNameHyphens",
        method = "apply"
)
public class ${functionName}Handler extends FunctionInitializer implements Function<$inputEventClassSimple, $outputEventClassSimple> {

    /**
     * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar
     * where the argument to echo is the JSON to be parsed.
     */
    public static void main(String...args) throws IOException {
        try (${functionName}Handler function = new ${functionName}Handler()) {
            function.run(args, context -> function.apply(context.get(${inputEventClassSimple}.class)));
        }
    }

    @Inject private ${functionName}Service ${functionNameProperty}Service;

    public ${functionName}Handler() { }

    public ${functionName}Handler(ApplicationContext context) {
        super(context);
    }

    @Override
    public $outputEventClassSimple apply($inputEventClassSimple event) {
         return ${functionNameProperty}Service.handle(event);
    }

}
