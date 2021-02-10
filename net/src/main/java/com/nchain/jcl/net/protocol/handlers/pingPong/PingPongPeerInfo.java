package com.nchain.jcl.net.protocol.handlers.pingPong;

import com.nchain.jcl.net.network.PeerAddress;
import com.nchain.jcl.net.protocol.messages.PingMsg;

/**
 * @author i.fernandez@nchain.com
 * Copyright (c) 2018-2020 nChain Ltd
 *
 * This class stores information for each Peer that is needed in order to implement the
 * Ping-Pong P2P
 */

public class PingPongPeerInfo {
    private PeerAddress peerAddress;
    private Long timeLastActivity;
    private Long timePingSent;
    private Long noncePingSent;

    // If enabled, the Ping/Pong protocol will be disabled for this Peer: The incoming PING/PONG
    // messages from this Peer will be processed, but no verification on timeouts will be made.
    private boolean pingPongDisabled;

    /** Constructor */
    public PingPongPeerInfo(PeerAddress peerAddress) {
        this.peerAddress = peerAddress;
        this.timeLastActivity = System.currentTimeMillis();
    }

    /** It resets the info, so it can be used in a new Ping-Pong P2P */
    public synchronized void reset() {
        timePingSent = null;
        noncePingSent = null;
    }
    /** It resets the time for the last Activity to NOW */
    public synchronized void updateActivity() {
        timeLastActivity = System.currentTimeMillis();
    }

    public synchronized void updatePingStarted(PingMsg pingMsg) {
        timePingSent = System.currentTimeMillis();
        noncePingSent = pingMsg.getNonce();
    }

    /** Enables the PingPong Verification for this Peer */
    public synchronized void enablePingPong() {
        this.pingPongDisabled = false;
    }

    /** Disables the PingPong Verification for this Peer */
    public synchronized void disablePingPong() {
        this.pingPongDisabled = true;
        reset();
    }

    public PeerAddress getPeerAddress() { return this.peerAddress; }
    public Long getTimeLastActivity()   { return this.timeLastActivity; }
    public Long getTimePingSent()       { return this.timePingSent; }
    public Long getNoncePingSent()      { return this.noncePingSent; }
    public boolean isPingPongDisabled() { return this.pingPongDisabled; }
}