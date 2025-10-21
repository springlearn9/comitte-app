package com.ls.comitte.util;

import com.ls.comitte.model.entity.Bid;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.ComitteMemberMap;
import com.ls.comitte.model.entity.Member;
import com.ls.comitte.model.request.BidRequest;
import com.ls.comitte.model.request.ComitteMemberMapRequest;
import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.request.MemberRequest;
import com.ls.comitte.model.response.BidResponse;
import com.ls.comitte.model.response.ComitteMemberMapResponse;
import com.ls.comitte.model.response.ComitteResponse;
import com.ls.comitte.model.response.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResponseMapper {
    ResponseMapper INSTANCE = Mappers.getMapper(ResponseMapper.class);

    MemberResponse toResponse(Member member);
    
    @Mapping(source = "owner.memberId", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    ComitteResponse toResponse(Comitte comitte);

    @Mapping(source = "comitte.comitteId", target = "comitteId")
    @Mapping(source = "comitte.comitteName", target = "comitteName")
    @Mapping(source = "finalBidder.memberId", target = "finalBidderId")
    @Mapping(source = "finalBidder.name", target = "finalBidderName")
    BidResponse toResponse(Bid bid);
    ComitteMemberMapResponse toResponse(ComitteMemberMap comitteMemberMap);


    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Member toEntity(MemberRequest memberRequest);

    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "comitteId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Comitte toEntity(ComitteRequest comitteRequest);

    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "bidId", ignore = true)
    @Mapping(target = "comitte", ignore = true)
    @Mapping(target = "finalBidder", ignore = true)
    Bid toEntity(BidRequest bidRequest);

    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "id", ignore = true)
    ComitteMemberMap toEntity(ComitteMemberMapRequest comitteMemberMapRequest);
}
