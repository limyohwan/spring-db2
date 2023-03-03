package hello.springtx.apply;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
public class InitTxTest {
    
    @Autowired
    Hello hello;
    
    @Test
    void go(){

    }

    @TestConfiguration
    static class InitTxTestConfig {
        
        @Bean
        Hello hello(){
            return new Hello();
        }
    }

    @Slf4j
    static class Hello {

        @PostConstruct
        @Transactional
        public void initV1(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello initV1 PostConstruct active ={}", txActive);
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello initV2 ApplicationReadyEvent active ={}", txActive);
        }
    }
}
