package com.elena.habitTracker.services;

import com.elena.habitTracker.areas.logs.entities.ApplicationLog;
import com.elena.habitTracker.areas.logs.models.view.ApplicationLogsPageViewModel;
import com.elena.habitTracker.areas.logs.repositories.LogRepository;
import com.elena.habitTracker.areas.logs.services.LogService;
import com.elena.habitTracker.areas.logs.services.LogServiceImpl;
import com.elena.habitTracker.areas.users.entities.User;
import com.elena.habitTracker.util.TestsUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@EnableSpringDataWebSupport
public class LogServiceTests {
    @Mock
    private LogRepository logRepository;

    private LogService logService;

    private User eli;

    private PageRequest pageable;

    @Before
    public void setUp() {
        logService = new LogServiceImpl(logRepository, new ModelMapper());

        this.eli = TestsUtils.createUserEli();

        pageable = PageRequest.of(1, 2);
    }

    @Test
    public void testGetAllByPage_givenValidLogs_shouldMapCorrectly() {
        //arrange
        LocalDateTime now = LocalDateTime.now();

        List<ApplicationLog> applicationLogs = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ApplicationLog applicationLog = TestsUtils.createApplicationLog(eli, now);
            applicationLogs.add(applicationLog);
        }

        when(this.logRepository.findAllByOrderByTimeDesc(pageable))
                .thenAnswer(a -> new PageImpl<ApplicationLog>(applicationLogs, pageable, applicationLogs.size()));

        //act
        ApplicationLogsPageViewModel pageViewModel = this.logService.getAllByPage(pageable);

        //assert
        Assert.assertNotNull("Page is null after creation", pageViewModel);
        Assert.assertNotNull("Logs in page are null after creation", pageViewModel.getLogs());

        for (int i = 0; i < pageViewModel.getLogs().getContent().size(); i++) {
            Assert.assertEquals("Messages in page differ", applicationLogs.get(i).getMessage(), pageViewModel.getLogs().getContent().get(i).getMessage());
            Assert.assertEquals("Times in page differ", applicationLogs.get(i).getTime(), pageViewModel.getLogs().getContent().get(i).getTime());
            Assert.assertEquals("Users in page differ", applicationLogs.get(i).getUser().toString(), pageViewModel.getLogs().getContent().get(i).getUser());
        }
    }
}

