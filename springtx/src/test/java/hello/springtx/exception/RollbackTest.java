package hello.springtx.exception;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService rollbackService;

    @Test
    void runtimeException(){
        Assertions.assertThatThrownBy(()-> rollbackService.runtimeException())
        .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException(){
        Assertions.assertThatThrownBy(()-> rollbackService.checkedException())
        .isInstanceOf(MyException.class);
    }

    @Test
    void rollbackFor(){
        Assertions.assertThatThrownBy(()-> rollbackService.rollbackFor())
        .isInstanceOf(MyException.class);
    }
    
    @TestConfiguration
    static class RollbackTestConfig{
        @Bean
        RollbackService rollbackService(){
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService{

        @Transactional
        public void runtimeException(){
            log.info("call runtimeExcetion");
            throw new RuntimeException();
        }

        @Transactional
        public void checkedException() throws MyException{
            log.info("call checkedException");
            throw new MyException();
        }

        @Transactional(rollbackOn = MyException.class)
        public void rollbackFor() throws MyException{
            log.info("call rollbackFor");
            throw new MyException();
        }
    }

    static class MyException extends Exception{

        public MyException() {
        }

        public MyException(String message) {
            super(message);
        }

        public MyException(Throwable cause) {
            super(cause);
        }

        public MyException(String message, Throwable cause) {
            super(message, cause);
        }

        public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
        
    }
}
