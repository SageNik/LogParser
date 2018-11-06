package com.log.parser.domain;

import lombok.*;

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
    private Date date;
    private String ip;
    private String request;
    private String status;
    private String userAgent;

    public Log(Date date, String ip, String request, String status, String userAgent) {
        this.date = date;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
    }
}
