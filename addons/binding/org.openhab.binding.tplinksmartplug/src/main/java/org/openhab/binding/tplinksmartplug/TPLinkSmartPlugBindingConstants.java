/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tplinksmartplug;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * The {@link TPLinkSmartPlugBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Kurt Zettel - Initial contribution
 */
public class TPLinkSmartPlugBindingConstants {

    public static final String BINDING_ID = "tplinksmartplug";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_SAMPLE = new ThingTypeUID(BINDING_ID, "sample");

    // List of all Channel ids
    public final static String CHANNEL_1 = "channel1";

    private static final String TP_LINK_SMART_PLUG_HS100 = "HS100";

    public final static ThingTypeUID HS100_UID = new ThingTypeUID(BINDING_ID, TP_LINK_SMART_PLUG_HS100);

    public final static Set<ThingTypeUID> SUPPORTED_TP_LINK_THING_TYPES_UIDS = ImmutableSet.of((HS100_UID));

}
