package com.hhjt;


import com.hhjt.handlers.MessageDecodeHandler;
import com.hhjt.handlers.MessageEncodeHandler;
import com.hhjt.handlers.MessageHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.ssl.SslHandler;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by paul on 2015/4/28.
 */
public class BootStart {
    private Logger logger = Logger.getLogger(BootStart.class);
    private int port;
    private int acceptThreadCount;
    private int handleThreadCount;
    public BootStart(int port, int acceptThreadCount, int handleThreadCount){
        this.port = port;
        this.acceptThreadCount = acceptThreadCount;
        this.handleThreadCount = handleThreadCount;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(acceptThreadCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(handleThreadCount);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
//                                    .addLast(new SslHandler(engine))
                                    .addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new MessageDecodeHandler())
                                    .addLast(new MessageHandle())
                            .addLast(new MessageEncodeHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            logger.info("Server start:");
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
//        System.out.println("path:" + BootStart.class.getResource("/").getPath());
//        File f = new File(BootStart.class.getResource("/").getPath());
//        System.out.println("path1:"+f);
//
//        File f2 = new File(BootStart.class.getResource("").getPath());
//        System.out.println("path2:"+f2);

//        File directory = new File("");
//        String courseFile = directory.getCanonicalPath();
//        System.out.println(courseFile);

        InputStream inputStream = BootStart.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        System.out.println("port = " + properties.getProperty("port") + " acceptThreadCount = " + properties.getProperty("acceptThreadCount")
        + " handlerThreadCount = " + properties.getProperty("handlerThreadCount"));

        int port = Integer.parseInt(properties.getProperty("port"));
        int acceptThreadCount = Integer.parseInt(properties.getProperty("acceptThreadCount"));
        int handlerThreadCount = Integer.parseInt(properties.getProperty("handlerThreadCount"));
        if (args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        new BootStart(port,acceptThreadCount,handlerThreadCount).run();
    }


}
