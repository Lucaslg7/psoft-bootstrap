public final class Sistema {
    private Sistema() {
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("Ana Souza", "CPF-123", "Rua A, 10", "11999990000");
        Pedido pedido = new Pedido("DEMO-1", cliente, "Depósito central", "Rua A, 10", 2.5, 20.0);
        CentralLogistica central = CentralLogistica.getInstance();

        central.registrarPedido(pedido);
        central.selecionarModalidadePedido(pedido.getId(), new EntregaConvencional());
        System.out.println("Custo: " + central.calcularCustoPedido(pedido.getId()));
        System.out.println("Prazo (dias): " + central.calcularPrazoEstimadoPedido(pedido.getId()));

        central.despacharPedido(pedido.getId());
        central.solicitarEntrega(pedido.getId());
        System.out.println("Pedidos em rota: " + central.verPedidosEmRota().size());
        central.registrarEntrega(pedido.getId());
        System.out.println("Status final: " + pedido.getStatus());
    }
}
