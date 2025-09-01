package com.example.demo.controller;

import com.example.demo.model.PurchaseOrder;
import com.example.demo.model.KickoffForm;
import com.example.demo.model.Machine;
import com.example.demo.service.PurchaseOrderService;
import com.example.demo.service.KickoffFormService;
import com.example.demo.service.MachineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/machine")
public class MachineController {
    @Autowired
    private PurchaseOrderService poService;
    @Autowired
    private KickoffFormService kickoffService;

       @Autowired
    private MachineService machineService;

    @GetMapping("/by-ccn")
    public Map<String, Object> getByCcn(@RequestParam String ccn) {
        Map<String, Object> result = new HashMap<>();
        PurchaseOrder po = poService.getAll().stream()
            .filter(p -> p.getCcn() != null && p.getCcn().equalsIgnoreCase(ccn))
            .findFirst().orElse(null);
        KickoffForm kickoff = kickoffService.getAll().stream()
            .filter(k -> k.getCcn() != null && k.getCcn().equalsIgnoreCase(ccn))
            .findFirst().orElse(null);
        result.put("po", po);
        result.put("kickoff", kickoff);
        result.put("machineBasics", kickoff != null ? kickoff.getMachineBasics() : null);
        result.put("machineFeatures", kickoff != null ? kickoff.getMachineFeatures() : null);
        result.put("additionalOptions", kickoff != null ? kickoff.getAdditionalOptions() : null);
        return result;
    }

    @PostMapping("/submit")
    public Machine submitMachine(@RequestBody Machine machine) {
        // Find by CCN, update if exists, else create new
        Optional<Machine> existing = machineService.getByCcn(machine.getCcn());
        if (existing.isPresent()) {
            Machine old = existing.get();
            machine.setId(old.getId()); // preserve ID for update
        }
        return machineService.save(machine);
    }

    @GetMapping("/live-status-summary")
    public Map<String, Integer> getLiveStatusSummary() {
        String[] allStatuses = {
            "awaited cfat", "cfat done", "cfat in progress", "customer cp trials", "degisn", "for dispatch", "hold-awaiting material", "hold no plan", "ifat done", "ifat in progress", "post cfat point closure", "post ifat point closure ", "ppc", "ready", "wip", "wip kanban", "yet to plan", "in hand"
        };
        Map<String, Integer> summary = new HashMap<>();
        for (String status : allStatuses) summary.put(status, 0);
        for (Object[] row : machineService.getLiveStatusSummary()) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            if (status != null) summary.put(status, count.intValue());
        }
        return summary;
    }
}
