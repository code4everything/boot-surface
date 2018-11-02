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
import java.io.IOException;
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

    public static <T extends Serializable> ResponseResult<T> upload(FileService<T> fileService, MultipartFile file,
                                                                    String storagePath,
                                                                    Map<String, Serializable> otherParam) throws IOException {
        ResponseResult<T> result = new ResponseResult<>();
        MultipartFileBean fileBean = new MultipartFileBean();
        // 设置文件信息
        fileBean.setMd5(new Digester(DigestAlgorithm.MD5).digestHex(file.getBytes()));
        fileBean.setOriginalFilename(file.getOriginalFilename()).setSize(file.getSize());
        fileBean.setFilename(fileBean.getMd5() + FileUtils.getSuffix(file.getOriginalFilename()));
        fileBean.setOtherParams(otherParam);
        // 检测文件是否存在
        Boolean exists = fileService.exists(fileBean);
        boolean shouldWrite = false;
        T t;
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
                return result.setCode(IntegerConsts.FOUR_HUNDRED).setMsg(StrUtil.format("{} upload failed",
                        fileBean.getOriginalFilename()));
            }
            // 将数据写入数据库
            t = fileService.save(fileBean);
            if (Validator.isNotNull(t)) {
                result.setData(t);
            }
        }
        return result;
    }
}
