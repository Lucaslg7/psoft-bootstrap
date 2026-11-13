public final class EntregaEconomica implements ModalidadeEntrega {
    private final double tarifaFixa;
    private final double custoPorKm;
    private final double desconto;
    private final double diasBase;
    private final double kmPorDia;

    public EntregaEconomica() {
        this(10.0, 2.0, 0.25, 2.0, 50.0);
    }

    public EntregaEconomica(double tarifaFixa, double custoPorKm, double desconto,
                            double diasBase, double kmPorDia) {
        this.tarifaFixa = validarNaoNegativo(tarifaFixa, "Tarifa fixa");
        this.custoPorKm = validarNaoNegativo(custoPorKm, "Custo por quilômetro");
        if (!Double.isFinite(desconto) || desconto < 0.0 || desconto > 1.0) {
            throw new IllegalArgumentException("Desconto deve ser finito e estar entre 0 e 1.");
        }
        this.desconto = desconto;
        this.diasBase = validarNaoNegativo(diasBase, "Prazo base");
        this.kmPorDia = validarPositivo(kmPorDia, "Quilômetros por dia");
    }

    @Override
    public double calcularCusto(Pedido pedido) {
        validarPedido(pedido);
        return (tarifaFixa + custoPorKm * pedido.getDistanciaEstimada()) * (1.0 - desconto);
    }

    @Override
    public double calcularPrazoEstimado(Pedido pedido) {
        validarPedido(pedido);
        return diasBase + pedido.getDistanciaEstimada() / kmPorDia;
    }

    private static void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo.");
        }
    }

    private static double validarNaoNegativo(double valor, String campo) {
        if (!Double.isFinite(valor) || valor < 0.0) {
            throw new IllegalArgumentException(campo + " deve ser finito e não negativo.");
        }
        return valor;
    }

    private static double validarPositivo(double valor, String campo) {
        if (!Double.isFinite(valor) || valor <= 0.0) {
            throw new IllegalArgumentException(campo + " deve ser finito e positivo.");
        }
        return valor;
    }
}
