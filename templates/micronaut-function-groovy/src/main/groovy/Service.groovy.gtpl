package $pkg

<% if (requiresImport(inputEventClass)) { %>import $inputEventClass<% } %>
<% if (requiresImport(outputEventClass)) { %>import $outputEventClass<% } %>

import com.agorapulse.micronaut.log4aws.LogError

import groovy.transform.CompileStatic

import javax.inject.Singleton

@Singleton
@CompileStatic
class ${functionName}Service {

    @LogError
    $outputEventClassSimple handle($inputEventClassSimple event) {
        // TODO: implement
        throw new UnsupportedOperationException('TODO: implement')
    }

}