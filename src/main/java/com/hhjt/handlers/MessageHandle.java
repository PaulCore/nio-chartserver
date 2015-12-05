package com.hhjt.handlers;

import com.hhjt.entities.User;
import com.hhjt.message.*;
import com.hhjt.queues.OnLineUsers;
import com.hhjt.queues.SessionQueue;
import com.hhjt.utils.CommandEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Created by paul on 2015/4/29.
 */
public class MessageHandle extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger(MessageHandle.class);
    private CommandEnum cmd;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        String command = message.getCmd();
        cmd = CommandEnum.valueOf(command);
        switch (cmd){
            case CORQ:
                connectRequestHandler((ConnectRequestMessage) message, ctx);
                break;
            case APRQ:
                sessionRequestHandler((SessionRequestMessage) message,ctx);
                break;
            case JOAS:
                sessionConfirmResponseHandler((SessionConfirmResponse) message,ctx);
                break;
            case SEND:
                contentSendHandler((ContentRequestMessage) message);
                break;
            case TEST:
                ctx.writeAndFlush(Unpooled.copiedBuffer("ok".getBytes()));
                logger.info("do test!");
//                ctx.close();
                break;
            default:
                    logger.error("命令出错");
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)){
                ctx.close();
//                System.out.println("READER_IDLE " + ++count);
            }else if (event.state().equals(IdleState.WRITER_IDLE)){
                ctx.writeAndFlush("hello");
                System.out.println("WRITER_IDLE");
            }else if (event.state().equals(IdleState.ALL_IDLE)){
                System.out.println("ALL_IDLE");
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     *
     * @param message
     * 首先判断用户是否已在队列中，若在返回0
     * 若不在加入队列，返回1
     */
    private void connectRequestHandler(ConnectRequestMessage message, ChannelHandlerContext ctx){
        logger.debug(message);
        ConnectResponseMessage resp = null;
        OnLineUsers onLineUsers = OnLineUsers.getOnLineUsers();
        if (onLineUsers.containUser(message.getRegisterNumber())){//存在，返回错误
           User user = onLineUsers.getUser(message.getRegisterNumber());
            user.getContext().close();
            user.setContext(ctx);
            resp = new ConnectResponseMessage(0,"1");
           logger.info(message.getRegisterNumber() + "已在线,重复登录");
        }else{//不存在，加入队列，返回正确
            User user = new User(ctx,message.getLevel());
            onLineUsers.addUser(message.getRegisterNumber(), user);
            resp = new ConnectResponseMessage(1);
            logger.info(message.getRegisterNumber() + "登陆成功");
        }
        ByteBuf respBuf = Unpooled.copiedBuffer(resp.encode());
        ctx.writeAndFlush(respBuf);
//        logger.info(message.getRegisterNumber() + "登陆成功");
//        ctx.close();
    }

    /**
     *
     * //1.查看被申请者的状态，若为不在线，掉线，离线，忙，则直接返回错误报文
     //错误代码为相应的状态码，不在线为0，2离线，3掉线，4忙，9被医师拒绝
     //2.若被申请者状态为在线，生成会话室，向被申请者发送会话要求报文，为防止其他用户再次
     //对此用户发起申请，将被申请者的状态置为忙
     * @param message
     * @param ctx
     */
    private void sessionRequestHandler(SessionRequestMessage message, ChannelHandlerContext ctx){
        logger.debug("sessionRequestHandler处理中。。。");
        SessionResponseMessage responseMessage = null;
        OnLineUsers users = OnLineUsers.getOnLineUsers();
        //请求医师在线
        if (users.containUser(message.getToNumber())){
            User to = users.getUser(message.getToNumber());
            //医师在线且空闲，向医师端发送会话要求消息
            char state = to.getState();
            if (state=='1'){
                SessionConfirmRequestMessage confirmRequestMessage = new SessionConfirmRequestMessage(message.getRegisterNumber(),message.getToNumber());
                ByteBuf respBuf = Unpooled.copiedBuffer(confirmRequestMessage.encode());
                to.getContext().writeAndFlush(respBuf);
                logger.debug("向"+message.getToNumber()+"发送会话要求成功");
                return;
            }else {
                responseMessage = new SessionResponseMessage("4",0);
                logger.debug("返回医师忙");
            }
        }else {
            responseMessage = new SessionResponseMessage("0",0);
            logger.debug("返回医师不在线");
        }
        ByteBuf respBuf = Unpooled.copiedBuffer(responseMessage.encode());
        ctx.writeAndFlush(respBuf);
        logger.debug("会话申请返回结束");
    }

    private void sessionConfirmResponseHandler(SessionConfirmResponse message, ChannelHandlerContext ctx){
        logger.debug("sessionConfirmResponseHandler处理中。。。");
        SessionResponseMessage responseMessage;
        //医师接收会话请求，向用户返回会话回复OK报文
        if (message.getResult() == 1){
            //建立session
            logger.debug("在线用户数量：" + OnLineUsers.getOnLineUsers().getUsers().size());
            logger.debug("from:" + message.getFromNumber() + " to:" + message.getToNumber());
            String sessionId = SessionQueue.getSessionQueue().createSession(message.getFromNumber(),message.getToNumber());
            responseMessage = new SessionResponseMessage(1,sessionId);
            logger.debug("医师同意会话请求");
        }else {
            responseMessage = new SessionResponseMessage("9",0);
            logger.debug("医师拒绝会话请求");
        }
        ByteBuf respBuf = Unpooled.copiedBuffer(responseMessage.encode());
        OnLineUsers.getOnLineUsers().getUser(message.getFromNumber()).getContext().writeAndFlush(respBuf);
        logger.debug("用户会话请求完毕");
    }

    private void contentSendHandler(ContentRequestMessage message){
        logger.debug("contentSendHandler");
        String sessionId = message.getSessionId();
        logger.debug("sessionId:" + sessionId);
        Set<String> users = SessionQueue.getSessionQueue().getSession(sessionId);
        logger.debug("发送者为：" + message.getFrom());
        for (String s: users){
            if (!(s.equals(message.getFrom()))){
                ByteBuf respBuf = Unpooled.copiedBuffer(message.getSource());
                OnLineUsers.getOnLineUsers().getUser(s).getContext().writeAndFlush(respBuf);
                logger.debug("消息转发成功");
            }
        }
    }

    private void sessionExitHandler(){

    }
    private void connectExitHandler(){

    }
    private void stateHandler(){

    }


}
