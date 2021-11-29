package com.nchain.jcl.net.protocol.handlers.blacklist;


import com.nchain.jcl.tools.handlers.Handler;

/**
 * @author i.fernandez@nchain.com
 * Copyright (c) 2018-2020 nChain Ltd
 *
 * Blacklist Handler.
 * This handler takes care of blacklisting those Hosts tat missbehave based on some criteria.
 */
public interface BlacklistHandler extends Handler {
    String HANDLER_ID = "Blacklist";

    @Override
    default String getId() { return HANDLER_ID; }

}
