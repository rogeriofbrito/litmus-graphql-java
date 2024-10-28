package com.rogeriofbrito.litmusgraphqljava;


import com.netflix.graphql.dgs.client.codegen.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class GraphQLQueryRequestDecorator {

    private final GraphQLQueryRequest graphQLQueryRequest;

    public GraphQLQueryRequestDecorator(GraphQLQueryRequest graphQLQueryRequest) {
        this.graphQLQueryRequest = graphQLQueryRequest;
    }

    public String serialize() {
        GraphQLQuery query = null;
        InputValueSerializer inputValueSerializer = null;
        BaseProjectionNode projection = null;
        ProjectionSerializer projectionSerializer = null;

        try {
            Field queryField = GraphQLQueryRequest.class.getDeclaredField("query");
            Field inputValueSerializerField = GraphQLQueryRequest.class.getDeclaredField("inputValueSerializer");
            Field projectionField = GraphQLQueryRequest.class.getDeclaredField("projection");
            Field projectionSerializerField = GraphQLQueryRequest.class.getDeclaredField("projectionSerializer");

            queryField.setAccessible(true);
            inputValueSerializerField.setAccessible(true);
            projectionField.setAccessible(true);
            projectionSerializerField.setAccessible(true);

            query = (GraphQLQuery)queryField.get(graphQLQueryRequest);
            inputValueSerializer = (InputValueSerializer)inputValueSerializerField.get(graphQLQueryRequest);
            projection = (BaseProjectionNode)projectionField.get(graphQLQueryRequest);
            projectionSerializer = (ProjectionSerializer)projectionSerializerField.get(graphQLQueryRequest);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(query.getOperationType());
        if (query.getName() != null) {
            builder.append(" ").append(query.getName());
        }

        builder.append(" {").append(query.getOperationName());
        Map<String, ?> input = query.getInput();
        if (!input.isEmpty()) {
            builder.append("(");
            Iterator<?> inputEntryIterator = input.entrySet().iterator();

            while(inputEntryIterator.hasNext()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>)inputEntryIterator.next();
                String key = (String)entry.getKey();
                Object value = entry.getValue();
                builder.append(key);
                builder.append(": ");
                builder.append(inputValueSerializer.serialize(value));
                if (inputEntryIterator.hasNext()) {
                    builder.append(", ");
                }
            }

            builder.append(")");
        }

        if (projection instanceof BaseSubProjectionNode) {
            Object root = ((BaseSubProjectionNode<?, ?>)projection).root();
            if (root == null) {
                throw new NullPointerException("null cannot be cast to non-null type com.netflix.graphql.dgs.client.codegen.BaseProjectionNode");
            }

            builder.append(projectionSerializer.serialize((BaseProjectionNode)root, false));
        } else if (projection != null) {
            builder.append(projectionSerializer.serialize(projection, false));
        }

        builder.append(" }");
        return builder.toString();
    }
}
