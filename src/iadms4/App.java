package iadms4;

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

/**
 * Entry point and composition root. HOW THE PROJECT FITS TOGETHER:
 *
 *   View  ->  Controller  ->  Service  ->  Repository  ->  Model
 *   (TUI)     (routes)        (rules)      (storage)       (data)
 *
 * A request from the user always travels down these layers and the result travels back up. We call
 * one such complete flow a "drill" (see the docs folder). The constructor below creates every
 * repository, passes them to the services, passes the services to the controllers, and passes the
 * controllers to the views (constructor injection, no framework). run() is the main loop:
 * setup if no accounts exist, otherwise login, then the dashboard that matches the logged-in person.
 */
public class App {
    private final AuthController auth;
    private final AuthView authView;
    private final CoordinatorDashboardView coordinatorView;
    private final CompanyRepDashboardView repView;
    private final StudentDashboardView studentView;
    private final InternDashboardView internView;

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

    /** Main loop: first-time setup -> login -> the dashboard of the logged-in person -> logout -> repeat. */
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

    /** Picks the dashboard from the TYPE of the logged-in person. Intern must be checked before Student. */
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

    public static void main(String[] args) {
        try {
            new App().run();
        } catch (Console.InputClosedException e) {
            // stdin closed: exit quietly
        }
        System.out.println("Goodbye.");
    }
}
