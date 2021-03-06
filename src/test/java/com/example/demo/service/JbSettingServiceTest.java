package com.example.demo.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.demo.model.Component;
import com.example.demo.model.JukeBox;
import com.example.demo.model.Setting;
import com.example.demo.model.Settings;
import com.example.demo.validation.JukeBoxValidator;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JbSettingServiceTest {

    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    private JbApiService jbApiServiceMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private SettingApiService settingApiServiceMock;
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    private JukeBoxValidator jukeBoxValidatorMock;
    @InjectMocks
    private JbSettingService jbSettingService;

    private Settings settings;
    private List<Setting> settingList;
    private List<String> requiredComponents;
    private final Logger logger = (Logger) LoggerFactory.getLogger(JbSettingService.class);
    private ListAppender<ILoggingEvent> listAppender;

    private static final String TEST_SETTING_ID = "testSettingId";
    private static final String TEST_MODEL_1 = "testModel1";
    private static final String TEST_MODEL_2 = "testModel2";


    private String componentName1 = "component1";
    private String componentName2 = "component2";
    private JukeBox jukeBox1;
    private JukeBox jukeBox2;
    private List<Component> componentsList;
    private Component c1;
    private Component c2;

    @Before
    public void setup() {
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        requiredComponents = Lists.list(componentName1, componentName2);

        Setting s1 = new Setting();
        s1.setId(TEST_SETTING_ID);
        s1.setRequires(requiredComponents);

        settingList = Lists.list(s1);

        settings = new Settings();
        settings.setSettings(settingList);

        when(settingApiServiceMock.getSettings().getBody()).thenReturn(settings);

        componentsList = new ArrayList<>();
        c1 = new Component();
        c2 = new Component();
        c1.setName(componentName1);
        c2.setName(componentName2);
        componentsList.addAll(Lists.list(c1, c2));

        jukeBox1 = new JukeBox();
        jukeBox1.setComponents(componentsList);
        jukeBox1.setModel(TEST_MODEL_1);

        jukeBox2 = new JukeBox();
        jukeBox2.setComponents(componentsList);
        jukeBox2.setModel(TEST_MODEL_2);

        when(jbApiServiceMock.getJukes().getBody()).thenReturn(new JukeBox[] {jukeBox1, jukeBox2});

        doNothing().when(jukeBoxValidatorMock).validateSettings(anyList());
    }

    @Test
    public void getSupportedJukeboxesTest_withNoModelParam() {
        when(jukeBoxValidatorMock.validateJukeBoxHasComponents(jukeBox1, requiredComponents)).thenReturn(true);
        when(jukeBoxValidatorMock.validateJukeBoxHasComponents(jukeBox2, requiredComponents)).thenReturn(true);
        when(jukeBoxValidatorMock.validateLimit(2,2)).thenReturn(2);
        doNothing().when(jukeBoxValidatorMock).validateOffset(eq(0), eq(2), anyList());

        List<JukeBox> actualList = jbSettingService.getSupportedJukeboxes(TEST_SETTING_ID, null, 0, 2);

        assertEquals(2, actualList.size());
        assertEquals(jukeBox1, actualList.get(0));
        assertEquals(jukeBox2, actualList.get(1));
        checkLog(2, listAppender.list.get(0), Level.INFO, "Validating supported components from jukebox.");
        checkLog(2, listAppender.list.get(1), Level.INFO, "Retrieving jukeboxes from page offset.");
    }

    @Test
    public void getSupportedJukeboxesTest_withNoJukeboxes() {
        when(jukeBoxValidatorMock.validateJukeBoxHasComponents(jukeBox1, requiredComponents)).thenReturn(false);
        when(jukeBoxValidatorMock.validateJukeBoxHasComponents(jukeBox2, requiredComponents)).thenReturn(false);

        List<JukeBox> actualList = jbSettingService.getSupportedJukeboxes(TEST_SETTING_ID, null, 0, 2);

        assertTrue(actualList.isEmpty());
        checkLog(1, listAppender.list.get(0), Level.INFO, "Validating supported components from jukebox.");
    }

    @Test
    public void getSupportedJukeboxesTest_withMatchingModel() {
        when(jukeBoxValidatorMock.validateJukeBoxHasComponents(jukeBox1, requiredComponents)).thenReturn(true);
        when(jukeBoxValidatorMock.validateJukeBoxHasComponents(jukeBox2, requiredComponents)).thenReturn(true);
        when(jukeBoxValidatorMock.validateLimit(2,2)).thenReturn(2);
        doNothing().when(jukeBoxValidatorMock).validateOffset(eq(0), eq(2), anyList());

        List<JukeBox> actualList = jbSettingService.getSupportedJukeboxes(TEST_SETTING_ID, TEST_MODEL_1, 0, 2);

        assertEquals(1, actualList.size());
        assertEquals(jukeBox1, actualList.get(0));
        checkLog(3, listAppender.list.get(0), Level.INFO, "Validating supported components from jukebox.");
        checkLog(3, listAppender.list.get(1), Level.INFO, "Retrieving supported jukebox for model: " + TEST_MODEL_1);
        checkLog(3, listAppender.list.get(2), Level.INFO, "Retrieving jukeboxes from page offset.");
    }

    @Test
    public void getSupportedJukeboxesTest_withNoMatchingModel() {
        when(jukeBoxValidatorMock.validateJukeBoxHasComponents(jukeBox1, requiredComponents)).thenReturn(true);
        when(jukeBoxValidatorMock.validateJukeBoxHasComponents(jukeBox2, requiredComponents)).thenReturn(true);
        when(jukeBoxValidatorMock.validateLimit(2,2)).thenReturn(2);

        String unsupportedModel = "No Model";

        List<JukeBox> actualList = jbSettingService.getSupportedJukeboxes(TEST_SETTING_ID, unsupportedModel, 0, 2);

        assertTrue(actualList.isEmpty());
        checkLog(2, listAppender.list.get(0), Level.INFO, "Validating supported components from jukebox.");
        checkLog(2, listAppender.list.get(1), Level.INFO, "Retrieving supported jukebox for model: " + unsupportedModel);

    }

    private void checkLog(final int listAppenderSize, final ILoggingEvent iLoggingEvent, final Level level, final String message) {
        assertEquals(listAppenderSize, listAppender.list.size());
        assertEquals(message, iLoggingEvent.getFormattedMessage());
        assertEquals(level, iLoggingEvent.getLevel());
    }

}
