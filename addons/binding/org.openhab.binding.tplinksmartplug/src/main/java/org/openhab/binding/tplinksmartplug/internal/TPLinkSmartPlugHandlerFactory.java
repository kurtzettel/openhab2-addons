/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tplinksmartplug.internal;

import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.tplinksmartplug.TPLinkSmartPlugBindingConstants;
import org.openhab.binding.tplinksmartplug.handler.TPLinkSmartPlugHandler;

/**
 * The {@link TPLinkSmartPlugHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Kurt Zettel - Initial contribution
 */
public class TPLinkSmartPlugHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = TPLinkSmartPlugBindingConstants.SUPPORTED_TP_LINK_THING_TYPES_UIDS;

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        boolean supported = SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
        return supported;
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(TPLinkSmartPlugBindingConstants.HS100_UID)) {
            return new TPLinkSmartPlugHandler(thing);
        }

        return null;
    }
}
