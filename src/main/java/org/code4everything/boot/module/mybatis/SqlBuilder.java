package org.code4everything.boot.module.mybatis;

import org.apache.ibatis.jdbc.SQL;

import java.util.Objects;

/**
 * MyBatis SQL语句生成工具类
 *
 * @author pantao
 * @since 2019/2/1
 **/
public class SqlBuilder {

    private Page page = null;

    private SQL sql;

    public SqlBuilder() {
        this.sql = new SQL();
    }

    public SqlBuilder(SQL sql) {
        this.sql = sql;
    }

    public SqlBuilder(int offset, int size) {
        this.sql = new SQL();
        page(offset, size);
    }

    public SqlBuilder(SQL sql, int offset, int size) {
        this.sql = sql;
        page(offset, size);
    }

    public SqlBuilder update(String table) {
        sql.UPDATE(table);
        return this;
    }

    public SqlBuilder set(String sets) {
        sql.SET(sets);
        return this;
    }

    public SqlBuilder set(String... sets) {
        sql.SET(sets);
        return this;
    }

    public SqlBuilder insertInto(String tableName) {
        sql.INSERT_INTO(tableName);
        return this;
    }

    public SqlBuilder values(String columns, String values) {
        sql.VALUES(columns, values);
        return this;
    }

    public SqlBuilder intoColumns(String... columns) {
        sql.INTO_COLUMNS(columns);
        return this;
    }

    public SqlBuilder intoValues(String... values) {
        sql.INTO_VALUES(values);
        return this;
    }

    public SqlBuilder select(String columns) {
        sql.SELECT(columns);
        return this;
    }

    public SqlBuilder select(String... columns) {
        sql.SELECT(columns);
        return this;
    }

    public SqlBuilder selectDistinct(String columns) {
        sql.SELECT_DISTINCT(columns);
        return this;
    }

    public SqlBuilder selectDistinct(String... columns) {
        sql.SELECT_DISTINCT(columns);
        return this;
    }

    public SqlBuilder deleteFrom(String table) {
        sql.DELETE_FROM(table);
        return this;
    }

    public SqlBuilder from(String table) {
        sql.FROM(table);
        return this;
    }

    public SqlBuilder from(String... tables) {
        sql.FROM(tables);
        return this;
    }

    public SqlBuilder join(String join) {
        sql.JOIN(join);
        return this;
    }

    public SqlBuilder join(String... joins) {
        sql.JOIN(joins);
        return this;
    }

    public SqlBuilder innerJoin(String join) {
        sql.INNER_JOIN(join);
        return this;
    }

    public SqlBuilder innerJoin(String... joins) {
        sql.INNER_JOIN(joins);
        return this;
    }

    public SqlBuilder leftOuterJoin(String join) {
        sql.LEFT_OUTER_JOIN(join);
        return this;
    }

    public SqlBuilder leftOuterJoin(String... joins) {
        sql.LEFT_OUTER_JOIN(joins);
        return this;
    }

    public SqlBuilder rightOuterJoin(String join) {
        sql.RIGHT_OUTER_JOIN(join);
        return this;
    }

    public SqlBuilder rightOuterJoin(String... joins) {
        sql.RIGHT_OUTER_JOIN(joins);
        return this;
    }

    public SqlBuilder outerJoin(String join) {
        sql.OUTER_JOIN(join);
        return this;
    }

    public SqlBuilder outerJoin(String... joins) {
        sql.OUTER_JOIN(joins);
        return this;
    }

    public SqlBuilder where(String conditions) {
        sql.WHERE(conditions);
        return this;
    }

    public SqlBuilder where(String... conditions) {
        sql.WHERE(conditions);
        return this;
    }

    public SqlBuilder or() {
        sql.OR();
        return this;
    }

    public SqlBuilder and() {
        sql.AND();
        return this;
    }

    public SqlBuilder groupBy(String columns) {
        sql.GROUP_BY(columns);
        return this;
    }

    public SqlBuilder groupBy(String... columns) {
        sql.GROUP_BY(columns);
        return this;
    }

    public SqlBuilder having(String conditions) {
        sql.HAVING(conditions);
        return this;
    }

    public SqlBuilder having(String... conditions) {
        sql.HAVING(conditions);
        return this;
    }

    public SqlBuilder orderBy(String columns) {
        sql.ORDER_BY(columns);
        return this;
    }

    public SqlBuilder orderBy(String... columns) {
        sql.ORDER_BY(columns);
        return this;
    }

    public SqlBuilder page(int offset, int size) {
        this.page = new Page(offset, size);
        return this;
    }

    public String build() {
        return sql.toString() + (Objects.isNull(page) ? "" : " limit " + (page.offset * page.size) + "," + page.size);
    }

    @Override
    public String toString() {
        return build();
    }

    private class Page {

        private int offset;

        private int size;

        private Page(int offset, int size) {
            this.offset = offset;
            this.size = size;
        }
    }
}
