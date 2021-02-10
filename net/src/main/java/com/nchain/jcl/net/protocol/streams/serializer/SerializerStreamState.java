package com.nchain.jcl.net.protocol.streams.serializer;

import com.google.common.base.Objects;
import com.nchain.jcl.net.protocol.streams.MessageStreamState;
import com.nchain.jcl.tools.streams.StreamState;

import java.math.BigInteger;

/**
 * @author i.fernandez@nchain.com
 * Copyright (c) 2018-2020 nChain Ltd
 *
 * A class storing the State of the SerializerStream
 */
public final class SerializerStreamState extends StreamState {
    // Some variables to count the number of messages processed:
    private BigInteger numMsgs = BigInteger.ZERO;

    public SerializerStreamState(BigInteger numMsgs) {
        if (numMsgs != null) this.numMsgs = numMsgs;
    }

    public BigInteger getNumMsgs() {
        return this.numMsgs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof MessageStreamState)) return false;
        SerializerStreamState other = (SerializerStreamState) obj;
        return Objects.equal(this.numMsgs, other.numMsgs);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.numMsgs);
    }

    public String toString() {
        return "SerializerStreamState(numMsgs=" + this.getNumMsgs() + ")";
    }

    public SerializerStreamStateBuilder toBuilder() {
        return new SerializerStreamStateBuilder().numMsgs(this.numMsgs);
    }

    public static SerializerStreamStateBuilder builder() {
        return new SerializerStreamStateBuilder();
    }

    /**
     * Builder
     */
    public static class SerializerStreamStateBuilder {
        private BigInteger numMsgs;

        SerializerStreamStateBuilder() {}

        public SerializerStreamState.SerializerStreamStateBuilder numMsgs(BigInteger numMsgs) {
            this.numMsgs = numMsgs;
            return this;
        }

        public SerializerStreamState build() {
            return new SerializerStreamState(numMsgs);
        }
    }
}
