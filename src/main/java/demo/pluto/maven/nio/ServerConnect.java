package demo.pluto.maven.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerConnect
{
    private static final int BUF_SIZE=1024;
    private static final int PORT = 18080;
    private static final int TIMEOUT = 3000;
 
    public static void main(String[] args)
    {
        selector();
    }
 
    public static void handleAccept(SelectionKey key) throws IOException{
        ServerSocketChannel ssChannel = (ServerSocketChannel)key.channel(); //获取键附加的Channel
        SocketChannel sc = ssChannel.accept(); //获取就绪接受的SocketChannel
        sc.configureBlocking(false); //设置为非阻塞状态
        sc.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocateDirect(BUF_SIZE)); //将Channel注册到Selector
    }
 
    public static void handleRead(SelectionKey key) throws IOException{
        SocketChannel sc = (SocketChannel)key.channel();
        ByteBuffer buf = (ByteBuffer)key.attachment();
        long bytesRead = sc.read(buf);//从Channel读取数据到Buffer
        while(bytesRead>0){//缓存中就绪的元素个数
            buf.flip();//反转buffer为读取状态
            while(buf.hasRemaining()){//告知在当前位置和限制之间是否有元素。
                System.out.print((char)buf.get()); //从Buffer中取出数据
            }
            System.out.println();
            buf.clear();//清除已读取数据，并设置状态为写入。
            bytesRead = sc.read(buf);
        }
        if(bytesRead == -1){//读到末尾
            sc.close();
        }
    }
 
    public static void handleWrite(SelectionKey key) throws IOException{
        ByteBuffer buf = (ByteBuffer)key.attachment();
        buf.flip();//反转buffer为读取状态
        SocketChannel sc = (SocketChannel) key.channel();
        while(buf.hasRemaining()){//告知在当前位置和限制之间是否有元素。
            sc.write(buf);//将Buffer内容写入到Channel
        }
        buf.compact();
    }
 
    public static void selector() {
        Selector selector = null;
        ServerSocketChannel ssc = null;
        try{
            selector = Selector.open(); //通过工厂方法创建selector
            ssc= ServerSocketChannel.open(); //通过工厂方法创建ServerSocketChannel并打开
            ssc.socket().bind(new InetSocketAddress(PORT));//注册侦听的端口
            ssc.configureBlocking(false);//非阻塞模式（与Selector一起使用时，Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式。）
            ssc.register(selector, SelectionKey.OP_ACCEPT);//注册Channel到Selector，并侦听Accept事件。
 
            while(true){
                if(selector.select(TIMEOUT) == 0){//获取就绪的channel数，有直接发回，没有则最多阻塞TIMEOUT时间。
                    System.out.println("==");
                    continue;
                }
                //获取就绪的键集的迭代器
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator(); 
                while(iter.hasNext()){
                    SelectionKey key = iter.next();
                    if(key.isAcceptable()){//测试此键的通道是否已准备好接受新的套接字连接。
                        handleAccept(key);
                    }
                    if(key.isReadable()){//测试此键的通道是否已准备好进行读取。
                        handleRead(key);
                    }
                    if(key.isWritable() && key.isValid()){//测试此键的通道是否已准备好进行写入并是否有效
                        handleWrite(key);
                    }
                    if(key.isConnectable()){//测试此键的通道是否已完成其套接字连接操作。
                        System.out.println("isConnectable = true");
                    }
                    iter.remove(); //处理结束将此键从就绪键集移除
                }
            }
 
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(selector!=null){
                    selector.close();
                }
                if(ssc!=null){
                    ssc.close(); //关闭ServerSocketChannel
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}