package com.ls.auth.util;

import com.ls.auth.model.entity.Member;
import com.ls.auth.model.request.MemberRequest;
import com.ls.auth.model.response.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    MemberResponse toResponse(Member member);

    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "memberId", ignore = true)
    Member toEntity(MemberRequest memberRequest);
}