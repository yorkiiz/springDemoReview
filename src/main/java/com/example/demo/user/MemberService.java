package com.example.demo.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.BusinessException;
import com.example.demo.dto.MemberVO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.Member;
import com.example.demo.entity.iml.MemberMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterDTO dto) {
        Long count = memberMapper.selectCount(
                new LambdaQueryWrapper<Member>().eq(Member::getUsername, dto.getUsername())
        );
        if (count > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        Member member = new Member();
        member.setUsername(dto.getUsername());
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        member.setNickname(dto.getNickname() != null ? dto.getNickname() : "");
        member.setPhone(dto.getPhone() != null ? dto.getPhone() : "");
        member.setRole("USER");
        member.setStatus(1);
        memberMapper.insert(member);
    }

    public MemberVO getUserInfo(String username) {
        Member member = memberMapper.selectOne(
                new LambdaQueryWrapper<Member>().eq(Member::getUsername, username)
        );
        if (member == null) {
            throw new BusinessException(404, "用户不存在");
        }

        MemberVO vo = new MemberVO();
        vo.setId(member.getId());
        vo.setUsername(member.getUsername());
        vo.setNickname(member.getNickname());
        vo.setPhone(member.getPhone());
        vo.setRole(member.getRole());
        vo.setCreateTime(member.getCreateTime());
        return vo;
    }
}