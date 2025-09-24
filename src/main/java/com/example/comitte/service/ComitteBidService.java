package com.example.comitte.service;

import com.example.comitte.model.dto.bid.BidItemDto;
import com.example.comitte.model.dto.bid.ComitteBidCreateDto;
import com.example.comitte.model.dto.bid.ComitteBidDto;
import com.example.comitte.model.entity.Bid;
import com.example.comitte.repository.BidRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComitteBidService {
    private final BidRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public Page<ComitteBidDto> list(Pageable p) {
        var pg = repo.findAll(p);
        var dtos = pg.stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtos, p, pg.getTotalElements());
    }

    public ComitteBidDto get(Long id) {
        return repo.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public ComitteBidDto create(ComitteBidCreateDto dto) {
        Bid b = Bid.builder().comitteId(dto.getComitteId()).comitteNumber(dto.getComitteNumber()).finalBidder(dto.getFinalBidder()).finalBidAmt(dto.getFinalBidAmt()).bidDate(dto.getBidDate()).build();
        try {
            b.setBids(mapper.writeValueAsString(dto.getBids()));
            b.setReceiversList(mapper.writeValueAsString(dto.getReceiversList()));
        } catch (Exception e) {
        }
        repo.save(b);
        return toDto(b);
    }

    @Transactional
    public ComitteBidDto update(Long id, ComitteBidCreateDto dto) {
        Bid b = repo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        b.setFinalBidAmt(dto.getFinalBidAmt());
        b.setFinalBidder(dto.getFinalBidder());
        try {
            b.setBids(mapper.writeValueAsString(dto.getBids()));
            b.setReceiversList(mapper.writeValueAsString(dto.getReceiversList()));
        } catch (Exception e) {
        }
        repo.save(b);
        return toDto(b);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Transactional
    public ComitteBidDto placeBid(Long id, BidItemDto bid) {
        Bid b = repo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        try {
            List<BidItemDto> list = mapper.readValue(b.getBids() == null ? "[]" : b.getBids(), new TypeReference<List<BidItemDto>>() {
            });
            list.add(bid);
            b.setBids(mapper.writeValueAsString(list));
            repo.save(b);
            return toDto(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ComitteBidDto toDto(Bid b) {
        ComitteBidDto d = new ComitteBidDto();
        d.setId(b.getId());
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
