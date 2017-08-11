package demo.pluto.maven.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import demo.pluto.maven.util.FileUtil;

/**
 * 批量读写Channel
 * <br/>
 * 批量读是指可以指定一个Buffer数组作为从Channel读出数据的存放区，数据会依据Buffer数组中的每个Buffer的顺序填充满每个Buffer。
 * </br>
 * 批量写是指可以指定一个Buffer数字作为写入Channel的数据存放区，数据会依据Buffer数组中的米格Buffer顺序，将每个Buffer的Position到Limit之间的数据写入Channel。
 * </br/>
 * 因此批量写是可以支持动态结构，批量读只支持静态结构
 * @author Pluto Xu a4yl9zz
 *
 */
public class ScattingAndGather
{
    public static void main(String args[]){
        gather();
    }
 
    /**
     * 批量写数据到Buffer
     * @author Pluto Xu a4yl9zz
     */
    public static void gather()
    {
        ByteBuffer header = ByteBuffer.allocate(10);
        ByteBuffer body = ByteBuffer.allocate(10);
 
        byte [] b1 = {'0', '1'};
        byte [] b2 = {'2', '3'};
        header.put(b1);
        body.put(b2);
 
        ByteBuffer [] buffs = {header, body};
 
        try
        {
            FileOutputStream os = new FileOutputStream(FileUtil.getFile("data/nio-data.txt"));
            FileChannel channel = os.getChannel();
            channel.write(buffs);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
