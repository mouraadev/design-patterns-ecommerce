package com.ecommerce.builder;

import com.ecommerce.decorator.Item;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.observer.ObservadorPedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Padrão BUILDER (construção passo a passo de um objeto complexo) combinado
 * com o padrão OBSERVER (o próprio Pedido funciona como "sujeito" que
 * notifica interessados sempre que seu status muda).
 */
public class Pedido {

    private final String id;
    private final String cliente;
    private final String enderecoEntrega;
    private final List<Item> itens;
    private StatusPedido status;
    private final List<ObservadorPedido> observadores = new ArrayList<>();

    private Pedido(Builder builder) {
        this.id = builder.id;
        this.cliente = builder.cliente;
        this.enderecoEntrega = builder.enderecoEntrega;
        this.itens = builder.itens;
        this.status = StatusPedido.CRIADO;
    }

    public String getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public List<Item> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public StatusPedido getStatus() {
        return status;
    }

    public double getValorTotal() {
        return itens.stream().mapToDouble(Item::getPreco).sum();
    }

    public void adicionarObservador(ObservadorPedido observador) {
        observadores.add(observador);
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
        notificarObservadores();
    }

    private void notificarObservadores() {
        for (ObservadorPedido observador : observadores) {
            observador.atualizar(this, status);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido ").append(id).append(" - Cliente: ").append(cliente).append("\n");
        for (Item item : itens) {
            sb.append("  - ").append(item.getDescricao())
              .append(" (R$ ").append(String.format("%.2f", item.getPreco())).append(")\n");
        }
        sb.append("Total: R$ ").append(String.format("%.2f", getValorTotal())).append("\n");
        sb.append("Status: ").append(status);
        return sb.toString();
    }

    /**
     * BUILDER: constrói um {@link Pedido} passo a passo, com métodos
     * encadeáveis (fluent interface).
     */
    public static class Builder {
        private final String id;
        private final String cliente;
        private String enderecoEntrega = "Não informado";
        private final List<Item> itens = new ArrayList<>();

        public Builder(String id, String cliente) {
            this.id = id;
            this.cliente = cliente;
        }

        public Builder comEndereco(String endereco) {
            this.enderecoEntrega = endereco;
            return this;
        }

        public Builder adicionarItem(Item item) {
            this.itens.add(item);
            return this;
        }

        public Pedido build() {
            if (itens.isEmpty()) {
                throw new IllegalStateException("O pedido precisa ter ao menos um item.");
            }
            return new Pedido(this);
        }
    }
}
