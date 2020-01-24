package $pkg

import groovy.transform.CompileStatic
import groovy.transform.ToString
import io.micronaut.context.annotation.ConfigurationProperties

@ToString
@CompileStatic
@ConfigurationProperties('${functionNameHyphens.replace('-', '.')}')
class ${functionName}Configuration {

    String value

}
