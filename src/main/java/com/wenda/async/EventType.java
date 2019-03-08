package com.wenda.async;
/**异步事件模型*/
public enum  EventType {
    LIKE(0),
    COMMIT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5),
    ADD_QUESTION(6);

    private int value;
    EventType(int vakue){
        this.value=vakue;
    }
    public int getValue(){
        return this.value;
    };
}
