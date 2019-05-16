package org.code4everything.boot.web.http;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RestTemplate 工具类
 *
 * @author pantao
 * @since 2019/5/16
 **/
public final class RestUtils {

    private static RestTemplate rest = null;

    private RestUtils() {}

    private static void checkRestTemplate() {
        if (Objects.isNull(rest)) {
            synchronized (RestUtils.class) {
                if (Objects.isNull(rest)) {
                    rest = new RestTemplate();
                    rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter() {

                        {
                            List<MediaType> mediaTypes = new ArrayList<>();
                            mediaTypes.add(MediaType.TEXT_PLAIN);
                            mediaTypes.add(MediaType.TEXT_HTML);
                            mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
                            setSupportedMediaTypes(mediaTypes);
                        }
                    });
                }
            }
        }
    }

    public static RestTemplate getTemplate() {
        checkRestTemplate();
        return rest;
    }

    public static RestTemplate newTemplate() {
        return new RestTemplate();
    }

    public static void get() {
        checkRestTemplate();
    }
}
