public interface ModalidadeEntrega {
    double calcularCusto(Pedido pedido);

    double calcularPrazoEstimado(Pedido pedido);
}
