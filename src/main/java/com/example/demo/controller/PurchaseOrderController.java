package com.example.demo.controller;

import com.example.demo.model.PurchaseOrder;
import com.example.demo.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/po")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService service;


    @GetMapping("/next-record-no")
    public String getNextRecordNo() {
        List<PurchaseOrder> all = service.getAll();
        int currentYear = Year.now().getValue();
        int maxMiddle = -1;
        Pattern pattern = Pattern.compile("PO-(\\d+)-" + currentYear);
        for (PurchaseOrder po : all) {
            if (po.getRecordNo() != null) {
                Matcher m = pattern.matcher(po.getRecordNo());
                if (m.matches()) {
                    int middle = Integer.parseInt(m.group(1));
                    if (middle > maxMiddle) maxMiddle = middle;
                }
            }
        }
        int nextMiddle = maxMiddle + 1;
        String middleStr = String.format("%02d", nextMiddle);
        return "PO-" + middleStr + "-" + currentYear;
    }

    @PostMapping
    public PurchaseOrder save(@RequestBody PurchaseOrder po) {
        // Set timestamp to now if not provided
        if (po.getTimestamp() == null) {
            po.setTimestamp(LocalDateTime.now());
        }
        return service.save(po);
    }

    @GetMapping("/ccn-exists")
    public CCNCheckResponse checkCcnExists(@RequestParam String ccn) {
        List<PurchaseOrder> all = service.getAll();
        boolean exists = all.stream().anyMatch(po -> po.getCcn() != null && po.getCcn().equalsIgnoreCase(ccn));
        // Suggest similar CCNs (case-insensitive, contains input)
        List<String> suggestions = all.stream()
            .map(PurchaseOrder::getCcn)
            .filter(existingCcn -> existingCcn != null && !existingCcn.equalsIgnoreCase(ccn) && existingCcn.toLowerCase().contains(ccn.toLowerCase()))
            .distinct()
            .limit(5)
            .collect(Collectors.toList());
        return new CCNCheckResponse(exists, suggestions);
    }

    @GetMapping("/suggestions")
    public List<String> getSuggestions(@RequestParam String field, @RequestParam String q) {
        List<PurchaseOrder> all = service.getAll();
        Set<String> unique = new HashSet<>();
        switch (field) {
            case "salesPerson":
                all.forEach(po -> { if (po.getSalesPerson() != null) unique.add(po.getSalesPerson()); });
                break;
            case "customerName":
                all.forEach(po -> { if (po.getCustomerName() != null) unique.add(po.getCustomerName()); });
                break;
           
            default:
                return List.of();
        }
        return unique.stream()
            .filter(val -> val.toLowerCase().contains(q.toLowerCase()))
            .sorted()
            .limit(10)
            .collect(Collectors.toList());
    }

    @PostMapping("/add-new")
    public String addNewValue(@RequestParam String field, @RequestParam String value) {
        boolean added = service.addNewValue(field, value);
        if (added) return "OK";
        throw new RuntimeException("Value already exists or invalid field");
    }

    @GetMapping("/by-ccn")
    public PurchaseOrder getByCcn(@RequestParam String ccn) {
        return service.getAll().stream()
            .filter(po -> po.getCcn() != null && po.getCcn().equalsIgnoreCase(ccn))
            .findFirst()
            .orElse(null);
    }

    @GetMapping
    public List<PurchaseOrder> getAll() {
        return service.getAll();
    }

    @GetMapping("/monthly-report")
    public List<PurchaseOrder> getMonthlyReport(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {
        java.time.LocalDate now = java.time.LocalDate.now();
        int y = (year != null) ? year : now.getYear();
        int m = (month != null) ? month : now.getMonthValue();
        return service.getReportsByMonth(y, m);
    }
}

class CCNCheckResponse {
    public boolean exists;
    public List<String> suggestions;
    public CCNCheckResponse(boolean exists, List<String> suggestions) {
        this.exists = exists;
        this.suggestions = suggestions;
    }
}
