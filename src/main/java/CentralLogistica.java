import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CentralLogistica {
    private static final CentralLogistica INSTANCIA = new CentralLogistica();

    private final List<Pedido> pedidos = new ArrayList<>();

    private CentralLogistica() {
    }

    public static CentralLogistica getInstance() {
        return INSTANCIA;
    }

    public void registrarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo.");
        }
        if (buscarPedidoOuNulo(pedido.getId()) != null) {
            throw new IllegalArgumentException("Já existe um pedido com o id " + pedido.getId() + ".");
        }
        pedidos.add(pedido);
    }

    public void selecionarModalidadePedido(String id, ModalidadeEntrega modalidade) {
        buscarPedido(id).setModalidadeEntrega(modalidade);
    }

    public List<Pedido> acompanharOperacoes() {
        return Collections.unmodifiableList(new ArrayList<>(pedidos));
    }

    public Pedido solicitarEntrega(String id) {
        Pedido pedido = buscarPedido(id);
        if (pedido.getStatus() != StatusEntrega.DESPACHADO) {
            throw new IllegalStateException("O pedido deve ser despachado antes de solicitar a entrega.");
        }
        pedido.calcularCustoEntrega();
        pedido.calcularPrazoEstimado();
        pedido.transicionarPara(StatusEntrega.EM_ROTA_DE_ENTREGA);
        return pedido;
    }

    public double calcularCustoPedido(String id) {
        return buscarPedido(id).calcularCustoEntrega();
    }

    public double calcularPrazoEstimadoPedido(String id) {
        return buscarPedido(id).calcularPrazoEstimado();
    }

    public void despacharPedido(String id) {
        Pedido pedido = buscarPedido(id);
        pedido.calcularCustoEntrega();
        pedido.calcularPrazoEstimado();
        pedido.transicionarPara(StatusEntrega.DESPACHADO);
    }

    public void registrarEntrega(String id) {
        Pedido pedido = buscarPedido(id);
        StatusEntrega status = pedido.getStatus();
        if (status != StatusEntrega.DESPACHADO && status != StatusEntrega.EM_ROTA_DE_ENTREGA) {
            throw new IllegalStateException("Somente pedidos despachados podem ser registrados como entregues.");
        }
        pedido.transicionarPara(StatusEntrega.ENTREGUE);
    }

    public List<Pedido> verPedidosEntregues() {
        return filtrarPorStatus(StatusEntrega.ENTREGUE);
    }

    public List<Pedido> verPedidosEmRota() {
        return filtrarPorStatus(StatusEntrega.EM_ROTA_DE_ENTREGA);
    }

    public List<Pedido> verPedidosNaCentralLogistica() {
        return filtrarPorStatus(StatusEntrega.NA_CENTRAL_LOGISTICA);
    }

    private List<Pedido> filtrarPorStatus(StatusEntrega status) {
        List<Pedido> correspondentes = pedidos.stream()
                .filter(pedido -> pedido.getStatus() == status)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(correspondentes);
    }

    private Pedido buscarPedido(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id do pedido não pode ser vazio.");
        }
        Pedido pedido = buscarPedidoOuNulo(id);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + id);
        }
        return pedido;
    }

    private Pedido buscarPedidoOuNulo(String id) {
        return pedidos.stream()
                .filter(pedido -> pedido.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
