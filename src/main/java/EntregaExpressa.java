public final class EntregaExpressa implements ModalidadeEntrega {
    private final double tarifaFixa;
    private final double custoPorKm;
    private final double adicional;
    private final double diasBase;
    private final double kmPorDia;

    public EntregaExpressa() {
        this(10.0, 2.0, 15.0, 0.5, 200.0);
    }

    public EntregaExpressa(double tarifaFixa, double custoPorKm, double adicional,
                           double diasBase, double kmPorDia) {
        this.tarifaFixa = validarNaoNegativo(tarifaFixa, "Tarifa fixa");
        this.custoPorKm = validarNaoNegativo(custoPorKm, "Custo por quilômetro");
        this.adicional = validarNaoNegativo(adicional, "Adicional expresso");
        this.diasBase = validarNaoNegativo(diasBase, "Prazo base");
        this.kmPorDia = validarPositivo(kmPorDia, "Quilômetros por dia");
    }

    @Override
    public double calcularCusto(Pedido pedido) {
        validarPedido(pedido);
        return tarifaFixa + custoPorKm * pedido.getDistanciaEstimada() + adicional;
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
