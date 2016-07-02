package org.openhab.binding.tplinksmartplug;

import java.io.ByteArrayOutputStream;

public class TPLinkResponseReader {

    private static final int TPLINK_XOR_INT = -0x55;

    public String decodeUdpResponse(byte[] response) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // int myByte = byteArray[0];
        // int c1 = (myByte >> 24) & 0xFF;
        // int c2 = (myByte >> 16) & 0xFF;
        // int c3 = (myByte >> 8) & 0xFF;
        // int c4 = myByte & 0xFF;
        // outputStream.write(c1);
        // outputStream.write(c2);
        // outputStream.write(c3);
        // outputStream.write(c4);
        // We need to figure out how to read the length. It is in the first four bytes.

        int xor = TPLINK_XOR_INT;

        for (int i = 0; i < response.length; i++) {
            int myByte = xor ^ response[i];
            xor = response[i];
            if (xor == 0) {
                break;
            }
            outputStream.write(myByte);
        }

        return new String(outputStream.toByteArray());
    }

    public String decodeTCPResponse(byte[] response) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int c1 = 24 * response[0];
        int c2 = 16 * response[1];
        int c3 = 8 * response[2];
        int c4 = response[3];
        int length = c1 + c2 + c3 + c4;

        int xor = TPLINK_XOR_INT;

        for (int i = 4; i < response.length; i++) {
            int myByte = xor ^ response[i];
            xor = response[i];
            outputStream.write(myByte);
        }

        return new String(outputStream.toByteArray());
    }

    public byte[] encodeUDPRequest(String request) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] requestBytes = request.getBytes();

        int xor = TPLINK_XOR_INT;

        for (int i = 0; i < requestBytes.length; i++) {
            int myByte = requestBytes[i] ^ xor;
            xor = myByte;
            outputStream.write(myByte);
        }

        return outputStream.toByteArray();
    }

    public byte[] encodeTCPRequest(String request) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] requestBytes = request.getBytes();

        int length = requestBytes.length;
        // This might break if the length has more than one byte.
        outputStream.write(0);
        outputStream.write(0);
        outputStream.write(0);
        outputStream.write(length);

        int xor = TPLINK_XOR_INT;

        for (int i = 0; i < requestBytes.length; i++) {
            int myByte = requestBytes[i] ^ xor;
            xor = myByte;
            outputStream.write(myByte);
        }

        return outputStream.toByteArray();
    }

    public int getLength(byte[] response) {
        int c1 = 24 * response[0];
        int c2 = 16 * response[1];
        int c3 = 8 * response[2];
        int c4 = response[3];
        int length = c1 + c2 + c3 + c4;

        return length;
    }

}
