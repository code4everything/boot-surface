package org.code4everything.boot.encoder;

/**
 * 字段加密
 *
 * @author pantao
 * @since 2019/4/2
 **/
public interface FieldEncoder {

    /**
     * 对对象字段进行加密
     *
     * @param data 待加密的对象
     *
     * @return 是否进行了加密
     *
     * @since 1.1.0
     */
    boolean encode(Object data);
}
