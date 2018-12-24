package org.code4everything.boot.bean;

import org.junit.Test;

import java.util.*;

public class ResponseResultTest {

    @Test
    public void testToString() {
        System.out.println(new ResponseResult(200, "test").toString());
    }

    @Test
    public void setCollectionData() {
        Map<String, String> test = new HashMap<>();
        test.put("test", "google");
        assert new ResponseResult<>(new HashMap<>(test)).equals(new ResponseResult<HashMap<String, String>>().castData(test));
        Collection<String> list = new LinkedList<>();
        assert new ResponseResult<>(new ArrayList<>(list)).equals(new ResponseResult<ArrayList<String>>().castData(list));
        assert !new ResponseResult<>().equals("");
    }
}
