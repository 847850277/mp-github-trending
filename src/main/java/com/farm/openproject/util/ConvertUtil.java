package com.farm.openproject.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author zhengpeng
 * io转换工具
 */
public class ConvertUtil {

    /**
     * inputStream转outputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parse(final InputStream in) throws Exception {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }


    /**
     * outputStream转inputStream
     * @param out
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream parse(final OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    /**
     * inputStream转String
     * @param in
     * @return
     * @throws Exception
     */
    public static String parseToString(final InputStream in) throws Exception {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream.toString();
    }


    /**
     * OutputStream 转String
     * @param out
     * @return
     * @throws Exception
     */
    public static String parseToString(final OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream.toString();
    }


    /**
     * String转inputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream parseToinputStream(final String in) throws Exception {
        final ByteArrayInputStream input = new ByteArrayInputStream(in.getBytes());
        return input;
    }


    /**
     * String转outputStream
     * @param
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parseToOutputStream(final String in) throws Exception {
        return parse(parseToinputStream(in));
    }
}
