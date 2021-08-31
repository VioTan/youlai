package com.youlai.auth.domain;

import com.core.common.util.CollectionUtils;
import com.youlai.admin.api.pojo.entity.SysUser;
import com.youlai.auth.common.enums.PasswordEncoderTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import static com.core.common.constant.GlobalConstants.STATUS_YES;

/**
 * @author:GSHG
 * @date: 2021-08-26 12:28
 * description:
 */
@Data
@NoArgsConstructor
public class OAuthUserDetails implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private Boolean enabled;

    private String clientId;

    public Collection<SimpleGrantedAuthority> authorities;

    /**
     * 重载构造方法，赋值OAuthUserDetails的属性
     * @param sysUser
     */
    public OAuthUserDetails(SysUser sysUser){
        this.setId(sysUser.getId());
        this.setUsername(sysUser.getUsername());
        this.setPassword(PasswordEncoderTypeEnum.BCRYPT.getPrefx() + sysUser.getPassword());
        this.setEnabled(STATUS_YES.equals(sysUser.getStatus()));
        if(CollectionUtils.isNotEmpty(sysUser.getRoles())){
            authorities = new ArrayList<>();
            sysUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        }

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
