package hello.springtx.apply;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy(){
        log.info("callservice  class ={}", callService.getClass());
    }

    @Test
    void externalCall(){
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV2TestConfig{

        @Bean
        InternalService internalService(){
            return new InternalService();
        }
        @Bean
        CallService callService(){
            return new CallService(internalService());
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService{

        private final InternalService internalService;
        
        public void external(){
            log.info("call external");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
            // boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            // log.info("tx readonly = {}", readOnly);
        }
    }

    @Slf4j
    static class InternalService{

        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
            // boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            // log.info("tx readonly = {}", readOnly);
        }
    }
}
