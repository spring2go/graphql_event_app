package com.spring2go.easyevent.type;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Bobo on 2022/2/19.
 */
@Data
@Accessors(chain = true)
public class AuthData {
    private Integer userId;
    private String token;
    private Integer tokenExpiration;
}
