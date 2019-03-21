package org.code4everything.boot.bean;

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
public class WePage<T> implements BaseBean, Serializable {

    private Integer currPage;

    private Integer pageSize;

    private Integer totalPages;

    private Long totalElements;

    private transient Collection<T> content;

    /**
     * 无参构造
     */
    public WePage() {}

    /**
     * 构造方法
     *
     * @param currPage 当前页
     * @param pageSize 页大小
     *
     * @since 1.0.9
     */
    public WePage(Integer currPage, Integer pageSize) {
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
    public WePage(Integer currPage, Integer pageSize, Integer totalPages) {
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
    public WePage(Integer currPage, Integer pageSize, Long totalElements) {
        this(currPage, pageSize, totalElements, new ArrayList<>());
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
    public WePage(Integer currPage, Integer pageSize, Integer totalElements, Collection<T> content) {
        this(currPage, pageSize, Long.valueOf(totalElements), content);
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
    public WePage(Integer currPage, Integer pageSize, Collection<T> content) {
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
    public WePage(Integer currPage, Integer pageSize, Long totalElements, Collection<T> content) {
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
    public Integer getElementSize() {
        return CollUtil.isEmpty(content) ? 0 : content.size();
    }

    /**
     * 获取总元素数量
     *
     * @return 总元素数量
     *
     * @since 1.0.9
     */
    public Long getTotalElements() {
        return totalElements;
    }

    /**
     * 设置总元素数量，总页数会发生响应的变化
     *
     * @param totalElements 总元素数量
     *
     * @return {@link WePage}
     *
     * @since 1.0.9
     */
    public WePage<T> setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
        computeTotalPage();
        return this;
    }

    /**
     * 设置总元素数量，总页数会发生响应的变化
     *
     * @param totalElements 总元素数量
     *
     * @return {@link WePage}
     *
     * @since 1.0.9
     */
    public WePage<T> setTotalElements2(Integer totalElements) {
        this.totalElements = Long.valueOf(totalElements);
        computeTotalPage();
        return this;
    }


    /**
     * 计算总页数
     *
     * @since 1.0.9
     */
    private void computeTotalPage() {
        if (ObjectUtil.isNotNull(pageSize) && pageSize > 0 && ObjectUtil.isNotNull(totalElements) && totalElements >= 0) {
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
    public Integer getTotalPages() {
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
    public WePage<T> setTotalPages(Integer totalPages) {
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

    @Override
    public String toString() {
        return "WePage{" + "currPage=" + currPage + ", pageSize=" + pageSize + ", totalPages=" + totalPages + ", " +
                "totalElements=" + totalElements + ", content=" + content + '}';
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
