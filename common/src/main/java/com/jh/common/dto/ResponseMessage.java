package com.jh.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ResponseMessage {
    private Object data;
    private int statusCode;
    private String resultMessage;
    private String detailMessage;
}