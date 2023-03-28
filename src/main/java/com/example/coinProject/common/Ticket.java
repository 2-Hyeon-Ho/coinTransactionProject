package com.example.coinProject.common;

import java.util.UUID;
import lombok.Data;

@Data(staticConstructor = "of")
public class Ticket {
    private final String ticket;
}
