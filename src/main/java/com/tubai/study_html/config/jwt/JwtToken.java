package com.tubai.study_html.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken implements AuthenticationToken {
    private String jwtToken;

    @Override
    public Object getPrincipal() {
        return jwtToken;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }
}
