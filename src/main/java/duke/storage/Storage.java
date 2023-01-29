package duke.storage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import duke.task.*;

/**
 * Storage class to remember previous input and load them upon next run
 *
 * @author Pearl Twe
 * @version CS2103T AY22/23 Semester 2
 */
public class Storage {
    private File file;
    private String filePath;
    private String dataDirectory;

    /**
     * Constructor for Storage
     */
    public Storage() {
        this.dataDirectory = "./data/";
        this.filePath = this.dataDirectory + "duke.txt";
        this.file = new File(this.filePath);
        File directory = new File(this.dataDirectory);
        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Saves the user input into duke.txt file.
     * @param taskList list of tasks inputted by user.
     */
    public void save(TaskList taskList) {
        try {
            FileWriter writer = new FileWriter(this.filePath);
            for (Task task : taskList.getTaskList()) {
                writer.write(task.encode() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads previous taskList saved in duke.txt
     * @return List that was previously saved
     */
    public List<Task> load() {
        List<Task> list = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(this.file);
            while(scanner.hasNextLine()) {
                String task = scanner.nextLine();
                String[] encoded = task.split(" ");
                String taskType = encoded[0];
                String taskDescription = encoded[1];
                String isDone = encoded[2];

                switch (taskType) {
                    case "todo":
                        Task decodedTodo = new Todo(taskDescription);
                        list.add(decodedTodo);
                        if (isDone.equals("true")) {
                            decodedTodo.markDone();
                        }
                        break;
                    case "deadline":
                        LocalDate date = LocalDate.parse(encoded[3]);
                        Task decodedDeadline = new Deadline(taskDescription, date);
                        list.add(decodedDeadline);
                        if (isDone.equals("true")) {
                            decodedDeadline.markDone();
                        }
                        break;
                    case "event":
                        LocalDate from = LocalDate.parse(encoded[3]);
                        LocalDate to = LocalDate.parse(encoded[4]);
                        Task decodedEvent = new Event(taskDescription, from, to);
                        list.add(decodedEvent);
                        if (isDone.equals("true")) {
                            decodedEvent.markDone();
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
        return list;
    }

}
