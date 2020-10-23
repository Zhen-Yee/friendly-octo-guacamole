package com.example.demo.service;

import com.example.demo.model.JukeBox;
import com.example.demo.model.Setting;
import com.example.demo.model.Settings;
import com.example.demo.validation.JukeBoxValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JbSettingService {

    private JbApiService jbApiService;
    private SettingApiService settingApiService;
    private JukeBoxValidator jukeBoxValidator;

    @Autowired
    public JbSettingService(JbApiService jbApiService, SettingApiService settingApiService, JukeBoxValidator jukeBoxValidator) {
        this.jbApiService = jbApiService;
        this.settingApiService = settingApiService;
        this.jukeBoxValidator = jukeBoxValidator;
    }

    public List<JukeBox> getSupportedJukeboxes(String settingId, String model, int offset, Integer limit) {
        Settings listOfSettings = settingApiService.getSettings().getBody();
        JukeBox[] jukeBoxes = jbApiService.getJukes().getBody();
        List<String> requiredComponents = getComponents(settingId, listOfSettings);

        log.info("Validating supported components from jukebox.");
        List<JukeBox> supportedJukeBoxes = Arrays.stream(jukeBoxes)
                .filter(jukeBox -> jukeBoxValidator.validateJukeBoxHasComponents(jukeBox, requiredComponents))
                .collect(Collectors.toList());

        if (supportedJukeBoxes.isEmpty()) {
            return new ArrayList<>();
        }

        int validatedLimit = jukeBoxValidator.validateLimit(limit, supportedJukeBoxes.size());

        if (!StringUtils.isBlank(model)) {
            return getJukeBoxListOfModel(supportedJukeBoxes, model, validatedLimit, offset);
        }

        jukeBoxValidator.validateOffset(offset, validatedLimit, supportedJukeBoxes);

        return getPaginatedJukeBoxes(supportedJukeBoxes, offset, validatedLimit);

    }

    private List<JukeBox> getJukeBoxListOfModel(List<JukeBox> supportedJukeBoxes, String model, int limit, int offset) {
        log.info("Retrieving supported jukebox for model: {}", model);
        List<JukeBox> supportedJukeBoxesModel = supportedJukeBoxes.stream()
                .filter(jukeBox -> jukeBox.getModel().equals(model))
                .collect(Collectors.toList());

        if (supportedJukeBoxesModel.isEmpty()) {
            return new ArrayList<>();
        }

        jukeBoxValidator.validateOffset(offset, limit, supportedJukeBoxesModel);

        return getPaginatedJukeBoxes(supportedJukeBoxesModel, offset, limit);
    }

    private List<JukeBox> getPaginatedJukeBoxes(List<JukeBox> jukeBoxList, int offset, int limit) {
        log.info("Retrieving jukeboxes from page offset.");
        int startIndex = offset * limit;
        int endIndex = Math.min(jukeBoxList.size(), startIndex + limit);

        return jukeBoxList.subList(startIndex, endIndex);
    }

    private List<String> getComponents(String settingId, Settings listOfSettings) {
        List<Setting> settingList = listOfSettings.getSettings().stream()
                .filter(setting -> setting.getId().equals(settingId))
                .collect(Collectors.toList());

        jukeBoxValidator.validateSettings(settingList);
        return settingList.get(0).getRequires();
    }

}
