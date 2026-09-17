package com.shop.ordering.domain.exception;


public class MensagensErro {
    public static final String ERRO_VALIDACAO_EMAIL_INVALIDO = "Email is invalid";
    public static final String ERRO_VALIDACAO_DATA_NASCIMENTO_DEVE_SER_PASSADA = "BirthDate must be a past date";
    public static final String ERRO_VALIDACAO_NOME_COMPLETO_NULO = "FullName cannot be null";
    public static final String ERRO_VALIDACAO_NOME_COMPLETO_EM_BRANCO = "FullName cannot be blank";
    public static final String ERRO_CLIENTE_ARQUIVADO = "Customer is archived it cannot be changed";
    public static final String ERRO_STATUS_PEDIDO_NAO_PODE_ALTERAR = "Cannot change order %s status from %s to %s";
    public static final String ERRO_PEDIDO_DATA_ENTREGA_INVALIDA = "Order %s expected delivery date %s is invalid, cannot be a past date";
    public static final String ERRO_PEDIDO_NAO_PODE_SER_REALIZADO = "Order %s cannot be placed without items";

}
