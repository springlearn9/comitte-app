package com.ls.construction.service;

import com.ls.construction.model.entity.PaymentDetails;
import com.ls.construction.model.entity.Project;
import com.ls.construction.model.request.PaymentDetailsRequest;
import com.ls.construction.model.response.PaymentDetailsResponse;
import com.ls.construction.repository.PaymentDetailsRepository;
import com.ls.construction.repository.ProjectRepository;
import com.ls.construction.util.ConstructionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentDetailsService {
    private final ConstructionMapper mapper = ConstructionMapper.INSTANCE;
    private static final String PAYMENT_NOT_FOUND = "Payment details not found";
    private static final String PROJECT_NOT_FOUND = "Project not found";

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final ProjectRepository projectRepository;

    public PaymentDetailsResponse get(Long paymentId) {
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PAYMENT_NOT_FOUND));
        return mapper.toResponse(paymentDetails);
    }

    public List<PaymentDetailsResponse> getAll() {
        return paymentDetailsRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<PaymentDetailsResponse> getByProject(Long projectId) {
        return paymentDetailsRepository.findByProjectProjectId(projectId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public PaymentDetailsResponse create(PaymentDetailsRequest paymentDetailsRequest) {
        PaymentDetails paymentDetails = mapper.toEntity(paymentDetailsRequest);
        
        if (paymentDetails.getAudit() == null) {
            paymentDetails.setAudit(new com.ls.common.model.AuditMetadata());
        }
        
        Project project = projectRepository.findById(paymentDetailsRequest.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND));
        paymentDetails.setProject(project);
        
        paymentDetailsRepository.save(paymentDetails);
        return mapper.toResponse(paymentDetails);
    }

    @Transactional
    public PaymentDetailsResponse update(Long paymentId, PaymentDetailsRequest paymentDetailsRequest) {
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PAYMENT_NOT_FOUND));
        
        paymentDetails.setPaymentDate(paymentDetailsRequest.getPaymentDate());
        paymentDetails.setDetails(paymentDetailsRequest.getDetails());
        paymentDetails.setPaymentType(paymentDetailsRequest.getPaymentType());
        paymentDetails.setReceiverDetails(paymentDetailsRequest.getReceiverDetails());
        paymentDetails.setTags(paymentDetailsRequest.getTags());
        
        paymentDetailsRepository.save(paymentDetails);
        return mapper.toResponse(paymentDetails);
    }

    @Transactional
    public void delete(Long paymentId) {
        if (!paymentDetailsRepository.existsById(paymentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PAYMENT_NOT_FOUND);
        }
        paymentDetailsRepository.deleteById(paymentId);
    }
}
