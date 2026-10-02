package com.example.demo.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Member;
import com.example.demo.entity.iml.MemberMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DemoUserDetailService implements UserDetailsService {

    private final MemberMapper memberMapper;

    public DemoUserDetailService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberMapper.selectOne(
                new LambdaQueryWrapper<Member>().eq(Member::getUsername, username)
        );
        if (member == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (member.getStatus() != 1) {
            throw new UsernameNotFoundException("用户已被禁用");
        }
        return User.withUsername(member.getUsername())
                .password(member.getPassword())
                .roles("ADMIN")
                .build();
    }
}
