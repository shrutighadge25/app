package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.PurchaseOrder;
import com.example.demo.model.PurchaseOrderReport;
import com.example.demo.repository.PurchaseOrderRepository;
import com.example.demo.repository.PurchaseOrderReportRepository;

import java.util.List;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository repository;
    @Autowired
    private PurchaseOrderReportRepository reportRepository;

    public PurchaseOrder save(PurchaseOrder po) {
        PurchaseOrder saved = repository.save(po);
        // Save to report table as well
        PurchaseOrderReport report = new PurchaseOrderReport();
        report.setRecordNo(po.getRecordNo());
        report.setTimestamp(po.getTimestamp());
        report.setCcn(po.getCcn());
        report.setSalesPerson(po.getSalesPerson());
        report.setPoReferenceNumber(po.getPoReferenceNumber());
        report.setPoDate(po.getPoDate());
        report.setPoDeliveryDate(po.getPoDeliveryDate());
        report.setPoValueLacs(po.getPoValueLacs());
        report.setCustomerName(po.getCustomerName());
        report.setMachineName(po.getMachineName());
        report.setModelName(po.getModelName());
        report.setMachineCategory(po.getMachineCategory());
        report.setOrderType(po.getOrderType());
        report.setCountry(po.getCountry());
        report.setState(po.getState());
        report.setCity(po.getCity());
        report.setAddressLine1(po.getAddressLine1());
        report.setAddressLine2(po.getAddressLine2());
        reportRepository.save(report);
        return saved;
    }

    public List<PurchaseOrder> getAll() {
        return repository.findAll();
    }

    public boolean addNewValue(String field, String value) {
        if (value == null || value.trim().isEmpty()) return false;
        final String trimmedValue = value.trim();
        List<PurchaseOrder> all = getAll();
        boolean exists = false;
        switch (field) {
            case "salesPerson":
                exists = all.stream().anyMatch(po -> trimmedValue.equalsIgnoreCase(po.getSalesPerson()));
                break;
            case "customerName":
                exists = all.stream().anyMatch(po -> trimmedValue.equalsIgnoreCase(po.getCustomerName()));
                break;
           
            default:
                return false;
        }
        if (exists) return false;
        // Add a dummy PurchaseOrder with only the new value set for the field
        PurchaseOrder po = new PurchaseOrder();
        switch (field) {
            case "salesPerson": po.setSalesPerson(trimmedValue); break;
            case "customerName": po.setCustomerName(trimmedValue); break;
          
        }
        save(po);
        return true;
    }

    public List<PurchaseOrder> getReportsByMonth(int year, int month) {
        java.time.LocalDateTime start = java.time.LocalDateTime.of(year, month, 1, 0, 0);
        java.time.LocalDateTime end = start.plusMonths(1);
        return repository.findAll().stream()
            .filter(po -> po.getTimestamp() != null &&
                (po.getTimestamp().isEqual(start) || po.getTimestamp().isAfter(start)) &&
                po.getTimestamp().isBefore(end))
            .toList();
    }
}
