package com.shanz.config;

/**
 * @ClassName:AESConfig
 * @Description: 非对称加密配置项
 * @Author: shanzheng
 * @Date: 2019/6/28 10:15
 * @Version:1.0
 **/
public class AESConfig {

    private String privateKey;  //私钥


    private String publicKey; //公钥


    private int aesKeyLength; // 秘钥长度


    public AESConfig build(){
        // 自定义加载
        return new AESConfig()
                .setAesKeyLength(16)
                .setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJPuSouEQTSKRjjI3JuAcbKUW0d67gQ83CDBabzOzTuSv28qKoNKW/exu9yWst0e3eMm8NsonOuNEigkZ77dKJp00ZAYLftsJgdlM4n/44sgLy5bnU7JRv3E13Uh8Ms/oi0vR+Ih8qhSSgfVqKFmPkOD6YE2XI7l55SGUORz3yWJAgMBAAECgYBJc6+F+ORbjzDw7yNC/xL1Zd6w7mQSrJlsljzHaDPVmbb+HoS/bgOeSJ0MGbO0QahRKru7Rj4sY36ZegIdbjjDPoGMzaWmYpRivWDR/GARYYCVH1B9sHQ2hAii87gGzrLqTWHJPQ0y7S391ZZvYdhkpZZDMHw3AlBkOpp0ObKntQJBANadI3hTy7hk7Z+MT5cvf2w/yd6cOlQKrFVvw/GpwYokRQkfgNKjiSzY1W0HD+8rFalHmCHHb44Y3R2w5zskfYsCQQCwdTTknz0ZDTQYz5U/aLSUSVbA2i11r09tWb57I9g+URy0xR4IUlrssrqY/szd2+jaTYHYAOF0/68qA5YxtnO7AkEAk+esAEtPxP7ZvPFIENuNJ582kpJ8qFrKtf7WvX/OgFPKuCTb9U77RMsTG5aYZq38ubM7p1v5B6cfDE1Urq/RTQJAMvSVopNtREewzJ1dHuNwigWnnKckS4ClchFhVRPjxRMNNBJb+tE1Wna+NaIWrNZtJmUNYr/WWz/eaEaC37YtMwJADCRtstt1VfhWY+QTVdPXIqWiui0UY66zoaKMBnkprJmdipfc1zgappWEKqC9CLRkZ0mWEXWKZWavcv5Lme8LIQ==")
                .setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCT7kqLhEE0ikY4yNybgHGylFtHeu4EPNwgwWm8zs07kr9vKiqDSlv3sbvclrLdHt3jJvDbKJzrjRIoJGe+3SiadNGQGC37bCYHZTOJ/+OLIC8uW51OyUb9xNd1IfDLP6ItL0fiIfKoUkoH1aihZj5Dg+mBNlyO5eeUhlDkc98liQIDAQAB");

    }

    public String getPrivateKey() {
        return privateKey;
    }

    public AESConfig setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public AESConfig setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public int getAesKeyLength() {
        return aesKeyLength;
    }

    public AESConfig setAesKeyLength(int aesKeyLength) {
        this.aesKeyLength = aesKeyLength;
        return this;
    }
}
