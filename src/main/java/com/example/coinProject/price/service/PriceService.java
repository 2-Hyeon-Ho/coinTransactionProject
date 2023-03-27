package com.example.coinProject.price.service;

import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.coin.repository.CoinRepository;
import com.example.coinProject.coin.service.CoinService;
import com.example.coinProject.coin.service.Feign;
import com.example.coinProject.price.dto.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PriceService {

    private final CoinRepository coinRepository;

    private double day = 14;

    private double average = (double) 1 / (1 + (day - 1));
    private static final int UNIT = 1;

    private static final int COUNT = 200;

    private final Feign feign;

    private List<Double> upList = new ArrayList<>();
    private List<Double> downList = new ArrayList<>();
    private static final double ZERO = 0;

    private Double getRsi(String market) {
        List<PriceResponse> tradePrices =
                feign.getTradePrice(UNIT, market, COUNT).stream().
                        sorted(Comparator.comparing(PriceResponse::getTimestamp))
                        .collect(Collectors.toList());
        for (int i = 0; i < tradePrices.size() - 1; i++) {
            double gapByTradePrice =
                    tradePrices.get(i + 1).getPrice().doubleValue() - tradePrices.get(i).getPrice()
                            .doubleValue();
            if (gapByTradePrice > 0) {
                upList.add(gapByTradePrice);
                downList.add(ZERO);

            } else if (gapByTradePrice < 0) {
                downList.add(gapByTradePrice * -1);
                upList.add(ZERO);
            } else {
                upList.add(ZERO);
                downList.add(ZERO);
            }
        }
        double rs = getAU() / getAD();
        return 100 - (100 / (1 + rs));
    }

    private double getAU() {
        double upEma = 0;
        if (!CollectionUtils.isEmpty(upList)) {
            upEma = upList.get(0);
            if (upList.size() > 1) {
                for (int i = 1; i < upList.size(); i++) {
                    upEma = (upList.get(i) * average) + (upEma * (1 - average));
                }
            }
        }
        return upEma;
    }

    private double getAD() {
        double downEma = 0;
        if (!CollectionUtils.isEmpty(downList)) {
            downEma = downList.get(0);
            if (downList.size() > 1) {
                for (int i = 1; i < downList.size(); i++) {
                    downEma = (downList.get(i) * average) + (downEma * (1 - average));
                }
            }

        }
        return downEma;
    }


    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void getAllMarketsRsi() {


        String market = "KRW-XRP";

        Coin coin = coinRepository.findFirstByMarket(market);
        Double rsi = getRsi(coin.getMarket());
        coinRepository.updateRsi(rsi, market);
    }


}