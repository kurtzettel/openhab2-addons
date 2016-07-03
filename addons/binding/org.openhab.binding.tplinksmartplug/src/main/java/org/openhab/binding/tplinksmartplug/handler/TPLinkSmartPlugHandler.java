/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tplinksmartplug.handler;

import static org.openhab.binding.tplinksmartplug.TPLinkSmartPlugBindingConstants.CHANNEL_POWER;

import java.io.IOException;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.tplinksmartplug.TPLinkController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TPLinkSmartPlugHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Kurt Zettel - Initial contribution
 */
public class TPLinkSmartPlugHandler extends BaseThingHandler {

    private Logger log = LoggerFactory.getLogger(TPLinkSmartPlugHandler.class);

    public TPLinkSmartPlugHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        log.info("handleCommand:" + channelUID.getId() + ", command:" + command.toString() + ", thing:"
                + getThing().getLabel());
        if (channelUID.getId().equals(CHANNEL_POWER)) {
            String ipAddress = getThing().getProperties().get("ip_address");
            TPLinkController tpLinkController = new TPLinkController();
            int state = 0;
            OnOffType onOffType = (OnOffType) command;
            if (OnOffType.ON.equals(onOffType)) {
                state = 1;
            } else {
                state = 0;
            }
            try {
                tpLinkController.setDeviceState(ipAddress, state);
            } catch (IOException e) {
                log.warn("Unable to change device state", e);
            }

            // Note: if communication with thing fails for some reason,
            // indicate that by setting the status with detail information
            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
            // "Could not control device at IP address x.x.x.x");
        }
    }

    @Override
    public void initialize() {
        // TODO: Initialize the thing. If done set status to ONLINE to indicate proper working.
        // Long running initialization should be done asynchronously in background.
        updateStatus(ThingStatus.ONLINE);

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work
        // as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }
}
