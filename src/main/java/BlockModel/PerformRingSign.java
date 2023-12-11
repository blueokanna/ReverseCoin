package BlockModel;

import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class PerformRingSign implements Serializable {

    private static final long serialVersionUID = 1145141919810L;

    private KeyPair[] generateKeyPairs(int numKeyPairs) throws NoSuchAlgorithmException {
        KeyPair[] keyPairs = new KeyPair[numKeyPairs];
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = new SecureRandom();
        keyGen.initialize(256, random);

        for (int i = 0; i < numKeyPairs; i++) {
            keyPairs[i] = keyGen.generateKeyPair();
        }
        return keyPairs;
    }

    public byte[] performRingSign(String data, int numKeyPairs, int signerIndex) {
        try {
            int numMembers = generateKeyPairs(numKeyPairs).length;
            KeyPair[] keyPairs = generateKeyPairs(numKeyPairs);
            List<PublicKey> publicKeys = new ArrayList<>();

            for (int i = 0; i < numMembers; i++) {
                if (i != signerIndex) {
                    publicKeys.add(keyPairs[i].getPublic());
                }
            }

            Signature signature = Signature.getInstance("SHA3-256withECDSA");
            signature.initSign(keyPairs[signerIndex].getPrivate());
            signature.update(data.getBytes());

            for (byte[] sig : publicKeysToBytes(publicKeys)) {
                signature.update(sig);
            }

            return signature.sign();
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean verifyRingSign(byte[] data, byte[] signature, KeyPair[] publicKeys) {
        try {
            int numMembers = publicKeys.length;
            Signature sig = Signature.getInstance("SHA3-256withECDSA");

            for (int i = 0; i < numMembers; i++) {
                sig.initVerify(publicKeys[i].getPublic());
                sig.update(data);

                if (i < numMembers - 1) {
                    sig.update(signature);
                }

                if (sig.verify(signature)) {
                    return true;
                }
            }
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private byte[][] publicKeysToBytes(List<PublicKey> publicKeys) {
        byte[][] bytes = new byte[publicKeys.size()][];
        for (int i = 0; i < publicKeys.size(); i++) {
            bytes[i] = publicKeys.get(i).getEncoded();
        }
        return bytes;
    }
}
