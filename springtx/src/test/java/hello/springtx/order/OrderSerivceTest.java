package hello.springtx.order;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class OrderSerivceTest {

    @Autowired
    OrderSerivce orderSerivce;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void complete() throws NotEnoughMoneyException{
        Order order = new Order();
        order.setUsername("정상");;

        orderSerivce.order(order);

        Order findOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtimeException() throws NotEnoughMoneyException{
        Order order = new Order();
        order.setUsername("예외");;

        Assertions.assertThatThrownBy(()->orderSerivce.order(order))
        .isInstanceOf(RuntimeException.class);
        

        Optional<Order> findOrder = orderRepository.findById(order.getId());
        Assertions.assertThat(findOrder.isEmpty()).isTrue();
    }

    @Test
    void bizException() throws NotEnoughMoneyException{
        Order order = new Order();
        order.setUsername("잔고부족");;

        try {
            orderSerivce.order(order);
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내");
        }

        Order findOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertThat(findOrder.getPayStatus()).isEqualTo("대기");
    }
}
