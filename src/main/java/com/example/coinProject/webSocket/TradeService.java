package com.example.coinProject.webSocket;

import com.example.coinProject.common.TradeType;
import com.example.coinProject.webSocket.UpbitWebSocketListener;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeService {


    private UpbitWebSocketListener upbitWebSocketListener;

    @Transactional
    public BigDecimal getCurrentPrice() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("wss://api.upbit.com/websocket/v1")
                .build();

        UpbitWebSocketListener webSocketListener = new UpbitWebSocketListener();
        webSocketListener.setParameters(TradeType.TICKER, List.of("KRW-XRP"));


        client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService().shutdown();
        return webSocketListener.getTradeResult().getTradePrice();
    }


}
