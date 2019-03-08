package com.wenda.async.handler;

import com.wenda.async.EventHandler;
import com.wenda.async.EventMode;
import com.wenda.async.EventType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
public class LikeHandler implements EventHandler {
    public void doHandle(EventMode eventMode){

    }

    @Override
    public List<EventType> getSupportEventTypes(){
        return Arrays.asList(EventType.LIKE);
    }
}
