package org.code4everything.boot.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 简单的分页信息
 *
 * @author pantao
 * @since 2019/3/20
 **/
public class WePage<T> implements BaseBean, Serializable {

    private Integer currPage;

    private Integer pageSize;

    private Integer totalPage;

    private transient Collection<T> content;

    /**
     * 无参构造
     */
    public WePage() {}

    /**
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param totalPage 总页数
     *
     * @since 1.0.9
     */
    public WePage(Integer currPage, Integer pageSize, Integer totalPage) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        content = new ArrayList<>();
    }

    /**
     * 获取当前页
     *
     * @return 当前页
     *
     * @since 1.0.9
     */
    public Integer getCurrPage() {
        return currPage;
    }

    /**
     * 设置当前页
     *
     * @param currPage 当前页
     *
     * @return {@link WePage}
     *
     * @since 1.0.9
     */
    public WePage<T> setCurrPage(Integer currPage) {
        this.currPage = currPage;
        return this;
    }

    /**
     * 获取页大小
     *
     * @return 页大小
     *
     * @since 1.0.9
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置页大小
     *
     * @param pageSize 页大小
     *
     * @return {@link WePage}
     *
     * @since 1.0.9
     */
    public WePage<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     *
     * @since 1.0.9
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * 设置总页数
     *
     * @param totalPage 总页数
     *
     * @return 总页数
     *
     * @since 1.0.9
     */
    public WePage<T> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    /**
     * 获取数据
     *
     * @return 数据
     *
     * @since 1.0.9
     */
    public Collection<T> getContent() {
        return content;
    }

    /**
     * 设置数据集
     *
     * @param content 数据集
     *
     * @return {@link WePage}
     *
     * @since 1.0.9
     */
    public WePage<T> setContent(Collection<T> content) {
        this.content = content;
        return this;
    }

    /**
     * 添加数据
     *
     * @param data 数据
     *
     * @return {@link WePage}
     *
     * @since 1.0.9
     */
    public WePage<T> add(T data) {
        content.add(data);
        return this;
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeInt(currPage);
        outputStream.writeInt(pageSize);
        outputStream.writeInt(totalPage);
        outputStream.writeObject(content);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        currPage = inputStream.readInt();
        pageSize = inputStream.readInt();
        totalPage = inputStream.readInt();
        content = (Collection<T>) inputStream.readObject();
    }
}
