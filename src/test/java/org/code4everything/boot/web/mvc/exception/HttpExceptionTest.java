package org.code4everything.boot.web.mvc.exception;

import org.junit.Test;

public class HttpExceptionTest {

    @Test
    public void testSetter() {
        CustomHttpException exception = new CustomHttpException();
        exception.setCode(400).setMsg("exception");
    }
}

class CustomHttpException extends HttpException {}
