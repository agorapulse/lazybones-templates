package $pkg

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import groovy.transform.CompileStatic

@CompileStatic
@DynamoDBTable(tableName = 'DynamoDBEntity')
class DynamoDBEntity {

    @DynamoDBHashKey String hashKey
    @DynamoDBRangeKey String rangeKey

}
