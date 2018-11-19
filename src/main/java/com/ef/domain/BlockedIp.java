package com.ef.domain;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BlockedIp {

    private Long id;
    private String ip;
    private String reason;

    public BlockedIp(String ip, String reason) {
        this.ip = ip;
        this.reason = reason;
    }
}
