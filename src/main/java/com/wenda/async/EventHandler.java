package com.wenda.async;

import org.springframework.ui.Model;

import java.util.List;

public interface EventHandler {
    void doHandle(EventMode eventMode);

    List<EventType> getSupportEventTypes();
}
