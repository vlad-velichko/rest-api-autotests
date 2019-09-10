package com.rest.qa;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import static java.sql.DriverManager.getConnection;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.impl.DSL.field;

public class DataBase {

    private String path;

    public DataBase(String path) {
        this.path = "jdbc:sqlite:" + path;
    }

    public Upload getUpload(int id) throws SQLException {
        Upload upload = null;
        //"SELECT user_id, payload_md5 FROM uploads WHERE id=" + id;
        try (DSLContext create = DSL.using(getConnection(path), SQLITE)) {
            Result<Record2<Object, Object>> result = create
                    .select(
                            field("user_id"),
                            field("payload_md5"))
                    .from("uploads")
                    .where(
                            field("id").eq(id))
                    .fetch();
            if (result.size() > 1) throw new SQLException("Not unique ID");

//            upload = new Upload(rs.getString("user_id"), rs.getString("payload_md5"));
        }
        return upload;
    }

    public int getUploadsCount() throws SQLException {
//        String query = "SELECT COUNT(id) FROM uploads";
//        try (ResultSet rs = getConnection(path).createStatement().executeQuery(query)) {
//            return rs.getInt("COUNT(id)");
//        }
        try (Connection con = getConnection(path);
             DSLContext create = DSL.using(con, SQLITE)) {
            System.out.println(create);
//            int result = create
//                    .selectCount()
//                    .from("uploads")
//                    .fetch();
//            if (result.size() > 1) throw new SQLException("Not unique ID");

        }
        return 0;
    }

    public void cleanUploads() throws SQLException {
        try (Connection con = getConnection(path);
             Statement st = con.createStatement()) {
            st.executeUpdate("DELETE FROM uploads");

            Logger.getLogger("DataBase").config("Table uploads was truncated!");
        }
    }
}
