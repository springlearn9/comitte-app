package com.ls.comitte.util;

import com.ls.comitte.model.entity.Bid;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.Member;
import com.ls.comitte.model.request.BidRequest;
import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.request.MemberRequest;

public class ServiceUtil {
    public static Member update(Member member, MemberRequest memberRequest) {
        if (memberRequest.getUsername() != null) {
            member.setUsername(memberRequest.getUsername());
        }
        if (memberRequest.getEmail() != null) {
            member.setEmail(memberRequest.getEmail());
        }
        if (memberRequest.getMobile() != null) {
            member.setMobile(memberRequest.getMobile());
        }
        if (memberRequest.getName() != null) {
            member.setName(memberRequest.getName());
        }

        if (memberRequest.getAadharNo() != null) {
            member.setAadharNo(memberRequest.getAadharNo());
        }
        if (memberRequest.getAddress() != null) {
            member.setAddress(memberRequest.getAddress());
        }
        if (memberRequest.getDob() != null) {
            member.setDob(memberRequest.getDob());
        }
        if (memberRequest.getPassword() != null) {
            member.setPassword(memberRequest.getPassword());
        }
        return member;
    }

    public static void update(Comitte comitte, ComitteRequest comitteRequest) {
        // Note: Owner is typically not updated after committee creation
        if (comitteRequest.getComitteName() != null) {
            comitte.setComitteName(comitteRequest.getComitteName());
        }
        if (comitteRequest.getStartDate() != null) {
            comitte.setStartDate(comitteRequest.getStartDate());
        }
        if (comitteRequest.getFullAmount() != null) {
            comitte.setFullAmount(comitteRequest.getFullAmount());
        }
        if (comitteRequest.getMembersCount() != null) {
            comitte.setMembersCount(comitteRequest.getMembersCount());
        }
        if (comitteRequest.getFullShare() != null) {
            comitte.setFullShare(comitteRequest.getFullShare());
        }
        if (comitteRequest.getDueDateDays() != null) {
            comitte.setDueDateDays(comitteRequest.getDueDateDays());
        }
        if (comitteRequest.getPaymentDateDays() != null) {
            comitte.setPaymentDateDays(comitteRequest.getPaymentDateDays());
        }
    }

    public static void update(Bid bid, BidRequest bidRequest) {
        // Note: Committee and final bidder relationships are handled in BidService
        // to ensure proper entity relationships are maintained
        // comitteNumber is auto-calculated, not from request

        // finalBidder relationship is handled in BidService, not here
        if (bidRequest.getFinalBidAmt() != null) {
            bid.setFinalBidAmt(bidRequest.getFinalBidAmt());
        }
        if (bidRequest.getBidDate() != null) {
            bid.setBidDate(bidRequest.getBidDate());
        }
        if (bidRequest.getReceiversList() != null) {
            bid.setReceiversList(bidRequest.getReceiversList());
        }
    }
}
