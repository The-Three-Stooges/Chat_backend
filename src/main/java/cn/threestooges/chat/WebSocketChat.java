package cn.threestooges.chat;

import cn.threestooges.chat.domain.Message;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 聊天服务器
 */
@Component
@ServerEndpoint("/chat")
public class WebSocketChat {
    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketChat> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;


    /**
     * 当客户端打开连接：1.添加会话对象 2.更新在线人数
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        this.session.getAsyncRemote().sendText("连接成功，当前在线人数："+webSocketSet.size());
    }

    /**
     * 关闭时删去该用户
     */
    @OnClose
    public void onClose(Session session){
        webSocketSet.remove(this);
    }

    /**
     * 当客户端发送消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        sendMessageToAll(message);
    }

    /**
     * 公共方法：发送信息给所有人
     */
    private static void sendMessageToAll(String msg) {
        webSocketSet.forEach((webSocketChat) -> {
            try {
                webSocketChat.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
