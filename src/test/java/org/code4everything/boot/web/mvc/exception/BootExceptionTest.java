package org.code4everything.boot.web.mvc.exception;

import org.junit.Test;

public class BootExceptionTest {

    @Test
    public void testSetter() {
        CustomBootException exception = new CustomBootException();
        exception.setCode(400).setMsg("exception");
    }
}

class CustomBootException extends BootException {}
