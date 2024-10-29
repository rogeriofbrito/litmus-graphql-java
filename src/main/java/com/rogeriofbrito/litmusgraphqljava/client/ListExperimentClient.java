package com.rogeriofbrito.litmusgraphqljava.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.client.codegen.BaseProjectionNode;
import com.netflix.graphql.dgs.client.codegen.GraphQLQuery;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.client.ListExperimentGraphQLQuery;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.client.ListExperimentProjectionRoot;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.ListExperimentRequest;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.ListExperimentResponse;
import com.rogeriofbrito.graphqlmusicstoremaven.generated.types.Pagination;
import com.rogeriofbrito.litmusgraphqljava.GraphQLQueryRequestDecorator;
import com.rogeriofbrito.litmusgraphqljava.config.LitmusConfig;
import com.rogeriofbrito.litmusgraphqljava.tokenstore.TokenStore;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class ListExperimentClient {

    private static final BaseProjectionNode FULL_PROJECTION = new ListExperimentProjectionRoot<>()
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
    private static final Map<String, String> VARIABLES_NAMES = Map.of(
            "projectID", "$projectID",
            "request", "$request");
    private static final Map<String, String> VARIABLES_TYPES = Map.of(
            "projectID", "ID!",
            "request", "ListExperimentRequest!");

    private final LitmusConfig litmusConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final TokenStore tokenStore;

    public ListExperimentClient(LitmusConfig litmusConfig, RestTemplate restTemplate, ObjectMapper objectMapper,
                                TokenStore tokenStore) {
        this.litmusConfig = litmusConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.tokenStore = tokenStore;
    }

    public ListExperimentResponse GetAllExperiments(String projectID, Integer page, Integer limit) {
        Map<String, ?> variables = Map.of(
                "projectID", projectID,
                "request", ListExperimentRequest
                        .newBuilder()
                        .pagination(Pagination
                                .newBuilder()
                                .page(page)
                                .limit(limit)
                                .build())
                        .build());

        GraphQLQuery graphQLQuery = getQueryFromVariables(variables);
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                graphQLQuery,
                FULL_PROJECTION);
        GraphQLQueryRequestDecorator graphQLQueryRequestDecorator = new GraphQLQueryRequestDecorator(
                graphQLQueryRequest,
                VARIABLES_NAMES,
                VARIABLES_TYPES);

        GraphQLRequest graphQLRequest = GraphQLRequest.builder()
                .operationName(graphQLQuery.getOperationName())
                .query(graphQLQueryRequestDecorator.serialize())
                .variables(variables)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Bearer ".concat(tokenStore.getLoginResponse().getAccessToken()));
        HttpEntity<?> request = new HttpEntity<>(graphQLRequest.getRequestBody(), headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(litmusConfig.getGraphqlApiUrl(), HttpMethod.POST, request, JsonNode.class);

        if (response.getStatusCode().isError()) {
            throw new RuntimeException("status code exception"); // TODO: create business exception
        }

        if (response.getBody() == null || response.getBody().get("data") == null) {
            throw new RuntimeException(); // TODO: create business exception
        }

        if (response.getBody().get("errors") != null) {
            Iterator<JsonNode> errorsIterator = response.getBody().get("errors").iterator();
            List<String> errorsMsg = new ArrayList<>();
            while (errorsIterator.hasNext()) {
                JsonNode error = errorsIterator.next();
                errorsMsg.add(error.get("message").toString());
            }

            throw new RuntimeException(errorsMsg.toString()); // TODO: create business exception
        }

        if (response.getBody().get("data").get("listExperiment") == null) {
            throw new RuntimeException(); // TODO: create business exception
        }

        String listExperimentResponseStr = response.getBody().get("data").get("listExperiment").toString();

        try {
            return objectMapper.readValue(listExperimentResponseStr, ListExperimentResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // TODO: create business exception
        }
    }

    public Optional<ListExperimentResponse> GetExperiment(String projectID, String experimentID) {
        return Optional.empty();
    }

    private GraphQLQuery getQueryFromVariables(Map<String, ?> variables) {
        return ListExperimentGraphQLQuery
                .newRequest()
                .projectID((String)variables.get("projectID"))
                .request((ListExperimentRequest)variables.get("request"))
                .build();
    }
}
