package com.example.demo.entity.iml;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
