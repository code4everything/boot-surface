package org.code4everything.boot.starter.web;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import org.apache.log4j.Logger;
import org.code4everything.boot.starter.bean.MultipartFileBean;
import org.code4everything.boot.starter.service.FileService;
import org.code4everything.boot.xtool.bean.ResponseResult;
import org.code4everything.boot.xtool.constant.IntegerConsts;
import org.code4everything.boot.xtool.util.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * 网络工具类
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class HttpUtils {

    private static final Logger LOGGER = Logger.getLogger(HttpUtils.class);

    private HttpUtils() {}

    /**
     * 文件上传（无数据表）
     *
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link ResponseResult}，{@link ResponseResult#getMsg()}返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T extends Serializable> ResponseResult<T> upload(MultipartFile file, String storagePath) {
        return upload(new FileService<T>() {}, file, storagePath, null);
    }

    /**
     * 文件上传
     *
     * @param fileService 文件服务 {@link FileService}
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link ResponseResult}，如果最后得到的 {@link ResponseResult#getData()}为NULL，则{@link
     *         ResponseResult#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T extends Serializable> ResponseResult<T> upload(FileService<T> fileService, MultipartFile file,
                                                                    String storagePath) {
        return upload(fileService, file, storagePath, null);
    }

    /**
     * 文件上传
     *
     * @param fileService 文件服务 {@link FileService}
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param otherParam 附加参数，在自己实现的 {@link FileService}接口中使用
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link ResponseResult}，如果最后得到的 {@link ResponseResult#getData()}为NULL，则{@link
     *         ResponseResult#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T extends Serializable> ResponseResult<T> upload(FileService<T> fileService, MultipartFile file,
                                                                    String storagePath,
                                                                    Map<String, Serializable> otherParam) {
        ResponseResult<T> result = new ResponseResult<>();
        MultipartFileBean fileBean = new MultipartFileBean();
        // 设置文件信息
        String ofn = file.getOriginalFilename();
        try {
            fileBean.setMd5(new Digester(DigestAlgorithm.MD5).digestHex(file.getBytes()));
        } catch (Exception e) {
            LOGGER.error(StrUtil.format("get md5 of file[{}] failed, message -> {}", ofn, e.getMessage()));
            return result.setCode(IntegerConsts.FOUR_HUNDRED).setMsg(StrUtil.format("{} upload failed", ofn));
        }
        fileBean.setOriginalFilename(ofn).setSize(file.getSize()).setOtherParams(otherParam);
        fileBean.setFilename(fileBean.getMd5() + FileUtils.getSuffix(file.getOriginalFilename()));
        // 检测文件是否存在
        Boolean exists = fileService.exists(fileBean);
        boolean shouldWrite = false;
        T t = null;
        if (Validator.isNull(exists)) {
            t = fileService.getByMultipartFile(fileBean);
            if (Validator.isNull(t)) {
                // 不存在时则可以写入磁盘
                shouldWrite = true;
            }
        } else if (!exists) {
            // 不存在时则可以写入磁盘
            shouldWrite = true;
        }
        if (shouldWrite) {
            try {
                // 写入磁盘
                file.transferTo(new File(storagePath + fileBean.getFilename()));
            } catch (Exception e) {
                LOGGER.error("upload file failed, message -> " + e.getMessage());
                return result.setCode(IntegerConsts.FOUR_HUNDRED).setMsg(StrUtil.format("{} upload failed", ofn));
            }
            // 将数据写入数据库
            t = fileService.save(fileBean);
        }
        if (Validator.isNull(t)) {
            result.setMsg(fileBean.getFilename());
        } else {
            result.setData(t);
        }
        return result;
    }
}
