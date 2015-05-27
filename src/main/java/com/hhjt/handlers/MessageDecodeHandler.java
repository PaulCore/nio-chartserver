package com.hhjt.handlers;

import com.hhjt.entities.User;
import com.hhjt.message.Message;
import com.hhjt.message.MessageFactory;
import com.hhjt.queues.OnLineUsers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by paul on 2015/4/28.
 */
public class MessageDecodeHandler extends ByteToMessageDecoder {
    private Logger logger = Logger.getLogger(MessageDecodeHandler.class);
    private int counter;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        if (bytes.length > 10){
            byteBuf.readBytes(bytes);
            //判断包头
            if (bytes[0]==35&&bytes[1]==35){
                //计算报文长度
                int dataLength = (bytes[2]-48)*1000+(bytes[3]-48)*100+(bytes[4]-48)*10+bytes[5]-48;
                logger.debug("dataLength = " + dataLength + " bytesLength = " + bytes.length);
                if (dataLength == bytes.length - 6){//长度正确
                    String cmd = new String(bytes,6,4);
                    Message message = MessageFactory.getMessage(cmd,bytes);
                    list.add(message);
                }else {
                    logger.info("报文长度出错");
                }
            }
        }else {
            logger.info("报文出现错误，扔掉！");
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("一个新连接接入" + ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel关闭" + ctx.channel());
//        ctx.close();
    }
}
