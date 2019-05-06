package org.code4everything.boot.base.bean;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

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
public class WeePage<T> implements BaseBean, Serializable {

    private static final long serialVersionUID = -7558939772837583007L;

    private int currPage = 1;

    private int pageSize = 1;

    private int totalPages = 0;

    private long totalElements = 0;

    private Collection<T> content;

    /**
     * 无参构造
     */
    public WeePage() {}

    /**
     * 构造方法
     *
     * @param currPage 当前页
     * @param pageSize 页大小
     *
     * @since 1.0.9
     */
    public WeePage(int currPage, int pageSize) {
        this(currPage, pageSize, new ArrayList<>());
    }

    /**
     * 构造方法
     *
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param totalPages 总页数
     *
     * @since 1.0.9
     */
    public WeePage(int currPage, int pageSize, int totalPages) {
        this(currPage, pageSize, new ArrayList<>());
        this.totalPages = totalPages;
    }

    /**
     * 构造方法
     *
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param totalElements 总元素数量
     *
     * @since 1.0.9
     */
    public WeePage(int currPage, int pageSize, long totalElements) {
        this(currPage, pageSize, totalElements, new ArrayList<>());
    }

    /**
     * 构造方法
     *
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param content 元素列表
     *
     * @since 1.0.9
     */
    public WeePage(int currPage, int pageSize, Collection<T> content) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        this.content = content;
    }

    /**
     * 构造方法
     *
     * @param currPage 当前页
     * @param pageSize 页大小
     * @param totalElements 总元素数量
     * @param content 元素列表
     *
     * @since 1.0.9
     */
    public WeePage(int currPage, int pageSize, long totalElements, Collection<T> content) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        computeTotalPage();
        this.content = content;
    }

    /**
     * 获取当前页元素数量
     *
     * @return 当前页元素数量
     *
     * @since 1.0.9
     */
    public int getElementSize() {
        return CollUtil.isEmpty(content) ? 0 : content.size();
    }

    /**
     * 获取总元素数量
     *
     * @return 总元素数量
     *
     * @since 1.0.9
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * 设置总元素数量，总页数会发生响应的变化
     *
     * @param totalElements 总元素数量
     *
     * @return {@link WeePage}
     *
     * @since 1.0.9
     */
    public WeePage<T> setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        computeTotalPage();
        return this;
    }

    /**
     * 计算总页数
     *
     * @since 1.0.9
     */
    private void computeTotalPage() {
        if (pageSize > 0 && totalElements >= 0) {
            totalPages = (int) (totalElements / pageSize) + (totalElements % pageSize > 0 ? 1 : 0);
        }
    }

    /**
     * 获取当前页
     *
     * @return 当前页
     *
     * @since 1.0.9
     */
    public int getCurrPage() {
        return currPage;
    }

    /**
     * 设置当前页
     *
     * @param currPage 当前页
     *
     * @return {@link WeePage}
     *
     * @since 1.0.9
     */
    public WeePage<T> setCurrPage(int currPage) {
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
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置页大小
     *
     * @param pageSize 页大小
     *
     * @return {@link WeePage}
     *
     * @since 1.0.9
     */
    public WeePage<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        computeTotalPage();
        return this;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     *
     * @since 1.0.9
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 设置总页数
     *
     * @param totalPages 总页数
     *
     * @return 总页数
     *
     * @since 1.0.9
     */
    public WeePage<T> setTotalPages(int totalPages) {
        this.totalPages = totalPages;
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
     * @return {@link WeePage}
     *
     * @since 1.0.9
     */
    public WeePage<T> setContent(Collection<T> content) {
        this.content = content;
        return this;
    }

    /**
     * 添加数据
     *
     * @param data 数据
     *
     * @return {@link WeePage}
     *
     * @since 1.0.9
     */
    public WeePage<T> add(T data) {
        content.add(data);
        return this;
    }

    @Override
    public String toString() {
        return "WeePage{" + "currPage=" + currPage + ", pageSize=" + pageSize + ", totalPages=" + totalPages + ", " + "totalElements=" + totalElements + ", content=" + content + '}';
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeInt(currPage);
        outputStream.writeInt(pageSize);
        outputStream.writeInt(totalPages);
        outputStream.writeLong(totalElements);
        outputStream.writeObject(ObjectUtil.serialize(content));
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        currPage = inputStream.readInt();
        pageSize = inputStream.readInt();
        totalPages = inputStream.readInt();
        totalElements = inputStream.readLong();
        content = ObjectUtil.unserialize((byte[]) inputStream.readObject());
    }
}
