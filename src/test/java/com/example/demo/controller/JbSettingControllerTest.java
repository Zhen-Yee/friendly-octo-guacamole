package com.example.demo.controller;

import com.example.demo.model.JukeBox;
import com.example.demo.service.JbSettingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@WebMvcTest(JbSettingController.class)
public class JbSettingControllerTest {

    @Mock
    private JbSettingService jbSettingServiceMock;
    @InjectMocks
    private JbSettingController jbSettingController;
    @Autowired
    private MockMvc mockMvc;

    private static final String TEST_SETTING_ID = "testSettingID";

    @Test
    public void getSupportedJukeBoxesTest() {
        List<JukeBox> jukeBoxes = new ArrayList<>();
        ResponseEntity<List<JukeBox>> expectedResponse = new ResponseEntity<>(jukeBoxes, HttpStatus.OK);

        when(jbSettingServiceMock.getSupportedJukeboxes(TEST_SETTING_ID, null, 0, null)).thenReturn(jukeBoxes);

        ResponseEntity<List<JukeBox>> actualResponse = jbSettingController.getSupportedJukeBoxes(TEST_SETTING_ID, null, 0, null);

        verify(jbSettingServiceMock, times(1)).getSupportedJukeboxes(TEST_SETTING_ID, null, 0, null);
        assertEquals(expectedResponse, actualResponse);
    }

}
