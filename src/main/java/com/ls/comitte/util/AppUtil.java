package com.ls.comitte.util;

import com.ls.comitte.model.entity.Bid;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.Member;
import com.ls.comitte.model.request.BidRequest;
import com.ls.comitte.model.request.ComitteRequest;
import com.ls.comitte.model.request.MemberRequest;

public class AppUtil {
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
        if (memberRequest.getPassword() != null) {
            member.setPassword(memberRequest.getPassword());
        }
        return member;
    }

    public static void update(Comitte comitte, ComitteRequest comitteRequest) {
        if (comitteRequest.getOwnerId() != null) {
            comitte.setOwnerId(comitteRequest.getOwnerId());
        }
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
        if (bidRequest.getComitteId() != null) {
            bid.setComitteId(bidRequest.getComitteId());
        }
        if (bidRequest.getComitteNumber() != null) {
            bid.setComitteNumber(bidRequest.getComitteNumber());
        }
        if (bidRequest.getFinalBidder() != null) {
            bid.setFinalBidder(bidRequest.getFinalBidder());
        }
        if (bidRequest.getFinalBidAmt() != null) {
            bid.setFinalBidAmt(bidRequest.getFinalBidAmt());
        }
        if (bidRequest.getBidDate() != null) {
            bid.setBidDate(bidRequest.getBidDate());
        }
        if (bidRequest.getBidItems() != null) {
            bid.setBidItems(bidRequest.getBidItems());
        }
        if (bidRequest.getReceiversList() != null) {
            bid.setReceiversList(bidRequest.getReceiversList());
        }
    }
}
