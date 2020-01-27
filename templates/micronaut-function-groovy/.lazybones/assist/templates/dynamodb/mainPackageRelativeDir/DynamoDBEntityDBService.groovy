package $pkg

import com.agorapulse.micronaut.aws.dynamodb.annotation.Service

/**
 * DynamoDBEntity data service.
 *
 * @link https://agorapulse.github.io/micronaut-libraries/#_declarative_services_with_service
 */
@Service(DynamoDBEntity)
interface DynamoDBEntityDBService {

    DynamoDBEntity get(String hashKey, String rangeKey)
    List<DynamoDBEntity> getAll(String hashKey, List<String> rangeKeys)

    DynamoDBEntity save(DynamoDBEntity entity)
    List<DynamoDBEntity> saveAll(DynamoDBEntity... entities)

    int count(String hashKey)
    int count(String hashKey, String rangeKey)

    void delete(DynamoDBEntity entity)
    void delete(String hashKey, String rangeKey)

}
