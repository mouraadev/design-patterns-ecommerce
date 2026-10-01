package com.ecommerce.builder;

import com.ecommerce.decorator.Item;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.PedidoInvalidoException;
import com.ecommerce.observer.ObservadorPedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Pedido {

    private final String id;
    private final String cliente;
    private final String enderecoEntrega;
    private final List<Item> itens;
    private volatile StatusPedido status;
    private final List<ObservadorPedido> observadores = new CopyOnWriteArrayList<>();

    private Pedido(Builder builder) {
        this.id = builder.id;
        this.cliente = builder.cliente;
        this.enderecoEntrega = builder.enderecoEntrega;
        this.itens = List.copyOf(builder.itens);
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
        return itens;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public double getValorTotal() {
        return itens.stream().mapToDouble(Item::getPreco).sum();
    }

    public void adicionarObservador(ObservadorPedido observador) {
        observadores.add(Objects.requireNonNull(observador, "O observador é obrigatório."));
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = Objects.requireNonNull(novoStatus, "O novo status é obrigatório.");
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

    public static class Builder {
        private final String id;
        private final String cliente;
        private String enderecoEntrega;
        private final List<Item> itens = new ArrayList<>();

        public Builder(String id, String cliente) {
            this.id = textoObrigatorio(id, "O identificador do pedido é obrigatório.");
            this.cliente = textoObrigatorio(cliente, "O cliente é obrigatório.");
        }

        public Builder comEndereco(String endereco) {
            this.enderecoEntrega = textoObrigatorio(endereco, "O endereço de entrega é obrigatório.");
            return this;
        }

        public Builder adicionarItem(Item item) {
            this.itens.add(Objects.requireNonNull(item, "O item é obrigatório."));
            return this;
        }

        public Pedido build() {
            if (itens.isEmpty()) {
                throw new PedidoInvalidoException("O pedido precisa ter ao menos um item.");
            }
            return new Pedido(this);
        }

        private static String textoObrigatorio(String valor, String mensagem) {
            if (valor == null || valor.isBlank()) {
                throw new PedidoInvalidoException(mensagem);
            }
            return valor.trim();
        }
    }
}
