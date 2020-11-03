package ru.zrv.newspagespr.newspage.service;

import java.security.MessageDigest;

public interface CryptoService {

    public String getHashString(String originalString);
}
