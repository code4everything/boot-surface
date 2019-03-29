package org.code4everything.boot.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpStatus;
import org.code4everything.boot.base.AssertUtils;
import org.code4everything.boot.bean.MultipartFileBean;
import org.code4everything.boot.bean.Response;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.MessageConsts;
import org.code4everything.boot.constant.StringConsts;
import org.code4everything.boot.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 网络工具类
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {}

    /**
     * 解析HTTP请求中的Body
     *
     * @param request {@link HttpServletRequest}
     *
     * @return Body
     *
     * @throws IOException 异常
     * @since 1.1.0
     */
    public static String parseRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            builder.append(line.trim());
        }
        return builder.toString();
    }

    /**
     * 获取Token
     *
     * @param request HTTP请求
     *
     * @return Token
     *
     * @since 1.0.4
     */
    public static String getToken(HttpServletRequest request) {
        return getToken(StringConsts.TOKEN, request);
    }

    /**
     * 获取Token
     *
     * @param tokenKey 自定义Token Key
     * @param request HTTP请求
     *
     * @return Token
     *
     * @since 1.0.9
     */
    public static String getToken(String tokenKey, HttpServletRequest request) {
        String token = request.getHeader(tokenKey);
        return StrUtil.isBlank(token) ? request.getParameter(tokenKey) : token;
    }


    /**
     * 获取Token
     *
     * @param request HTTP请求
     *
     * @return Token
     *
     * @since 1.0.4
     */
    public static String requireToken(HttpServletRequest request) {
        return AssertUtils.assertTokenNotBlank(getToken(request));
    }

    /**
     * 向浏览器响应文件
     *
     * @param fileService 文件服务 {@link FileService}
     * @param request HTTP请求
     * @param <T> 文件实体类型
     *
     * @return 文件流
     *
     * @throws IOException 可能发生的异常
     * @since 1.0.2
     */
    public static <T> ResponseEntity<InputStreamSource> responseFile(FileService<T> fileService,
                                                                     HttpServletRequest request) throws IOException {
        return responseFile(fileService.getLocalPathByAccessUrl(request.getServletPath()));
    }

    /**
     * 向浏览器响应文件
     *
     * @param localPath 文件本地路径
     *
     * @return 文件流
     *
     * @throws IOException 可能发生的异常
     * @since 1.0.2
     */
    public static ResponseEntity<InputStreamSource> responseFile(String localPath) throws IOException {
        FileSystemResource file = null;
        if (FileUtil.exist(localPath)) {
            file = new FileSystemResource(localPath);
        }
        if (ObjectUtil.isNull(file)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentLength(file.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(file.getInputStream()));
    }

    /**
     * 批量上传文件
     *
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果
     *
     * @since 1.0.0
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(MultipartHttpServletRequest request,
                                                                   String storagePath, boolean digestBytes) {
        return multiUpload(new FileService<T>() {}, request, storagePath, digestBytes, null, false);
    }

    /**
     * 批量上传文件
     *
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     * @param forceWrite 是否强制写入文件
     *
     * @return 响应结果
     *
     * @since 1.0.4
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(MultipartHttpServletRequest request,
                                                                   String storagePath, boolean digestBytes,
                                                                   boolean forceWrite) {
        return multiUpload(new FileService<T>() {}, request, storagePath, digestBytes, null, forceWrite);
    }

    /**
     * 批量上传文件
     *
     * @param fileService 文件服务 {@link FileService}
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果
     *
     * @since 1.0.0
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(FileService<T> fileService,
                                                                   MultipartHttpServletRequest request,
                                                                   String storagePath, boolean digestBytes) {
        return multiUpload(fileService, request, storagePath, digestBytes, null, false);
    }

    /**
     * 批量上传文件
     *
     * @param fileService 文件服务 {@link FileService}
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     * @param forceWrite 是否强制写入文件
     *
     * @return 响应结果
     *
     * @since 1.0.4
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(FileService<T> fileService,
                                                                   MultipartHttpServletRequest request,
                                                                   String storagePath, boolean digestBytes,
                                                                   boolean forceWrite) {
        return multiUpload(fileService, request, storagePath, digestBytes, null, forceWrite);
    }

    /**
     * 批量上传文件
     *
     * @param fileService 文件服务 {@link FileService}
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link FileService}方法中使用
     * @param <T> 数据表类型
     *
     * @return 响应结果
     *
     * @since 1.0.0
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(FileService<T> fileService,
                                                                   MultipartHttpServletRequest request,
                                                                   String storagePath, boolean digestBytes,
                                                                   Map<String, Object> params) {
        return multiUpload(fileService, request, storagePath, digestBytes, params, false);
    }


    /**
     * 批量上传文件
     *
     * @param fileService 文件服务 {@link FileService}
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link FileService}方法中使用
     * @param <T> 数据表类型
     * @param forceWrite 是否强制写入文件
     *
     * @return 响应结果
     *
     * @since 1.0.4
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(FileService<T> fileService,
                                                                   MultipartHttpServletRequest request,
                                                                   String storagePath, boolean digestBytes,
                                                                   Map<String, Object> params, boolean forceWrite) {
        Map<String, MultipartFile> fileMap = request.getFileMap();
        if (CollectionUtil.isEmpty(fileMap)) {
            return new Response<>(HttpStatus.HTTP_BAD_REQUEST, MessageConsts.FILE_UNAVAILABLE_ZH);
        } else {
            ArrayList<Response<T>> list = new ArrayList<>();
            Collection<MultipartFile> set = fileMap.values();
            set.forEach(f -> list.add(upload(fileService, f, storagePath, digestBytes, params, forceWrite)));
            return new Response<>(list);
        }
    }

    /**
     * 文件上传（无数据表）
     *
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果上传成功，{@link Response#getMsg()}返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T> Response<T> upload(MultipartFile file, String storagePath, boolean digestBytes) {
        return upload(new FileService<T>() {}, file, storagePath, digestBytes, null, false);
    }

    /**
     * 文件上传（无数据表）
     *
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     * @param forceWrite 是否强制写入文件
     *
     * @return 响应结果 {@link Response}。如果上传成功，{@link Response#getMsg()}返回文件的MD5文件名
     *
     * @since 1.0.4
     */
    public static <T> Response<T> upload(MultipartFile file, String storagePath, boolean digestBytes,
                                         boolean forceWrite) {
        return upload(new FileService<T>() {}, file, storagePath, digestBytes, null, forceWrite);
    }

    /**
     * 文件上传
     *
     * @param fileService 文件服务 {@link FileService}
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T> Response<T> upload(FileService<T> fileService, MultipartFile file, String storagePath,
                                         boolean digestBytes) {
        return upload(fileService, file, storagePath, digestBytes, null, false);
    }

    /**
     * 文件上传
     *
     * @param fileService 文件服务 {@link FileService}
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     * @param forceWrite 是否强制写入文件
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.4
     */
    public static <T> Response<T> upload(FileService<T> fileService, MultipartFile file, String storagePath,
                                         boolean digestBytes, boolean forceWrite) {
        return upload(fileService, file, storagePath, digestBytes, null, forceWrite);
    }

    /**
     * 文件上传
     *
     * @param fileService 文件服务 {@link FileService}
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link FileService}方法中使用
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T> Response<T> upload(FileService<T> fileService, MultipartFile file, String storagePath,
                                         boolean digestBytes, Map<String, Object> params) {
        return upload(fileService, file, storagePath, digestBytes, params, false);
    }

    /**
     * 文件上传
     *
     * @param fileService 文件服务 {@link FileService}
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param digestBytes 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link FileService}方法中使用
     * @param <T> 数据表类型
     * @param forceWrite 是否强制写入文件
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T> Response<T> upload(FileService<T> fileService, MultipartFile file, String storagePath,
                                         boolean digestBytes, Map<String, Object> params, boolean forceWrite) {
        Response<T> result = new Response<>();
        if (file.getSize() > BootConfig.getMaxUploadFileSize()) {
            return result.error("file size must less than " + BootConfig.getMaxUploadFileSize());
        }
        MultipartFileBean fileBean = new MultipartFileBean();
        fileBean.setStoragePath(storagePath + (storagePath.endsWith(File.separator) ? "" : File.separator));
        // 设置文件信息
        String ofn = file.getOriginalFilename();
        if (digestBytes) {
            try {
                // 设置MD5
                fileBean.setMd5(DigestUtil.md5Hex(file.getBytes()));
            } catch (Exception e) {
                LOGGER.error("get md5 of file[{}] failed, message -> {}", ofn, e.getMessage());
                return result.error(HttpStatus.HTTP_UNAVAILABLE, ofn + " upload failed");
            }
            fileBean.setFilename(fileBean.getMd5() + StrUtil.DOT + FileUtil.extName(ofn));
        } else {
            fileBean.setFilename(ofn);
        }
        fileBean.setOriginalFilename(ofn).setSize(file.getSize()).setParams(params);
        // 检测文件是否存在
        Boolean exists = fileService.exists(fileBean);
        boolean shouldWrite = false;
        T t = null;
        if (Objects.isNull(exists)) {
            t = fileService.getBy(fileBean);
            if (Objects.isNull(t)) {
                // 不存在时则可以写入磁盘
                shouldWrite = true;
            }
        } else if (!exists) {
            // 不存在时则可以写入磁盘
            shouldWrite = true;
        }
        if (shouldWrite || forceWrite) {
            try {
                // 写入磁盘
                file.transferTo(new File(fileBean.getStoragePath() + fileBean.getFilename()));
            } catch (Exception e) {
                LOGGER.error("upload file failed, message -> {}", e.getMessage());
                return result.error(HttpStatus.HTTP_UNAVAILABLE, ofn + " upload failed");
            }
            // 将数据写入数据库
            t = fileService.save(fileBean, t);
        }
        return Objects.isNull(t) ? result.setMsg(fileBean.getFilename()) : result.setData(t);
    }
}
