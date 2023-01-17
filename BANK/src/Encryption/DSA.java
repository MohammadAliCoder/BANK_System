package Encryption;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DSA {
    
   // static String alg = "DSA";
 //   KeyPairGenerator kg;

//    public DSA() throws NoSuchAlgorithmException {
//        this.kg = KeyPairGenerator.getInstance(alg);
//        kg.initialize(1024);
//        //KeyPair keyPair = kg.genKeyPair();
//    }
    
//  public static void main(String[] args) throws Exception {
//    //String alg = "DSA";
//    //KeyPairGenerator kg = KeyPairGenerator.getInstance(alg);
//   // kg.initialize(1024);
//   DSA dsa = new DSA();
//   // KeyPair keyPair = dsa.kg.genKeyPair();
//    KeyPair keyPair = dsa.generateKeyPair();
//    Key privateKey = keyPair.getPrivate();
//    
//     byte[] privateKeyEnc = privateKey.getEncoded();
//    byte[] privateKeyPem = Base64.getEncoder().encode(privateKeyEnc);
//    String privateKeyPemStr = new String(privateKeyPem);
//    
//  
//    
//    System.out.println("hi    "+privateKeyPemStr);
//    byte[] signature = performSigning("test", privateKeyPemStr); //
//    System.out.println("Digital signature for given text: "+new String(signature, "UTF8"));
//   
//    Key publicKey = keyPair.getPublic();
//   
//    
//    byte[] publicKeyEnc = publicKey.getEncoded();
//    byte[] publicKeyPem = Base64.getEncoder().encode(publicKeyEnc);
//    String publicKeyPemStr = new String(publicKeyPem);
//    
//    
//    System.out.println("hi    "+publicKeyPemStr);
//      System.out.println(performVerification("test", signature, publicKeyPemStr));
//    
//  }

 public static byte[] performSigning(String s, String privateKey) throws Exception {
     System.out.println("Start 1");
     String alg = "DSA";
    Signature sign = Signature.getInstance(alg);
    //PrivateKey privateKey = keyPair.getPrivate();
    //PublicKey publicKey = keyPair.getPublic();
    PrivateKey privkey = KeyFactory.getInstance(alg)
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    
    //PublicKey key = KeyFactory.getInstance(ALGORITHM)
       //         .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
    
    //PrivateKey key = KeyFactory.getInstance(ALGORITHM)
    //            .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    
    sign.initSign(privkey);
    sign.update(s.getBytes());
    
    System.out.println("end 1");
    return sign.sign();
  }

 public static String performVerification(String s, byte[] signature, String publicKey)
      throws Exception {
     System.out.println("sing:  "+signature.toString());
     System.out.println("pubkey:  "+publicKey);
     System.out.println("Start 2");
     String alg = "DSA";
    Signature sign = Signature.getInstance(alg);
   PublicKey publkey = KeyFactory.getInstance(alg)
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
    sign.initVerify(publkey);
    sign.update(s.getBytes());
    //System.out.println(sign.verify(signature));
    System.out.println("end 2");
    if( sign.verify(signature)){
         System.out.println("ttttt");
        return "true";}
        else{
                System.out.println("fffff");
        return "false";
                }
  }
 
  public static KeyPair generateKeyPair()
            throws NoSuchAlgorithmException, NoSuchProviderException {
        String alg = "DSA";
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(alg);

       // SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

       KeyPairGenerator kg = KeyPairGenerator.getInstance(alg);
//        kg.initialize(1024);
//        //KeyPair keyPair = kg.genKeyPair();
       
       
        // 512 is keysize
        //keyGen.initialize(512, random);
        keyGen.initialize(1024);
        KeyPair generateKeyPair = keyGen.genKeyPair();
        System.out.println("Key generated successfully");
        return generateKeyPair;
    }
}