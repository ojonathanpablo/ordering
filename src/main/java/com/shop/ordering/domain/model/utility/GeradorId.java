package com.shop.ordering.domain.model.utility;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import io.hypersistence.tsid.TSID;

import java.util.UUID;

public class GeradorId {

    private static final TimeBasedEpochRandomGenerator geradorEpocaBaseadoTempo = Generators.timeBasedEpochRandomGenerator();

    private static final TSID.Factory fabricaTsid = TSID.Factory.INSTANCE;

    private GeradorId() {
    }

    public static UUID gerarUUIDBaseadoTempo() {
        return geradorEpocaBaseadoTempo.generate();
    }


    /*
     * TSID_NODE=0
     * TSID_NODE_COUNT=3
     */
    public static TSID gerarTSID() {
        return fabricaTsid.generate();
    }
}
