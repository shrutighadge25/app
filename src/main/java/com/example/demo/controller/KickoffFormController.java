package com.example.demo.controller;

import com.example.demo.model.KickoffForm;
import com.example.demo.service.KickoffFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/kickoff")
public class KickoffFormController {
    @Autowired
    private KickoffFormService service;

    @GetMapping("/next-kom-no")
    public String getNextKomNo() {
        List<KickoffForm> all = service.getAll();
        int currentYear = Year.now().getValue();
        int maxMiddle = -1;
        Pattern pattern = Pattern.compile("KOM-(\\d+)-" + currentYear);
        for (KickoffForm form : all) {
            if (form.getKomNo() != null) {
                Matcher m = pattern.matcher(form.getKomNo());
                if (m.matches()) {
                    int middle = Integer.parseInt(m.group(1));
                    if (middle > maxMiddle) maxMiddle = middle;
                }
            }
        }
        int nextMiddle = maxMiddle + 1;
        String middleStr = String.format("%02d", nextMiddle);
        return "KOM-" + middleStr + "-" + currentYear;
    }

    @PostMapping
    public KickoffForm save(@RequestBody KickoffForm form) {
        return service.save(form);
    }

    @GetMapping
    public List<KickoffForm> getAll() {
        return service.getAll();
    }
    @GetMapping("/members-present-options")
public List<String> getUniqueMembersPresent() {
    return service.getUniqueMembersPresent();
}
}
