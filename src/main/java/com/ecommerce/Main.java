package com.ecommerce;

import com.ecommerce.builder.Pedido;
import com.ecommerce.decorator.EmbalagemPresente;
import com.ecommerce.decorator.Item;
import com.ecommerce.decorator.ProdutoItem;
import com.ecommerce.decorator.SeguroEnvio;
import com.ecommerce.facade.PedidoFacade;
import com.ecommerce.factory.Produto;
import com.ecommerce.factory.ProdutoFactory;
import com.ecommerce.strategy.EstrategiaPagamento;
import com.ecommerce.strategy.PagamentoCartaoCredito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {
        LOGGER.info("=== Sistema de Pedidos - Demonstração de Design Patterns ===");

        Produto notebook = ProdutoFactory.criar(ProdutoFactory.TipoProduto.ELETRONICO, "Notebook Gamer", 4500.00);
        Produto livro = ProdutoFactory.criar(ProdutoFactory.TipoProduto.LIVRO, "Clean Code", 89.90);
        Produto camiseta = ProdutoFactory.criar(ProdutoFactory.TipoProduto.ROUPA, "Camiseta DIO", 59.90);

        Item itemNotebook = new SeguroEnvio(new ProdutoItem(notebook));
        Item itemLivro = new EmbalagemPresente(new ProdutoItem(livro));
        Item itemCamiseta = new ProdutoItem(camiseta);

        Pedido pedido = new Pedido.Builder("PED-001", "Maria Silva")
                .comEndereco("Rua das Flores, 123 - São Paulo/SP")
                .adicionarItem(itemNotebook)
                .adicionarItem(itemLivro)
                .adicionarItem(itemCamiseta)
                .build();

        LOGGER.info("{}", pedido);

        EstrategiaPagamento pagamento = new PagamentoCartaoCredito("1234567812345678", 3);

        PedidoFacade facade = new PedidoFacade();
        facade.finalizarPedido(pedido, pagamento);

        LOGGER.info("Status final do pedido: {}", pedido.getStatus());
    }
}
