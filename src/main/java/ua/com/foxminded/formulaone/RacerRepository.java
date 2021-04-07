package ua.com.foxminded.formulaone;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;
import java.util.stream.Stream;

public class RacerRepository {

	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd_HH:mm:ss.SSS";

	public List<Racer> getRacers(Stream<String> startLog, Stream<String> endLog, Stream<String> abbreviations) {
		Map<String, LocalDateTime> startTimes = startLog
				.collect(toMap(s -> s.substring(0, 3), s -> parseTimeDateFromString(s)));

		Map<String, LocalDateTime> endTimes = endLog
				.collect(toMap(s -> s.substring(0, 3), s -> parseTimeDateFromString(s)));

		return abbreviations.map(this::createRacer).peek(racer -> {
			LocalDateTime startTime = startTimes.get(racer.getAbbreviation());
			LocalDateTime endTime = endTimes.get(racer.getAbbreviation());
			racer.setBestLapTime(Duration.between(startTime, endTime));
		}).collect(toList());
	}

	public Racer createRacer(String string) {
		String[] params = string.split("_");
		return new Racer(params[0], params[1], params[2]);
	}

	private LocalDateTime parseTimeDateFromString(String dateTime) {
		return LocalDateTime.parse(dateTime.substring(3), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
	}
}
