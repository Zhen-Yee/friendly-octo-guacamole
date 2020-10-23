package com.example.demo.validation;

import com.example.demo.exception.OffsetOutOfBoundException;
import com.example.demo.exception.SettingNotFoundException;
import com.example.demo.model.JukeBox;
import com.example.demo.model.Setting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JukeBoxValidator {

    public boolean validateJukeBoxHasComponents(JukeBox jukeBox, List<String> requiredComponents) {
        List<String> components = new ArrayList<>(requiredComponents);
        jukeBox.getComponents().stream().forEach(component -> {
            if (components.contains(component.getName())) {
                components.remove(component.getName());
            }
        });

        return components.isEmpty();
    }

    public void validateSettings(List<Setting> settingList) {
        log.info("Validating list of supported settings.");
        if (settingList.size() != 1) {
            throw new SettingNotFoundException();
        }
    }

    public void validateOffset(int offset, int limit, List<JukeBox> jukeBoxList) {
        log.info("Validating offset parameter.");
        int maxPages = new BigDecimal(jukeBoxList.size()).divide(new BigDecimal(limit), RoundingMode.UP).intValue();
        if (offset + 1 > maxPages) {
            throw new OffsetOutOfBoundException();
        }
    }

    public int validateLimit(Integer limit, int size) {
        log.info("Validating limit.");
        return Optional.ofNullable(limit).orElse(size);
    }

}
