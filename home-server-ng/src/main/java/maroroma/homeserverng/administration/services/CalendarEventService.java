package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.administration.model.calendar.CalendarEvent;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalendarEventService {

    @InjectNanoRepository(
            file = @Property("homeserver.administrations.calendarevents.store"),
            persistedType = CalendarEvent.class
    )
    private NanoRepository calendarEventsRepo;

    public List<CalendarEvent> getActiveCalendarEvents() {
        return this.calendarEventsRepo.findAll(
                CalendarEvent::matches);
    }

    public List<CalendarEvent> getAllCalendarEvents() {
        return this.calendarEventsRepo.getAll();
    }

    public List<CalendarEvent> updateCalendarEvent(CalendarEvent calendarEventToUpdate) {
        return this.calendarEventsRepo.update(calendarEventToUpdate);
    }

    public List<CalendarEvent> addNewCalendarEvent(CalendarEvent calendarEvent) {
        calendarEvent.setId(UUID.randomUUID().toString());
        return this.calendarEventsRepo.save(calendarEvent);
    }

    public List<CalendarEvent> deleteCalendarEvent(String calendarEventId) {
        return this.calendarEventsRepo.delete(calendarEventId);
    }


}
