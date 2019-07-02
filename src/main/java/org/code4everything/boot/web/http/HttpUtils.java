package org.code4everything.boot.web.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.code4everything.boot.base.constant.MessageConsts;
import org.code4everything.boot.base.constant.StringConsts;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.service.BootFileService;
import org.code4everything.boot.web.mvc.AssertUtils;
import org.code4everything.boot.web.mvc.Response;
import org.code4everything.boot.web.mvc.exception.ExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpStatus;
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
public final class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {}

    /**
     * 请求是否包括某个参数
     *
     * @param request {@link HttpServletRequest}
     * @param keys 参数数组
     *
     * @return 是否包含某个参数
     *
     * @since 1.1.2
     */
    public static boolean hasParamterAny(HttpServletRequest request, String... keys) {
        if (ArrayUtil.isEmpty(keys)) {
            return true;
        }
        Map map = request.getParameterMap();
        for (String key : keys) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 请求是否包含全部参数
     *
     * @param request {@link HttpServletRequest}
     * @param keys 参数数组
     *
     * @return 是否包含全部参数
     *
     * @since 1.1.2
     */
    public static boolean hasParamters(HttpServletRequest request, String... keys) {
        if (ArrayUtil.isNotEmpty(keys)) {
            Map map = request.getParameterMap();
            for (String key : keys) {
                if (!map.containsKey(key)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 要求指定的参数不能为空指针
     *
     * @param request {@link HttpServletRequest}
     * @param key 参数
     *
     * @return 参数值
     *
     * @since 1.1.2
     */
    public static String requireParameter(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        if (ObjectUtil.isNotNull(value)) {
            return value;
        }
        throw ExceptionFactory.exception(HttpStatus.BAD_REQUEST, String.format("parameter '%s' must no be empty", key));
    }

    /**
     * 要求指定的参数不能为空指针
     *
     * @param request {@link HttpServletRequest}
     * @param keys 参数集合
     *
     * @since 1.1.2
     */
    public static void requireParameters(HttpServletRequest request, String... keys) {
        if (ArrayUtil.isNotEmpty(keys)) {
            for (String key : keys) {
                requireParameter(request, key);
            }
        }
    }

    /**
     * 是否是设置的成功响应码
     *
     * @param response {@link Response}
     *
     * @return 是否是设置的成功响应码
     *
     * @since 1.1.0
     */
    public static boolean isOk(Response<?> response) {
        return ObjectUtil.isNotNull(response) && response.isOk();
    }

    /**
     * 是否是2xx成功响应码
     *
     * @param response {@link Response}
     *
     * @return 是否是2xx成功响应码
     *
     * @since 1.1.0
     */
    public static boolean is2xxOk(Response<?> response) {
        return ObjectUtil.isNotNull(response) && response.getCode() >= 200 && response.getCode() < 300;
    }

    /**
     * 获取整型
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     * @param defaultValue 默认值
     *
     * @return 整型
     *
     * @since 1.1.1
     */
    public static int getIntegerValue(HttpServletRequest request, String key, int defaultValue) {
        String value = request.getParameter(key);
        return Objects.isNull(value) ? defaultValue : Integer.parseInt(value);
    }

    /**
     * 获取整型
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     *
     * @return 整型
     *
     * @since 1.1.1
     */
    public static int getIntegerValue(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        return Objects.isNull(value) ? 0 : Integer.parseInt(value);
    }

    /**
     * 获取整型
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     *
     * @return 整型
     *
     * @since 1.1.1
     */
    public static Integer getInteger(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        return Objects.isNull(value) ? null : Integer.parseInt(value);
    }

    /**
     * 获取整型
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     * @param defaultValue 默认值
     *
     * @return 整型
     *
     * @since 1.1.1
     */
    public static Integer getInteger(HttpServletRequest request, String key, Integer defaultValue) {
        String value = request.getParameter(key);
        return Objects.isNull(value) ? defaultValue : Integer.parseInt(value);
    }

    /**
     * 获取布尔值
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     *
     * @return 布尔值
     *
     * @since 1.1.0
     */
    public static boolean getBooleanValue(HttpServletRequest request, String key) {
        return getBooleanValue(request, key, false);
    }

    /**
     * 获取布尔值
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     * @param defaultValue 默认值
     *
     * @return 布尔值
     *
     * @since 1.1.0
     */
    public static boolean getBooleanValue(HttpServletRequest request, String key, boolean defaultValue) {
        String value = getString(request, key);
        if (isTrue(value)) {
            return true;
        }
        if (isFalse(value)) {
            return false;
        }
        return defaultValue;
    }

    /**
     * 获取布尔值
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     *
     * @return 布尔值
     *
     * @since 1.1.0
     */
    public static Boolean getBoolean(HttpServletRequest request, String key) {
        return getBoolean(request, key, false);
    }

    /**
     * 获取布尔值
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     * @param defaultValue 默认值
     *
     * @return 布尔值
     *
     * @since 1.1.0
     */
    public static Boolean getBoolean(HttpServletRequest request, String key, boolean defaultValue) {
        String value = getString(request, key);
        if (isTrue(value)) {
            return Boolean.TRUE;
        }
        if (isFalse(value)) {
            return Boolean.FALSE;
        }
        return defaultValue;
    }

    /**
     * 值是否等于 true, 1
     *
     * @param value 值
     *
     * @return 是否是true
     *
     * @since 1.1.2
     */
    private static boolean isTrue(String value) {
        if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
            return true;
        }
        if (StringConsts.Sign.ONE.equals(value)) {
            return true;
        }
        return false;
    }

    /**
     * 值是否等于 false, 0
     *
     * @param value 值
     *
     * @return 是否是false
     *
     * @since 1.1.2
     */
    private static boolean isFalse(String value) {
        if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            return true;
        }
        if (StringConsts.Sign.ZERO.equals(value)) {
            return true;
        }
        return false;
    }

    /**
     * 获取字符串
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     *
     * @return 字符串
     *
     * @since 1.1.0
     */
    public static String getString(HttpServletRequest request, String key) {
        return request.getParameter(key);
    }

    /**
     * 获取字符串
     *
     * @param request {@link HttpServletRequest}
     * @param key 键
     * @param defaultValue 默认值
     *
     * @return 字符串
     *
     * @since 1.1.0
     */
    public static String getString(HttpServletRequest request, String key, String defaultValue) {
        return ObjectUtil.defaultIfNull(request.getParameter(key), defaultValue);
    }

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
        // 获取输入流
        BufferedReader br = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;
        // 一行一行读取数据
        while ((line = br.readLine()) != null) {
            // 去掉首位多余的空格
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
        return getTokenAwesome(StringConsts.TOKEN, request);
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
        String token = getTokenAwesome(tokenKey, request);
        return StrUtil.isEmpty(token) && !StringConsts.TOKEN.equalsIgnoreCase(tokenKey) ? getToken(request) : token;
    }

    /**
     * 获取Token
     *
     * @param tokenKey 自定义Token Key
     * @param request HTTP请求
     *
     * @return Token
     *
     * @since 1.1.0
     */
    private static String getTokenAwesome(String tokenKey, HttpServletRequest request) {
        String token = request.getHeader(tokenKey);
        return StrUtil.isEmpty(token) ? request.getParameter(tokenKey) : token;
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

    // -----------------------------------------响应文件流---------------------------------------------------

    /**
     * 向浏览器响应文件
     *
     * @param service 文件服务 {@link BootFileService}
     * @param request HTTP请求
     * @param <T> 文件实体类型
     *
     * @return 文件流
     *
     * @throws IOException 可能发生的异常
     * @since 1.0.2
     */
    public static <T> ResponseEntity<InputStreamSource> responseFile(BootFileService<T> service,
                                                                     HttpServletRequest request) throws IOException {
        return responseFile(service.getLocalPathByAccessUrl(request.getRequestURI()));
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
            // 响应404
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentLength(file.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(file.getInputStream()));
    }

    // ----------------------------------网络多文件上传---------------------------------------------------

    /**
     * 批量上传文件
     *
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果
     *
     * @since 1.0.0
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(MultipartHttpServletRequest request,
                                                                   String storage, boolean md5) {
        return multiUpload(new BootFileService<T>() {}, request, storage, md5, null, false);
    }

    /**
     * 批量上传文件
     *
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     * @param force 是否强制写入文件
     *
     * @return 响应结果
     *
     * @since 1.0.4
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(MultipartHttpServletRequest request,
                                                                   String storage, boolean md5, boolean force) {
        return multiUpload(new BootFileService<T>() {}, request, storage, md5, null, force);
    }

    /**
     * 批量上传文件
     *
     * @param service 文件服务 {@link BootFileService}
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果
     *
     * @since 1.0.0
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(BootFileService<T> service,
                                                                   MultipartHttpServletRequest request,
                                                                   String storage, boolean md5) {
        return multiUpload(service, request, storage, md5, null, false);
    }

    /**
     * 批量上传文件
     *
     * @param service 文件服务 {@link BootFileService}
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     * @param force 是否强制写入文件
     *
     * @return 响应结果
     *
     * @since 1.0.4
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(BootFileService<T> service,
                                                                   MultipartHttpServletRequest request,
                                                                   String storage, boolean md5, boolean force) {
        return multiUpload(service, request, storage, md5, null, force);
    }

    /**
     * 批量上传文件
     *
     * @param service 文件服务 {@link BootFileService}
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link BootFileService}方法中使用
     * @param <T> 数据表类型
     *
     * @return 响应结果
     *
     * @since 1.0.0
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(BootFileService<T> service,
                                                                   MultipartHttpServletRequest request,
                                                                   String storage, boolean md5,
                                                                   Map<String, Object> params) {
        return multiUpload(service, request, storage, md5, params, false);
    }


    /**
     * 批量上传文件
     *
     * @param service 文件服务 {@link BootFileService}
     * @param request 文件请求 {@link MultipartHttpServletRequest}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link BootFileService}方法中使用
     * @param <T> 数据表类型
     * @param force 是否强制写入文件
     *
     * @return 响应结果
     *
     * @since 1.0.4
     */
    public static <T> Response<ArrayList<Response<T>>> multiUpload(BootFileService<T> service,
                                                                   MultipartHttpServletRequest request,
                                                                   String storage, boolean md5,
                                                                   Map<String, Object> params, boolean force) {
        Map<String, MultipartFile> fileMap = request.getFileMap();
        if (CollUtil.isEmpty(fileMap)) {
            // 文件集合为空
            return new Response<>(HttpStatus.BAD_REQUEST.value(), MessageConsts.FILE_UNAVAILABLE_ZH);
        } else {
            ArrayList<Response<T>> list = new ArrayList<>();
            Collection<MultipartFile> set = fileMap.values();
            // 遍历的上传每个文件，并记录结果
            set.forEach(f -> list.add(upload(service, f, storage, md5, params, force)));
            return new Response<>(list);
        }
    }

    // ----------------------------------------网络单文件上传----------------------------------------------------

    /**
     * 文件上传（无数据表）
     *
     * @param file 文件 {@link MultipartFile}
     * @param storagePath 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果上传成功，{@link Response#getMsg()}返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T> Response<T> upload(MultipartFile file, String storagePath, boolean md5) {
        return upload(new BootFileService<T>() {}, file, storagePath, md5, null, false);
    }

    /**
     * 文件上传（无数据表）
     *
     * @param file 文件 {@link MultipartFile}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     * @param force 是否强制写入文件
     *
     * @return 响应结果 {@link Response}。如果上传成功，{@link Response#getMsg()}返回文件的MD5文件名
     *
     * @since 1.0.4
     */
    public static <T> Response<T> upload(MultipartFile file, String storage, boolean md5, boolean force) {
        return upload(new BootFileService<T>() {}, file, storage, md5, null, force);
    }

    /**
     * 文件上传
     *
     * @param service 文件服务 {@link BootFileService}
     * @param file 文件 {@link MultipartFile}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T> Response<T> upload(BootFileService<T> service, MultipartFile file, String storage, boolean md5) {
        return upload(service, file, storage, md5, null, false);
    }

    /**
     * 文件上传
     *
     * @param service 文件服务 {@link BootFileService}
     * @param file 文件 {@link MultipartFile}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     * @param force 是否强制写入文件
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.4
     */
    public static <T> Response<T> upload(BootFileService<T> service, MultipartFile file, String storage, boolean md5,
                                         boolean force) {
        return upload(service, file, storage, md5, null, force);
    }

    /**
     * 文件上传
     *
     * @param service 文件服务 {@link BootFileService}
     * @param file 文件 {@link MultipartFile}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link BootFileService}方法中使用
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T> Response<T> upload(BootFileService<T> service, MultipartFile file, String storage, boolean md5,
                                         Map<String, Object> params) {
        return upload(service, file, storage, md5, params, false);
    }

    /**
     * 文件上传
     *
     * @param service 文件服务 {@link BootFileService}
     * @param file 文件 {@link MultipartFile}
     * @param storage 文件存储路径，如：/root/boot/
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link BootFileService}方法中使用
     * @param <T> 数据表类型
     * @param force 是否强制写入文件
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.0.0
     */
    public static <T> Response<T> upload(BootFileService<T> service, MultipartFile file, String storage, boolean md5,
                                         Map<String, Object> params, boolean force) {
        Response<T> response = new Response<>();
        // 文件空指针检测
        if (Objects.isNull(file)) {
            return response.error("file is null");
        }
        // 检测文件大小是否超标
        if (file.getSize() > BootConfig.getMaxUploadFileSize()) {
            return response.error("file size must less than " + BootConfig.getMaxUploadFileSize());
        }
        // 格式化存储路径
        DustFile dustFile = new DustFile();
        FileUtil.mkdir(storage);
        dustFile.setStoragePath(storage + (storage.endsWith(File.separator) ? "" : File.separator));
        // 设置文件信息
        String ofn = file.getOriginalFilename();
        if (md5) {
            try {
                // 设置MD5
                dustFile.setMd5(DigestUtil.md5Hex(file.getBytes()));
            } catch (Exception e) {
                LOGGER.error("get md5 of file[{}] failed, message -> {}", ofn, e.getMessage());
                return response.error(HttpStatus.SERVICE_UNAVAILABLE.value(), ofn + " upload failed");
            }
            dustFile.setFilename(dustFile.getMd5() + StrUtil.DOT + FileUtil.extName(ofn));
        } else {
            dustFile.setFilename(ofn);
        }
        dustFile.setOriginalFilename(ofn).setSize(file.getSize()).setParams(params);
        // 检测文件是否存在
        Boolean exists = service.exists(dustFile);
        boolean shouldWrite = false;
        T t = null;
        if (Objects.isNull(exists)) {
            // 如果进入这里，表示用户没有实现exists方法
            t = service.getBy(dustFile);
            if (Objects.isNull(t)) {
                // 不存在时则可以写入磁盘
                shouldWrite = true;
            }
        } else if (!exists) {
            // 不存在时则可以写入磁盘
            shouldWrite = true;
        }
        if (shouldWrite || force) {
            try {
                // 写入磁盘
                file.transferTo(new File(dustFile.getStoragePath() + dustFile.getFilename()));
            } catch (Exception e) {
                LOGGER.error("upload file failed, message -> {}", e.getMessage());
                return response.error(HttpStatus.SERVICE_UNAVAILABLE.value(), ofn + " upload failed");
            }
            // 将数据写入数据库
            t = service.save(dustFile, t);
        }
        return Objects.isNull(t) ? response.setMsg(dustFile.getFilename()) : response.setData(t);
    }

    // ------------------------------------------------字节数组-----------------------------------------

    /**
     * 文件上传
     *
     * @param bytes 文件字节流
     * @param storage 文件存储路径，如：/root/boot/
     * @param name 文件名
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.1.0
     */
    public static <T> Response<T> upload(byte[] bytes, String storage, String name, boolean md5) {
        return upload(bytes, storage, name, md5, false);
    }

    /**
     * 文件上传
     *
     * @param bytes 文件字节流
     * @param storage 文件存储路径，如：/root/boot/
     * @param name 文件名
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param force 是否强制写入文件
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.1.0
     */
    public static <T> Response<T> upload(byte[] bytes, String storage, String name, boolean md5, boolean force) {
        return upload(new BootFileService<T>() {}, bytes, storage, name, md5, null, force);
    }

    /**
     * 文件上传
     *
     * @param service 文件服务 {@link BootFileService}
     * @param bytes 文件字节流
     * @param storage 文件存储路径，如：/root/boot/
     * @param name 文件名
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.1.0
     */
    public static <T> Response<T> upload(BootFileService<T> service, byte[] bytes, String storage, String name,
                                         boolean md5) {
        return upload(service, bytes, storage, name, md5, null, false);
    }

    /**
     * 文件上传
     *
     * @param service 文件服务 {@link BootFileService}
     * @param bytes 文件字节流
     * @param storage 文件存储路径，如：/root/boot/
     * @param name 文件名
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param force 是否强制写入文件
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.1.0
     */
    public static <T> Response<T> upload(BootFileService<T> service, byte[] bytes, String storage, String name,
                                         boolean md5, boolean force) {
        return upload(service, bytes, storage, name, md5, null, force);
    }

    /**
     * 文件上传
     *
     * @param service 文件服务 {@link BootFileService}
     * @param bytes 文件字节流
     * @param storage 文件存储路径，如：/root/boot/
     * @param name 文件名
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link BootFileService}方法中使用
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.1.0
     */
    public static <T> Response<T> upload(BootFileService<T> service, byte[] bytes, String storage, String name,
                                         boolean md5, Map<String, Object> params) {
        return upload(service, bytes, storage, name, md5, params, false);
    }

    /**
     * 文件上传
     *
     * @param service 文件服务 {@link BootFileService}
     * @param bytes 文件字节流
     * @param storage 文件存储路径，如：/root/boot/
     * @param name 文件名
     * @param md5 是否计算文件的MD5码（大文件不建议计算，防止堆内存泄漏）
     * @param params 自定义参数，在自己实现的 {@link BootFileService}方法中使用
     * @param force 是否强制写入文件
     * @param <T> 数据表类型
     *
     * @return 响应结果 {@link Response}。如果文件上传成功且最后得到的 {@link Response#getData()}为NULL，则{@link
     *         Response#getMsg()}将返回文件的MD5文件名
     *
     * @since 1.1.0
     */
    public static <T> Response<T> upload(BootFileService<T> service, byte[] bytes, String storage, String name,
                                         boolean md5, Map<String, Object> params, boolean force) {
        Response<T> response = new Response<>();
        // 检测文件大小是否超标
        if (bytes.length > BootConfig.getMaxUploadFileSize()) {
            return response.error("file size must less than " + BootConfig.getMaxUploadFileSize());
        }
        // 格式化存储路径
        DustFile dustFile = new DustFile();
        FileUtil.mkdir(storage);
        dustFile.setStoragePath(storage + (storage.endsWith(File.separator) ? "" : File.separator));
        // 设置文件信息
        if (md5) {
            try {
                // 设置MD5
                dustFile.setMd5(DigestUtil.md5Hex(bytes));
            } catch (Exception e) {
                LOGGER.error("get md5 of file[{}] failed, message -> {}", name, e.getMessage());
                return response.error(HttpStatus.SERVICE_UNAVAILABLE.value(), name + " upload failed");
            }
            dustFile.setFilename(dustFile.getMd5() + StrUtil.DOT + FileUtil.extName(name));
        } else {
            dustFile.setFilename(name);
        }
        dustFile.setOriginalFilename(name).setSize((long) bytes.length).setParams(params);
        // 检测文件是否存在
        Boolean exists = service.exists(dustFile);
        boolean shouldWrite;
        T t = null;
        if (Objects.isNull(exists)) {
            t = service.getBy(dustFile);
            shouldWrite = Objects.isNull(t);
        } else {
            shouldWrite = !exists;
        }
        if (shouldWrite || force) {
            try {
                // 写入磁盘
                FileUtil.writeBytes(bytes, dustFile.getStoragePath() + dustFile.getFilename());
            } catch (Exception e) {
                LOGGER.error("upload file failed, message -> {}", e.getMessage());
                return response.error(HttpStatus.SERVICE_UNAVAILABLE.value(), name + " upload failed");
            }
            // 将数据写入数据库
            t = service.save(dustFile, t);
        }
        return Objects.isNull(t) ? response.setMsg(dustFile.getFilename()) : response.setData(t);
    }
}
