package com.sunjoy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 优先缓存用户请求体的参数，
 *
 * @auth Habib
 * @date 2024/10/16
 */
@Slf4j
@Component
public class GlobalRequestCacheFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getHeaders().getContentType() == null) {
            return chain.filter(exchange);
        } else {
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        // 将 DataBuffer 转换为字节数组
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);

                        String requestBody = new String(bytes, StandardCharsets.UTF_8);
                        if (log.isDebugEnabled()) {
                            log.debug("GlobalRequestCacheFilter Request Body: " + requestBody);
                        }
                        // 创建新的请求体，以便后续处理
                        ServerHttpRequestDecorator decoratedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return Flux.just(craeteNewDataBuffer(requestBody)); // 返回原始请求体, 重置读数据的指针
                            }
                        };

                        // 使用装饰后的请求继续执行链
                        return chain.filter(exchange.mutate().request(decoratedRequest).build());
                    });
        }

    }

    /**
     * 重新创建 DataBuffer
     *
     * @param requestBody
     * @return
     */
    private DataBuffer craeteNewDataBuffer(String requestBody) {
        // 创建 DataBufferFactory 实例
        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
        // 将字符串转换为 DataBuffer
        DataBuffer newDataBuffer = dataBufferFactory.allocateBuffer();
        newDataBuffer.write(requestBody.getBytes());
        return newDataBuffer;
    }

    /**
     * 设备最高优先级
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}