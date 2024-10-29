package com.rogeriofbrito.litmusgraphqljava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.ListExperimentResponse;
import com.rogeriofbrito.litmusgraphqljava.client.ListExperimentClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MyGraphQL {

    private final ListExperimentClient listExperimentClient;

    public MyGraphQL(ListExperimentClient listExperimentClient) {
        this.listExperimentClient = listExperimentClient;
    }

    @PostConstruct
    private void postConstruct() throws JsonProcessingException {
        String projectID = "ac3598af-0c62-4ad5-a323-848cafaaa881";
        Integer page = 0;
        Integer limit = 10;
        ListExperimentResponse listExperimentResponse = listExperimentClient.GetAllExperiments(projectID, page, limit);
        System.out.println(listExperimentResponse);
    }
}
