package com.ecommerce.exception;

public final class EstoqueInsuficienteException extends PedidoInvalidoException {
    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
