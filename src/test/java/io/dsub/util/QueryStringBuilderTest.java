package io.dsub.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QueryStringBuilderTest {

    private static final QueryStringBuilder sqlGenerator = QueryStringBuilder.getInstance();

    private final String SCHEMA = "schema";
    private final String TABLE = "table";
    private Map<String, String> multiMap;
    private Map<String, String> singleMap;

    @BeforeEach
    void resetData() {
        multiMap = new HashMap<>();
        multiMap.put("key_1", "value_1");
        multiMap.put("key_2", "value_2");
        multiMap.put("key_3", "value_3");

        singleMap = new HashMap<>();
        singleMap.put("key", "value");
    }

    @Test
    void testGetSelectQuery() {
        String selectQuery = sqlGenerator.getSelectQuery(SCHEMA, TABLE);
        assertEquals("SELECT * FROM schema.table", selectQuery);

        selectQuery = sqlGenerator.getSelectQuery(SCHEMA, TABLE, "");
        assertEquals("SELECT * FROM schema.table", selectQuery);

        selectQuery = sqlGenerator.getSelectQuery(SCHEMA, TABLE, "WHERE key = value");
        assertEquals("SELECT * FROM schema.table WHERE key = value", selectQuery);

        selectQuery = sqlGenerator.getSelectQuery(SCHEMA, TABLE, new HashMap<>());
        assertEquals("SELECT * FROM schema.table", selectQuery);

        selectQuery = sqlGenerator.getSelectQuery(SCHEMA, TABLE, singleMap);
        assertEquals("SELECT * FROM schema.table WHERE key = 'value'", selectQuery);

        selectQuery = sqlGenerator.getSelectQuery(SCHEMA, TABLE, multiMap);
        assertEquals("SELECT * FROM schema.table WHERE key_1 = 'value_1' AND key_2 = 'value_2' AND key_3 = 'value_3'", selectQuery);
    }

    @Test
    void testGetUpdateQuery() {
        String updateQuery = sqlGenerator.getUpdateQuery(SCHEMA, TABLE, singleMap, singleMap);
        assertEquals("UPDATE schema.table SET key = 'value' WHERE key = 'value'", updateQuery);

        updateQuery = sqlGenerator.getUpdateQuery(SCHEMA, TABLE, multiMap, singleMap);
        assertEquals("UPDATE schema.table SET key_1 = 'value_1', key_2 = 'value_2', key_3 = 'value_3' WHERE key = 'value'", updateQuery);

        updateQuery = sqlGenerator.getUpdateQuery(SCHEMA, TABLE, singleMap, multiMap);
        assertEquals("UPDATE schema.table SET key = 'value' WHERE key_1 = 'value_1' AND key_2 = 'value_2' AND key_3 = 'value_3'", updateQuery);

        updateQuery = sqlGenerator.getUpdateQuery(SCHEMA, TABLE, multiMap, multiMap);
        assertEquals("UPDATE schema.table SET key_1 = 'value_1', key_2 = 'value_2', key_3 = 'value_3' WHERE key_1 = 'value_1' AND key_2 = 'value_2' AND key_3 = 'value_3'", updateQuery);
    }

    @Test
    void testGetInsertQuery() {
        String insertQuery = sqlGenerator.getInsertQuery(SCHEMA, TABLE, singleMap);
        assertEquals("INSERT INTO schema.table (key) VALUES ('value') ON DUPLICATE KEY UPDATE key = 'value'", insertQuery);

        insertQuery = sqlGenerator.getInsertQuery(SCHEMA, TABLE, multiMap);
        assertEquals("INSERT INTO schema.table (key_1, key_2, key_3) VALUES ('value_1', 'value_2', 'value_3') ON DUPLICATE KEY UPDATE key_1 = 'value_1', key_2 = 'value_2', key_3 = 'value_3'", insertQuery);
    }

    @Test
    void testGetDeleteQuery() {
        String deleteQuery = sqlGenerator.getDeleteQuery(SCHEMA, TABLE);
        assertEquals("DELETE FROM schema.table", deleteQuery);

        deleteQuery = sqlGenerator.getDeleteQuery(SCHEMA, TABLE, singleMap);
        assertEquals("DELETE FROM schema.table WHERE key = 'value'", deleteQuery);

        deleteQuery = sqlGenerator.getDeleteQuery(SCHEMA, TABLE, multiMap);
        assertEquals("DELETE FROM schema.table WHERE key_1 = 'value_1' AND key_2 = 'value_2' AND key_3 = 'value_3'", deleteQuery);
    }
}