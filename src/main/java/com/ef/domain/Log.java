package com.ef.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Ник on 06.11.2018.
 */
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
    private String status;
    private String userAgent;

    public Log(LocalDateTime date, String ip, String request, String status, String userAgent) {
        this.logDate = date;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
    }
}
