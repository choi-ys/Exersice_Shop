package io.exercise.shop.exception;

/**
 * @author : choi-ys
 * @date : 2021/03/23 2:01 오후
 * @Content : 재고 부족 예외
 */
public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}