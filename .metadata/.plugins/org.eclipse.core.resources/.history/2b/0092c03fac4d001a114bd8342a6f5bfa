package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import core.Aim;
import core.Diagram;
import core.Task;
import exception.FileReaderException;
import exception.Message;

public class CSVReader extends CsvIO {
	List<Task> tasks = new ArrayList<>();
	HashMap<String, Task> tasksByName = new HashMap<>();
	HashMap<String, List<String>> tasksInfo;

	public CSVReader(String filePath) throws IOException {
		super();

		tasksInfo = readFile(filePath);

		computeTaskList(tasksInfo);

		putTaskNext(tasksInfo);

	}

	public Diagram getDiagram() {
		return new Diagram(tasks);

	}

	private void putTaskNext(HashMap<String, List<String>> tasksInfo) {
		for (Task task : tasks) {

			List<String> nexts = tasksInfo.get(task.getName());
			for (String next : nexts) {
				Task nextTask = tasksByName.get(next);
				task.addNext(nextTask);
			}

		}
	}

	private void computeTaskList(HashMap<String, List<String>> tasksInfo) {
		for (Entry<String, List<String>> entry : tasksInfo.entrySet()) {
			String name = entry.getKey();

			Task task;
			if (entry.getValue().isEmpty()) {
				task = new Aim(name);
			} else {
				task = new Task(name);
			}
			tasks.add(task);
			tasksByName.put(name, task);
		}
	}

	private HashMap<String, List<String>> readFile(String filePath) throws IOException {
		HashMap<String, List<String>> value = new HashMap<>();
		try (BufferedReader fichierSource = new BufferedReader(new FileReader(filePath));) {
			String line;

			while ((line = fichierSource.readLine()) != null) {
				String[] tabChaine = line.split(COMMA_DELIMITER);
				String taskName = tabChaine[0].trim();
				if (taskName.equals("")) {
					throw new FileReaderException(Message.FILE_CONTENT_KO);
				}
				List<String> next = new ArrayList<>();
				for (int i = 1; i < tabChaine.length; i++) {
					next.add(tabChaine[i]);
				}
				value.put(tabChaine[0], next);
			}

		} catch (FileNotFoundException e) {
			throw new FileReaderException(Message.NO_FILE);
		}
		return value;
	}

	public String toString() {
		StringBuilder returnValue = new StringBuilder();
		for (Entry<String, List<String>> entry : tasksInfo.entrySet()) {
			returnValue.append((entry.getKey() + "/" + entry.getValue() + "\n"));
		}
		return returnValue.toString();
	}
}
