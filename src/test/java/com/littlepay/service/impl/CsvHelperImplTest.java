package com.littlepay.service.impl;

import com.littlepay.dto.Tap;
import com.littlepay.util.Constants;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CsvHelperImplTest {

    @InjectMocks
    private CsvHelperImpl csvHelper;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(csvHelper, "inputFilePath", "src/test/resources/input.csv");
    }

    @Test
    public void read_skipping_header() throws IOException {
        List<Tap> taps = csvHelper.readData();
        assertEquals(1, taps.size());
    }

    @Test
    public void read_data() throws IOException {
        List<Tap> taps = csvHelper.readData();
        assertEquals(1, taps.size());
        Tap tap = taps.get(0);
        assertEquals(Constants.TAP_TYPE.ON, tap.getTapType());
        assertEquals("Stop1", tap.getStopId());
        assertEquals("Company1", tap.getCompanyId());
        assertEquals("Bus37", tap.getBusId());
        assertEquals("5500005555555559", tap.getPan());
        assertEquals("22-01-2023 13:00:00", DateTimeFormat.forPattern("dd-MM-YYYY HH:mm:ss").print(tap.getDateTime()));
    }
}