package com.shop.ordering.domain.exception;


public class MensagensErro {
    public static final String ERRO_VALIDACAO_DATA_NASCIMENTO_DEVE_SER_PASSADA = "BirthDate must be a past date";
    public static final String ERRO_VALIDACAO_NOME_COMPLETO_NULO = "FullName cannot be null";
    public static final String ERRO_CLIENTE_ARQUIVADO = "Customer is archived it cannot be changed";
    public static final String ERRO_STATUS_PEDIDO_NAO_PODE_ALTERAR = "Cannot change order %s status from %s to %s";
    public static final String ERRO_PEDIDO_DATA_ENTREGA_INVALIDA = "Order %s expected delivery date %s is invalid, cannot be a past date";
    public static final String ERRO_PEDIDO_NAO_PODE_SER_REALIZADO_SEM_ITENS = "Order %s cannot be placed without items";
    public static final String ERRO_PEDIDO_NAO_PODE_SER_REALIZADO_SEM_INFO_ENTREGA = "Order %s cannot be placed without shipping info";
    public static final String ERRO_PEDIDO_NAO_PODE_SER_REALIZADO_SEM_INFO_COBRANCA = "Order %s cannot be placed without billing info";
    public static final String ERRO_PEDIDO_NAO_PODE_SER_REALIZADO_SEM_METODO_PAGAMENTO = "Order %s cannot be placed without payment method";
    public static final String ERRO_PEDIDO_NAO_CONTEM_ITEM = "Order %s does not contain item %s";
    public static final String ERRO_PRODUTO_FORA_DE_ESTOQUE = "Product %s is out of stock";
    public static final String ERRO_PEDIDO_NAO_PODE_SER_ALTERADO = "Order %s with status %s cannot be edited";
    public static final String ERRO_CARRINHO_COMPRAS_NAO_CONTEM_ITEM = "Shopping Cart %s does not contain item %s";
    public static final String ERRO_CARRINHO_COMPRAS_NAO_CONTEM_PRODUTO = "Shopping Cart %s does not contain product %s";
    public static final String ERRO_CARRINHO_COMPRAS_ITEM_PRODUTO_INCOMPATIVEL = "Shopping Cart %s cannot be updated, incompatible product %s";
}
