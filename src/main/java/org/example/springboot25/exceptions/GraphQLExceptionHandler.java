package org.example.springboot25.exceptions;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
        else if (throwable instanceof EntityNotFoundException) {
            GraphQLError error = GraphqlErrorBuilder.newError(datafetch)
                    .message("Resource not found: " + throwable.getMessage())
                    .extensions(Map.of("code", "NOT_FOUND"))
                    .build();
            return Mono.just(List.of(error));
        }
        else if (throwable instanceof ValidationException) {
            GraphQLError error = GraphqlErrorBuilder.newError(datafetch)
                    .message("Validation error: " + throwable.getMessage())
                    .extensions(Map.of("code", "VALIDATION_ERROR"))
                    .build();
            return Mono.just(List.of(error));
        }
        else {
            GraphQLError error = GraphqlErrorBuilder.newError(datafetch)
                    .message("Internal server error occurred")
                    .extensions(Map.of("code", "INTERNAL_ERROR"))
                    .build();
            return Mono.just(List.of(error));
        }
    }
}

