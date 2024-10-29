package com.rogeriofbrito.litmusgraphqljava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.ListExperimentResponse;
import com.rogeriofbrito.litmusgraphqljava.client.ListExperimentClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MyGraphQL {

    @PostConstruct
    private void postConstruct() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String litmusGraphQLAPIUrl = "http://localhost:8185/api/query";
        ObjectMapper objectMapper = new ObjectMapper();
        ListExperimentClient listExperimentClient = new ListExperimentClient(restTemplate,
                litmusGraphQLAPIUrl,
                objectMapper);

        String token = "";
        String projectID = "ac3598af-0c62-4ad5-a323-848cafaaa881";
        Integer page = 0;
        Integer limit = 10;
        ListExperimentResponse listExperimentResponse = listExperimentClient.GetAllExperiments(token, projectID, page, limit);
        System.out.println(listExperimentResponse);
    }
}
