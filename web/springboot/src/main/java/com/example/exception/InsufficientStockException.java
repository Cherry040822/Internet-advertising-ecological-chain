package com.example.exception;

public class InsufficientStockException extends RuntimeException {
    private final String bookTitle;
    private final int remainingStock;

    public InsufficientStockException(String bookTitle, int remainingStock) {
        super("库存不足");
        this.bookTitle = bookTitle;
        this.remainingStock = remainingStock;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getRemainingStock() {
        return remainingStock;
    }
}
