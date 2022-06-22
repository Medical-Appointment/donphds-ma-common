package com.donphds.ma.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.donphds.ma.common.constants.CommonConstants.PATTERN_DATETIME;

@Slf4j
@UtilityClass
public class CommonUtil {

  public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern(PATTERN_DATETIME);


  public static String getFileExtension(String filename) {
    String suffix = FileNameUtil.getSuffix(filename);
    return StringUtils.isBlank(suffix) ? suffix : "." + suffix;
  }

  public static String RSA2048Sign(String data, String privateKey) {
    try {
      KeyFactory rsa = KeyFactory.getInstance("RSA");
      InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(privateKey.getBytes()));
      StringWriter writer = new StringWriter();
      IoUtil.copy(reader, writer);
      byte[] privateKeyByte = writer.toString().getBytes();
      privateKeyByte = Base64.decode(privateKeyByte);
      PrivateKey key = rsa.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
      Signature signature = Signature.getInstance("SHA256WithRSA");
      signature.initSign(key);
      signature.update(data.getBytes());
      return new String(Base64.encode(signature.sign()));
    } catch (Exception e) {
      log.info("RSA2048签名失败");
      return "";
    }
  }

  /**
   * 不支持重复的字段名
   *
   * @param resource
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T toBean(Object resource, Class<T> clazz) {
    BeanCopier copier = BeanCopier.create(resource.getClass(), clazz, false);
    try {
      T t = clazz.getConstructor().newInstance();
      copier.copy(resource, t, null);
      return t;
    } catch (Exception e) {
      return null;
    }
  }

  public static Map<String, Object> bean2Map(Object resource) {
    return BeanMap.create(resource);
  }

  public static <T> T map2Bean(Map source, Class<T> clazz) {
    try {
      T t = clazz.getConstructor().newInstance();
      BeanMap beanMap = BeanMap.create(t);
      beanMap.putAll(source);
      return t;
    } catch (Exception e) {
      return null;
    }
  }

}
