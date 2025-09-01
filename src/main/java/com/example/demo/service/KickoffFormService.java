package com.example.demo.service;

import com.example.demo.model.KickoffForm;
import com.example.demo.repository.KickoffFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class KickoffFormService {
    @Autowired
    private KickoffFormRepository repository;

    public KickoffForm save(KickoffForm form) {
        return repository.save(form);
    }

    public List<KickoffForm> getAll() {
        return repository.findAll();
    }

    public List<String> getUniqueMembersPresent() {
    List<KickoffForm> all = repository.findAll();
    Set<String> unique = new LinkedHashSet<>();
    for (KickoffForm form : all) {
        if (form.getMembersPresent() != null) {
            String[] arr = form.getMembersPresent().split(",");
            for (String s : arr) {
                String trimmed = s.trim();
                if (!trimmed.isEmpty()) unique.add(trimmed);
            }
        }
    }
    return new ArrayList<>(unique);
}
}
