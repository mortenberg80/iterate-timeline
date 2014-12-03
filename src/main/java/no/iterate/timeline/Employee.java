package no.iterate.timeline;

import java.time.LocalDate;
import java.util.Optional;

public class Employee {
    public LocalDate startDate;
    public Optional<LocalDate> endDate;
    public String name;

    public String getName() {
		return name;
	}

	public Employee(LocalDate startDate, Optional<LocalDate> endDate,
            String name) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
    }
    
    public boolean isEmployedAt(LocalDate date) {
        boolean hasStarted = startDate.isBefore(date) || startDate.isEqual(date);
        boolean hasNotQuit = !endDate.isPresent() || endDate.get().isAfter(date);
        return hasStarted && hasNotQuit;
    }
}
