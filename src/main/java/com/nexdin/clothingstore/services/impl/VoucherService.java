package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.Vouchers;
import com.nexdin.clothingstore.domain.enums.EVoucherStatus;
import com.nexdin.clothingstore.domain.enums.EVoucherType;
import com.nexdin.clothingstore.payload.request.VoucherRequest;
import com.nexdin.clothingstore.repository.IVoucherRepository;
import com.nexdin.clothingstore.services.IVoucherService;
import com.nexdin.clothingstore.utils.FactoryEnum;
import com.nexdin.clothingstore.utils.IDGenerate;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VoucherService implements IVoucherService {
    @Autowired
    private IVoucherRepository voucherRepository;

    @Override
    public void existByCode(String code) {
        if (voucherRepository.existsByCode(code)) {
            log.info("[existByCode] - Voucher with code '{}' existed", code);
            throw new EntityExistsException("Voucher code " + code + " exist");
        }
    }

    @Override
    public Vouchers create(VoucherRequest request) {
        existByCode(request.getCode());

        Vouchers voucher = Vouchers.builder()
                .voucherID(IDGenerate.generate())
                .code(request.getCode())
                .description(request.getDescription())
                .voucherType(FactoryEnum.getEnumInstance(EVoucherType.class, request.getType()))
                .voucherValue(request.getVoucherValue())
                .minOrderValue(request.getMinOrderValue())
                .maxValueAmount(request.getMaxValueAmount())
                .startDate(LocalDateTime.parse(request.getStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .endDate(LocalDateTime.parse(request.getEndDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .usage_limit(request.getUsage_limit())
                .used_count(0)
                .voucherStatus(EVoucherStatus.ACTIVE)
                .build();

        voucherRepository.save(voucher);
        log.info("[create] - Created '{}' successfully", voucher.getVoucherID());

        return voucher;
    }

    @Override
    public Vouchers update(String voucherID, VoucherRequest request) {
        Vouchers oldVoucher = getByID(voucherID);

        oldVoucher.setCode(request.getCode());
        oldVoucher.setDescription(request.getDescription());
        oldVoucher.setVoucherType(FactoryEnum.getEnumInstance(EVoucherType.class, request.getType()));
        oldVoucher.setVoucherValue(request.getVoucherValue());
        oldVoucher.setMinOrderValue(request.getMinOrderValue());
        oldVoucher.setMaxValueAmount(request.getMaxValueAmount());
        oldVoucher.setStartDate(LocalDateTime.parse(request.getStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        oldVoucher.setEndDate(LocalDateTime.parse(request.getEndDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        oldVoucher.setUsage_limit(request.getUsage_limit());

        voucherRepository.save(oldVoucher);
        log.info("[update] - Updated '{}' successfully", voucherID);

        return oldVoucher;
    }

    @Override
    public void deleteByID(String voucherID) {
        Vouchers voucher = getByID(voucherID);
        voucherRepository.delete(voucher);
        log.info("[deleteByID] - Deleted '{}' successfully", voucherID);
    }

    @Override
    public void deleteByCode(String code) {
        Vouchers voucher = getByCode(code);
        log.info("[deleteByCode] - Deleted '{}' successfully", code);
    }

    @Override
    public void disableVoucher(String voucherID) {
        Vouchers voucher = getByID(voucherID);
        voucher.setVoucherStatus(EVoucherStatus.DISABLED);
        voucherRepository.save(voucher);
        log.info("[disableVoucher] - Disable '{}' successfully", voucherID);
    }

    @Override
    public Vouchers getByID(String voucherID) {
        Vouchers voucher = voucherRepository.findById(voucherID).orElseThrow(
                () -> {
                    log.warn("[getByID] - Not found voucher by ID: {}", voucherID);
                    return new EntityNotFoundException("Not found voucher by ID: " + voucherID);
                });
        log.info("[getByID] - Found voucher by ID: {}", voucherID);
        return voucher;
    }

    @Override
    public Vouchers getByCode(String code) {
        Vouchers voucher = voucherRepository.findByCode(code);
        if (voucher == null) {
            log.warn("[getByCode] - Not found voucher by code: {}", code);
            throw new EntityNotFoundException("Not found voucher by code: " + code);
        }
        return voucher;
    }

    @Override
    public List<Vouchers> getAll() {
        List<Vouchers> vouchers = voucherRepository.findAll();
        log.info("[getAll] - Retrieved {} vouchers", vouchers.size());
        return vouchers;
    }

    @Override
    public List<Vouchers> searchVoucher(String type, String voucherValue, String minOrderValue,
                                        String maxValueAmount, String startDate, String endDate,
                                        String usage_limit, String used_count, String status) {
        Specification<Vouchers> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("voucherType"), FactoryEnum.getEnumInstance(EVoucherType.class, type)));
            }
            if (isValidInt(voucherValue)) {
                predicates.add(criteriaBuilder.equal(root.get("voucherValue"), Integer.parseInt(voucherValue)));
            }
            if (isValidInt(minOrderValue)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("minOrderValue"), Integer.parseInt(minOrderValue)));
            }
            if (isValidInt(maxValueAmount)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxValueAmount"), Integer.parseInt(maxValueAmount)));
            }
            if (startDate != null && endDate != null) {
                try {
                    LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    predicates.add(criteriaBuilder.between(root.get("startDate"), start, end));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Use dd/MM/yyyy HH:mm:ss");
                }
            }
            if (isValidInt(usage_limit)) {
                predicates.add(criteriaBuilder.equal(root.get("usage_limit"), Integer.parseInt(usage_limit)));
            }
            if (isValidInt(used_count)) {
                predicates.add(criteriaBuilder.equal(root.get("used_count"), Integer.parseInt(used_count)));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("voucherStatus"), FactoryEnum.getEnumInstance(EVoucherStatus.class, status)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<Vouchers> vouchers = voucherRepository.findAll(spec);
        log.info("[searchVoucher] - Retrieved {} vouchers by query dynamic", vouchers.size());
        return vouchers;
    }

    @Override
    public Vouchers getAndLockByCode(String code) {
        Vouchers voucher = voucherRepository.findAndLockByCode(code);
        if (voucher == null) throw new EntityNotFoundException("Not found voucher by code: " + code);
        return voucher;
    }

    @Override
    public Integer applyVoucher(Vouchers voucher, int orderValue) {
        if (!isValidVoucher(voucher, orderValue)) return null;

        int discountAmount = 0;
        if (voucher.getVoucherType() == EVoucherType.FIXED) {
            discountAmount = voucher.getVoucherValue();
        } else if (voucher.getVoucherType() == EVoucherType.PERCENTAGE) {
            discountAmount = (orderValue * voucher.getVoucherValue()) / 100;
            discountAmount = Math.min(discountAmount, voucher.getMaxValueAmount());
        }

        voucher.setUsed_count(voucher.getUsed_count() + 1);
        voucherRepository.save(voucher);

        return discountAmount;
    }

    private boolean isValidVoucher(Vouchers vouchers, int orderValue) {
        LocalDateTime now = LocalDateTime.now();
        if (vouchers.getVoucherStatus() != EVoucherStatus.ACTIVE) return false;
        if (now.isBefore(vouchers.getStartDate()) || now.isAfter(vouchers.getEndDate())) return false;
        if (orderValue < vouchers.getMinOrderValue()) return false;
        if (vouchers.getUsed_count() >= vouchers.getUsage_limit()) {
            vouchers.setVoucherStatus(EVoucherStatus.DISABLED);
            voucherRepository.save(vouchers);
            return false;
        }
        return true;
    }

    private boolean isValidInt(String value) {
        if (value == null || value.isBlank()) return false;
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
