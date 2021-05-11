package com.thunder.transport.socket.util;

import com.thunder.entity.RpcRequest;
import com.thunder.enumeration.PackageType;
import com.thunder.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Socket方式将数据序列化并写入输出流中【编码】
 */
public class ObjectWriter {

    private static final Logger logger = LoggerFactory.getLogger(ObjectWriter.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static void writeObject(OutputStream out, Object object, CommonSerializer serializer) throws IOException {
        out.write(intToBytes(MAGIC_NUMBER));
        if(object instanceof RpcRequest) {
            out.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            out.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
        byte[] bytes1 = intToBytes(serializer.getCode());
        out.write(bytes1);
        byte[] bytes = serializer.serialize(object);
        out.write(intToBytes(bytes.length));
        out.write(bytes);
        out.flush();
    }

    /**
     * @description 将Int转换为字节数组
     *
     * @return [byte[]]
     * @date [2021-03-10 22:15]
     */
    private static byte[] intToBytes(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }
}