package com.donphds.ma.common.config;

import com.donphds.ma.common.secret.KeyManager;
import com.donphds.ma.common.secret.KeyProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyAutoConfiguration {

  @Bean
  public KeyManager keyManager(KeyProvider keyProvider) {
    return new KeyManager(keyProvider);
  }

  @Bean
  public KeyProvider keyProvider() {
    return new KeyProvider();
  }
}
