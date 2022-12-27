package net.breezeware.enumeration;

/**
 * Order Status Enum is used to Set and Update the Order Status
 */
public enum OrderStatus {

    PROCESS("process"), PLACED("Order Placed"), CANCEL("Cancelled"),

    RECEIVED("Received Order by Staff"), PREPARED("Order Prepared"), PENDING("Pending Delivery"),
    DELIVERED("Order Delivered");

    final String status;

    public String getStatus() {
        return status;
    }

    OrderStatus(String status) {
        this.status = status;
    }
}
