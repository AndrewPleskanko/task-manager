package com.example.mailsender.consumer;

public interface MessageConsumer<T> {
    void consume(T message);
}
