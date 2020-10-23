package com.example.demo.validation;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.demo.exception.OffsetOutOfBoundException;
import com.example.demo.exception.SettingNotFoundException;
import com.example.demo.model.Component;
import com.example.demo.model.JukeBox;
import com.example.demo.model.Setting;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunWith(MockitoJUnitRunner.class)
public class JukeBoxValidatorTest {

    private JukeBoxValidator jukeBoxValidator;
    private String componentName1 = "component1";
    private String componentName2 = "component2";
    private List<String> requiredComponents;
    private JukeBox jukeBox;
    private List<Component> componentsList;
    private Component c1;
    private Component c2;

    private final Logger logger = (Logger) LoggerFactory.getLogger(JukeBoxValidator.class);
    private ListAppender<ILoggingEvent> listAppender;

    @Before
    public void setup() {
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        jukeBoxValidator = new JukeBoxValidator();
        jukeBox = new JukeBox();
        requiredComponents = Lists.list(componentName1, componentName2);
        componentsList = new ArrayList<>();
        c1 = new Component();
        c2 = new Component();
        c1.setName(componentName1);
        c2.setName(componentName2);
        componentsList.addAll(Lists.list(c1, c2));
        jukeBox.setComponents(componentsList);
    }

    @Test
    public void validateJukeBoxHasComponentsTest_withAllComponents() {
        assertTrue(jukeBoxValidator.validateJukeBoxHasComponents(jukeBox, requiredComponents));
    }

    @Test
    public void validateJukeBoxHasComponentsTest_withMissingComponents() {
        requiredComponents.add("missingComponent");
        assertFalse(jukeBoxValidator.validateJukeBoxHasComponents(jukeBox, requiredComponents));
    }

    @Test
    public void validateJukeBoxHasComponentsTest_jukeBoxHasExtraComponent() {
        Component c3 = new Component();
        c3.setName("New Component");
        componentsList.add(c3);
        jukeBox.setComponents(componentsList);

        assertTrue(jukeBoxValidator.validateJukeBoxHasComponents(jukeBox, requiredComponents));
    }

    @Test
    public void validateJukeBoxHasComponentsTest_emptyRequiredComponents() {
        assertTrue(jukeBoxValidator.validateJukeBoxHasComponents(jukeBox, new ArrayList<>()));
    }

    @Test
    public void validateSettings_containsOneSetting() {
        List<Setting> settingList = Lists.list(new Setting());
        assertDoesNotThrow(() -> jukeBoxValidator.validateSettings(settingList));
        checkLog(1, listAppender.list.get(0), Level.INFO, "Validating list of supported settings.");
    }

    @Test
    public void validateSettings_containsNoSetting() {
        Exception expectedException = new SettingNotFoundException();
        Exception actualException = assertThrows(SettingNotFoundException.class, () -> jukeBoxValidator.validateSettings(new ArrayList<>()));
        checkLog(1, listAppender.list.get(0), Level.INFO, "Validating list of supported settings.");

        assertEquals(expectedException.getCause(), actualException.getCause());
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    public void validateSettings_containsMoreThanOneSetting() {
        List<Setting> settingList = Lists.list(new Setting(), new Setting());

        Exception expectedException = new SettingNotFoundException();
        Exception actualException = assertThrows(SettingNotFoundException.class, () -> jukeBoxValidator.validateSettings(settingList));
        checkLog(1, listAppender.list.get(0), Level.INFO, "Validating list of supported settings.");

        assertEquals(expectedException.getCause(), actualException.getCause());
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    public void validateLimt_limitExist() {
        assertEquals(10, jukeBoxValidator.validateLimit(10, 1));
        checkLog(1, listAppender.list.get(0), Level.INFO, "Validating limit.");
    }

    @Test
    public void validateLimt_limitDoesNotExist() {
        assertEquals(1, jukeBoxValidator.validateLimit(null, 1));
        checkLog(1, listAppender.list.get(0), Level.INFO, "Validating limit.");
    }

    @Test
    public void validateOffset_withGoodOffsetAndLimit() {
        assertDoesNotThrow(() -> jukeBoxValidator.validateOffset(0, 1, Lists.list(new JukeBox())));
        checkLog(1, listAppender.list.get(0), Level.INFO, "Validating offset parameter.");
    }

    @Test
    public void validateOffset_offsetIsBiggerThanMaxPages() {
        Exception expectedException = new OffsetOutOfBoundException();
        Exception actualException = assertThrows(OffsetOutOfBoundException.class, () -> jukeBoxValidator.validateOffset(1, 1, Lists.list(new JukeBox())));
        checkLog(1, listAppender.list.get(0), Level.INFO, "Validating offset parameter.");

        assertEquals(expectedException.getCause(), actualException.getCause());
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    private void checkLog(final int listAppenderSize, final ILoggingEvent iLoggingEvent, final Level level, final String message) {
        assertEquals(listAppenderSize, listAppender.list.size());
        assertEquals(message, iLoggingEvent.getFormattedMessage());
        assertEquals(level, iLoggingEvent.getLevel());
    }

}
