package io.dsub.util;

import io.dsub.constants.QueryType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryStringBuilder {
    private QueryStringBuilder(){}

    private static final QueryStringBuilder instance = new QueryStringBuilder();

    public static QueryStringBuilder getInstance() {
        return instance;
    }

    public String getSelectQuery(String schema, String table) {
        return buildQuery(QueryType.SELECT, schema, table, "");
    }

    public String getSelectQuery(String schema, String table, Map<String, String> conditions) {
        return buildQuery(QueryType.SELECT, schema, table, conditions);
    }

    public String getSelectQuery(String schema, String table, String conditionString) {
        return buildQuery(QueryType.SELECT, schema, table, conditionString);
    }

    public String getUpdateQuery(String schema, String table, Map<String, String> entryPairs, Map<String, String> conditions) {
        return buildQuery(schema, table, entryPairs, conditions);
    }

    public String getInsertQuery(String schema, String table, Map<String, String> entryPairs) {
        return buildQuery(schema, table, entryPairs);
    }

    public String getDeleteQuery(String schema, String table) {
        return buildQuery(QueryType.DELETE, schema, table, "");
    }

    public String getDeleteQuery(String schema, String table, Map<String, String> conditions) {
        return buildQuery(QueryType.DELETE, schema, table, conditions);
    }

    public String getDeleteQuery(String schema, String table, String conditionString) {
        return buildQuery(QueryType.DELETE, schema, table, conditionString);
    }

    private String buildQuery(String schema, String table, Map<String, String> entryPairs, Map<String, String> conditions) {
        String queryString = getBaseString(QueryType.UPDATE, schema, table) + getIntermediates(QueryType.UPDATE, entryPairs) + getConditionals(conditions);
        return queryString.trim();
    }

    private String buildQuery(String schema, String table, Map<String, String> entryPairs) {
        String queryString = getBaseString(QueryType.INSERT, schema, table) + getIntermediates(QueryType.INSERT, entryPairs);
        return queryString.trim();
    }

    private String buildQuery(QueryType op, String schema, String table, Map<String, String> conditions) {
        String queryString = getBaseString(op, schema, table) + getConditionals(conditions);
        return queryString.trim();
    }

    private String buildQuery(QueryType op, String schema, String table, String conditionString) {
        String queryString = getBaseString(op, schema, table) + conditionString;
        return queryString.trim();
    }

    private String getConditionals(Map<String, String> conditions) {

        if (conditions == null || conditions.size() == 0) {
            return "";
        }

        String condString = conditions.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().equals("null"))
                .map(entry -> entry.getKey() + " = " + makeValueString(entry.getValue()))
                .reduce((acc, curr) -> {
                    if (acc.length() == 0) {
                        return curr;
                    }
                    return curr + " AND " + acc; // reverse the order from end to start
                })
                .orElse("");
        return condString.isBlank() ? "" : "WHERE " + condString;
    }

    private String getIntermediates(QueryType op, Map<String, String> pair) {
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
                .filter(entry -> !entry.getValue().equals("null"))
                .map(entry -> entry.getKey() + " = " + makeValueString(entry.getValue()))
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

    private String makeValueString(String s) {
        return String.format("'%s'", s);
    }

    private String buildInsertIntermediates(Map<String, String> pair) {
        if (pair == null || pair.size() == 0) {
            return "";
        }

        pair = pair.entrySet().stream()
                .filter(v -> !v.getValue().equals("null"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String updateBlock = pair.entrySet().stream()
                .filter(v -> !v.getKey().equals("id"))
                .map(v -> v.getKey() + " = " + String.format("'%s'", v.getValue()))
                .reduce((acc, curr) -> curr + ", " + acc)
                .orElse("");

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

        String query = columns + " VALUES " + values;

        if (updateBlock.length() > 0) {
            query += " ON DUPLICATE KEY UPDATE " + updateBlock;
        }

        return query;
    }


    private String getBaseString(QueryType op, String schema, String table) {
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
