package com.example.degree.Service;

import com.example.degree.Entity.Prefix;
import com.example.degree.Repository.PrefixeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrefixeService {

    @Autowired
    private PrefixeRepository prefixeRepository;

    public Prefix getPrefixFromName(String name){
        return prefixeRepository.getPrefixByName(name);
    }

    public Prefix savePrefix(Prefix prefix){
        return prefixeRepository.save(prefix);
    }
}
