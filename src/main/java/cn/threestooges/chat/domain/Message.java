package cn.threestooges.chat.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class Message {
    private String message;
    private int type;
    private Long fromUserId;
    private Long toUserId;


}
