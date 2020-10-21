package io.dsub.util;

import io.dsub.constants.QueryOp;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryStringGenerator {
    private QueryStringGenerator(){}

    private static final QueryStringGenerator instance = new QueryStringGenerator();

    public static QueryStringGenerator getInstance() {
        return instance;
    }

    public String getSelectQuery(String schema, String table) {
        return buildQuery(QueryOp.SELECT, schema, table, "", "");
    }

    public String getSelectQuery(String schema, String table, Map<String, String> conditions) {
        return buildQuery(QueryOp.SELECT, schema, table, "", conditions);
    }

    public String getSelectQuery(String schema, String table, String conditionString) {
        return buildQuery(QueryOp.SELECT, schema, table, "", conditionString);
    }

    public String getUpdateQuery(String schema, String table, Map<String, String> entryPairs, Map<String, String> conditions) {
        return buildQuery(QueryOp.UPDATE, schema, table, entryPairs, conditions);
    }

    public String getInsertQuery(String schema, String table, Map<String, String> entryPairs) {
        return buildQuery(QueryOp.INSERT, schema, table, entryPairs, "");
    }

    public String getDeleteQuery(String schema, String table) {
        return buildQuery(QueryOp.DELETE, schema, table,"", "");
    }

    public String getDeleteQuery(String schema, String table, Map<String, String> conditions) {
        return buildQuery(QueryOp.DELETE, schema, table, "", conditions);
    }

    public String getDeleteQuery(String schema, String table, String conditionString) {
        return buildQuery(QueryOp.DELETE, schema, table, "", conditionString);
    }

    private String buildQuery(QueryOp op, String schema, String table, Map<String, String> entryPairs, Map<String, String> conditions) {
        String queryString = getBaseString(op, schema, table) + getIntermediates(op, entryPairs) + getConditionals(conditions);
        return queryString.trim();
    }

    private String buildQuery(QueryOp op, String schema, String table, Map<String, String> entryPairs, String conditionString) {
        String queryString = getBaseString(op, schema, table) + getIntermediates(op, entryPairs) + conditionString;
        return queryString.trim();
    }

    private String buildQuery(QueryOp op, String schema, String table, String entryString, Map<String, String> conditions) {
        String queryString = getBaseString(op, schema, table) + entryString + getConditionals(conditions);
        return queryString.trim();
    }

    private String buildQuery(QueryOp op, String schema, String table, String entryString, String conditionString) {
        String queryString = getBaseString(op, schema, table) + entryString + conditionString;
        return queryString.trim();
    }

    private String getConditionals(Map<String, String> conditions) {

        if (conditions == null || conditions.size() == 0) {
            return "";
        }

        String conditionString = conditions.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .reduce((acc, curr) -> {
                    if (acc.length() == 0) {
                        return curr;
                    }
                    return curr + " AND " + acc; // reverse the order from end to start
                })
                .get();
        return "WHERE " + conditionString;
    }

    private String getIntermediates(QueryOp op, Map<String, String> pair) {
        String s = "";

        switch(op) {
            case INSERT -> s = buildInsertIntermediates(pair);
            case UPDATE -> s = buildUpdateIntermediates(pair);
        }

        return s;
    }

    private String buildUpdateIntermediates(Map<String, String> pair) {
        if (pair == null || pair.size() == 0) {
            return "";
        }

        List<String> list = pair.entrySet().stream()
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .collect(Collectors.toUnmodifiableList());

        StringBuilder sb = new StringBuilder();

        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.size() > 1 && i < list.size() - 1) {
                sb.append(", ");
            }
            sb.append(list.get(i));
        }

        return "SET "+ sb.toString() + " ";
    }

    private String buildInsertIntermediates(Map<String, String> pair) {
        if (pair == null || pair.size() == 0) {
            return "";
        }

        String columns = pair.keySet().stream()
                .reduce((acc, curr) -> curr + ", " + acc)
                .map(s -> "(" + s + ")")
                .get();

        String values = pair.values()
                .stream()
                .map(s -> String.format("'%s'", s))
                .reduce((acc, curr) -> curr + ", " + acc)
                .map(s -> "(" + s + ")")
                .get();
        return columns + " VALUES " + values;
    }


    private String getBaseString(QueryOp op, String schema, String table) {
        String base;

        switch (op) {
            case INSERT -> base = "INSERT INTO";
            case UPDATE -> base = "UPDATE";
            case SELECT -> base = "SELECT * FROM";
            case DELETE -> base = "DELETE FROM";
            default -> throw new UnsupportedOperationException("operation is not supported");
        }

        return String.format("%s %s.%s ", base, schema, table);
    }

}
