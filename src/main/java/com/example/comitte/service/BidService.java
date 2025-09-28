package com.example.comitte.service;

import com.example.comitte.model.dto.bid.BidItemDto;
import com.example.comitte.model.dto.bid.ComitteBidCreateDto;
import com.example.comitte.model.dto.bid.ComitteBidDto;
import com.example.comitte.model.entity.Bid;
import com.example.comitte.repository.BidRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public ComitteBidDto get(Long id) {
        return bidRepository.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<ComitteBidDto> getBidsByComitteId(Long comitteId) {
        return bidRepository.findByComitteId(comitteId)
                .stream()
                .map(this::toDto) // if using a mapper
                .toList();
    }

    @Transactional
    public ComitteBidDto create(ComitteBidCreateDto dto) {
        Bid b = Bid.builder().comitteId(dto.getComitteId()).comitteNumber(dto.getComitteNumber()).finalBidder(dto.getFinalBidder()).finalBidAmt(dto.getFinalBidAmt()).bidDate(dto.getBidDate()).build();
        try {
            b.setBids(mapper.writeValueAsString(dto.getBids()));
            b.setReceiversList(mapper.writeValueAsString(dto.getReceiversList()));
        } catch (Exception e) {
        }
        bidRepository.save(b);
        return toDto(b);
    }

    @Transactional
    public ComitteBidDto update(Long id, ComitteBidCreateDto dto) {
        Bid b = bidRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        b.setFinalBidAmt(dto.getFinalBidAmt());
        b.setFinalBidder(dto.getFinalBidder());
        try {
            b.setBids(mapper.writeValueAsString(dto.getBids()));
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
    public ComitteBidDto placeBid(Long id, BidItemDto bid) {
        Bid b = bidRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        try {
            List<BidItemDto> list = mapper.readValue(b.getBids() == null ? "[]" : b.getBids(), new TypeReference<List<BidItemDto>>() {
            });
            list.add(bid);
            b.setBids(mapper.writeValueAsString(list));
            bidRepository.save(b);
            return toDto(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ComitteBidDto toDto(Bid b) {
        ComitteBidDto d = new ComitteBidDto();
        d.setId(b.getBidId());
        d.setComitteId(b.getComitteId());
        d.setComitteNumber(b.getComitteNumber());
        d.setFinalBidder(b.getFinalBidder());
        d.setFinalBidAmt(b.getFinalBidAmt());
        d.setBidDate(b.getBidDate());
        try {
            d.setBids(mapper.readValue(b.getBids() == null ? "[]" : b.getBids(), new TypeReference<List<BidItemDto>>() {
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
