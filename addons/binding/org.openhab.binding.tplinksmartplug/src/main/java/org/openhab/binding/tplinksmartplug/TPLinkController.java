package org.openhab.binding.tplinksmartplug;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TPLinkController {

    // FIXME This is mine, move it to its own jar.

    private Logger log = LoggerFactory.getLogger(TPLinkController.class);

    private TPLinkResponseReader tpLinkResponseReader = new TPLinkResponseReader();

    public JSONArray getDevices() throws IOException {

        JSONObject deviceInfoRequest = new JSONObject();
        JSONObject systemJson = new JSONObject();
        systemJson.put("get_sysinfo", JSONObject.NULL);
        deviceInfoRequest.put("system", systemJson);

        JSONObject emeterJson = new JSONObject();
        emeterJson.put("get_realtime", JSONObject.NULL);
        deviceInfoRequest.put("emeter", emeterJson);

        log.info("Preparing request:" + deviceInfoRequest.toString());

        byte[] udpRequest = tpLinkResponseReader.encodeUDPRequest(deviceInfoRequest.toString());

        JSONArray devices = new JSONArray();

        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.setSoTimeout((int) TimeUnit.SECONDS.toMillis(2));
            DatagramPacket outgoingPacket = new DatagramPacket(udpRequest, udpRequest.length,
                    InetAddress.getByName("255.255.255.255"), 9999);
            datagramSocket.send(outgoingPacket);

            byte[] buffer = new byte[1000];
            DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
            try {
                while (true) {
                    datagramSocket.receive(dataPacket);
                    String address = dataPacket.getAddress().getHostAddress().toString();
                    log.info("Response from:" + address);
                    byte[] responseBytes = dataPacket.getData();
                    String udpResponse = tpLinkResponseReader.decodeUdpResponse(responseBytes);
                    log.info("Response: " + udpResponse);
                    JSONObject deviceJson = new JSONObject(udpResponse);
                    deviceJson.put("ip_address", address);
                    devices.put(deviceJson);
                }
            } catch (SocketTimeoutException e) {
                log.info("Received Socket Timeout Exception");
            }
        }
        return devices;

    }

    public void setDeviceState(String deviceIp, int newState) throws IOException {
        log.info("Changing status of device at " + deviceIp);
        // {"system":{"set_relay_state":{"state":1}}}
        JSONObject stateRequest = new JSONObject();
        JSONObject systemJson = new JSONObject();
        stateRequest.put("system", systemJson);
        JSONObject setRelayStateJson = new JSONObject();
        systemJson.put("set_relay_state", setRelayStateJson);
        setRelayStateJson.put("state", newState);

        log.info("New request Json: " + stateRequest.toString());
        byte[] changeStateRequest = tpLinkResponseReader.encodeTCPRequest(stateRequest.toString());
        try (Socket socket = new Socket(deviceIp, 9999)) {
            IOUtils.write(changeStateRequest, socket.getOutputStream());
            byte[] lengthBuffer = new byte[4];
            socket.getInputStream().read(lengthBuffer, 0, 4);
            int length = tpLinkResponseReader.getLength(lengthBuffer);
            log.info("Response Length:" + length);
            byte[] response = new byte[length];
            socket.getInputStream().read(response, 0, response.length);

            String responseString = tpLinkResponseReader.decodeUdpResponse(response);
            log.info("Response:" + responseString);
        }
    }

}
