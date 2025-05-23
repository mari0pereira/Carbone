package com.example.bike.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {

    private int id;
    private BigDecimal valorPago;
    private String metodoPagamento;
    private String statusPagamento;
    private LocalDateTime dataPagamento;
}
