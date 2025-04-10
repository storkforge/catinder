package org.example.springboot25.exceptions;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionResolver {

    @Override
    @NonNull
    public Mono<List<GraphQLError>> resolveException(@NonNull Throwable throwable, @NonNull DataFetchingEnvironment datafetch) {
        if (throwable instanceof IllegalArgumentException) {
            GraphQLError error = GraphqlErrorBuilder.newError(datafetch)
                    .message(throwable.getMessage())
                    .build();
            return Mono.just(List.of(error));
        }
        return Mono.empty();
    }
}

