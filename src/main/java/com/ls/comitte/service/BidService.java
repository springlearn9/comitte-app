package com.ls.comitte.service;

import com.ls.comitte.model.BidItem;
import com.ls.comitte.model.entity.Bid;
import com.ls.comitte.model.request.BidRequest;
import com.ls.comitte.model.response.BidResponse;
import com.ls.comitte.repository.BidRepository;
import com.ls.comitte.util.ServiceUtil;
import com.ls.comitte.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final ResponseMapper mapper = ResponseMapper.INSTANCE;
    private static final String BID_NOT_FOUND = "Bid not found";
    private final BidRepository bidRepository;

    public BidResponse get(Long id) {
        return bidRepository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException(BID_NOT_FOUND));
    }

    public List<BidResponse> getBidsByComitteId(Long comitteId) {
        return bidRepository.findByComitteId(comitteId).stream().map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public BidResponse create(BidRequest bidRequest) {
        Bid bid = mapper.toEntity(bidRequest);
        bidRepository.save(bid);
        return mapper.toResponse(bid);
    }

    @Transactional
    public BidResponse update(Long bidId, BidRequest bidRequest) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new RuntimeException(BID_NOT_FOUND));
        ServiceUtil.update(bid, bidRequest);
        bidRepository.save(bid);
        return mapper.toResponse(bid);
    }

    @Transactional
    public void delete(Long id) {
        bidRepository.deleteById(id);
    }

    @Transactional
    public BidResponse placeBid(Long id, BidItem bidItem) {
        Bid bid = bidRepository.findById(id).orElseThrow(() -> new RuntimeException(BID_NOT_FOUND));
        List<BidItem> bidItems = bid.getBidItems();
        if (CollectionUtils.isEmpty(bidItems)) {
            bidItems = new ArrayList<>();
        }
        bidItems.add(bidItem);
        bid.setBidItems(bidItems);

        bidRepository.save(bid);
        return mapper.toResponse(bid);

    }

}
