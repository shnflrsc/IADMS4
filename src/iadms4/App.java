package iadms4;

import java.util.NoSuchElementException;

import iadms4.controller.AccountController;
import iadms4.controller.AuthController;
import iadms4.controller.CompanyRepController;
import iadms4.controller.CoordinatorController;
import iadms4.controller.InternController;
import iadms4.controller.StudentController;
import iadms4.model.CompanyRepresentative;
import iadms4.model.Coordinator;
import iadms4.model.Intern;
import iadms4.model.Person;
import iadms4.model.Student;
import iadms4.repository.AccountRepository;
import iadms4.repository.ApplicationRepository;
import iadms4.repository.CompanyRepository;
import iadms4.repository.DailyLogRepository;
import iadms4.repository.EvaluationRepository;
import iadms4.repository.OpeningRepository;
import iadms4.repository.PersonRepository;
import iadms4.repository.PositionRepository;
import iadms4.service.ApplicationService;
import iadms4.service.AuthService;
import iadms4.service.CompanyService;
import iadms4.service.EvaluationService;
import iadms4.service.InternService;
import iadms4.service.OpeningService;
import iadms4.service.PersonService;
import iadms4.service.ReportService;
import iadms4.view.AccountSettingsView;
import iadms4.view.AuthView;
import iadms4.view.CompanyRepDashboardView;
import iadms4.view.Console;
import iadms4.view.CoordinatorDashboardView;
import iadms4.view.InternDashboardView;
import iadms4.view.StudentDashboardView;

/*
 * Entry point of the program. It creates every object, connects them, and runs the main loop.
 *
 *   View  ->  Controller  ->  Service  ->  Repository  ->  Model
 *   (TUI)     (routes)        (rules)      (storage)       (data)
 *
 * A request from the user travels down these layers and the result travels back up.
 */
public class App {
    private final AuthController auth;
    private final AuthView authView;
    private final CoordinatorDashboardView coordinatorView;
    private final CompanyRepDashboardView repView;
    private final StudentDashboardView studentView;
    private final InternDashboardView internView;

    // Builds the application from the bottom layer up: repositories, services, controllers, then views.
    public App() {
        AccountRepository accountRepo = new AccountRepository();
        PersonRepository personRepo = new PersonRepository();
        CompanyRepository companyRepo = new CompanyRepository();
        PositionRepository positionRepo = new PositionRepository();
        OpeningRepository openingRepo = new OpeningRepository();
        ApplicationRepository applicationRepo = new ApplicationRepository();
        DailyLogRepository dailyLogRepo = new DailyLogRepository();
        EvaluationRepository evaluationRepo = new EvaluationRepository();

        AuthService authService = new AuthService(accountRepo);
        PersonService personService = new PersonService(personRepo, accountRepo, applicationRepo, evaluationRepo);
        CompanyService companyService = new CompanyService(companyRepo, personRepo, positionRepo);
        OpeningService openingService = new OpeningService(positionRepo, openingRepo, personRepo, applicationRepo);
        ApplicationService applicationService = new ApplicationService(applicationRepo, personRepo, personService);
        InternService internService = new InternService(personRepo, dailyLogRepo);
        EvaluationService evaluationService = new EvaluationService(evaluationRepo, personRepo);
        ReportService reportService = new ReportService(personRepo, companyRepo, openingRepo, applicationRepo,
                evaluationRepo);

        Console console = new Console();
        auth = new AuthController(authService, personService);
        authView = new AuthView(console, auth);
        AccountSettingsView settings = new AccountSettingsView(console, new AccountController(authService, personService));
        coordinatorView = new CoordinatorDashboardView(console,
                new CoordinatorController(personService, companyService, openingService, applicationService,
                        evaluationService, authService, reportService),
                settings);
        repView = new CompanyRepDashboardView(console,
                new CompanyRepController(openingService, applicationService, internService, evaluationService,
                        reportService),
                settings);
        studentView = new StudentDashboardView(console, new StudentController(applicationService, personService),
                settings);
        internView = new InternDashboardView(console, new InternController(internService, evaluationService),
                settings);
    }

    // Main loop: setup, then login, then the dashboard, then logout. It ends when the user exits at login.
    public void run() {
        while (true) {
            if (auth.needsSetup()) {
                authView.setup();
            } else if (!auth.isLoggedIn()) {
                if (!authView.login()) {
                    return;
                }
            } else {
                showDashboard(auth.currentPerson());
                auth.logout();
            }
        }
    }

    // Shows the dashboard for the type of the given person. Intern is checked before Student on purpose.
    private void showDashboard(Person person) {
        if (person instanceof Coordinator) {
            coordinatorView.show(person.getName());
        } else if (person instanceof CompanyRepresentative) {
            repView.show(person.getName());
        } else if (person instanceof Intern) {
            internView.show();
        } else if (person instanceof Student) {
            studentView.show();
        }
    }

    // Starts the program. If the input is closed (for example with Ctrl+D), it exits quietly.
    public static void main(String[] args) {
        try {
            new App().run();
        } catch (NoSuchElementException e) {
            // Nothing more can be read, so the program just ends.
        }
        System.out.println("Goodbye.");
    }
}
