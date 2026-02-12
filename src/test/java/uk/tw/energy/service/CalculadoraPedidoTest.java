package uk.tw.energy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class CalculadoraPedidoTest {

    private CalculadoraPedido calculadora;
    private Date dataDiaUtil;
    private Date dataFimDeSemana;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraPedido();

        // Criando datas fixas para evitar que o teste falhe dependendo do dia que for executado
        Calendar cal = Calendar.getInstance();

        cal.set(2023, Calendar.OCTOBER, 25); // Uma Quarta-feira
        dataDiaUtil = cal.getTime();

        cal.set(2023, Calendar.OCTOBER, 28); // Um Sábado
        dataFimDeSemana = cal.getTime();
    }

    @Test
    @DisplayName("Deve aplicar 5% de desconto para cliente REGULAR com compra acima de 500")
    void clienteRegularComDesconto() {
        double resultado = calculadora.calcularValorFinal(1000.0, "REGULAR", false, "DINHEIRO", dataDiaUtil);
        // 1000 - 5% = 950
        assertThat(resultado).isEqualTo(950.0);
    }

    @Test
    @DisplayName("Deve aplicar 10% de desconto e subtrair 20 reais para cliente PREMIUM com cupom")
    void clientePremiumComCupom() {
        double resultado = calculadora.calcularValorFinal(100.0, "PREMIUM", true, "DINHEIRO", dataDiaUtil);
        // (100 - 10%) - 20 = 70
        assertThat(resultado).isEqualTo(70.0);
    }

    @Test
    @DisplayName("Deve aplicar desconto de PIX (5%) sobre o valor já com desconto de cliente VIP")
    void clienteVipComPix() {
        double resultado = calculadora.calcularValorFinal(200.0, "VIP", false, "PIX", dataDiaUtil);
        // VIP: 200 - 15% = 170
        // PIX: 170 - 5% = 161.5
        assertThat(resultado).isEqualTo(161.5);
    }

    @Test
    @DisplayName("Deve adicionar taxa de 10 reais se o pedido for no fim de semana")
    void taxaFimDeSemana() {
        double resultado = calculadora.calcularValorFinal(100.0, "REGULAR", false, "DINHEIRO", dataFimDeSemana);
        // 100 + 10 = 110
        assertThat(resultado).isEqualTo(110.0);
    }

    @Test
    @DisplayName("Não deve permitir que o valor final seja negativo")
    void valorFinalNaoNegativo() {
        // VIP com cupom em valor baixo: (10 - 15%) - 50 = -41.5 -> deve retornar 0
        double resultado = calculadora.calcularValorFinal(10.0, "VIP", true, "DINHEIRO", dataDiaUtil);
        assertThat(resultado).isEqualTo(0.0);
    }
}