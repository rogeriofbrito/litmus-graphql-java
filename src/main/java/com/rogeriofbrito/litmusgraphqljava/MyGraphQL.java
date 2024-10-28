package com.rogeriofbrito.litmusgraphqljava;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.netflix.graphql.dgs.client.codegen.BaseProjectionNode;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.client.ListExperimentGraphQLQuery;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.client.ListExperimentProjectionRoot;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.ListExperimentRequest;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.ListExperimentResponse;
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
        BaseProjectionNode projection = new ListExperimentProjectionRoot<>()
                .totalNoOfExperiments()
                .experiments()
                    //.__typename()
                    .weightages()
                        //.__typename()
                        .faultName()
                        .weightage()
                        .getParent()
                    .infra()
                        //.__typename()
                        .createdBy()
                            //.__typename()
                            .userID()
                            .username()
                            .email()
                            .getParent()
                        .updatedBy()
                            //.__typename()
                            .userID()
                            .username()
                            .email()
                            .getParent()
                        //.infraType()
                        //    .__typename()
                        //    .getParent()
                        //.updateStatus()
                        //    .__typename()
                        //    .getParent()
                        .projectID()
                        .infraID()
                        .name()
                        .description()
                        .tags()
                        .environmentID()
                        .platformName()
                        .isActive()
                        .isInfraConfirmed()
                        .isRemoved()
                        .updatedAt()
                        .createdAt()
                        .noOfExperiments()
                        .noOfExperimentRuns()
                        .token()
                        .infraNamespace()
                        .serviceAccount()
                        .infraScope()
                        .infraNsExists()
                        .infraSaExists()
                        .lastExperimentTimestamp()
                        .startTime()
                        .version()
                        .getParent()
                    .createdBy()
                        //.__typename()
                        .userID()
                        .username()
                        .email()
                        .getParent()
                    //.recentExperimentRunDetails()
                        //.__typename()
                        //.getParent()
                    .updatedBy()
                        //.__typename()
                        .userID()
                        .username()
                        .email()
                        .getParent()
                    .projectID()
                    .experimentID()
                    .experimentManifest()
                    .cronSyntax()
                    .name()
                    .description()
                    .isCustomExperiment()
                    .updatedAt()
                    .createdAt()
                    .isRemoved()
                    .tags()
                    .description();

        Map<String, ?> variables = Map.of(
                "projectID","ac3598af-0c62-4ad5-a323-848cafaaa881",
                "request", ListExperimentRequest
                        .newBuilder()
                        .pagination(Pagination
                                .newBuilder()
                                .page(0)
                                .limit(7)
                                .build())
                        .build());
        Map<String, String> variablesNames = Map.of(
                "projectID", "$projectID",
                "request", "$request");
        Map<String, String> variablesTypes = Map.of(
                "projectID", "ID!",
                "request", "ListExperimentRequest!");

        ListExperimentGraphQLQuery listExperimentGraphQLQuery = ListExperimentGraphQLQuery
                .newRequest()
                .projectID((String)variables.get("projectID"))
                .request((ListExperimentRequest)variables.get("request"))
                .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(listExperimentGraphQLQuery, projection);
        GraphQLQueryRequestDecorator graphQLQueryRequestDecorator = new GraphQLQueryRequestDecorator(graphQLQueryRequest, variablesNames, variablesTypes);

        GraphQLRequest graphQLRequest = GraphQLRequest.builder()
                .operationName(listExperimentGraphQLQuery.getOperationName())
                .query(graphQLQueryRequestDecorator.serialize())
                .variables(variables)
                .build();

        System.out.println(new ObjectMapper().writeValueAsString(graphQLRequest.getRequestBody()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "");
        HttpEntity<?> request = new HttpEntity<>(graphQLRequest.getRequestBody(), headers);
        JsonNode response = restTemplate.postForObject("http://localhost:8185/api/query", request, JsonNode.class);
        System.out.println(response);
        System.out.println(new ObjectMapper().readValue(response.get("data").get("listExperiment").toString(), ListExperimentResponse.class));
    }
}
