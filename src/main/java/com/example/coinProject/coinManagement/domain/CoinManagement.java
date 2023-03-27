package com.example.coinProject.coinManagement.domain;

import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.user.domain.User;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinManagement {

    @EmbeddedId
    private Pk pk;
    @ManyToOne
    @MapsId("userId")
    private User user;
    @ManyToOne
    @MapsId("coinId")
    private Coin coin;

    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    @Getter
    public static class Pk implements Serializable {

        @Column(name = "user_id")
        private Long userId;

        @Column(name = "coin_id")
        private Long coinId;
    }
}
