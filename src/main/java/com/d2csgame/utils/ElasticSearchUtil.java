package com.d2csgame.utils;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ElasticSearchUtil {
    public static Supplier<Query> supplier() {
        Supplier<Query> supplier = () -> Query.of(q -> q.matchAll(matchAllQuery()));
        return supplier;
    }

    public static MatchAllQuery matchAllQuery() {
        val matchAllQuery = new MatchAllQuery.Builder();
        return matchAllQuery.build();
    }

    public static Supplier<Query> supplierWithNameField(String fieldValue) {
        Supplier<Query> supplier = () -> Query.of(q -> q.match(matchQueryWithNameField(fieldValue)));
        return supplier;
    }

    public static MatchQuery matchQueryWithNameField(String fieldValue) {
        val matchQuery = new MatchQuery.Builder();
        return matchQuery.field("name").query(fieldValue).analyzer("standard").build();
    }

    public static Supplier<Query> supplierQueryForBoolQuery(String productName, Integer quantity) {
        Supplier<Query> supplier = () -> Query.of(q -> q.bool(boolQuery(productName, quantity)));
        return supplier;
    }

    public static BoolQuery boolQuery(String productName, Integer quantity) {
        val boolQuery = new BoolQuery.Builder();
        return boolQuery.filter(termQuery(productName)).must(matchQuery(quantity)).build();
    }

    public static List<Query> termQuery(String productName) {
        final List<Query> terms = new ArrayList<>();
        val termQuery = new TermQuery.Builder();
        terms.add(Query.of(q -> q.term(termQuery.field("name").value(productName).build())));
        return terms;
    }

    public static List<Query> matchQuery(Integer qty) {
        final List<Query> matches = new ArrayList<>();
        val matchQuery = new MatchQuery.Builder();
        matches.add(Query.of(q -> q.match(matchQuery.field("quantity").query(qty).build())));
        return matches;
    }

    public static Supplier<Query> supplierFuzzyQuery(String approximateProductName) {
        Supplier<Query> supplier = () -> Query.of(q -> q.fuzzy(createFuzzyQuery(approximateProductName)));
        return supplier;
    }

    public static FuzzyQuery createFuzzyQuery(String approximateProductName) {
        val fuzzyQuery = new FuzzyQuery.Builder();
        return fuzzyQuery.field("name").value(approximateProductName).fuzziness("auto").build();
    }

    public static Supplier<Query> supplierMultiMatch(String key, List<String> fields) {
        return () -> multiMatchQuery(key, fields);
    }

    public static Query multiMatchQuery(String key, List<String> fields) {
        List<Query> shouldQueries = new ArrayList<>();

        // MultiMatch Query with Fuzziness
        MultiMatchQuery multiMatch = new MultiMatchQuery.Builder()
                .query(key)
                .fields(fields)
                .fuzziness("AUTO")
                .type(TextQueryType.BestFields)
                .minimumShouldMatch("70%")
                .build();

        // Adding MultiMatch Query to should clause
        shouldQueries.add(Query.of(q -> q.multiMatch(multiMatch)));

        // Wildcard Queries for Partial Matching on Each Field
        for (String field : fields) {
            WildcardQuery wildcardQuery = new WildcardQuery.Builder()
                    .field(field)
                    .value(key + "*")  // Matches terms starting with 'key'
                    .caseInsensitive(true)  // Optional: make it case insensitive
                    .build();

            // Adding Wildcard Query to should clause
            shouldQueries.add(Query.of(q -> q.wildcard(wildcardQuery)));
        }

        // Combine with Bool Query
        BoolQuery boolQuery = new BoolQuery.Builder()
                .should(shouldQueries)  // At least one should clause should match
                .minimumShouldMatch("1")  // Ensure that either the wildcard or multi_match is satisfied
                .build();

        return Query.of(q -> q.bool(boolQuery));
    }
}

