package com.example.coinProject.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderRequest {

  private String market;

  private String side;

  private String volume;

  private String price;

  @JsonProperty("ord_type")
  private String ordType;




}
