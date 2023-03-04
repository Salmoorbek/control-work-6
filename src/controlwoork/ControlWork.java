package controlwoork;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import server.BasicServer;
import server.ContentType;
import server.ResponseCodes;
import util.FileService;
import util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ControlWork extends BasicServer {
    private static Calendar calendar = new Calendar();
    private final static Configuration freemarker = initFreeMarker();

    public ControlWork(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this::homeIndex);
        registerGet("/calendar", this::calendarHandler);
        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);
        registerGet("/tasks", this::tasksHandler);
        registerGet("/task/add", this::addTask);
        registerPost("/task/add", this::addTaskPost);
        registerGet("/task", this::taskHandler);
        registerPost("/task", this::changeTask);
        registerGet("/task/delete", this::deleteTask);
    }

    private void homeIndex(HttpExchange exchange) {
        renderTemplate(exchange, "login.html", new Login(false));
    }

    private void loginPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        boolean check = false;
        var model = new UserModel();
        var users = model.getUsers();
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        for (User e : users) {
            if(e.getMail().equalsIgnoreCase(parsed.get("mail"))){
                if(e.getPassword().equalsIgnoreCase(parsed.get("password"))){
                    check = true;
                }
            }
        }
        if(check)
            redirect303(exchange, "/calendar");
        else
            renderTemplate(exchange, "login.html", new Login(true));
    }

    private void loginGet(HttpExchange exchange) {
        renderTemplate(exchange, "login.html", new Login(false));
    }

    private void deleteTask(HttpExchange exchange) {
        String query = getQueryParams(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(query, "&");
        for (int i = 0; i < calendar.getDays().size(); i++){
            if(parsed.get("date").equalsIgnoreCase(calendar.getDays().get(i).getDate().toString())){
                for (int j = 0; j < calendar.getDays().get(i).getTasks().size(); j++){
                    if (parsed.get("taskId").equals(calendar.getDays().get(i).getTasks().get(j).getTaskId())){
                        calendar.getDays().get(i).getTasks().remove(j);
                        break;
                    }
                }
                break;
            }
        }
        FileService.writeFile(calendar.getDays());
        redirect303(exchange, "/tasks?date=" + parsed.get("date"));
    }

    private void addTaskPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        if(parsed.get("name") != null && parsed.get("description") != null) {
            for (int i = 0; i < calendar.getDays().size(); i++){
                if(parsed.get("day").equals(calendar.getDays().get(i).getDate().toString())){
                    calendar.getDays().get(i).getTasks().add(new Task(parsed.get("taskId"), parsed.get("name"), parsed.get("description"), Arrays.stream(Type.values()).filter(e -> e.getName().equalsIgnoreCase(parsed.get("selected"))).findFirst().get()));
                    break;
                }
            }
            FileService.writeFile(calendar.getDays());
            redirect303(exchange, "/tasks?date=" + parsed.get("day"));
        }else {
            renderTemplate(exchange, "error.html", calendar);
        }
    }

    private void addTask(HttpExchange exchange) {
        Map<String,Object> data = new HashMap<>();
        String query = getQueryParams(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(query, "&");
        Task task = new Task();

            for (var day : calendar.getDays()) {
                if (parsed.get("date").equals(day.getDate().toString())) {
                    data.put("day", day);
                    data.put("task", task);
                    data.put("types", Type.values());
                    break;
                }
            }
            renderTemplate(exchange, "newTask.html", data);


    }

    private void changeTask(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        for (int i = 0; i < calendar.getDays().size(); i++){
            if(parsed.get("day").equalsIgnoreCase(calendar.getDays().get(i).getDate().toString())){
                for (int j = 0; j < calendar.getDays().get(i).getTasks().size(); j++){
                    if (parsed.get("taskId").equals(calendar.getDays().get(i).getTasks().get(j).getTaskId())){
                        calendar.getDays().get(i).getTasks().set(j, new Task(parsed.get("taskId"), parsed.get("name"), parsed.get("description"), Arrays.stream(Type.values()).filter(e -> e.getName().equalsIgnoreCase(parsed.get("selected"))).findFirst().get()));
                        break;
                    }
                }
                break;
            }
        }
        FileService.writeFile(calendar.getDays());
        redirect303(exchange, "/tasks?date=" + parsed.get("day"));
    }

    private void tasksHandler(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(queryParams, "&");
        Day currentDay = new Day();

            for (var day : calendar.getDays()) {
                if (parsed.get("date").equals(day.getDate().toString())) {
                    currentDay = day;
                }
            }
            renderTemplate(exchange, "tasks.html", currentDay);
    }

    private void taskHandler(HttpExchange exchange) {
        Map<String,Object> data = new HashMap<>();
        String query = getQueryParams(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(query, "&");
        Task task;
        for (var day: calendar.getDays()) {
            if(parsed.get("date").equalsIgnoreCase(day.getDate().toString())){
                for (int j = 0; j < day.getTasks().size(); j++){
                    if (parsed.get("taskId").equals(day.getTasks().get(j).getTaskId())){
                        task = day.getTasks().get(j);
                        data.put("day", day);
                        data.put("task", task);
                        data.put("types", Type.values());
                        break;
                    }
                }
                break;
            }
        }
        renderTemplate(exchange, "task.html", data);
    }

    private void calendarHandler(HttpExchange exchange) {
        renderTemplate(exchange, "calendar.html", new Calendar());
    }
    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
