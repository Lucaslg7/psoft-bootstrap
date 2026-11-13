public final class Cliente {
    private final String nome;
    private final String cpf;
    private final String endereco;
    private final String telefone;

    public Cliente(String nome, String cpf, String endereco, String telefone) {
        this.nome = validarTexto(nome, "Nome");
        this.cpf = validarTexto(cpf, "CPF");
        this.endereco = validarTexto(endereco, "Endereço");
        this.telefone = validarTexto(telefone, "Telefone");
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
        return valor;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }
}
