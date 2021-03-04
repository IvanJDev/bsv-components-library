package com.nchain.jcl.net.protocol.events.data;


import com.nchain.jcl.net.network.PeerAddress;
import com.nchain.jcl.net.protocol.messages.GetAddrMsg;
import com.nchain.jcl.net.protocol.messages.common.BitcoinMsg;

/**
 * @author i.fernandez@nchain.com
 * Copyright (c) 2018-2020 nChain Ltd
 *
 * An Event triggered when a GET_ADDR Message is received from a Remote Peer.
 */
public final class GetAddrMsgReceivedEvent extends MsgReceivedEvent<GetAddrMsg> {
    public GetAddrMsgReceivedEvent(PeerAddress peerAddress, BitcoinMsg<GetAddrMsg> btcMsg) {
        super(peerAddress, btcMsg);
    }
}
