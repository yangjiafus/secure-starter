package com.ctspcl.secure.stater.wrapper;

import com.ctspcl.secure.stater.DecryptUtil;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/10
 **/
public class SecretRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public SecretRequestWrapper(HttpServletRequest request, String bodyCharacters) {
        super(request);
        if (StringUtils.hasText(bodyCharacters)){
            this.body = bodyCharacters.getBytes(Charset.forName(DecryptUtil.CHARSET_UTF_8));
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamWrapper(body);
    }

    @Override
    public int getContentLength() {
        if (body == null){
            return 0;
        }
        return body.length;
    }

    @Override
    public long getContentLengthLong() {
        if (body == null){
            return 0;
        }
        return body.length;
    }

}
