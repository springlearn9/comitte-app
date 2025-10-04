package com.ls.comitte.service;

import com.ls.comitte.model.BidItem;
import com.ls.comitte.model.entity.Bid;
import com.ls.comitte.model.request.BidRequest;
import com.ls.comitte.model.response.BidResponse;
import com.ls.comitte.repository.BidRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public BidResponse get(Long id) {
        return bidRepository.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<BidResponse> getBidsByComitteId(Long comitteId) {
        return bidRepository.findByComitteId(comitteId).stream().map(this::toDto) // if using a mapper
                .toList();
    }

    @Transactional
    public BidResponse create(BidRequest dto) {
        Bid b = Bid.builder().comitteId(dto.getComitteId()).comitteNumber(dto.getComitteNumber()).finalBidder(dto.getFinalBidder()).finalBidAmt(dto.getFinalBidAmt()).bidDate(dto.getBidDate()).build();
        try {
            b.setBids(mapper.writeValueAsString(dto.getBidItems()));
            b.setReceiversList(mapper.writeValueAsString(dto.getReceiversList()));
        } catch (Exception e) {
        }
        bidRepository.save(b);
        return toDto(b);
    }

    @Transactional
    public BidResponse update(Long id, BidRequest dto) {
        Bid b = bidRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        b.setFinalBidAmt(dto.getFinalBidAmt());
        b.setFinalBidder(dto.getFinalBidder());
        try {
            b.setBids(mapper.writeValueAsString(dto.getBidItems()));
            b.setReceiversList(mapper.writeValueAsString(dto.getReceiversList()));
        } catch (Exception e) {
        }
        bidRepository.save(b);
        return toDto(b);
    }

    @Transactional
    public void delete(Long id) {
        bidRepository.deleteById(id);
    }

    @Transactional
    public BidResponse placeBid(Long id, BidItem bidItem) {
        Bid b = bidRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        try {
            List<BidItem> list = mapper.readValue(b.getBids() == null ? "[]" : b.getBids(), new TypeReference<List<BidItem>>() {
            });
            list.add(bidItem);
            b.setBids(mapper.writeValueAsString(list));
            bidRepository.save(b);
            return toDto(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BidResponse toDto(Bid b) {
        BidResponse d = new BidResponse();
        d.setId(b.getBidId());
        d.setComitteId(b.getComitteId());
        d.setComitteNumber(b.getComitteNumber());
        d.setFinalBidder(b.getFinalBidder());
        d.setFinalBidAmt(b.getFinalBidAmt());
        d.setBidDate(b.getBidDate());
        try {
            d.setBidItems(mapper.readValue(b.getBids() == null ? "[]" : b.getBids(), new TypeReference<List<BidItem>>() {
            }));
            d.setReceiversList(mapper.readValue(b.getReceiversList() == null ? "[]" : b.getReceiversList(), new TypeReference<List<Long>>() {
            }));
        } catch (Exception e) {
        }
        d.setCreatedTimestamp(b.getCreatedTimestamp());
        d.setUpdatedTimestamp(b.getUpdatedTimestamp());
        return d;
    }
}
