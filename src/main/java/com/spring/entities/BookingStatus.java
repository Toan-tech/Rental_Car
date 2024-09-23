package com.spring.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum BookingStatus {
    Confirmed,
    Pending_Deposit,
    In_Progress,
    Pending_Payment,
    Completed,
    CANCELLED
}
