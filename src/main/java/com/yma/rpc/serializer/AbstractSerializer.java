package com.yma.rpc.serializer;

import com.yma.rpc.serializer.impl.HessianSerializer;

/**
 * 序列化对象基类
 * @author Created by huang xiao bao
 * @date 2019-05-07 10:23:45
 */
public abstract class AbstractSerializer {
    /**
     * 将对象序列化为字节数组
     * @param obj 对象
     * @param <T> 泛型
     * @return 字节数组
     */
    public abstract <T> byte[] serialize(T obj);

    /**
     * 将字节数组转化为对象
     * @param bytes 字节数组
     * @param clazz 类
     * @param <T> 泛型
     * @return 对象
     */
    public abstract <T> T deserialize(byte[] bytes, Class<T> clazz);

    public enum SerializeEnum {
        /**
         * 使用hessian进行序列化
         */
        HESSIAN(HessianSerializer.class);

        private Class<? extends AbstractSerializer> serializerClass;

        SerializeEnum(Class<? extends AbstractSerializer> serializerClass) {
            this.serializerClass = serializerClass;
        }

        public AbstractSerializer getSerializer() {
            try {
                return serializerClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static SerializeEnum match(String name, SerializeEnum defaultSerializer) {
            for (SerializeEnum item : SerializeEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
            return defaultSerializer;
        }
    }

}
