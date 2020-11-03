package ru.zrv.newspagespr.newspage.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class SHA256CryptoService implements CryptoService {

    MessageDigest messageDigest;

    @Override
    public String getHashString(String originalString) {
        byte[] encodedHash = messageDigest.digest(originalString.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
