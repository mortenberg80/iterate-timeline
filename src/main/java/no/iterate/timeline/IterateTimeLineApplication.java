package no.iterate.timeline;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IterateTimeLineApplication {

    private final List<Employee> allEmployees;
    private final LocalDate start;
    private final LocalDate end = LocalDate.now();
    private final List<LocalDate> timeLine;
	private final Map<LocalDate, List<Employee>> employeeTimeLine;

    public static void main(String[] args) throws Exception {
	    List<Employee> employees = loadEmployees();
	    
	    IterateTimeLineApplication app = new IterateTimeLineApplication(employees);
	    app.run();
	}

	public IterateTimeLineApplication(List<Employee> employees) {
        this.allEmployees = employees;
        start = employees.stream().min((a, b) -> {
            return a.startDate.isBefore(b.startDate) ? 0 : 1;
        }).orElseThrow(() -> new IllegalArgumentException()).startDate;
        timeLine = createTimeLine(start);

        employeeTimeLine = timeLine.stream().collect(
                Collectors.toMap(
                		Function.<LocalDate>identity(),
                        new Function<LocalDate, List<Employee>>() {
                        	public List<Employee> apply(LocalDate date) {
                        		return allEmployees.stream().filter(e -> e.isEmployedAt(date)).collect(Collectors.toList());
                        	}
                        }));
    }

    private List<LocalDate> createTimeLine(LocalDate start2) {
        List<LocalDate> result = new ArrayList<>();
        LocalDate currentDate = start;
        while ( currentDate.isBefore(end)) {
            result.add(currentDate);
            currentDate = currentDate.plusMonths(1);
        }
        return result;
    }

    private static List<Employee> loadEmployees() throws Exception {
    	Path employeesFile = Paths.get(ClassLoader.getSystemResource("employees").toURI());
        try (Stream<String> lines = Files.lines(employeesFile)) {
        	return lines.map(s -> {
        		String[] tokens = s.split(",");
        		Optional<LocalDate> endDate = tokens[2].isEmpty() ? Optional.empty() : Optional.of(LocalDate.parse(tokens[2]));
        		return new Employee(LocalDate.parse(tokens[1]), endDate, tokens[0]);
        	}).collect(Collectors.toList());
        }
	}

	private void run() {
        for (LocalDate localDate : timeLine) {
        	System.out.println(String.format("%s : %s", localDate, employeeTimeLine.get(localDate).stream().map(Employee::getName).collect(Collectors.toList())));
        	try {
        		Thread.sleep(100);
        	} catch (InterruptedException e) {
        	}
		}
    }
}
