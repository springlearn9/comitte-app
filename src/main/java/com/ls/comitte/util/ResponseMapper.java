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
    @Mapping(target = "monthlyShare", expression = "java(calculateMonthlyShare(bid))")
    BidResponse toResponse(Bid bid);
    
    // Map from relationships directly
    @Mapping(source = "comitte.comitteId", target = "comitteId")
    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "comitte.comitteName", target = "comitteName")
    @Mapping(source = "member.name", target = "memberName")
    @Mapping(source = "member.mobile", target = "memberMobile")
    ComitteMemberMapResponse toResponse(ComitteMemberMap comitteMemberMap);


    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "memberId", ignore = true)
    Member toEntity(MemberRequest memberRequest);

    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "comitteId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "bidsCount", ignore = true)
    Comitte toEntity(ComitteRequest comitteRequest);

    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "bidId", ignore = true)
    @Mapping(target = "comitte", ignore = true)
    @Mapping(target = "finalBidder", ignore = true)
    @Mapping(target = "comitteNumber", ignore = true)
    @Mapping(target = "monthlyShare", ignore = true)
    Bid toEntity(BidRequest bidRequest);

    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comitte", ignore = true)
    @Mapping(target = "member", ignore = true)
    ComitteMemberMap toEntity(ComitteMemberMapRequest comitteMemberMapRequest);

    /**
     * Calculates monthly share for a bid using the formula: (fullAmount - finalBidAmt) / membersCount
     * @param bid the bid entity containing comitte and finalBidAmt information
     * @return calculated monthly share amount
     */
    default Integer calculateMonthlyShare(Bid bid) {
        if (bid == null || bid.getComitte() == null || 
            bid.getFinalBidAmt() == null || 
            bid.getComitte().getFullAmount() == null || 
            bid.getComitte().getMembersCount() == null ||
            bid.getComitte().getMembersCount() == 0) {
            return null;
        }
        
        Integer fullAmount = bid.getComitte().getFullAmount();
        Integer finalBidAmt = bid.getFinalBidAmt();
        Integer membersCount = bid.getComitte().getMembersCount();
        
        return (fullAmount - finalBidAmt) / membersCount;
    }
}
