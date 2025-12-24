package com.example.ebank.util;

import com.example.ebank.config.AppProperties;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RibValidator {
    private final Pattern pattern;

    public RibValidator(AppProperties props) {
        this.pattern = Pattern.compile(props.getRib().getRegex());
    }

    public boolean isValid(String rib) {
        if (rib == null) return false;
        return pattern.matcher(rib.trim()).matches();
    }
}
