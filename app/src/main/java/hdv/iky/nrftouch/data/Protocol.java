package hdv.iky.nrftouch.data;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import hdv.iky.nrftouch.util.CommonUtils;

/**
 * Created by Ann on 2/23/16.
 */
public class Protocol {
    public static byte HEADER = (byte) (byte) 0xCA;
    public static byte OPCODE_FINDBYTE = (byte) 0x7;
    public static byte OPCODE_SETLOCK = (byte) 0x2;
    public static byte OPCODE_SETVIBRATE = (byte) 0x3;
    public static byte OPCODE_READSTATUS = (byte) 0x4;
    public static byte OPCODE_LOGON = (byte) 0x1;
    public static byte OPCODE_RENAME = (byte) 0x6;
    public static byte OPCODE_CHANGEPIN = (byte) 0x5;
    public static byte OPCODE_OPENTRUNK = (byte) 0x9;
    public static byte OPCODE_CONFIGURE_RSSI = (byte) 0x0B;
    public static byte OPCODE_VERSION = (byte) 0x0F;
    public static byte OPCODE_START_EMERGENCY = (byte) 0x1C;
    public static byte OPCODE_CHANGE_PIN_SMARTKEY = (byte) 0x1D;
    public static byte OPCODE_GET_PIN_SMARTKEY = (byte) 0x1E;

    public static byte APP_OPCODE_FINDBYTE = (byte) 0x87;
    public static byte APP_OPCODE_SETLOCK = (byte) 0x82;
    public static byte APP_OPCODE_SETVIBRATE = (byte) 0x83;
    public static byte APP_OPCODE_READSTATUS = (byte) 0x84;
    public static byte APP_OPCODE_LOGON = (byte) 0x81;
    public static byte APP_OPCODE_ALARM = (byte) 0x88;
    public static byte APP_OPCODE_RENAME = (byte) 0x86;
    public static byte APP_OPCODE_CHANGEPIN = (byte) 0x85;
    public static byte APP_OPCODE_START_EMERGENCY = (byte) 0x9C;
    public static byte APP_OPCODE_CHANGE_PIN_SMARTKEY = (byte) 0x9D;
    public static byte APP_OPCODE_GET_PIN_SMARTKEY = (byte) 0x9E;
    public static byte APP_OPCODE_RSSI = (byte) 0x8D;
    public static byte APP_OPCODE_VERSION = (byte) 0x8F;


    public static byte STATUS_CODE_SUCCESS = (byte) 0x0;
    public static byte STATUS_CODE_BUSY = (byte) 0x44;
    public static byte STATUS_CODE_ERRORPIN = (byte) 0x55;
    public static byte STATUS_CODE_MALFUNCTION = (byte) 0x96;

    public static byte PIN_ENCRYPT_LENGTH = (byte) 0x04;
    public static byte PIN_SMARTKEY_LENGTH = (byte) 0x09;

    public static byte[] generateCmd(byte opcode, String pin, byte[] data){
        try {
        /*
        Encrypted
         */
            byte[] bytesPinData = new byte[4 + data.length];
            for (int i = 0; i < pin.getBytes().length; i++) {
                bytesPinData[i] = pin.getBytes()[i];
            }
            for (int i = 0; i < data.length; i++) {
                bytesPinData[4 + i] = data[i];
            }
            byte[] bytesEncrypted = CommonUtils.encryptePin(bytesPinData);

        /*
        Calculate CRC
         */
            byte crc = CommonUtils.calculateCRC(bytesEncrypted, data);

        /*
        Add to result
         */
            byte lengthResult = (byte) (8 + data.length);
            byte[] result = new byte[lengthResult];

            result[0] = HEADER;
            result[1] = opcode;
            result[2] = (byte) (bytesEncrypted.length + data.length);
            result[3] = bytesEncrypted[3];
            result[4] = bytesEncrypted[2];
            result[5] = bytesEncrypted[1];
            result[6] = bytesEncrypted[0];
            for (int i = 0; i < data.length; i++) {
                result[7 + i] = data[i];
            }
            result[7 + data.length] = crc;
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public static byte[] startEmergency(String pin, byte value){
        return generateCmd(OPCODE_START_EMERGENCY,pin, new byte[]{value});
    }
    public static byte[] configureRSSI(String pin, byte valueTurnOn, byte valueTurnOff){
        return generateCmd(OPCODE_CONFIGURE_RSSI,pin, new byte[]{valueTurnOn, valueTurnOff});
    }

    public static byte[] version(String pin){
        return generateCmd(OPCODE_VERSION,pin, new byte[]{1});
    }
    public static byte[] findbyke(String pin){
        return generateCmd(OPCODE_FINDBYTE,pin, new byte[]{1});
    }

    public static byte[] openTrunk(String pin){
        return generateCmd(OPCODE_OPENTRUNK,pin, new byte[]{1});
    }

    public static byte[] setLockOn(String pin){
        return generateCmd(OPCODE_SETLOCK,pin, new byte[]{1});

    }

    public static byte[] setLockOff(String pin){
        return generateCmd(OPCODE_SETLOCK,pin, new byte[]{0});
    }

    public static byte[] setVibrateOn(String pin){
        return generateCmd(OPCODE_SETVIBRATE,pin, new byte[]{1});
    }

    public static byte[] setVibrateOff(String pin){
        return generateCmd(OPCODE_SETVIBRATE,pin, new byte[]{0});
    }

    public static byte[] readStatus(String pin){
        return generateCmd(OPCODE_READSTATUS,pin, new byte[]{1});
    }

    public static byte[] logOn(String pin){
        return generateCmd(OPCODE_LOGON,pin, new byte[]{1});
    }

    public static byte[] rename(String pin, String newName){
        return generateCmd(OPCODE_RENAME,pin,newName.getBytes());
    }

    public static byte[] changePin(String pin, String newPin){
        //tripledes pin and newpin
        /*
        Use the encrypt TripleDES
        Key: oldpin*6
        Message: 8 byte newpin
        Mode: ECB
         */
        try {
            byte[] bytesTripleDes = encrypt(pin, newPin);
            return generateCmd(OPCODE_CHANGEPIN, pin, bytesTripleDes);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] changePinSmartkey(String pin, String newPINSmartkey){
        return generateCmd(OPCODE_CHANGE_PIN_SMARTKEY,pin,newPINSmartkey.getBytes());
    }

    public static byte[] getPinSmartkey(String pin){
        return generateCmd(OPCODE_GET_PIN_SMARTKEY,pin,new byte[]{0});
    }

    public static byte[] encrypt(String oldPin, String newPin) throws Exception {
        // Create an array to hold the key

        byte[] bytesNewPin = new byte[8];
        for (int i = 0; i < 4; i++) {
            bytesNewPin[i] = newPin.getBytes()[i];
            
        }


        byte[] bytesOldPin = new byte[24];
        for (int i = 0; i < 6; i++) {

            for (int i1 = 0; i1 < oldPin.getBytes().length; i1++) {
                bytesOldPin[i*4 + i1] = oldPin.getBytes()[i1];
            }

        }
        final SecretKey key = new SecretKeySpec(bytesOldPin, "DESede");
        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        final byte[] cipherText = cipher.doFinal(bytesNewPin);

        return Arrays.copyOfRange(cipherText,0,8);
    }


}
