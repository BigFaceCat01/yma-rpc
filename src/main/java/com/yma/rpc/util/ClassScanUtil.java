package com.yma.rpc.util;

import com.yma.rpc.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-08 11:25:00
 */
@Slf4j
public class ClassScanUtil {
    /**
     * 从包package中获取所有的Class
     *
     * @param basePackage 根包名
     * @param callback    过滤回调
     */
    public static void scan(String basePackage, FilterCallback callback) {
        if (Objects.isNull(basePackage) || basePackage.trim().length() == 0) {
            return;
        }
        //替换包名为文件路径分隔
        String packageDirName = basePackage.replace(CommonConstants.DOT, CommonConstants.SEPARATOR);
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                // 用于判断是目录还是jar包
                String protocol = url.getProtocol();
                if (CommonConstants.PROTOCOL_FILE.equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
                    // 加载目录下的class类文件
                    loadInFileSystem(basePackage, filePath, callback);
                } else if (CommonConstants.PROTOCOL_JAR.equals(protocol)) {
                    //加载jar包文件
                    loadInJar(url, packageDirName, callback);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }


    /**
     * 获取目录结构下class文件
     *
     * @param packageName 包名
     * @param packagePath 包路径
     * @param callback    回调
     */
    private static void loadInFileSystem(String packageName, String packagePath, FilterCallback callback) {
        File dir = new File(packagePath);
        // 如果不存在或者不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //获取包下的所有文件(.class结尾)以及目录
        File[] files = dir.listFiles(file -> (
                file.isDirectory()
                        || (file.getName().endsWith(CommonConstants.DOT_CLASS))));
        //file.listFiles可能返回null，所以进行判断
        if (Objects.isNull(files)) {
            return;
        }
        for (File file : files) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                loadInFileSystem(packageName + CommonConstants.DOT + file.getName(), file.getAbsolutePath(), callback);
                continue;
            }
            // 如果是java类文件 去掉后面的.class 只留下类名
            String className = file.getName().replace(CommonConstants.DOT_CLASS, CommonConstants.EMPTY_STRING);
            try {
                //class.forName内部默认会执行初始化,而loadClass不会执行
                Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass(packageName + CommonConstants.DOT + className);
                //执行回调
                callback.doFilter(aClass);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            }

        }
    }

    /**
     * 获取jar包下class文件
     *
     * @param url            url
     * @param packageDirName 根包目录
     * @param callback       回调
     */
    private static void loadInJar(URL url, String packageDirName, FilterCallback callback) {
        JarFile jar;
        try {
            jar = ((JarURLConnection) url.openConnection())
                    .getJarFile();
            log.info("loading jar >> {}", jar.getName());
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                //name去掉开头的'/'
                if (name.startsWith(CommonConstants.SEPARATOR)) {
                    name = name.substring(1);
                }
                // 如果前半部分和定义的包名不同
                if (!name.startsWith(packageDirName)) {
                    continue;
                }
                String fullName = name.replace(CommonConstants.SEPARATOR, CommonConstants.DOT);
                // 如果是一个.class文件
                if (name.endsWith(CommonConstants.DOT_CLASS)) {
                    // 去掉后面的".class" 获取真正的类名
                    String className = fullName.replace(CommonConstants.DOT_CLASS, CommonConstants.EMPTY_STRING);
                    //class.forName内部默认会执行初始化,而loadClass不会执行
                    Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                    //执行回调
                    callback.doFilter(aClass);
                }
            }
        } catch (Exception e) {
            log.error("load jar error : {} \r\n{}", e.getMessage(), e);
        }
    }

    /**
     * 查找类时过滤回调
     */
    public interface FilterCallback {
        /**
         * 过滤实现
         *
         * @param source 当前类
         */
        void doFilter(Class<?> source);
    }


    private ClassScanUtil() {
    }
}
