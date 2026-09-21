package com.shop.ordering.domain.model.exception;

import com.shop.ordering.domain.model.valueobject.id.PedidoId;

import static com.shop.ordering.domain.model.exception.MensagensErro.*;

public class PedidoNaoPodeSerRealizadoException extends ExcecaoDominio {

    private PedidoNaoPodeSerRealizadoException(String mensagem) {
        super(mensagem);
    }

    public static PedidoNaoPodeSerRealizadoException semItens(PedidoId id) {
        return new PedidoNaoPodeSerRealizadoException(
                String.format(ERRO_PEDIDO_NAO_PODE_SER_REALIZADO_SEM_ITENS, id)
        );
    }

    public static PedidoNaoPodeSerRealizadoException semInfoEntrega(PedidoId id) {
        return new PedidoNaoPodeSerRealizadoException(
                String.format(ERRO_PEDIDO_NAO_PODE_SER_REALIZADO_SEM_INFO_ENTREGA, id)
        );
    }

    public static PedidoNaoPodeSerRealizadoException semInfoCobranca(PedidoId id) {
        return new PedidoNaoPodeSerRealizadoException(
                String.format(ERRO_PEDIDO_NAO_PODE_SER_REALIZADO_SEM_INFO_COBRANCA, id)
        );
    }

    public static PedidoNaoPodeSerRealizadoException semMetodoPagamento(PedidoId id) {
        return new PedidoNaoPodeSerRealizadoException(
                String.format(ERRO_PEDIDO_NAO_PODE_SER_REALIZADO_SEM_METODO_PAGAMENTO, id)
        );
    }
}
