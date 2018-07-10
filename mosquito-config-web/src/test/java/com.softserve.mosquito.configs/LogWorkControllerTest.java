package com.softserve.mosquito.configs;

import com.softserve.mosquito.controllers.LogWorkController;
import com.softserve.mosquito.controllers.PriorityController;
import com.softserve.mosquito.dtos.LogWorkDto;
import com.softserve.mosquito.dtos.PriorityDto;
import com.softserve.mosquito.services.api.LogWorkService;
import com.softserve.mosquito.services.api.PriorityService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebAppConfig.class})
@WebAppConfiguration
public class LogWorkControllerTest {
    private static final Long UNKNOWN_ID = Long.MAX_VALUE;

    private MockMvc mockMvc;

    @Mock
    private LogWorkService logWorkServiceMock;

    @InjectMocks
    private LogWorkController logWorkController;

    public LogWorkControllerTest() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(logWorkController).build();
    }
    @Test
    public void createLogWork_success() throws Exception {
        LogWorkDto logWorkDto = new LogWorkDto();
        logWorkDto.
        when(priorityServiceMock.save(eq(priorityDto))).thenReturn(priorityDto);
        mockMvc.perform(
                post("/api/priorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(priorityDto)))
                .andExpect(status().isCreated());

        verify(priorityServiceMock, times(1)).save(any(PriorityDto.class));
        verifyNoMoreInteractions(priorityServiceMock);
    }
}
