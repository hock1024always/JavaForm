package com.homework.zx_msgservice.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mailcarrier")
public class MailCarrier {
    private Integer MCid;
    private String host;
    private String Username;
    private String Password;
    private String encoding;
    private String port;
    private int SmtpState;
    private int StarttlsEnable;
}
