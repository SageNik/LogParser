package com.ef.domain;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Log {

    private Long id;
    private LocalDateTime logDate;
    private String ip;
    private String request;
    private Integer status;
    private String userAgent;

    public Log(LocalDateTime date, String ip, String request, Integer status, String userAgent) {
        this.logDate = date;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
    }
}
