package com.rest.qa;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.io.File;

import static java.sql.DriverManager.getConnection;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class DataBase {

    static {
        System.setProperty("org.jooq.no-logo", "true");
    }

    private String path;
    private Supplier<DSLContext> dsl = () -> DSL.using(getConnection("jdbc:sqlite:" + path), SQLITE);

    public DataBase(String path) {
        this.path = path;
    }

    public long getDbLastModified() {
        return new File(path).lastModified();
    }

    private String getValueById(String value, int id) throws Exception {
        try (DSLContext sql = dsl.get()) {
            return sql.select(field(value)).from("uploads").where(field("id").eq(id)).fetchOne(0).toString();
        }
    }

    public String getUserId(int id) throws Exception {
        return getValueById("user_id", id);
    }

    public String getPayloadMD5(int id) throws Exception {
        return getValueById("payload_md5", id);
    }

    public void cleanUploads() throws Exception {
        try (DSLContext sql = dsl.get()) {
            sql.delete(table("uploads")).execute();
        }
    }
}
