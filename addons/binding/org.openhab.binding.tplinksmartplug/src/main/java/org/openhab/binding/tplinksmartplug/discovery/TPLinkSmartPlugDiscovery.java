package org.openhab.binding.tplinksmartplug.discovery;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openhab.binding.tplinksmartplug.TPLinkController;
import org.openhab.binding.tplinksmartplug.TPLinkSmartPlugBindingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TPLinkSmartPlugDiscovery extends AbstractDiscoveryService {

    private Logger log = LoggerFactory.getLogger(TPLinkSmartPlugDiscovery.class);

    public TPLinkSmartPlugDiscovery() throws IllegalArgumentException {
        super(TPLinkSmartPlugBindingConstants.SUPPORTED_TP_LINK_THING_TYPES_UIDS, 10, false);
    }

    @Override
    protected void startScan() {
        log.debug("Starting scan for TPLink SmartPlugs.");

        TPLinkController tpLinkController = new TPLinkController();
        try {
            JSONArray devices = tpLinkController.getDevices();
            for (int i = 0; i < devices.length(); i++) {
                JSONObject jsonObject = devices.getJSONObject(i);
                log.info("Scan result found: " + jsonObject.toString(2));

                JSONObject sysInfo = jsonObject.getJSONObject("system").getJSONObject("get_sysinfo");
                String deviceId = sysInfo.getString("mac");
                ThingUID uid = new ThingUID(TPLinkSmartPlugBindingConstants.HS100_UID, deviceId.replace(':', '_'));

                if (uid != null) {
                    HashMap<String, Object> properties = new HashMap<String, Object>();

                    properties.put("ip_address", jsonObject.getString("ip_address"));
                    properties.put("state", "" + sysInfo.getInt("relay_state"));
                    properties.put(TPLinkSmartPlugBindingConstants.CHANNEL_POWER, sysInfo.getInt("relay_state"));
                    properties.put("deviceId", deviceId);

                    DiscoveryResult result = DiscoveryResultBuilder.create(uid).withProperties(properties)
                            .withLabel(sysInfo.getString("alias")).build();

                    thingDiscovered(result);
                }
            }
        } catch (IOException e) {
            log.warn("Unable to scan for devices", e);
        }
    }

}
