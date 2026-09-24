package hospital.dao;

import hospital.database.DatabaseConnection;

import java.sql.*;
import java.util.*;

/**
 * Central JDBC repository used by the console application.
 * It keeps SQL/connection handling out of the menu code.
 */
public class HospitalDAO {

    public int executeInsert(String sql, Object... params) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, params);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new SQLException("No generated key was returned.");
                return rs.getInt(1);
            }
        }
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            return ps.executeUpdate();
        }
    }

    public List<Map<String,Object>> query(String sql, Object... params) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return toRows(rs);
            }
        }
    }

    public Map<String,Object> one(String sql, Object... params) throws SQLException {
        List<Map<String,Object>> rows = query(sql, params);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public int count(String table) throws SQLException {
        return intValue(one("SELECT COUNT(*) AS C FROM " + safeIdentifier(table)));
    }

    public boolean exists(String sql, Object... params) throws SQLException {
        return !query(sql, params).isEmpty();
    }

    public void transaction(List<StatementWork> work) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);
            try {
                for (StatementWork item : work) item.run(c);
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(old);
            }
        }
    }

    @FunctionalInterface
    public interface StatementWork {
        void run(Connection connection) throws SQLException;
    }

    private static void bind(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (p instanceof java.time.LocalDate d) ps.setDate(i + 1, java.sql.Date.valueOf(d));
            else if (p instanceof java.time.LocalDateTime dt) ps.setTimestamp(i + 1, Timestamp.valueOf(dt));
            else ps.setObject(i + 1, p);
        }
    }

    private static List<Map<String,Object>> toRows(ResultSet rs) throws SQLException {
        List<Map<String,Object>> rows = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        int n = md.getColumnCount();
        while (rs.next()) {
            Map<String,Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= n; i++) {
                row.put(md.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

    public static int intValue(Map<String,Object> row) {
        if (row == null || row.isEmpty()) return 0;
        Object value = row.values().iterator().next();
        return value == null ? 0 : ((Number)value).intValue();
    }

    public static String safeIdentifier(String value) {
        if (value == null || !value.matches("[A-Za-z_][A-Za-z0-9_]*"))
            throw new IllegalArgumentException("Invalid SQL identifier.");
        return value;
    }
}
