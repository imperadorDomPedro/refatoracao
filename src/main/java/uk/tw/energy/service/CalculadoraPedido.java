package uk.tw.energy.service;

import java.util.Date;

public class CalculadoraPedido {

    public double calcularValorFinal(
            double total,
            String tipoCliente,
            boolean possuiCupom,
            String formaPagamento,
            Date dataPedido
    ) {

        double valorFinal = total;

        // Desconto por tipo de cliente
        if (tipoCliente.equals("REGULAR")) {
            if (total > 500) {
                valorFinal = total * 0.95;
            }
        } else if (tipoCliente.equals("PREMIUM")) {
            valorFinal = total * 0.90;
            if (possuiCupom) {
                valorFinal = valorFinal - 20;
            }
        } else if (tipoCliente.equals("VIP")) {
            valorFinal = total * 0.85;
            if (possuiCupom) {
                valorFinal = valorFinal - 50;
            }
        }

        // Ajuste por forma de pagamento
        if (formaPagamento.equals("CARTAO_CREDITO")) {
            valorFinal = valorFinal + (valorFinal * 0.02);
        } else if (formaPagamento.equals("PIX")) {
            valorFinal = valorFinal - (valorFinal * 0.05);
        } else if (formaPagamento.equals("BOLETO")) {
            valorFinal = valorFinal - (valorFinal * 0.03);
        }

        // Regra especial para fim de semana
        if (dataPedido.getDay() == 0 || dataPedido.getDay() == 6) {
            valorFinal = valorFinal + 10;
        }

        if (valorFinal < 0) {
            valorFinal = 0;
        }

        return valorFinal;
    }
}
