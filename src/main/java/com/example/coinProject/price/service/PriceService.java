package com.example.coinProject.price.service;

import com.example.coinProject.coin.repository.CoinRepository;
import com.example.coinProject.coin.service.Feign;
import com.example.coinProject.common.LimitedQueue;
import com.example.coinProject.price.dto.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PriceService {

//    private static final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
//        "yyyy-MM-dd HH:mm:ss"));
//    private static final String yesterday = LocalDateTime.now().minusDays(1)
//        .format(DateTimeFormatter.ofPattern(
//            "yyyy-MM-dd HH:mm:ss"));
    private static final int UNIT = 5;
    private static final double EMA = (double) 1 / (1 + (14 - 1));
    private static final int COUNT = 200;

    private final Feign feign;
    private final CoinRepository coinRepository;
    LimitedQueue<Double> upQueue = new LimitedQueue<>(200);
    LimitedQueue<Double> downQueue = new LimitedQueue<>(200);


    @Transactional
    public Double getRsi() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss"));
        String yesterday = LocalDateTime.now().minusDays(1)
            .format(DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss"));
        List<PriceResponse> todayPrices = feign.getTradePrice(UNIT, "KRW-BTC",
            now, COUNT);
        List<PriceResponse> yesterdayPrices = feign.getTradePrice(UNIT, "KRW-BTC",
            yesterday, COUNT);



        for (int i = 0; i < 200; i++) {
            Double todayPrice = todayPrices.get(i).getPrice().doubleValue();
            Double yesterdayPrice = yesterdayPrices.get(i).getPrice().doubleValue();

            double difference = todayPrice - yesterdayPrice;
            if (difference > 0) {
                upQueue.add(difference);
                downQueue.add(0d);
            } else if (difference < 0) {
                upQueue.add(0d);
                downQueue.add(Math.abs(difference));
            } else {
                upQueue.add(difference);
                downQueue.add(difference);
            }
        }

//        double v = getAU().doubleValue();
//        double v1 = getAD().doubleValue();
//        System.out.println("v = " + v);
//        System.out.println("v1 = " + v1);
        double rs = getAU() / getAD();
        return 100 - (100 / (1 + rs));
    }

    private String minusMinute(LocalDateTime date, int unit) {
        return date.minusMinutes(unit).format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
    }

    private double getAU() {
        double upEma = 0d;
        if (!CollectionUtils.isEmpty(upQueue)) {
            upEma = upQueue.remove();
            if (upQueue.size() > 1) {
//                for (BigDecimal upElement : upQueue) {
//                    upEma = (upElement.multiply(BigDecimal.valueOf(EMA))).add(upEma.multiply(
//                        BigDecimal.valueOf(1 - EMA)));
//                }

//                for (Double upElement : upQueue) {
//                    upEma = (upElement * EMA) + (upEma * (1-EMA));
//                }
                while (!upQueue.isEmpty()) {
                    Double upElement = upQueue.poll();
                    upEma = (upElement * EMA) + (upEma * (1-EMA));
                }
            }
        }
        return upEma;
    }

    private double getAD() {
        double downEma = 0d;
        if (!CollectionUtils.isEmpty(downQueue)) {
            downEma = downQueue.remove();
            if (downQueue.size() > 1) {
//                for (BigDecimal downElement : downQueue) {
//                    downEma = (downElement.multiply(BigDecimal.valueOf(EMA))).add(downEma.multiply(
//                        BigDecimal.valueOf(1 - EMA)));
//                }

//                for (Double downElement : downQueue) {
//                    downEma = (downElement * EMA) + (downEma * (1-EMA));
//                }
                while (!downQueue.isEmpty()) {
                    Double downElement = downQueue.poll();
                    downEma = (downElement * EMA) + (downEma * (1-EMA));
                }
            }
        }
        return downEma;
    }
}