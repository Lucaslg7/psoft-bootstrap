import java.util.Objects;

public final class Pedido {
    private final String id;
    private final Cliente cliente;
    private final String endOrigem;
    private final String endDestino;
    private final double peso;
    private final double distanciaEstimada;
    private ModalidadeEntrega modalidade;
    private StatusEntrega status;

    public Pedido(String id, Cliente cliente, String endOrigem, String endDestino,
                  double peso, double distanciaEstimada) {
        this.id = validarTexto(id, "Id do pedido");
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }
        this.cliente = cliente;
        this.endOrigem = validarTexto(endOrigem, "Endereço de origem");
        this.endDestino = validarTexto(endDestino, "Endereço de destino");
        if (!Double.isFinite(peso) || peso <= 0.0) {
            throw new IllegalArgumentException("Peso deve ser um número finito e positivo.");
        }
        if (!Double.isFinite(distanciaEstimada) || distanciaEstimada < 0.0) {
            throw new IllegalArgumentException("Distância deve ser finita e não negativa.");
        }
        this.peso = peso;
        this.distanciaEstimada = distanciaEstimada;
        this.status = StatusEntrega.NA_CENTRAL_LOGISTICA;
    }

    public Pedido(String id, Cliente cliente, String endOrigem, String endDestino,
                  double peso, double distanciaEstimada, ModalidadeEntrega modalidade) {
        this(id, cliente, endOrigem, endDestino, peso, distanciaEstimada);
        setModalidadeEntrega(modalidade);
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
        return valor;
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getEndOrigem() {
        return endOrigem;
    }

    public String getEndDestino() {
        return endDestino;
    }

    public double getPeso() {
        return peso;
    }

    public double getDistanciaEstimada() {
        return distanciaEstimada;
    }

    public ModalidadeEntrega getModalidadeEntrega() {
        return modalidade;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public void setModalidadeEntrega(ModalidadeEntrega modalidade) {
        if (modalidade == null) {
            throw new IllegalArgumentException("Modalidade de entrega não pode ser nula.");
        }
        if (status != StatusEntrega.NA_CENTRAL_LOGISTICA) {
            throw new IllegalStateException("A modalidade não pode ser alterada após o despacho.");
        }
        this.modalidade = modalidade;
    }

    public double calcularCustoEntrega() {
        if (modalidade == null) {
            throw new IllegalStateException("Selecione uma modalidade de entrega antes do cálculo.");
        }
        return validarResultado(modalidade.calcularCusto(this), "Custo");
    }

    public double calcularPrazoEstimado() {
        if (modalidade == null) {
            throw new IllegalStateException("Selecione uma modalidade de entrega antes do cálculo.");
        }
        return validarResultado(modalidade.calcularPrazoEstimado(this), "Prazo");
    }

    private static double validarResultado(double valor, String campo) {
        if (!Double.isFinite(valor) || valor < 0.0) {
            throw new IllegalStateException(campo + " calculado deve ser finito e não negativo.");
        }
        return valor;
    }

    void transicionarPara(StatusEntrega novoStatus) {
        Objects.requireNonNull(novoStatus, "Novo status não pode ser nulo.");
        boolean permitida = (status == StatusEntrega.NA_CENTRAL_LOGISTICA
                        && novoStatus == StatusEntrega.DESPACHADO)
                || (status == StatusEntrega.DESPACHADO
                        && (novoStatus == StatusEntrega.EM_ROTA_DE_ENTREGA
                        || novoStatus == StatusEntrega.ENTREGUE))
                || (status == StatusEntrega.EM_ROTA_DE_ENTREGA
                        && novoStatus == StatusEntrega.ENTREGUE);
        if (!permitida) {
            throw new IllegalStateException("Transição de " + status + " para " + novoStatus + " não permitida.");
        }
        status = novoStatus;
    }
}
