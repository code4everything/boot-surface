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

    /**
     * 分页
     *
     * @since 1.0.7
     */
    private Page page = null;

    /**
     * SQL 生成器
     *
     * @since 1.0.7
     */
    private SQL sql;

    /**
     * 无参构造
     *
     * @since 1.0.7
     */
    public SqlBuilder() {
        this.sql = new SQL();
    }

    /**
     * 自定义 SQL 生成器
     *
     * @param sql {@link SQL}
     *
     * @since 1.0.7
     */
    public SqlBuilder(SQL sql) {
        this.sql = sql;
    }

    /**
     * 定义分页
     *
     * @param offset 页偏移
     * @param size 页大小
     *
     * @since 1.0.7
     */
    public SqlBuilder(int offset, int size) {
        this.sql = new SQL();
        page(offset, size);
    }

    /**
     * 定义分页和SQL生成器
     *
     * @param sql {@link SQL}
     * @param offset 页偏移
     * @param size 页大小
     *
     * @since 1.0.7
     */
    public SqlBuilder(SQL sql, int offset, int size) {
        this.sql = sql;
        page(offset, size);
    }

    /**
     * 更新
     *
     * @param table 表名
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder update(String table) {
        sql.UPDATE(table);
        return this;
    }

    /**
     * 更新
     *
     * @param sets 列值集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder set(String sets) {
        sql.SET(sets);
        return this;
    }

    /**
     * 更新
     *
     * @param sets 列值集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder set(String... sets) {
        sql.SET(sets);
        return this;
    }

    /**
     * 插入
     *
     * @param tableName 表名
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder insertInto(String tableName) {
        sql.INSERT_INTO(tableName);
        return this;
    }

    /**
     * 插入
     *
     * @param columns 列集合
     * @param values 值集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder values(String columns, String values) {
        sql.VALUES(columns, values);
        return this;
    }

    /**
     * 插入
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder intoColumns(String... columns) {
        sql.INTO_COLUMNS(columns);
        return this;
    }

    /**
     * 插入
     *
     * @param values 值集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder intoValues(String... values) {
        sql.INTO_VALUES(values);
        return this;
    }

    /**
     * 查找
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder select(String columns) {
        sql.SELECT(columns);
        return this;
    }

    /**
     * 查找
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder select(String... columns) {
        sql.SELECT(columns);
        return this;
    }

    /**
     * 查找（唯一）
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder selectDistinct(String columns) {
        sql.SELECT_DISTINCT(columns);
        return this;
    }

    /**
     * 查找（唯一）
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder selectDistinct(String... columns) {
        sql.SELECT_DISTINCT(columns);
        return this;
    }

    /**
     * 删除
     *
     * @param table 表名
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder deleteFrom(String table) {
        sql.DELETE_FROM(table);
        return this;
    }

    /**
     * 查找
     *
     * @param table 表名
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder from(String table) {
        sql.FROM(table);
        return this;
    }

    /**
     * 查找
     *
     * @param tables 表集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder from(String... tables) {
        sql.FROM(tables);
        return this;
    }

    /**
     * 多表连接
     *
     * @param join 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder join(String join) {
        sql.JOIN(join);
        return this;
    }

    /**
     * 多表连接
     *
     * @param joins 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder join(String... joins) {
        sql.JOIN(joins);
        return this;
    }

    /**
     * 内连接
     *
     * @param join 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder innerJoin(String join) {
        sql.INNER_JOIN(join);
        return this;
    }

    /**
     * 内连接
     *
     * @param joins 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder innerJoin(String... joins) {
        sql.INNER_JOIN(joins);
        return this;
    }

    /**
     * 左外连接
     *
     * @param join 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder leftOuterJoin(String join) {
        sql.LEFT_OUTER_JOIN(join);
        return this;
    }

    /**
     * 左外连接
     *
     * @param joins 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder leftOuterJoin(String... joins) {
        sql.LEFT_OUTER_JOIN(joins);
        return this;
    }

    /**
     * 右外连接
     *
     * @param join 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder rightOuterJoin(String join) {
        sql.RIGHT_OUTER_JOIN(join);
        return this;
    }

    /**
     * 右外连接
     *
     * @param joins 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder rightOuterJoin(String... joins) {
        sql.RIGHT_OUTER_JOIN(joins);
        return this;
    }

    /**
     * 外连接
     *
     * @param join 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder outerJoin(String join) {
        sql.OUTER_JOIN(join);
        return this;
    }

    /**
     * 外连接
     *
     * @param joins 连接信息
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder outerJoin(String... joins) {
        sql.OUTER_JOIN(joins);
        return this;
    }

    /**
     * 条件
     *
     * @param conditions 条件集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder where(String conditions) {
        sql.WHERE(conditions);
        return this;
    }

    /**
     * 条件
     *
     * @param conditions 条件集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder where(String... conditions) {
        sql.WHERE(conditions);
        return this;
    }

    /**
     * 条件或
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder or() {
        sql.OR();
        return this;
    }

    /**
     * 条件与
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder and() {
        sql.AND();
        return this;
    }

    /**
     * 分组
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder groupBy(String columns) {
        sql.GROUP_BY(columns);
        return this;
    }

    /**
     * 分组
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder groupBy(String... columns) {
        sql.GROUP_BY(columns);
        return this;
    }

    /**
     * 聚合
     *
     * @param conditions 条件集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder having(String conditions) {
        sql.HAVING(conditions);
        return this;
    }

    /**
     * 聚合
     *
     * @param conditions 条件集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder having(String... conditions) {
        sql.HAVING(conditions);
        return this;
    }

    /**
     * 排序
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder orderBy(String columns) {
        sql.ORDER_BY(columns);
        return this;
    }

    /**
     * 排序
     *
     * @param columns 列集合
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder orderBy(String... columns) {
        sql.ORDER_BY(columns);
        return this;
    }

    /**
     * 分页
     *
     * @param offset 页偏移
     * @param size 页大小
     *
     * @return {@link SqlBuilder}
     *
     * @since 1.0.7
     */
    public SqlBuilder page(int offset, int size) {
        this.page = new Page(offset, size);
        return this;
    }

    /**
     * 生成 SQL 语句
     *
     * @return SQL 语句
     *
     * @since 1.0.7
     */
    public String build() {
        return sql.toString() + (Objects.isNull(page) ? "" : " limit " + (page.offset * page.size) + "," + page.size);
    }

    @Override
    public String toString() {
        return build();
    }

    /**
     * 分页
     */
    private class Page {

        /**
         * 页偏移
         *
         * @since 1.0.7
         */
        private int offset;

        /**
         * 页大小
         *
         * @since 1.0.7
         */
        private int size;

        /**
         * 分页
         *
         * @param offset 页偏移
         * @param size 页大小
         *
         * @since 1.0.7
         */
        private Page(int offset, int size) {
            this.offset = offset;
            this.size = size;
        }
    }
}
