package com.rogeriofbrito.litmusgraphqljava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.client.ListExperimentGraphQLQuery;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.client.ListExperimentProjectionRoot;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.ListExperimentRequest;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.Pagination;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class MyGraphQL {

    @PostConstruct
    private void postConstruct() throws JsonProcessingException {
        ListExperimentGraphQLQuery listExperimentGraphQLQuery = ListExperimentGraphQLQuery
                .newRequest()
                .projectID("test")
                .request(ListExperimentRequest
                        .newBuilder()
                        .pagination(Pagination
                                .newBuilder()
                                .page(0)
                                .limit(7)
                                .build())
                        .build())
                .build();
        var root = new ListExperimentProjectionRoot<>().totalNoOfExperiments();
        var experimentsProj = root.experiments().experimentID().createdAt().description();
        Map<String, String> variablesNames = Map.of("projectID", "$projectID", "request", "$request");
        Map<String, String> variablesTypes = Map.of("projectID", "ID!", "request", "ListExperimentRequest!");

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(listExperimentGraphQLQuery, experimentsProj);
        GraphQLQueryRequestDecorator graphQLQueryRequestDecorator = new GraphQLQueryRequestDecorator(graphQLQueryRequest, variablesNames, variablesTypes);

        GraphQLRequest graphQLRequest = GraphQLRequest.builder()
                .operationName(listExperimentGraphQLQuery.getOperationName())
                .query(graphQLQueryRequestDecorator.serialize())
                .variables(Map.of(
                        "projectID",
                        "ac3598af-0c62-4ad5-a323-848cafaaa881",
                        "request",
                        ListExperimentRequest
                        .newBuilder()
                        .pagination(Pagination
                                .newBuilder()
                                .page(0)
                                .limit(7)
                                .build())
                        .build()))
                .build();

        System.out.println(new ObjectMapper().writeValueAsString(graphQLRequest.getRequestBody()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "");
        HttpEntity<?> request = new HttpEntity<>(graphQLRequest.getRequestBody(), headers);
        String response = restTemplate.postForObject("http://localhost:8185/api/query", request, String.class);
        System.out.println(response);
    }
}
